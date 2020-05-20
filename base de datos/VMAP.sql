/*==============================================================*/
/* Table: ESTUDIANTES                                           */
/*==============================================================*/
create table ESTUDIANTES
(
   CARNET               varchar(100) not null,
   NOMBRES              varchar(100) not null,
   APELLIDOS            varchar(100) not null,
   CARRERA              varchar(100) not null,
   ACTIVO               bool not null default false,
   primary key (CARNET)
);

/*==============================================================*/
/* Table: POINTS                                                */
/*==============================================================*/
create table POINTS
(
   ID_POINT             int not null auto_increment,
   X                    float not null,
   Y                    float not null,
   Z                    float not null,
   primary key (ID_POINT)
);

/*==============================================================*/
/* Table: LOCACIONES                                            */
/*==============================================================*/
create table LOCACIONES
(
   ID_LOCACION          int not null auto_increment,
   ID_POINT             int,
   NOMBRE               varchar(100) not null,
   DESCRIPCION          varchar(500) not null,
   FACULTAD             varchar(100) not null,
   HORARIOS             varchar(100) not null,
   primary key (ID_LOCACION),
   constraint FK_REFERENCE_3 foreign key (ID_POINT)
      references POINTS (ID_POINT)
);

/*==============================================================*/
/* Table: PINTERES                                              */
/*==============================================================*/
create table PINTERES
(
   CARNET               varchar(100) not null,
   ID_LOCACION          int not null,
   FECHA                datetime not null default 'GETDATE()',
   primary key (CARNET, ID_LOCACION),
   constraint FK_REFERENCE_1 foreign key (CARNET)
      references ESTUDIANTES (CARNET),
   constraint FK_REFERENCE_2 foreign key (ID_LOCACION)
      references LOCACIONES (ID_LOCACION)
);

/*==============================================================*/
/* Table: VERSIONADO                                            */
/*==============================================================*/
create table VERSIONADO
(
   CURRENT              varchar(10) not null default '1.0'
);
