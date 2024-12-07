create index booking_accommodation_id_idx on bookings(accommodation_id);
create index booking_user_id_and_status_idx on bookings(user_id, status);