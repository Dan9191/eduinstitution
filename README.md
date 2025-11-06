# Educational Institution Service (eduinstitution)

## Swagger

Доступен при запуске

http://localhost:8084/swagger-ui.html

## Описание

Сервис для управления образовательным учреждением. Предоставляет функциональность для управления пользователями, курсами, заданиями и другими аспектами образовательного процесса. 

## Сборка

Требуется Java 21 и Gradle.

### Сборка
```shell
./gradlew clean build
```

### Запуск
```shell
./gradlew bootRun
```

## Конфигурация

Настройки в `src/main/resources/application.yaml`:

| Переменная                                | Значение по умолчанию                                           | Описание                                      |
|-------------------------------------------|-----------------------------------------------------------------|-----------------------------------------------|
| SERVER_PORT                               | 8080                                                            | Порт сервиса                                  |
| SPRING_APPLICATION_NAME                   | eduinstitution                                                  | Имя приложения                                |
| SPRING_DATASOURCE_URL                     | jdbc:postgresql://localhost:5432/test?currentSchema=edu_service | URL базы данных PostgreSQL                     |
| SPRING_DATASOURCE_USERNAME                | test                                                            | Пользователь базы данных                      |
| SPRING_DATASOURCE_PASSWORD                | test                                                            | Пароль базы данных                            |
| SPRING_JPA_HIBERNATE_DDL_AUTO             | update                                                          | Режим создания схемы базы данных              |
| SPRING_FLYWAY_ENABLED                     | true                                                            | Включение Flyway миграций                     |
| SPRING_FLYWAY_LOCATIONS                   | classpath:db/migration                                          | Расположение миграционных скриптов            |
| SPRING_FLYWAY_BASELINE_ON_MIGRATE         | true                                                            | Базовая миграция при необходимости            |
| MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE | health,info,metrics,env,build,git                               | Экспонируемые эндпоинты Actuator              |

## Примеры запросов

### Пользователи

#### Создание пользователя
```http
POST /user/create
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "role": "STUDENT",
  "bio": "Software developer and teacher",
  "avatarUrl": "https://example.com/avatar.jpg"
}
```

#### Получение пользователя по ID
```http
GET /user/1
```

### Категории

#### Получение всех категорий
```http
GET /category
```

#### Получение категории по ID
```http
GET /category/1
```
