
create table simple (
	id int identity(1,1),
	string_col varchar(255),
	int_col int,
	primary key (id)
);

create table numeric_types (
	bit_col bit,	
	tinyint_col tinyint,
	smallint_col smallint,
	int_col int,	
	bigint_col bigint,
	smallmoney_col smallmoney,
	money_col money,
	decimal_col decimal,
	numeric_col numeric,
	float_col float,
	real_col real
);

create table datetime_types (
	datetime_col datetime,
	smalldatetime_col smalldatetime
);

create table string_types (
	char_col char,
	varchar_col varchar(255),
	text_col text,
	nchar_col nchar,
	nvarchar_col nvarchar(255),
	ntext_col ntext
);

create table binary_types (
	binary_col binary(255),
	varbinary_col varbinary(255),
	image_col image
);
