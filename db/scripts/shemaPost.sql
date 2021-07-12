CREATE TABLE post(
    id serial primary key,
	name varchar(255),
    text text,
    link varchar(255) NOT NULL,
    created date,
    CONSTRAINT link_unique UNIQUE (link)
);