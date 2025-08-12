CREATE TABLE `available-pharmacies`
(
    id                BIGINT AUTO_INCREMENT NOT NULL,
    `pharmacy-id`     INT                   NOT NULL,
    `working-hour-id` INT                   NOT NULL,
    date              datetime              NOT NULL,
    `pulled-version`  INT                   NOT NULL,
    CONSTRAINT `pk_available-pharmacies` PRIMARY KEY (id)
);

CREATE TABLE pharmacies
(
    id             INT AUTO_INCREMENT NOT NULL,
    name           VARCHAR(255)       NOT NULL,
    address        VARCHAR(255)       NOT NULL,
    region         VARCHAR(255)       NOT NULL,
    `phone-number` VARCHAR(255)       NULL,
    CONSTRAINT pk_pharmacies PRIMARY KEY (id)
);

CREATE TABLE `working-hours`
(
    id                  INT AUTO_INCREMENT NOT NULL,
    `working-hour-text` VARCHAR(255)       NOT NULL,
    CONSTRAINT `pk_working-hours` PRIMARY KEY (id)
);

ALTER TABLE `available-pharmacies`
    ADD CONSTRAINT `FK_AVAILABLE-PHARMACIES_ON_PHARMACY-ID` FOREIGN KEY (`pharmacy-id`) REFERENCES pharmacies (id);

ALTER TABLE `available-pharmacies`
    ADD CONSTRAINT `FK_AVAILABLE-PHARMACIES_ON_WORKING-HOUR-ID` FOREIGN KEY (`working-hour-id`) REFERENCES `working-hours` (id);

