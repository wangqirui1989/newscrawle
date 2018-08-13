package com.zjs.newscrawle.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: Qirui Wang
 * @Description: 链接过滤器
 * @Date: 13/8/18
 */
public class LinkFilterImpl implements LinkFilter {

    private Pattern listLinkPattern;

    public LinkFilterImpl(String pattern) {
        this.listLinkPattern = Pattern.compile(pattern);
    }

    @Override
    public boolean valid(String linkHref) {
        if (linkHref == null) {
            return false;
        }

        Matcher matcher = listLinkPattern.matcher(linkHref);

        if (matcher.find()) {
            return true;
        }

        return false;
    }
}
