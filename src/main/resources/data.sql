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
-- Article 1: AI in Waste Management
INSERT INTO news (news_id, description, image_url, name, status, published_date)
SELECT 1,
       'The global recycling crisis is often a problem of contamination. When a greasy pizza box or the wrong type of plastic enters a recycling stream, it can ruin an entire batch of material. To solve this, facilities are now integrating "Computer Vision" and Artificial Intelligence.\n\nAI-powered robotic arms use high-speed cameras to identify objects in milliseconds. Unlike human sorters, these systems don''t get tired and can distinguish between types of plastic with nearly 99% accuracy. This precision ensures that the resulting recycled flakes are of the highest quality, making them more valuable to manufacturers.\n\nAs we look toward the end of 2026, many cities are planning to integrate AI directly into "Smart Bins" on the street. These bins provide real-time feedback to users, letting them know if an item is recyclable before they drop it in.',
       'https://images.unsplash.com/photo-1611284446314-60a58ac0deb9',
       'The Brains Behind the Bin: How AI is Revolutionizing Sorting',
       'Completed',
       '2026-02-03 08:00:00'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM news WHERE news_id = 1);

-- Article 2: Urban Mining
INSERT INTO news (news_id, description, image_url, name, status, published_date)
SELECT 2,
       'The minerals required to power our modern lives—lithium, cobalt, and gold—are finite and environmentally costly to mine. However, a massive "mine" already exists right in our junk drawers. This is the concept of Urban Mining.\n\nOne ton of discarded circuit boards contains significantly more gold than a ton of raw gold ore. By focusing on "e-waste," companies can recover precious metals using 80% less energy than traditional mining operations. This circular approach doesn''t just save energy; it prevents toxic heavy metals from leaching into the soil of landfills.\n\nFor the consumer, this transition means better trade-in programs and "Right to Repair" initiatives. When we design electronics to be easily disassembled, we make it profitable for companies to recycle them.',
       'https://images.unsplash.com/photo-1550009158-9ebf69173e03',
       'Urban Mining: Why Your Old Smartphone is a Gold Mine',
       'Completed',
       '2026-02-02 12:00:00'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM news WHERE news_id = 2);

-- Article 3: Psychology of Habits
INSERT INTO news (news_id, description, image_url, name, status, published_date)
SELECT 3,
       'Why is it that even when we know recycling is important, we sometimes find it difficult to follow through? Environmental psychologists suggest that "friction" is the biggest enemy of sustainability. If a recycling bin is further away than a trash can, the likelihood of recycling drops significantly.\n\nTo combat this, "Nudge Theory" is being applied to urban design. By using bright colors and satisfyng sounds, cities are making sustainability the "path of least resistance." This is where apps like BinItRight come in.\n\nBy providing instant clarity and rewards, we move from "conscious effort" to "automatic habit." When recycling becomes a game, it starts being a core part of our identity.',
       'https://images.unsplash.com/photo-1532996122724-e3c354a0b15b',
       'Beyond the Blue Bin: The Psychology of Sustainable Habits',
       'Completed',
       '2026-02-01 09:30:00'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM news WHERE news_id = 3);

-- Article 4: Circular Fashion
INSERT INTO news (news_id, description, image_url, name, status, published_date)
SELECT 4,
       'The fashion industry is responsible for nearly 10% of global carbon emissions. The "fast fashion" model has led to a culture of disposability, where garments are worn only a few times before being discarded. But a revolution is happening in textile recycling.\n\nNew "Chemical Recycling" methods can now break down blended fabrics that were previously impossible to recycle. These processes dissolve fibers back into molecular building blocks, allowing for the creation of new yarn identical to virgin material.\n\nBrands are also beginning to adopt "Digital Product Passports." By scanning a QR code, consumers can see the entire history of the garment: where the fiber was grown and how to recycle it when it wears out.',
       'https://images.unsplash.com/photo-1582408921715-18e7806365c1',
       'Breaking the Thread: The Future of Circular Fashion',
       'Completed',
       '2026-01-28 15:45:00'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM news WHERE news_id = 4);

