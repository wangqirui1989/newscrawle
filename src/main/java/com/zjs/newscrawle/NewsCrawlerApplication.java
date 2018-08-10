package com.zjs.newscrawle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Qirui Wang
 * @date 07/08/2018
 * @usage: 主程序入口
 */
@EnableScheduling
@SpringBootApplication
public class NewsCrawlerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewsCrawlerApplication.class, args);
    }
}
