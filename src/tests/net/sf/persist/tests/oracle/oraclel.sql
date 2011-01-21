
create user persist identified by persist default tablespace users temporary tablespace temp;
alter user persist quota unlimited on users;
grant create session to persist;
grant create table to persist;
grant create sequence to persist;
grant create procedure to persist;
grant create trigger to persist;

create table simple (
	id int,
	string_col varchar(255),
	int_col int,
	primary key (id)
);
create sequence simple_seq  start with 1 increment by 1 nomaxvalue;
create trigger simple_trigger
before insert on simple
for each row
begin
select simple_seq.nextval into :new.id from dual;
end;

create table numeric_types (
	number_col number,
	binary_float_col binary_float,
	binary_double_col binary_double
);

create table datetime_types (
    date_col date,
    timestamp_col timestamp
    --interval_year_col interval year to month,
    --interval_day_col interval day to second
);

create table string_types (
	long_col long,
	char1_col char(1),
	char_col char(255),
	nchar1_col nchar,
	nchar_col nchar(255),
	nvarchar2_col nvarchar2(2000),
    varchar2_col varchar2(2000),
    clob_col clob,
    nclob_col nclob
);

create table binary_types (
	raw_col raw(2000),
	long_raw_col long raw,
	--rowid_col rowid,
	--urowid_col urowid,
	blob_col blob
	--bfile_col bfile
);