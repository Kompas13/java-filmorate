DROP TABLE IF EXISTS GENRE CASCADE;
DROP TABLE IF EXISTS MPA_RATING CASCADE;
DROP TABLE IF EXISTS USERS CASCADE;
DROP TABLE IF EXISTS FILM CASCADE;
DROP TABLE IF EXISTS FILM_GENRE CASCADE;
DROP TABLE IF EXISTS FILM_LIKES CASCADE;
DROP TABLE IF EXISTS FRIENDS CASCADE;

CREATE TABLE IF NOT EXISTS GENRE (
GENRE_ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
NAME     CHARACTER VARYING(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS MPA_RATING (
RATING_ID   INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
NAME        CHARACTER VARYING(50) NOT NULL,
DESCRIPTION CHARACTER VARYING(200) NOT NULL
);

CREATE TABLE IF NOT EXISTS USERS (
USER_ID  BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
EMAIL    CHARACTER VARYING(200) NOT NULL,
LOGIN    CHARACTER VARYING(200) NOT NULL,
NAME     CHARACTER VARYING(50) NOT NULL,
BIRTHDAY DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS FILM (
FILM_ID      BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
NAME         CHARACTER VARYING(200) NOT NULL,
DESCRIPTION  CHARACTER VARYING(200) NOT NULL,
RELEASE_DATE DATE NOT NULL,
DURATION     INTEGER NOT NULL,
RATING_ID    INTEGER NOT NULL REFERENCES MPA_RATING (RATING_ID)
);

CREATE TABLE IF NOT EXISTS FILM_GENRE (
FILM_ID  BIGINT NOT NULL REFERENCES FILM (FILM_ID),
GENRE_ID INTEGER NOT NULL REFERENCES GENRE (GENRE_ID)
);

CREATE TABLE IF NOT EXISTS FILM_LIKES (
FILM_ID BIGINT NOT NULL REFERENCES FILM (FILM_ID),
USER_ID BIGINT NOT NULL REFERENCES USERS (USER_ID),
PRIMARY KEY(FILM_ID, USER_ID)
);

CREATE TABLE IF NOT EXISTS FRIENDS (
FIRST_USER_ID  BIGINT NOT NULL REFERENCES USERS (USER_ID),
SECOND_USER_ID BIGINT NOT NULL REFERENCES USERS (USER_ID),
STATUS         BOOLEAN NOT NULL,
PRIMARY KEY(FIRST_USER_ID, SECOND_USER_ID)
);

