package com.fyelci.sorumania.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.ZonedDateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ScoreHistory.
 */
@Entity
@Table(name = "score_history")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ScoreHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "score")
    private Integer score;

    @Column(name = "content_id")
    private Long contentId;

    @Column(name = "create_date")
    private ZonedDateTime createDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "transaction_type_id")
    private Lov transactionType;

    @ManyToOne
    @JoinColumn(name = "content_type_id")
    private Lov contentType;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Lov getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(Lov lov) {
        this.transactionType = lov;
    }

    public Lov getContentType() {
        return contentType;
    }

    public void setContentType(Lov lov) {
        this.contentType = lov;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ScoreHistory scoreHistory = (ScoreHistory) o;
        return Objects.equals(id, scoreHistory.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ScoreHistory{" +
            "id=" + id +
            ", score='" + score + "'" +
            ", contentId='" + contentId + "'" +
            ", createDate='" + createDate + "'" +
            '}';
    }
}
