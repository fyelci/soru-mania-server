package com.fyelci.sorumania.web.rest.dto;

import org.springframework.data.domain.AbstractPageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Created by fatih on 4/1/16.
 */
public class QuestionListParams extends AbstractPageRequest {

    private static final long serialVersionUID = -4331229118956089562L;
    private final Sort sort;

    private Long categoryId;
    private Long lessonId;
    private Integer listType;   //Yeniler, Popülerler, Cevapsızlar vs.

    public QuestionListParams(int page, int size) {
        this(page, size, (Sort)null);
    }

    public QuestionListParams(int page, int size, Sort.Direction direction, String... properties) {
        this(page, size, new Sort(direction, properties));
    }

    public QuestionListParams(int page, int size, Sort sort) {
        super(page, size);
        this.sort = sort;
    }

    public QuestionListParams(int page, int size, Sort sort, Long categoryId, Long lessonId, Integer listType) {
        super(page, size);
        this.sort = sort;
        this.categoryId = categoryId;
        this.lessonId = lessonId;
        this.listType = listType;
    }

    public Sort getSort() {
        return this.sort;
    }

    public Pageable next() {
        return new QuestionListParams(this.getPageNumber() + 1, this.getPageSize(), this.getSort());
    }

    public QuestionListParams previous() {
        return this.getPageNumber() == 0?this:new QuestionListParams(this.getPageNumber() - 1, this.getPageSize(), this.getSort());
    }

    public Pageable first() {
        return new QuestionListParams(0, this.getPageSize(), this.getSort());
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getLessonId() {
        return lessonId;
    }

    public void setLessonId(Long lessonId) {
        this.lessonId = lessonId;
    }

    public Integer getListType() {
        return listType;
    }

    public void setListType(Integer listType) {
        this.listType = listType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        QuestionListParams that = (QuestionListParams) o;

        if (sort != null ? !sort.equals(that.sort) : that.sort != null) return false;
        if (categoryId != null ? !categoryId.equals(that.categoryId) : that.categoryId != null) return false;
        if (lessonId != null ? !lessonId.equals(that.lessonId) : that.lessonId != null) return false;
        return !(listType != null ? !listType.equals(that.listType) : that.listType != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (sort != null ? sort.hashCode() : 0);
        result = 31 * result + (categoryId != null ? categoryId.hashCode() : 0);
        result = 31 * result + (lessonId != null ? lessonId.hashCode() : 0);
        result = 31 * result + (listType != null ? listType.hashCode() : 0);
        return result;
    }
}
