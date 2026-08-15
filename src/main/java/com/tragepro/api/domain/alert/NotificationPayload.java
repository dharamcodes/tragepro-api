package com.tragepro.api.domain.alert;

import lombok.Builder;

@Builder
public record NotificationPayload(
    String recipient, String subject, String message, NotificationChannelType channelType) {}
