package com.tragepro.api.alert.core.channel;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import com.tragepro.api.domain.alert.NotificationChannelType;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/** Factory pattern resolving {@link NotificationChannel} implementations dynamically. */
@Component
public class NotificationChannelFactory {

  private final Map<NotificationChannelType, NotificationChannel> channelMap;

  public NotificationChannelFactory(List<NotificationChannel> channels) {
    this.channelMap =
        channels.stream()
            .collect(Collectors.toMap(NotificationChannel::getChannelType, Function.identity()));
  }

  public NotificationChannel getChannel(NotificationChannelType type) {
    NotificationChannel channel = channelMap.get(type);
    if (channel == null) {
      throw new AppException(ErrorType.INVALID_PARAMETER);
    }
    return channel;
  }
}
