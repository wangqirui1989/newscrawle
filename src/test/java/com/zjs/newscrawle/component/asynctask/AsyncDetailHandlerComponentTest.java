package com.zjs.newscrawle.component.asynctask;

import com.zjs.newscrawle.utils.Utils;
import org.junit.Test;

/**
 * @Author: Qirui Wang
 * @Description: TODO
 * @Date: 22/8/18
 */
public class AsyncDetailHandlerComponentTest {

    @Test
    public void extractNewsId() {
        System.out.println(Utils.extractNewsId("http://news.sina.com.cn/gov/xlxw/2018-08-22/doc-ihhzsnec0345803.html"));
    }
}