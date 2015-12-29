package com.fyelci.sorumania.web.rest.mapper;

import com.fyelci.sorumania.domain.*;
import com.fyelci.sorumania.web.rest.dto.CommentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Comment and its DTO CommentDTO.
 */
@Mapper(componentModel = "spring", uses = {})
@DecoratedWith(CommentMapperDecorator.class)
public interface CommentMapper {

    @Mapping(source = "question.id", target = "questionId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "commentStatus.id", target = "commentStatusId")
    @Mapping(source = "commentStatus.name", target = "commentStatusName")
    @Mapping(target = "readableCreateDate", ignore = true)
    @Mapping(target = "readableModifyDate", ignore = true)
    @Mapping(target = "user", ignore = true)
    CommentDTO commentToCommentDTO(Comment comment);

    @Mapping(source = "questionId", target = "question")
    @Mapping(source = "userId", target = "user")
    @Mapping(source = "commentStatusId", target = "commentStatus")
    Comment commentDTOToComment(CommentDTO commentDTO);

    default Question questionFromId(Long id) {
        if (id == null) {
            return null;
        }
        Question question = new Question();
        question.setId(id);
        return question;
    }

    default User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }

    default Lov lovFromId(Long id) {
        if (id == null) {
            return null;
        }
        Lov lov = new Lov();
        lov.setId(id);
        return lov;
    }
}
