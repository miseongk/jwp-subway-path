CREATE TABLE IF NOT EXISTS LINE
(
    id    BIGINT AUTO_INCREMENT NOT NULL,
    name  VARCHAR(255)          NOT NULL,
    color VARCHAR(20)           NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS STATION
(
    id      BIGINT AUTO_INCREMENT NOT NULL,
    name    VARCHAR(255)          NOT NULL,
    line_id BIGINT                NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (line_id) REFERENCES LINE (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS SECTION
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    start_station_id BIGINT                NOT NULL,
    end_station_id   BIGINT                NOT NULL,
    distance         INT                   NOT NULL,
    line_id          BIGINT                NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (start_station_id) REFERENCES STATION (id) ON DELETE CASCADE,
    FOREIGN KEY (end_station_id) REFERENCES STATION (id) ON DELETE CASCADE,
    FOREIGN KEY (line_id) REFERENCES LINE (id) ON DELETE CASCADE
);
