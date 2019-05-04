-- NOTE: CHANGE '[ABSOLUTE PATH]' TO THE ACTUAL PATH OF THE 'DATABASE' FOLDER BEFORE RUNNING THIS SCRIPT

-- BADGES
UPDATE achievement
SET badge_large = pg_read_binary_file('[ABSOLUTE PATH]/badges/energy/energy-bronze-95px.png')
WHERE achievement_name = '25_energy';
UPDATE achievement
SET badge_small = pg_read_binary_file('[ABSOLUTE PATH]/badges/energy/energy-bronze-50px.png')
WHERE achievement_name = '25_energy';

UPDATE achievement
SET badge_large = pg_read_binary_file('[ABSOLUTE PATH]/badges/energy/energy-silver-95px.png')
WHERE achievement_name = '50_energy';
UPDATE achievement
SET badge_small = pg_read_binary_file('[ABSOLUTE PATH]/badges/energy/energy-silver-50px.png')
WHERE achievement_name = '50_energy';

UPDATE achievement
SET badge_large = pg_read_binary_file('[ABSOLUTE PATH]/badges/energy/energy-gold-95px.png')
WHERE achievement_name = '100_energy';
UPDATE achievement
SET badge_small = pg_read_binary_file('[ABSOLUTE PATH]/badges/energy/energy-gold-50px.png')
WHERE achievement_name = '100_energy';


UPDATE achievement
SET badge_large = pg_read_binary_file('[ABSOLUTE PATH]/badges/food/food-bronze-95px.png')
WHERE achievement_name = '25_food';
UPDATE achievement
SET badge_small = pg_read_binary_file('[ABSOLUTE PATH]/badges/food/food-bronze-50px.png')
WHERE achievement_name = '25_food';

UPDATE achievement
SET badge_large = pg_read_binary_file('[ABSOLUTE PATH]/badges/food/food-silver-95px.png')
WHERE achievement_name = '50_food';
UPDATE achievement
SET badge_small = pg_read_binary_file('[ABSOLUTE PATH]/badges/food/food-silver-50px.png')
WHERE achievement_name = '50_food';

UPDATE achievement
SET badge_large = pg_read_binary_file('[ABSOLUTE PATH]/badges/food/food-gold-95px.png')
WHERE achievement_name = '100_food';
UPDATE achievement
SET badge_small = pg_read_binary_file('[ABSOLUTE PATH]/badges/food/food-gold-50px.png')
WHERE achievement_name = '100_food';


UPDATE achievement
SET badge_large = pg_read_binary_file('[ABSOLUTE PATH]/badges/transport/transport-bronze-95px.png')
WHERE achievement_name = '25_transport';
UPDATE achievement
SET badge_small = pg_read_binary_file('[ABSOLUTE PATH]/badges/transport/transport-bronze-50px.png')
WHERE achievement_name = '25_transport';

UPDATE achievement
SET badge_large = pg_read_binary_file('[ABSOLUTE PATH]/badges/transport/transport-silver-95px.png')
WHERE achievement_name = '50_transport';
UPDATE achievement
SET badge_small = pg_read_binary_file('[ABSOLUTE PATH]/badges/transport/transport-silver-50px.png')
WHERE achievement_name = '50_transport';

UPDATE achievement
SET badge_large = pg_read_binary_file('[ABSOLUTE PATH]/badges/transport/transport-gold-95px.png')
WHERE achievement_name = '100_transport';
UPDATE achievement
SET badge_small = pg_read_binary_file('[ABSOLUTE PATH]/badges/transport/transport-gold-50px.png')
WHERE achievement_name = '100_transport';


UPDATE achievement
SET badge_large = pg_read_binary_file('[ABSOLUTE PATH]/badges/temperature/temperature-bronze-95px.png')
WHERE achievement_name = '25_temperature';
UPDATE achievement
SET badge_small = pg_read_binary_file('[ABSOLUTE PATH]/badges/temperature/temperature-bronze-50px.png')
WHERE achievement_name = '25_temperature';

