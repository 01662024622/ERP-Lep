package com.fastwok.crawler.job;

import com.fastwok.crawler.services.isservice.TaskOrderService;
import com.fastwok.crawler.services.isservice.TaskService;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class CrawlerFwSchedule {
    @Autowired
    TaskOrderService taskOrderService;

    @Scheduled(fixedDelay = 1143200000)
    public void importData() throws UnirestException, InterruptedException {
        taskOrderService.getData();
    }
}
