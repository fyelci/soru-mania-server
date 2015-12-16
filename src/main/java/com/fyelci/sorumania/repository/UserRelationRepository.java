package com.fyelci.sorumania.repository;

import com.fyelci.sorumania.domain.UserRelation;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UserRelation entity.
 */
public interface UserRelationRepository extends JpaRepository<UserRelation,Long> {

    @Query("select userRelation from UserRelation userRelation where userRelation.user.login = ?#{principal.username}")
    List<UserRelation> findByUserIsCurrentUser();

    @Query("select userRelation from UserRelation userRelation where userRelation.relatedUser.login = ?#{principal.username}")
    List<UserRelation> findByRelatedUserIsCurrentUser();

}
