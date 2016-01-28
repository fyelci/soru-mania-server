package com.fyelci.sorumania.repository;

import com.fyelci.sorumania.config.Constants;
import com.fyelci.sorumania.domain.User;

import java.time.ZonedDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneByActivationKey(String activationKey);

    List<User> findAllByActivatedIsFalseAndCreatedDateBefore(ZonedDateTime dateTime);

    Optional<User> findOneByResetKey(String resetKey);

    Optional<User> findOneByEmail(String email);

    Optional<User> findOneByLogin(String login);

    Optional<User> findOneById(Long userId);

    @Override
    void delete(User t);


    @Query("select user " +
        " from User user, UserRelation ul " +
        " where ul.user.id = ?1 " +
        " and ul.relationType.id = " + Constants.UserRelationTypes.APPROVED +
        " and user.id = ul.relatedUser.id " +
        " order by ul.createDate desc")
    Page<User> listFollowingUsers(Long userId, Pageable pageable);

    @Query("select user " +
        " from User user, UserRelation ul " +
        " where ul.relatedUser.id = ?1 " +
        " and ul.relationType.id = " + Constants.UserRelationTypes.APPROVED +
        " and user.id = ul.user.id " +
        " order by ul.createDate desc")
    Page<User> listFollowerUsers(Long userId, Pageable pageable);

}
