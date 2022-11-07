package com.fastwok.crawler.repository;

import com.fastwok.crawler.entities.Customer;
import com.fastwok.crawler.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

    public interface ItemRepository extends JpaRepository<Item, Integer> {
        @Query("SELECT i FROM Item i where i.IsActive=true AND IsNull(i.Kiot_Id, '') = '' ")
        List<Item> getData();

    }


