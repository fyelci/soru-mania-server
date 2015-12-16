package com.fyelci.sorumania.repository;

import com.fyelci.sorumania.domain.UserContentPreference;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UserContentPreference entity.
 */
public interface UserContentPreferenceRepository extends JpaRepository<UserContentPreference,Long> {

    @Query("select userContentPreference from UserContentPreference userContentPreference where userContentPreference.user.login = ?#{principal.username}")
    List<UserContentPreference> findByUserIsCurrentUser();

}
