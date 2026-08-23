package com.tragepro.api.alert.core.event;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import com.tragepro.api.common.ContainerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest
@ActiveProfiles("test")
class AlertEventSystemTest extends ContainerConfig {

    @Autowired
    private AlertEventPublisher publisher;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @MockitoSpyBean
    private AlertEventListener listener;

    @Test
    void testPublishAndListen() {
        AlertEvent event = new AlertEvent("test-id", "test-message");

        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.executeWithoutResult(status -> {
            publisher.publish(event);
        });

        verify(listener, timeout(5000)).on(event);
    }
}
