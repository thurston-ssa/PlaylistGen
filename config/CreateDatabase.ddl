CREATE DATABASE playlistdb;

USE playlistdb;

CREATE TABLE `shows` (
	`ID` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	`Name` VARCHAR(500) NULL DEFAULT NULL,	
	`imgUrl` VARCHAR(300) NULL DEFAULT NULL,
	`thumbUrl` VARCHAR(300) NULL DEFAULT NULL,
	PRIMARY KEY (`ID`)
)

;
CREATE TABLE `episodes` (

	`ID` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	`episodeId` INT(10) UNSIGNED NOT NULL,
	`name` VARCHAR(500) NULL DEFAULT NULL,
	`duration` INT(11) NOT NULL,
	`fileUrl` VARCHAR(300) NULL DEFAULT NULL,
	`showId` INT(10) UNSIGNED NOT NULL,
	`playlistId` INT(10) UNSIGNED NOT NULL,
	`playOrder` int(10) unsigned not null,
	PRIMARY KEY (ID),
	FOREIGN KEY (showId) REFERENCES shows(ID)
)
;

CREATE TABLE users
(id INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
email VARCHAR(255) NOT NULL,
salt VARCHAR(50) NOT NULL,
hash VARCHAR(50) NOT NULL,
firstname VARCHAR(50) NOT NULL,
lastname VARCHAR(50) NOT NULL,
street VARCHAR(50),
city VARCHAR(50),
state VARCHAR(2),
zip VARCHAR(9),
UNIQUE (email))
;


CREATE TABLE `playlists` (
	`ID` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	`Name` VARCHAR(500) NULL DEFAULT NULL,
	`UserId` int unsigned not null,
	`targetDuration` int unsigned,
	PRIMARY KEY (`ID`),
	foreign key (UserID) references users(ID) on delete cascade
)
;

