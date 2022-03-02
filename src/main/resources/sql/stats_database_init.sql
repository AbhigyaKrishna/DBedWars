-- Create table `PLAYER_STATS`
CREATE TABLE IF NOT EXISTS `player_stars`
(
    `uuid` VARCHAR
(
    36
) UNIQUE PRIMARY KEY NOT NULL,
    `name` VARCHAR
(
    16
) NOT NULL,
    `level` INT NOT NULL,
    `level_progress` DOUBLE NOT NULL,
    `coins` DOUBLE NOT NULL,
    `winstreak` SMALLINT NOT NULL,
    `points` DOUBLE NOT NULL
    );