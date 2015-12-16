package com.fyelci.sorumania.repository;

import com.fyelci.sorumania.domain.ReportedContent;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ReportedContent entity.
 */
public interface ReportedContentRepository extends JpaRepository<ReportedContent,Long> {

    @Query("select reportedContent from ReportedContent reportedContent where reportedContent.reporterUser.login = ?#{principal.username}")
    List<ReportedContent> findByReporterUserIsCurrentUser();

}
