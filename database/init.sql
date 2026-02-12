CREATE DATABASE IF NOT EXISTS u370330862_mediaplanet;
USE u370330862_mediaplanet;

-- Users Table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'USER',
    active BOOLEAN DEFAULT TRUE,
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);

-- Languages Table
CREATE TABLE IF NOT EXISTS languages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    logo VARCHAR(500),
    language_name VARCHAR(100) NOT NULL,
    status BOOLEAN NOT NULL DEFAULT TRUE,
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
CREATE INDEX idx_languages_name ON languages(language_name);

-- Markets Table
CREATE TABLE IF NOT EXISTS markets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    logo VARCHAR(500),
    market_name VARCHAR(100) NOT NULL,
    status BOOLEAN NOT NULL DEFAULT TRUE,
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
CREATE INDEX idx_markets_name ON markets(market_name);

-- Genres Table
CREATE TABLE IF NOT EXISTS genres (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    logo VARCHAR(500),
    genre_name VARCHAR(100) NOT NULL,
    status BOOLEAN NOT NULL DEFAULT TRUE,
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
CREATE INDEX idx_genres_name ON genres(genre_name);

-- Job Machines Table
CREATE TABLE IF NOT EXISTS job_machines (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    machine_name VARCHAR(100) NOT NULL,
    value VARCHAR(255),
    description VARCHAR(500),
    status BOOLEAN NOT NULL DEFAULT TRUE,
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
CREATE INDEX idx_job_machines_name ON job_machines(machine_name);

-- Resource Paths Table
CREATE TABLE IF NOT EXISTS resource_paths (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    job_machine_id BIGINT NOT NULL,
    value VARCHAR(500),
    description VARCHAR(500),
    status BOOLEAN NOT NULL DEFAULT TRUE,
    priority VARCHAR(50), -- Primary, Secondary
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (job_machine_id) REFERENCES job_machines(id)
);
CREATE INDEX idx_resource_paths_machine ON resource_paths(job_machine_id);
CREATE INDEX idx_resource_paths_priority ON resource_paths(priority);

-- Channels Table
CREATE TABLE IF NOT EXISTS channels (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    channel_name VARCHAR(100) NOT NULL,
    logo LONGTEXT,
    description VARCHAR(500),
    status BOOLEAN NOT NULL DEFAULT TRUE,
    language_id BIGINT,
    market_id BIGINT,
    genre_id BIGINT,
    job_machine_id BIGINT,
    primary_path_id BIGINT,
    secondary_path_id BIGINT,
    ad_detection BOOLEAN DEFAULT FALSE,
    news_detection BOOLEAN DEFAULT FALSE,
    ocr BOOLEAN DEFAULT FALSE,
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (language_id) REFERENCES languages(id),
    FOREIGN KEY (market_id) REFERENCES markets(id),
    FOREIGN KEY (genre_id) REFERENCES genres(id),
    FOREIGN KEY (job_machine_id) REFERENCES job_machines(id),
    FOREIGN KEY (primary_path_id) REFERENCES resource_paths(id),
    FOREIGN KEY (secondary_path_id) REFERENCES resource_paths(id)
);
CREATE INDEX idx_channels_name ON channels(channel_name);
CREATE INDEX idx_channels_status ON channels(status);
CREATE INDEX idx_channels_lang ON channels(language_id);
CREATE INDEX idx_channels_market ON channels(market_id);
CREATE INDEX idx_channels_genre ON channels(genre_id);
CREATE INDEX idx_channels_machine ON channels(job_machine_id);

-- Task Executions Table
CREATE TABLE IF NOT EXISTS task_executions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    channel_id BIGINT NOT NULL,
    task_type VARCHAR(20) NOT NULL, -- AD, NEWS, OCR
    status VARCHAR(20) NOT NULL, -- RUNNING, STOPPED, FAILED
    start_time DATETIME,
    stop_time DATETIME,
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (channel_id) REFERENCES channels(id)
);
CREATE INDEX idx_task_exec_channel ON task_executions(channel_id);
CREATE INDEX idx_task_exec_type ON task_executions(task_type);
CREATE INDEX idx_task_exec_status ON task_executions(status);
CREATE INDEX idx_task_exec_date ON task_executions(create_date);

-- Generated Contents Table
CREATE TABLE IF NOT EXISTS generated_contents (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    channel_id BIGINT NOT NULL,
    task_type VARCHAR(20) NOT NULL, -- AD, NEWS, OCR
    content TEXT,
    image_url VARCHAR(255),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (channel_id) REFERENCES channels(id)
);
CREATE INDEX idx_gen_content_channel ON generated_contents(channel_id);
CREATE INDEX idx_gen_content_type ON generated_contents(task_type);
CREATE INDEX idx_gen_content_time ON generated_contents(timestamp);

-- Seed Administrator Account (password is 'admin')
INSERT INTO users (username, email, password, role, active) 
VALUES ('admin', 'admin@mediaplanet.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN', true)
ON DUPLICATE KEY UPDATE 
password = VALUES(password), 
active = VALUES(active);
