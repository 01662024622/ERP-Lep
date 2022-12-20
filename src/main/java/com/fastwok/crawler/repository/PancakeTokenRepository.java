package com.fastwok.crawler.repository;

import com.fastwok.crawler.entities.PancakeToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PancakeTokenRepository extends JpaRepository<PancakeToken, Long> {
    PancakeToken findTopByOrderByIdDesc();
}