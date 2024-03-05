package com.gsl.tech.mockorder.impl;

import com.gsl.tech.mockorder.ConsumeModeService;
import com.gsl.tech.mockorder.MailWorkHandler;
import org.springframework.stereotype.Service;

/**
 * @description: C1和C2共同消费，C3和C4也是共同消费，但C3和C4都依赖C1和C2，然后C5依赖C3和C4
 * @date 2021/5/23 11:05
 */
@Service("scene8")
public class Scene8 extends ConsumeModeService {

    @Override
    protected void disruptorOperate() {
        MailWorkHandler c1 = new MailWorkHandler(eventCountPrinter);
        MailWorkHandler c2 = new MailWorkHandler(eventCountPrinter);
        MailWorkHandler c3 = new MailWorkHandler(eventCountPrinter);
        MailWorkHandler c4 = new MailWorkHandler(eventCountPrinter);
        MailWorkHandler c5 = new MailWorkHandler(eventCountPrinter);

        disruptor
                // C1和C2共同消费
                .handleEventsWithWorkerPool(c1, c2)
                // C3和C4也是共同消费，但C3和C4都依赖C1和C2
                .thenHandleEventsWithWorkerPool(c3, c4)
                // 然后C5依赖C3和C4
                .thenHandleEventsWithWorkerPool(c5);
    }
}
