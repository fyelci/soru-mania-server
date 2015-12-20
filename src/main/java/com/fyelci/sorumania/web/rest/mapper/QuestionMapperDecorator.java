package com.fyelci.sorumania.web.rest.mapper;

import com.fyelci.sorumania.domain.Question;
import com.fyelci.sorumania.util.DateUtil;
import com.fyelci.sorumania.web.rest.dto.QuestionDTO;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Locale;


/**
 * Created by fatih on 20/12/15.
 */
public abstract class QuestionMapperDecorator implements  QuestionMapper{

    @Autowired
    @Qualifier("delegate")
    private QuestionMapper delegate;

    @Override
    public QuestionDTO questionToQuestionDTO(Question question) {
        QuestionDTO dto = delegate.questionToQuestionDTO(question);

        if (question != null) {
            PrettyTime p = new PrettyTime(new Locale("tr"));
            dto.setReadableCreateDate(question.getCreateDate() != null ? p.format(DateUtil.toJavaUtilDateFromZonedDateTime(question.getCreateDate())) : "");
            dto.setReadableModifyDate(question.getLastModifiedDate() != null ? p.format(DateUtil.toJavaUtilDateFromZonedDateTime(question.getLastModifiedDate())) : "");
        }

        return dto;
    }


}
