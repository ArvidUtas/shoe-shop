use shoeshop;


call addToCart(1,2,null);

DROP PROCEDURE IF EXISTS addToCart;
delimiter //
CREATE PROCEDURE addToCart (IN customerID INT, IN productID INT, IN orderID INT, OUT affectedRows INT)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
        BEGIN
            ROLLBACK;
            RESIGNAL SET MESSAGE_TEXT = 'Error while running.';
        END;
    -- TODO: Lägg in mer specifika fel hanterare: DECLARE EXIT HANDLER FOR 1234
    START TRANSACTION;
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
        -- ELSE
            -- returnera felmeddelande?
        END IF;
        SET affectedRows = ROW_COUNT();
        SELECT orders_id FROM orders_contains_product ORDER BY lastUpdate DESC LIMIT 1 ;
    COMMIT;
END//
delimiter ;


CREATE TABLE out_of_stock (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    time_added TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE);

DROP TRIGGER IF EXISTS after_product_update;
delimiter //
CREATE TRIGGER after_product_update AFTER UPDATE ON product
    FOR EACH ROW
    BEGIN
        IF NEW.stock = 0 THEN
            INSERT INTO out_of_stock (product_id) VALUES (NEW.id);
        END IF;
    END //
delimiter ;