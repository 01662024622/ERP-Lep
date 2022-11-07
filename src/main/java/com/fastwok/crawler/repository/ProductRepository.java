package com.fastwok.crawler.repository;

import com.fastwok.crawler.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
