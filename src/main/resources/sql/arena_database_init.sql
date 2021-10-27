-- Create arena category table
CREATE TABLE IF NOT EXISTS `ARENA_CATEGORY_{category}` (
    `UUID` VARCHAR(36) NOT NULL PRIMARY KEY,
    `NAME` VARCHAR(16) NOT NULL,
    `TOTAL_WINS` MEDIUMINT NOT NULL,
    `DAILY_WINS` SMALLINT NOT NULL,
    `WEEKLY_WINS` SMALLINT NOT NULL,
    `MONTHLY_WINS` INT NOT NULL,
    `TOTAL_KILLS` MEDIUMINT NOT NULL,
    `DAILY_KILLS` SMALLINT NOT NULL,
    `WEEKLY_KILLS` INT NOT NULL,
    `MONTHLY_KILLS` MEDIUMINT NOT NULL,
    `TOTAL_FINAL_KILLS` MEDIUMINT NOT NULL,
    `DAILY_FINAL_KILLS` SMALLINT NOT NULL,
    `WEEKLY_FINAL_KILLS` INT NOT NULL,
    `MONTHLY_FINAL_KILLS` INT NOT NULL,
    `TOTAL_BEDS_BROKEN` MEDIUMINT NOT NULL,
    `DAILY_BEDS_BROKEN` SMALLINT NOT NULL,
    `WEEKLY_BEDS_BROKEN` INT NOT NULL,
    `MONTHLY_BEDS_BROKEN` INT NOT NULL,
    `TOTAL_BED_LOST` MEDIUMINT NOT NULL,
    `DAILY_BED_LOST` SMALLINT NOT NULL,
    `WEEKLY_BED_LOST` SMALLINT NOT NULL,
    `MONTHLY_BED_LOST` INT NOT NULL,
    `TOTAL_LOSES` MEDIUMINT NOT NULL,
    `DAILY_LOSES` SMALLINT NOT NULL,
    `WEEKLY_LOSES` INT NOT NULL,
    `MONTHLY_LOSES` INT NOT NULL,
    `TOTAL_GAMES_PLAYED` BIGINT NOT NULL,
    `DAILY_GAMES_PLAYED` SMALLINT NOT NULL,
    `WEEKLY_GAMES_PLAYED` INT NOT NULL,
    `MONTHLY_GAMES_PLAYED` MEDIUMINT NOT NULL
);