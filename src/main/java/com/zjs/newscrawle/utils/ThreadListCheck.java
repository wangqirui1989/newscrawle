package com.zjs.newscrawle.utils;

import java.util.List;
import java.util.concurrent.Future;

/**
 * @Author: Qirui Wang
 * @Description: 线程检测接口
 * @Date: 4/9/18
 */
public interface ThreadListCheck<E> {

    boolean isDone(List<Future<E>> list);
}
