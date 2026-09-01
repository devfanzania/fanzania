INSERT INTO [FANTASYCRICKET].[dbo].[UserRole]
(UserRoleId,UserRole,UserRoleDesc)VALUES(1,'admin','Admin User of the apps')
INSERT INTO [FANTASYCRICKET].[dbo].[UserRole]
(UserRoleId,UserRole,UserRoleDesc)VALUES(2,'regular','Regular user for playing fantasy')

INSERT INTO CountryList(Country)VALUES('INDIA');
INSERT INTO CountryList(Country)VALUES('PAKISTAN');
INSERT INTO CountryList(Country)VALUES('BANGALADESH');
INSERT INTO CountryList(Country)VALUES('WEST INDIES');
INSERT INTO CountryList(Country)VALUES('SRI LANKA');
INSERT INTO CountryList(Country)VALUES('AFGANISTAN');
INSERT INTO CountryList(Country)VALUES('ENGLAND');



INSERT INTO [FANTASYCRICKET].[dbo].[Tournament]
(TournamentId,TournamentName,TournamentStatus,TournamentStage,TournamentStartDate)
VALUES(1,'INDTOURENGLAND','INPROGRESS','TOUR','2018-07-01')

INSERT INTO [FANTASYCRICKET].[dbo].[ParticipationTeam]
(ParticipationTeamName,TournamentId)
VALUES('IND',1)

INSERT INTO [FANTASYCRICKET].[dbo].[ParticipationTeam]
(ParticipationTeamName,TournamentId)
VALUES('ENG',1)



INSERT INTO [FANTASYCRICKET].[dbo].[Match]
(MatchNo,MatchType,TournamentId,Team1,Team2,MatchScheduledDate,MatchStatus,MatchDate)
VALUES(1,'TEST',1,'ENG','IND',GETDATE()-1,'UPCOMING',GETDATE())

INSERT INTO [FANTASYCRICKET].[dbo].[Match]
(MatchNo,MatchType,TournamentId,Team1,Team2,MatchScheduledDate,MatchStatus,MatchDate)
VALUES(2,'TEST',1,'ENG','IND',GETDATE()-1,'UPCOMING',GETDATE()+1)


INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification]
(TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(1,'VIRAT KOHLI','local','batsman',1,110000,1);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification]
(TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(1,'CHETESHWHAR PUJARA','local','batsman',1,95000,1);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification]
(TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(1,'MURALI VIJAY','local','batsman',1,95000,1);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification]
(TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(1,'SHIKHAR DHAWAN','local','batsman',1,95000,1);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification]
(TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(1,'AJINKYA RAHANE','local','batsman',1,95000,1);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification]
(TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(1,'KARUN NAIR','local','batsman',1,90000,1);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification]
(TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(1,'LOKESH RAHUL','local','batsman',1,95000,1);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification]
(TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(1,'SHREYAS IYER','local','batsman',1,85000,1);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification]
(TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(1,'RAVINDRA JADEJA','local','allrounder',1,90000,1);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification]
(TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(1,'DINESH KARTHIK','local','wicketkeeper',1,90000,1);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification]
(TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(1,'RISHABH PANT','local','wicketkeeper',1,85000,1);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification]
(TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(1,'JASPRIT BUMRAH','local','bowler',1,95000,1);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification]
(TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(1,'ISHANT SHARMA','local','bowler',1,95000,1);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification]
(TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(1,'KULDEEP YADAV','local','bowler',1,95000,1);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification]
(TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(1,'SHARDUL THAKUR','local','bowler',1,90000,1);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification]
(TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(1,'HARDIK PANDYA','local','allrounder',1,95000,1);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification]
(TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(1,'MOHAMMED SHAMI','local','bowler',1,90000,1);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification]
(TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(1,'UMESH YADAV','local','bowler',1,90000,1);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification]
(TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(1,'RAVICHANDRAN ASHWIN','local','allrounder',1,100000,1);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification]
(TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(1,'JONNY BAIRSTOW','foreign','wicketkeeper',1,95000,2);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification]
(TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(1,'JOE ROOT','foreign','batsman',1,105000,2);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification]
(TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(1,'JOS BUTTLER','foreign','wicketkeeper',1,100000,2);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification]
(TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(1,'MOEEN ALI','foreign','allrounder',1,95000,2);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification]
(TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(1,'BEN STOKES','foreign','allrounder',1,100000,2);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification]
(TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(1,'ADIL RASHID','foreign','bowler',1,90000,2);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification]
(TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(1,'JAMIE ANDERSON','foreign','bowler',1,100000,2);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification]
(TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(1,'STUART BROAD','foreign','bowler',1,95000,2);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification]
(TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(1,'ALASTAIR COOK','foreign','batsman',1,95000,2);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification]
(TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(1,'SAM CURRAN','foreign','allrounder',1,90000,2);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification]
(TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(1,'KEATON JENNINGS','foreign','batsman',1,85000,2);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification]
(TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(1,'OLLIE POPE','foreign','batsman',1,80000,2);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification]
(TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(1,'JAMIE PORTER','foreign','batsman',1,80000,2);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification]
(TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(1,'CHRIS WOAKES','foreign','allrounder',1,90000,2);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification]
(TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(1,'GH Vihari','local','bowler',1,65000,1);


INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(11728,'A Cook')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(440780,'K Jennings')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(8917,'M Ali')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(303669,'J Root')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(297433,'J Bairstow')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(311158,'B Stokes')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(308967,'J Buttler')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(662973,'S Curran')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(244497,'A Rashid')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(10617,'S Broad')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(8608,'J Anderson')

INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(32540,'C Pujara')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(253802,'V Kohli')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(277916,'A Rahane')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(452044,'G Vihari')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(931581,'R Pant')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(234675,'R Jadeja')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(481896,'M Shami')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(236779,'I Sharma')

INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(625383,'J Bumrah')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(28235,'S Dhawan')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(422108,'L Rahul')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(34102,'R Sharma')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(942645,'K Ahmed')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(430246,'Y Chahal')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(28081,'M Dhoni')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(290716,'K Jadhav')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(30045,'D Karthik')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(559235,'K Yadav')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(326016,'B Kumar')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(290630,'M Pandey')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(625371,'H Pandya')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(554691,'A Patel')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(33141,'A Rayudu')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(475281,'S Thakur')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(56007,'M Mortaza')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(56143,'S Al Hasan')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(550137,'A Hider')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(270135,'A Haque')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(536936,'L Das')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(56025,'Mahmudullah')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(629063,'M Hasan')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(269237,'M Mithun')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(550133,'M Hossain')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(56029,'M Rahim')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(330902,'M Rahman')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(629058,'N Hossain')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(446810,'N Islam')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(300619,'R Hossain')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(56194,'T Iqbal')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(49764,'A Mathews')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(429748,'A Aponso')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(552152,'D Chameera')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(300628,'D Chandimal')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(574178,'A Dananjaya')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(465793,'D de Silva')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(345821,'D Gunathilaka')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(49619,'S Lakmal')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(49758,'L Malinga')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(629074,'K Mendis')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(49920,'D Perera')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(300631,'K Perera')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(233514,'T Perera')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(499594,'K Rajitha')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(437316,'D Shanaka')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(50747,'U Tharanga')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(320652,'A Stanikzai')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(440963,'A Alam')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(352048,'G Naib')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(440970,'H Shahidi')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(703323,'Ihsanullah')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(308235,'J Ahmadi')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(25913,'M Nabi')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(419873,'M Shahzad')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(974109,'M Ur Rahman')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(633399,'M Ahmad')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(524049,'N Zadran')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(533956,'R Shah')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(793463,'R Khan')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(318339,'S Shenwari')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(524050,'S Shirzad')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(973945,'Wafadar')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(227760,'S Ahmed')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(494230,'A Ali')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(348144,'B Azam')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(681117,'F Ashraf')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(512191,'F Zaman')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(318788,'H Sohail')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(681305,'H Ali')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(568276,'Imam-ul-Haq')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(259551,'J Khan')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(290948,'M Amir')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(348148,'M Nawaz')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(922943,'S Khan')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(1072470,'S Afridi')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(233901,'S Masood')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(42657,'S Malik')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(697279,'U Khan')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(422965,'S Jayasuriya')

INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(237095,'M Vijay')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(26421,'R Ashwin')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(1070168,'P Shaw')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(471342,'K Pandya')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(376116,'U Yadav')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(261354,'N Coulter-Nile')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(334337,'P Handscomb')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(530011,'T Head')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(787987,'M Labuschagne')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(603410,'B McDermott')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(6683,'S Marsh')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(774223,'J Richardson')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(533042,'B Stanlake')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(459508,'A Tye')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(489889,'P Cummins')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(434813,'M Harris')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(326637,'C Lynn')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(325026,'G Maxwell')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(308798,'D Short')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(311592,'M Starc')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(5334,'A Finch')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(288284,'J Hazlewood')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(215155,'U Khawaja')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(272279,'N Lyon')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(272450,'M Marsh')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(7252,'T Paine')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(7898,'P Siddle')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(325012,'M Stoinis')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(379504,'A Zampa')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(553800,'C Tremain')
INSERT INTO [dbo].[APIPlayerKey](APIPId,PlayerName) values(32242,'P Patel')

UPDATE a SET a.APIPId = b.APIPId
			FROM [dbo].PlayerClassification a JOIN APIPlayerKey b 
ON (a.PlayerName = b.PlayerName)


insert into PointRules
(TournamentId,RunScored,FourBonus,SixBonus,HalfCenturyBonus,CenturyBonus,DismissalDuck,WicketTaken,
Wicket3UpBonus,Wicket5UpBonus,MaidenOver,Hattrick,Captain,CatchTaken,Stumping,RunOutDirect,RunOutThrower,RunOutCatcher,Nitro,MoM)
values(1,1,2,3,25,50,-10,10,15,25,2,25,2,10,10,10,4,6,2,50)




============ASIA Cup==========

INSERT INTO [FANTASYCRICKET].[dbo].[Tournament]
(TournamentId,TournamentName,TournamentStatus,TournamentStage,TournamentStartDate)
VALUES(2,'AsiaCup2018','INPROGRESS','LEAGUE','2018-09-15')

INSERT INTO [FANTASYCRICKET].[dbo].[ParticipationTeam]
(ParticipationTeamName,TournamentId,TeamDescription)
VALUES('IND',2,'India')

INSERT INTO [FANTASYCRICKET].[dbo].[ParticipationTeam]
(ParticipationTeamName,TournamentId,TeamDescription)
VALUES('PAK',2,'Pakistan')

INSERT INTO [FANTASYCRICKET].[dbo].[ParticipationTeam]
(ParticipationTeamName,TournamentId,TeamDescription)
VALUES('SRI',2,'Sri Lanka')

INSERT INTO [FANTASYCRICKET].[dbo].[ParticipationTeam]
(ParticipationTeamName,TournamentId,TeamDescription)
VALUES('BAN',2,'Bangladesh')

INSERT INTO [FANTASYCRICKET].[dbo].[ParticipationTeam]
(ParticipationTeamName,TournamentId,TeamDescription)
VALUES('AFG',2,'Afghanistan')

INSERT INTO [FANTASYCRICKET].[dbo].[ParticipationTeam]
(ParticipationTeamName,TournamentId,TeamDescription)
VALUES('HKG',2,'Hong Kong')

insert into PointRules
(TournamentId,RunScored,FourBonus,SixBonus,HalfCenturyBonus,CenturyBonus,DismissalDuck,WicketTaken,
Wicket3UpBonus,Wicket5UpBonus,MaidenOver,Hattrick,Captain,CatchTaken,Stumping,RunOutDirect,RunOutThrower,RunOutCatcher,Nitro,MoM)
values(2,1,2,3,25,50,-10,10,25,50,15,25,2,5,10,10,4,6,2,50)



INSERT INTO [FANTASYCRICKET].[dbo].[Match]
(MatchNo,MatchType,TournamentId,Team1,Team2,MatchScheduledDate,MatchStatus,MatchDate)
VALUES(1,'ODI',2,'BAN','SRI','2018-09-15 00:00:00','UPCOMING','2018-09-15 00:00:00')

INSERT INTO [FANTASYCRICKET].[dbo].[Match]
(MatchNo,MatchType,TournamentId,Team1,Team2,MatchScheduledDate,MatchStatus,MatchDate)
VALUES(2,'ODI',2,'HKG','PAK','2018-09-16 00:00:00','UPCOMING','2018-09-16 00:00:00')

INSERT INTO [FANTASYCRICKET].[dbo].[Match]
(MatchNo,MatchType,TournamentId,Team1,Team2,MatchScheduledDate,MatchStatus,MatchDate)
VALUES(3,'ODI',2,'AFG','SRI','2018-09-17 00:00:00','UPCOMING','2018-09-17 00:00:00')

INSERT INTO [FANTASYCRICKET].[dbo].[Match]
(MatchNo,MatchType,TournamentId,Team1,Team2,MatchScheduledDate,MatchStatus,MatchDate)
VALUES(4,'ODI',2,'HKG','IND','2018-09-18 00:00:00','UPCOMING','2018-09-18 00:00:00')

INSERT INTO [FANTASYCRICKET].[dbo].[Match]
(MatchNo,MatchType,TournamentId,Team1,Team2,MatchScheduledDate,MatchStatus,MatchDate)
VALUES(5,'ODI',2,'IND','PAK','2018-09-19 00:00:00','UPCOMING','2018-09-19 00:00:00')

INSERT INTO [FANTASYCRICKET].[dbo].[Match]
(MatchNo,MatchType,TournamentId,Team1,Team2,MatchScheduledDate,MatchStatus,MatchDate)
VALUES(6,'ODI',2,'AFG','BAN','2018-09-20 00:00:00','UPCOMING','2018-09-20 00:00:00')

INSERT INTO [FANTASYCRICKET].[dbo].[Match]
(MatchNo,MatchType,TournamentId,Team1,Team2,MatchScheduledDate,MatchStatus,MatchDate)
VALUES(7,'ODI',2,'UPCOMING','UPCOMING','2018-09-21 00:00:00','UPCOMING','2018-09-21 00:00:00')

INSERT INTO [FANTASYCRICKET].[dbo].[Match]
(MatchNo,MatchType,TournamentId,Team1,Team2,MatchScheduledDate,MatchStatus,MatchDate)
VALUES(8,'ODI',2,'UPCOMING','UPCOMING','2018-09-21 00:00:00','UPCOMING','2018-09-21 00:00:00')

INSERT INTO [FANTASYCRICKET].[dbo].[Match]
(MatchNo,MatchType,TournamentId,Team1,Team2,MatchScheduledDate,MatchStatus,MatchDate)
VALUES(9,'ODI',2,'UPCOMING','UPCOMING','2018-09-23 00:00:00','UPCOMING','2018-09-23 00:00:00')

INSERT INTO [FANTASYCRICKET].[dbo].[Match]
(MatchNo,MatchType,TournamentId,Team1,Team2,MatchScheduledDate,MatchStatus,MatchDate)
VALUES(10,'ODI',2,'UPCOMING','UPCOMING','2018-09-23 00:00:00','UPCOMING','2018-09-23 00:00:00')

INSERT INTO [FANTASYCRICKET].[dbo].[Match]
(MatchNo,MatchType,TournamentId,Team1,Team2,MatchScheduledDate,MatchStatus,MatchDate)
VALUES(11,'ODI',2,'UPCOMING','UPCOMING','2018-09-25 00:00:00','UPCOMING','2018-09-25 00:00:00')

INSERT INTO [FANTASYCRICKET].[dbo].[Match]
(MatchNo,MatchType,TournamentId,Team1,Team2,MatchScheduledDate,MatchStatus,MatchDate)
VALUES(12,'ODI',2,'UPCOMING','UPCOMING','2018-09-25 00:00:00','UPCOMING','2018-09-25 00:00:00')

INSERT INTO [FANTASYCRICKET].[dbo].[Match]
(MatchNo,MatchType,TournamentId,Team1,Team2,MatchScheduledDate,MatchStatus,MatchDate)
VALUES(13,'ODI',2,'UPCOMING','UPCOMING','2018-09-28 00:00:00','UPCOMING','2018-09-28 00:00:00')

INSERT INTO [FANTASYCRICKET].[dbo].[APIDetails]
(MatchId,APIName,APIKey,UniqueId,ModifyDateTime)
VALUES(4,'FantasySummary','amaf7LLSWhNqYz7J4aNA6RGNhmr2','1153243',GETDATE())

INSERT INTO [FANTASYCRICKET].[dbo].[APIDetails]
(MatchId,APIName,APIKey,UniqueId,ModifyDateTime)
VALUES(5,'FantasySummary','amaf7LLSWhNqYz7J4aNA6RGNhmr2','1153244',GETDATE())

INSERT INTO [FANTASYCRICKET].[dbo].[APIDetails]
(MatchId,APIName,APIKey,UniqueId,ModifyDateTime)
VALUES(6,'FantasySummary','amaf7LLSWhNqYz7J4aNA6RGNhmr2','1153245',GETDATE())

INSERT INTO [FANTASYCRICKET].[dbo].[APIDetails]
(MatchId,APIName,APIKey,UniqueId,ModifyDateTime)
VALUES(7,'FantasySummary','amaf7LLSWhNqYz7J4aNA6RGNhmr2','1153246',GETDATE())

INSERT INTO [FANTASYCRICKET].[dbo].[APIDetails]
(MatchId,APIName,APIKey,UniqueId,ModifyDateTime)
VALUES(8,'FantasySummary','amaf7LLSWhNqYz7J4aNA6RGNhmr2','1153247',GETDATE())

INSERT INTO [FANTASYCRICKET].[dbo].[APIDetails]
(MatchId,APIName,APIKey,UniqueId,ModifyDateTime)
VALUES(9,'FantasySummary','amaf7LLSWhNqYz7J4aNA6RGNhmr2','1153248',GETDATE())

INSERT INTO [FANTASYCRICKET].[dbo].[APIDetails]
(MatchId,APIName,APIKey,UniqueId,ModifyDateTime)
VALUES(10,'FantasySummary','amaf7LLSWhNqYz7J4aNA6RGNhmr2','1153249',GETDATE())

INSERT INTO [FANTASYCRICKET].[dbo].[APIDetails]
(MatchId,APIName,APIKey,UniqueId,ModifyDateTime)
VALUES(11,'FantasySummary','amaf7LLSWhNqYz7J4aNA6RGNhmr2','1153250',GETDATE())

INSERT INTO [FANTASYCRICKET].[dbo].[APIDetails]
(MatchId,APIName,APIKey,UniqueId,ModifyDateTime)
VALUES(12,'FantasySummary','amaf7LLSWhNqYz7J4aNA6RGNhmr2','1153251',GETDATE())

INSERT INTO [FANTASYCRICKET].[dbo].[APIDetails]
(MatchId,APIName,APIKey,UniqueId,ModifyDateTime)
VALUES(13,'FantasySummary','amaf7LLSWhNqYz7J4aNA6RGNhmr2','1153252',GETDATE())

INSERT INTO [FANTASYCRICKET].[dbo].[APIDetails]
(MatchId,APIName,APIKey,UniqueId,ModifyDateTime)
VALUES(14,'FantasySummary','amaf7LLSWhNqYz7J4aNA6RGNhmr2','1153253',GETDATE())

INSERT INTO [FANTASYCRICKET].[dbo].[APIDetails]
(MatchId,APIName,APIKey,UniqueId,ModifyDateTime)
VALUES(15,'FantasySummary','amaf7LLSWhNqYz7J4aNA6RGNhmr2','1153254',GETDATE())

INSERT INTO [FANTASYCRICKET].[dbo].[APIDetails]
(MatchId,APIName,APIKey,UniqueId,ModifyDateTime)
VALUES(16,'FantasySummary','amaf7LLSWhNqYz7J4aNA6RGNhmr2','1153255',GETDATE())


INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Rohit Sharma','local','batsman',1,95000,3);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Jasprit Bumrah','local','bowler',1,95000,3);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Bhuvneshwar Kumar','local','bowler',1,90000,3);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'MS Dhoni','local','wicketkeeper',1,90000,3);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Hardik Pandya','local','allrounder',1,90000,3);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Shikhar Dhawan','local','batsman',1,90000,3);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Lokesh Rahul','local','wicketkeeper',1,90000,3);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Dinesh Karthik','local','wicketkeeper',1,85000,3);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Yuzvendra Chahal','local','bowler',1,85000,3);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Kedar Jadhav','local','allrounder',1,85000,3);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Manish Pandey','local','batsman',1,85000,3);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Kuldeep Yadav','local','bowler',1,85000,3);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Shardul Thakur','local','bowler',1,85000,3);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Ambati Rayudu','local','batsman',1,85000,3);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Axar Patel','local','allrounder',1,80000,3);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Khaleel Ahmed','local','bowler',1,75000,3);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Hasan Ali','local','bowler',1,95000,4);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Shoaib Malik','local','allrounder',1,95000,4);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Mohammad Amir','local','bowler',1,95000,4);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Babar Azam','local','batsman',1,90000,4);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Sarfraz Ahmed','local','wicketkeeper',1,90000,4);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Junaid Khan','local','bowler',1,90000,4);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Fakhar Zaman','local','batsman',1,90000,4);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Shadab Khan','local','bowler',1,85000,4);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Mohammad Nawaz','local','allrounder',1,85000,4);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Haris Sohil','local','batsman',1,85000,4);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Shan Masood','local','batsman',1,85000,4);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Asif Ali','local','batsman',1,80000,4);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Usman Khan','local','bowler',1,80000,4);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Faheem Ashraf','local','allrounder',1,80000,4);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Shaheen Afridi','local','bowler',1,75000,4);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Imam-ul-Haq','local','batsman',1,75000,4);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Angelo Mathews','local','allrounder',1,95000,5);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Lasith Malinga','local','bowler',1,95000,5);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Thisara Perera','local','allrounder',1,90000,5);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'GSNFG Jayasuriya','local','allrounder',1,80000,5);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Kusal Mendis','local','batsman',1,90000,5);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Kusal Perera','local','wicketkeeper',1,90000,5);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Dinesh Chandimal','local','wicketkeeper',1,85000,5);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Upul Tharanga','local','batsman',1,90000,5);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Niroshan Dickwella','local','wicketkeeper',1,85000,5);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Dilruwan Perera','local','allrounder',1,85000,5);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Dushmantha Chameera','local','bowler',1,85000,5);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Akila Dananjaya','local','allrounder',1,85000,5);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Dhananjaya de Silva','local','allrounder',1,85000,5);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Suranga Lakmal','local','bowler',1,85000,5);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Danushka Gunathilaka','local','allrounder',1,80000,5);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Dasun Shanaka','local','allrounder',1,80000,5);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Amila Aponso','local','bowler',1,80000,5);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Kasun Rajitha','local','bowler',1,75000,5);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Shakib Al Hasan','local','allrounder',1,100000,6);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Mustafizur Rahman','local','bowler',1,95000,6);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Mushfiqur Rahim','local','wicketkeeper',1,95000,6);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Tamim Iqbal','local','batsman',1,90000,6);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Mahmudullah','local','allrounder',1,90000,6);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Rubel Hossain','local','bowler',1,85000,6);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Mehidy Hasan','local','allrounder',1,85000,6);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Mashrafe Mortaza','local','allrounder',1,85000,6);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Liton Das','local','wicketkeeper',1,85000,6);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Mominul Haque','local','batsman',1,85000,6);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Mosaddek Hossain','local','allrounder',1,80000,6);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Mohammad Mithun','local','batsman',1,75000,6);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Nazmul Hossain','local','allrounder',1,75000,6);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Nazmul Islam','local','bowler',1,75000,6);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Abu Hider','local','allrounder',1,75000,6);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Ariful Haque','local','allrounder',1,75000,6);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Rashid Khan','local','bowler',1,100000,7);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Mujeeb Ur Rahman','local','bowler',1,95000,7);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Mohammad Shahzad','local','wicketkeeper',1,90000,7);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Mohammad Nabi','local','allrounder',1,90000,7);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Samiullah Shenwari','local','allrounder',1,85000,7);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Asghar Afghan','local','batsman',1,85000,7);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Rahmat Shah','local','allrounder',1,80000,7);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Gulbadin Naib','local','allrounder',1,75000,7);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Hashmatullah Shahidi','local','batsman',1,75000,7);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Yamin Ahmadzai','local','allrounder',1,70000,7);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Aftab Alam','local','bowler',1,75000,7);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Javed Ahmadi','local','batsman',1,75000,7);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Najibullah Zadran','local','batsman',1,75000,7);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Munir Ahmad','local','wicketkeeper',1,75000,7);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Sayed Shirzad','local','bowler',1,70000,7);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Ihsanullah Janat','local','batsman',1,70000,7);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Anshuman Rath','local','wicketkeeper',1,80000,8);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Babar Hayat','local','batsman',1,80000,8);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Aizaz Khan','local','allrounder',1,80000,8);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Arshad Haroon Mohammad','local','allrounder',1,80000,8);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Tanwir Afzal','local','allrounder',1,75000,8);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Nadeem Ahmed','local','bowler',1,75000,8);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Christopher Carter','local','wicketkeeper',1,75000,8);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Scott McKechnie','local','batsman',1,75000,8);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Kinchit Shah','local','allrounder',1,75000,8);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Nizakat Khan','local','allrounder',1,75000,8);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Ehsan Khan','local','bowler',1,75000,8);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Waqas Khan','local','batsman',1,70000,8);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Tanveer Ahmed','local','bowler',1,70000,8);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Aftab Hussain','local','bowler',1,70000,8);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Raag Kapur','local','bowler',1,70000,8);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Cameron McAuslan','local','batsman',1,70000,8);
INSERT INTO [FANTASYCRICKET].[dbo].[PlayerClassification](TournamentId,PlayerName,PlayerType,PlayerSpeciality,PlayerStatus,PlayerValue,ParticipationTeamId) VALUES(2,'Ehsan Nawaz','local','bowler',1,70000,8);

