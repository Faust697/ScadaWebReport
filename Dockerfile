# Используйте официальный образ Java
FROM openjdk:11-jre-slim

# Установка рабочей директории
WORKDIR /app

# Копирование JAR-файла вашего проекта в контейнер
COPY target/ScadaWebReport-0.0.1-SNAPSHOT.jar app.jar

# Открытие порта для веб-приложения
EXPOSE 443

# Команда для запуска приложения при старте контейнера
CMD ["java", "-jar", "app.jar"]
