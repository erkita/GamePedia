CREATE SCHEMA IF NOT EXISTS GamePedia;

USE GamePedia;

DROP TABLE IF EXISTS UserReviews;
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS GameTagLinkers;
DROP TABLE IF EXISTS PopularTags;
DROP TABLE IF EXISTS GamePublisherLinkers;
DROP TABLE IF EXISTS Publishers;
DROP TABLE IF EXISTS GameDeveloperLinkers;
DROP TABLE IF EXISTS Developers;
DROP TABLE IF EXISTS GivenReviews;
DROP TABLE IF EXISTS GamePlatformLinkers;
DROP TABLE IF EXISTS Platforms;
DROP TABLE IF EXISTS GameGenreLinkers;
DROP TABLE IF EXISTS Genres;
DROP TABLE IF EXISTS GameNumplayersLinkers;
DROP TABLE IF EXISTS NumPlayers;
DROP TABLE IF EXISTS Games;


CREATE TABLE Games (
	GameId INT,
	GameName VARCHAR(255),
	Description TEXT,
	ReleaseDate DATE,
	Price DECIMAL(5,2),
	PicLink VARCHAR(255),
	CONSTRAINT pk_Games_GameId PRIMARY KEY (GameId)
);


CREATE TABLE NumPlayers (
	NumPlayersId INT,
	NumberName varchar(20),
	CONSTRAINT pk_NumPlayers_NumPlayersId PRIMARY KEY (NumPlayersId)
);

CREATE TABLE GameNumplayersLinkers (
	GNLinkerId INT AUTO_INCREMENT,
	GameId INT,
	NumPlayersId INT,
	CONSTRAINT pk_GameNumplayersLinkers_GNLinkerId  PRIMARY KEY(GNLinkerId),
	CONSTRAINT fk_GameNumplayersLinkers_GameId FOREIGN KEY(GameId)
		REFERENCES Games(GameId)
		ON UPDATE CASCADE ON DELETE SET NULL,
	CONSTRAINT fk_GameNumplayersLinkers_NumPlayersId FOREIGN KEY(NumPlayersId)
		REFERENCES NumPlayers(NumPlayersId)
		ON UPDATE CASCADE ON DELETE SET NULL	
);


CREATE TABLE Genres (
	GenreId INT,
	GenreName VARCHAR(255),
	CONSTRAINT pk_Genres_GenreId PRIMARY KEY (GenreId)
);

CREATE TABLE GameGenreLinkers (
	GGLinkerId INT AUTO_INCREMENT,
	GameId INT,
	GenreId INT,
	CONSTRAINT pk_GameGenreLinkers_GGLinkerId  PRIMARY KEY(GGLinkerId),
	CONSTRAINT fk_GameGenreLinkers_GameId FOREIGN KEY(GameId)
		REFERENCES Games(GameId)
		ON UPDATE CASCADE ON DELETE SET NULL,
	CONSTRAINT fk_GameGenreLinkers_GameGenreId FOREIGN KEY(GenreId)
		REFERENCES Genres(GenreId)
		ON UPDATE CASCADE ON DELETE SET NULL	
);



CREATE TABLE Platforms (
	PlatformId INT AUTO_INCREMENT,
    PlatformName VARCHAR(10),
	CONSTRAINT pk_Platforms_PlatformId PRIMARY KEY(PlatformId)
);


