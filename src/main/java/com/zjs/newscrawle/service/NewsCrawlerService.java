package com.zjs.newscrawle.service;

import com.zjs.newscrawle.component.SinaDetailHandlerImpl;
import com.zjs.newscrawle.config.WebConfig;
import com.zjs.newscrawle.pojo.TwoTuple;
import com.zjs.newscrawle.utils.HeadingEnum;
import com.zjs.newscrawle.utils.Utils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @Author: Qirui Wang
 * @Description: 爬虫服务
 * @Date: 11/8/18
 */
@Service
public class NewsCrawlerService {

    @Resource
    private WebConfig webConfig;

    @Resource
    private SinaDetailHandlerImpl sinaDetailHandler;

    private Elements links;

    /***
     *
     * @author Qirui Wang
     * @date 11/8/18 10:56
     * @usage 获得新闻链接
     * @method getNewsLinkSet
     * @param
     * @return java.util.Set<org.jsoup.nodes.Element>
     */
    public void setLinks() {

        String url = webConfig.getUrl();

        try {
            Document doc = Jsoup.connect(url).get();
            links = doc.select("a[href]");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     *
     * @author Qirui Wang
     * @date 13/8/18 21:25
     * @usage 获得详细链接
     * @method getDetailLinks
     * @param
     * @return java.util.Set<org.jsoup.nodes.Element>
     */
    @Async
    public Future<Set<Element>> getDetailLinks() {
        Set<Element> detailLinkSet = new HashSet<>();
        for (Element link : links) {
            if (Utils.validDetail(link)) {
                detailLinkSet.add(link);
            }
        }
        return new AsyncResult<>(detailLinkSet);
    }

    /**
     *
     * @author Qirui Wang
     * @date 13/8/18 21:44
     * @usage 获得指定标题链接
     * @method getHeadingLinks
     * @param
     * @return java.util.concurrent.Future<java.util.Set<org.jsoup.nodes.Element>>
     */
    @Async
    public Future<Set<Element>> getHeadingLinks() {
        Set<Element> headLinkSet = new HashSet<>();
        for (Element link : links) {
            if (HeadingEnum.isValid(link)) {
                headLinkSet.add(link);
            }
        }

        return new AsyncResult<>(headLinkSet);
    }
    
    /**  
     *    
     * @author Qirui Wang  
     * @date 13/8/18 21:54
     * @usage 获得详细页和标题页集合
     * @method generateHeadingAndDetails  
     * @param  
     * @return com.zjs.newscrawle.service.TwoTuple<java.util.Set<org.jsoup.nodes.Element>>  
     */  
    public TwoTuple<Set<Element>> generateHeadingAndDetails()
            throws InterruptedException, ExecutionException {

        Set<Element> headSet;
        Set<Element> detailSet;

        Future<Set<Element>> headingTask = getHeadingLinks();
        Future<Set<Element>> detailTask = getDetailLinks();

        while (true) {
            if (headingTask.isDone() && detailTask.isDone()) {
                headSet = headingTask.get();
                detailSet = headingTask.get();

                Thread.sleep(1000);
                break;
            }
        }

        return new TwoTuple<>(headSet, detailSet);
    }

    public void handlerHeadingLink() {

    }

    public void handlerDetailLinks() {

    }

}
