CREATE TABLE role_table
(
    id   bigint                                              NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT role_table_pkey PRIMARY KEY (id),
    CONSTRAINT uk_role_name UNIQUE (name)
);

CREATE TABLE courses
(
    id            bigint                                              NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    begin_date    timestamp without time zone                         NOT NULL,
    creator       character varying(255) COLLATE pg_catalog."default" NOT NULL,
    description   character varying(255) COLLATE pg_catalog."default",
    end_date      timestamp without time zone                         NOT NULL,
    name          character varying(255) COLLATE pg_catalog."default" NOT NULL,
    passing_score integer                                             NOT NULL,
    CONSTRAINT courses_pkey PRIMARY KEY (id),
    CONSTRAINT uk_course_name UNIQUE (name)
);

CREATE TABLE tasks
(
    id          bigint                                              NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    description character varying(255) COLLATE pg_catalog."default",
    name        character varying(255) COLLATE pg_catalog."default" NOT NULL,
    course_id   bigint,
    CONSTRAINT tasks_pkey PRIMARY KEY (id),
    CONSTRAINT uk_task_name UNIQUE (name),
    CONSTRAINT tasks_course_fk FOREIGN KEY (course_id)
        REFERENCES courses (id) MATCH SIMPLE
);

CREATE TABLE users
(
    id        bigint                                              NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    age       integer,
    email     character varying(255) COLLATE pg_catalog."default" NOT NULL,
    full_name character varying(255) COLLATE pg_catalog."default",
    login     character varying(255) COLLATE pg_catalog."default" NOT NULL,
    password  character varying(255) COLLATE pg_catalog."default" NOT NULL,
    phone     character varying(255) COLLATE pg_catalog."default",
    role_id   bigint,
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT uk_users_email UNIQUE (email),
    CONSTRAINT uk_users_phone UNIQUE (phone),
    CONSTRAINT uk_users_login UNIQUE (login),
    CONSTRAINT users_role_fk FOREIGN KEY (role_id)
        REFERENCES role_table (id) MATCH SIMPLE
);

CREATE TABLE skills
(
    id    bigint                                              NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    name  character varying(255) COLLATE pg_catalog."default" NOT NULL,
    image character varying(255)                              NOT NULL,
    CONSTRAINT skills_pkey PRIMARY KEY (id),
    CONSTRAINT uk_skill_name UNIQUE (name),
    CONSTRAINT uk_skill_image UNIQUE (image)
);

CREATE TABLE comments
(
    id        bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    content   character varying(255) COLLATE pg_catalog."default",
    course_id bigint,
    user_id   bigint,
    CONSTRAINT comments_pkey PRIMARY KEY (id),
    CONSTRAINT comments_course_fk FOREIGN KEY (course_id)
        REFERENCES courses (id) MATCH SIMPLE,
    CONSTRAINT comments_user_fk FOREIGN KEY (user_id)
        REFERENCES users (id) MATCH SIMPLE
);

CREATE TABLE materials
(
    id      bigint                                              NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    creator character varying(255) COLLATE pg_catalog."default",
    link    character varying(255) COLLATE pg_catalog."default",
    name    character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT materials_pkey PRIMARY KEY (id),
    CONSTRAINT uk_materials_name UNIQUE (name)
);

CREATE TABLE course_results
(
    id        bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    status    character varying(255) COLLATE pg_catalog."default",
    course_id bigint,
    user_id   bigint,
    CONSTRAINT course_results_pkey PRIMARY KEY (id),
    CONSTRAINT course_results_course_fk FOREIGN KEY (course_id)
        REFERENCES courses (id) MATCH SIMPLE,
    CONSTRAINT course_results_user_fk FOREIGN KEY (user_id)
        REFERENCES users (id) MATCH SIMPLE
);

CREATE TABLE material_to_course
(
    material_id bigint NOT NULL,
    course_id   bigint NOT NULL,
    CONSTRAINT material_to_course_pkey PRIMARY KEY (material_id, course_id),
    CONSTRAINT material_to_course_material_fk FOREIGN KEY (material_id)
        REFERENCES materials (id) MATCH SIMPLE,
    CONSTRAINT material_to_course_course_fk FOREIGN KEY (course_id)
        REFERENCES courses (id) MATCH SIMPLE
);

CREATE TABLE material_to_user
(
    material_id bigint NOT NULL,
    user_id     bigint NOT NULL,
    CONSTRAINT material_to_user_pkey PRIMARY KEY (material_id, user_id),
    CONSTRAINT material_to_user_user_fk FOREIGN KEY (user_id)
        REFERENCES users (id) MATCH SIMPLE,
    CONSTRAINT material_to_user_material_fk FOREIGN KEY (material_id)
        REFERENCES materials (id) MATCH SIMPLE
);

CREATE TABLE skill_to_course
(
    id        bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    level     integer,
    course_id bigint,
    skill_id  bigint,
    CONSTRAINT skill_to_course_pkey PRIMARY KEY (id),
    CONSTRAINT skill_to_course_course_fk FOREIGN KEY (course_id)
        REFERENCES courses (id) MATCH SIMPLE,
    CONSTRAINT skill_to_course_skill_fk FOREIGN KEY (skill_id)
        REFERENCES skills (id) MATCH SIMPLE
);

