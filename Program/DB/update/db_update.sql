ALTER TABLE dish_rating_info ADD COLUMN `code` INT NOT NULL AFTER `name`;
UPDATE world_info SET event_flag = 3 WHERE id = 1;