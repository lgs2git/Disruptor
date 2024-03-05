package com.gsl.tech.mockorder.impl;

import com.gsl.tech.mockorder.ConsumeModeService;
import com.gsl.tech.mockorder.MailEventHandler;
import org.springframework.stereotype.Service;

/**
 * @description: C1独立消费，C2和C3也独立消费，但依赖C1，C4依赖C2和C3
 */
@Service("scene6")
public class Scene6 extends ConsumeModeService {

    @Override
    protected void disruptorOperate() {
        MailEventHandler c1 = new MailEventHandler(eventCountPrinter);
        MailEventHandler c2 = new MailEventHandler(eventCountPrinter);
        MailEventHandler c3 = new MailEventHandler(eventCountPrinter);
        MailEventHandler c4 = new MailEventHandler(eventCountPrinter);

        disruptor
                // C1
                .handleEventsWith(c1)
                // C2和C3也独立消费
                .then(c2, c3)
                // C4依赖C2和C3
                .then(c4);
    }
}
