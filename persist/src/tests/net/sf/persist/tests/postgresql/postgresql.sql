
CREATE ROLE persist LOGIN
  ENCRYPTED PASSWORD 'md5fd309c7743308991db59ae0f924556d2'
  NOSUPERUSER NOINHERIT CREATEDB NOCREATEROLE;

CREATE DATABASE persist
  WITH OWNER = persist
       ENCODING = 'SQL_ASCII'
       TABLESPACE = pg_default;

create table simple (
	id serial,
	string_col varchar(255),
	int_col int,
	primary key (id)
);

create table numeric_types (
	smallint_col smallint, 
	integer_col integer, 
	bigint_col bigint, 
	decimal_col decimal, 
	numeric_col numeric, 
	real_col real, 
	double_precision_col double precision,
	--money_col money,
	--bit_col bit,
	--bit_varying_col bit varying,
	boolean_col boolean
);

create table string_types (
	char1_col char(1),
	char_col char(255),
	varchar_col varchar(255),
	text_col text,
	clob_col oid
);

create table geometric_types (
	point_col point, 
	line_col line, 
	lseg_col lseg, 
	box_col box, 
	path_col path, 
	polygon_col polygon, 
	circle_col circle
);

create table network_address_types (
	cidr_col cidr, 
	inet_col inet, 
	macaddr_col macaddr 
);

create table array_types (
    int_col integer[],
	text_col text[][]
);

create table datetime_types (
	date_col date,
	timestamp_col timestamp,
	time_col time
);

create table binary_types (
	bytea_col bytea,
	blob_col oid
);



