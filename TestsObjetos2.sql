SET search_path = tripsplitterclonetest;
--set search_path = tripsplitterclone;

truncate account,movement,debt,city, country ,"user",expense,expensemovement,usertrip ,trip RESTART IDENTITY;

--Bateria de tests para paises
INSERT INTO country(id, deleted, name) VALUES (nextval('country_seq'), FALSE, 'Argentina');
INSERT INTO country(id, deleted, name) VALUES (nextval('country_seq'), FALSE,  NULL);
INSERT INTO country(id, deleted, name) VALUES (nextval('country_seq'), FALSE, 'Bolivia');
INSERT INTO country(id, deleted, name) VALUES (nextval('country_seq'), FALSE, 'Brasil');
INSERT INTO country(id, deleted, name) VALUES (nextval('country_seq'), FALSE, 'Canadá');

--Bateria de tests para ciudades
INSERT INTO city(id, deleted, name, country_id) SELECT nextval('city_seq'), FALSE, 'La Plata', country.id FROM country WHERE name ='Argentina';
INSERT INTO city(id, deleted, name, country_id) VALUES (nextval('city_seq'), FALSE, 'Tokyo', NULL);
INSERT INTO city(id, deleted, name, country_id) SELECT nextval('city_seq'), FALSE, NULL, country.id FROM country WHERE name ='Bolivia';
INSERT INTO city(id, deleted, name, country_id) SELECT nextval('city_seq'), FALSE, 'Rio de Janeiro', country.id FROM country WHERE name ='Brasil';
INSERT INTO city(id, deleted, name, country_id) SELECT nextval('city_seq'), FALSE, 'Brasilia', country.id FROM country WHERE name ='Brasil';
INSERT INTO city(id, deleted, name, country_id) SELECT nextval('city_seq'), FALSE, 'Sao Paulo', country.id FROM country WHERE name ='Brasil';
INSERT INTO city(id, deleted, name, country_id) SELECT nextval('city_seq'), FALSE, 'Toronto', country.id FROM country WHERE name ='Canadá';

--Bateria de tests para usuarios
INSERT INTO "user"(id, deleted, email, hashcost, loginattempts, name, password) VALUES (nextval('user_seq'), FALSE, 'axel@axel.com', 10, 0, 'Axel Collard Bovy', 'axel');
INSERT INTO "user"(id, deleted, email, hashcost, loginattempts, name, password) VALUES (nextval('user_seq'), FALSE, 'pablo@pablo.com', 10, 0, 'Pablo Perez', NULL);
INSERT INTO "user"(id, deleted, email, hashcost, loginattempts, name, password) VALUES (nextval('user_seq'), FALSE, 'andres@andres.com', 10, 0, NULL, 'andres');
INSERT INTO "user"(id, deleted, email, hashcost, loginattempts, name, password) VALUES (nextval('user_seq'), FALSE, 'andré', 10, 0, 'André no tiene email', 'andré');
INSERT INTO "user"(id, deleted, email, hashcost, loginattempts, name, password) VALUES (nextval('user_seq'), FALSE, NULL, 10, 0, 'Pedro Gonzalez', 'Pedro');
INSERT INTO "user"(id, deleted, email, hashcost, loginattempts, name, password) VALUES (nextval('user_seq'), FALSE, NULL, 10, 0, NULL, NULL);
INSERT INTO "user"(id, deleted, email, hashcost, loginattempts, name, password) VALUES (nextval('user_seq'), FALSE, 'martin@martin.com', 10, 0, 'Martin Garcia', 'martin');
INSERT INTO "user"(id, deleted, email, hashcost, loginattempts, name, password) VALUES (nextval('user_seq'), FALSE, 'nicolas@nicolas.com', 10, 0, 'Nicolas Villanueva', 'nicolas');

--Bateria de tests para viajes
INSERT INTO trip(id, deleted, owner_id, destination_id) VALUES (nextval('trip_seq'), FALSE, NULL, NULL);

INSERT INTO trip(id, deleted, owner_id, destination_id) 
	SELECT nextval('trip_seq'), FALSE, u.id, ci.id
		FROM "user" u,city ci 
		WHERE ci.name='Brasilia' AND u.name='Martin Garcia';

INSERT INTO usertrip(id, deleted, trip_id, user_id,account_id) SELECT nextval('usertrip_seq'), FALSE, currval('trip_seq'), u.id, NULL FROM "user" u WHERE u.name = 'Martin Garcia';
		
		
INSERT INTO trip(id, deleted, owner_id, destination_id) 
	SELECT nextval('trip_seq'), FALSE, NULL, ci.id
		FROM "user" u, city ci
		WHERE ci.name='Sao Paulo' AND u.name='Martin Garcia';

INSERT INTO trip(id, deleted, owner_id, destination_id) 
	SELECT nextval('trip_seq'), FALSE, u.id, NULL
		FROM "user" u, city ci
		WHERE ci.name='Sao Paulo' AND u.name='Martin Garcia';

INSERT INTO trip(id, deleted, owner_id, destination_id) 
	SELECT nextval('trip_seq'), FALSE, u.id, ci.id
		FROM "user" u,city ci
		WHERE ci.name='Rio de Janeiro' AND u.name='Martin Garcia';

