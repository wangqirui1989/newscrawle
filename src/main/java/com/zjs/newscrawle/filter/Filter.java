package com.zjs.newscrawle.filter;

import com.zjs.newscrawle.utils.UtilsConstant;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: Qirui Wang
 * @Description: 链接过滤器
 * @Date: 22/8/18
 */
@Component
public class Filter {

    private Pattern linkPattern;

    public void setPattern(String pattern) {
        linkPattern = Pattern.compile(pattern);
    }

    private boolean valid(String url) {
        if (url == null) {
            return false;
        }

        Matcher matcher = linkPattern.matcher(url);

        if (matcher.find()) {
            return true;
        }

        return false;
    }

    public boolean isValid(String url) {
        for(String str : UtilsConstant.FILTER_ARRAY) {
            setPattern(str);
            if (valid(url)) {
                return true;
            }
        }

        return false;
    }
}
