package com.fyelci.sorumania.web.rest;

import com.fyelci.sorumania.Application;
import com.fyelci.sorumania.domain.Comment;
import com.fyelci.sorumania.repository.CommentRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the CommentResource REST controller.
 *
 * @see CommentResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CommentResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));

    private static final String DEFAULT_TEXT = "AAAAA";
    private static final String UPDATED_TEXT = "BBBBB";
    private static final String DEFAULT_MEDIA_URL = "AAAAA";
    private static final String UPDATED_MEDIA_URL = "BBBBB";

    private static final ZonedDateTime DEFAULT_CREATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATE_DATE_STR = dateTimeFormatter.format(DEFAULT_CREATE_DATE);

    private static final ZonedDateTime DEFAULT_LAST_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_LAST_MODIFIED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_LAST_MODIFIED_DATE_STR = dateTimeFormatter.format(DEFAULT_LAST_MODIFIED_DATE);

    private static final Long DEFAULT_PARENT_ID = 1L;
    private static final Long UPDATED_PARENT_ID = 2L;

    @Inject
    private CommentRepository commentRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCommentMockMvc;

    private Comment comment;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CommentResource commentResource = new CommentResource();
        ReflectionTestUtils.setField(commentResource, "commentRepository", commentRepository);
        this.restCommentMockMvc = MockMvcBuilders.standaloneSetup(commentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        comment = new Comment();
        comment.setText(DEFAULT_TEXT);
        comment.setMediaUrl(DEFAULT_MEDIA_URL);
        comment.setCreateDate(DEFAULT_CREATE_DATE);
        comment.setLastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        comment.setParentId(DEFAULT_PARENT_ID);
    }

    @Test
    @Transactional
    public void createComment() throws Exception {
        int databaseSizeBeforeCreate = commentRepository.findAll().size();

        // Create the Comment

        restCommentMockMvc.perform(post("/api/comments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(comment)))
                .andExpect(status().isCreated());

        // Validate the Comment in the database
        List<Comment> comments = commentRepository.findAll();
        assertThat(comments).hasSize(databaseSizeBeforeCreate + 1);
        Comment testComment = comments.get(comments.size() - 1);
        assertThat(testComment.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testComment.getMediaUrl()).isEqualTo(DEFAULT_MEDIA_URL);
        assertThat(testComment.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testComment.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testComment.getParentId()).isEqualTo(DEFAULT_PARENT_ID);
    }

    @Test
    @Transactional
    public void getAllComments() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(comment);

        // Get all the comments
        restCommentMockMvc.perform(get("/api/comments?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(comment.getId().intValue())))
                .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())))
                .andExpect(jsonPath("$.[*].mediaUrl").value(hasItem(DEFAULT_MEDIA_URL.toString())))
                .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE_STR)))
                .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE_STR)))
                .andExpect(jsonPath("$.[*].parentId").value(hasItem(DEFAULT_PARENT_ID.intValue())));
    }

    @Test
    @Transactional
    public void getComment() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(comment);

        // Get the comment
        restCommentMockMvc.perform(get("/api/comments/{id}", comment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(comment.getId().intValue()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()))
            .andExpect(jsonPath("$.mediaUrl").value(DEFAULT_MEDIA_URL.toString()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE_STR))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE_STR))
            .andExpect(jsonPath("$.parentId").value(DEFAULT_PARENT_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingComment() throws Exception {
        // Get the comment
        restCommentMockMvc.perform(get("/api/comments/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateComment() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(comment);

		int databaseSizeBeforeUpdate = commentRepository.findAll().size();

        // Update the comment
        comment.setText(UPDATED_TEXT);
        comment.setMediaUrl(UPDATED_MEDIA_URL);
        comment.setCreateDate(UPDATED_CREATE_DATE);
        comment.setLastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        comment.setParentId(UPDATED_PARENT_ID);

        restCommentMockMvc.perform(put("/api/comments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(comment)))
                .andExpect(status().isOk());

        // Validate the Comment in the database
        List<Comment> comments = commentRepository.findAll();
        assertThat(comments).hasSize(databaseSizeBeforeUpdate);
        Comment testComment = comments.get(comments.size() - 1);
        assertThat(testComment.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testComment.getMediaUrl()).isEqualTo(UPDATED_MEDIA_URL);
        assertThat(testComment.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testComment.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testComment.getParentId()).isEqualTo(UPDATED_PARENT_ID);
    }

    @Test
    @Transactional
    public void deleteComment() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(comment);

		int databaseSizeBeforeDelete = commentRepository.findAll().size();

        // Get the comment
        restCommentMockMvc.perform(delete("/api/comments/{id}", comment.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Comment> comments = commentRepository.findAll();
        assertThat(comments).hasSize(databaseSizeBeforeDelete - 1);
    }
}
