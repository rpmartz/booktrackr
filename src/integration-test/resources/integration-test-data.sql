DELETE FROM book
WHERE id is not null;

INSERT INTO book (id, author, notes, title) VALUES ('09c4d7e2-01e5-4dea-a8cd-f3bfc908b316', 'George Orwell', 'A look into the future'::TEXT, '1984');
INSERT INTO book (id, author, notes, title) VALUES ('09c4d7e2-01e5-4dea-a8cd-f3bfc908b317', 'F. Scott Fitzgerald', 'The Roaring 20s', 'The Great Gatsby');
