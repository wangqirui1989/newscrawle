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
    CHINA("http://news.sina.com.cn/china/", "国内新闻"),

    INTERNATIONAL("http://news.sina.com.cn/world/", "国际新闻"),

    SOCIETY("http://news.sina.com.cn/society/", "社会新闻"),

    MILITRY("http://mil.news.sina.com.cn/", "军事新闻"),

    OPINION("http://news.sina.com.cn/opinion/", "评论"),

    GOVERMENT("http://news.sina.com.cn/gov/", "政务新闻"),

    CULTURE("http://cul.news.sina.com.cn/", "文化新闻"),

    SPORTS("http://sports.sina.com.cn/体育", "体育新闻"),

    ENTERTAINMENT("http://ent.sina.com.cn/娱乐", "娱乐新闻"),

    FINANCE("http://finance.sina.com.cn/", "财经新闻"),

    TECH("http://tech.sina.com.cn/", "科技新闻"),

    ;

    private String url;
    private String category;

    HeadingEnum(String url, String category) {
        this.url = url;
        this.category = category;
    }

    public String getUrl() {
        return url;
    }

    public String getCategory() {
        return category;
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

    public static String getCategoryByUrl(String url) {
        HeadingEnum[] headingEnums = HeadingEnum.values();
        for (int i = 0; i < headingEnums.length; i++) {
            if (url.equals(headingEnums[i].getUrl())) {
                return headingEnums[i].getCategory();
            }
        }
        return null;
    }
}
