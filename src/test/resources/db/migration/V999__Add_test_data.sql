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
    (20, 10),
    (30, 10),
    (40, 20),
    (50, 20);

INSERT INTO dietiestatesdb.public.real_estate_agents (user_id) VALUES
    (20),
    (40);

INSERT INTO dietiestatesdb.public.real_estate_managers (user_id) VALUES
    (30),
    (50);

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
    (10, 10),
    (10, 20),
    (20, 20);

INSERT INTO dietiestatesdb.public.insertions_for_sale (id, price) VALUES
    (10, 300000.0);

INSERT INTO dietiestatesdb.public.insertions_for_rent (id, rent) VALUES
    (20, 1500.0),
    (30, 2500.0);