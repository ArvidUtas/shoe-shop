use shoeshop;
select * from product;
select * from orders;
select * from orders_contains_product;
-- ALTER TABLE customer ADD COLUMN password VARCHAR(20);
-- UPDATE customer SET password = 'abc123' WHERE id=1;
-- UPDATE customer SET password = 'def123' WHERE id=2;
-- ALTER TABLE orders ADD COLUMN isActive boolean default TRUE;
-- UPDATE orders SET isActive = FALSE;

DROP PROCEDURE IF EXISTS addToCart;
delimiter //
CREATE PROCEDURE addToCart (IN customerID INT, IN orderID INT, IN productID INT)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
        BEGIN
            ROLLBACK;
            RESIGNAL SET MESSAGE_TEXT = 'Error while running.';
        END;
    -- TODO: Lägg in mer specifika fel hanterare: DECLARE EXIT HANDLER FOR 1234
    START TRANSACTION ;
        IF orderID IS NULL AND
           (SELECT count(*) FROM orders WHERE orders.isActive = TRUE AND customerID = orders.customer_id) = 0 THEN
            INSERT INTO orders (customer_id) VALUES (customerID);
            INSERT INTO orders_contains_product (product_id, orders_id, price)
            VALUES (productID, LAST_INSERT_ID(), (SELECT price FROM product WHERE product.id = productID));
            UPDATE product SET stock = stock - 1 WHERE id = productID;
        ELSEIF orderID IS NOT NULL AND
               (SELECT count(*) FROM orders WHERE orders.isActive = TRUE AND customerID = orders.customer_id) = 1 THEN
            INSERT INTO orders_contains_product (product_id, orders_id, price)
            VALUES (productID, orderID, (SELECT price FROM product WHERE product.id = productID));
            UPDATE product SET stock = stock - 1 WHERE id = productID;
        ELSE
            -- returnera felmeddelande?
        END IF;
END//
delimiter ;

