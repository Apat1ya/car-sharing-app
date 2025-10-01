package mate.carsharingapp.service.telegram;

import java.util.Optional;
import lombok.SneakyThrows;
import mate.carsharingapp.model.user.User;
import mate.carsharingapp.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
public class UpdateConsumer implements LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;
    private final UserRepository userRepository;

    public UpdateConsumer(UserRepository userRepository,
                          @Value("${telegram.bot.token}") String telegramBotToken) {
        this.userRepository = userRepository;
        this.telegramClient = new OkHttpTelegramClient(telegramBotToken);
    }

    @SneakyThrows
    @Override
    public void consume(Update update) {
        if (update.hasMessage()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            if (messageText.equals("/start")) {
                SendMessage message = SendMessage.builder()
                        .text("Hello, to sign in, enter your email.(for example: name@gmail.com).")
                        .chatId(chatId)
                        .build();
                telegramClient.execute(message);
            } else if (messageText.contains("@")) {
                Optional<User> optionalUser = userRepository.findByEmail(messageText);
                if (optionalUser.isPresent()) {
                    User user = optionalUser.get();
                    user.setTelegramChatId(chatId);
                    userRepository.save(user);

                    SendMessage message = SendMessage.builder()
                            .text("Your email has been successfully linked.")
                            .chatId(chatId)
                            .build();
                    telegramClient.execute(message);
                } else {
                    SendMessage message = SendMessage.builder()
                            .text("Your email is not registered")
                            .chatId(chatId)
                            .build();
                    telegramClient.execute(message);
                }
            } else {
                SendMessage message = SendMessage.builder()
                        .text("I don't understand you.")
                        .chatId(chatId)
                        .build();
                telegramClient.execute(message);
            }

        }
    }
}
