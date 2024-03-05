package com.gsl.tech.mockorder.impl;

import com.gsl.tech.mockorder.ConsumeModeService;
import com.gsl.tech.mockorder.MailEventHandler;
import com.gsl.tech.mockorder.MailWorkHandler;
import org.springframework.stereotype.Service;

/**
 * @description: C1和C2独立消费，C3和C4是共同消费，但C3和C4都依赖C1和C2，然后C5依赖C3和C4
 * @date 2021/5/23 11:05
 */
@Service("scene10")
public class Scene10 extends ConsumeModeService {

    @Override
    protected void disruptorOperate() {
        MailEventHandler c1 = new MailEventHandler(eventCountPrinter);
        MailEventHandler c2 = new MailEventHandler(eventCountPrinter);
        MailWorkHandler c3 = new MailWorkHandler(eventCountPrinter);
        MailWorkHandler c4 = new MailWorkHandler(eventCountPrinter);
        MailEventHandler c5 = new MailEventHandler(eventCountPrinter);

        disruptor
                // C1和C2独立消费
                .handleEventsWith(c1, c2)
                // C3和C4是共同消费，但C3和C4都依赖C1和C2
                .thenHandleEventsWithWorkerPool(c3, c4)
                // 然后C5依赖C3和C4
                .then(c5);
    }
}
