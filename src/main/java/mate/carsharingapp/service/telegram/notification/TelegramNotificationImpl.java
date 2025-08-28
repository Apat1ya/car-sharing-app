package mate.carsharingapp.service.telegram.notification;

import java.time.LocalDate;
import lombok.SneakyThrows;
import mate.carsharingapp.model.rental.Rental;
import mate.carsharingapp.model.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Service
public class TelegramNotificationImpl implements TelegramNotificationService {
    private final TelegramClient telegramClient;

    public TelegramNotificationImpl(@Value("${telegram.bot.token}") String botToken) {
        this.telegramClient = new OkHttpTelegramClient(botToken);
    }

    @SneakyThrows
    @Override
    public void sendNotification(User user, String message) {
        Long chatId = user.getTelegramChatId();

        if (chatId == null) {
            System.out.println("User" + user.getEmail() + " has no Telegram chatId.");
            return;
        }
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .build();
        telegramClient.execute(sendMessage);
    }

    @Override
    public void sentNotificationCreateRental(Rental rental, User user, String car) {
        String message = "✅ Your rental # " + rental.getId() + " for the car " + car
                + "has been successfully created!";
        sendNotification(user,message);
    }

    @Override
    public void sentNotificationReturnedRental(Rental rental, User user) {
        String message = "✅ Your rental #" + rental.getId()
                + " has been successfully completed.";
        sendNotification(user,message);
    }

    @Override
    public void sendNotificationOverdueRental(Rental rental, User user, LocalDate date) {
        String message = "⚠ Your rental #" + rental.getId()
                + " for the car is overdue! Return date: "
                + date;
        sendNotification(user,message);
    }
}
