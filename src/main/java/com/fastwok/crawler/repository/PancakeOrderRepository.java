package com.fastwok.crawler.repository;

import com.fastwok.crawler.entities.PancakeOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PancakeOrderRepository extends JpaRepository<PancakeOrder, Long> {
    @Query("SELECT COUNT(o.pId) FROM PancakeOrder o where o.pId=?1")
    long countByPId(long id);
}
