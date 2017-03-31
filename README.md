# SQL create database

CREATE DATABASE book_store;

USE book_store;

CREATE TABLE `book` (
  `book_id` int(11) NOT NULL AUTO_INCREMENT,
  `isbn_no` varchar(13) NOT NULL,
  `title` varchar(255) NOT NULL,
  `author` varchar(255) NOT NULL,
  `id` bigint(20) NOT NULL,
  `isbnNo` int(11) DEFAULT NULL,
  PRIMARY KEY (`book_id`),
  KEY `isbn_no` (`isbn_no`,`author`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=11 ;

# Database connection 
Please change database conection in conf/application.conf
db {
  default.driver = com.mysql.jdbc.Driver
  default.url = "jdbc:mysql://localhost:8889/book_store"
  default.user=root
  default.password="root"

  default.jndiName=DefaultDS
}