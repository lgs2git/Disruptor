package com.gsl.tech.basic;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

public class StringEventProducer {
    // 存储数据的环形队列
    private final RingBuffer<StringEvent> ringBuffer;

    public StringEventProducer(RingBuffer<StringEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(String content) {
        // ringBuffer是个队列，其next方法返回的是下最后一条记录之后的位置，这是个可用位置
        long sequence = ringBuffer.next();

        try {
            // sequence位置取出的事件是空事件
            StringEvent stringEvent = ringBuffer.get(sequence);
            // 空事件添加业务信息
            stringEvent.setValue(content);
        } finally {
            // 发布
            ringBuffer.publish(sequence);
        }
    }

    /**
     * 等同于onData方式
     */
    public void onDataByLambda(String content){
        ringBuffer.publishEvent((event, sequence, args)-> event.setValue(content));
        //等同如上
//        ringBuffer.publishEvent((event,sequence,args)->{event.setValue(args);}, content);
    }
}
