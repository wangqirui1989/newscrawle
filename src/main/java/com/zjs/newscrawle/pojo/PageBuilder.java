package com.zjs.newscrawle.pojo;

/**
 * @Author: Qirui Wang
 * @Description: 建造Page类
 * @Date: 18/8/18
 */
public class PageBuilder {

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
