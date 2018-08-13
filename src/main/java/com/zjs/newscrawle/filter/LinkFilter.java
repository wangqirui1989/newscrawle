package com.zjs.newscrawle.filter;

/**
 * @Author: Qirui Wang
 * @Description: 链接过滤接口
 * @Date: 13/8/18
 */
public interface LinkFilter {

    boolean valid(String linkHref);

}
