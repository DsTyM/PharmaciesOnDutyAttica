-- liquibase formatted sql

-- changeset stavr:1604359583361-1
CREATE TABLE `available-pharmacies`
(
    id                BIGINT AUTO_INCREMENT NOT NULL,
    date              VARCHAR(255)          NULL,
    `pulled-version`  INT                   NULL,
    `pharmacy-id`     INT                   NOT NULL,
    `working-hour-id` INT                   NOT NULL,
    CONSTRAINT `available-pharmaciesPK` PRIMARY KEY (id)
);

-- changeset stavr:1604359583361-2
CREATE TABLE pharmacies
(
    id             INT          NOT NULL,
    address        VARCHAR(255) NULL,
    name           VARCHAR(255) NULL,
    `phone-number` VARCHAR(255) NULL,
    region         VARCHAR(255) NULL,
    CONSTRAINT pharmaciesPK PRIMARY KEY (id)
);

-- changeset stavr:1604359583361-3
CREATE TABLE `working-hours`
(
    id                  INT          NOT NULL,
    `working-hour-text` VARCHAR(255) NULL,
    CONSTRAINT `working-hoursPK` PRIMARY KEY (id)
);

-- changeset stavr:1604359583361-4
ALTER TABLE `available-pharmacies`
    ADD CONSTRAINT FK7wtid4pxs9fx8t1rhxwq32s6g FOREIGN KEY (`working-hour-id`) REFERENCES `working-hours` (id);

-- changeset stavr:1604359583361-5
ALTER TABLE `available-pharmacies`
    ADD CONSTRAINT FKfk39uiju5awbrgcwmf65oqdr8 FOREIGN KEY (`pharmacy-id`) REFERENCES pharmacies (id);

