-- V1__init.sql
CREATE TABLE categories
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE users
(
    id    BIGSERIAL PRIMARY KEY,
    name  VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    role  VARCHAR(20)  NOT NULL CHECK (role IN ('STUDENT', 'TEACHER', 'ADMIN'))
);

CREATE TABLE profiles
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT NOT NULL UNIQUE REFERENCES users (id) ON DELETE CASCADE,
    bio        TEXT,
    avatar_url VARCHAR(512)
);

CREATE TABLE courses
(
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description TEXT,
    category_id BIGINT REFERENCES categories (id),
    teacher_id  BIGINT       NOT NULL REFERENCES users (id),
    duration    INTEGER,
    start_date  DATE
);

CREATE TABLE tags
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE course_tag
(
    course_id BIGINT NOT NULL REFERENCES courses (id) ON DELETE CASCADE,
    tag_id    BIGINT NOT NULL REFERENCES tags (id) ON DELETE CASCADE,
    PRIMARY KEY (course_id, tag_id)
);

CREATE TABLE enrollments
(
    user_id     BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    course_id   BIGINT NOT NULL REFERENCES courses (id) ON DELETE CASCADE,
    enroll_date DATE        DEFAULT CURRENT_DATE,
    status      VARCHAR(20) DEFAULT 'Active' CHECK (status IN ('Active', 'Completed', 'Dropped')),
    PRIMARY KEY (user_id, course_id)
);

CREATE TABLE modules
(
    id          BIGSERIAL PRIMARY KEY,
    course_id   BIGINT       NOT NULL REFERENCES courses (id) ON DELETE CASCADE,
    title       VARCHAR(255) NOT NULL,
    order_index INTEGER,
    description TEXT
);

CREATE TABLE lessons
(
    id        BIGSERIAL PRIMARY KEY,
    module_id BIGINT       NOT NULL REFERENCES modules (id) ON DELETE CASCADE,
    title     VARCHAR(255) NOT NULL,
    content   TEXT,
    video_url VARCHAR(512)
);

CREATE TABLE assignments
(
    id          BIGSERIAL PRIMARY KEY,
    lesson_id   BIGINT       NOT NULL REFERENCES lessons (id) ON DELETE CASCADE,
    title       VARCHAR(255) NOT NULL,
    description TEXT,
    due_date    DATE,
    max_score   INTEGER
);

CREATE TABLE submissions
(
    id            BIGSERIAL PRIMARY KEY,
    assignment_id BIGINT NOT NULL REFERENCES assignments (id),
    student_id    BIGINT NOT NULL REFERENCES users (id),
    submitted_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    content       TEXT,
    score         INTEGER,
    feedback      TEXT,
    UNIQUE (assignment_id, student_id)
);

CREATE TABLE quizzes
(
    id         BIGSERIAL PRIMARY KEY,
    module_id  BIGINT UNIQUE REFERENCES modules (id) ON DELETE SET NULL,
    title      VARCHAR(255),
    time_limit INTEGER
);

CREATE TABLE questions
(
    id      BIGSERIAL PRIMARY KEY,
    quiz_id BIGINT      NOT NULL REFERENCES quizzes (id) ON DELETE CASCADE,
    text    TEXT        NOT NULL,
    type    VARCHAR(20) NOT NULL CHECK (type IN ('SINGLE_CHOICE', 'MULTIPLE_CHOICE', 'TEXT'))
);

CREATE TABLE answer_options
(
    id          BIGSERIAL PRIMARY KEY,
    question_id BIGINT       NOT NULL REFERENCES questions (id) ON DELETE CASCADE,
    text        VARCHAR(512) NOT NULL,
    is_correct  BOOLEAN      NOT NULL DEFAULT FALSE
);

CREATE TABLE quiz_submissions
(
    id         BIGSERIAL PRIMARY KEY,
    quiz_id    BIGINT NOT NULL REFERENCES quizzes (id),
    student_id BIGINT NOT NULL REFERENCES users (id),
    score      INTEGER,
    taken_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE course_reviews
(
    id         BIGSERIAL PRIMARY KEY,
    course_id  BIGINT NOT NULL REFERENCES courses (id),
    student_id BIGINT NOT NULL REFERENCES users (id),
    rating     INTEGER CHECK (rating BETWEEN 1 AND 5),
    comment    TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (course_id, student_id)
);