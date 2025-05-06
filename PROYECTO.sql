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
Constraint cod_pk primary key (codigo)
);
Create table Linea_ticket(
codigo int(5) NOT NULL,
cod_receta int(3) NOT NULL,
precio double,
Constraint Codrt foreign key (cod_receta) references receta(codigo),
Constraint cod_pk primary key (codigo)
);
Create table ticket(
codigo int(3) NOT NULL,
cod_Lticket int(5) NOT NULL,
cod_cliente int(5) NOT NULL,
Constraint CodLt foreign key (cod_Lticket) references linea_ticket(codigo),
Constraint CodCl foreign key (cod_cliente) references cliente(codigo),
Constraint cod_pk primary key (codigo)
);
Insert into Cliente(codigo, nombre, foto)
values
(1, 'Encarne',' ___.jpg'),
(6, 'Vinla Den',' ___.jpg'),
(2, 'Manuel José',' ___.jpg'), 
(3, 'Wi lee rex',' ___.jpg'),
(4, 'Perro xanches',' ___.jpg'),
(5, 'Col Etas',' ___.jpg');
Insert into Receta(codigo, nombre, precio)
values
(1, 'Hamburguesa de ternera', 5),
(6, 'Hamburguesa de pollo', 4),
(2, 'Nuggets', 7), 
(3, 'Happy meal', 2),
(4, 'Patatas fritas', 2),
(5, 'Ensalada', 3);
