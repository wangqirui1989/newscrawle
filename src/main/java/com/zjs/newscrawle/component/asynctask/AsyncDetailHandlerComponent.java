package com.zjs.newscrawle.component.asynctask;

import com.alibaba.fastjson.JSONObject;
import com.zjs.newscrawle.config.WebConfig;
import com.zjs.newscrawle.pojo.Statics;
import com.zjs.newscrawle.pojo.TwoTuple;
import com.zjs.newscrawle.utils.HeadingEnum;
import com.zjs.newscrawle.utils.Utils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @Author: Qirui Wang
 * @Description: 异步详细网页处理模块
 * @Date: 21/8/18
 */
@Component
public class AsyncDetailHandlerComponent {

    @Resource
    private WebConfig webConfig;

    private String commentJsonUrl;

    private String agent;

    @PostConstruct
    public void init() {
        commentJsonUrl = webConfig.getCommentUrl();
        agent = webConfig.getAgent();
    }

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

        Elements h1 = articleNode.getElementsByTag("h1");
        String title = null;
        if (h1.size() != 0) {
            title = h1.first().text();
        }

        String createdTimeFromClassDate = getElementTextByClass("date", articleNode);
        String createdTime = (createdTimeFromClassDate == null) ? getElementTextByClass("time-source", articleNode) : createdTimeFromClassDate;

        return new AsyncResult<>(new TwoTuple<>(title, createdTime));
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
        String author = getElementTextByClass("show_author", articleNode);
        String editor = getElementTextByClass("article-editor", articleNode);

        String resultAuthor = (author == null) ? editor : author;

        return new AsyncResult<>(new TwoTuple<>(resultAuthor, getCategory(articleNode)));
    }

    /**
     *
     * @author Qirui Wang
     * @date 22/8/18 16:47
     * @usage 获得类别
     * @method getCategory
     * @param articleNode
     * @return java.lang.String
     */
    private String getCategory(Element articleNode) {
        String category = getCategoryFromNodes(articleNode.getElementsByClass("channel-path"));

        if (category == null) {
            category = getCategoryFromNodes(articleNode.getElementsByClass("bread"));
        }

        return category;
    }

    /**
     *
     * @author Qirui Wang
     * @date 22/8/18 23:25
     * @usage 从节点获取分类
     * @method getCategoryFromNodes
     * @param nodes
     * @return java.lang.String
     */
    private String getCategoryFromNodes(Elements nodes) {
        String category = null;
        if (nodes.size() == 1) {
            category = nodes.first().child(0).text();
        }
        return category;
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
                } else if (node.parent().hasAttr("data-sudaclick")) {
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
    public Future<Statics> getStatics(Element articleNode, String originUrl) {
        Statics statics = new Statics();
        String category = getCategory(articleNode);
        if (category != null) {
            if (originUrl.contains("doc")) {
                getStaticsFromCommentPage(category, statics, originUrl);
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
     * @param category
     * @param statics
     * @return com.zjs.newscrawle.pojo.Statics
     */
    @SuppressWarnings("unchecked")
    private void getStaticsFromCommentPage(String category, Statics statics, String url) {

            StringBuilder stringBuilder = new StringBuilder();
            String id = Utils.extractNewsId(url);
            String categoryCode = HeadingEnum.getCodeByCategory(category);

            if (categoryCode != null) {
                String commentUrl = stringBuilder.append(commentJsonUrl)
                        .append("channel=")
                        .append(categoryCode)
                        .append("&newsid=comos-")
                        .append(id).toString();

                String doc = null;
                try {
                    doc = Jsoup.connect(commentUrl).timeout(50000).userAgent(agent).ignoreContentType(true).execute().body();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }

                JSONObject jsonObject = JSONObject.parseObject(doc);

                Map<String, Object> map = (Map<String, Object>) jsonObject;

                Map<String, Object> countMap = (Map<String, Object>) ((Map<String, Object>) map.get("result")).get("count");
                // 参与
                String total = countMap.get("total").toString();

                // 评论
                String comment = countMap.get("show").toString();

                // 热点数
                String hotHit = countMap.get("qreply").toString();

                statics.setHotHit(Integer.parseInt(hotHit));
                statics.setComments(Integer.parseInt(comment));
                statics.setInterview(Integer.parseInt(total));
            }

    }

}
