drop database shoeshop;
create database shoeshop;
use shoeshop;

create table brand (	
id int not null auto_increment primary key,
name varchar(20));

create table postcode (	
postcode int not null primary key,
city varchar(20) not null);

create table category (	
id int not null auto_increment primary key,
name varchar(20) not null);

create table model (	
id int not null auto_increment primary key,
name varchar(20) not null,
description varchar(20) not null,
brand_id int not null,
foreign key (brand_id) references brand(id) on delete cascade); -- Om märket tas bort så är inte dess modeller relevanta heller

create table model_partof_category (
id int not null auto_increment primary key, 
model_id int not null, 
category_id int, 
foreign key (model_id) references model(id) on delete cascade, -- Om modellen tas bort så behöver den inte mappas
foreign key (category_id) references category(id) on delete cascade); -- Om kategorin tas bort så behöver den inte mappas

create table product (
id int not null auto_increment primary key,
model_id int not null,
price int not null,
stock int not null,
size int,
colour varchar(20),
foreign key (model_id) references model(id) on delete cascade); -- Om modellen tas bort så är inte dess varor relevanta heller

create table customer (
id int not null auto_increment primary key,
firstname varchar(20) not null,
lastname varchar(20) not null,
address varchar(50) not null,
postcode int not null,
password VARCHAR(20),
foreign key (postcode) references postcode(postcode) on update cascade); -- En adress kan inte vara utan postkod, men den kan ändras

create table orders (
id int not null auto_increment primary key,
order_time timestamp default CURRENT_TIMESTAMP,
customer_id int, 
isActive boolean default TRUE,
foreign key (customer_id) references customer(id) on delete set null); -- Man måste spara ordern för bokföring även om man skulle ta bort kunden

create table orders_contains_product (
id int not null auto_increment primary key, 
price int not null,
product_id int,
orders_id int not null, 
foreign key (product_id) references product(id) on delete set null, -- Om produkten skulle tas bort kan man ändå räkna summan på ordern
foreign key (orders_id) references orders(id) on delete cascade); -- Om beställningen tas bort så behöver den inte mappas

insert into brand (name) values 
('ecco'),
('adidas'),
('Puma'),
('Dr. Martens'),
('Nike');

insert into postcode values 
(83989,'Östersund'),
(84220,'Sveg'),
(12107,'Stockholm-globen'),
(14840,'Segersäng'),
(19561,'Arlandastad'),
(23371,'Malmö-Sturup'),
(27170,'Nybrostrand'),
(23032,'Malmö-Sturup'),
(62230,'Gotlands Tofta');

insert into category (name) values 
('Herrskor'),
('Damskor'),
('Barnskor'),
('Sportskor'),
('Kängor'),
('Sandaler'),
('Sneakers'),
('Diverse');

insert into model (name, description,brand_id) values 
('Snabba sandalen','bla bla',1),
('SL72','snygg sko',2),
('Stor-kängan','bababah',4),
('Långben','ipsum etc',5),
('Campus','också fin',2);

insert into model_partof_category (model_id,category_id) values 
(1,2),
(1,6),
(2,1),
(2,7),
(3,1),
(3,5),
(4,2),
(4,4),
(5,1),
(5,7);

insert into product (model_id,price,stock,size,colour)
values 
(1,400,24,38,'Svart'),
(1,400,12,40,'Svart'),
(2,1200,3,44,'Röd/Blå'),
(2,1000,6,42,'Grön/Gul'),
(3,1400,8,43,'Svart'),
(4,999,14,37,'Vit'),
(5,600,5,44,'Röd'),
(5,800,9,43,'Svart');

insert into customer (firstname,lastname,address,postcode)
values 
('Arvid','Utas','Gamla gatan 1',83989),
('James','Brown','Virriga vägen 7',84220),
('Bob','Marley','Torftiga torget 10',12107),
('Nina','Simone','Aspiga Allen 18',14840),
('Björk','Björksson','Lata leden 9',19561),
('Prince','Symbol','Sena stigen 6',23371);

insert into orders (order_time,customer_id,isActive) values
('2023-09-23',1,0),
('2023-11-09',2,0),
('2024-02-11',3,0),
('2024-06-04',4,0),
('2024-08-15',5,0),
('2024-09-05',1,0),
('2024-10-05',6,0);

insert into orders_contains_product (price,product_id,orders_id) values
(600,7,1),
(800,8,1),
(1400,5,2),
(600,7,3),
(400,1,4),
(999,6,4),
(999,1,5),
(1200,3,6),
(1000,4,7);

create index IX_name on model(name); -- Kommer finnas många och kommer ofta sökas av kund 
create index IX_city on postcode(city); -- Finns väldigt många post-orter, om man söker på postkod får man oftast inte en hel stad
create index IX_order_time on orders(order_time); -- Kommer antagligen sökas ofta av redovisningsskäl




