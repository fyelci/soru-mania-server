package com.fyelci.sorumania.web.rest.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the QuestionRating entity.
 */
public class QuestionRatingDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer rate;

    private ZonedDateTime createDate;

    private ZonedDateTime lastModifiedDate;

    private Long questionId;
    private Long userId;

    private String userLogin;

    private Integer rateCount;

    private BigDecimal rateAvg;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public ZonedDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Integer getRateCount() {
        return rateCount;
    }

    public void setRateCount(Integer rateCount) {
        this.rateCount = rateCount;
    }

    public BigDecimal getRateAvg() {
        return rateAvg;
    }

    public void setRateAvg(BigDecimal rateAvg) {
        this.rateAvg = rateAvg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        QuestionRatingDTO questionRatingDTO = (QuestionRatingDTO) o;

        if ( ! Objects.equals(id, questionRatingDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "QuestionRatingDTO{" +
            "id=" + id +
            ", rate='" + rate + "'" +
            ", createDate='" + createDate + "'" +
            ", lastModifiedDate='" + lastModifiedDate + "'" +
            '}';
    }
}
