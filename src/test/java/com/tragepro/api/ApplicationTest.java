package com.tragepro.api;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;

@Slf4j
@SpringBootTest
class ApplicationTest {

    @Test
    void contextLoads() {
        ApplicationModules modules = ApplicationModules.of(Application.class);
        modules.forEach(module -> log.info(module.getDisplayName()));
        modules.verify();
    }
}
