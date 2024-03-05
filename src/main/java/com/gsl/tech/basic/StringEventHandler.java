package com.gsl.tech.basic;

import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

@Slf4j
public class StringEventHandler implements EventHandler<StringEvent> {

    private final Consumer<?> consumer;

    public StringEventHandler(Consumer<?> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void onEvent(StringEvent stringEvent, long l, boolean b) throws Exception {
        log.info("stringEvent:[{}],[{}],[{}]",stringEvent,l,b);
        Thread.sleep(10);
        if (null!=stringEvent){
            log.info("方法onEvent：accept()");
            consumer.accept(null);
        }
    }
}
