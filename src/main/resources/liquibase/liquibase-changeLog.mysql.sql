-- liquibase formatted sql

-- changeset stavr:1738775484607-1
CREATE TABLE `available-pharmacies`
(
    id                BIGINT AUTO_INCREMENT NOT NULL,
    date              datetime(6)           NOT NULL,
    `pulled-version`  INT                   NOT NULL,
    `pharmacy-id`     INT                   NOT NULL,
    `working-hour-id` INT                   NOT NULL,
    CONSTRAINT `available-pharmaciesPK` PRIMARY KEY (id)
);

-- changeset stavr:1738775484607-2
CREATE TABLE pharmacies
(
    id             INT AUTO_INCREMENT NOT NULL,
    address        VARCHAR(255)       NOT NULL,
    name           VARCHAR(255)       NOT NULL,
    `phone-number` VARCHAR(255)       NULL,
    region         VARCHAR(255)       NOT NULL,
    CONSTRAINT pharmaciesPK PRIMARY KEY (id)
);

-- changeset stavr:1738775484607-3
CREATE TABLE `working-hours`
(
    id                  INT AUTO_INCREMENT NOT NULL,
    `working-hour-text` VARCHAR(255)       NOT NULL,
    CONSTRAINT `working-hoursPK` PRIMARY KEY (id)
);

-- changeset stavr:1738775484607-4
ALTER TABLE `available-pharmacies`
    ADD CONSTRAINT FK7wtid4pxs9fx8t1rhxwq32s6g FOREIGN KEY (`working-hour-id`) REFERENCES `working-hours` (id);

-- changeset stavr:1738775484607-5
ALTER TABLE `available-pharmacies`
    ADD CONSTRAINT FKfk39uiju5awbrgcwmf65oqdr8 FOREIGN KEY (`pharmacy-id`) REFERENCES pharmacies (id);

