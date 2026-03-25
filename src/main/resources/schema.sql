CREATE TABLE quantity_measurement_entity (

                                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                             operation VARCHAR(50),
                                             measurement_type VARCHAR(50),
                                             unit1 VARCHAR(50),
                                             value1 DOUBLE,
                                             unit2 VARCHAR(50),
                                             value2 DOUBLE,
                                             result BOOLEAN,
                                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP);
CREATE TABLE user (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      username VARCHAR(50),
                      password VARCHAR(100)
);
INSERT INTO user (id, username, password)
VALUES (1, 'admin', '$2a$10$GIu5uw5ZKEDqq2iziV/R/ejaq.S8wOH5nVDeInanHqpqN5.kFbwRi');

SELECT * FROM user;

-- CREATE DATABASE quantity_measurement_db;
-- USE quantity_measurement_db;
-- CREATE TABLE quantity_measurement_entity (
--                                              id INT AUTO_INCREMENT PRIMARY KEY,
--                                              operation VARCHAR(20),
--                                              measurement_type VARCHAR(20),
--                                              value1 DOUBLE,
--                                              value2 DOUBLE,
--                                              result BOOLEAN
-- );
-- SELECT * FROM quantity_measurement_entity;