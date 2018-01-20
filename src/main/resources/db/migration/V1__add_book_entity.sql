CREATE SEQUENCE hibernate_sequence
  START 1
  INCREMENT BY 1;

CREATE TABLE book
(
  id     BIGINT PRIMARY KEY NOT NULL,
  author VARCHAR(255)       NOT NULL,
  notes  TEXT,
  title  VARCHAR(255)       NOT NULL
);