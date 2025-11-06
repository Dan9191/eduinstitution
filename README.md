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

### Модули

#### Создание модуля
```http
POST /module
Content-Type: application/json

{
  "courseId": 1,
  "title": "Java Fundamentals",
  "orderIndex": 1,
  "description": "This module covers Java fundamentals..."
}
```

##### Поля:
- `courseId` (long, обязательное) - ID курса, к которому относится модуль
- `title` (string, обязательное) - Название модуля
- `orderIndex` (integer, опционально) - Порядковый номер модуля
- `description` (string, опционально) - Описание модуля

##### Ответ (201 Created):
```json
{
  "id": 1,
  "title": "Java Fundamentals",
  "orderIndex": 1,
  "description": "This module covers Java fundamentals...",
  "courseId": 1,
  "courseTitle": "Java Programming"
}
```

#### Получение модуля по ID
```http
GET /module/1
```

##### Ответ (200 OK):
```json
{
  "id": 1,
  "title": "Java Fundamentals",
  "orderIndex": 1,
  "description": "This module covers Java fundamentals...",
  "courseId": 1,
  "courseTitle": "Java Programming"
}
```

#### Обновление модуля
```http
PUT /module/1
Content-Type: application/json

{
  "title": "Updated Java Fundamentals",
  "orderIndex": 2,
  "description": "Updated description..."
}
```

##### Ответ (200 OK):
```json
{
  "id": 1,
  "title": "Updated Java Fundamentals",
  "orderIndex": 2,
  "description": "Updated description...",
  "courseId": 1,
  "courseTitle": "Java Programming"
}
```

#### Удаление модуля
```http
DELETE /module/1
```

##### Ответ (204 No Content)

#### Получение всех модулей по курсу
```http
GET /module/course/1
```

##### Параметр:
- `courseId` (path variable, обязательный) - ID курса

##### Ответ (200 OK):
```json
[
  {
    "id": 1,
    "title": "Java Fundamentals",
    "orderIndex": 1,
    "description": "This module covers Java fundamentals...",
    "courseId": 1,
    "courseTitle": "Java Programming"
  }
]
```

### Уроки

#### Создание урока
```http
POST /lesson
Content-Type: application/json

{
  "moduleId": 1,
  "title": "Introduction to Java",
  "content": "This lesson covers Java basics...",
  "videoUrl": "https://example.com/video1.mp4"
}
```

##### Поля:
- `moduleId` (long, обязательное) - ID модуля, к которому относится урок
- `title` (string, обязательное) - Название урока
- `content` (string, опционально) - Содержание урока
- `videoUrl` (string, опционально) - URL видео урока

##### Ответ (201 Created):
```json
{
  "id": 1,
  "title": "Introduction to Java",
  "content": "This lesson covers Java basics...",
  "videoUrl": "https://example.com/video1.mp4",
  "moduleId": 1,
  "moduleTitle": "Java Fundamentals"
}
```

#### Получение урока по ID
```http
GET /lesson/1
```

##### Ответ (200 OK):
```json
{
  "id": 1,
  "title": "Introduction to Java",
  "content": "This lesson covers Java basics...",
  "videoUrl": "https://example.com/video1.mp4",
  "moduleId": 1,
  "moduleTitle": "Java Fundamentals"
}
```

#### Обновление урока
```http
PUT /lesson/1
Content-Type: application/json

{
  "title": "Updated Introduction to Java",
  "content": "Updated content...",
  "videoUrl": "https://example.com/updated-video.mp4"
}
```

##### Ответ (200 OK):
```json
{
  "id": 1,
  "title": "Updated Introduction to Java",
  "content": "Updated content...",
  "videoUrl": "https://example.com/updated-video.mp4",
  "moduleId": 1,
  "moduleTitle": "Java Fundamentals"
}
```

#### Удаление урока
```http
DELETE /lesson/1
```

##### Ответ (204 No Content)

#### Получение всех уроков по модулю
```http
GET /lesson/module/1
```

##### Параметр:
- `moduleId` (path variable, обязательный) - ID модуля

