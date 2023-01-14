/*
SQLyog Community
MySQL - 5.7.27-log : Database - boom
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`boom` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;

USE `boom`;

/*Table structure for table `auth_token_info` */

DROP TABLE IF EXISTS `auth_token_info`;

CREATE TABLE `auth_token_info` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `token` varchar(12) COLLATE utf8mb4_unicode_ci NOT NULL,
  `validator` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` int(10) unsigned NOT NULL,
  `expired` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `token` (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `carousel_info` */

DROP TABLE IF EXISTS `carousel_info`;

CREATE TABLE `carousel_info` (
  `id` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  `name` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `description` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `local` tinyint(4) NOT NULL,
  `available` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `demo_session_info` */

DROP TABLE IF EXISTS `demo_session_info`;

CREATE TABLE `demo_session_info` (
  `id` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  `demo_time` datetime NOT NULL,
  `demo_location` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Table structure for table `demo_signup_info` */

DROP TABLE IF EXISTS `demo_signup_info`;

CREATE TABLE `demo_signup_info` (
  `id` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  `game_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `speaker_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text NOT NULL,
  `flag` smallint(5) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Table structure for table `device_info` */

DROP TABLE IF EXISTS `device_info`;

CREATE TABLE `device_info` (
  `id` smallint(8) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(125) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `serial` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `image` varchar(18) COLLATE utf8mb4_unicode_ci NOT NULL,
  `dept` tinyint(4) NOT NULL,
  `type` tinyint(4) NOT NULL,
  `status` tinyint(4) NOT NULL,
  `buy_date` datetime NOT NULL DEFAULT '1970-01-01 00:00:00',
  `note` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `hold_date` datetime NOT NULL,
  `release_date` datetime NOT NULL,
  `extend_date` datetime NOT NULL DEFAULT '1970-01-01 00:00:00',
  `user_id` int(10) unsigned NOT NULL,
  `username` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `available` tinyint(4) NOT NULL,
  `flag` int(11) NOT NULL,
  `regist_count` int(11) NOT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `device_register_info` */

DROP TABLE IF EXISTS `device_register_info`;

CREATE TABLE `device_register_info` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `device_id` smallint(5) unsigned NOT NULL,
  `device_name` varchar(125) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` int(10) unsigned NOT NULL,
  `username` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `expired` tinyint(4) NOT NULL,
  `start_date` datetime NOT NULL,
  `end_date` datetime NOT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_device` (`device_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `dish_info` */

DROP TABLE IF EXISTS `dish_info`;

