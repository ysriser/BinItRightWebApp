SELECT 1;


INSERT IGNORE INTO app_users
(
    user_id,
    created_at,
    email_address,
    locale,
    name,
    password_hash,
    role,
    username,
    carbon_emission_saved,
    current_rank,
    updated_at,
    user_address
)
VALUES
(
    2,
    NOW(),
    'muthu@test.com',
    'en_SG',
    'Muthu Raj',
    '$2a$10$dummyhashvalue',
    'USER',
    'muthu',
    12.5,
    1,
    NOW(),
    'Singapore'
);

INSERT IGNORE INTO waste_categories
(
    cat_id,
    avg_weight,
    emission_factor,
    icon_url,
    is_hazardous,
    name,
    stream_type
)
VALUES
(
    1,
    0.30,
    1.50,
    'plastic',
    b'0',
    'Plastic',
    'GENERAL'
),
(
    2,
    1.20,
    4.20,
    'e-waste',
    b'1',
    'E-Waste',
    'E_WASTE'
),
(
    3,
    0.50,
    0.90,
    'glass',
    b'0',
    'Glass',
    'GENERAL'
);

INSERT IGNORE INTO check_in
(
    checkin_id,
    checkin_time,
    duration,
    file_name,
    quantity,
    reward_points,
    status,
    drop_off_id,
    user_id,
    waste_categories_cat_id
)
VALUES
(
    1001,
    '2026-02-02 10:15:00',
    12,
    'plastic_1.jpg',
    3,
    30,
    'COMPLETED',
    10,
    1,
    1
),
(
    1002,
    '2026-02-01 09:30:00',
    20,
    'ewaste_1.jpg',
    1,
    50,
    'COMPLETED',
    11,
    1,
    2
),
(
    1003,
    '2026-01-30 18:45:00',
    15,
    'glass_1.jpg',
    5,
    25,
    'COMPLETED',
    12,
    1,
    3
);
