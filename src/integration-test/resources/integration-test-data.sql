DELETE FROM book
WHERE id is not null;
DELETE FROM authorities
WHERE id IS NOT NULL;
DELETE FROM users
WHERE id IS NOT NULL;

INSERT INTO users (id, created, modified, version, email, enabled, first_name, last_name, password)
VALUES (1, '2016-03-26 21:08:58.551000', '2016-03-26 21:08:58.551000', 0,
        'booktrackr@ryanpmartz.com',
        TRUE, 'Ryan', 'Martz',
        '$2a$10$Fo4wvoGJUAGV5POUr7hDD.bKNrLuQhQU9.IH7YY93Lv7RX4Zd4ZqK');

INSERT INTO authorities (id, created, modified, version, authority, user_id)
VALUES
  (1, '2016-03-26 21:08:58.466000', '2016-03-26 21:08:58.466000', 0, 'ROLE_USER',
   1);

INSERT INTO book (id, created, modified, version, author, notes, title, user_id) VALUES
  (2, '2016-03-26 21:08:58.551000', '2016-03-26 21:08:58.551000', 0, 'George Orwell', 'A look into the future' :: TEXT,
   '1984',
   1);
INSERT INTO book (id, created, modified, version, author, notes, title, user_id) VALUES
  (3, '2016-03-26 21:08:58.551000', '2016-03-26 21:08:58.551000', 0, 'F. Scott Fitzgerald', 'The Roaring 20s',
   'The Great Gatsby',
   1);
