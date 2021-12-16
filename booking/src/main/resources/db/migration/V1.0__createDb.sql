create sequence hibernate_sequence start with 1 increment by 1;
create table booking (id bigint not null, ongoing integer, trip_id bigint, user_user_id varchar(255), primary key (id));
create table trip (id bigint not null, status varchar(255), primary key (id));
create table user_data (user_id varchar(255) not null, primary key (user_id));
alter table booking add constraint FKkp5ujmgvd2pmsehwpu2vyjkwb foreign key (trip_id) references trip;
alter table booking add constraint FK9le4hohuc9j46or7qdup1iy6 foreign key (user_user_id) references user_data;