-- Article 5: General Recycling Guide (Replaces Zero-Waste Summit)
INSERT INTO news (news_id, description, image_url, name, status, published_date)
SELECT 5,
       'Recycling can feel like a guessing game, but it doesn''t have to be. The most important rule to remember is: "When in doubt, find out—or throw it out." Contamination is the leading reason why recycled materials end up in landfills anyway.\n\nTo keep your recycling stream clean, focus on the "Big Four": Paper/Cardboard, Plastic Bottles, Metal Cans, and Glass Jars. A quick rinse to remove food residue is often all that is needed. Avoid "Wish-cycling"—the act of putting non-recyclable items like soft plastics (bread bags) or coffee cups into the bin hoping they will be recycled.\n\nBy mastering these basics, you ensure that your local facility can actually process what you drop off. Remember, small, consistent actions by millions of people create a far greater impact than a few people doing zero-waste perfectly.',
       'https://images.unsplash.com/photo-1604187351574-c75ca79f5807',
       'Recycling 101: A Simple Guide to Getting it Right',
       'Completed',
       '2026-02-04 09:00:00'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM news WHERE news_id = 5);

-- Event 1: Future Workshop
INSERT INTO events (event_id, title, description, location_name, postal_code, start_time, end_time, image_url)
SELECT 1, 'Sustainable Living Workshop', 'Learn practical tips for reducing waste and living more sustainably in your daily life.', 'Yew Tee Community Club', '689286', '2026-02-10 18:00:00', '2026-02-10 20:00:00', 'https://images.unsplash.com/photo-1544928147-79a2dbc1f389'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM events WHERE event_id = 1);

-- Event 2: Beach Cleanup
INSERT INTO events (event_id, title, description, location_name, postal_code, start_time, end_time, image_url)
SELECT 2, 'Community Beach Cleanup', 'Join us for a morning of cleaning up our beautiful coastline. Meet at the main pavilion.', 'East Coast Park Area D', '449880', '2026-02-14 09:00:00', '2026-02-14 12:00:00', 'https://images.unsplash.com/photo-1708955148629-b1c0884fb371'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM events WHERE event_id = 2);

-- Event 3: Recycling Drive
INSERT INTO events (event_id, title, description, location_name, postal_code, start_time, end_time, image_url)
SELECT 3, 'Neighborhood Recycling Drive', 'Bring your old electronics, batteries, and paper for safe disposal and recycling.', 'Clementi Mall', '129588', '2026-02-15 10:00:00', '2026-02-15 16:00:00', 'https://images.unsplash.com/photo-1599059813005-11265ba4b4ce'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM events WHERE event_id = 3);

-- Event 4: Upcycling Class
INSERT INTO events (event_id, title, description, location_name, postal_code, start_time, end_time, image_url)
SELECT 4, 'Creative Upcycling Class', 'Turn your "trash" into treasure! This session focuses on turning old textiles into reusable bags.', 'National Library', '188064', '2026-02-18 14:00:00', '2026-02-18 17:00:00', 'https://images.unsplash.com/photo-1582408921715-18e7806365c1'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM events WHERE event_id = 4);

-- Event 5: Zero-Waste Talk
INSERT INTO events (event_id, title, description, location_name, postal_code, start_time, end_time, image_url)
SELECT 5, 'Zero-Waste Lifestyle Talk', 'A special talk by environmental experts on how to achieve a zero-waste home in Singapore.', 'National Library', '188064', '2026-02-22 11:00:00', '2026-02-22 13:00:00', 'https://images.unsplash.com/photo-1540575467063-178a50c2df87'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM events WHERE event_id = 5);

-- Event 6: Past Event (Used for filtering test)
INSERT INTO events (event_id, title, description, location_name, postal_code, start_time, end_time, image_url, status)
SELECT 6,'New Year E-Waste Collection', 'This event happened last month. It should be filtered out by the backend logic.','Tampines Hub','528523','2026-01-05 09:00:00','2026-01-05 17:00:00', 'https://images.unsplash.com/photo-1550009158-9ebf69173e03','APPROVED'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM events WHERE event_id = 6);
