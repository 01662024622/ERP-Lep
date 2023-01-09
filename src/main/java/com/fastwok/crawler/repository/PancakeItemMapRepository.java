package com.fastwok.crawler.repository;

import com.fastwok.crawler.entities.PancakeItemMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Pageable;
import java.util.List;

public interface PancakeItemMapRepository extends JpaRepository<PancakeItemMap, Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM pancake_item_map WHERE pId=?1 LIMIT 1")
    PancakeItemMap findFirstByPId(int id);
    @Query(nativeQuery = true, value = "SELECT * FROM pancake_item_map WHERE code=?1 LIMIT 1")
    PancakeItemMap findFirstByPCode(String code);

    @Query(nativeQuery = true, value = "SELECT * FROM pancake_item_map WHERE push = 0 ORDER BY id DESC LIMIT ?1 OFFSET ?2")
    List<PancakeItemMap> getByPage(int limit,int offset);
}
