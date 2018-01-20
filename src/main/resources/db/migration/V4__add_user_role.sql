CREATE TABLE IF NOT EXISTS authorities
(
  id        BIGINT PRIMARY KEY NOT NULL,
  created   TIMESTAMP          NOT NULL,
  modified  TIMESTAMP          NOT NULL,
  version   INTEGER,
  authority VARCHAR(255),
  user_id   BIGINT             NOT NULL,
  CONSTRAINT fk_s21btj9mlob1djhm3amivbe5e FOREIGN KEY (user_id) REFERENCES users (id)
);