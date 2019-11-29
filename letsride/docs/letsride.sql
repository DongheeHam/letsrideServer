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


DROP TABLE IF EXISTS SESSION;
create table SESSION(
	l_token varchar(128) not null comment "l토큰",
	sessionid varchar(128) not null unique key comment "session.getId()",
	current_rno int(11) comment "현재 접속방",
	make_rno int(11) comment "만든 방(방장인 방)",
	date datetime default now() 	comment "접속 시간",
	
	primary key(l_token),
	unique key(sessionid),
	index current_rno(current_rno)
)ENGINE=InnoDB;

DROP TABLE IF EXISTS ROOM;
create table ROOM(
	rno int(11) not null auto_increment 	comment "방 번호",
	title varchar(128) not null comment "방 제목",
	maker varchar(128) not null 	comment "방장 (session.l_token 참조)",
	schedule varchar(128) comment "출발예정시간(ex : 약9시 10분)",
	num int(5) comment "현재인원",
	date datetime default now() 	comment "방 개설 시간",
	
	primary key(rno),
	constraint SESSION_ROOM_1 foreign key (maker) 
	references SESSION(l_token) on delete cascade on update cascade
)ENGINE=InnoDB;

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

