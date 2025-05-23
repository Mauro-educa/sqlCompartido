create database juegoCocina;
use juegoCocina;

drop table ticket;
drop table Linea_ticket;
drop table Receta;
drop table Cliente;

Create table Cliente(
codigo int(5) NOT NULL,
nombre varchar(20) NOT NULL,
foto varchar(50),
Constraint cod_pk primary key (codigo)
);
Create table Receta(
codigo int(5) NOT NULL,
nombre varchar(50) NOT NULL,
precio double,
foto varchar(50),
Constraint cod_pk primary key (codigo)
);
Create table Linea_ticket(
codigo int(5) NOT NULL,
Cod_ticket int(5) NOT NULL,
cod_receta int(3) NOT NULL,
precio_rec double,
Constraint Codrt foreign key (cod_receta) references receta(codigo),
Constraint Codt foreign key (cod_ticket) references ticket(codigo),
Constraint cod_pk primary key (codigo)
);
Create table ticket(
codigo int(3) NOT NULL,
cod_Lticket int(5) NOT NULL,
cod_cliente int NOT NULL,
Constraint CodLt foreign key (cod_Lticket) references linea_ticket(codigo),
Constraint CodCl foreign key (cod_cliente) references cliente(codigo),
Constraint cod_pk primary key (codigo)
);
Insert into Cliente(codigo, nombre, foto)
values
(0, 'Vinla Den','cliente0.png'),
(1, 'Ventarna Ventarnez','cliente1.png'),
(2, 'Manuel José','cliente2.png'), 
(3, 'Wi Lee Rex','cliente3.png'),
(4, 'Perro Sanxe','cliente4.png'),
(5, 'Col Etas','cliente5.png'),
(6, 'Avar Icia','cliente6.png'),
(7, 'Penaldo Elbicho','cliente7.png'),
(8, 'Ichiban Kasuga','cliente8.png'),
(9, 'Kilos Bappe', 'cliente9.png'),
(10, 'Pepsi Pulgas','cliente10.png'),
(11, 'Propanol Aupa','cliente11.png'),
(12, 'Seelvea Loupez', 'cliente12.png'),
(13, 'Morgan Godman', 'cliente13.png');

Insert into Receta(codigo, nombre, precio, foto)
values
(0, 'Hamburguesa de pollo', 4, 'receta0.png'),
(1, 'Hamburguesa de ternera', 5, 'receta1.png'),
(2, 'Nuggets', 7, 'receta2.png'), 
(3, 'Sad meal', 2, 'receta3.png'),
(4, 'Patatas fritas', 2, 'receta4.png'),
(5, 'Ensalada', 3, 'receta5.png'),
(6, 'Perrito caliente', 4, 'receta6.png'),
(7, 'Pizza margarita', 8, 'receta7.png'),
(8, 'Pizza jamón queso', 11, 'receta8.png'),
(9, 'Sándwich mixto', 3, 'receta9.png'),
(10, 'Patatas gajo', 5, 'receta10.png'),
(11, 'Refresco', 2, 'receta11.png');
