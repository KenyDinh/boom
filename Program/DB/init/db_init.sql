DROP TABLE IF EXISTS user_info;
CREATE TABLE user_info (
	id INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	username VARCHAR(32) NOT NULL,
	password VARCHAR(32) NOT NULL,
	flag INT NOT NULL,
	INDEX(username),
	UNIQUE(username)
) ENGINE INNODB;

DROP TABLE IF EXISTS milktea_user_info;
CREATE TABLE milktea_user_info (
	user_id INT UNSIGNED NOT NULL PRIMARY KEY,
	username VARCHAR(32) NOT NULL,
	dish_count INT UNSIGNED NOT NULL,
	order_count INT UNSIGNED NOT NULL,
	total_money INT UNSIGNED NOT NULL,
	total_sugar INT UNSIGNED NOT NULL,
	total_ice INT UNSIGNED NOT NULL,
	total_topping INT UNSIGNED NOT NULL,
	latest_order_id INT UNSIGNED NOT NULL,
	INDEX(user_id)
) ENGINE INNODB;

DROP TABLE IF EXISTS shop_info;
CREATE TABLE shop_info (
	id INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(55) NOT NULL,
	url VARCHAR(255) NOT NULL,
	address VARCHAR(255) NOT NULL,
	pre_image_url VARCHAR(255) NOT NULL,
	image_url VARCHAR(255) NOT NULL,
	opening_count INT UNSIGNED NOT NULL,
	ordered_dish_count INT UNSIGNED NOT NULL,
	voting_count INT UNSIGNED NOT NULL,
	star_count INT UNSIGNED NOT NULL,
	INDEX(url),
	UNIQUE(url)
) ENGINE INNODB;

DROP TABLE IF EXISTS dish_info;
CREATE TABLE dish_info (
	id INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	shop_id INT UNSIGNED NOT NULL,
	detail TEXT NOT NULL,
	INDEX(shop_id)
) ENGINE INNODB;

DROP TABLE IF EXISTS menu_info;
CREATE TABLE menu_info(
	id INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(255) NOT NULL,
	shop_id INT UNSIGNED NOT NULL,
	sale TINYINT UNSIGNED NOT NULL,
	code VARCHAR(30) NOT NULL,
	max_discount INT UNSIGNED NOT NULL,
	shipping_fee INT UNSIGNED NOT NULL,
	description TEXT NOT NULL,
	status TINYINT NOT NULL,
	flag INT NOT NULL,
	created DATETIME NOT NULL,
	expired DATETIME NOT NULL,
	updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	INDEX(shop_id)
) ENGINE INNODB;

DROP TABLE IF EXISTS order_info;
CREATE TABLE order_info(
	id INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	user_id INT UNSIGNED NOT NULL,
	username VARCHAR(32) NOT NULL,
	menu_id INT UNSIGNED NOT NULL,
	shop_id INT UNSIGNED NOT NULL,
	dish_name VARCHAR(125) NOT NULL,
	dish_type VARCHAR(125) NOT NULL,
	dish_price INT UNSIGNED NOT NULL,
	attr_price INT UNSIGNED NOT NULL,
	final_price INT UNSIGNED NOT NULL,
	dish_code INT NOT NULL,
	voting_star TINYINT UNSIGNED NOT NULL,
	quantity INT UNSIGNED NOT NULL,
	size VARCHAR(125) NOT NULL,
	ice VARCHAR(125) NOT NULL,
	sugar VARCHAR(125) NOT NULL,
	option_list TEXT NOT NULL,
	flag INT NOT NULL,
	created DATETIME NOT NULL,
	updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	INDEX(user_id),
	INDEX(menu_id),
	INDEX(shop_id),
	INDEX(shop_id, dish_code),
	INDEX(user_id, menu_id)
) ENGINE INNODB;

DROP TABLE IF EXISTS nihongo_user_info;
CREATE TABLE nihongo_user_info(
	user_id INT UNSIGNED NOT NULL PRIMARY KEY,
	username VARCHAR(32) NOT NULL,
	star INT NOT NULL,
	created DATETIME NOT NULL,
	updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	INDEX(user_id),
	INDEX(username)
) ENGINE INNODB;

