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

    @Value("${web.commentUrl}")
    private String commentUrl;

    @Value(("${web.agent}"))
    private String agent;

    public String getUrl() {
        return url;
    }

    public String getCommentUrl() {
        return commentUrl;
    }

    public String getAgent() {
        return agent;
    }
}
