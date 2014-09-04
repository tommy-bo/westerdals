create table post_kontor (
	navn varchar(30) not null,
	postnummer_liste varchar(100) not null,
	primary key (navn)
);

insert into post_kontor(navn, postnummer_liste)
values('Tøyen', '1001 1002 1003 1004 1005 1006'),
values('Grønland', '1100 1101 1102'),
values('Sentrum', '0050 0051 0052 0053 0054 0055 0056 0057 0058 0059 0060');