UPDATE achievement
SET badge_large = pg_read_binary_file('[ABSOLUTE PATH]/badges/temperature/temperature-silver-95px.png')
WHERE achievement_name = '50_temperature';
UPDATE achievement
SET badge_small = pg_read_binary_file('[ABSOLUTE PATH]/badges/temperature/temperature-silver-50px.png')
WHERE achievement_name = '50_temperature';

UPDATE achievement
SET badge_large = pg_read_binary_file('[ABSOLUTE PATH]/badges/temperature/temperature-gold-95px.png')
WHERE achievement_name = '100_temperature';
UPDATE achievement
SET badge_small = pg_read_binary_file('[ABSOLUTE PATH]/badges/temperature/temperature-gold-50px.png')
WHERE achievement_name = '100_temperature';


UPDATE achievement
SET badge_large = pg_read_binary_file('[ABSOLUTE PATH]/badges/other/other-bronze-95px.png')
WHERE achievement_name = '25_other';
UPDATE achievement
SET badge_small = pg_read_binary_file('[ABSOLUTE PATH]/badges/other/other-bronze-50px.png')
WHERE achievement_name = '25_other';

UPDATE achievement
SET badge_large = pg_read_binary_file('[ABSOLUTE PATH]/badges/other/other-silver-95px.png')
WHERE achievement_name = '50_other';
UPDATE achievement
SET badge_small = pg_read_binary_file('[ABSOLUTE PATH]/badges/other/other-silver-50px.png')
WHERE achievement_name = '50_other';

UPDATE achievement
SET badge_large = pg_read_binary_file('[ABSOLUTE PATH]/badges/other/other-gold-95px.png')
WHERE achievement_name = '100_other';
UPDATE achievement
SET badge_small = pg_read_binary_file('[ABSOLUTE PATH]/badges/other/other-gold-50px.png')
WHERE achievement_name = '100_other';
-- ENDOF BADGES


-- CATEGORY LOGOS
UPDATE category
SET category_image = pg_read_binary_file('[ABSOLUTE PATH]/badges/energy/energy-logo-40px.png')
WHERE category_name = 'energy category';


UPDATE category
SET category_image = pg_read_binary_file('[ABSOLUTE PATH]/badges/food/food-logo-40px.png')
WHERE category_name = 'food category';


UPDATE category
SET category_image = pg_read_binary_file('[ABSOLUTE PATH]/badges/transport/transport-logo-40px.png')
WHERE category_name = 'transportation category';


UPDATE category
SET category_image = pg_read_binary_file('[ABSOLUTE PATH]/badges/temperature/temperature-logo-40px.png')
WHERE category_name = 'temperature category';


UPDATE category
SET category_image = pg_read_binary_file('[ABSOLUTE PATH]/badges/other/other-logo-40px.png')
WHERE category_name = 'other';
-- ENDOF CATEGORY LOGOS


-- PROFILE PICTURES
UPDATE user_details
SET profile_picture = pg_read_binary_file('[ABSOLUTE PATH]/mock-profile-pictures/AI-generated-1.png')
WHERE username = 'jakob_hand';

UPDATE user_details
SET profile_picture = pg_read_binary_file('[ABSOLUTE PATH]/mock-profile-pictures/AI-generated-2.png')
WHERE username = 'secern';

UPDATE user_details
SET profile_picture = pg_read_binary_file('[ABSOLUTE PATH]/mock-profile-pictures/AI-generated-3.png')
WHERE username = 'darius';

UPDATE user_details
SET profile_picture = pg_read_binary_file('[ABSOLUTE PATH]/mock-profile-pictures/AI-generated-4.png')
WHERE username = 'pandacrazybr';

UPDATE user_details
SET profile_picture = pg_read_binary_file('[ABSOLUTE PATH]/mock-profile-pictures/AI-generated-5.png')
WHERE username = 'wout1999';

UPDATE user_details
SET profile_picture = pg_read_binary_file('[ABSOLUTE PATH]/mock-profile-pictures/AI-generated-6.png')
WHERE username = 'durnity';

UPDATE user_details
SET profile_picture = pg_read_binary_file('[ABSOLUTE PATH]/mock-profile-pictures/AI-generated-7.png')
WHERE username = 'encted';
-- ENDOF PROFILE PICTURES
