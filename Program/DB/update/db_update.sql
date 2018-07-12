RENAME TABLE friday_user_info TO milktea_user_info;
ALTER TABLE menu_info ADD COLUMN max_discount INT UNSIGNED NOT NULL DEFAULT 0 AFTER `code`, ADD COLUMN description TEXT NOT NULL AFTER shipping_fee;
ALTER TABLE order_info CHANGE COLUMN dish_sale_price final_price INT UNSIGNED NOT NULL;
ALTER TABLE dish_info ADD COLUMN `name` VARCHAR(125) NOT NULL AFTER shop_id;
ALTER TABLE order_info ADD COLUMN dish_id INT UNSIGNED NOT NULL AFTER shop_id;