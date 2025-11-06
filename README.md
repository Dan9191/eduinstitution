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

##### Поля:
- `name` (string, обязательное) - Имя пользователя
- `email` (string, обязательное) - Email пользователя (уникальное значение)
- `role` (string, обязательное) - Роль пользователя (STUDENT, TEACHER, ADMIN)
- `bio` (string, опционально) - Биография пользователя
- `avatarUrl` (string, опционально) - URL аватара пользователя

#### Получение пользователя по ID
```http
GET /user/1
```

##### Параметр:
- `id` (long, обязательный) - Уникальный идентификатор пользователя

### Курсы

#### Создание курса
```http
POST /course/create
Content-Type: application/json

{
  "title": "Java Programming",
  "description": "Complete course on Java programming",
  "categoryId": 1,
  "teacherId": 1,
  "duration": 30,
  "startDate": "2025-01-15"
}
```

##### Поля:
- `title` (string, обязательное) - Название курса
- `description` (string, опционально) - Описание курса
- `categoryId` (long, обязательное) - ID категории, к которой относится курс
- `teacherId` (long, обязательное) - ID преподавателя, ведущего курс
- `duration` (integer, опционально) - Продолжительность курса в днях (по умолчанию 30)
- `startDate` (date, опционально) - Дата начала курса (по умолчанию текущая дата)

#### Получение всех курсов с пагинацией
```http
GET /course
```

##### Параметры пагинации:
- `page` - номер страницы (по умолчанию 0)
- `size` - размер страницы (по умолчанию 10)
- `sort` - сортировка (необязательно)

##### Примеры:
```http
GET /course?page=0&size=5
GET /course?page=1&size=10&sort=title
```

##### Ответ:
```json
{
  "content": [
    {
      "id": 1,
      "title": "Java Programming",
      "description": "Complete course on Java programming",
      "categoryId": 1,
      "categoryName": "Programming",
      "teacherId": 1,
      "teacherName": "John Doe",
      "duration": 30,
      "startDate": "2025-01-15"
    }
  ],
  "pageable": {
    "sort": { "empty": true, "unsorted": true, "sorted": false },
    "offset": 0,
    "pageNumber": 0,
    "pageSize": 10,
    "paged": true,
    "unpaged": false
  },
  "totalElements": 1,
  "totalPages": 1,
  "size": 10,
  "number": 0,
  "sort": { "empty": true, "unsorted": true, "sorted": false },
  "numberOfElements": 1,
  "first": true,
  "last": true
}
```

### Категории

#### Получение всех категорий
```http
GET /category
```

##### Ответ:
```json
[
  {
    "id": 1,
    "name": "Programming"
  },
  {
    "id": 2,
    "name": "Web Development"
  }
]
```

#### Получение категории по ID
```http
GET /category/1
```

##### Параметр:
- `id` (long, обязательный) - Уникальный идентификатор категории

##### Ответ:
```json
{
  "id": 1,
  "name": "Programming"
}
```

### Записи студентов на курсы

#### Запись студента на курс
```http
POST /enrollment/enroll
Content-Type: application/json

{
  "studentId": 1,
  "courseId": 1
}
```

##### Поля:
- `studentId` (long, обязательное) - ID студента
- `courseId` (long, обязательное) - ID курса

##### Ответ (201 Created):
```json
{
  "studentId": 1,
  "studentName": "John Doe",
  "courseId": 1,
  "courseTitle": "Java Programming",
  "enrollDate": "2025-01-15",
  "status": "Active"
}
```

#### Отписка студента от курса
```http
DELETE /enrollment/unenroll/1/1
```

##### Параметры:
- `studentId` (path variable, обязательный) - ID студента
- `courseId` (path variable, обязательный) - ID курса

##### Ответ (204 No Content)

#### Получение всех записей студента
```http
GET /enrollment/student/1
```

##### Параметр:
- `studentId` (path variable, обязательный) - ID студента

##### Ответ (200 OK):
```json
[
  {
    "studentId": 1,
    "studentName": "John Doe",
    "courseId": 1,
    "courseTitle": "Java Programming",
    "enrollDate": "2025-01-15",
    "status": "Active"
  }
]
```

#### Получение всех студентов, записанных на курс
```http
GET /enrollment/course/1
```

##### Параметр:
- `courseId` (path variable, обязательный) - ID курса

##### Ответ (200 OK):
```json
[
  {
    "studentId": 1,
    "studentName": "John Doe",
    "courseId": 1,
    "courseTitle": "Java Programming",
    "enrollDate": "2025-01-15",
    "status": "Active"
  }
]
```

### Системные эндпоинты

#### Простой эндпоинт для проверки
```http
GET /hi
```

##### Ответ:
```
hi
```
