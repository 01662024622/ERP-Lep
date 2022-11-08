package com.fastwok.crawler.repository;

import com.fastwok.crawler.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
}


