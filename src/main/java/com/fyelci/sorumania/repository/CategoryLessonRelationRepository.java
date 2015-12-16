package com.fyelci.sorumania.repository;

import com.fyelci.sorumania.domain.CategoryLessonRelation;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CategoryLessonRelation entity.
 */
public interface CategoryLessonRelationRepository extends JpaRepository<CategoryLessonRelation,Long> {

}
