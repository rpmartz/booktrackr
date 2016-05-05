DELETE FROM book
WHERE id >= 1;

INSERT INTO book (id, author, notes, title) VALUES ('1', 'George Orwell', 'A look into the future'::TEXT, '1984');
INSERT INTO book (id, author, notes, title) VALUES ('2', 'F. Scott Fitzgerald', 'The Roaring 20s', 'The Great Gatsby');
