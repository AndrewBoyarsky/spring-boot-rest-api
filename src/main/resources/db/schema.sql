DROP TABLE products IF EXISTS;
DROP TABLE categories IF EXISTS;
DROP SEQUENCE global_seq IF EXISTS;

CREATE SEQUENCE GLOBAL_SEQ AS INTEGER START WITH 100000;

CREATE TABLE categories (
  id INTEGER GENERATED BY DEFAULT AS SEQUENCE GLOBAL_SEQ PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(255) NOT NULL
);
CREATE UNIQUE INDEX categories_unique_name_idx ON CATEGORIES (name);

CREATE TABLE products (
  id INTEGER GENERATED BY DEFAULT AS SEQUENCE GLOBAL_SEQ PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(255) NOT NULL,
  price INT NOT NULL,
  category_id INTEGER NOT NULL,
  FOREIGN KEY (category_id) REFERENCES CATEGORIES (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX products_unique_name_idx ON PRODUCTS (name);