DROP TABLE IF EXISTS nihongo_word_info;
CREATE TABLE nihongo_word_info(
	id INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	word VARCHAR(125) NOT NULL,
	sideword VARCHAR(55) NOT NULL,
	wordtype INT NOT NULL,
	meaning TEXT NOT NULL,
	description TEXT NOT NULL,
	reference INT NOT NULL,
	created DATETIME NOT NULL,
	updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE INNODB;

DROP TABLE IF EXISTS nihongo_progress_info;
CREATE TABLE nihongo_progress_info(
	id INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	user_id INT UNSIGNED NOT NULL,
	test_id INT NOT NULL,
	progress INT NOT NULL,
	created DATETIME NOT NULL,
	updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	INDEX(user_id, test_id)
) ENGINE INNODB;

DROP TABLE IF EXISTS nihongo_pet_info;
CREATE TABLE nihongo_pet_info(
	id INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	pet_name VARCHAR(125) NOT NULL,
	max_level INT NOT NULL,
	created DATETIME NOT NULL,
	updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	INDEX(pet_name)
) ENGINE INNODB;

DROP TABLE IF EXISTS nihongo_owning_info;
CREATE TABLE nihongo_owning_info(
	id INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	user_id INT UNSIGNED NOT NULL,
	pet_id INT UNSIGNED NOT NULL,
	current_level INT NOT NULL,
	created DATETIME NOT NULL,
	updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	INDEX(user_id, pet_id)
) ENGINE INNODB;

DROP TABLE IF EXISTS world_info;
CREATE TABLE world_info (
	id INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	event_flag INT NOT NULL
) ENGINE INNODB;

DROP TABLE IF EXISTS dish_rating_info;
CREATE TABLE dish_rating_info (
	id INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	shop_id INT UNSIGNED NOT NULL,
	name VARCHAR(125) NOT NULL,
	code INT NOT NULL,
	order_count INT UNSIGNED NOT NULL,
	star_count INT UNSIGNED NOT NULL,
	updated TIMESTAMP NOT NULL,
	INDEX(shop_id,name)
) ENGINE INNODB;

DROP TABLE IF EXISTS survey_info;
CREATE TABLE survey_info (
	id INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(255) NOT NULL,
	status TINYINT NOT NULL,
	description TEXT NOT NULL,
	max_choice TINYINT NOT NULL,
	max_retry TINYINT NOT NULL,
	created DATETIME NOT NULL,
	expired DATETIME NOT NULL,
	updated TIMESTAMP NOT NULL,
	INDEX(status)
) ENGINE INNODB;

DROP TABLE IF EXISTS survey_option_info;
CREATE TABLE survey_option_info (
	id INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	survey_id INT UNSIGNED NOT NULL,
	name VARCHAR(255) NOT NULL,
	content TEXT NOT NULL,
	image VARCHAR(255) NOT NULL,
	video VARCHAR(255) NOT NULL,
	ref_url VARCHAR(255) NOT NULL,
	INDEX(survey_id)
) ENGINE INNODB;

DROP TABLE IF EXISTS survey_result_info;
CREATE TABLE survey_result_info (
	id INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	survey_id INT UNSIGNED NOT NULL,
	user VARCHAR(32) NOT NULL,
	result TEXT NOT NULL,
	retry_remain TINYINT NOT NULL,
	created DATETIME NOT NULL,
	INDEX(user),
	INDEX(survey_id)
) ENGINE INNODB;

DROP TABLE IF EXiSTS cannon_block_info;
CREATE TABLE cannon_block_info (
	id INT UNSIGNED NOT  NULL PRIMARY KEY AUTO_INCREMENT,
	user_id INT UNSIGNED NOT NULL,        # userID...
	user_board VARCHAR(128) NOT NULL,
	bot_board VARCHAR(128) NOT NULL,
	user_hp INT UNSIGNED NOT NULL,
	bot_hp INT UNSIGNED NOT NULL,
	status TINYINT NOT NULL DEFAULT 0
) ENGINE INNODB;

DROP TABLE IF EXISTS cannon_player_info;
CREATE TABLE cannon_player_info (
	user_id INT UNSIGNED NOT NULL,  
	username VARCHAR(32) NOT NULL,
	score INT NOT NULL,
	status TINYINT NOT NULL DEFAULT 0,
	created DATETIME NOT NULL,
	updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	INDEX(user_id),
	INDEX(username)
) ENGINE INNODB;

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