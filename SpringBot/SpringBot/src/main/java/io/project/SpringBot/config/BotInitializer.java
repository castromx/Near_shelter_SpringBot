package io.project.SpringBot.config; // Пакет, в якому знаходиться клас BotInitializer.

import io.project.SpringBot.service.TelegramBot; // Для доступу до бота Telegram.
import org.springframework.beans.factory.annotation.Autowired; // Для Імпорту залежностей.
import org.springframework.context.event.ContextRefreshedEvent; // Для обробки подій в Spring контексті.
import org.springframework.context.event.EventListener; // Для підписки на події.
import org.springframework.stereotype.Component; // Для позначення класу як компонент Spring.
import org.telegram.telegrambots.meta.TelegramBotsApi; // Для роботи з API телеграм-ботів.
import org.telegram.telegrambots.meta.exceptions.TelegramApiException; // Для обробки винятків Telegram API.
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession; // Для створення сесії бота.

@Component // Позначаємо цей клас як компонент Spring для автоматичного сканування та імпорту його в контекст додатку.
public class BotInitializer { // Оголошення класу BotInitializer.

    @Autowired // Анотація, що позначає поле bot як залежність, яку необхідно імпортувати Spring.
    TelegramBot bot; // Екземпляр класу TelegramBot для обробки повідомлень в Telegram.

    @EventListener({ContextRefreshedEvent.class}) // Позначає метод як слухача події ContextRefreshedEvent, яка виникає після ініціалізації Spring контексту.
    public void init() throws TelegramApiException { // Оголошення методу init(), який ініціалізує телеграм-бота при запуску додатку.

        // Створюємо об'єкт TelegramBotsApi, який використовується для реєстрації бота.
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

        try {
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            // Обробляємо виняток, якщо реєстрація бота не вдалася.
            e.printStackTrace();
        }
    }
}
