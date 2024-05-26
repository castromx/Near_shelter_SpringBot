package io.project.SpringBot; // Пакет, в якому знаходиться клас SpringBotApplication.

import org.springframework.boot.SpringApplication; // Використовується для запуску додатку Spring Boot.
import org.springframework.boot.autoconfigure.SpringBootApplication; // Для автоматичного конфігурування додатку Spring Boot.

@SpringBootApplication // Анотація, що позначає цей клас як основний клас додатку Spring Boot і включає в себе автоматичну конфігурацію Spring Boot.
public class SpringBotApplication { // Оголошення класу SpringBotApplication.

	public static void main(String[] args) {
		SpringApplication.run(SpringBotApplication.class, args); // Ініціалізує додаток Spring Boot і запускає його з переданими параметрами.
	}
}
