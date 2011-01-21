
create table simple (
	id int auto_increment,
	string_col varchar(255),
	int_col int,
	primary key (id)
);

create table numeric_types (
	int_col int,
	boolean_col boolean,
	tinyint_col tinyint,
	smallint_col smallint,
	bigint_col bigint,
	decimal_col decimal,
	double_col double,
	real_col real
);

create table datetime_types (
	time_col time,
	date_col date,
	timestamp_col timestamp
);

create table binary_types (
	binary_col binary,
	blob_col blob,
	other_col other
	--uuid_col uuid
); 

create table string_types (
	varchar_col varchar,
	varchar_ignorecase_col varchar_ignorecase,
	char_col char,
	clob_col clob
);


create table all_types (

	int_col int,
	boolean_col boolean,
	tinyint_col tinyint,
	smallint_col smallint,
	bigint_col bigint,
	decimal_col decimal,
	double_col double,
	real_col real,

	time_col time,
	date_col date,
	timestamp_col timestamp,

	binary_col binary,
	blob_col blob,
	other_col other,
	uuid_col uuid,

	varchar_col varchar,
	varchar_ignorecase_col varchar_ignorecase,
	char_col char,
	clob_col clob,

	id int auto_increment
);