package com.fastwok.crawler.job;

import com.fastwok.crawler.services.isservice.TaskOrderService;
import com.fastwok.crawler.services.isservice.TaskPancakeOrderService;
import com.fastwok.crawler.services.isservice.TaskPushItemPancakeService;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class CrawlerFwSchedule {
    @Autowired
    TaskPancakeOrderService taskPancakeOrderService;

    @Scheduled(fixedDelay = 60000)
    public void importData2() throws UnirestException, InterruptedException {
        taskPancakeOrderService.getData();
    }
}
