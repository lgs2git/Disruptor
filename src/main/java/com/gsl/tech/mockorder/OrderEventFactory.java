package com.gsl.tech.mockorder;

import com.lmax.disruptor.EventFactory;

/**
 * @description: 定义如何创建事件
 * @date 2021/5/23 11:50
 */
public class OrderEventFactory implements EventFactory<OrderEvent> {

    @Override
    public OrderEvent newInstance() {
        return new OrderEvent();
    }
}