##### Ответ (200 OK):
```json
[
  {
    "id": 1,
    "title": "Introduction to Java",
    "content": "This lesson covers Java basics...",
    "videoUrl": "https://example.com/video1.mp4",
    "moduleId": 1,
    "moduleTitle": "Java Fundamentals"
  }
]
```

### Задания

#### Создание задания
```http
POST /assignment
Content-Type: application/json

{
  "lessonId": 1,
  "title": "Homework #1: Java Basics",
  "description": "Complete the exercises on Java basics",
  "dueDate": "2025-01-30",
  "maxScore": 100
}
```

##### Поля:
- `lessonId` (long, обязательное) - ID урока, к которому относится задание
- `title` (string, обязательное) - Название задания
- `description` (string, опционально) - Описание задания
- `dueDate` (date, опционально) - Дата сдачи задания
- `maxScore` (integer, опционально) - Максимальный балл за задание

##### Ответ (201 Created):
```json
{
  "id": 1,
  "title": "Homework #1: Java Basics",
  "description": "Complete the exercises on Java basics",
  "dueDate": "2025-01-30",
  "maxScore": 100,
  "lessonId": 1,
  "lessonTitle": "Introduction to Java"
}
```

#### Получение задания по ID
```http
GET /assignment/1
```

##### Ответ (200 OK):
```json
{
  "id": 1,
  "title": "Homework #1: Java Basics",
  "description": "Complete the exercises on Java basics",
  "dueDate": "2025-01-30",
  "maxScore": 100,
  "lessonId": 1,
  "lessonTitle": "Introduction to Java"
}
```

#### Обновление задания
```http
PUT /assignment/1
Content-Type: application/json

{
  "title": "Updated Homework #1: Java Basics",
  "description": "Updated description...",
  "dueDate": "2025-02-01",
  "maxScore": 90
}
```

##### Ответ (200 OK):
```json
{
  "id": 1,
  "title": "Updated Homework #1: Java Basics",
  "description": "Updated description...",
  "dueDate": "2025-02-01",
  "maxScore": 90,
  "lessonId": 1,
  "lessonTitle": "Introduction to Java"
}
```

#### Удаление задания
```http
DELETE /assignment/1
```

##### Ответ (204 No Content)

#### Получение всех заданий по уроку
```http
GET /assignment/lesson/1
```

##### Параметр:
- `lessonId` (path variable, обязательный) - ID урока

##### Ответ (200 OK):
```json
[
  {
    "id": 1,
    "title": "Homework #1: Java Basics",
    "description": "Complete the exercises on Java basics",
    "dueDate": "2025-01-30",
    "maxScore": 100,
    "lessonId": 1,
    "lessonTitle": "Introduction to Java"
  }
]
```

### Ответы/решения студентов

#### Создание ответа/решения студента
```http
POST /submission
Content-Type: application/json

{
  "studentId": 1,
  "assignmentId": 1,
  "content": "The solution to the assignment..."
}
```

##### Поля:
- `studentId` (long, обязательное) - ID студента, который отправляет решение
- `assignmentId` (long, обязательное) - ID задания, на которое отправляется решение
- `content` (string, обязательное) - Содержание решения/ответа

##### Ответ (201 Created):
```json
{
  "id": 1,
  "submittedAt": "2025-01-15T14:30:00",
  "content": "The solution to the assignment...",
  "score": null,
  "feedback": null,
  "assignmentId": 1,
  "assignmentTitle": "Homework #1: Java Basics",
  "studentId": 1,
  "studentName": "John Doe"
}
```

#### Получение ответа/решения по ID
```http
GET /submission/1
```

##### Ответ (200 OK):
```json
{
  "id": 1,
  "submittedAt": "2025-01-15T14:30:00",
  "content": "The solution to the assignment...",
  "score": null,
  "feedback": null,
  "assignmentId": 1,
  "assignmentTitle": "Homework #1: Java Basics",
  "studentId": 1,
  "studentName": "John Doe"
}
```

#### Выставление оценки за ответ/решение
```http
PUT /submission/1/grade
Content-Type: application/json

{
  "score": 95,
  "feedback": "Good work, but consider optimization..."
}
```

##### Поля:
- `score` (integer, опционально, 0-100) - Балл за задание
- `feedback` (string, опционально) - Обратная связь преподавателя

