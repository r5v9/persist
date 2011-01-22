
create user persist identified by persist default tablespace users temporary tablespace temp;
alter user persist quota unlimited on users;
grant create session to persist;
grant create table to persist;
grant create sequence to persist;
grant create procedure to persist;
grant create trigger to persist;
