INSERT INTO dietiestatesdb.public.real_estate_agencies (id, name, iban) VALUES
    (10, 'Test Agency 1', '1111111111'),
    (20, 'Test Agency 2', '2222222222');

INSERT INTO dietiestatesdb.public.users (id, username, cognito_sub) VALUES
    (10, 'baseUserName', 'baseUserSub'),
    (20, 'agent1UserName', 'agent1Sub'),
    (30, 'manager1UserName', 'manager1Sub'),
    (40, 'agent2UserName', 'agent2Sub'),
    (50, 'manager2UserName', 'manager2Sub');

INSERT INTO dietiestatesdb.public.business_users (user_id, agency_id) VALUES
    (20, 10), -- agent1
    (30, 10), -- manager1
    (40, 20), -- agent2
    (50, 20); -- manager2

INSERT INTO dietiestatesdb.public.real_estate_agents (user_id) VALUES
    (20), -- agent1
    (40); -- agent2

INSERT INTO dietiestatesdb.public.real_estate_managers (user_id) VALUES
    (30), -- manager1
    (50); -- manager2

INSERT INTO dietiestatesdb.public.user_roles (role_id, user_id) VALUES
    (1, 30),  -- manager1
    (1, 50),  -- manager2
    (2, 20),  -- agent1
    (2, 40);  -- agent2

INSERT INTO dietiestatesdb.public.addresses (id, city, state, street, housenumber, location) VALUES
    (10, 'CityA', 'StateA', 'StreetA', '1A', ST_SetSRID(ST_MakePoint(10.0, 20.0), 4326)),
    (20, 'CityB', 'StateB', 'StreetB', '2B', ST_SetSRID(ST_MakePoint(30.0, 40.0), 4326)),
    (30, 'CityC', 'StateC', 'StreetC', '3C', ST_SetSRID(ST_MakePoint(50.0, 60.0), 4326));

INSERT INTO dietiestatesdb.public.tags (id, name) VALUES
    (10, 'Garden'),
    (20, 'Garage');

INSERT INTO dietiestatesdb.public.insertions (id, description, address_id, uploader_id, agency_id, size, number_of_rooms, floor, has_elevator) VALUES
    (10, null, 10, 20, 10, 85.0, 3, 2, true),
    (20, null, 20, 30, 10, 120.0, 5, null, false),
    (30, null, 30, 40, 20, 60.0, 2, 5, true);

INSERT INTO dietiestatesdb.public.insertion_tags (insertion_id, tag_id) VALUES
    (10, 10), -- Insertion 10 has tag 'Garden'
    (10, 20), -- Insertion 10 has tag 'Garage'
    (20, 20); -- Insertion 20 has tag 'Garage'

INSERT INTO dietiestatesdb.public.insertions_for_sale (id, price) VALUES
    (10, 300000.0);

INSERT INTO dietiestatesdb.public.insertions_for_rent (id, rent) VALUES
    (20, 1500.0),
    (30, 2500.0);

INSERT INTO dietiestatesdb.public.saved_searches (id, user_id, geometry, distance, min_size, min_number_of_rooms, max_floor, has_elevator) VALUES
    (10, 10, ST_SetSRID(ST_MakePoint(15.0, 25.0), 4326), 5000.0, 50.0, 2, null, null),
    (20, 10, ST_SetSRID(ST_MakePoint(35.0, 45.0), 4326), 10000.0, null, null, 3, true);