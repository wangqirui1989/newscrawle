package com.zjs.newscrawle.component.asynctask;

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
import java.util.Iterator;
import java.util.concurrent.Future;

/**
 * @Author: Qirui Wang
 * @Description: 异步详细网页处理模块
 * @Date: 21/8/18
 */
@Component
public class AsyncDetailHandlerComponent {

    /**
     * 评论节点标志
     */
    private static final String COMMENT_NODE = "comment";

    private static final String HREF_ATTR = "href";

    /**
     *
     * @author Qirui Wang
     * @date 18/8/18 20:56
     * @usage 获得标题和创建时间
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
     * @usage 获得作者和类别
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
     * @usage 抓取指定的css
     * @method getElementTextByClass
     * @param className
     * @param articleNode
     * @return java.lang.String
     */
    private String getElementTextByClass(String className, Element articleNode) {
        Elements nodes = articleNode.getElementsByClass(className);
        if (nodes.size() != 0) {
            return nodes.first().text();
        }
        return null;
    }

    /**
     *
     * @author Qirui Wang
     * @date 18/8/18 20:56
     * @usage 获得来源和源连接
     * @method getSourceAndSourceLink
     * @param articleNode
     * @return com.zjs.newscrawle.pojo.TwoTuple<java.lang.String>
     */
    @Async
    public Future<TwoTuple<String>> getSourceAndSourceLink(Element articleNode) {
        Elements nodes = articleNode.getElementsByTag("a");
        TwoTuple<String> twoTuple = new TwoTuple<>();
        if (nodes.size() != 0) {
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
        Element node = articleNode.attr("node-type", COMMENT_NODE);
        Statics statics = new Statics();
        if (node != null) {
            String url = node.attr(HREF_ATTR);
            if (url != null && url.length() != 0) {
                statics = getStaticsFromCommentPage(url);
            }
        }
        return new AsyncResult<>(statics);
    }

    /**
     *
     * @author Qirui Wang
     * @date 18/8/18 20:54
     * @usage 从评论页中获取指定数据
     * @method getStaticsFromCommentPage
     * @param url
     * @return com.zjs.newscrawle.pojo.Statics
     */
    private Statics getStaticsFromCommentPage(String url) {

        Statics statics = new Statics();

        try {
            Document doc = Jsoup.connect(url).get();
            Element node = doc.attr("comment-type", "count");
            if (node != null) {
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
