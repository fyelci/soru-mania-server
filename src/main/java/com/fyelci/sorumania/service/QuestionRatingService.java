package com.fyelci.sorumania.service;

import com.fyelci.sorumania.domain.Question;
import com.fyelci.sorumania.domain.QuestionRating;
import com.fyelci.sorumania.repository.QuestionRatingRepository;
import com.fyelci.sorumania.repository.QuestionRepository;
import com.fyelci.sorumania.util.SoruManiaUtil;
import com.fyelci.sorumania.web.rest.dto.QuestionRatingDTO;
import com.fyelci.sorumania.web.rest.mapper.QuestionRatingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * Service Implementation for managing QuestionRating.
 */
@Service
@Transactional
public class QuestionRatingService {

    private final Logger log = LoggerFactory.getLogger(QuestionRatingService.class);

    @Inject
    private QuestionRatingRepository questionRatingRepository;

    @Inject
    private QuestionRatingMapper questionRatingMapper;


    @Inject
    private QuestionRepository questionRepository;

    /**
     * Save a questionRating.
     * @return the persisted entity
     */
    public QuestionRatingDTO save(QuestionRatingDTO questionRatingDTO) {
        log.debug("Request to save QuestionRating : {}", questionRatingDTO);

        QuestionRating questionRating = questionRatingRepository.getUsersRate(questionRatingDTO.getQuestionId());

        if(questionRating == null) {
            questionRating = questionRatingMapper.questionRatingDTOToQuestionRating(questionRatingDTO);
            questionRating.setCreateDate(ZonedDateTime.now());
        } else {
            questionRating.setRate(questionRatingDTO.getRate());
            questionRating.setLastModifiedDate(ZonedDateTime.now());
        }

        questionRating = questionRatingRepository.save(questionRating);

        //Oylamayı yaptıktan sonra sorunun üzerindeki bilgileri de güncelle
        Object[] firstResult = questionRatingRepository.calculateRatingAvg(questionRating.getQuestion().getId());
        Object[] rateResult  = (Object[])firstResult[0];
        long totalRate = ((Long) rateResult[0]).longValue();
        long rateCount = ((Long) rateResult[1]).longValue();
        double avg = SoruManiaUtil.round((double) totalRate / (double) rateCount, 1);

        //Save Question
        Question questionToSave = questionRepository.findOne(questionRating.getQuestion().getId());
        questionToSave.setRateAvg(new BigDecimal(avg));
        questionToSave.setRateCount(new Long(rateCount).intValue());
        questionRepository.save(questionToSave);


        QuestionRatingDTO result = questionRatingMapper.questionRatingToQuestionRatingDTO(questionRating);
        result.setRateAvg(new BigDecimal(avg));
        result.setRateCount(new Long(rateCount).intValue());
        return result;
    }

    /**
     *  get all the questionRatings.
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<QuestionRating> findAll(Pageable pageable) {
        log.debug("Request to get all QuestionRatings");
        Page<QuestionRating> result = questionRatingRepository.findAll(pageable);
        return result;
    }

    /**
     *  get one questionRating by id.
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public QuestionRatingDTO findOne(Long id) {
        log.debug("Request to get QuestionRating : {}", id);
        QuestionRating questionRating = questionRatingRepository.findOne(id);
        QuestionRatingDTO questionRatingDTO = questionRatingMapper.questionRatingToQuestionRatingDTO(questionRating);
        return questionRatingDTO;
    }

    @Transactional(readOnly = true)
    public Integer getUserQuestionRating(Long questionId) {
        log.debug("Request to get QuestionRating by question id: {}", questionId);
        QuestionRating questionRating = questionRatingRepository.getUsersRate(questionId);
        QuestionRatingDTO questionRatingDTO = questionRatingMapper.questionRatingToQuestionRatingDTO(questionRating);
        if(questionRatingDTO == null) {
            return null;
        } else {
            return questionRatingDTO.getRate();
        }

    }

    /**
     *  delete the  questionRating by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete QuestionRating : {}", id);
        questionRatingRepository.delete(id);
    }
}
