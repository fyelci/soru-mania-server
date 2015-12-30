package com.fyelci.sorumania.repository;

import com.fyelci.sorumania.domain.Stuff;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Stuff entity.
 */
public interface StuffRepository extends JpaRepository<Stuff,Long> {

}
