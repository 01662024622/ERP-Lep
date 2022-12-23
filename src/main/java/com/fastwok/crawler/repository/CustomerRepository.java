package com.fastwok.crawler.repository;

import com.fastwok.crawler.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    @Query("SELECT COUNT(o.phone) FROM Customer o where o.phone=?1")
    long countByPhone(String phone);
    @Query(value = "SELECT * FROM ERPCustomer  where phone=?1 LIMIT 1",nativeQuery = true)
    Customer getByPhone(String phone);
}
