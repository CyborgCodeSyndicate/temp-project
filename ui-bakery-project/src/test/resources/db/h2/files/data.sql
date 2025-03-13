INSERT INTO orders
  (id, customerName, customerDetails, phoneNumber, location, product)
VALUES
  (1, 'John Terry', 'Address', '+1-555-7777', 'Bakery', 'Strawberry Bun'),
  (2, 'Petar Terry', 'Address', '+1-222-7778', 'Store', 'Strawberry Bun');

INSERT INTO sellers
  (id, email, password)
VALUES
  (1, 'barista@vaadin.com', 'barista'),
  (2, 'admin@vaadin.com', 'admin');