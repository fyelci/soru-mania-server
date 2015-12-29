package com.fyelci.sorumania.web.rest.mapper;

import com.fyelci.sorumania.domain.Comment;
import com.fyelci.sorumania.domain.Question;
import com.fyelci.sorumania.util.DateUtil;
import com.fyelci.sorumania.web.rest.dto.CommentDTO;
import com.fyelci.sorumania.web.rest.dto.QuestionDTO;
import com.fyelci.sorumania.web.rest.dto.UserDTO;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Locale;

/**
 * Created by fatih on 20/12/15.
 */
public abstract class CommentMapperDecorator implements CommentMapper{

    @Autowired
    @Qualifier("delegate")
    private CommentMapper delegate;

    @Override
    public CommentDTO commentToCommentDTO(Comment comment) {
        CommentDTO dto = delegate.commentToCommentDTO(comment);

        if (comment != null) {
            PrettyTime p = new PrettyTime(new Locale("tr"));
            dto.setReadableCreateDate(comment.getCreateDate() != null ? p.format(DateUtil.toJavaUtilDateFromZonedDateTime(comment.getCreateDate())) : "");
            dto.setReadableModifyDate(comment.getLastModifiedDate() != null ? p.format(DateUtil.toJavaUtilDateFromZonedDateTime(comment.getLastModifiedDate())) : "");

            if(comment.getUser() != null) {
                dto.setUser(new UserDTO(comment.getUser()));
            }
        }

        return dto;
    }

}
