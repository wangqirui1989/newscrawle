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
    CHINA("http://news.sina.com.cn/china/", "国内新闻", "gn"),

    INTERNATIONAL("http://news.sina.com.cn/world/", "国际新闻", "gj"),

    SOCIETY("http://news.sina.com.cn/society/", "社会新闻", "sh"),

    MILITRY("http://mil.news.sina.com.cn/", "军事新闻", "jc"),

    OPINION("http://news.sina.com.cn/opinion/", "评论", "pl"),

    GOVERMENT("http://news.sina.com.cn/gov/", "新浪政务", "gn"),

    CULTURE("http://cul.news.sina.com.cn/", "文化新闻", "sh"),

    SPORTS("http://sports.sina.com.cn/体育", "体育新闻", "ty"),

    ENTERTAINMENT("http://ent.sina.com.cn/娱乐", "娱乐新闻", "yl"),

    FINANCE("http://finance.sina.com.cn/", "财经新闻", "cj"),

    TECH("http://tech.sina.com.cn/", "科技新闻", "kj"),

    ;

    private String url;
    private String category;
    private String code;

    HeadingEnum(String url, String category, String code) {
        this.url = url;
        this.category = category;
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public String getCategory() {
        return category;
    }

    public String getCode() {
        return code;
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

    public static String getCodeByCategory(String category) {
        HeadingEnum[] headingEnums = HeadingEnum.values();
        for (int i = 0; i < headingEnums.length; i++) {
            if (category.equals(headingEnums[i].getCategory())) {
                return headingEnums[i].getCode();
            }
        }
        return null;
    }
}
