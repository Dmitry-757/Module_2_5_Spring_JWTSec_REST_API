CREATE TABLE files (
                       id int NOT NULL AUTO_INCREMENT,
                       filePath varchar(255) DEFAULT NULL,
                       name varchar(255) DEFAULT NULL,
                       PRIMARY KEY (`id`)
);

CREATE TABLE users (
                       id int NOT NULL AUTO_INCREMENT,
                       name varchar(255) DEFAULT NULL,
                       PRIMARY KEY (id)
);

CREATE TABLE events (
                         id int NOT NULL AUTO_INCREMENT,
                         typeOfEvent varchar(100) DEFAULT NULL,
                         file_id int DEFAULT NULL,
                         user_id int DEFAULT NULL,
                         PRIMARY KEY (id),
                         UNIQUE KEY UK_file_id (file_id),
                         KEY `FK_user_id2users(id)` (user_id),
                         CONSTRAINT `FK_file_id2files(id)` FOREIGN KEY (file_id) REFERENCES files (id),
                         CONSTRAINT `FK_user_id2users(id)` FOREIGN KEY (user_id) REFERENCES users (id)
);

