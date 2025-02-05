use shoeshop;

select * from model;
select * from brand;
select * from customer;
select * from product;
select * from out_of_stock;
select * from orders;
select * from orders_contains_product;
-- ALTER TABLE customer ADD COLUMN password VARCHAR(20);
-- ALTER TABLE customer ADD COLUMN email VARCHAR(30) UNIQUE ;
-- UPDATE customer SET password = 'abc123' WHERE id=1;
-- UPDATE customer SET password = 'def123' WHERE id=2;
-- ALTER TABLE orders ADD COLUMN isActive boolean default TRUE;
-- ALTER TABLE orders ADD COLUMN lastUpdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;
-- ALTER TABLE orders_contains_product ADD COLUMN lastUpdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;
-- UPDATE orders SET isActive = FALSE;
SELECT p.id, brand.name as brand, m.name, m.description, p.size, p.colour from brand
                                                                                   INNER JOIN shoeshop.model m on brand.id = m.brand_id
                                                                                   INNER JOIN shoeshop.product p on m.id = p.model_id WHERE p.stock != 0;
SELECT customer.id, firstname, lastname, address, postcode,
       (SELECT orders.id FROM shoeshop.orders
        WHERE orders.customer_id = customer.id
          AND orders.isActive = TRUE
        ORDER BY orders.order_time DESC
        LIMIT 1) AS activeOrder FROM shoeshop.customer
WHERE email = 'jamesbrown@a.se' AND password = 'def123' LIMIT 1;

SELECT p.id, brand.name as brand, m.name as model, m.description, p.size, p.colour, p.price
                            FROM shoeshop.brand
                            INNER JOIN shoeshop.model m ON brand.id = m.brand_id
                            INNER JOIN shoeshop.product p ON m.id = p.model_id
                            INNER JOIN shoeshop.orders_contains_product ocp ON m.id = p.model_id
                            WHERE ocp.orders_id = 2;

SELECT p.id, brand.name as brand, m.name as model, m.description, p.size, p.colour, p.price
FROM shoeshop.orders_contains_product ocp
         INNER JOIN shoeshop.product p ON ocp.product_id = p.id
         INNER JOIN shoeshop.model m ON p.model_id = m.id
         INNER JOIN shoeshop.brand ON m.brand_id = brand.id
WHERE ocp.orders_id = ?;

select * from orders_contains_product ocp where ocp.orders_id = 4;