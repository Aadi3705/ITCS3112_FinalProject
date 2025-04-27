create table Customer (
Cusomer_ID INT AUTO_INCREMENT PRIMARY KEY,
First_Name VARCHAR(15) NOT NULL,
Last_Name VARCHAR(15) NOT NULL,
Birth_Date DATE NOT NULL, 
Email VARCHAR(50) NOT NULL,
User_id VARCHAR(20) NOT NULL,
passcode VARCHAR(20) NOT NULL,
SSN INT NOT NULL); 

ALTER TABLE Customer CHANGE COLUMN Cusomer_ID Customer_ID INT;
ALTER TABLE Customer ADD CONSTRAINT unique_user_id UNIQUE (User_id);

create table Account (
Account_ID INT AUTO_INCREMENT PRIMARY KEY,
User_id VARCHAR(20) NOT NULL, 
Bank_Account_Number VARCHAR(20) NOT NULL UNIQUE, 
Current_Balance DECIMAL(10,2) NOT NULL DEFAULT 0.00,
FOREIGN KEY (User_id) REFERENCES Customer(User_id) ON DELETE CASCADE
);

create table Transaction (
Transaction_ID INT AUTO_INCREMENT PRIMARY KEY,
Bank_Account_Number VARCHAR(20) NOT NULL, 
Transaction_Type CHAR(1) NOT NULL CHECK (Transaction_Type IN ('W', 'D')), 
Amount_Entered DECIMAL(10,2) NOT NULL DEFAULT 0.00,
Transaction_Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (Bank_Account_Number) REFERENCES Account(Bank_Account_Number) ON DELETE CASCADE
);

select * from account;
select * from customer;
select * from transaction;

SET SQL_SAFE_UPDATES = 0;
delete from customer;
SET SQL_SAFE_UPDATES = 1;


ALTER TABLE customer DROP PRIMARY KEY;
ALTER TABLE customer MODIFY Customer_ID INT NOT NULL AUTO_INCREMENT,
                     ADD PRIMARY KEY (Customer_ID);
    


