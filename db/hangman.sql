BEGIN TRANSACTION;
CREATE TABLE Game (ID INTEGER PRIMARY KEY, Word TEXT, CreateDate TEXT, LastEditDate TEXT, CompleteDate TEXT);
CREATE TABLE Turn (ID INTEGER PRIMARY KEY, GameID NUMERIC, Guess TEXT, GuessDate TEXT);
COMMIT;
