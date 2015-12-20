package com.fyelci.sorumania.web.rest.mapper;

import com.fyelci.sorumania.domain.*;
import com.fyelci.sorumania.web.rest.dto.QuestionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Question and its DTO QuestionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface QuestionMapper {

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "lesson.id", target = "lessonId")
    @Mapping(source = "lesson.name", target = "lessonName")
    @Mapping(source = "questionStatus.id", target = "questionStatusId")
    @Mapping(source = "questionStatus.name", target = "questionStatusName")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    QuestionDTO questionToQuestionDTO(Question question);

    @Mapping(source = "categoryId", target = "category")
    @Mapping(source = "lessonId", target = "lesson")
    @Mapping(source = "questionStatusId", target = "questionStatus")
    @Mapping(source = "userId", target = "user")
    Question questionDTOToQuestion(QuestionDTO questionDTO);

    default Lov lovFromId(Long id) {
        if (id == null) {
            return null;
        }
        Lov lov = new Lov();
        lov.setId(id);
        return lov;
    }

    default User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }
}
