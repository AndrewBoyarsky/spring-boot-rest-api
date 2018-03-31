DELETE FROM products;
DELETE FROM categories;
ALTER SEQUENCE global_seq RESTART WITH 100000;
-- notebooks
INSERT INTO categories (name, description) VALUES
  ('Notebooks', 'Laptops, notebooks, transformers');
-- hardware
INSERT INTO categories (name, description) VALUES
  ('Computer hardware', 'Video cards, processors, motherboards');
INSERT INTO categories (name, description) VALUES
  ('DEFAULT_CATEGORY', '');
INSERT INTO products (name, description, price, category_id) VALUES
  ('Lenovo Ideapad 520', 'Business laptop with Windows 10', 739.99 * 100, 100000),
  ('Acer Swift 3', 'Light laptop for office work', 612.99 * 100, 100000);

INSERT INTO products (name, description, price, category_id) VALUES
  ('Asus GTX 1060 6gb', 'Powerful video adapter for 1080p', 549.99 * 100, 100001),
  ('MSI x299 Tomahawk', 'Ultimate motherboard for overclockers', 405.79 * 100, 100001),
  ('Intel Core i5-8600', 'Hexa-core processor with turbo boost', 181.45 * 100, 100001);

INSERT INTO products (name, description, price, category_id) VALUES
  ('Iphone X', 'New smartphone from Apple', 1205.99 * 100, 100002),
  ('D-link Dir-300', 'Router', 23.49 * 100, 100002),
  ('Canon Pixma mp340', 'Printer, scanner, xerox', 102.79 * 100, 100002);
