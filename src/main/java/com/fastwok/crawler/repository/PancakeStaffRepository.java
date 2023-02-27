package com.fastwok.crawler.repository;

import com.fastwok.crawler.entities.PancakeStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PancakeStaffRepository extends JpaRepository<PancakeStaff, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM pancake_staff p WHERE p.p_id = ?1 LIMIT 1")
    PancakeStaff getPancakeStaffByNId(String key);
}

