insert into locations(id, country, city, street, postal_code)
values (1, 'Ukraine', 'Kyiv', 'Lesyi Ukraunky Street 10', '01001');

insert into locations(id, country, city, street, postal_code)
values (2, 'Ukraine', 'Kharkiv', 'Science Street', '61111');

insert into accommodations (id, location_id, type, size, daily_rate, availability)
values (1, 1, 'CONDO', 'Bedroom 1', 100, 10);

insert into accommodations (id, location_id, type, size, daily_rate, availability)
values (2, 2, 'APARTMENT', 'Bedroom 1, Washroom 2', 200, 5);

insert into users (id, email, password, first_name, last_name)
values (2, 'mykhailo@gmail.com', '1234', 'Ivan', 'Franko');

insert into bookings (id, check_in_date, check_out_date, accommodation_id, user_id, status)
values (1, '2024-12-18', '2024-12-24', 1, 1, 'PENDING');

insert into bookings (id, check_in_date, check_out_date, accommodation_id, user_id, status)
values (2, '2024-12-20', '2024-12-28', 2, 1, 'PENDING');
