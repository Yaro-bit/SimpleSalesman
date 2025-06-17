package com.simplesalesman.config;

import java.util.concurrent.Executor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AsyncConfig.class)
class AsyncConfigTest {

    /** Inject the exact bean defined in AsyncConfig */
    @Autowired
    @Qualifier("taskExecutor")
    private Executor taskExecutor;

    /** Expected values – keep in sync with AsyncConfig defaults */
    @Value("${app.async.core:4}")
    private int expectedCorePoolSize;

    @Value("${app.async.max:8}")
    private int expectedMaxPoolSize;

    @Value("${app.async.queue:100}")
    private int expectedQueueCapacity;

    @Test
    void verifiesThreadPoolSettings() {
        // Ensure the bean is the correct type
        assertThat(taskExecutor).isInstanceOf(ThreadPoolTaskExecutor.class);

        ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) taskExecutor;

        // Validate individual settings
        assertThat(executor.getCorePoolSize()).isEqualTo(expectedCorePoolSize);
        assertThat(executor.getMaxPoolSize()).isEqualTo(expectedMaxPoolSize);
        assertThat(executor.getQueueCapacity()).isEqualTo(expectedQueueCapacity);
        assertThat(executor.getThreadNamePrefix()).isEqualTo("SimpleSalesman-Async-");
    }
}
