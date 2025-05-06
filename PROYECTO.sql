Create table Cliente(
codigo int(5) NOT NULL,
nombre varchar(20) NOT NULL,
foto varchar(50) NOT NULL,
Constraint cod_pk primary key (codigo)
)
Create table Receta(
codigo int(5) NOT NULL,
nombre varchar(50) NOT NULL,
precio double,
Constraint cod_pk primary key (codigo)
)
Create table Linea_ticket(
codigo int(5) NOT NULL,
cod_receta int(3) NOT NULL,
precio double,
Constraint Codrt foreign key (cod_receta) references receta(codigo),
Constraint cod_pk primary key (codigo)
)
