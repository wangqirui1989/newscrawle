package com.zjs.newscrawle.pojo;

import java.io.Serializable;

/**
 * @Author: Qirui Wang
 * @Description: 新闻类别
 * @Date: 18/8/18
 */
public class Page extends Statics implements Serializable {

    private String website;

    private String category;

    private String title;

    private String createdTime;

    private String link;

    private String author;

    private String source;

    private String sourceLink;

    public Page() {

    }


    public void setWebsite(String website) {
        this.website = website;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setSourceLink(String sourceLink) {
        this.sourceLink = sourceLink;
    }
}
