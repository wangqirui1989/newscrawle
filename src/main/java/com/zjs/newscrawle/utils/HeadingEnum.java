package com.zjs.newscrawle.utils;

import org.jsoup.nodes.Element;

/**
 * @Author: Qirui Wang
 * @Description: 标题枚举
 * @Date: 13/8/18
 */
public enum HeadingEnum {

    /**
     * 标题枚举内容
     */
    CHINA("http://news.sina.com.cn/china/"),

    INTERNATIONAL("http://news.sina.com.cn/world/"),

    SOCIETY("http://news.sina.com.cn/society/"),

    MILITRY("http://mil.news.sina.com.cn/"),

    OPINION("http://news.sina.com.cn/opinion/"),

    GOVERMENT("http://news.sina.com.cn/gov/"),

    CULTURE("http://cul.news.sina.com.cn/"),

    SPORTS("http://sports.sina.com.cn/体育"),

    ENTERTAINMENT("http://ent.sina.com.cn/娱乐"),

    FINANCE("http://finance.sina.com.cn/"),

    TECH("http://tech.sina.com.cn/"),

    ;

    private String url;

    HeadingEnum(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public static boolean isValid(Element link) {
        String href = link.attr("href");
        HeadingEnum[] headingEnums = HeadingEnum.values();
        for (int i = 0; i < headingEnums.length; i++) {
            if (href.equals(headingEnums[i].getUrl())) {
                return true;
            }
        }
        return false;
    }
}