##### Ответ (200 OK):
```json
{
  "id": 1,
  "submittedAt": "2025-01-15T14:30:00",
  "content": "The solution to the assignment...",
  "score": 95,
  "feedback": "Good work, but consider optimization...",
  "assignmentId": 1,
  "assignmentTitle": "Homework #1: Java Basics",
  "studentId": 1,
  "studentName": "John Doe"
}
```

#### Получение всех ответов/решений по студенту
```http
GET /submission/student/1
```

##### Параметр:
- `studentId` (path variable, обязательный) - ID студента

##### Ответ (200 OK):
```json
[
  {
    "id": 1,
    "submittedAt": "2025-01-15T14:30:00",
    "content": "The solution to the assignment...",
    "score": 95,
    "feedback": "Good work, but consider optimization...",
    "assignmentId": 1,
    "assignmentTitle": "Homework #1: Java Basics",
    "studentId": 1,
    "studentName": "John Doe"
  }
]
```

#### Получение всех ответов/решений по заданию
```http
GET /submission/assignment/1
```

##### Параметр:
- `assignmentId` (path variable, обязательный) - ID задания

##### Ответ (200 OK):
```json
[
  {
    "id": 1,
    "submittedAt": "2025-01-15T14:30:00",
    "content": "The solution to the assignment...",
    "score": 95,
    "feedback": "Good work, but consider optimization...",
    "assignmentId": 1,
    "assignmentTitle": "Homework #1: Java Basics",
    "studentId": 1,
    "studentName": "John Doe"
  }
]
```

### Тесты

#### Создание теста
```http
POST /quiz
Content-Type: application/json

{
  "moduleId": 1,
  "title": "Midterm Exam",
  "timeLimit": 60
}
```

##### Поля:
- `moduleId` (long, обязательное) - ID модуля, к которому относится тест
- `title` (string, обязательное) - Название теста
- `timeLimit` (integer, опционально) - Ограничение по времени в минутах

##### Ответ (201 Created):
```json
{
  "id": 1,
  "title": "Midterm Exam",
  "timeLimit": 60,
  "moduleId": 1,
  "moduleTitle": "Java Fundamentals"
}
```

#### Получение теста по ID
```http
GET /quiz/1
```

##### Ответ (200 OK):
```json
{
  "id": 1,
  "title": "Midterm Exam",
  "timeLimit": 60,
  "moduleId": 1,
  "moduleTitle": "Java Fundamentals"
}
```

#### Обновление теста
```http
PUT /quiz/1
Content-Type: application/json

{
  "title": "Updated Midterm Exam",
  "timeLimit": 90
}
```

##### Ответ (200 OK):
```json
{
  "id": 1,
  "title": "Updated Midterm Exam",
  "timeLimit": 90,
  "moduleId": 1,
  "moduleTitle": "Java Fundamentals"
}
```

#### Удаление теста
```http
DELETE /quiz/1
```

##### Ответ (204 No Content)

#### Получение теста по ID модуля
```http
GET /quiz/module/1
```

##### Параметр:
- `moduleId` (path variable, обязательный) - ID модуля

##### Ответ (200 OK):
```json
{
  "id": 1,
  "title": "Midterm Exam",
  "timeLimit": 60,
  "moduleId": 1,
  "moduleTitle": "Java Fundamentals"
}
```

### Вопросы

#### Создание вопроса
```http
POST /question
Content-Type: application/json

{
  "quizId": 1,
  "text": "What is the capital of France?",
  "type": "SINGLE_CHOICE"
}
```

##### Поля:
- `quizId` (long, обязательное) - ID теста, к которому относится вопрос
- `text` (string, обязательное) - Текст вопроса
- `type` (string, обязательное) - Тип вопроса (SINGLE_CHOICE, MULTIPLE_CHOICE, TEXT)

##### Ответ (201 Created):
```json
{
  "id": 1,
  "text": "What is the capital of France?",
  "type": "SINGLE_CHOICE",
  "quizId": 1,
  "quizTitle": "Midterm Exam"
}
```

#### Получение вопроса по ID
```http
GET /question/1
```

##### Ответ (200 OK):
```json
{
  "id": 1,
  "text": "What is the capital of France?",
  "type": "SINGLE_CHOICE",
  "quizId": 1,
  "quizTitle": "Midterm Exam"
}
```

