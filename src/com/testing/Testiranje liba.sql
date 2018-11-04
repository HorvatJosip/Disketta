IF(DB_ID('DiskettaTesting') IS NULL)
	CREATE DATABASE DiskettaTesting
GO
USE DiskettaTesting
GO

CREATE TABLE Testiranje
(
	ID INT PRIMARY KEY IDENTITY NOT NULL,
	String NVARCHAR(2000) NULL,
	Integer INT NULL,
	Boolean BIT NULL,
	AnotherString NVARCHAR(1000) NULL,
	Date DATETIME DEFAULT GETDATE() NULL
)

GO

CREATE PROC addItem
	@String NVARCHAR(2000),
	@Integer INT,
	@Boolean BIT,
	@AnotherString NVARCHAR(1000),
	@ID INT OUTPUT
AS
BEGIN
	INSERT INTO Testiranje(String, Integer, Boolean, AnotherString)
	VALUES(@String, @Integer, @Boolean, @AnotherString)

	SELECT @ID = MAX(ID)
	FROM TEstiranje
END

GO

CREATE PROC removeItem
	@IDItem INT
AS
	DELETE FROM Testiranje WHERE ID = @IDItem

GO

CREATE PROC updateItem
	@String NVARCHAR(2000),
	@Integer INT,
	@Boolean BIT,
	@AnotherString NVARCHAR(1000),
	@IDItem INT
AS
UPDATE Testiranje SET
	String = @String,
	Integer = @Integer,
	Boolean = @Boolean,
	AnotherString = @AnotherString
WHERE ID = @IDItem

GO

CREATE PROC getItem
	@IDItem INT
AS
SELECT * FROM Testiranje WHERE ID = @IDItem

GO

CREATE PROC getItems
AS
SELECT * FROM Testiranje

GO

CREATE PROC clear
AS
DELETE FROM Testiranje
DBCC CHECKIDENT('Testiranje', RESEED, 0)

GO

INSERT INTO Testiranje(String, Integer, Boolean, Date, AnotherString) VALUES
('Stuff5', 223, 1, '20170517', 'Shit3'),
('Stuff1', 95, 0, NULL, 'Shit7'),
('Stuff2', 3, 1, '20180412', 'Shit6'),
('Stuff3', 111, 1, NULL, 'Shit5'),
('Stuff4', 21, 0, '20120527', 'Shit4'),
('Stuff6', 24, 0, '20051213', 'Shit2'),
('Stuff7', 266, 1, NULL, 'Shit1')

/*
EXEC clear
*/