CREATE TABLE `dish_info` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `shop_id` int(10) unsigned NOT NULL,
  `detail` text COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `shop_id` (`shop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `dish_rating_info` */

DROP TABLE IF EXISTS `dish_rating_info`;

CREATE TABLE `dish_rating_info` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `shop_id` int(10) unsigned NOT NULL,
  `name` varchar(125) CHARACTER SET utf8 NOT NULL,
  `code` int(11) NOT NULL,
  `order_count` int(10) unsigned NOT NULL,
  `star_count` int(10) unsigned NOT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `shop_id` (`shop_id`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `manage_log_info` */

DROP TABLE IF EXISTS `manage_log_info`;

CREATE TABLE `manage_log_info` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `username` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` tinyint(4) NOT NULL,
  `param` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `menu_info` */

DROP TABLE IF EXISTS `menu_info`;

CREATE TABLE `menu_info` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 NOT NULL,
  `shop_id` int(10) unsigned NOT NULL,
  `sale` tinyint(3) unsigned NOT NULL,
  `code` varchar(30) CHARACTER SET utf8 NOT NULL,
  `max_discount` int(10) unsigned NOT NULL,
  `shipping_fee` int(10) unsigned NOT NULL,
  `description` text CHARACTER SET utf8 NOT NULL,
  `status` tinyint(4) NOT NULL,
  `dept` int(11) NOT NULL,
  `flag` int(11) NOT NULL,
  `created` datetime NOT NULL,
  `expired` datetime NOT NULL DEFAULT '1970-01-01 00:00:00',
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `shop_id` (`shop_id`),
  KEY `status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `milktea_user_info` */

DROP TABLE IF EXISTS `milktea_user_info`;

CREATE TABLE `milktea_user_info` (
  `user_id` int(10) unsigned NOT NULL,
  `username` varchar(32) CHARACTER SET utf8 NOT NULL,
  `dish_count` int(10) unsigned NOT NULL,
  `order_count` int(10) unsigned NOT NULL,
  `total_money` int(10) unsigned NOT NULL,
  `total_sugar` int(10) unsigned NOT NULL,
  `total_ice` int(10) unsigned NOT NULL,
  `total_topping` int(10) unsigned NOT NULL,
  `free_ticket` tinyint(4) NOT NULL,
  `latest_order_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`user_id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `nihongo_owning_info` */

DROP TABLE IF EXISTS `nihongo_owning_info`;

CREATE TABLE `nihongo_owning_info` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `pet_id` int(10) unsigned NOT NULL,
  `current_level` int(11) NOT NULL,
  `created` datetime NOT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`,`pet_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `nihongo_pet_info` */

DROP TABLE IF EXISTS `nihongo_pet_info`;

CREATE TABLE `nihongo_pet_info` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `pet_name` varchar(125) CHARACTER SET utf8 NOT NULL,
  `max_level` int(11) NOT NULL,
  `created` datetime NOT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `pet_name` (`pet_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `nihongo_progress_info` */

DROP TABLE IF EXISTS `nihongo_progress_info`;

CREATE TABLE `nihongo_progress_info` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `test_id` int(11) NOT NULL,
  `progress` int(11) NOT NULL,
  `created` datetime NOT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`,`test_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `nihongo_user_info` */

DROP TABLE IF EXISTS `nihongo_user_info`;

CREATE TABLE `nihongo_user_info` (
  `user_id` int(10) unsigned NOT NULL,
  `username` varchar(32) CHARACTER SET utf8 NOT NULL,
  `star` int(11) NOT NULL,
  `created` datetime NOT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  KEY `user_id` (`user_id`),
  KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `nihongo_word_info` */

DROP TABLE IF EXISTS `nihongo_word_info`;

CREATE TABLE `nihongo_word_info` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `word` varchar(125) CHARACTER SET utf8 NOT NULL,
  `sideword` varchar(55) CHARACTER SET utf8 NOT NULL,
  `wordtype` int(11) NOT NULL,
  `meaning` text CHARACTER SET utf8 NOT NULL,
  `description` text CHARACTER SET utf8 NOT NULL,
  `reference` int(11) NOT NULL,
  `created` datetime NOT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `order_info` */

DROP TABLE IF EXISTS `order_info`;

CREATE TABLE `order_info` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `username` varchar(32) CHARACTER SET utf8 NOT NULL,
  `menu_id` int(10) unsigned NOT NULL,
  `shop_id` int(10) unsigned NOT NULL,
  `dish_name` varchar(125) CHARACTER SET utf8 NOT NULL,
  `dish_type` varchar(125) CHARACTER SET utf8 NOT NULL,
  `dish_price` int(10) unsigned NOT NULL,
  `attr_price` int(10) unsigned NOT NULL,
  `final_price` int(10) unsigned NOT NULL,
  `dish_code` int(11) NOT NULL,
  `voting_star` tinyint(3) unsigned NOT NULL,
  `comment` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `quantity` int(10) unsigned NOT NULL,
  `size` varchar(125) CHARACTER SET utf8 NOT NULL,
  `ice` varchar(125) CHARACTER SET utf8 NOT NULL,
  `sugar` varchar(125) CHARACTER SET utf8 NOT NULL,
  `option_list` text CHARACTER SET utf8 NOT NULL,
  `flag` int(11) NOT NULL,
  `created` datetime NOT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `menu_id` (`menu_id`),
  KEY `shop_id` (`shop_id`),
  KEY `shop_id_2` (`shop_id`,`dish_code`),
  KEY `user_id_2` (`user_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `quiz_info` */

DROP TABLE IF EXISTS `quiz_info`;

CREATE TABLE `quiz_info` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `host` int(10) unsigned DEFAULT NULL,
  `name` varchar(128) NOT NULL,
  `subject` tinyint(4) NOT NULL,
  `level` tinyint(4) NOT NULL,
  `max_player` tinyint(4) NOT NULL,
  `question_num` tinyint(4) NOT NULL,
  `time_per_question` int(10) unsigned NOT NULL,
  `status` tinyint(4) NOT NULL,
  `retry` tinyint(4) NOT NULL,
  `flag` tinyint(4) NOT NULL,
  `player_num` tinyint(4) NOT NULL,
  `current_question` tinyint(4) NOT NULL,
  `current_option_order` tinytext NOT NULL,
  `question_data` text NOT NULL,
  `created` datetime NOT NULL,
  `expired` datetime NOT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`,`created`),
  KEY `host` (`host`),
  KEY `name_2` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Table structure for table `quiz_log_info` */

DROP TABLE IF EXISTS `quiz_log_info`;

CREATE TABLE `quiz_log_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `quiz_id` int(10) unsigned NOT NULL,
  `question_index` tinyint(4) NOT NULL,
  `user_id` int(10) unsigned NOT NULL,
  `username` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `correct_answer` varchar(3) COLLATE utf8mb4_unicode_ci NOT NULL,
  `player_answer` varchar(3) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `quiz_player_info` */

DROP TABLE IF EXISTS `quiz_player_info`;

CREATE TABLE `quiz_player_info` (
  `user_id` int(10) unsigned NOT NULL,
  `username` varchar(32) NOT NULL,
  `quiz_id` int(10) unsigned NOT NULL,
  `status` tinyint(4) NOT NULL,
  `retry` tinyint(4) NOT NULL,
  `answer` varchar(16) NOT NULL,
  `correct_count` tinyint(4) NOT NULL,
  `correct_point` int(11) NOT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  KEY `quiz_id` (`quiz_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Table structure for table `shop_info` */

DROP TABLE IF EXISTS `shop_info`;

CREATE TABLE `shop_info` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(55) CHARACTER SET utf8 NOT NULL,
  `url` varchar(255) CHARACTER SET utf8 NOT NULL,
  `address` varchar(255) CHARACTER SET utf8 NOT NULL,
  `pre_image_url` varchar(255) CHARACTER SET utf8 NOT NULL,
  `image_url` varchar(255) CHARACTER SET utf8 NOT NULL,
  `opening_count` int(10) unsigned NOT NULL,
  `ordered_dish_count` int(10) unsigned NOT NULL,
  `voting_count` int(10) unsigned NOT NULL,
  `star_count` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `url_unique` (`url`),
  KEY `open_count` (`opening_count`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `survey_info` */

DROP TABLE IF EXISTS `survey_info`;

CREATE TABLE `survey_info` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(125) COLLATE utf8mb4_unicode_ci NOT NULL,
  `pathname` varchar(125) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` tinyint(4) NOT NULL,
  `type` tinyint(4) NOT NULL,
  `flag` int(11) NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `created` datetime NOT NULL,
  `expired` datetime NOT NULL,
  `updated` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `survey_option_info` */

DROP TABLE IF EXISTS `survey_option_info`;

CREATE TABLE `survey_option_info` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `question_id` int(10) unsigned NOT NULL,
  `type` tinyint(4) NOT NULL,
  `title` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `param` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `question` (`question_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `survey_question_info` */

DROP TABLE IF EXISTS `survey_question_info`;

CREATE TABLE `survey_question_info` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `survey_id` int(10) unsigned NOT NULL,
  `idx` tinyint(4) NOT NULL,
  `type` tinyint(4) NOT NULL,
  `title` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `min_choice` int(11) NOT NULL,
  `max_choice` int(11) NOT NULL,
  `optional` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `survey` (`survey_id`,`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `survey_result_info` */

DROP TABLE IF EXISTS `survey_result_info`;

CREATE TABLE `survey_result_info` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `survey_id` int(10) unsigned NOT NULL,
  `user_id` varchar(12) COLLATE utf8mb4_unicode_ci NOT NULL,
  `username` varchar(125) COLLATE utf8mb4_unicode_ci NOT NULL,
  `department` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `result` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `progress` tinyint(4) NOT NULL,
  `updated` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `survey_user` (`survey_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `survey_user_access_info` */

DROP TABLE IF EXISTS `survey_user_access_info`;

CREATE TABLE `survey_user_access_info` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `survey_id` int(10) unsigned NOT NULL,
  `user_code` varchar(6) COLLATE utf8mb4_unicode_ci NOT NULL,
  `flag` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `survey_id` (`survey_id`,`user_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `user_info` */

DROP TABLE IF EXISTS `user_info`;

CREATE TABLE `user_info` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(32) CHARACTER SET utf8 NOT NULL,
  `password` varchar(32) CHARACTER SET utf8 NOT NULL,
  `empid` varchar(6) COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(125) COLLATE utf8mb4_unicode_ci NOT NULL,
  `role` int(11) NOT NULL,
  `dept` int(11) NOT NULL,
  `flag` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `world_info` */

DROP TABLE IF EXISTS `world_info`;

CREATE TABLE `world_info` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `event_flag` int(11) NOT NULL,
  `game_flag` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
