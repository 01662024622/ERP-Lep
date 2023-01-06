package com.fastwok.crawler.repository;

import com.fastwok.crawler.entities.PancakeItemMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Pageable;
import java.util.List;

public interface PancakeItemMapRepository extends JpaRepository<PancakeItemMap, Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM PancakeItemMap WHERE pId=?1 LIMIT 1")
    PancakeItemMap findFirstByPId(int id);

    @Query(nativeQuery = true, value = "SELECT * FROM pancake_item_map WHERE push = 0 LIMIT ?1 OFFSET ?2")
    List<PancakeItemMap> getByPage(int limit,int offset);
}
