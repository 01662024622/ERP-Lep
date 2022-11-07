package com.fastwok.crawler.util;

import org.hibernate.dialect.MySQL5Dialect;

public class CustomMysqlDialect extends MySQL5Dialect {
    @Override
    public String getTableTypeString(){
        return " ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
    }
}
