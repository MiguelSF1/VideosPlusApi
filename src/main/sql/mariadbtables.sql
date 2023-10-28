-- Create a database for VideosPlus
CREATE DATABASE videosplus;

-- Switch to the videosPlus database
USE videosplus;

-- Table for Users
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    permission_level INT NOT NULL
);

-- Table for Movies
CREATE TABLE movies (
    movie_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    release_date DATE NOT NULL,
    duration INT NOT NULL,
    poster VARCHAR(255) NOT NULL,
    rating FLOAT NOT NULL,
    genre VARCHAR(255) NOT NULL,
    summary TEXT NOT NULL
);

-- Table for Movie Versions
CREATE TABLE movie_versions (
    version_id INT AUTO_INCREMENT PRIMARY KEY,
    movie_id INT,
    movie_format VARCHAR(50) NOT NULL,
    movie_resolution VARCHAR(10) NOT NULL, 
    movie_link VARCHAR(100) NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movies(movie_id)
);

CREATE TABLE user_favorites (
   user_id INT,
   movie_id INT,
   FOREIGN KEY (user_id) REFERENCES users(user_id),
   FOREIGN KEY (movie_id) REFERENCES movies(movie_id)
);

