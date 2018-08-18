package com.zjs.newscrawle.component;

import com.zjs.newscrawle.pojo.Page;
import com.zjs.newscrawle.pojo.PageBuilder;
import com.zjs.newscrawle.pojo.Statics;
import com.zjs.newscrawle.pojo.TwoTuple;
import com.zjs.newscrawle.utils.HeadingEnum;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @Author: Qirui Wang
 * @Description: 详细页处理
 * @Date: 18/8/18
 */
@Component
public class SinaDetailHandlerImpl implements DetailHandler {

    /**
     * 评论节点标志
     */
    private static final String COMMENT_NODE = "comment";

    private static final String HREF_ATTR = "href";

    /**
     *
     * @author Qirui Wang
     * @date 18/8/18 19:31
     * @usage TODO
     * @method detailHandler
     * @param url
     * @param set
     * @return com.zjs.newscrawle.pojo.Page
     */
    @Override
    public List<Page> detailHandler(String url, Set<Element> set) throws InterruptedException, ExecutionException {
        Document doc = null;
        List<Page> pageList = new ArrayList<>();
        PageBuilder pageBuilder;
        for (Element element : set) {

            try {
                doc = Jsoup.connect(element.attr("href")).get();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

            if (doc != null) {

                Future<TwoTuple<String>> titleAndCreatedTimeTask = getTitleAndCreatedTime(doc);
                Future<TwoTuple<String>> getAuthorAndCategoryTask = getAuthorAndCategory(doc);
                Future<TwoTuple<String>> getSourceAndSourceLinkTask = getSourceAndSourceLink(doc);
                Future<Statics> getStaticsTask = getStatics(doc);

                while (true) {
                    if (titleAndCreatedTimeTask.isDone() && getAuthorAndCategoryTask.isDone() &&
                            getSourceAndSourceLinkTask.isDone() && getStaticsTask.isDone()) {
                        TwoTuple<String> titleAndCreatedTime = titleAndCreatedTimeTask.get();
                        TwoTuple<String> authorAndCategory = getAuthorAndCategoryTask.get();
                        TwoTuple<String> sourceAndSourceLink = getSourceAndSourceLinkTask.get();
                        Statics statics = getStaticsTask.get();

                        pageBuilder = new PageBuilder();
                        pageBuilder.buildAuthor(authorAndCategory.getFirstIndex());
                        pageBuilder.buildCreatedTime(titleAndCreatedTime.getSecondIndex());
                        pageBuilder.buildTitle(titleAndCreatedTime.getFirstIndex());
                        pageBuilder.buildLink(element.attr("href"));
                        pageBuilder.buildSource(sourceAndSourceLink.getFirstIndex());
                        pageBuilder.buildSourceLink(sourceAndSourceLink.getSecondIndex());
                        pageBuilder.buildInterview(statics.getInterview());
                        pageBuilder.buildHotHits(statics.getHotHit());
                        pageBuilder.buildCategory(authorAndCategory.getSecondIndex());

                        pageList.add(pageBuilder.buildPage());

                        break;
                    }
                }

                Thread.sleep(1000);

            }
        }

        return pageList;
    }
    
    /**  
     *    
     * @author Qirui Wang  
     * @date 18/8/18 20:56
     * @usage TODO
     * @method getTitleAndCreatedTime  
     * @param articleNode 
     * @return com.zjs.newscrawle.pojo.TwoTuple<java.lang.String>  
     */  
    @Async
    public Future<TwoTuple<String>> getTitleAndCreatedTime(Element articleNode) {
        return new AsyncResult<>(new TwoTuple<>(getElementTextByClass("main-title", articleNode),
                getElementTextByClass("date", articleNode)));
    }
    
    /**  
     *    
     * @author Qirui Wang  
     * @date 18/8/18 20:56
     * @usage TODO
     * @method getAuthor  
     * @param articleNode 
     * @return java.lang.String  
     */  
    @Async
    public Future<TwoTuple<String>> getAuthorAndCategory(Element articleNode) {
        Elements nodes = articleNode.getElementsByTag("a");
        String category = null;
        if (nodes != null ) {
            Iterator<Element> iterator = nodes.iterator();
            while (iterator.hasNext()) {
                Element node = iterator.next();
                category = HeadingEnum.getCategoryByUrl(node.attr(HREF_ATTR));
                if (category != null) {
                    break;
                }
            }
        }
        return new AsyncResult<>(new TwoTuple<>(getElementTextByClass("show_author", articleNode), category));
    }

    /**
     *
     * @author Qirui Wang
     * @date 18/8/18 19:53
     * @usage TODO
     * @method getElementTextByClass
     * @param className
     * @param articleNode
     * @return java.lang.String
     */
    private String getElementTextByClass(String className, Element articleNode) {
        Elements nodes = articleNode.getElementsByClass(className);
        if (nodes != null) {
            return nodes.first().text();
        }
        return null;
    }
    
    /**  
     *    
     * @author Qirui Wang  
     * @date 18/8/18 20:56
     * @usage TODO
     * @method getSourceAndSourceLink  
     * @param articleNode 
     * @return com.zjs.newscrawle.pojo.TwoTuple<java.lang.String>  
     */  
    @Async
    public Future<TwoTuple<String>> getSourceAndSourceLink(Element articleNode) {
        Elements nodes = articleNode.getElementsByTag("a");
        TwoTuple<String> twoTuple = null;
        if (nodes != null) {
            Iterator<Element> iterator = nodes.iterator();
            while (iterator.hasNext()) {
                Element node = iterator.next();
                if (node.hasClass("source")) {
                    twoTuple = new TwoTuple<>(node.text(), node.attr("href"));
                }
            }
        }
        return new AsyncResult<>(twoTuple);
    }

    /**
     *
     * @author Qirui Wang
     * @date 18/8/18 20:54
     * @usage 获得统计数据
     * @method getStatics
     * @param articleNode
     * @return com.zjs.newscrawle.pojo.Statics
     */
    @Async
    public Future<Statics> getStatics(Element articleNode) {
        Elements nodes = articleNode.getElementsByTag("a");
        Statics statics = new Statics();
        if (nodes != null) {
            Iterator<Element> iterator = nodes.iterator();
            while (iterator.hasNext()) {
                Element node = iterator.next();
                if (COMMENT_NODE.equals(node.attr("node-type"))) {
                    String url = node.attr(HREF_ATTR);
                    statics = getStaticsFromCommentPage(url);
                }
            }
        }
        return new AsyncResult<>(statics);
    }

    /**
     *
     * @author Qirui Wang
     * @date 18/8/18 20:54
     * @usage
     * @method getStaticsFromCommentPage
     * @param url
     * @return com.zjs.newscrawle.pojo.Statics
     */
    private Statics getStaticsFromCommentPage(String url) {

        Statics statics = new Statics();

        try {
            Document doc = Jsoup.connect(url).get();
            Elements nodes = doc.select("span[comment-type]");
            if (nodes != null) {
                Element node = nodes.first();
                String comment = node.child(0).text();
                String interview = node.child(2).text();

                statics.setComments(Integer.parseInt(comment));
                statics.setInterview(Integer.parseInt(interview));
                statics.setHotHit(Integer.parseInt(interview));
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return statics;
    }
}