CREATE TABLE skill_to_user
(
    id       bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    level    integer,
    skill_id bigint,
    user_id  bigint,
    CONSTRAINT skill_to_user_pkey PRIMARY KEY (id),
    CONSTRAINT skill_to_user_skill_fk FOREIGN KEY (skill_id)
        REFERENCES skills (id) MATCH SIMPLE,
    CONSTRAINT skill_to_user_user_fk FOREIGN KEY (user_id)
        REFERENCES users (id) MATCH SIMPLE
);

CREATE TABLE task_answers
(
    id      bigint                                              NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    choice  character varying(255) COLLATE pg_catalog."default" NOT NULL,
    task_id bigint,
    CONSTRAINT task_answers_pkey PRIMARY KEY (id),
    CONSTRAINT task_answers_task_fk FOREIGN KEY (task_id)
        REFERENCES tasks (id) MATCH SIMPLE
);

CREATE TABLE task_results
(
    id        bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    status    character varying(255) COLLATE pg_catalog."default",
    course_id bigint,
    task_id   bigint,
    user_id   bigint,
    CONSTRAINT task_results_pkey PRIMARY KEY (id),
    CONSTRAINT task_results_course_fk FOREIGN KEY (course_id)
        REFERENCES courses (id) MATCH SIMPLE,
    CONSTRAINT task_results_task_fk FOREIGN KEY (task_id)
        REFERENCES tasks (id) MATCH SIMPLE,
    CONSTRAINT task_results_user_fk FOREIGN KEY (user_id)
        REFERENCES users (id) MATCH SIMPLE
);

CREATE TABLE users_to_courses
(
    user_id   bigint NOT NULL,
    course_id bigint NOT NULL,
    CONSTRAINT users_to_courses_pkey PRIMARY KEY (user_id, course_id),
    CONSTRAINT users_to_courses_course_fk FOREIGN KEY (course_id)
        REFERENCES courses (id) MATCH SIMPLE,
    CONSTRAINT users_to_courses_user_fk FOREIGN KEY (user_id)
        REFERENCES users (id) MATCH SIMPLE
);

CREATE TABLE refresh_tokens
(
    id          bigint                      NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    user_id     bigint                      NOT NULL,
    token       character varying(255) COLLATE pg_catalog."default",
    expiry_date timestamp without time zone NOT NULL,
    CONSTRAINT tokens_user_fk FOREIGN KEY (user_id)
        REFERENCES users (id) MATCH SIMPLE
);

INSERT INTO materials (creator, link, name)
VALUES ('SuperAdmin', 'https://docs.oracle.com/javase/7/docs/api/', 'java doc'),
       ('SuperAdmin',
        'https://www.flaticon.com/free-icon/java_152760?term=java&page=1&position=3&page=1&position=3&related_id=152760&origin=search',
        'icons'),
       ('SuperAdmin', 'https://www.youtube.com/watch?v=2YkkMSzY5Iw&list=PLX7_6po9cuzOcty_LiF9OtJ1KMxbsILck&index=8',
        'music'),
       ('SuperAdmin', 'https://www.google.com/', 'all answers');

INSERT INTO role_table (id, name)
VALUES (2, 'ROLE_ADMIN'),
       (1, 'ROLE_USER');

INSERT INTO courses (begin_date, creator, description, end_date, name, passing_score)
VALUES ('2018-11-30 18:35:24.000000', 'test2', 'test2', '2019-11-30 18:35:24.000000', 'test2', 34),
       ('2018-11-30 18:35:24.000000', 'test2', 'test3', '2019-11-30 18:35:24.000000', 'test3', 34);

INSERT INTO users (age, email, full_name, login, password, phone, role_id)
VALUES (22, 'admin@gmail.com', 'Best Admin', 'SuperAdmin',
        '$2a$04$rWEXbzfjGiJK2NI1R89UHuzuxUoE36iC/ZXZlDZPw8EMM0znJDNpa', NULL, 2),
       (19, 'leo.jek7@gmail.com', 'Yevhen X', 'xizorg',
        '$2a$10$KY5VNXoQe6B/zJc6lzOdJuDAwUApV8PmdANpyiapeBu9dT9wlttf.', NULL, 1);

INSERT INTO skills (name, image)
VALUES ('speed', 'https://image.flaticon.com/icons/png/512/3564/3564796.png'),
       ('power', 'https://image.flaticon.com/icons/png/512/1625/1625674.png'),
       ('mind', 'https://image.flaticon.com/icons/png/512/1491/1491165.png'),
       ('agility', 'https://image.flaticon.com/icons/png/512/1388/1388570.png'),
       ('java', 'https://image.flaticon.com/icons/png/512/152/152760.png');

INSERT INTO skill_to_course (level, course_id, skill_id)
VALUES (4, 2, 1);

INSERT INTO skill_to_user (level, skill_id, user_id)
VALUES (4, 1, 1),
       (4, 2, 2),
       (5, 5, 2);

INSERT INTO material_to_user(material_id, user_id)
VALUES (1, 2),
       (2, 2);

CREATE SEQUENCE hibernate_sequence START 1;