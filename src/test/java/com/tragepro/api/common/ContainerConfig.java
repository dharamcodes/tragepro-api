package com.tragepro.api.common;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.mongodb.MongoDBContainer;

public class ContainerConfig {

    @Container
    @ServiceConnection
    protected static MongoDBContainer mongo = new MongoDBContainer("mongo:6.0").withReuse(true);

    @BeforeAll
    static void init() {
        mongo.start();
    }
}
