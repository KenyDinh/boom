DROP TABLE IF EXISTS quiz_info;
CREATE TABLE quiz_info (
	id INT UNSIGNED NOT NULL PRIMARY KEY,
	host INT UNSIGNED,
	name VARCHAR(32) NOT NULL,
	subject tinyint not null,
	level tinyint not null,
	max_player tinyint not null,
	question_num tinyint not null,
	time_per_question int unsigned not null,
	status tinyint not null,
	player_num tinyint not null,
	current_question tinyint not null,
	current_question_data text not null,
	question_data text not null,
	created datetime not null,
	expired datetime not null,
	updated timestamp not null,
	UNIQUE(name,created),
	INDEX(host),
	INDEX(name)
) ENGINE INNODB;

DROP TABLE IF EXISTS quiz_player_info;
CREATE TABLE quiz_player_info (
	user_id INT UNSIGNED NOT NULL PRIMARY KEY,
	username VARCHAR(32) NOT NULL,
	quiz_id INT UNSIGNED NOT NULL,
	status TINYINT NOT NULL,
	answer VARCHAR(16) NOT NULL,
	correct_count TINYINT NOT NULL,
	correct_point INT NOT NULL,
	updated TIMESTAMP NOT NULL,
	INDEX(quiz_id)
) ENGINE INNODB;