CREATE TABLE GamePlatformLinkers(
	GPId INT,
	GameId INT,
	PlatformId INT,
	CONSTRAINT pk_GamePlatformLinkers_GPId  PRIMARY KEY(GPId),
	CONSTRAINT fk_GamePlatformLinkers_PlatformId 
		FOREIGN KEY(PlatformId)
		REFERENCES Platforms(PlatformId)
		ON UPDATE CASCADE ON DELETE SET NULL,
	CONSTRAINT fk_GamePlatformLinkers_GameId 
		FOREIGN KEY(GameId)
		REFERENCES Games(GameId)
		ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE GivenReviews(
	GivenReviewId INT,
	GameId INT,
	PositiveRatings INT,
	NegativeRatings INT,
	CONSTRAINT pk_GivenReviews_GivenReviewId PRIMARY KEY(GivenReviewId),
	CONSTRAINT fk_GivenReviews_GameId FOREIGN KEY(GameId)
		REFERENCES Games(GameId)
		ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE Developers(
	DeveloperId INT,
	DeveloperName VARCHAR(255),
	CONSTRAINT pk_Developers_DeveloperId PRIMARY KEY(DeveloperId)
);

CREATE TABLE GameDeveloperLinkers(
	GDId INT AUTO_INCREMENT,
	GameId INT,
	DeveloperId INT,
	CONSTRAINT pk_GameDeveloperLinkers_GDId PRIMARY KEY(GDId),
	CONSTRAINT fk_GameDeveloperLinker_GameId 
		FOREIGN KEY(GameId)
		REFERENCES Games(GameId)
		ON UPDATE CASCADE ON DELETE SET NULL,
	CONSTRAINT fk_GameDeveloperLinker_DeveloperId
		FOREIGN KEY(DeveloperId)
		REFERENCES Developers(DeveloperId)
		ON UPDATE CASCADE ON DELETE SET NULL	
);

CREATE TABLE Publishers (
	PublisherId INT,
	PublisherName VARCHAR(255),
	CONSTRAINT pk_Publishers_PublisherId PRIMARY KEY (PublisherId)
);

CREATE TABLE GamePublisherLinkers (
	GPId INT AUTO_INCREMENT,
	GameId INT,
	PublisherId INT,
	CONSTRAINT pk_GamePublisherLinkers_GPId PRIMARY KEY (GPId),
	CONSTRAINT fk_GamePublisherLinkers_GameId FOREIGN KEY (GameId)
		REFERENCES Games(GameId) ON UPDATE CASCADE ON DELETE SET NULL,
	CONSTRAINT fk_GamePublisherLinkers_PublisherId FOREIGN KEY (PublisherId)
		REFERENCES Publishers(PublisherId) ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE PopularTags (
	TagId INT,
	TagName VARCHAR(255),
	CONSTRAINT pk_PopularTags_TagId PRIMARY KEY (TagId)
);

CREATE TABLE GameTagLinkers (
	GTId INT AUTO_INCREMENT,
	GameId INT,
	TagId INT,
	CONSTRAINT pk_GameTagLinkers_GTId PRIMARY KEY (GTId),
	CONSTRAINT fk_GameTagLinkers_GameId FOREIGN KEY (GameId)
		REFERENCES Games(GameId) ON UPDATE CASCADE ON DELETE SET NULL,
	CONSTRAINT fk_GameTagLinkers_TagId FOREIGN KEY (TagId)
		REFERENCES PopularTags(TagId) ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE Users (
	UserId INT,
	UserName VARCHAR(255),
	Email VARCHAR(255),
	FirstName VARCHAR(255),
	Passwords VARCHAR(255),
	CONSTRAINT pk_Users_UserId PRIMARY KEY (UserId)
);

CREATE TABLE UserReviews (
	UserReviewId INT AUTO_INCREMENT,
    UserId INT,
    GivenReviewId INT,
    IsPositive boolean NOT NULL,
    CONSTRAINT pk_UserReviews_UserReviewId PRIMARY KEY (UserReviewId),
    CONSTRAINT fk_UserReviews_UserId
		FOREIGN KEY (UserId)
        REFERENCES Users(UserId)
        ON UPDATE CASCADE ON DELETE SET NULL,
	CONSTRAINT fk_UserReviews_GivenReviewId
		FOREIGN KEY (GivenReviewId)
        REFERENCES GivenReviews(GivenReviewId)
        ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT uq_Userviews_UserIdGivenReviewId
		UNIQUE(UserId, GivenReviewId)
);


LOAD DATA LOCAL INFILE '/Users/cdog/Desktop/projects/GamePedia/numplayers.csv' INTO TABLE numplayers
  FIELDS TERMINATED BY ','
  LINES TERMINATED BY '\r\n'
  IGNORE 1 LINES;

LOAD DATA LOCAL INFILE '/Users/cdog/Desktop/projects/GamePedia/game_final_2.csv' INTO TABLE games 
COLUMNS TERMINATED BY ',' 
LINES TERMINATED BY '\r\n'
IGNORE 1 LINES
(GameId, GameName, Description, @ReleaseDate, Price, PicLink)
    SET ReleaseDate = STR_TO_DATE(@ReleaseDate, '%m/%d/%Y');

LOAD DATA LOCAL INFILE '/Users/cdog/Desktop/projects/GamePedia/gameNumPlayersLinker_final.csv' INTO TABLE GameNumplayersLinkers
  FIELDS TERMINATED BY ',' 
  LINES TERMINATED BY '\r\n'
  IGNORE 1 LINES;

LOAD DATA LOCAL INFILE '/Users/cdog/Desktop/projects/GamePedia/genre.csv' INTO TABLE genres
  FIELDS TERMINATED BY ',' 
  LINES TERMINATED BY '\r\n'
  IGNORE 1 LINES;
  
LOAD DATA LOCAL INFILE '/Users/cdog/Desktop/projects/GamePedia/gamegenreLinker_final.csv' INTO TABLE gamegenreLinkers
  FIELDS TERMINATED BY ',' 
  LINES TERMINATED BY '\r\n'
  IGNORE 1 LINES;

LOAD DATA LOCAL INFILE '/Users/cdog/Desktop/projects/GamePedia/platform.csv' INTO TABLE platforms
  FIELDS TERMINATED BY ',' 
  LINES TERMINATED BY '\r\n'
  IGNORE 1 LINES;
  
LOAD DATA LOCAL INFILE '/Users/cdog/Desktop/projects/GamePedia/gameplatformLinker_final.csv' INTO TABLE gameplatformLinkers
  FIELDS TERMINATED BY ',' 
  LINES TERMINATED BY '\r\n'
  IGNORE 1 LINES;

LOAD DATA LOCAL INFILE '/Users/cdog/Desktop/projects/GamePedia/gamereviews.csv' INTO TABLE givenreviews
  FIELDS TERMINATED BY ',' 
  LINES TERMINATED BY '\r\n'
  IGNORE 1 LINES;
  
LOAD DATA LOCAL INFILE '/Users/cdog/Desktop/projects/GamePedia/developers_final.csv' INTO TABLE developers
  FIELDS TERMINATED BY ',' 
  LINES TERMINATED BY '\r\n'
  IGNORE 1 LINES;
  
LOAD DATA LOCAL INFILE '/Users/cdog/Desktop/projects/GamePedia/gamedeveloperLinker_final.csv' INTO TABLE gamedeveloperLinkers
  FIELDS TERMINATED BY ',' 
  LINES TERMINATED BY '\r\n'
  IGNORE 1 LINES;
  
LOAD DATA LOCAL INFILE '/Users/cdog/Desktop/projects/GamePedia/publishers_final.csv' INTO TABLE publishers
  FIELDS TERMINATED BY ','
  LINES TERMINATED BY '\r\n'
  IGNORE 1 LINES;

LOAD DATA LOCAL INFILE '/Users/cdog/Desktop/projects/GamePedia/gamepublisherlinker_final.csv' INTO TABLE GamePublisherLinkers
  FIELDS TERMINATED BY ','
  LINES TERMINATED BY '\r\n'
  IGNORE 1 LINES;
  
LOAD DATA LOCAL INFILE '/Users/cdog/Desktop/projects/GamePedia/populartags.csv' INTO TABLE populartags
  FIELDS TERMINATED BY ',' 
  LINES TERMINATED BY '\r\n'
  IGNORE 1 LINES;
  
LOAD DATA LOCAL INFILE '/Users/cdog/Desktop/projects/GamePedia/gametagLinker_final.csv' INTO TABLE gametagLinkers
  FIELDS TERMINATED BY ',' 
  LINES TERMINATED BY '\r\n'
  IGNORE 1 LINES;
  
LOAD DATA LOCAL INFILE '/Users/cdog/Desktop/projects/GamePedia/users.csv' INTO TABLE users
  FIELDS TERMINATED BY ',' 
  LINES TERMINATED BY '\r\n'
  IGNORE 1 LINES;

INSERT INTO UserReviews(UserId,GivenReviewId,IsPositive) 
	VALUES(1,1,true);
INSERT INTO UserReviews(UserId,GivenReviewId,IsPositive) 
	VALUES(2,1,false);
INSERT INTO UserReviews(UserId,GivenReviewId,IsPositive) 
	VALUES(2,2,true);
INSERT INTO UserReviews(UserId,GivenReviewId,IsPositive) 
	VALUES(2,10,true);
INSERT INTO UserReviews(UserId,GivenReviewId,IsPositive) 
	VALUES(3,3,true);
INSERT INTO UserReviews(UserId,GivenReviewId,IsPositive) 
	VALUES(3,1,false);
INSERT INTO UserReviews(UserId,GivenReviewId,IsPositive) 
	VALUES(3,10,true);
