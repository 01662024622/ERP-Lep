package com.fastwok.crawler.repository;

import com.fastwok.crawler.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT COUNT(o.pId) FROM Order o where o.pId=?1")
    long countByPId(long id);
}
