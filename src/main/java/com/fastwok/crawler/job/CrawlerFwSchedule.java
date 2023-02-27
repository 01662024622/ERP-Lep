package com.fastwok.crawler.job;

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
    TaskPushItemPancakeService taskPushItemPancakeService;

    @Scheduled(fixedDelay = 600000)
    public void importData2() throws UnirestException, InterruptedException {
        taskPushItemPancakeService.getData();
    }
}
