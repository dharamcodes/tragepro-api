package com.tragepro.api.common;

import org.testcontainers.mongodb.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

public class MongoContainer {

    private static final String IMAGE_VERSION = "mongo:latest";

    public static MongoDBContainer getInstance() {
        return new MongoDBContainer(DockerImageName.parse(IMAGE_VERSION));
    }
}
