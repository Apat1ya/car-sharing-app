package mate.carsharingapp.config;

import mate.carsharingapp.service.telegram.notification.TelegramNotificationImpl;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {
    @Bean
    public TelegramNotificationImpl telegramNotification() {
        return Mockito.mock(TelegramNotificationImpl.class);
    }
}
