package com.gsl.tech.basic;

public interface BasicEventService {

    /**
     * 发布一个事件
     */
    void publish(String value);

    /**
     * 发布一个事件，方式2
     */
    void publishEvent(String value);

    /**
     * 返回已经处理的任务总数
     */
    long eventCount();
}
