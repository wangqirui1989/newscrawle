package com.zjs.newscrawle.component;

import com.zjs.newscrawle.service.NewsCrawlerService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: Qirui Wang
 * @Description: 定时任务模块
 * @Date: 11/8/18
 */
@Component
public class TaskScheduling {

    @Resource
    private NewsCrawlerService newsCrawlerService;

}
