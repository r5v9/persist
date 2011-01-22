
create user persist with password 'persist';

CREATE DATABASE persist
  WITH OWNER = persist
       ENCODING = 'UTF8'
       TABLESPACE = pg_default;
