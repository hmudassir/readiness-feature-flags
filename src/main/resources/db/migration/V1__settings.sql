create table app_settings (
	id int primary key,
	ready_token varchar(40) not null
);

insert into app_settings (id, ready_token) values (1, 'ok');
