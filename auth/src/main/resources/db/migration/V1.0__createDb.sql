drop table if exists authorities CASCADE;
drop table if exists users CASCADE;
create table authorities (username varchar(255) not null, authority varchar(255));
create table users (username varchar(255) not null, enabled boolean not null, password varchar(255), primary key (username));
alter table authorities add constraint FKhjuy9y4fd8v5m3klig05ktofg foreign key (username) references users;