#### Обновление вопроса
```http
PUT /question/1
Content-Type: application/json

{
  "text": "What is the capital of France? (Updated)",
  "type": "MULTIPLE_CHOICE"
}
```

##### Ответ (200 OK):
```json
{
  "id": 1,
  "text": "What is the capital of France? (Updated)",
  "type": "MULTIPLE_CHOICE",
  "quizId": 1,
  "quizTitle": "Midterm Exam"
}
```

#### Удаление вопроса
```http
DELETE /question/1
```

##### Ответ (204 No Content)

#### Получение всех вопросов по тесту
```http
GET /question/quiz/1
```

##### Параметр:
- `quizId` (path variable, обязательный) - ID теста

##### Ответ (200 OK):
```json
[
  {
    "id": 1,
    "text": "What is the capital of France?",
    "type": "SINGLE_CHOICE",
    "quizId": 1,
    "quizTitle": "Midterm Exam"
  }
]
```

### Варианты ответов

#### Создание варианта ответа
```http
POST /answer-option
Content-Type: application/json

{
  "questionId": 1,
  "text": "Paris",
  "isCorrect": true
}
```

##### Поля:
- `questionId` (long, обязательное) - ID вопроса, к которому относится вариант ответа
- `text` (string, обязательное) - Текст варианта ответа
- `isCorrect` (boolean, опционально) - Указывает, является ли вариант правильным

##### Ответ (201 Created):
```json
{
  "id": 1,
  "text": "Paris",
  "isCorrect": true,
  "questionId": 1,
  "questionText": "What is the capital of France?"
}
```

#### Получение варианта ответа по ID
```http
GET /answer-option/1
```

##### Ответ (200 OK):
```json
{
  "id": 1,
  "text": "Paris",
  "isCorrect": true,
  "questionId": 1,
  "questionText": "What is the capital of France?"
}
```

#### Обновление варианта ответа
```http
PUT /answer-option/1
Content-Type: application/json

{
  "text": "London",
  "isCorrect": false
}
```

##### Ответ (200 OK):
```json
{
  "id": 1,
  "text": "London",
  "isCorrect": false,
  "questionId": 1,
  "questionText": "What is the capital of France?"
}
```

#### Удаление варианта ответа
```http
DELETE /answer-option/1
```

##### Ответ (204 No Content)

#### Получение всех вариантов ответов по вопросу
```http
GET /answer-option/question/1
```

##### Параметр:
- `questionId` (path variable, обязательный) - ID вопроса

##### Ответ (200 OK):
```json
[
  {
    "id": 1,
    "text": "Paris",
    "isCorrect": true,
    "questionId": 1,
    "questionText": "What is the capital of France?"
  }
]
```

### Результаты тестов

#### Создание результата теста
```http
POST /quiz-submission
Content-Type: application/json

{
  "quizId": 1,
  "studentId": 1,
  "score": 85
}
```

##### Поля:
- `quizId` (long, обязательное) - ID теста, который был сдан
- `studentId` (long, обязательное) - ID студента, который сдал тест
- `score` (integer, обязательное) - Балл, полученный в тесте

##### Ответ (201 Created):
```json
{
  "id": 1,
  "score": 85,
  "takenAt": "2025-01-15T10:30:00",
  "quizId": 1,
  "quizTitle": "Midterm Exam",
  "studentId": 1,
  "studentName": "John Doe"
}
```

#### Получение результата теста по ID
```http
GET /quiz-submission/1
```

##### Ответ (200 OK):
```json
{
  "id": 1,
  "score": 85,
  "takenAt": "2025-01-15T10:30:00",
  "quizId": 1,
  "quizTitle": "Midterm Exam",
  "studentId": 1,
  "studentName": "John Doe"
}
```

#### Обновление результата теста
```http
PUT /quiz-submission/1
Content-Type: application/json

{
  "score": 90
}
```

##### Ответ (200 OK):
```json
{
  "id": 1,
  "score": 90,
  "takenAt": "2025-01-15T10:30:00",
  "quizId": 1,
  "quizTitle": "Midterm Exam",
  "studentId": 1,
  "studentName": "John Doe"
}
```

