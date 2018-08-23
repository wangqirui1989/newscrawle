package com.zjs.newscrawle.component;

import com.zjs.newscrawle.component.asynctask.AsyncDetailHandlerComponent;
import com.zjs.newscrawle.config.WebConfig;
import com.zjs.newscrawle.filter.Filter;
import com.zjs.newscrawle.pojo.Page;
import com.zjs.newscrawle.pojo.Statics;
import com.zjs.newscrawle.pojo.TwoTuple;
import com.zjs.newscrawle.utils.Utils;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @Author: Qirui Wang
 * @Description: 详细页处理
 * @Date: 18/8/18
 */
@Component
public class DetailHandler {

    @Resource
    private AsyncDetailHandlerComponent asyncDetailHandlerComponent;

    @Resource
    private WebConfig webConfig;

    @Resource
    private Filter filter;

    private static Logger logger = LoggerFactory.getLogger(DetailHandler.class);

    /**
     *
     * @author Qirui Wang
     * @date 18/8/18 19:31
     * @usage 异步处理详细页
     * @method detailHandler
     * @param set
     * @return com.zjs.newscrawle.pojo.Page
     */
    @Async
    public Future<List<Page>> detailAsyncTask(Set<Element> set) throws InterruptedException, ExecutionException {

        long currentTimeMillis = System.currentTimeMillis();

        List<Page> pageList = new ArrayList<>();
        Utils.PageBuilder pageBuilder;
        for (Element element : set) {
            String url = element.attr("href");
            if (!filter.isValid(url)) {
                try {

                    Document doc = Jsoup.connect(url).timeout(50000).get();

                    if (doc != null) {

                        Future<TwoTuple<String>> titleAndCreatedTimeTask = asyncDetailHandlerComponent.getTitleAndCreatedTime(doc);
                        Future<TwoTuple<String>> getAuthorAndCategoryTask = asyncDetailHandlerComponent.getAuthorAndCategory(doc);
                        Future<TwoTuple<String>> getSourceAndSourceLinkTask = asyncDetailHandlerComponent.getSourceAndSourceLink(doc);
                        Future<Statics> getStaticsTask = asyncDetailHandlerComponent.getStatics(doc, url);

                        while (true) {
                            if (titleAndCreatedTimeTask.isDone() && getAuthorAndCategoryTask.isDone() &&
                                    getSourceAndSourceLinkTask.isDone() && getStaticsTask.isDone()) {
                                TwoTuple<String> titleAndCreatedTime = titleAndCreatedTimeTask.get();
                                TwoTuple<String> authorAndCategory = getAuthorAndCategoryTask.get();
                                TwoTuple<String> sourceAndSourceLink = getSourceAndSourceLinkTask.get();
                                Statics statics = getStaticsTask.get();

                                // 构建返回数据格式

                                pageBuilder = new Utils.PageBuilder();
                                pageBuilder.buildWebSite(webConfig.getUrl());
                                pageBuilder.buildAuthor(Utils.getAuthor(authorAndCategory.getFirstIndex()));
                                pageBuilder.buildCreatedTime(titleAndCreatedTime.getSecondIndex());
                                pageBuilder.buildTitle(titleAndCreatedTime.getFirstIndex());
                                pageBuilder.buildLink(element.attr("href"));
                                pageBuilder.buildSource(sourceAndSourceLink.getFirstIndex());
                                pageBuilder.buildSourceLink(sourceAndSourceLink.getSecondIndex());
                                pageBuilder.buildInterview(statics.getInterview());
                                pageBuilder.buildHotHits(statics.getHotHit());
                                pageBuilder.buildComments(statics.getComments());
                                pageBuilder.buildCategory(authorAndCategory.getSecondIndex());

                                pageList.add(pageBuilder.buildPage());

                                break;
                            }
                        }

                        Thread.sleep(1000);

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

        long endTimeMillis = System.currentTimeMillis();

        logger.debug("[" + Thread.currentThread().getName() + " Thread] Consuming: " + (currentTimeMillis - endTimeMillis) + "ms");

        return new AsyncResult<>(pageList);
    }
}
