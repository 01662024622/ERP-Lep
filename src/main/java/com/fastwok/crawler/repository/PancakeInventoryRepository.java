package com.fastwok.crawler.repository;

import com.fastwok.crawler.entities.PancakeInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PancakeInventoryRepository extends JpaRepository<PancakeInventory, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM pancake_inventory WHERE pId=?1 LIMIT 1")
    PancakeInventory findFirstByPId(String id);
}
