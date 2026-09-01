USE master;  
GO  

SET ANSI_NULLS ON
GO
  
IF NOT EXISTS(SELECT * from sys.databases WHERE name='FANTASYCRICKET')  
BEGIN  
    CREATE DATABASE FANTASYCRICKET;  
END  

GO

USE [FANTASYCRICKET]
GO
SET ANSI_NULLS ON
GO

CREATE TABLE [dbo].[UserRole] (
[UserRoleId] [int]  NOT NULL,--1 admin, 2 regular
[UserRole] [varchar](50) NOT NULL,
[UserRoleDesc] [varchar](100)  NULL,
PRIMARY KEY CLUSTERED 
(
	[UserRoleId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];

GO

CREATE TABLE [dbo].[Users] (
  [UserId] [int] IDENTITY(1,1) NOT NULL,
  [UserName] [varchar](100)  NULL,
  [Name] [varchar](255)  NULL,
  [Password] varchar(225)  NULL,
  [Email] varchar(256)  NULL,
  [CountryId] int NULL,
  [FirstName] [varchar](50) NULL,
  [LastName] [varchar](50) NULL,
  [DOB] DATE NULL,
  [PhoneNumber] varchar(30) NULL,
  [ActivationToken] varchar(256)  NULL,
  [Active] [bit] NULL DEFAULT (0),
  [UserRoleId] int NOT NULL,
  [SignUpDate] [datetime]  NULL,
  [LastSignIn] [datetime] NULL,
  [LastPasswordFailureDate] [datetime] NULL,
  [PasswordFailuresSinceLastSuccess] [int]  NULL DEFAULT(0),
  [PasswordChangedDate] [datetime] NULL,
  [PasswordVerificationToken] [varchar](256) NULL,
  [SessionId] [varchar](100) NULL,
  [LoginLocation] [varchar](100) NULL,
  [SessionCreationDate] [datetime] NULL,
  [SessionActive] [bit] NULL,
  [BackgroundTheme] varchar(100) NULL,
  [WalletPoints] int NULL,
  [DailyTotal] int NULL,
  [TournamentTotal] int NULL,
  [OrigWalletPoints] int NULL default(0),
  [OrigDailyTotal] int NULL default(0),
  [OrigTournamentTotal] int NULL default(0),
  [WeeklyPoints] int NULL,
  [WeeklyRank] int NULL,
  [DeviceMacAddress] varchar(500) NULL,
  [LoginProvider] varchar(200) NULL,
  [LoginProviderAccessToken] varchar(500) NULL,
  [ProfileImage] varchar(50) NULL,
  [LastLoginDeviceType] varchar(20) NULL,
  [AndroidNotificationId] varchar(250) NULL,
  [IOSNotificationId] varchar(250) NULL,
  [WebNotificationId] varchar(250) NULL,
  [CommPreference] bit NULL,
  ReferralCode varchar(20) NULL,
  ReferralCodeUsed varchar(20) NULL,
  ReferralCount int NULL DEFAULT(0),
  ReferralCountRemains int NULL DEFAULT(0),
  UserTier int NULL DEFAULT(1),--1 BRONZE, 2 SILVER, 3 GOLD, 4 PLATINUM
  TierStartDate DATE NULL,
  SubscriptionType int NULL DEFAULT(0),
  SubscriptionDate [datetime] NULL,
  LoginPreference varchar(20) NULL, -- tournament, match
PRIMARY KEY CLUSTERED 
(
	[UserId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];

GO

CREATE TABLE [dbo].[UserProfile] (
[UserProfileId] int IDENTITY(1,1) NOT NULL,
[UserId] int NOT NULL,
[KYCDocName] [varchar](50) NULL,
[KYCDocImage] [varchar](50) NULL,
PANName varchar(100) NULL,
PANNumber varchar(20) NULL,
PANDOB varchar(20) NULL,
PANState varchar(30) NULL,
KYCStatus varchar(20) NULL, --pending, submitted , saved , approved, rejected
MobileVerified varchar(5) NULL,
RPContactId varchar(100) NULL,
BankVerified varchar(5) NULL,
RPfaId varchar(100) NULL,
BankVerificationDate [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[UserProfileId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];

GO

CREATE TABLE  [dbo].[Tournament] (
  [TournamentId] int IDENTITY(1,1) NOT NULL,
  [TournamentName] varchar(50) NULL, 
  [TournamentStatus] varchar(50) NULL,
  [TournamentStage] varchar(20) NULL,--T20, ODI, TEST
  [TournamentLogo] varchar(30) NULL,
  [StartIndicator] bit NULL DEFAULT(0),
  [TournamentComplete] bit NULL,
  [TournamentStartDate] Date NULL,
  [TournamentEndDate] Date NULL,
  [TriggerVal] varchar(20) NULL,
  TournamentType varchar(10) NULL, -- T, D, S
  [TournamentKey] varchar(100) NULL,
PRIMARY KEY CLUSTERED 
(
	[TournamentId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];

GO

CREATE TABLE  [dbo].[League] (
  [LeagueId] [int] IDENTITY(1,1) NOT NULL,
  [LeagueName] varchar(150) NOT NULL,
  [LeaguePin] varchar(30) NOT NULL,
  [TournamentId] int  NOT NULL,
  [LeagueLeader] [varchar](256) NULL,
  [LeagueLeaderId] int NULL,
  [LeaguePoints] [int] NULL DEFAULT 0,
  [LeagueRank] int NULL,
  [LeagueCreationDate] datetime NULL,
PRIMARY KEY CLUSTERED 
(
	[LeagueId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];

GO

CREATE TABLE  [dbo].[UserLeague] (
  [UserLeagueId] [int] IDENTITY(1,1) NOT NULL,
  [UserId] int NOT NULL,
  [LeagueId] int NOT NULL,
  [LeagueApproved] bit  NULL,
  [IsLeagueLeader] varchar(10)  NULL,
  [TeamOldLeagueRank] int NULL DEFAULT(0),
  [TeamNewLeagueRank] int NULL DEFAULT(0),
  [LeagueJoinedDate] datetime NULL DEFAULT(NULL),
  [LeagueApprovedDate] datetime NULL DEFAULT(NULL),
PRIMARY KEY CLUSTERED 
(
	[UserLeagueId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];

GO

CREATE TABLE [dbo].[UserTeam] (
  [UserTeamId] [int] IDENTITY(1,1) NOT NULL,
  [UserTeamName] varchar(150) NOT NULL,
  [TournamentId] int NOT NULL,
  [UserId] int NOT NULL,
  [SubsLeftAtSnapShot] int NULL,
  [SubsLeft] int NULL,
  [NitroLeft] int NULL,
  [PainKillerLeft] int NULL,
  [AutoPilotLeft] int NULL,
  [TotalPoints] int  NULL DEFAULT(0),
  [LastMatchPoints] int NULL DEFAULT(0),
  [TeamCompositionId] int NULL,
  [TeamRank] int NULL,
  [CreatedDate] datetime NULL,
  [ModifiedDate] datetime NULL,
  [NitroUsedCounter] int NULL DEFAULT(0),
  [PainKillerUsedCounter] int NULL DEFAULT(0),
  [AutoPilotUsedCounter] int NULL DEFAULT(0),
PRIMARY KEY CLUSTERED 
(
	[UserTeamId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];

GO

CREATE TABLE [dbo].[ParticipationTeam] (
  [ParticipationTeamId] [int] IDENTITY(1,1) NOT NULL,
  [ParticipationTeamName] varchar(50) NOT NULL,
  [TeamShortName] varchar(5) NULL,
  [TeamDescription] varchar(150)  NULL,
  [TeamImage] varchar(50) NULL,
  [TournamentId] int NULL,
  [MatchPlayed] int NOT NULL DEFAULT(0),
  [MatchWon] int NOT NULL DEFAULT(0),
  [MatchLost] int NOT NULL DEFAULT(0),
  [MatchDraw] int NOT NULL DEFAULT(0),
  [TeamPoints] int NOT NULL DEFAULT(0),
  DailyActive bit NULL DEFAULT(1),
  RapidTeamId int NULL,
  TeamKey varchar(20) NULL,
PRIMARY KEY CLUSTERED 
(
	[ParticipationTeamId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];  

GO

CREATE TABLE [dbo].[PlayerClassification] (
  [PlayerId] [int] IDENTITY(1,1) NOT NULL,
  [TournamentId] int NULL,
  [APIPId] int NULL ,
  [PlayerName] varchar(50) NOT NULL,
  [PlayerShortName] varchar(30) NULL,
  [PlayerDesc] varchar(5000)  NULL,
  [PlayerType] varchar(20)  NULL,-- overseas or local or capped or uncapped
  [PlayerSpeciality] varchar(20)  NULL,--batsman, bowler, allrounder, 
  [PlayerStatus] bit NULL,--active 1, inactive 0
  [PlayerValue] int NULL,
  [ParticipationTeamId] int null,
  [TotalPoints] int null default(0),
  PlayingInd bit null default(0),
PRIMARY KEY CLUSTERED 
(
	[PlayerId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];


GO

CREATE TABLE [dbo].[APIPlayerKey] (
  [APIPId] [int] IDENTITY(1,1) NOT NULL,
  PlayerKey varchar(150) null,
  [PlayerName] varchar(150) NOT NULL,
  [PlayerImage] varchar(30) NULL,
  RapidPlayerId int NULL,
  PlayerStats varchar(3000) NULL,
PRIMARY KEY CLUSTERED 
(
	[APIPId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];

GO

CREATE TABLE [dbo].[PaymentGatewayDetails] (
  [PaymentGatewayId] [int] IDENTITY(1,1) NOT NULL,
  [Environment] varchar(20) null,
  [PaymentGatewayName] varchar(150) NOT NULL,
  [PGAuthKey] varchar(200) NULL,
  [PGClientId] varchar(200) NULL,
  [PGClientSecret] varchar(200) NULL,
  [URL] varchar(200) NULL,
PRIMARY KEY CLUSTERED 
(
	[PaymentGatewayId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];


GO
CREATE TABLE [dbo].[TeamComposition] (
  [TeamCompositionId] [int] IDENTITY(1,1) NOT NULL,
  [Player1] int NULL,
  [Player2] int NULL,
  [Player3] int NULL,
  [Player4] int NULL,
  [Player5] int NULL,
  [Player6] int NULL,
  [Player7] int NULL,
  [Player8] int NULL,
  [Player9] int NULL,
  [Player10] int NULL,
  [Player11] int NULL,
  [TeamCapt] int NULL,
  [TeamVCapt] int NULL,
  [NitroUsed] bit NULL DEFAULT(0),
  [PainKillerUsed] bit NULL DEFAULT(0),
  [AutoPilotUsed] bit NULL DEFAULT(0),
  [CreatedByUserId] int NULL,
  [CreatedDateTime] datetime NULL,
  [UpdatedDateTime] datetime NULL,
  [TeamOpen] bit NULL DEFAULT(1),
  WinnerPrediction varchar(50) NULL,
PRIMARY KEY CLUSTERED 
(
	[TeamCompositionId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];

GO


GO
CREATE TABLE [dbo].[UserTeamSnapshot] (
  [SnapshotId] [int] IDENTITY(1,1) NOT NULL,
  [MatchId] int NOT NULL,
  [UserTeamId] int NOT NULL,
  [UserId] int NULL,
  [Inning1TeamCompositionId] int NULL,
  [Inning2TeamCompositionId] int NULL,
  [Inning3TeamCompositionId] int NULL,
  [Inning4TeamCompositionId] int NULL,
  [Inning1Capt] int NULL,
  [Inning1VCapt] int NULL,
  [Inning2Capt] int NULL,
  [Inning3Capt] int NULL,
  [Inning4Capt] int NULL,
  [TransferUsed] int NULL,
  [NitroMultiplier] int NULL DEFAULT(1),
  [PainKillerUsed] bit NULL DEFAULT(0),
  [AutoPilotUsed] bit NULL DEFAULT(0),
  [CreatedDate] datetime NULL,
  [ModifiedDate] datetime NULL,
  WinnerPrediction varchar(50) NULL,
PRIMARY KEY CLUSTERED 
(
	[SnapshotId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];

GO

CREATE TABLE [dbo].[UserTeamMatchPoint] (
  [UserTeamMatchPointId] [int] IDENTITY(1,1) NOT NULL,
  [MatchId] int NOT NULL,
  [UserTeamId] int NOT NULL,
  [UserId] int NULL,
  [Player1] int NULL,
  [Player2] int NULL,
  [Player3] int NULL,
  [Player4] int NULL,
  [Player5] int NULL,
  [Player6] int NULL,
  [Player7] int NULL,
  [Player8] int NULL,
  [Player9] int NULL,
  [Player10] int NULL,
  [Player11] int NULL,
  [Inning1Capt] int NULL,
  [Inning1VCapt] int NULL,
  [Inning2Capt] int NULL,
  [Inning3Capt] int NULL,
  [Inning4Capt] int NULL,
  [Player1Point] int NULL,
  [Player2Point] int NULL,
  [Player3Point] int NULL,
  [Player4Point] int NULL,
  [Player5Point] int NULL,
  [Player6Point] int NULL,
  [Player7Point] int NULL,
  [Player8Point] int NULL,
  [Player9Point] int NULL,
  [Player10Point] int NULL,
  [Player11Point] int NULL,
  [PainKillerPlayerPoint] int NULL,
  [AveragePoints] int NULL,
  [WinnerPredictionPoints] int NULL default(0),
  [MatchTotalPoints] int NULL,
  [CreatedDate] datetime NULL
PRIMARY KEY CLUSTERED 
(
	[UserTeamMatchPointId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];

GO

CREATE TABLE [dbo].[MatchPlayerStats] (
  [MatchPlayerStatsId] [int] IDENTITY(1,1) NOT NULL,
  [PlayerId] int NULL,
  [PlayerName] varchar(100) NULL,
  [MatchId] int NULL,
  [APIPId] int NULL DEFAULT(0),
  [Inning1Frunout] int NULL DEFAULT(0),
  [Inning1Fstumped] int NULL DEFAULT(0),
  [Inning1Fbowled] int NULL DEFAULT(0),
  [Inning1Flbw] int NULL DEFAULT(0),
  [Inning1Fcatche] int NULL DEFAULT(0),
  [Inning1BL6s] int NULL DEFAULT(0),
  [Inning1BL4s] int NULL DEFAULT(0),
  [Inning1BL0s] int NULL DEFAULT(0),
  [Inning1BLecon] decimal(5,2) NULL DEFAULT(0),
  [Inning1BLwicket] int NULL DEFAULT(0),
  [Inning1BLrun] int NULL DEFAULT(0),
  [Inning1BLmaiden] int NULL DEFAULT(0),
  [Inning1BLover] decimal(5,1) NULL DEFAULT(0),
  [Inning1BLhattrick] int NULL DEFAULT(0),
  [Inning1BTdismissal] varchar(30) NULL ,
  [Inning1BTstrikerate] decimal(5,2) NULL DEFAULT(0),
  [Inning1BT6s] int NULL DEFAULT(0),
  [Inning1BT4s] int NULL DEFAULT(0),
  [Inning1BTballfaced] int NULL DEFAULT(0),
  [Inning1BTrun] int NULL DEFAULT(0),
  [Inning1BTdismissalinfo] varchar(200) NULL ,
  [Inning2Frunout] int NULL DEFAULT(0),
  [Inning2Fstumped] int NULL DEFAULT(0),
  [Inning2Fbowled] int NULL DEFAULT(0),
  [Inning2Flbw] int NULL DEFAULT(0),
  [Inning2Fcatche] int NULL DEFAULT(0),
  [Inning2BL6s] int NULL DEFAULT(0),
  [Inning2BL4s] int NULL DEFAULT(0),
  [Inning2BL0s] int NULL DEFAULT(0),
  [Inning2BLecon] decimal(5,2) NULL DEFAULT(0),
  [Inning2BLwicket] int NULL DEFAULT(0),
  [Inning2BLrun] int NULL DEFAULT(0),
  [Inning2BLmaiden] int NULL DEFAULT(0),
  [Inning2BLover] decimal(5,1) NULL DEFAULT(0),
  [Inning2BLhattrick] int NULL DEFAULT(0),
  [Inning2BTdismissal] varchar(30) NULL ,
  [Inning2BTstrikerate] decimal(5,2) NULL DEFAULT(0),
  [Inning2BT6s] int NULL DEFAULT(0),
  [Inning2BT4s] int NULL DEFAULT(0),
  [Inning2BTballfaced] int NULL DEFAULT(0),
  [Inning2BTrun] int NULL DEFAULT(0),
  [Inning2BTdismissalinfo] varchar(200) NULL ,  
  [Inning3Frunout] int NULL DEFAULT(0),
  [Inning3Fstumped] int NULL DEFAULT(0),
  [Inning3Fbowled] int NULL DEFAULT(0),
  [Inning3Flbw] int NULL DEFAULT(0),
  [Inning3Fcatche] int NULL DEFAULT(0),
  [Inning3BL6s] int NULL DEFAULT(0),
  [Inning3BL4s] int NULL DEFAULT(0),
  [Inning3BL0s] int NULL DEFAULT(0),
  [Inning3BLecon] decimal(5,2) NULL DEFAULT(0),
  [Inning3BLwicket] int NULL DEFAULT(0),
  [Inning3BLrun] int NULL DEFAULT(0),
  [Inning3BLmaiden] int NULL DEFAULT(0),
  [Inning3BLover] decimal(5,1) NULL DEFAULT(0),
  [Inning3BLhattrick] int NULL DEFAULT(0),
  [Inning3BTdismissal] varchar(30) NULL ,
  [Inning3BTstrikerate] decimal(5,2) NULL DEFAULT(0),
  [Inning3BT6s] int NULL DEFAULT(0),
  [Inning3BT4s] int NULL DEFAULT(0),
  [Inning3BTballfaced] int NULL DEFAULT(0),
  [Inning3BTrun] int NULL DEFAULT(0),
  [Inning3BTdismissalinfo] varchar(200) NULL ,
  [Inning4Frunout] int NULL DEFAULT(0),
  [Inning4Fstumped] int NULL DEFAULT(0),
  [Inning4Fbowled] int NULL DEFAULT(0),
  [Inning4Flbw] int NULL DEFAULT(0),
  [Inning4Fcatche] int NULL DEFAULT(0),
  [Inning4BL6s] int NULL DEFAULT(0),
  [Inning4BL4s] int NULL DEFAULT(0),
  [Inning4BL0s] int NULL DEFAULT(0),
  [Inning4BLecon] decimal(5,2) NULL DEFAULT(0),
  [Inning4BLwicket] int NULL DEFAULT(0),
  [Inning4BLrun] int NULL DEFAULT(0),
  [Inning4BLmaiden] int NULL DEFAULT(0),
  [Inning4BLover] decimal(5,1) NULL DEFAULT(0),
  [Inning4BLhattrick] int NULL DEFAULT(0),
  [Inning4BTdismissal] varchar(30) NULL ,
  [Inning4BTstrikerate] decimal(5,2) NULL DEFAULT(0),
  [Inning4BT6s] int NULL DEFAULT(0),
  [Inning4BT4s] int NULL DEFAULT(0),
  [Inning4BTballfaced] int NULL DEFAULT(0),
  [Inning4BTrun] int NULL DEFAULT(0),
  [Inning4BTdismissalinfo] varchar(200) NULL ,
  [Runs] int NOT NULL DEFAULT(0),
  [Wickets] int NOT NULL DEFAULT(0),
  [Catches] int NOT NULL DEFAULT(0), 
  [BatAvg] decimal(5,2) NOT NULL DEFAULT(0),
  [BowlAvg] decimal(5,2) NOT NULL DEFAULT(0),
  [EconRate] decimal(5,2) NOT NULL DEFAULT(0),
  [StrikeRate] decimal(5,2) NOT NULL DEFAULT(0),
  [MoM] bit DEFAULT(0),
  PlayerIndicator varchar(5) NULL,
  [BattingPoints] int NULL DEFAULT(0),
  [BowlingPoints] int NULL DEFAULT(0),
  [FieldingPoints] int NULL DEFAULT(0),
  [Inning1Points] int NULL DEFAULT(0),
  [Inning2Points] int NULL DEFAULT(0),
  [Inning3Points] int NULL DEFAULT(0),
  [Inning4Points] int NULL DEFAULT(0),
  [TotalPoints] int NULL DEFAULT(0),
PRIMARY KEY CLUSTERED 
(
	[MatchPlayerStatsId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];


GO

CREATE TABLE [dbo].[PointRules](
	[PointRulesId] [int] IDENTITY(1,1) NOT NULL,
	[TournamentId] int NULL,
	[TournamentStage] varchar(10) NULL,
	[RunScored] int NULL,
	[FourBonus] int NULL,
	[SixBonus] int NULL,
	[HalfCenturyBonus] int NULL,
	[CenturyBonus] int NULL,
	[DismissalDuck] int NULL,
	[MinBall4SR] int NULL,
	[StrikeRateBelow50] int NULL,
	[StrikeRate50To60] int NULL,
	[StrikeRate60To70] int NULL,
	[StrikeRate110To150] int NULL,
	[StrikeRateUp150] int NULL,
	[WicketTaken] int NULL,
	[Wicket3UpBonus] int NULL,
	[Wicket5UpBonus] int NULL,
	[MaidenOver] int NULL,
	[Hattrick] int NULL,
	[MinOver4ER] int NULL,
	[EconomyBelow4] int NULL,
	[Economy4To5] int NULL,
	[Economy5To6] int NULL,
	[Economy9To11] int NULL,
	[EconomyUp11] int NULL,
	[Captain] int NULL,
	[ViceCaptain] int NULL,
	[CatchTaken] int NULL,
	[Stumping] int NULL,
	[RunOutDirect] int NULL,
	[RunOutThrower] int NULL,
	[RunOutCatcher] int NULL,
	[Nitro] int NULL,
	[MoM] int NULL,
	[ModifiedDateTime] datetime NULL,
PRIMARY KEY CLUSTERED 
(
	[PointRulesId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
)ON [PRIMARY];

GO

CREATE TABLE [dbo].[Match] (
  [MatchId] [int] IDENTITY(1,1) NOT NULL,
  [MatchNo] int NULL,
  [MatchType] varchar(30) NULL,--TEST, T20, ODI
  [Venue] varchar(50) NULL,
  [MatchStage] varchar(50) NULL, --league,quarters,semis, final, D
  [TournamentId] int NULL,
  [Team1] varchar(50) NOT NULL,
  [Team1Score] varchar(200) NULL,
  [Team1Extras] varchar(50) NULL,
  Team1RR decimal(5,2) NULL,
  [Team2] varchar(50) NOT NULL,
  [Team2Score] varchar(200) NULL,
  [Team2Extras] varchar(50) NULL,
  Team2RR decimal(5,2) NULL,
  [Inning1BattingTeam] varchar(150) NULL,
  [Inning2BattingTeam] varchar(150) NULL,
  [Winner] varchar(150) NULL DEFAULT(0),
  [Loser] varchar(150) NULL DEFAULT(0),
  [Draw] int NULL DEFAULT(0),
  [TossWinner] varchar(150) NULL,
  [TotalPoints] int  NULL DEFAULT(0),
  [MatchScheduledDate] date NULL,
  [MatchStatus] varchar(30) NULL,
  [MatchComplete] bit NULL,
  [MatchDate] datetime NULL,  
  [MatchScheduledTime] time NULL, 
  WeeklyActive bit NULL DEFAULT(0),
  MatchSummary varchar(500) NULL,
  ShowScore bit NULL DEFAULT(0),
  Weather varchar(30) NULL,
  MatchCity varchar(100) NULL,
PRIMARY KEY CLUSTERED 
(
	[MatchId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];

GO

CREATE TABLE [dbo].[APIDetails] (
  [APIDetailsId] [int] IDENTITY(1,1) NOT NULL,
  [MatchId] int NULL,
  [APIName] varchar(250) NULL,
  [APIKey] varchar(150) NULL,
  [UniqueId] varchar(150) NULL,
  [ResponsePayload] [varchar] (max) NULL,
  [ScorePayload] [varchar] (5000) NULL,
  [ModifyDateTime] datetime NULL, 
  RapidMatchId int NULL,
  RapidSeriesId int NULL,
PRIMARY KEY CLUSTERED 
(
	[APIDetailsId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];

GO

CREATE TABLE [dbo].[UserFeedback] (
  [UserFeedbackId] [int] IDENTITY(1,1) NOT NULL,
  [UserEmail] varchar(250) NULL,
  [UserName] varchar(100) NULL,
  [Messages] varchar(5000) NULL,
  [MessageAbout] varchar(100) NULL,
  [Status] varchar(30) NULL,
  [FeedbackDate] datetime NULL,   
  [Resolveddate] datetime NULL,
PRIMARY KEY CLUSTERED 
(
	[UserFeedbackId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];

GO

CREATE TABLE [dbo].[Audit] (
  [AuditId] [int] IDENTITY(1,1) NOT NULL,
  [EnvCode] [varchar](50) NULL,
  [ProductName] [varchar](50) NULL,
  [APIName] [varchar](500) NULL,
  [FileName] varchar(256) NULL,
  [LoginUser] [varchar](200) NULL,
  [HostName] [varchar](100) NULL,
  [TransactionId] [varchar](100) NULL,
  [TransactionType] [varchar](50) NULL,
  [Status][varchar](50) NULL,
  [Message][varchar](200) NULL,
  [Payload] [varchar] (max) NULL,
  [LogTimeStamp] [datetime] NULL,
  PRIMARY KEY CLUSTERED 
(
	[AuditId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];

GO

CREATE TABLE [dbo].[Exception] (
  [ExceptionId] [int] IDENTITY(1,1) NOT NULL,
  [EnvCode] [varchar](50) NULL,
  [ProductName] [varchar](50) NULL,
  [APIName] [varchar](500) NULL,
  [FileName] varchar(256) NULL,
  [LoginUser] [varchar](200) NULL,
  [HostName] [varchar](100) NULL,
  [TransactionId] [varchar](100) NULL,
  [TransactionType] [varchar](50) NULL,
  [ErrorCode][varchar](50) NULL,
  [ErrorMessage][varchar](500) NULL,
  [StackTrace] [varchar](max) NULL,
  [Payload] [varchar] (max) NULL,
  [ExceptionTimeStamp] [datetime] NULL,
  PRIMARY KEY CLUSTERED 
(
	[ExceptionId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];

GO

CREATE TABLE [dbo].[CountryList] (
  [CountryId] [int] IDENTITY(1,1) NOT NULL,
  [Country] [varchar](100) NULL,
  [Active] [bit] NULL DEFAULT (1)
  PRIMARY KEY CLUSTERED 
(
	[CountryId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];

CREATE TABLE [dbo].[TeamSelectionRule] (
  [TeamSelectionRuleId] [int] IDENTITY(1,1) NOT NULL,
  [TournamentId] int NOT NULL,
  [WicketKeeper] [int] NULL,
  [MaxWicketKeeper] [int] NULL default(3),
  [MaxBatsman] [int] NULL,
  [MinBatsman] [int] NULL,
  [MaxBowler] [int] NULL,
  [MinBowler] [int] NULL,
  [MaxAllrounder] [int] NULL,
  [MinAllrounder] [int] NULL,
  [MaxSameTeamPlayer] int NULL,
  [MaxOverseasPlayer] int NULL,
  [TotalPlayers] [int] NULL,
  [TotalBudget] int NULL,
  [SubCount] int NULL,
  [NitroCount] int NULL,
  [PainKillerCount] int NULL,
  [AutoPilotCount] int NULL,
  PRIMARY KEY CLUSTERED 
(
	[TeamSelectionRuleId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];

GO


CREATE TABLE [dbo].[AutoSelectionTeam] (
  [AutoSelectionTeamId] [int] IDENTITY(1,1) NOT NULL,
  [TournamentId] int NULL,
  [Player1] int NULL,
  [Player2] int NULL,
  [Player3] int NULL,
  [Player4] int NULL,
  [Player5] int NULL,
  [Player6] int NULL,
  [Player7] int NULL,
  [Player8] int NULL,
  [Player9] int NULL,
  [Player10] int NULL,
  [Player11] int NULL,
  [TeamCapt] int NULL,
  [TeamVCapt] int NULL,
PRIMARY KEY CLUSTERED 
(
	[AutoSelectionTeamId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];

GO


CREATE TABLE [dbo].[StaticURLs] (
[StaticURLsId] [int] IDENTITY(1,1) NOT NULL,
[FAQs] [varchar](500) NULL,
[AboutUs] [varchar](500) NULL,
[HowtoPlay] [varchar](500) NULL,
[PrivacyNotice] [varchar](500) NULL,
[TnC] [varchar](500) NULL,
[PointRules] [varchar](500) NULL,
[TeamCompositionRules] [varchar](500) NULL,
PRIMARY KEY CLUSTERED 
(
	[StaticURLsId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];

GO




CREATE TABLE [dbo].[Notifications] (
[NotificationId] [int] IDENTITY(1,1) NOT NULL,
[UserId] int NULL,
Title varchar(50) NULL,
[Message] [varchar](500) NULL,
[MessageType] int NULL,-- 1. league joinee , 2. Prize won for Daily, 3. Referral joined, 4. Prize for Tournament, 5. Referral Reward calculated
[Active] bit NULL,
ReadActive bit NULL DEFAULT(1),
UpdateDate datetime NULL,
InsertDate datetime NULL,
PRIMARY KEY CLUSTERED 
(
	[NotificationId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];

GO

CREATE TABLE [dbo].[Reward] (
[RewardId] [int] IDENTITY(1,1) NOT NULL,
[RewardDate] date NULL,
[RewardType] varchar(30) NULL, -- Match Contest, Weekly, Monthly, Tournament, Referral
[UserId] int NULL,
[Active] bit NULL,
RewardAmount int NULL DEFAULT(0),
[Details] [varchar](500) NULL,
[Comments] [varchar](200) NULL,
UpdateDate datetime NULL,
PRIMARY KEY CLUSTERED 
(
	[RewardId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];

GO

CREATE TABLE [dbo].[Claim] (
[ClaimId] [int] IDENTITY(1,1) NOT NULL,
[ClaimDate] date NULL,
[UserId] int NULL,
ClaimAmount int NULL DEFAULT(0),
[Bundle] [varchar](200) NULL,
Vouchar varchar(200) NULL,
[Comments] [varchar](200) NULL,
UpdateDate datetime NULL,
PRIMARY KEY CLUSTERED 
(
	[ClaimId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];

GO

CREATE TABLE [dbo].[WalletInfo] (
[WalletInfoId] [int] IDENTITY(1,1) NOT NULL,
[UserId] int NOT NULL,
WalletAmount decimal(7,2) NULL DEFAULT(0),
[Comments] [varchar](500) NULL,
UpdateDate datetime NULL,
PRIMARY KEY CLUSTERED 
(
	[WalletInfoId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];



GO

CREATE TABLE [dbo].[TransactionDetails] (
[TransactionDetailsId] [int] IDENTITY(1,1) NOT NULL,
UserId int NULL,
TransactionDate datetime NULL,
TransactionType [varchar](50) NULL,
Amount decimal(7,2) NULL DEFAULT(0),
status [varchar](50) NULL,
utr varchar(100) NULL,
reference_id [varchar](50) NULL,
Reason [varchar](500) NULL,
Currency [varchar](50) NULL,
PRIMARY KEY CLUSTERED 
(
	[TransactionDetailsId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];

GO

CREATE TABLE [dbo].[FunFact] (
[FunFactId] [int] IDENTITY(1,1) NOT NULL,
FunMessage varchar(200) NULL,
Active bit default(1),
PRIMARY KEY CLUSTERED 
(
	[FunFactId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];

GO

CREATE TABLE [dbo].[ClubEmailList] (
[ClubEmailListId] [int] IDENTITY(1,1) NOT NULL,
ClubEmail varchar(300) NULL,
PRIMARY KEY CLUSTERED 
(
	[ClubEmailListId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];

GO

INSERT INTO [FANTASYCRICKET].[dbo].[UserRole]
(UserRoleId,UserRole,UserRoleDesc)VALUES(1,'admin','Admin User of the apps')
INSERT INTO [FANTASYCRICKET].[dbo].[UserRole]
(UserRoleId,UserRole,UserRoleDesc)VALUES(2,'regular','Regular user for playing fantasy')

INSERT INTO CountryList(Country)VALUES('INDIA');
INSERT INTO CountryList(Country)VALUES('PAKISTAN');
INSERT INTO CountryList(Country)VALUES('BANGALADESH');
INSERT INTO CountryList(Country)VALUES('WEST INDIES');
INSERT INTO CountryList(Country)VALUES('SRI LANKA');
INSERT INTO CountryList(Country)VALUES('AFGHANISTAN');
INSERT INTO CountryList(Country)VALUES('ENGLAND');
INSERT INTO CountryList(Country)VALUES('AUSTRALIA');
INSERT INTO CountryList(Country)VALUES('NEW ZEALAND');
INSERT INTO CountryList(Country)VALUES('GERMANY');
INSERT INTO CountryList(Country)VALUES('NETHERLAND');
INSERT INTO CountryList(Country)VALUES('SCOTLAND');
INSERT INTO CountryList(Country)VALUES('NEPAL');
INSERT INTO CountryList(Country)VALUES('USA');
INSERT INTO CountryList(Country)VALUES('CANADA');
INSERT INTO CountryList(Country)VALUES('OTHER');

insert into StaticURLs(FAQs,AboutUs,HowtoPlay,PrivacyNotice,TnC,PointRules,TeamCompositionRules)
VALUES(
'https://www.fanzania.com/Home/FAQ_Online',
'https://www.fanzania.com/Home/About_Online',
'https://www.fanzania.com/Home/HowToPlay_Online',
'https://www.fanzania.com/Home/PrivacyPolicy',
'https://www.fanzania.com/Home/TermsCondition',
'https://www.fanzania.com/Home/PointScoring_Online',
'https://www.fanzania.com/Home/TeamComposition_Online'
)


