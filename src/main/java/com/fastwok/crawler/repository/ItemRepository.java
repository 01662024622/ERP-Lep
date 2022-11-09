package com.fastwok.crawler.repository;

import com.fastwok.crawler.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    @Query(nativeQuery = true,value = "SELECT * FROM Item WHERE pId=?1 LIMIT 1")
    Item findFirstByPId(long id);
}


