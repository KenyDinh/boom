DROP TABLE IF EXISTS user_ticket_info;
CREATE TABLE user_ticket_info (
	id INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	user_id INT UNSIGNED NOT NULL,
	ticket_type TINYINT UNSIGNED NOT NULL,
	ticket_remain TINYINT UNSIGNED NOT NULL,
	total_num SMALLINT UNSIGNED NOT NULL,
	UNIQUE(user_id,ticket_type),
	INDEX(user_id,ticket_type)
) ENGINE INNODB;

INSERT INTO user_ticket_info (SELECT 0, user_id, 1, free_ticket, free_ticket FROM milktea_user_info WHERE free_ticket > 0);
UPDATE order_info SET flag = flag & ~(1 << 5);
ALTER TABLE order_info ADD COLUMN ticket TINYINT UNSIGNED NOT NULL AFTER flag;
ALTER TABLE milktea_user_info DROP COLUMN free_ticket;