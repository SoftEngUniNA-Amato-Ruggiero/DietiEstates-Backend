CREATE INDEX IF NOT EXISTS addresses_location ON addresses using gist (location);
CREATE INDEX IF NOT EXISTS insertions_uploader_id ON insertions (uploader_id);
CREATE INDEX IF NOT EXISTS insertions_agency_id ON insertions (agency_id);
CREATE INDEX IF NOT EXISTS insertions_address_id ON insertions (address_id);
CREATE INDEX IF NOT EXISTS insertions_tags ON insertion_tags (tag);
CREATE INDEX IF NOT EXISTS insertions_for_rent_rent ON insertions_for_rent (rent);
CREATE INDEX IF NOT EXISTS insertions_for_sale_price ON insertions_for_sale (price);
CREATE INDEX IF NOT EXISTS business_users_agency_id ON business_users (agency_id);
