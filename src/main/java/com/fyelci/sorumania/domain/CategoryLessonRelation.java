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
 * A CategoryLessonRelation.
 */
@Entity
@Table(name = "category_lesson_relation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CategoryLessonRelation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "create_date")
    private ZonedDateTime createDate;

    @Column(name = "last_modified_date")
    private ZonedDateTime lastModifiedDate;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Lov category;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lov lesson;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Lov getCategory() {
        return category;
    }

    public void setCategory(Lov lov) {
        this.category = lov;
    }

    public Lov getLesson() {
        return lesson;
    }

    public void setLesson(Lov lov) {
        this.lesson = lov;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CategoryLessonRelation categoryLessonRelation = (CategoryLessonRelation) o;
        return Objects.equals(id, categoryLessonRelation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CategoryLessonRelation{" +
            "id=" + id +
            ", createDate='" + createDate + "'" +
            ", lastModifiedDate='" + lastModifiedDate + "'" +
            '}';
    }
}
