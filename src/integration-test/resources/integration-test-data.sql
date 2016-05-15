DELETE FROM book
WHERE id is not null;
DELETE FROM authorities
WHERE id IS NOT NULL;
DELETE FROM users
WHERE id IS NOT NULL;

INSERT INTO users (id, created, modified, version, email, enabled, first_name, last_name, password)
VALUES ('abee7ba3-8bf7-496c-a19a-de0471fc06c1', '2016-03-26 21:08:58.551000', '2016-03-26 21:08:58.551000', 0,
        'martzrp@gmail.com',
        TRUE, 'Ryan', 'Martz',
        '$2a$10$Fo4wvoGJUAGV5POUr7hDD.bKNrLuQhQU9.IH7YY93Lv7RX4Zd4ZqK');

INSERT INTO authorities (id, created, modified, version, authority, user_id)
VALUES
  ('6d61facc-04a4-420c-aa66-ca0122a055ea', '2016-03-26 21:08:58.466000', '2016-03-26 21:08:58.466000', 0, 'ROLE_USER',
   'abee7ba3-8bf7-496c-a19a-de0471fc06c1');

INSERT INTO book (id, author, notes, title, user_id) VALUES
  ('09c4d7e2-01e5-4dea-a8cd-f3bfc908b316', 'George Orwell', 'A look into the future' :: TEXT, '1984',
   'abee7ba3-8bf7-496c-a19a-de0471fc06c1');
INSERT INTO book (id, author, notes, title, user_id) VALUES
  ('09c4d7e2-01e5-4dea-a8cd-f3bfc908b317', 'F. Scott Fitzgerald', 'The Roaring 20s', 'The Great Gatsby',
   'abee7ba3-8bf7-496c-a19a-de0471fc06c1');
