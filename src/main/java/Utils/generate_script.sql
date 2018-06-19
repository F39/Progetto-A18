CREATE DATABASE forza4;
USE forza4;
CREATE TABLE user (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `auth_token` varchar(255) DEFAULT NULL,
  `email_confirmed` tinyint(1) DEFAULT NULL,
  `email_token` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
);
INSERT INTO forza4.user
(username,
email,
password,
email_confirmed)
VALUES
('testUser',
'fake.email@mail.com',
'super_password123',
1);