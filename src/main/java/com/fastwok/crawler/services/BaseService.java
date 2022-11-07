package com.fastwok.crawler.services;

import com.mashape.unirest.http.exceptions.UnirestException;

public interface BaseService {
    public void getData() throws UnirestException, InterruptedException;
}
