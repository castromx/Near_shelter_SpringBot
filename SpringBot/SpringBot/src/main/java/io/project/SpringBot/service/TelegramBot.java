package io.project.SpringBot.service; // Пакет, в якому знаходиться клас TelegramBot.

import io.project.SpringBot.config.BotConfig;
import org.springframework.stereotype.Component; // Для позначення класу як компонент Spring.
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location; // Для роботи з геолокацією.
import org.telegram.telegrambots.meta.api.objects.Update; // Для отримання оновлень.
import org.telegram.telegrambots.meta.exceptions.TelegramApiException; // Для обробки винятків Telegram API.

import java.io.FileReader;
import java.io.IOException; // Для обробки винятків вводу/виводу.
import java.util.ArrayList; // Для роботи зі списками.
import java.util.HashMap; // Для роботи з хеш-картами.
import java.util.List;
import java.util.Map; // Для роботи з асоціативними масивами.

import org.json.simple.JSONArray; // Для роботи з JSON-масивами.
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@Component // Позначаємо цей клас як компонент Spring для автоматичного сканування та імпорту його в контекст додатку.
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;

    public TelegramBot(BotConfig config) {
        this.config = config;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            long chatId = update.getMessage().getChatId();

            if (update.getMessage().hasText()) {
                String messageText = update.getMessage().getText();

                switch (messageText) {
                    case "/start":
                        startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                        break;
                    default:
                        sendMessage(chatId, "Це поки що не підтримується");
                }
            } else if (update.getMessage().hasLocation()) {
                Location location = update.getMessage().getLocation(); // Отримуємо геолокацію з повідомлення.
                handleLocation(chatId, location.getLatitude(), location.getLongitude());
            }
        }
    }

    private List<Map<String, Object>> readPointsFromJSON(String fileName) {
        List<Map<String, Object>> points = new ArrayList<>(); // Створюємо список для зберігання точок.
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(fileName)) {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            JSONArray records = (JSONArray) jsonObject.get("records");

            for (Object obj : records) {
                JSONArray record = (JSONArray) obj;

                // Створюємо Map для зберігання атрибутів точки.
                Map<String, Object> point = new HashMap<>();
                point.put("district", record.get(3));
                point.put("streetType", record.get(4));
                point.put("streetName", record.get(5));
                point.put("houseNumber", record.get(6));
                point.put("x", ((Number) record.get(18)).doubleValue());
                point.put("y", ((Number) record.get(17)).doubleValue());

                points.add(point); // Додаємо точку до списку точок.
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return points; // Повертаємо список точок.
    }

    private Map<String, Object> findNearestPoint(double target_x, double target_y, List<Map<String, Object>> points) {
        Map<String, Object> nearestPoint = null; // Початкове значення найближчої точки.
        double minDistance = Double.POSITIVE_INFINITY; // Початкове значення мінімальної відстані.

        for (Map<String, Object> point : points) {
            double x = (double) point.get("x");
            double y = (double) point.get("y");

            // Обчислюємо відстань між цільовими координатами і координатами поточної точки.
            double distance = Math.sqrt(Math.pow(target_x - x, 2) + Math.pow(target_y - y, 2));


            if (distance < minDistance) {
                minDistance = distance; // Оновлюємо мінімальну відстань.
                nearestPoint = point; // Оновлюємо найближчу точку.
            }
        }

        return nearestPoint; // Повертаємо знайдену найближчу точку.
    }

    private void handleLocation(long chatId, double target_x, double target_y) {
        // Метод для обробки отриманих геолокаційних координат.
        List<Map<String, Object>> points = readPointsFromJSON("maps.json"); // Отримуємо список точок з JSON-файлу.
        Map<String, Object> nearestPoint = findNearestPoint(target_x, target_y, points); // Знаходимо найближчу точку.

        // Формування відповіді з інформацією про найближчу точку.
        String response = String.format("Найближча точка:\nРайон %s,\n%s %s, Буд. %s",
                nearestPoint.get("district"), nearestPoint.get("streetType"), nearestPoint.get("streetName"),
                nearestPoint.get("houseNumber"));

        sendMessage(chatId, response); // Надсилаємо відповідь.
    }

    private void startCommandReceived(long chatId, String name) {
        // Метод для обробки команди "/start".
        String answer = "Привіт, " + name + ", зайди з мобільного пристрою, та надішли своє місцерозташування";
        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage(); // Створюємо об'єкт повідомлення.
        message.setChatId(String.valueOf(chatId)); // Встановлюємо ID чату.
        message.setText(textToSend); // Встановлюємо текст повідомлення.

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
