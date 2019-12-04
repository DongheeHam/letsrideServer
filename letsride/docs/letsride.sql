use letsride

DROP TABLE IF EXISTS USER;
create table USER(
	id int(11) not null 	comment "카톡id",
	nickname varchar(128) comment "카톡이름",
	imagepath varchar(512) comment "프사위치",
	gender enum('f','m') comment "성별",
	l_token varchar(128) not null comment "l토큰(uuid)(카톡accessToken이 변경될 때 같이 갱신)",
	access_token varchar(128) not null comment "카톡 엑세스토큰",
	date datetime default now() 	comment "가입일",
	
	primary key(id),
	unique key(l_token)
)ENGINE=InnoDB;


DROP TABLE IF EXISTS ROOM;
create table ROOM(
	rno int(11) not null auto_increment 	comment "방 번호",
	title varchar(128) not null comment "방 제목",
	maker varchar(128) not null 	comment "방장 (session.l_token 참조)",
	schedule varchar(128) comment "출발예정시간(ex : 약9시 10분)",
	gender enum('f','m','a') default 'a' comment "허용성별 _female male all_",
	destination varchar(64) comment "도착지 _정문,성결관,중생관_",
	num int(5) default 0 comment "현재인원",
	date datetime default now() comment "방 개설 시간",
	
	primary key(rno)
)ENGINE=InnoDB;

DROP TABLE IF EXISTS USER_IN_ROOM;
create table USER_IN_ROOM(
	rno int(11) not null comment "방 번호",
	l_token varchar(128) not null comment "유저 토큰",
	
	primary key(rno,l_token),
	constraint USER_IN_ROOM_1 foreign key (rno) 
	references ROOM(rno) on delete cascade on update cascade
)ENGINE=InnoDB;

create trigger ins_USER_IN_ROOM after insert on USER_IN_ROOM
for each row update ROOM set ROOM.num=(select count(*) from USER_IN_ROOM where USER_IN_ROOM.rno=NEW.rno) where ROOM.rno=NEW.rno;

create trigger del_USER_IN_ROOM after delete on USER_IN_ROOM
for each row update ROOM set ROOM.num=(select count(*) from USER_IN_ROOM where USER_IN_ROOM.rno=old.rno) where ROOM.rno=old.rno;

insert into ROOM (title,maker,schedule,destination,gender) values ('테스트방1','qqq','약 9시 12분','성결관','a');
insert into ROOM (title,maker,schedule,destination,gender) values ('테스트방2','qqq','약 11시 0분','중생관','f');
insert into ROOM (title,maker,schedule,destination,gender) values ('테스트방3','qqq','약 12시 20분','정문','m');

insert into CHAT (rno,writer,content) values ('2','www','안녕안녕');
insert into CHAT (rno,writer,content) values ('2','eee','방가방가');
insert into CHAT (rno,writer,content) values ('2','www','어디어디');
insert into CHAT (rno,writer,content) values ('1','rrr','어디야');
insert into CHAT (rno,writer,content) values ('3','ccc','안농');

DROP TABLE IF EXISTS CHAT;
create table CHAT( 
	cno int(11) not null auto_increment comment "글 번호",
	rno int(11) not null comment "글이 작성된 방 (참조)",
	writer varchar(128) not null comment "작성자 session.l_token",
	content varchar(10000) not null comment "내용",
	date datetime default now() 	comment "작성 시간",
	
	primary key(cno),
	constraint CHAT_1 foreign key (rno) 
	references ROOM(rno) on delete cascade on update cascade
)ENGINE=InnoDB;

DROP TABLE IF EXISTS SESSION;
create table SESSION(
	l_token varchar(128) not null comment "l토큰",
	sessionid varchar(128) not null unique key comment "session.getId()",
	current_rno int(11) comment "현재 접속방",
	make_rno int(11) comment "만든 방(방장인 방)",
	date datetime default now() comment "접속 시간",
	
	primary key(l_token),
	unique key(sessionid),
	index current_rno(current_rno)
)ENGINE=InnoDB;

