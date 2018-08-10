package com.zjs.newscrawle.config;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: Qirui Wang
 * @Description: TODO
 * @Date: 9/8/18
 */
@Configuration
public class WebConfig {

    @Value("${web.url}")
    private String url;

    @Bean
    public Set<Element> newsLinkSet() {

        Set<Element> linkSet = new HashSet<>();

        try {
            Document doc = Jsoup.connect(url).get();
            Elements links = doc.select("a[href]");
            for (Element link : links) {

                if (link.attr("href").contains(url)) {
                    linkSet.add(link);
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return linkSet;
    }
}
