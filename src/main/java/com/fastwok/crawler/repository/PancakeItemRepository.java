package com.fastwok.crawler.repository;

import com.fastwok.crawler.entities.PancakeItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PancakeItemRepository extends JpaRepository<PancakeItem, Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM PancakeItem WHERE pId=?1 LIMIT 1")
    PancakeItem findFirstByPId(String id);
}


