package com.fastwok.crawler.repository;

import com.fastwok.crawler.entities.PancakeShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PancakeShopRepository extends JpaRepository<PancakeShop, Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM pancake_shop p WHERE p.active=1 LIMIT 1")
    List<PancakeShop> getPancakeShopByActive();
}


