package com.zjs.newscrawle.component;

import com.zjs.newscrawle.pojo.Page;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.Set;

/**
 * @Author: Qirui Wang
 * @Description: TODO
 * @Date: 18/8/18
 */
public interface DetailHandler {

    List<Page> detailHandler(String url, Set<Element> set) throws Exception;

}
