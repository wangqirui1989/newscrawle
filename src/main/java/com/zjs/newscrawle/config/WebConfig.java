package com.zjs.newscrawle.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Qirui Wang
 * @Description: TODO
 * @Date: 9/8/18
 */
@Configuration
public class WebConfig {

    @Value("${web.url}")
    private String url;

    public String getUrl() {
        return url;
    }
}
