package com.tragepro.api.data.client;

import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("${data.feed.base-url}")
public interface DataFeedClient {}
