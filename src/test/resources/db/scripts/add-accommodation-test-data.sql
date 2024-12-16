insert into locations(id, country, city, street, postal_code)
values (1, 'Ukraine', 'Kyiv', 'Lesyi Ukraunky Street 10', '01001');

insert into locations(id, country, city, street, postal_code)
values (2, 'Ukraine', 'Kharkiv', 'Science Street', '61111');

insert into amenities (id, title, description) values (1, 'Wi-fi', 'Wi-fi technology');

insert into accommodations (id, location_id, type, size, daily_rate, availability)
values (1, 1, 'CONDO', 'Bedroom 1', 100, 10);

insert into accommodations (id, location_id, type, size, daily_rate, availability)
values (2, 2, 'APARTMENT', 'Bedroom 1, Washroom 2', 200, 5);

insert into accommodations (id, location_id, type, size, daily_rate, availability)
values (3, 2, 'VACATION_HOME', 'Bedroom 1', 95, 125);

insert into accommodations_amenities (accommodation_id, amenity_id) values (1, 1);
insert into accommodations_amenities (accommodation_id, amenity_id) values (2, 1);
insert into accommodations_amenities (accommodation_id, amenity_id) values (3, 1);
