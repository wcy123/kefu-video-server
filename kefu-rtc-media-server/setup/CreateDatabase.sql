CREATE TABLE rtc_media_entity
(
    id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    agent_id BINARY(255),
    agent_user_name VARCHAR(255),
    app_name VARCHAR(255),
    created TIMESTAMP DEFAULT 'CURRENT_TIMESTAMP' NOT NULL,
    last_modified DATETIME,
    media_type VARCHAR(255),
    org_name VARCHAR(255),
    state INT(11),
    visitor_user_name VARCHAR(255)
);
