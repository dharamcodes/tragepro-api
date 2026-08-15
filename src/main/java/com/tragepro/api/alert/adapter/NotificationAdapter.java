package com.tragepro.api.alert.adapter;

import com.tragepro.api.domain.alert.NotificationPayload;

public interface NotificationAdapter {

  void sendNotification(NotificationPayload payload);
}
