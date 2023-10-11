ALTER TABLE tbl_users
    ADD CONSTRAINT email_unique UNIQUE (email);