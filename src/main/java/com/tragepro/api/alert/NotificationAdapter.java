package com.tragepro.api.alert;

import com.tragepro.api.alert.model.NotificationPayload;

public interface NotificationAdapter {

  void sendNotification(NotificationPayload payload);
}
