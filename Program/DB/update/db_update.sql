-- world info
DROP TABLE IF EXISTS world_info;
CREATE TABLE world_info (
	id INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	event_flag INT NOT NULL
) ENGINE INNODB;
INSERT INTO world_info VALUES(1,0);

-- dish_rating_info
DROP TABLE IF EXISTS dish_rating_info;
CREATE TABLE dish_rating_info (
	id INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	shop_id INT UNSIGNED NOT NULL,
	`name` VARCHAR(125) NOT NULL,
	order_count INT UNSIGNED NOT NULL,
	star_count INT UNSIGNED NOT NULL,
	updated TIMESTAMP NOT NULL,
	INDEX(shop_id,`name`)
) ENGINE INNODB;

-- order info
ALTER TABLE order_info DROP COLUMN dish_id, ADD COLUMN attr_price INT UNSIGNED NOT NULL AFTER dish_price, 
	ADD COLUMN size VARCHAR(125) NOT NULL AFTER quantity, ADD COLUMN ice VARCHAR(125) NOT NULL AFTER size, ADD COLUMN sugar VARCHAR(125) NOT NULL AFTER ice;

-- menu info
ALTER TABLE menu_info ADD COLUMN expired DATETIME NOT NULL DEFAULT '1970-01-01 00:00:00' AFTER created;
UPDATE menu_info SET expired = (SELECT ADDDATE(created, INTERVAL 1 DAY));

-- drop shop_option_info
DROP TABLE IF EXISTS shop_option_info;

-- dish_info
ALTER TABLE dish_info DROP COLUMN `name`;