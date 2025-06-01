package com.simplesalesman.config;

import java.util.concurrent.Executor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import static org.assertj.core.api.Assertions.assertThat; // <-- Hinzufügen!

@SpringBootTest
class AsyncConfigTest {

    @Autowired
    private Executor taskExecutor;

    @Test
    void testTaskExecutorBeanConfiguration() {
        // Prüft, ob der taskExecutor-Bean korrekt vom Typ ThreadPoolTaskExecutor ist
        assertThat(taskExecutor).isInstanceOf(ThreadPoolTaskExecutor.class);

        // Cast, um spezifische Konfiguration zu prüfen
        ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) taskExecutor;

        // Prüft, ob corePoolSize richtig gesetzt ist
        assertThat(executor.getCorePoolSize()).isEqualTo(4);

        // Prüft, ob maxPoolSize richtig gesetzt ist
        assertThat(executor.getMaxPoolSize()).isEqualTo(8);

        // Prüft, ob queueCapacity richtig gesetzt ist
        assertThat(executor.getQueueCapacity()).isEqualTo(100);

        // Prüft, ob threadNamePrefix richtig gesetzt ist
        assertThat(executor.getThreadNamePrefix()).isEqualTo("SimpleSalesman-Async-");
    }
}
