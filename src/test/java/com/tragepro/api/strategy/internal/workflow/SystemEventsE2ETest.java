package com.tragepro.api.strategy.internal.workflow;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.tragepro.api.alert.AlertEvent;
import com.tragepro.api.alert.internal.event.AlertEventListener;
import com.tragepro.api.alert.internal.event.AlertEventPublisher;
import com.tragepro.api.common.ContainerConfig;
import com.tragepro.api.datafeed.DatafeedEvent;
import com.tragepro.api.datafeed.internal.event.DatafeedEventListener;
import com.tragepro.api.datafeed.internal.event.DatafeedEventPublisher;
import com.tragepro.api.identity.IdentityEvent;
import com.tragepro.api.identity.internal.event.IdentityEventListener;
import com.tragepro.api.identity.internal.event.IdentityEventPublisher;
import com.tragepro.api.journal.JournalEvent;
import com.tragepro.api.journal.internal.event.JournalEventListener;
import com.tragepro.api.journal.internal.event.JournalEventPublisher;
import com.tragepro.api.strategy.StrategyEvent;
import com.tragepro.api.strategy.internal.event.StrategyEventListener;
import com.tragepro.api.strategy.internal.event.StrategyEventPublisher;
import com.tragepro.api.trading.TradingEvent;
import com.tragepro.api.trading.internal.event.TradingEventListener;
import com.tragepro.api.trading.internal.event.TradingEventPublisher;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest
@ActiveProfiles("test")
class SystemEventsE2ETest extends ContainerConfig {

  @Autowired private AlertEventPublisher alertPublisher;
  @Autowired private AlertEventListener alertListener;

  @Autowired private DatafeedEventPublisher dataPublisher;
  @Autowired private DatafeedEventListener dataListener;

  @Autowired private IdentityEventPublisher identityPublisher;
  @Autowired private IdentityEventListener identityListener;

  @Autowired private JournalEventPublisher journalPublisher;
  @Autowired private JournalEventListener journalListener;

  @Autowired private StrategyEventPublisher strategyPublisher;
  @Autowired private StrategyEventListener strategyListener;

  @Autowired private TradingEventPublisher tradingPublisher;
  @Autowired private TradingEventListener tradingListener;

  @Autowired private PlatformTransactionManager transactionManager;

  @Test
  @DisplayName("End-to-End Test: All System Module Event Publishers and Listeners")
  void testAllModuleEvents_EndToEnd() {
    TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
    transactionTemplate.executeWithoutResult(
        status -> {
          AlertEvent alertEvt = new AlertEvent("a1", "alert message");
          assertDoesNotThrow(() -> alertPublisher.publish(alertEvt));
          assertDoesNotThrow(() -> alertListener.on(alertEvt));

          DatafeedEvent dataEvt = new DatafeedEvent("d1", List.of());
          assertDoesNotThrow(() -> dataPublisher.publish(dataEvt));
          assertDoesNotThrow(() -> dataListener.on(dataEvt));

          IdentityEvent identityEvt = new IdentityEvent("i1", "identity message");
          assertDoesNotThrow(() -> identityPublisher.publish(identityEvt));
          assertDoesNotThrow(() -> identityListener.on(identityEvt));

          JournalEvent journalEvt = new JournalEvent("j1", "journal message");
          assertDoesNotThrow(() -> journalPublisher.publish(journalEvt));
          assertDoesNotThrow(() -> journalListener.on(journalEvt));

          StrategyEvent strategyEvt = new StrategyEvent("s1", "strategy message");
          assertDoesNotThrow(() -> strategyPublisher.publish(strategyEvt));
          assertDoesNotThrow(() -> strategyListener.on(strategyEvt));

          TradingEvent tradingEvt = new TradingEvent("t1", "trading message");
          assertDoesNotThrow(() -> tradingPublisher.publish(tradingEvt));
          assertDoesNotThrow(() -> tradingListener.on(tradingEvt));
        });
  }
}
