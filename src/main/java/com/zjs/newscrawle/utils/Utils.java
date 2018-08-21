package com.zjs.newscrawle.utils;

import com.zjs.newscrawle.pojo.Page;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.concurrent.Future;

/**
 * @Author: Qirui Wang
 * @Description: 工具类
 * @Date: 13/8/18
 */
public class Utils {

    private static final String HTML = "html";

    private static final String SHTML = "shtml";

    /**
     *
     * @author Qirui Wang
     * @date 13/8/18 21:05
     * @usage 判断详细页
     * @method validDetail
     * @param link
     * @return boolean
     */
    public static  boolean validDetail(Element link) {
        String url = link.attr("href");
        if (url.contains(HTML) || url.contains(SHTML)) {
            return true;
        }
        return false;
    }

    public static String getAuthor(String author) {
        if (author != null) {
            String[] array = author.split("：");
            return array[1];
        }
        return null;
    }

    /**
     *
     * @author Qirui Wang
     * @date 21/8/18 17:52
     * @usage 检测任务完成情况
     * @method checkThreadArray
     * @param list
     * @return boolean
     */
    public static boolean checkThreadArray(List<Future<List<Page>>> list) {
        for (Future<List<Page>> task : list) {
            if (!task.isDone()) {
                return false;
            }
        }
        return true;
    }

    public static class PageBuilder {
        private Page page;

        public PageBuilder() {
            page = new Page();
        }

        public void buildWebSite(String website) {
            page.setWebsite(website);
        }

        public void buildCategory(String category) {
            page.setCategory(category);
        }

        public void buildTitle(String title) {
            page.setTitle(title);
        }

        public void buildAuthor(String author) {
            page.setAuthor(author);
        }

        public void buildCreatedTime(String createdTime) {
            page.setCreatedTime(createdTime);
        }

        public void buildLink(String link) {
            page.setLink(link);
        }

        public void buildSource(String source) {
            page.setSource(source);
        }

        public void buildSourceLink(String sourceLink) {
            page.setSourceLink(sourceLink);
        }

        public void buildComments(int comments) {
            page.setComments(comments);
        }

        public void buildInterview(int interview) {
            page.setInterview(interview);
        }

        public void buildHotHits(int hotHit) {
            page.setHotHit(hotHit);
        }

        public Page buildPage() {
            return page;
        }
    }
}
