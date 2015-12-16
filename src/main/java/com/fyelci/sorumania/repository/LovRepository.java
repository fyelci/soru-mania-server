package com.fyelci.sorumania.repository;

import com.fyelci.sorumania.domain.Lov;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Lov entity.
 */
public interface LovRepository extends JpaRepository<Lov,Long> {

    public List<Lov> findByTypeOrderBySequenceAsc(String type);
}
