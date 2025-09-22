create index if not exists addresses_location on addresses using gist(location);
create index if not exists insertions_uploader_id on insertions(uploader_id);
create index if not exists insertions_agency_id on insertions(agency_id);
create index if not exists insertions_address_id on insertions(address_id);
create index if not exists insertions_tags on insertion_tags(tags);
create index if not exists insertions_for_rent_rent on insertions_for_rent(rent);
create index if not exists insertions_for_sale_price on insertions_for_sale(price);
create index if not exists business_users_agency_id on business_users(agency_id);
