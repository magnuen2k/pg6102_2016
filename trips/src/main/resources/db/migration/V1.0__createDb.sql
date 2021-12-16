create sequence hibernate_sequence start with 1 increment by 1;
create table boat (name varchar(255) not null, builder varchar(255), crew_size integer, length integer, max_passengers integer, primary key (name));
create table port (name varchar(255) not null, max_boats integer, primary key (name));
create table trip (trip_id bigint not null, crew integer, passengers integer, status integer, trip_year integer, boat_name varchar(255), destination_name varchar(255), origin_name varchar(255), primary key (trip_id));
alter table trip add constraint FK88l5w6tl59ggmp9dlsupad6fe foreign key (boat_name) references boat;
alter table trip add constraint FKeyg31bo65rncnokvb4tl9tpr8 foreign key (destination_name) references port;
alter table trip add constraint FKga9haamta2wjp1ei58xs87cf foreign key (origin_name) references port;