INSERT INTO usertrip(id, deleted, trip_id, user_id,account_id) SELECT nextval('usertrip_seq'), FALSE, currval('trip_seq'), u.id, NULL FROM "user" u WHERE u.name = 'Martin Garcia';

INSERT INTO trip(id, deleted, owner_id, destination_id) 
	SELECT nextval('trip_seq'), FALSE, u.id, ci.id
		FROM "user" u,city ci
		WHERE ci.name='Toronto' AND u.name='Martin Garcia';
		
INSERT INTO usertrip(id, deleted, trip_id, user_id,account_id) SELECT nextval('usertrip_seq'), FALSE, currval('trip_seq'), u.id, NULL FROM "user" u WHERE u.name = 'Martin Garcia';

	



set search_path = tripsplitterclone;

truncate account,movement,debt,city, country ,"user",expense,expensemovement,usertrip ,trip RESTART IDENTITY;

UPDATE "user" SET deleted = true WHERE name = 'Test subject' OR name = 'Yo admin' OR name = 'Ariel Dominguez' OR name = 'Gonzalo Chivicoy';
INSERT INTO "user"(id, deleted, email, hashcost, loginattempts, name, password) VALUES (nextval('user_seq'), FALSE, 'test@test.com', 10, 0, 'Test subject', '$2a$10$NpvRcqRZE8GhKalczl/vJO.8lRokwW1hf2XIXh5mDPfWm6Z1vwGlG');
INSERT INTO "user"(id, deleted, email, hashcost, loginattempts, name, password) VALUES (nextval('user_seq'), FALSE, 'axel', 10, 0, 'Yo admin', '$2a$10$g4ISPi7IYP02GpdC1.eEwOSjzA/ZkDVEikaIResD5kQKJCq5qK4vO');
INSERT INTO "user"(id, deleted, email, hashcost, loginattempts, name, password) VALUES (nextval('user_seq'), FALSE, 'ariel@ariel.com', 10, 0, 'Ariel Dominguez', '$2a$10$NpvRcqRZE8GhKalczl/vJO.8lRokwW1hf2XIXh5mDPfWm6Z1vwGlG');
INSERT INTO "user"(id, deleted, email, hashcost, loginattempts, name, password) VALUES (nextval('user_seq'), FALSE, 'gonzalo@gonzalo.com', 10, 0, 'Gonzalo Chivicoy', '$2a$10$NpvRcqRZE8GhKalczl/vJO.8lRokwW1hf2XIXh5mDPfWm6Z1vwGlG');

UPDATE country SET deleted = true WHERE name = 'Grecia';
UPDATE city SET deleted = true WHERE name = 'Atenas' OR name = 'Rodas' OR name = 'Salónica';

INSERT INTO country(id, deleted, name) VALUES (nextval('country_seq'), FALSE, 'Grecia');
INSERT INTO city(id, deleted, name, country_id) SELECT nextval('city_seq'), FALSE, 'Atenas', country.id FROM country WHERE name ='Grecia';
INSERT INTO city(id, deleted, name, country_id) SELECT nextval('city_seq'), FALSE, 'Rodas', country.id FROM country WHERE name ='Grecia';
INSERT INTO city(id, deleted, name, country_id) SELECT nextval('city_seq'), FALSE, 'Salónica', country.id FROM country WHERE name ='Grecia';

INSERT INTO trip(id, deleted, owner_id, destination_id) 
	SELECT nextval('trip_seq'), FALSE, u.id, ci.id
		FROM "user" u,city ci
		WHERE ci.name='Rodas' AND u.name='Test subject';
		
INSERT INTO account(id,deleted,balance,creation) VALUES (nextval('account_seq'),FALSE,0.00,CURRENT_DATE);		
INSERT INTO usertrip(id, deleted, trip_id, user_id,account_id) SELECT nextval('usertrip_seq'), FALSE, currval('trip_seq'), u.id,currval('account_seq') FROM "user" u WHERE u.name = 'Test subject';

INSERT INTO account(id,deleted,balance,creation) VALUES (nextval('account_seq'),FALSE,0.00,CURRENT_DATE);		
INSERT INTO usertrip(id, deleted, trip_id, user_id,account_id) SELECT nextval('usertrip_seq'), FALSE, currval('trip_seq'), u.id,currval('account_seq') FROM "user" u WHERE u.name = 'Ariel Dominguez';

INSERT INTO trip(id, deleted, owner_id, destination_id) 
	SELECT nextval('trip_seq'), FALSE, u.id, ci.id
		FROM "user" u,city ci
		WHERE ci.name='Atenas' AND u.name='Test subject';

INSERT INTO account(id,deleted,balance,creation) VALUES (nextval('account_seq'),FALSE,0.00,CURRENT_DATE);
INSERT INTO usertrip(id, deleted, trip_id, user_id,account_id) SELECT nextval('usertrip_seq'), FALSE, currval('trip_seq'), u.id,currval('account_seq') FROM "user" u WHERE u.name = 'Test subject';

INSERT INTO account(id,deleted,balance,creation) VALUES (nextval('account_seq'),FALSE,0.00,CURRENT_DATE);
INSERT INTO usertrip(id, deleted, trip_id, user_id,account_id) SELECT nextval('usertrip_seq'), FALSE, currval('trip_seq'), u.id,currval('account_seq') FROM "user" u WHERE u.name = 'Gonzalo Chivicoy';