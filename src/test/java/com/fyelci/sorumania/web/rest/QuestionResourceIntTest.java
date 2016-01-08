package com.fyelci.sorumania.web.rest;

import com.fyelci.sorumania.Application;
import com.fyelci.sorumania.domain.Authority;
import com.fyelci.sorumania.domain.Question;
import com.fyelci.sorumania.domain.User;
import com.fyelci.sorumania.repository.LovRepository;
import com.fyelci.sorumania.repository.QuestionRepository;
import com.fyelci.sorumania.repository.UserRepository;
import com.fyelci.sorumania.security.AuthoritiesConstants;
import com.fyelci.sorumania.security.SecurityUtils;
import com.fyelci.sorumania.service.QuestionService;
import com.fyelci.sorumania.web.rest.dto.QuestionDTO;
import com.fyelci.sorumania.web.rest.mapper.QuestionMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.mail.search.SearchTerm;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the QuestionResource REST controller.
 *
 * @see QuestionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class QuestionResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));

    private static final String DEFAULT_DETAIL = "AAAAA";
    private static final String UPDATED_DETAIL = "BBBBB";
    private static final String DEFAULT_MEDIA_URL = "AAAAA";
    private static final String UPDATED_MEDIA_URL = "BBBBB";

    private static final Integer DEFAULT_COMMENT_COUNT = 0;
    private static final Integer UPDATED_COMMENT_COUNT = 2;

    private static final ZonedDateTime DEFAULT_CREATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATE_DATE_STR = dateTimeFormatter.format(DEFAULT_CREATE_DATE);

    private static final ZonedDateTime DEFAULT_LAST_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_LAST_MODIFIED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_LAST_MODIFIED_DATE_STR = dateTimeFormatter.format(DEFAULT_LAST_MODIFIED_DATE);

    private static final Long DEFAULT_USER_ID = 3L;
    private static final Long DEFAULT_CATEGORY_ID = 3L;
    private static final Long DEFAULT_LESSON_ID = 101L;

    @Inject
    private QuestionRepository questionRepository;

    @Inject
    private QuestionService questionService;

    @Inject
    private QuestionMapper questionMapper;

    @Inject
    private UserRepository userRepository;

    @Inject
    private LovRepository lovRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restQuestionMockMvc;

    private Question question;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        QuestionResource questionResource = new QuestionResource();
        ReflectionTestUtils.setField(questionResource, "questionRepository", questionRepository);
        ReflectionTestUtils.setField(questionResource, "questionMapper", questionMapper);
        ReflectionTestUtils.setField(questionResource, "questionService", questionService);
        this.restQuestionMockMvc = MockMvcBuilders.standaloneSetup(questionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();

        User repoUser = userRepository.findOne(DEFAULT_USER_ID);

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        SimpleGrantedAuthority t = new SimpleGrantedAuthority(AuthoritiesConstants.ADMIN);
        grantedAuthorities.add(t);

        org.springframework.security.core.userdetails.User user =
            new org.springframework.security.core.userdetails.User(repoUser.getLogin(),
                repoUser.getPassword(),
                grantedAuthorities
            );

        Authentication authentication = Mockito.mock(Authentication.class);
        // Mockito.whens() for your authorization object
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(securityContext.getAuthentication().getPrincipal()).thenReturn(user);
        SecurityContextHolder.setContext(securityContext);
    }

    @Before
    public void initTest() {
        question = new Question();
        question.setDetail(DEFAULT_DETAIL);
        question.setMediaUrl(DEFAULT_MEDIA_URL);
        question.setCommentCount(DEFAULT_COMMENT_COUNT);
        question.setCreateDate(DEFAULT_CREATE_DATE);
        question.setLastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        question.setUser(userRepository.findOne(DEFAULT_USER_ID));
        question.setCategory(lovRepository.findOne(DEFAULT_CATEGORY_ID));
        question.setLesson(lovRepository.findOne(DEFAULT_LESSON_ID));
    }

    @Test
    @Transactional
    public void createQuestion() throws Exception {
        int databaseSizeBeforeCreate = questionRepository.findAll().size();

        // Create the Question
        QuestionDTO questionDTO = questionMapper.questionToQuestionDTO(question);

        restQuestionMockMvc.perform(post("/api/questions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(questionDTO)))
                .andExpect(status().isCreated());

        // Validate the Question in the database
        List<Question> questions = questionRepository.findAll();
        assertThat(questions).hasSize(databaseSizeBeforeCreate + 1);
        Question testQuestion = questions.get(questions.size() - 1);
        assertThat(testQuestion.getDetail()).isEqualTo(DEFAULT_DETAIL);
        assertThat(testQuestion.getMediaUrl()).isEqualTo(DEFAULT_MEDIA_URL);
        assertThat(testQuestion.getCommentCount()).isEqualTo(DEFAULT_COMMENT_COUNT);
        assertThat(testQuestion.getCreateDate()).isNotNull();
    }

    @Test
    @Transactional
    public void getAllQuestions() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questions
        restQuestionMockMvc.perform(get("/api/questions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(question.getId().intValue())))
                .andExpect(jsonPath("$.[*].detail").value(hasItem(DEFAULT_DETAIL.toString())))
                .andExpect(jsonPath("$.[*].mediaUrl").value(hasItem(DEFAULT_MEDIA_URL.toString())))
                .andExpect(jsonPath("$.[*].commentCount").value(hasItem(DEFAULT_COMMENT_COUNT)))
                .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE_STR)))
                .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE_STR)));
    }

    @Test
    @Transactional
    public void getQuestion() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get the question
        restQuestionMockMvc.perform(get("/api/questions/{id}", question.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(question.getId().intValue()))
            .andExpect(jsonPath("$.detail").value(DEFAULT_DETAIL.toString()))
            .andExpect(jsonPath("$.mediaUrl").value(DEFAULT_MEDIA_URL.toString()))
            .andExpect(jsonPath("$.commentCount").value(DEFAULT_COMMENT_COUNT))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE_STR))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingQuestion() throws Exception {
        // Get the question
        restQuestionMockMvc.perform(get("/api/questions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuestion() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

		int databaseSizeBeforeUpdate = questionRepository.findAll().size();

        // Update the question
        question.setDetail(UPDATED_DETAIL);
        question.setMediaUrl(UPDATED_MEDIA_URL);
        question.setCommentCount(UPDATED_COMMENT_COUNT);
        question.setCreateDate(UPDATED_CREATE_DATE);
        question.setLastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        QuestionDTO questionDTO = questionMapper.questionToQuestionDTO(question);

        restQuestionMockMvc.perform(put("/api/questions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(questionDTO)))
                .andExpect(status().isOk());

        // Validate the Question in the database
        List<Question> questions = questionRepository.findAll();
        assertThat(questions).hasSize(databaseSizeBeforeUpdate);
        Question testQuestion = questions.get(questions.size() - 1);
        assertThat(testQuestion.getDetail()).isEqualTo(UPDATED_DETAIL);
        assertThat(testQuestion.getMediaUrl()).isEqualTo(UPDATED_MEDIA_URL);
        assertThat(testQuestion.getCommentCount()).isEqualTo(UPDATED_COMMENT_COUNT);
        assertThat(testQuestion.getCreateDate()).isNotNull();
        assertThat(testQuestion.getLastModifiedDate()).isNotNull();
    }

    @Test
    @Transactional
    public void deleteQuestion() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

		int databaseSizeBeforeDelete = questionRepository.findAll().size();

        // Get the question
        restQuestionMockMvc.perform(delete("/api/questions/{id}", question.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Question> questions = questionRepository.findAll();
        assertThat(questions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
