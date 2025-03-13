CREATE TABLE orders (
    id INT PRIMARY KEY,
    customerName VARCHAR(255),
    customerDetails VARCHAR(255),
    phoneNumber VARCHAR(50),
    location VARCHAR(255),
    product VARCHAR(255)
);

CREATE TABLE sellers (
    id INT PRIMARY KEY,
    email VARCHAR(255),
    password VARCHAR(255)
);