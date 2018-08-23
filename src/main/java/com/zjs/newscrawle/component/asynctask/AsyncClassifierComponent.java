package com.zjs.newscrawle.component.asynctask;

import com.zjs.newscrawle.utils.HeadingEnum;
import com.zjs.newscrawle.utils.Utils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Future;

/**
 * @Author: Qirui Wang
 * @Description: 异步分类
 * @Date: 21/8/18
 */
@Component
public class AsyncClassifierComponent {

    /**
     *
     * @author Qirui Wang
     * @date 13/8/18 21:25
     * @usage 获得详细链接
     * @method getDetailLinks
     * @param
     * @return java.util.Set<org.jsoup.nodes.Element>
     */
    @Async
    public Future<Set<Element>> getDetailLinks(Elements links) {
        Set<Element> detailLinkSet = new HashSet<>();
        for (Element link : links) {
            if (Utils.validDetail(link)) {
                detailLinkSet.add(link);
            }
        }
        return new AsyncResult<>(detailLinkSet);
    }

    /**
     *
     * @author Qirui Wang
     * @date 13/8/18 21:44
     * @usage 获得指定标题链接
     * @method getHeadingLinks
     * @param
     * @return java.util.concurrent.Future<java.util.Set<org.jsoup.nodes.Element>>
     */
    @Async
    public Future<Set<Element>> getHeadingLinks(Elements links) {
        Set<Element> headLinkSet = new HashSet<>();
        for (Element link : links) {
            if (HeadingEnum.isValid(link)) {
                headLinkSet.add(link);
            }
        }

        return new AsyncResult<>(headLinkSet);
    }
}
