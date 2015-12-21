package com.fyelci.sorumania.web.rest.dto;

import com.fyelci.sorumania.domain.Comment;
import com.fyelci.sorumania.domain.User;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.*;


/**
 * A DTO for the Question entity.
 */
public class QuestionDTO implements Serializable {

    private Long id;

    private String detail;

    private String mediaUrl;

    private Integer commentCount;

    private ZonedDateTime createDate;

    private ZonedDateTime lastModifiedDate;

    private Long categoryId;

    private String categoryName;

    private Long lessonId;

    private String lessonName;

    private Long questionStatusId;

    private String questionStatusName;

    private Long userId;

    private String userLogin;

    private UserDTO user;

    List<CommentDTO> commentList = new ArrayList<>();

    private String readableCreateDate;
    private String readableModifyDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long lovId) {
        this.categoryId = lovId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String lovName) {
        this.categoryName = lovName;
    }

    public Long getLessonId() {
        return lessonId;
    }

    public void setLessonId(Long lovId) {
        this.lessonId = lovId;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lovName) {
        this.lessonName = lovName;
    }

    public Long getQuestionStatusId() {
        return questionStatusId;
    }

    public void setQuestionStatusId(Long lovId) {
        this.questionStatusId = lovId;
    }

    public String getQuestionStatusName() {
        return questionStatusName;
    }

    public void setQuestionStatusName(String lovName) {
        this.questionStatusName = lovName;
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

    public List<CommentDTO> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CommentDTO> commentList) {
        this.commentList = commentList;
    }

    public String getReadableCreateDate() {
        return readableCreateDate;
    }

    public void setReadableCreateDate(String readableCreateDate) {
        this.readableCreateDate = readableCreateDate;
    }

    public String getReadableModifyDate() {
        return readableModifyDate;
    }

    public void setReadableModifyDate(String readableModifyDate) {
        this.readableModifyDate = readableModifyDate;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        QuestionDTO questionDTO = (QuestionDTO) o;

        if ( ! Objects.equals(id, questionDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "QuestionDTO{" +
            "id=" + id +
            ", detail='" + detail + "'" +
            ", mediaUrl='" + mediaUrl + "'" +
            ", commentCount='" + commentCount + "'" +
            ", createDate='" + createDate + "'" +
            ", lastModifiedDate='" + lastModifiedDate + "'" +
            '}';
    }
}
