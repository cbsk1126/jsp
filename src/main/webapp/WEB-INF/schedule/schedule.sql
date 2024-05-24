show tables;

create table schedule (
  idx   int not null auto_increment,	/* 스케줄관리 고유번호 */
  mid   varchar(20) not null,					/* 회원 아이디(일정검색시 필요) */
  sDate datetime not null,						/* 일정 등록 날짜 */
  part  varchar(10) not null,					/* 일정분류(1.모임, 2.업무 ,3.학습, 4.여행, 5:기타 */
  content text not null,							/* 일정 상세내역 */
  primary key(idx),
  foreign key(mid) references member(mid)
);

desc schedule;

insert into schedule values (default, 'hkd1234', '2024-05-3','모임', '초등학교동창회');
insert into schedule values (default, 'hkd1234', '2024-05-4','학습', '프로젝트기획');
insert into schedule values (default, 'hkd1234', '2024-05-4','기타', '병원예약');
insert into schedule values (default, 'kms1234', '2024-05-4','업무', '기획팀 5월 정기총회');
insert into schedule values (default, 'hkd1234', '2024-05-9','업무', '소득세 신고서 작성');
insert into schedule values (default, 'kms1234', '2024-05-14','모임', '초등학교동창회');
insert into schedule values (default, 'hkd1234', '2024-05-14','모임', '초등학교동창회');
insert into schedule values (default, 'hkd1234', '2024-05-18','학습', '자바프로그램 복습');
insert into schedule values (default, 'kms1234', '2024-05-20','업무', '담당자 미팅');
insert into schedule values (default, 'hkd1234', '2024-05-20','모임', '가족모임');
insert into schedule values (default, 'kms1234', '2024-05-20','여행', '제주도여행');
insert into schedule values (default, 'hkd1234', '2024-05-22','모임', '친구모임');
insert into schedule values (default, 'hkd1234', '2024-05-24','학습', '프로젝트 제안서 제출');
insert into schedule values (default, 'hkd1234', '2024-05-24','학습', '프로젝트 설계');
insert into schedule values (default, 'hkd1234', '2024-05-24','기타', '치과예약');
insert into schedule values (default, 'kms1234', '2024-05-24','모임', '고등학교 동창회');
insert into schedule values (default, 'hkd1234', '2024-05-25','여행', '한라산 등반');
insert into schedule values (default, 'admin', '2024-05-26','업무', '소득세 신고');
insert into schedule values (default, 'hkd1234', '2024-06-10','모임', '가족모임');
insert into schedule values (default, 'hkd1234', '2024-06-12','학습', 'JSP 복습');
insert into schedule values (default, 'kms1234', '2024-06-25','모임', '가족모임');
insert into schedule values (default, 'hkd1234', '2024-06-25','업무', '프로젝트 토론');
insert into schedule values (default, 'kms1234', '2024-06-30','학습', '스프링 학습시작');

select * from schedule where mid = 'hkd1234' and substring(sDate,1,10) = '2024-05-14' order by sDate;
select * from schedule where mid = 'hkd1234' and date_format(sDate, '%Y-%m-%d') = '2024-05-24' order by sDate;

select * from schedule where mid = 'hkd1234' and date_format(sDate, '%Y-%m') = '2024-05' order by sDate, part;
select *,count(*) as cnt from schedule where mid = 'hkd1234' and date_format(sDate, '%Y-%m') = '2024-05' order by sDate, part;
select *,count(*) as cnt from schedule where mid = 'hkd1234' and date_format(sDate, '%Y-%m') = '2024-05' group by sDate order by sDate, part;
select *,count(*) as cnt from schedule where mid = 'hkd1234' and date_format(sDate, '%Y-%m') = '2024-05' group by sDate,part order by sDate, part;
select *,count(*) as partCnt from schedule where mid = 'hkd1234' and date_format(sDate, '%Y-%m') = '2024-05' group by sDate,part order by sDate, part

select * from schedule where mid = 'hkd1234' and date_format(sDate, '%Y-%m-%d') = '2024-05-24' order by sDate, part;

select *,count(*) as cnt from schedule where mid = 'hkd1234' and date_format(sDate, '%Y-%m-%d') = '2024-05-24' order by sDate, part;

select *,count(*) as cnt from schedule where mid = 'hkd1234' and date_format(sDate, '%Y-%m-%d') = '2024-05-24' group by part  order by sDate, part;
