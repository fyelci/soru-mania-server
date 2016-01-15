package com.fyelci.sorumania.service;

import com.fyelci.sorumania.config.Constants;
import com.fyelci.sorumania.domain.Question;
import com.fyelci.sorumania.domain.ReportedContent;
import com.fyelci.sorumania.repository.ReportedContentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.ZonedDateTime;

/**
 * Created by fatih on 14/1/16.
 */

@Service
@Transactional
public class ReportedContentService {

    private final Logger log = LoggerFactory.getLogger(ReportedContentService.class);

    @Inject
    private ReportedContentRepository reportedContentRepository;

    @Inject
    private CommentService commentService;

    @Inject
    private QuestionService questionService;



    public ReportedContent save(ReportedContent reportedContent) {
        log.debug("Request to save ReportedContent : {}", reportedContent);

        reportedContent.setCreateDate(ZonedDateTime.now());

        boolean isCommentReport = false;

        ReportedContent current = null;
        ReportedContent result = null;
        if(reportedContent.getComment() != null) {
            //Bir yorumun sikayeti ise
            isCommentReport = true;
            current = reportedContentRepository.findUsersCommentReport(reportedContent.getQuestion().getId(), reportedContent.getComment().getId());
        } else {
            // Bir sorunun sikayeti ise
            current = reportedContentRepository.findUsersQuestionReport(reportedContent.getQuestion().getId());
        }

        if(current != null) {
            log.debug("User already reported this comment/question. Do nothing");
            result = current;
        } else {
            result = reportedContentRepository.save(reportedContent);
        }

        Long reportCount;
        if(isCommentReport) {
            reportCount = reportedContentRepository.countByQuestionAndComment(result.getQuestion(), result.getComment());
            if(reportCount.longValue() > Constants.ReportedContentLimits.COMMENT) {
                commentService.deactivateReportedComment(result.getComment());
            }
        } else {
            reportCount = reportedContentRepository.countByQuestionAndCommentIsNull(result.getQuestion());
            if(reportCount.longValue() > Constants.ReportedContentLimits.QUESTION) {
                questionService.deactivateReportedQuestion(result.getQuestion());
            }
        }

        return result;
    }
}
