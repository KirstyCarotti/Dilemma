BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS `User` (
	`UserID`	INTEGER,
	`Username`	TEXT,
	`Email`	TEXT,
	`Password`	TEXT,
	`CategoryID`	INTEGER,
	PRIMARY KEY(`UserID`)
);
CREATE TABLE IF NOT EXISTS `Question` (
	`QuestionID`	INTEGER,
	`UserID`	INTEGER,
	`Question`	TEXT,
	`CategoryID`	INTEGER,
	PRIMARY KEY(`QuestionID`)
);
CREATE TABLE IF NOT EXISTS `Preferences` (
	`UserID`	INTEGER,
	`ShowComment`	INTEGER,
	`Anonymous`	INTEGER,
	PRIMARY KEY(`UserID`)
);
CREATE TABLE IF NOT EXISTS `Friend` (
	`UserID`	INTEGER,
	`FriendID`	INTEGER,
	PRIMARY KEY(`UserID`,`FriendID`)
);
CREATE TABLE IF NOT EXISTS `Comment` (
	`CommentID`	INTEGER,
	`UserID`	INTEGER,
	`AnswerID`	INTEGER,
	`CommentDescription`	TEXT,
	PRIMARY KEY(`CommentID`)
);
CREATE TABLE IF NOT EXISTS `Category` (
	`CategoryID`	INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,
	`CategoryName`	TEXT
);
CREATE TABLE IF NOT EXISTS `Answer` (
	`AnswerID`	INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,
	`QuestionID`	INTEGER,
	`AnswerText`	TEXT,
	`NoOfClicks`	INTEGER,
	`Colour`	INTEGER,
	FOREIGN KEY(`QuestionID`) REFERENCES `Question`(`QuestionID`)
);
COMMIT;
