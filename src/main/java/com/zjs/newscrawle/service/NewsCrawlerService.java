package com.zjs.newscrawle.service;

import com.zjs.newscrawle.component.DetailHandler;
import com.zjs.newscrawle.component.HeadHandler;
import com.zjs.newscrawle.component.asynctask.AsyncClassifierComponent;
import com.zjs.newscrawle.config.TaskExecutorConfig;
import com.zjs.newscrawle.config.WebConfig;
import com.zjs.newscrawle.pojo.Page;
import com.zjs.newscrawle.pojo.TwoTuple;
import com.zjs.newscrawle.utils.ThreadListCheck;
import com.zjs.newscrawle.utils.Utils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
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
    private TaskExecutorConfig taskExecutorConfig;

    @Resource
    private DetailHandler detailHandler;

    @Resource
    private HeadHandler headHandler;

    @Resource
    private AsyncClassifierComponent asyncClassifierComponent;

    private Elements links;

    /**
     * 自定义线程数
     */
    private int threadNum;

    @PostConstruct
    public void init() {
        int num = taskExecutorConfig.getCorePoolSize()/2;
        threadNum = (num == 0)? 1 : num;
    }

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

        Future<Set<Element>> headingTask = asyncClassifierComponent.getHeadingLinks(links);
        Future<Set<Element>> detailTask = asyncClassifierComponent.getDetailLinks(links);

        while (true) {
            if (headingTask.isDone() && detailTask.isDone()) {
                headSet = headingTask.get();
                detailSet = detailTask.get();

                Thread.sleep(1000);
                break;
            }
        }

        return new TwoTuple<>(headSet, detailSet);
    }

    /**
     *
     * @author Qirui Wang
     * @date 4/9/18 21:31
     * @usage 处理标题页面
     * @method headHandler
     * @param set
     * @return java.util.List<com.zjs.newscrawle.pojo.Page>
     */
    public List<Page> headHandler(Set<Element> set) throws InterruptedException, ExecutionException {
        // 构建任务分组
        Set<Element>[] elementArray = generateTaskGroup(set);

        // 执行异步操作
        List<Future<Set<Element>>> threadList = new ArrayList<>();
        for (int i = 0; i < elementArray.length; i++) {
            threadList.add(headHandler.getDetailFromHead(elementArray[i]));
        }

        Set<Element> resultSet = new HashSet<>();

        while (true) {
            if (Utils.checkThreadArray(threadList, new ThreadListCheck<Set<Element>>() {
                @Override
                public boolean isDone(List<Future<Set<Element>>> list) {
                    for (Future<Set<Element>> task : list) {
                        if (!task.isDone()) {
                            return false;
                        }
                    }
                    return true;
                }
            })) {
                for (int num = 0; num < elementArray.length; num++) {
                    resultSet.addAll(threadList.get(num).get());
                }
                break;
            }
        }

        return detailHandler(resultSet);
    }

    /**
     *
     * @author Qirui Wang
     * @date 21/8/18 16:57
     * @usage 处理详细页
     * @method detailHandler
     * @param set
     * @return java.util.List<com.zjs.newscrawle.pojo.Page>
     */
    public List<Page> detailHandler(Set<Element> set) throws InterruptedException, ExecutionException {
        // 构建任务分组
        Set<Element>[] elementArray = generateTaskGroup(set);

        // 执行异步操作
        List<Future<List<Page>>> threadList = new ArrayList<>();
        for (int i = 0;  i< elementArray.length; i++) {
            threadList.add(detailHandler.detailAsyncTask(elementArray[i]));
        }

        List<Page> resultList = new ArrayList<>();

        while (true) {
            if (Utils.checkThreadArray(threadList, new ThreadListCheck<List<Page>>() {
                @Override
                public boolean isDone(List<Future<List<Page>>> list) {
                    for (Future<List<Page>> task : list) {
                        if (!task.isDone()) {
                            return false;
                        }
                    }
                    return true;
                }
            })) {
                for (int num = 0; num < elementArray.length; num++) {
                    resultList.addAll(threadList.get(num).get());
                }
                break;
            }
        }

        Thread.sleep(1000);

        return resultList;
    }

    /**
     *
     * @author Qirui Wang
     * @date 4/9/18 21:19
     * @usage 构建任务分组
     * @method generateTaskGroup
     * @param set
     * @return java.util.Set<org.jsoup.nodes.Element>[]
     */
    @SuppressWarnings("unchecked")
    private Set<Element>[] generateTaskGroup(Set<Element> set) {
        //  构建任务组
        Set<Element>[] elementArray;
        Set[] tempArray = new Set[threadNum];
        elementArray = (Set<Element>[]) tempArray;

        // 任务分组
        Iterator<Element> iterator = set.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            Element element = iterator.next();
            int sequence = index % threadNum;
            if (elementArray[sequence] == null) {
                elementArray[sequence] = new HashSet<Element>(){{
                    add(element);
                }};
            }
            else {
                elementArray[sequence].add(element);
            }
            index++;
        }

        return elementArray;
    }

}
