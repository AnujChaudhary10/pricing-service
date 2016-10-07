DROP TABLE products_price IF EXISTS;

CREATE TABLE products_price (
  product_id   INTEGER PRIMARY KEY,
  product_price DOUBLE PRECISION
);
