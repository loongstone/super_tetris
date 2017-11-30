package com.loongstone.supertetris.view;

/**
 * 存储方块的类,具有简单队列的性质,可以插入队尾,取出对首.并增添了List的特性,可以取从队首开始索引的数据
 *
 * @author loongstone
 * @date 2017/11/30
 */

public interface ListQueue<E> {
    /**
     * 添加元素到队首
     *
     * @param e
     * @return 插入成功
     */
    boolean add(E e);

    /**
     * 返回元素,并从队列中删除
     *
     * @return
     */
    E removeFirst();

    /**
     * 从队列开始索引元素并返回
     *
     * @param index 索引,从队首0开始
     * @return 从队列开始索引的元素
     */
    E getFromFirst(int index);

    /**
     * @return 队列是否为空
     */
    boolean isEmpty();
}
