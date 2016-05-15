ALTER TABLE book
  ADD COLUMN user_id UUID;
ALTER TABLE book
  ADD CONSTRAINT fk_s21btj9mlob1djhm3amivbe FOREIGN KEY (user_id) REFERENCES users (id);
CREATE INDEX bk_user_idx ON book (user_id);