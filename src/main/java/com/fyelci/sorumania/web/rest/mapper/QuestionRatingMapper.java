package com.fyelci.sorumania.web.rest.mapper;

import com.fyelci.sorumania.domain.*;
import com.fyelci.sorumania.web.rest.dto.QuestionRatingDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity QuestionRating and its DTO QuestionRatingDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface QuestionRatingMapper {

    @Mapping(source = "question.id", target = "questionId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    QuestionRatingDTO questionRatingToQuestionRatingDTO(QuestionRating questionRating);

    @Mapping(source = "questionId", target = "question")
    @Mapping(source = "userId", target = "user")
    QuestionRating questionRatingDTOToQuestionRating(QuestionRatingDTO questionRatingDTO);

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
}
