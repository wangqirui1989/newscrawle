package com.zjs.newscrawle.pojo;

/**
 * @Author: Qirui Wang
 * @Description: 两元祖
 * @Date: 13/8/18
 */
public class TwoTuple<E> {

    private E firstIndex;

    private E secondIndex;

    public TwoTuple(E firstIndex, E secondIndex) {
        this.firstIndex = firstIndex;
        this.secondIndex = secondIndex;
    }

    public E getFirstIndex() {
        return firstIndex;
    }

    public void setFirstIndex(E firstIndex) {
        this.firstIndex = firstIndex;
    }

    public E getSecondIndex() {
        return secondIndex;
    }

    public void setSecondIndex(E secondIndex) {
        this.secondIndex = secondIndex;
    }
}
