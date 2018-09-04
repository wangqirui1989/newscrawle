package com.zjs.newscrawle.component;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Future;

/**
 * @Author: Qirui Wang
 * @Description: 标题页处理
 * @Date: 4/9/18
 */
@Component
public class HeadHandler {

    private Logger logger = LoggerFactory.getLogger(HeadHandler.class);

    @Async
    public Future<Set<Element>> getDetailFromHead(Set<Element> headSet) {
        Set<Element> resultSet = new HashSet<>();
        String url;
        if (headSet != null) {
            for (Element node : headSet) {
                url = node.attr("href");
                try {
                    Document doc = Jsoup.connect(url.trim()).timeout(50000).get();
                    Elements links = doc.select("a[href]");
                    for (Element link : links) {
                        resultSet.add(link);
                    }
                } catch (UnknownHostException uhe) {
                    logger.error("UNKNOWN HOST: " + url);
                } catch (HttpStatusException he) {
                    logger.error(he.getStatusCode() + " CODE: " + url);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        return new AsyncResult<>(resultSet);
    }

}