#### Удаление результата теста
```http
DELETE /quiz-submission/1
```

##### Ответ (204 No Content)

#### Получение всех результатов тестов по студенту
```http
GET /quiz-submission/student/1
```

##### Параметр:
- `studentId` (path variable, обязательный) - ID студента

##### Ответ (200 OK):
```json
[
  {
    "id": 1,
    "score": 85,
    "takenAt": "2025-01-15T10:30:00",
    "quizId": 1,
    "quizTitle": "Midterm Exam",
    "studentId": 1,
    "studentName": "John Doe"
  }
]
```

#### Получение всех результатов тестов по тесту
```http
GET /quiz-submission/quiz/1
```

##### Параметр:
- `quizId` (path variable, обязательный) - ID теста

##### Ответ (200 OK):
```json
[
  {
    "id": 1,
    "score": 85,
    "takenAt": "2025-01-15T10:30:00",
    "quizId": 1,
    "quizTitle": "Midterm Exam",
    "studentId": 1,
    "studentName": "John Doe"
  }
]
```

### Отзывы о курсах

#### Добавление отзыва о курсе (addReview method)
```http
POST /course-review
Content-Type: application/json

{
  "courseId": 1,
  "studentId": 1,
  "rating": 5,
  "comment": "Great course with excellent content!"
}
```

##### Поля:
- `courseId` (long, обязательное) - ID курса, о котором пишется отзыв
- `studentId` (long, обязательное) - ID студента, который пишет отзыв
- `rating` (integer, обязательное, 1-5) - Рейтинг курса
- `comment` (string, опционально) - Комментарий в отзыве

##### Ответ (201 Created):
```json
{
  "id": 1,
  "rating": 5,
  "comment": "Great course with excellent content!",
  "createdAt": "2025-01-15T16:45:00",
  "courseId": 1,
  "courseTitle": "Java Programming",
  "studentId": 1,
  "studentName": "John Doe"
}
```

#### Получение отзыва по ID
```http
GET /course-review/1
```

##### Ответ (200 OK):
```json
{
  "id": 1,
  "rating": 5,
  "comment": "Great course with excellent content!",
  "createdAt": "2025-01-15T16:45:00",
  "courseId": 1,
  "courseTitle": "Java Programming",
  "studentId": 1,
  "studentName": "John Doe"
}
```

#### Обновление отзыва
```http
PUT /course-review/1
Content-Type: application/json

{
  "rating": 4,
  "comment": "Good course but could be improved."
}
```

##### Ответ (200 OK):
```json
{
  "id": 1,
  "rating": 4,
  "comment": "Good course but could be improved.",
  "createdAt": "2025-01-15T16:45:00",
  "courseId": 1,
  "courseTitle": "Java Programming",
  "studentId": 1,
  "studentName": "John Doe"
}
```

#### Удаление отзыва
```http
DELETE /course-review/1
```

##### Ответ (204 No Content)

#### Получение всех отзывов по курсу (getReviewsByCourse method)
```http
GET /course-review/course/1
```

##### Параметр:
- `courseId` (path variable, обязательный) - ID курса

##### Ответ (200 OK):
```json
[
  {
    "id": 1,
    "rating": 5,
    "comment": "Great course with excellent content!",
    "createdAt": "2025-01-15T16:45:00",
    "courseId": 1,
    "courseTitle": "Java Programming",
    "studentId": 1,
    "studentName": "John Doe"
  }
]
```

#### Получение всех отзывов по студенту
```http
GET /course-review/student/1
```

##### Параметр:
- `studentId` (path variable, обязательный) - ID студента

##### Ответ (200 OK):
```json
[
  {
    "id": 1,
    "rating": 5,
    "comment": "Great course with excellent content!",
    "createdAt": "2025-01-15T16:45:00",
    "courseId": 1,
    "courseTitle": "Java Programming",
    "studentId": 1,
    "studentName": "John Doe"
  }
]
```

#### Получение среднего рейтинга по курсу
```http
GET /course-review/course/1/average-rating
```

##### Параметр:
- `courseId` (path variable, обязательный) - ID курса

##### Ответ (200 OK):
```json
4.5
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
