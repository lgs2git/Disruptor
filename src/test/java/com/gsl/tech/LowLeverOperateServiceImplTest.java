package com.gsl.tech;

import com.gsl.tech.lowleveloperate.LowLevelOperateService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertEquals;


/**
 * 测试环形队列 基本使用
 * 测试lowleveloperate包下
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class LowLeverOperateServiceImplTest {

    @Autowired
    @Qualifier("oneConsumer")
    LowLevelOperateService oneConsumer;

    @Autowired
    @Qualifier("multiConsumer")
    LowLevelOperateService multiConsumer;

    @Autowired
    @Qualifier("workerPoolConsumer")
    LowLevelOperateService workerPoolConsumer;

    private static final int EVENT_COUNT = 100;

    private void testLowLevelOperateService(LowLevelOperateService service, int eventCount, int expectEventCount) throws InterruptedException {
        for (int i = 0; i < eventCount; i++) {
            log.info("publich {}", i);
            service.publish(String.valueOf(i));
        }

        // 异步消费，因此需要延时等待
        log.info("暂停10s");
        Thread.sleep(10000);

        // 消费的事件总数应该等于发布的事件数
        assertEquals(expectEventCount, service.eventCount());
    }

    /**
     * 测试100个事件，单个消费者消费
     * @throws InterruptedException
     */
    @Test
    public void testOneConsumer() throws InterruptedException {
        log.info("start testOneConsumerService");
        testLowLevelOperateService(oneConsumer, EVENT_COUNT, EVENT_COUNT);
    }

    /**
     * 100个事件，三个消费者，每个都独自消费这个100个事件
     * @throws InterruptedException
     */
    @Test
    public void testMultiConsumer() throws InterruptedException {
        log.info("start testMultiConsumer");
        testLowLevelOperateService(multiConsumer, EVENT_COUNT, EVENT_COUNT * LowLevelOperateService.CONSUMER_NUM);
    }


    /**
     * 100个事件，三个消费者共同消费这个100个事件
     * @throws InterruptedException
     */
    @Test
    public void testWorkerPoolConsumer() throws InterruptedException {
        log.info("start testWorkerPoolConsumer");
        testLowLevelOperateService(workerPoolConsumer, EVENT_COUNT, EVENT_COUNT);
    }

}
