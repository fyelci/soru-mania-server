package com.fyelci.sorumania.web.rest.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Comment entity.
 */
public class CommentDTO implements Serializable {

    private Long id;

    private String text;

    private String mediaUrl;

    private ZonedDateTime createDate;

    private ZonedDateTime lastModifiedDate;

    private Long parentId;

    private Long questionId;
    private Long userId;

    private String userLogin;

    private Long commentStatusId;

    private String commentStatusName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
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

    public Long getCommentStatusId() {
        return commentStatusId;
    }

    public void setCommentStatusId(Long lovId) {
        this.commentStatusId = lovId;
    }

    public String getCommentStatusName() {
        return commentStatusName;
    }

    public void setCommentStatusName(String lovName) {
        this.commentStatusName = lovName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CommentDTO commentDTO = (CommentDTO) o;

        if ( ! Objects.equals(id, commentDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CommentDTO{" +
            "id=" + id +
            ", text='" + text + "'" +
            ", mediaUrl='" + mediaUrl + "'" +
            ", createDate='" + createDate + "'" +
            ", lastModifiedDate='" + lastModifiedDate + "'" +
            ", parentId='" + parentId + "'" +
            '}';
    }
}
