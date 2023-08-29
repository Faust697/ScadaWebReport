





-- Создание схемы
CREATE SCHEMA IF NOT EXISTS main_schema;

-- Использование созданной схемы
SET SCHEMA main_schema;

-- Создание таблицы

CREATE TABLE IF NOT EXISTS channel_data (
    id SERIAL PRIMARY KEY,
    channel_name VARCHAR(255),
    flow_rate_id BIGINT,
    water_level_id BIGINT,
    total_flow_id BIGINT,
    progress_status VARCHAR(255),
    explanation VARCHAR(255),
    calibration_status VARCHAR(255)
);