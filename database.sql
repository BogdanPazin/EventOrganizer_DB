CREATE DATABASE IF NOT EXISTS `events`;
USE `events`;

DROP TABLE IF EXISTS `confirmed`;
DROP TABLE IF EXISTS `declined`;
DROP TABLE IF EXISTS `notShownUp`;

DROP TABLE IF EXISTS `guest`;
CREATE TABLE `guest` (
	`id` int(99) NOT NULL AUTO_INCREMENT,
	`name` varchar(45) DEFAULT NULL,
	`email` varchar(45) DEFAULT NULL,
	`number` varchar(45) DEFAULT NULL,
    `attended` int(99) DEFAULT 0,
    `confirmed` int(99) DEFAULT 0,
    `notShownUp` int(99) DEFAULT 0,
    `declined` int(99) DEFAULT 0,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `event`;
CREATE TABLE `event` (
	`id` int(11) NOT NULL AUTO_INCREMENT,
	`theme` varchar(45) DEFAULT NULL,
	`location` varchar(45) DEFAULT NULL,
	`date` DATE DEFAULT NULL,
    `completed` TINYINT(1) DEFAULT 0,
    `canceled` TINYINT(1) DEFAULT 0,
    `upcoming` TINYINT(1) DEFAULT 0,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB;

CREATE TABLE `confirmed` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `event_id` INT NOT NULL,
    `guest_id` INT NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`event_id`) REFERENCES `event`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`guest_id`) REFERENCES `guest`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE `declined` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `event_id` INT NOT NULL,
    `guest_id` INT NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`event_id`) REFERENCES `event`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`guest_id`) REFERENCES `guest`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE `notShownUp` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `event_id` INT NOT NULL,
    `guest_id` INT NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`event_id`) REFERENCES `event`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`guest_id`) REFERENCES `guest`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;