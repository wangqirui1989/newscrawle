package com.zjs.newscrawle.utils;

import org.jsoup.nodes.Element;

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
}
