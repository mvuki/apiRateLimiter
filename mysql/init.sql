-- init.sql

CREATE DATABASE IF NOT EXISTS companyxyz;
USE companyxyz;

CREATE TABLE IF NOT EXISTS user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    
);

CREATE TABLE IF NOT EXISTS package (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DOUBLE,
    number_request_per_month INT,
    number_sms_per_month INT,
    number_email_per_month INT,
    Description VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    
);

CREATE TABLE IF NOT EXISTS client_subscription (
    client_id INT,
    package_id INT,
    month INT,
    year INT,
    
    
);


INSERT INTO user (username, email,password) VALUES
    ('user1', 'user1@example.com',''),
    ('user2', 'user2@example.com','');
