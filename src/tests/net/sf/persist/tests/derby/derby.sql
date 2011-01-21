
with ij: CONNECT 'jdbc:derby:persist;create=true';

create table simple (
	id integer not null generated always as identity (start with 1, increment by 1),
	string_col varchar(255),
	int_col integer,
	primary key (id)
);

create table numeric_types (
	smallint_col smallint,
	integer_col integer,
	bigint_col bigint,
	real_col real,
	double_precision_col double precision,
	float_col float,
	decimal_col decimal,
	numeric_col numeric
);

create table string_types (
	char_col char,
	varchar_col varchar(255),
	long_varchar_col long varchar,
	clob_col clob
);

create table binary_types (
	char_bit_col char for bit data,
	varchar_bit_col varchar(255) for bit data,
	long_varchar_bit_col long varchar for bit data,
	blob_col blob
);

create table datetime_types (
	date_col date,
	time_col time,
	timestamp_col timestamp
);