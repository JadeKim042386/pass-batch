INSERT INTO package (package_name, count, period, created_at)
VALUES ('Starter PT 10회', 10, 60, '2022-08-01 00:00:00'),
       ('Starter PT 20회', 20, 120, '2022-08-01 00:00:00'),
       ('Starter PT 30회', 30, 180, '2022-08-01 00:00:00'),
       ('무료 이벤트 필라테스 1회', 1, NULL, '2022-08-01 00:00:00'),
       ('바디 챌린지 PT 4주', NULL, 28, '2022-08-01 00:00:00'),
       ('바디 챌린지 PT 8주', NULL, 48, '2022-08-01 00:00:00'),
       ('인바디 상담', NULL, NULL, '2022-08-01 00:00:00');

INSERT INTO user_group (user_group_id, user_group_name, description, created_at)
VALUES ('HANBADA', '한바다', '한바다 임직원 그룹', '2022-08-01 00:00:00'),
       ('TAESAN', '태산', '태산 임직원 그룹', '2022-08-01 00:00:00');

INSERT INTO user_account (user_id, user_name, user_group_id, status, phone, meta, created_at)
VALUES ('A1000000', '우영우', 'HANBADA', 'ACTIVE', '01011112222', NULL, '2022-08-01 00:00:00'),
       ('A1000001', '최수연', 'HANBADA', 'ACTIVE', '01033334444', NULL, '2022-08-01 00:00:00'),
       ('A1000002', '이준호', 'HANBADA', 'INACTIVE', '01055556666', NULL, '2022-08-01 00:00:00'),
       ('B1000010', '권민우', 'HANBADA', 'ACTIVE', '01077778888', NULL, '2022-08-01 00:00:00'),
       ('B1000011', '동그라미', 'TAESAN', 'INACTIVE', '01088889999', NULL, '2022-08-01 00:00:00'),
       ('B2000000', '한선영', 'TAESAN', 'ACTIVE', '01099990000', NULL, '2022-08-01 00:00:00'),
       ('B2000001', '태수미', 'TAESAN', 'ACTIVE', '01000001111', NULL, '2022-08-01 00:00:00');

INSERT INTO pass_ticket (package_id, user_id, status, remaining_count, started_at, ended_at, expired_at, created_at) VALUES
    (1, 'A1000000', 'PROGRESSED', 100, '2022-08-15 00:00:00', '2022-08-16 00:00:00', NULL, '2022-08-15 00:00:00');

INSERT INTO booking (pass_ticket_id, user_id, status, used_pass, attended, started_at, ended_at, cancelled_at, created_at) VALUES
    (1, 'A1000000', 'COMPLETED', false, true, '2022-08-15 00:00:00', '2022-08-16 00:00:00', NULL, '2022-08-15 00:00:00');
