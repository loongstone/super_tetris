package com.loongstone.supertetris.view;

import java.util.LinkedList;

/**
 * @author loongstone
 * @date 2017/11/30
 */

public class SimpleListQueue<E> extends LinkedList<E> implements ListQueue<E> {
    @Override
    public boolean add(E e) {
        return super.add(e);
    }

    @Override
    public E removeFirst() {
        return super.removeFirst();
    }

    @Override
    public E getFromFirst(int index) {
        return super.get(index);
    }
}
