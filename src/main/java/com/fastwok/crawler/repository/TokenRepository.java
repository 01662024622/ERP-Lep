package com.fastwok.crawler.repository;

import com.fastwok.crawler.entities.Item;
import com.fastwok.crawler.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Token findTopByOrderByIdDesc();
}