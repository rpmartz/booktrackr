CREATE TABLE users
(
  id         UUID PRIMARY KEY NOT NULL,
  created    TIMESTAMP        NOT NULL,
  modified   TIMESTAMP        NOT NULL,
  version    INTEGER          NOT NULL,
  email      VARCHAR(255),
  enabled    BOOLEAN          NOT NULL DEFAULT FALSE,
  first_name VARCHAR(255),
  last_name  VARCHAR(255),
  password   VARCHAR(255)
);

CREATE UNIQUE INDEX uk_email ON users (email);