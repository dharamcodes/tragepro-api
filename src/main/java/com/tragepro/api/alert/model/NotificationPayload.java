package com.tragepro.api.alert.model;

import lombok.Builder;

@Builder
public record NotificationPayload(
    String recipient, String subject, String message, NotificationChannelType channelType) {}
