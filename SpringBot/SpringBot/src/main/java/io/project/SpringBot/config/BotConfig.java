package io.project.SpringBot.config;

import lombok.Data; // Імпортуємо анотацію Data з проекту Lombok для автоматичної генерації геттерів, сеттерів та інших методів для полів класу.
import org.springframework.beans.factory.annotation.Value; // Для імпорту значень властивостей з application.properties.
import org.springframework.context.annotation.Configuration; // Для вказівки, що цей клас є конфігураційним класом.
import org.springframework.context.annotation.PropertySource; // Для вказівки джерела властивостей.

@Configuration // Позначаємо цей клас як конфігураційний для Spring.
@Data // Анотація Lombok, яка генерує геттери, сеттери та інші методи для полів класу.
@PropertySource("application.properties") // Вказуємо шлях до файлу application.properties, з якого будуть зчитуватися властивості.
public class BotConfig {

    @Value("${bot.name}") // Встановлюємо значення поля botName з файлу application.properties.
    String botName;

    @Value("${bot.token}") // Встановлюємо значення поля token з файлу application.properties.
    String token;
}
