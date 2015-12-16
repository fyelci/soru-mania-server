package com.fyelci.sorumania.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.ZonedDateTime;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A UserContentPreference.
 */
@Entity
@Table(name = "user_content_preference")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserContentPreference implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "content_id", nullable = false)
    private Long contentId;

    @Column(name = "create_date")
    private ZonedDateTime createDate;

    @Column(name = "last_modified_date")
    private ZonedDateTime lastModifiedDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "content_type_id")
    private Lov contentType;

    @ManyToOne
    @JoinColumn(name = "content_preference_id")
    private Lov contentPreference;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Lov getContentType() {
        return contentType;
    }

    public void setContentType(Lov lov) {
        this.contentType = lov;
    }

    public Lov getContentPreference() {
        return contentPreference;
    }

    public void setContentPreference(Lov lov) {
        this.contentPreference = lov;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserContentPreference userContentPreference = (UserContentPreference) o;
        return Objects.equals(id, userContentPreference.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "UserContentPreference{" +
            "id=" + id +
            ", contentId='" + contentId + "'" +
            ", createDate='" + createDate + "'" +
            ", lastModifiedDate='" + lastModifiedDate + "'" +
            '}';
    }
}
