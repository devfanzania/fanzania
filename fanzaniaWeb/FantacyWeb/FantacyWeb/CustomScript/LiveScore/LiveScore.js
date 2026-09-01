
var TournementList = new Array();
var CurrentSelectTournamentNo = '';
var MatchList = new Array();
var CurrentSelectMatchNo = '';

function LiveScoreClick() {
    var Mid = $('#MatchId').val();
    var Tid = $('#TournamemtId').val();
    var Team1 = $('#Team1').val();
    var Team2 = $('#Team2').val();
    gridTeam1.reload({ page: 1, MatchId: Mid, TournamentId: Tid, Team: Team1 });
    gridTeam2.reload({ page: 1, MatchId: Mid, TournamentId: Tid, Team: Team2 });
}

function TrackLeagueClick() {
    var Mid = $('#MatchId').val();
    var Tid = $('#TournamemtId').val();
    ShowLiveLeague(Tid)
}

function GotoBackLeague() {
   
}

function GotoBackLeagueUser() {
    
}

function ScoreRefreshClick() {
    var Mid = $('#MatchId').val();
    var Tid = $('#TournamemtId').val();
    var UTId = $('#UserTeamId').val();
    var Uid = $('#UserId').val();
    gridLiveScore.reload({ page: 1, TournamentId: Tid, MatchId: Mid, UserTeamId: UTId, UserId: Uid });
}

function LoadTournament() {
    var uurl = '/LiveScore/InprogressTournament';
    $.ajax({
        url: uurl,
        type: 'POST',
        dataType: 'json',
        async: true,
        cache: false,
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            if (data.length > 0) {
                var defaultindex;
                var divs = '';
                var cnt = 0; var flag = 0; var hasTournament = false; var activeclass = ''; var itemCnt = 0;
                $.each(data, function (index, value) {
                    TournementList[itemCnt] = new Array(itemCnt, value.TournamentId, value.TournamentName, value.TournamentStatus);
                    itemCnt = itemCnt + 1;
                    hasTournament = true;
                    var cBox = '';
                    if (index == 0) {
                        defaultindex = value.TournamentId;
                        MatchDetails(defaultindex);
                        activeclass = 'active';
                        CurrentSelectTournamentNo = itemCnt;
                    }
                    else { activeclass = ''; CurrentSelectTournamentNo = itemCnt; }
                    cBox += '<div class="item ' + activeclass + '"><div class="slide"><h1>' + value.TournamentName + '</h1><h3>' + value.TournamentStartDate + ' - ' + value.TournamentEndDate + '</h3></div></div>';
                    divs += cBox;
                });
                $('#TournamentBox').html(divs);
                $('#hdnTourCnt').val(itemCnt);
                $('#spinnerContainer').hide();
            }
            else {
                location.href = '/Dashboard/Index';
            }
            if (itemCnt == 1) {
                $('#TournamentSlide_prev').hide();
                $('#TournamentSlide_next').hide();
            }
            else {
                $('#TournamentSlide_prev').show();
                $('#TournamentSlide_next').show();
            }
        },
        error: function (req, status, error) {
            toastr_warning("Opps! something went wrong. Try reload this page.");
            return false;
        }
    });
}

function OnChangeTournamentSlide(type) {
    if (TournementList.length == CurrentSelectTournamentNo && type == 'next' || CurrentSelectTournamentNo == 1 && type == 'prev') {
    }
    else {
        if (type == 'prev') {
            CurrentSelectTournamentNo = CurrentSelectTournamentNo - 1;
        } else {
            CurrentSelectTournamentNo = CurrentSelectTournamentNo + 1;
        }
        var tourId = TournementList[CurrentSelectTournamentNo - 1][1];
        var tourName = TournementList[CurrentSelectTournamentNo - 1][2];
        var tourStatus = TournementList[CurrentSelectTournamentNo - 1][3];
        MatchDetails(tourId);
    }
}

function OnChangeMatchSlide(type) {
    if (MatchList.length == CurrentSelectMatchNo && type == 'next' || CurrentSelectMatchNo == 1 && type == 'prev') {
    }
    else {
        if (type == 'prev') {
            CurrentSelectMatchNo = CurrentSelectMatchNo - 1;
        } else {
            CurrentSelectMatchNo = CurrentSelectMatchNo + 1;
        }
    }
    var matchId = MatchList[CurrentSelectMatchNo - 1][1];
    var tournamentId = MatchList[CurrentSelectMatchNo - 1][2];
    var team1 = MatchList[CurrentSelectMatchNo - 1][3];
    var team2 = MatchList[CurrentSelectMatchNo - 1][4];
    $('#MatchId').val(matchId);
    $('#MatchVenue').val(MatchList[CurrentSelectMatchNo - 1][5]);
    $('#MatchDate').val(MatchList[CurrentSelectMatchNo - 1][6]);
    $('#T1').val(MatchList[CurrentSelectMatchNo - 1][7]);
    $('#T2').val(MatchList[CurrentSelectMatchNo - 1][8]);
    $('#T1Score').val(MatchList[CurrentSelectMatchNo - 1][11]);
    $('#T2Score').val(MatchList[CurrentSelectMatchNo - 1][12]);
    $('#T1Img').val(MatchList[CurrentSelectMatchNo - 1][9]);
    $('#T2Img').val(MatchList[CurrentSelectMatchNo - 1][10]);

    $('#t1s').html('<img class="imgLeft" src="https://www.fanzania.com/FantasyCricketRest/ImagePath/image/cricket/teams/' + MatchList[CurrentSelectMatchNo - 1][9] + '" alt=""/><span>' + (MatchList[CurrentSelectMatchNo - 1][11]).replace("~", " ").replace(" ~", " ").replace("~ ", " ") + '</span>');
    $('#t2s').html('<img class="imgLeft" src="https://www.fanzania.com/FantasyCricketRest/ImagePath/image/cricket/teams/' + MatchList[CurrentSelectMatchNo - 1][10] + '" alt=""/><span>' + (MatchList[CurrentSelectMatchNo - 1][12]).replace("~", " ").replace(" ~", " ").replace("~ ", " ") + '</span>');


    GetDetailInfo(matchId, tournamentId, team1, team2);
}

function LoadTourInfo(Tid, itemNo) {
    if (itemNo != 0) {
        var tourcnt = $('#hdnTourCnt').val();
        var i = 1;
        for (i = 1; i <= tourcnt; i++) {
            $('#item_' + i).removeClass('activeSelect');
        }
        $('#item_' + itemNo).addClass('activeSelect');
    }
    MatchDetails(Tid);
}

function MatchDetails(Tid) {

    var uurl = '/LiveScore/ShowMatch';
    var pData = { TournamentId: Tid };
    var jsonData = JSON.stringify(pData);
    $.ajax({
        url: uurl,
        type: 'POST',
        data: jsonData,
        dataType: 'json',
        async: true,
        cache: false,
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            if (data.length > 0) {
            $('#matchlist').show();
            $('#tableList').show();
            $('#blankMsg').hide();
            var defaultindex;
            var divs = '';
            var cnt = 0; var flag = 0; var hasTournament = false; var itemCnt = 0; 
            var slideId = 1;
                $.each(data, function (index, value) {
                    localStorage.setItem("LiveTeamshortname1", value.Team1ShortName);
                    localStorage.setItem("LiveTeamshortname2", value.Team2ShortName);
                    localStorage.setItem("LiveMatchNo", value.MatchNo);
                //MatchList[itemCnt] = new Array(itemCnt, value.MatchId, value.TournamentId, value.Team1, value.Team2);
                MatchList[itemCnt] = new Array(itemCnt, value.MatchId, value.TournamentId, value.Team1, value.Team2, value.Venue, value.MatchDate, value.Team1ShortName, value.Team2ShortName, value.Team1Image, value.Team2Image, value.Team1Score, value.Team2Score);
                itemCnt = itemCnt + 1;
                hasTournament = true; var cBox = '';
                    if (index == 0) {
                        GetDetailInfo(value.MatchId, value.TournamentId, value.Team1, value.Team2);
                        CurrentSelectMatchNo = itemCnt;
                        cBox += '<div class="item active">'
                        if (value.ShowScore == false) {
                            $('.a_liveScore').hide();
                        } else { $('.a_liveScore').show();}
                        $('#MatchId').val(value.MatchId);
                        $('#MatchVenue').val(value.Venue);
                        $('#MatchDate').val(value.MatchDate);
                        $('#t1s').html('<img class="imgLeft" src="https://www.fanzania.com/FantasyCricketRest/ImagePath/image/cricket/teams/' + value.Team1Image + '" alt=""/><span>' + (value.Team1Score).replace("~", " ").replace(" ~", " ").replace("~ ", " ") + '</span>');
                        $('#t2s').html('<img class="imgLeft" src="https://www.fanzania.com/FantasyCricketRest/ImagePath/image/cricket/teams/' + value.Team2Image + '" alt=""/><span>' + (value.Team2Score).replace("~", " ").replace(" ~", " ").replace("~ ", " ") + '</span>');
                        $('#T1').val(value.Team1ShortName);
                        $('#T2').val(value.Team2ShortName);
                        $('#T1Img').val(value.Team1Image);
                        $('#T2Img').val(value.Team2Image);
                    }
                    else {
                        cBox += '<div class="item">';
                        CurrentSelectMatchNo = itemCnt;
                    }
                    cBox += '<div class="col-md-12"><a href="#1">'
                    cBox += '<div id="match_' + itemCnt + '" class="itemLiveMatch"><div  class="category-icon-item"><div class="icon-box">'
                    cBox += '<div class="matchFullscreen"><span>' + value.Team1ShortName + ' <img class="imgLeft" src="https://www.fanzania.com/FantasyCricketRest/ImagePath/image/cricket/teams/' + value.Team1Image + '" alt=""  height="20px" width="40px"/></span> v <span><img class="imgRight" src="https://www.fanzania.com/FantasyCricketRest/ImagePath/image/cricket/teams/' + value.Team2Image + '" alt="" height="20px" width="40px"/> ' + value.Team2ShortName + '</span></div>'
                cBox += '<div class="matchSmallscreen"><span>' + value.Team1ShortName + ' <img class="imgLeft" src="https://www.fanzania.com/FantasyCricketRest/ImagePath/image/cricket/teams/' + value.Team1Image + '" alt=""  height="20px" width="40px"/></span> v <span><img class="imgRight" src="https://www.fanzania.com/FantasyCricketRest/ImagePath/image/cricket/teams/' + value.Team2Image + '" alt="" height="20px" width="40px"/> ' + value.Team2ShortName + '</span></div>'
                    cBox += '</div></div></div></a></div></div>'
                    cnt = cnt + 1;
                divs += cBox;
            });
            $('#MatchDetails').html(divs);
        }
        else {
            $('#matchlist').hide();
            $('#tableList').hide();
            $('#blankMsg').show();
        }
        },
        error: function (req, status, error) {
            toastr_warning("Opps! something went wrong. Try reload this page.");
            return false;
        }
    });
}

function GetDetailInfo(Mid, Tid, Team1, Team2) {
     
    $('#MatchId').val(Mid);
    $('#TournamemtId').val(Tid);
    $('#Team1').val(Team1);
    $('#Team2').val(Team2);
    $('#team1label').html(Team1);
    $('#team2label').html(Team2);
    GotoBackLeague();
    if (gridTeam1 != null) {
        gridTeam1.reload({ page: 1, MatchId: Mid, TournamentId: Tid, Team: Team1 });
    } else {
        ShowTeam1(Mid, Tid, Team1);
    }
    if (gridTeam2 != null) {
        gridTeam2.reload({ page: 1, MatchId: Mid, TournamentId: Tid, Team: Team2 });
    } else {
        ShowTeam2(Mid, Tid, Team2);
    }
     ShowLiveLeague(Tid);
}

var gridTeam1;
function ShowTeam1(Mid, Tid, Team) {
     
    gridTeam1 = $("#team1").grid({
        dataSource: { url: '/LiveScore/ShowMatch_Score', data: { MatchId: Mid, TournamentId: Tid, Team: Team } },
        notFoundText: "No players found!",
        responsive: true,
        uiLibrary: "bootstrap",
        fixedHeader: true,
        height: 350,
        minWidth: 500,
        columns: [
            { field: "", title: "Type", width: "10%", align: "center", cssClass: "PlayerSpeciality" },
            { field: "", title: "Player", width: "28%", align: "left", cssClass: "PlayerName" },
            { field: "", title: "", width: "10%", align: "center", cssClass: "PlayerType" },
            { field: "BattingPoints", title: "Batting", width: "13%", align: "center", cssClass: "BattingPoints" },
            { field: "BowlingPoints", title: "Bowling", width: "13%", align: "center", cssClass: "BowlingPoints" },
            { field: "FieldingPoints", title: 'Fielding', width: "13%", align: "center", cssClass: "FieldingPoints", type: 'int' },
            { field: "TotalPoints", title: "Total", width: "13%", align: "center", cssClass: "TotalPoints" }
        ],
    });
    gridTeam1.on("rowDataBound", function (e, $row, id, record) {
        if (id == 1) {
            $('#thisMatch').html(record.CurrentMatchPoints);
            $('#liveTotal').html(record.AllTotalPoints);
            //$('#teamname').html(record.UserTeamName);
            var powerPlay = '';
            if (record.PowerPlay == 'NITRO') {
                powerPlay = 'You are on ' + record.PowerPlay + ' <img class="ppImg" src="/Assets/Icon/nitro.png"/>';
            } else if (record.PowerPlay == 'AUTOCAPTAIN') {
                powerPlay = 'You are on ' + record.PowerPlay + ' <img class="ppImg" src="/Assets/Icon/Autocaptain.png"/>';
            } else if (record.PowerPlay == 'PAINKILLER') {
                powerPlay = 'You are on ' + record.PowerPlay + ' <img class="ppImg" src="/Assets/Icon/Painkiller.png"/>';
            } else {
                powerPlay = '';
            }
            //$('#powerplay').html(powerPlay);
        }
        if (record.PlayerSelected == 'True')
        {
            $row.css('background-color', '#e2dada'); $row.css('color', '#000');
        }
        else {
            $row.css('background-color', '#fff'); $row.css('color', '#000');
        }
        if (record.Capt == 'True') {
            $row.find('.PlayerName').html(record.PlayerName + ' <img src="/Assets/Icon/Captain.png" height="20px" width="18px"/>');
        } else if (record.VCapt == 'True') {
            $row.find('.PlayerName').html(record.PlayerName + ' <img src="/Assets/Icon/ViceCaptain.png" height="20px" width="18px"/>');
        } else {
            $row.find('.PlayerName').html(record.PlayerName);
        }
        if (record.PlayerType == 'overseas') {
            $row.find('.PlayerType').html('<img src="/Assets/Icon/Overseas.png" alt="" height="18px" width="18px"/>');
        }
        //else {
        //    $row.find('.PlayerType').html('<img src="" alt="" height="18px" width="18px"/>');
        //}
        
        if (record.PlayerSpeciality == 'batsman') {
            $row.find('.PlayerSpeciality').html('<img src="/Assets/Icon/BatsmanBlackCircle.png" alt="" height="20px" width="20px"/>');
        }
        else if (record.PlayerSpeciality == 'wicketkeeper') {
            $row.find('.PlayerSpeciality').html('<img src="/Assets/Icon/WicketkeeperBlackCircle.png" alt="" height="20px" width="20px"/>');
        }
        else if (record.PlayerSpeciality == 'allrounder') {
            $row.find('.PlayerSpeciality').html('<img src="/Assets/Icon/AllrounderBlackCircle.png" alt="" height="20px" width="20px"/>');
        }
        else if (record.PlayerSpeciality == 'bowler') {
            $row.find('.PlayerSpeciality').html('<img src="/Assets/Icon/BowlerBlackCircle.png" alt="" height="20px" width="20px"/>');
        }
    });

}

var gridTeam2;
function ShowTeam2(Mid, Tid, Team) {

    gridTeam2 = $("#team2").grid({
        dataSource: { url: '/LiveScore/ShowMatch_Score', data: { MatchId: Mid, TournamentId: Tid, Team: Team } },
        notFoundText: "No players found!",
        responsive: true,
        uiLibrary: "bootstrap",
        fixedHeader: true,
        height: 350,
        minWidth: 500,
        columns: [
            { field: "", title: "Type", width: "10%", align: "center", cssClass: "PlayerSpeciality" },
            { field: "", title: "Player", width: "28%", align: "left", cssClass: "PlayerName" },
            { field: "", title: "", width: "10%", align: "center", cssClass: "PlayerType" },
            { field: "BattingPoints", title: "Batting", width: "13%", align: "center", cssClass: "BattingPoints" },
            { field: "BowlingPoints", title: "Bowling", width: "13%", align: "center", cssClass: "BowlingPoints" },
            { field: "FieldingPoints", title: 'Fielding', width: "13%", align: "center", cssClass: "FieldingPoints", type: 'int' },
            { field: "TotalPoints", title: "Total", width: "13%", align: "center", cssClass: "TotalPoints" }

        ],
    });
    gridTeam2.on("rowDataBound", function (e, $row, id, record) {
        if (record.PlayerSelected == 'True') {
            $row.css('background-color', '#e2dada'); $row.css('color', '#000');
        }
        else {
            $row.css('background-color', '#fff'); $row.css('color', '#000');
        }
        if (record.Capt == 'True')
        {
            $row.find('.PlayerName').html(record.PlayerName + ' <img src="/Assets/Icon/Captain.png" height="20px" width="18px"/>');
        } else if (record.VCapt == 'True')
        {
            $row.find('.PlayerName').html(record.PlayerName + ' <img src="/Assets/Icon/ViceCaptain.png" height="20px" width="18px"/>');
        } else {
            $row.find('.PlayerName').html(record.PlayerName);
        }
        if (record.PlayerType == 'overseas') {
            $row.find('.PlayerType').html('<img src="/Assets/Icon/Overseas.png" alt="" height="18px" width="18px"/>');
        }
        //else
        //{
        //    $row.find('.PlayerType').html('<img src="" alt="" height="18px" width="18px"/>');
        //}
        if (record.PlayerSpeciality == 'batsman') {
            $row.find('.PlayerSpeciality').html('<img src="/Assets/Icon/BatsmanBlackCircle.png" alt="" height="20px" width="20px"/>');
        }
        else if (record.PlayerSpeciality == 'wicketkeeper') {
            $row.find('.PlayerSpeciality').html('<img src="/Assets/Icon/WicketkeeperBlackCircle.png" alt="" height="20px" width="20px"/>');
        }
        else if (record.PlayerSpeciality == 'allrounder') {
            $row.find('.PlayerSpeciality').html('<img src="/Assets/Icon/AllrounderBlackCircle.png" alt="" height="20px" width="20px"/>');
        }
        else if (record.PlayerSpeciality == 'bowler') {
            $row.find('.PlayerSpeciality').html('<img src="/Assets/Icon/BowlerBlackCircle.png" alt="" height="20px" width="20px"/>');
        }
    });

}


function ShowLiveLeague(Tid) {
    
    $('#spinnerContainer').show();
  
    var uurl = '/LiveScore/LiveLeague';
    var pData = { TournamentId: Tid };
    var jsonData = JSON.stringify(pData);
    $.ajax({
        url: uurl,
        type: 'POST',
        data: jsonData,
        dataType: 'json',
        async: true,
        cache: false,
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            var divs = '';
            var activeclass = ''; var itemCnt = 0;
            var MId = $('#MatchId').val();
                $.each(data, function (index, value) {
                    itemCnt = itemCnt + 1;
                    var cBox = '';
                    if (index == 0) {
                        GotoLiveLeagueUser(value.LeagueId, value.TournamentId, MId, itemCnt);
                        activeclass = 'active';
                    } else { activeclass = ''; }
                    cBox += '<li id="Litem_' + itemCnt + '" class=" livematch-league ' + activeclass + '"><span href="#" onclick="GotoLiveLeagueUser(\'' + value.LeagueId + '\',\'' + value.TournamentId + '\',\'' + MId + '\',\'' + itemCnt + '\')">' + value.LeagueName + '</span></li>';
                    divs += cBox;
                });
                $('#hdnLeagueCnt').val(itemCnt);

            $('#UserLeagueBox').html(divs);
            $('#spinnerContainer').hide();
        },
        error: function (req, status, error) {
            $('#spinnerContainer').hide();
            toastr_warning("Oops. We are unable to connect to our servers. Please try again.11!");
            return false;
        }
    });
}

var gridLiveleagueUser;
function GotoLiveLeagueUser(LId, TId, MId, itemNo) {
    if (itemNo != 0) {
        var leaguecnt = $('#hdnLeagueCnt').val();
        var i = 1;
        for (i = 1; i <= leaguecnt; i++) {
            $('#Litem_' + i).removeClass('active');
        }
        $('#Litem_' + itemNo).addClass('active');
    }
    if (gridLiveleagueUser != null) {
        gridLiveleagueUser.reload({ page: 1, TournamentId: TId, MatchId: MId, LeagueId: LId });
    } else {
        gridLiveleagueUser = $("#gridLiveleagueUser").grid({
            dataSource: { url: '/LiveScore/LiveLeagueUser', data: { TournamentId: TId, MatchId: MId, LeagueId: LId } },
            notFoundText: "No User found!",
            responsive: true,
            uiLibrary: "bootstrap",
            fixedHeader: true,
            height: 350,
            minWidth: 500,
            columns: [
                { field: "", title: "Rank", width: "8%", align: "center", cssClass: "TeamNewStanding" },
                { field: "", title: "Team", width: "10%", align: "left", cssClass: "UserTeamName" },
                { field: "", title: "", width: "7%", align: "center", cssClass: "PP" },
               
                { field: "Transfers", title: "Transfers", width: "15%", align: "center", cssClass: "Transfers" },
                { field: "TransferUsed", title: "Match Transfers", width: "16%", align: "center", cssClass: "TransferUsed" },
                { field: "TotalPoints", title: "Live Total", width: "15%", align: "center", cssClass: "LiveTotal" },
                { field: "CurrentMatchPoints", title: "This Match", width: "15%", align: "center", cssClass: "ThisMatch" },
                { field: "", title: "", width: "4%", align: "center", cssClass: "Action" }
            ],
        });
        gridLiveleagueUser.on("rowDataBound", function (e, $row, id, record) {
           // console.log(id );
           // return;
            //TransfersUsed
           
            if (record.UserId == Session_usedId) {
                localStorage.setItem("myTeamId", record.UserTeamId);
                localStorage.setItem("myTeamname", record.UserTeamName);
               // $row.css('background-color', '#e2dada'); $row.css('color', '#000');
                $row.css('background-color', '#fff'); $row.css('color', '#000');
            }
            else {
                $row.css('background-color', '#fff'); $row.css('color', '#000');
            }
            $row.find('.UserTeamName').html('<p class="OwnerTeamL">' + record.UserTeamName + '</p><p class="OwnerNameL">' + record.UserName + '</p>');
          

            if (record.PowerPlay == 'NITRO') {
                $row.find('.PP').html('<img src="/Assets/Icon/Nitro.png" height="15px" width="15px"/>');
            } else if (record.PowerPlay == 'AUTOCAPTAIN') {
                $row.find('.PP').html('<img src="/Assets/Icon/Autocaptain.png" height="15px" width="15px"/>');
            } else if (record.PowerPlay == 'PAINKILLER') {
                $row.find('.PP').html('<img src="/Assets/Icon/Painkiller.png" height="15px" width="15px"/>');
            } else {
                $row.find('.PP').html('');
            }
            if (parseInt(record.TeamNewStanding) == 0) {
                $row.find('.TeamNewStanding').html('<i class="fa fa-minus"></i>');
            }
            else if (parseInt(record.TeamNewStanding) < parseInt(record.TeamOldStanding) && parseInt(record.TeamOldStanding) > 0 && parseInt(record.TeamNewStanding) > 0) {
                $row.find('.TeamNewStanding').html('<i class="green-arrow fa fa-caret-up"></i><small>' + record.TeamNewStanding + '</small><small>(+' + (parseInt(record.TeamOldStanding) - parseInt(record.TeamNewStanding)) + ')</small>');
            }
            else if (parseInt(record.TeamNewStanding) > parseInt(record.TeamOldStanding) && parseInt(record.TeamOldStanding) > 0 && parseInt(record.TeamNewStanding) > 0) {
                $row.find('.TeamNewStanding').html('<i class="red-arrow fa fa-caret-down"></i><small>' + record.TeamNewStanding + '</small><small>(-' + (parseInt(record.TeamNewStanding) - parseInt(record.TeamOldStanding)) + ')</small>');
            }
            else {
                $row.find('.TeamNewStanding').html('<i class="grey-circle fa fa-circle"></i><small>' + record.TeamNewStanding + '</small><small></small>');
            }
            var MatchId = $('#MatchId').val();
            var TournamemtId = $('#TournamemtId').val();
            $row.find('.Action').html('<i class="fa fa-angle-right an-r" aria-hidden="true" title="View Live Score" onclick="GotoLiveScore(\'' + record.UserTeamId + '\',\'' + record.UserId + '\',\'' + TournamemtId + '\',\'' + MatchId + '\',\'' + record.UserTeamName + '\')";></i>');
            if (id == 1) {
                GotoLiveScore(record.UserTeamId, record.UserId, TournamemtId, MatchId, record.UserTeamName);
            }
        });
    }
}


var gridLiveScore;
function GotoLiveScore(UTId, Uid, TId, MId,TName) {
   // showcomparebtn(UTId, Uid, TId, MId);
    var btnshow = '';
    $('#UserTeamId').val(UTId);
    $('#UserId').val(Uid);
    var myteamID = localStorage.getItem("myTeamId");
   
   // btnshow = '<button type="button"  id="dpaybtn2" onclick="comapre(\'' + myteamID + '\',\'' + UTId + '\',\'' + TId + '\',\'' + MId + '\',\'' + TName + '\')"; class="btn-red btnteamcompare"> Team Compare</button>'
    btnshow = '<a href="#" class="btn3"  style="border-left: 0px solid #192D39 !important; margin: 0px 0px 0px 0!important; width: 198px !important ;float: right;" onclick="comapre(\'' + myteamID + '\',\'' + UTId + '\',\'' + TId + '\',\'' + MId + '\',\'' + TName + '\')";>Team Compare <img style="top: 6px!important;height: 35px;max-width: 45px!important;"src="/Assets/Icon/teamcompare.001.png"></a>'

    $('#showbtn').html(btnshow);
    if (Uid != Session_usedId) {
        $('#showbtn').show();
    } else {
        $('#showbtn').hide();
    }
    if (gridLiveScore != null) {
        gridLiveScore.reload({ page: 1, TournamentId: TId, MatchId: MId, UserTeamId: UTId, UserId: Uid });
    } else {
       
      
        gridLiveScore = $("#gridLiveScore").grid({
            dataSource: { url: '/LiveScore/LiveScore', data: { TournamentId: TId, MatchId: MId, UserTeamId: UTId, UserId: Uid } },
            notFoundText: "No players found!",
            responsive: true,
            uiLibrary: "bootstrap",
            fixedHeader: true,
            height: 350,
            minWidth: 500,
            columns: [
                { field: "", title: "Type", width: "10%", align: "center", cssClass: "PlayerSpeciality" },
                { field: "", title: "Player", width: "28%", align: "left", cssClass: "PlayerName" },
                { field: "", title: "", width: "10%", align: "center", cssClass: "PlayerType" },
                { field: "BattingPoints", title: "Batting", width: "13%", align: "center", cssClass: "BattingPoints" },
                { field: "BowlingPoints", title: "Bowling", width: "13%", align: "center", cssClass: "BowlingPoints" },
                { field: "FieldingPoints", title: 'Fielding', width: "13%", align: "center", cssClass: "FieldingPoints" },
                { field: "TotalPoints", title: "Total", width: "13%", align: "center", cssClass: "TotalPoints" }
               
            ],
        });
        gridLiveScore.on("rowDataBound", function (e, $row, id, record) {
            if (record.PlayerSelected == 'True') {
                $row.css('background-color', '#e2dada'); $row.css('color', '#000');
            }
            else {
                $row.css('background-color', '#fff'); $row.css('color', '#000');
            }
            if (record.Capt == 'True') {
                $row.find('.PlayerName').html(record.PlayerName + ' <img src="/Assets/Icon/Captain.png" height="20px" width="18px"/>');
            } else if (record.VCapt == 'True') {
                $row.find('.PlayerName').html(record.PlayerName + ' <img src="/Assets/Icon/ViceCaptain.png" height="20px" width="18px"/>');
            } else {
                $row.find('.PlayerName').html(record.PlayerName);
            }
            if (record.PlayerType == 'overseas') {
                $row.find('.PlayerType').html('<img src="/Assets/Icon/Overseas.png" alt="" height="18px" width="18px"/>');
            }
            else
            {
                $row.find('.PlayerType').html('');
            }
            if (record.PlayerSpeciality == 'batsman') {
                $row.find('.PlayerSpeciality').html('<img src="/Assets/Icon/BatsmanBlackCircle.png" alt="" height="20px" width="20px"/>');
            }
            else if (record.PlayerSpeciality == 'wicketkeeper') {
                $row.find('.PlayerSpeciality').html('<img src="/Assets/Icon/WicketkeeperBlackCircle.png" alt="" height="20px" width="20px"/>');
            }
            else if (record.PlayerSpeciality == 'allrounder') {
                $row.find('.PlayerSpeciality').html('<img src="/Assets/Icon/AllrounderBlackCircle.png" alt="" height="20px" width="20px"/>');
            }
            else if (record.PlayerSpeciality == 'bowler') {
                $row.find('.PlayerSpeciality').html('<img src="/Assets/Icon/BowlerBlackCircle.png" alt="" height="20px" width="20px"/>');
            }
        });
    }
   
    
}

function ChageDatFormat(date) {
    var arr = date.split('-');
    var months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
    var i = 1;
    var mon = '';
    for (i; i <= months.length; i++) {
        if (i == parseInt(arr[1])) {
            mon = months[i];
            break;
        }
    }
    var formatddate = arr[2] + ' ' + mon + ' ' + arr[0];
    return formatddate;
}

function OpenScore() {
    
    var pData = {
        MatchId: $('#MatchId').val(),
        T1Img: $('#T1Img').val(),
        T2Img: $('#T2Img').val(),
        T1: $('#T1').val(),
        T2: $('#T2').val(),
        MatchDate: $('#MatchDate').val(),
        Venue: $('#MatchVenue').val()
    };
    var jsonData = JSON.stringify(pData);
    sessionStorage.setItem('ScoreBoardData', jsonData);
   // window.location.href = '/LiveScore/ScoreBoard';
  
    window.open(
        '/LiveScore/ScoreBoard',
        '_blank' // <- This is what makes it open in a new window.
    );

    //$('#T1_p').attr("src", "https://www.fanzania.com/FantasyCricketRest/ImagePath/image/cricket/teams/" + $('#T1Img').val());
    //$('#T2_p').attr("src", "https://www.fanzania.com/FantasyCricketRest/ImagePath/image/cricket/teams/" + $('#T2Img').val());

    //$('#matchBtw').html($('#T1').val() + ' v ' + $('#T2').val());
    //$('#mDate').html($('#MatchDate').val());
    //$('#mVenue').html($('#MatchVenue').val());
   
    //ScoreBoardInng1(1);
    //ScoreBoardInng2(2);
    //$('#ModalScore').modal('show');
}
var gridLiveScorec;
function comapre(MYTID, OTID, TID,MID,TName) {
   
    LiveMatchComparison(MYTID, OTID, TID, MID,TName);
    TeamPointsComparison(MYTID, OTID, TID, TName)
    var LiveTeamshortname1 = localStorage.getItem("LiveTeamshortname1");
    var LiveTeamshortname2 = localStorage.getItem("LiveTeamshortname2");
    var liveteams = LiveTeamshortname1 + " V " + LiveTeamshortname2;
    $('#liveteams').html(liveteams);
    var LiveMatchNo = localStorage.getItem("LiveMatchNo");
    $('#livematchno').html('M' + LiveMatchNo);
   // alert(localStorage.getItem("OtherPowerPlay"))
    var opowerPlayh = '';
    if (localStorage.getItem("OtherPowerPlay") == 'NITRO') {
        opowerPlayh = TName + '<img width="28px" class="titlesideimage" src="/Assets/Icon/Nitro_red.png">';
    } else if (localStorage.getItem("OtherPowerPlay") == 'AUTOCAPTAIN') {
        opowerPlayh = TName + '<img width="28px" class="titlesideimage" src="/Assets/Icon/Autocaptain.png">';
    } else if (localStorage.getItem("OtherPowerPlay") == 'PAINKILLER') {
        opowerPlayh = TName + '<img width="28px" class="titlesideimage" src="/Assets/Icon/Painkiller.png">';
    } else {
        opowerPlayh = TName;
    }
    $('#Modal_Teamname').html(opowerPlayh);
    $('#gmyteam').html(localStorage.getItem("myTeamname"));
    $('#goteam').html(TName);
    $('#MyMatchTotalPoints1').html(localStorage.getItem("MyMatchTotalPoints1"));
    $('#OtherMatchTotalPoints1').html(localStorage.getItem("OtherMatchTotalPoints1"));

 //   $('.behindpoints').html(localStorage.getItem("Matchtotalpointcheck"));

    //var contentg = '';
   // var totalpointg = Number(localStorage.getItem("MyMatchTotalPoints1")) - Number(localStorage.getItem("OtherMatchTotalPoints1"));
   // if (totalpointg > 0) {
    //    var contentg = '<p class="dec">You are AHEAD</span> <span style="font-weight: 600;color: green;">' + totalpointg + ' </span></p>';
    //} else if (totalpointg < 0) {
     //   var contentg = '<p class="dec">You are BEHIND</span> <span style="font-weight: 600;color: red;">' + totalpointg.toString().substring(1) + ' </span></p>';
  //  } else if (totalpointg == 0) {
      //  var contentg = '<p class="dec">Both Teams Are on SEAM Points</span> <span style="font-weight: 600;">' + totalpointg + ' </span></p>';
    //}
   // $('#contentg').html(contentg);
    
    $("#comparepopup").modal('show');

}
function LiveMatchComparison(MYTID, OTID, TID, MID, TName) {
    $('#gridlivematch').grid('destroy', true, true);
    $('#mytotalpoints').html('');
    $('#othertotalpoints').html('');
    $('#Matchtotalpointplus1').html('');
    $('#Matchtotalpointminus').html('');
   
    let MyTotalPoints1 = 0;
    let OtherTotalPoints1 = 0;
    let Matchtotalpointcheck = 0;
    var MyPnameRendere = function (value, record, $cell, $displayEl, id) {
        if (record.MyPlayerSelected == 'True') {
            $cell.css('background-color', '#eeeeee');
        }
        if(id == 1) {
            localStorage.setItem("MyPowerPlay", record.MyPowerPlay);
            localStorage.setItem("OtherPowerPlay", record.OtherPowerPlay);
        }
    }
    var myTeamname = localStorage.getItem("myTeamname");
   
    var powerPlay = '';
    if (localStorage.getItem("MyPowerPlay") == 'NITRO') {
        powerPlay = myTeamname + ' <img class="ppImg" src="/Assets/Icon/nitro.png" height="20px" width="20px"/>';
    } else if (localStorage.getItem("MyPowerPlay") == 'AUTOCAPTAIN') {
        powerPlay = myTeamname + ' <img class="ppImg" src="/Assets/Icon/Autocaptain.png" height="20px" width="20px"/>';
    } else if (localStorage.getItem("MyPowerPlay") == 'PAINKILLER') {
        powerPlay = myTeamname + ' <img class="ppImg" src="/Assets/Icon/Painkiller.png" height="20px" width="20px"/>';
    } else {
        powerPlay = myTeamname ;
    }
    var opowerPlay = '';
    if (localStorage.getItem("OtherPowerPlay") == 'NITRO') {
        opowerPlay = TName + ' <img class="ppImg" src="/Assets/Icon/nitro.png" height="20px" width="20px"/>';
    } else if (localStorage.getItem("OtherPowerPlay") == 'AUTOCAPTAIN') {
        opowerPlay = TName + ' <img class="ppImg" src="/Assets/Icon/Autocaptain.png" height="20px" width="20px"/>';
    } else if (localStorage.getItem("OtherPowerPlay") == 'PAINKILLER') {
        opowerPlay = TName + ' <img class="ppImg" src="/Assets/Icon/Painkiller.png" height="20px" width="20px"/>';
    } else {
        opowerPlay = TName;
    }
    var OPnameRendere = function (value, record, $cell, $displayEl) {
        if (record.OtherPlayerSelected == 'True') {
            $cell.css('background-color', '#eeeeee');
        }
    }
       gridLiveScorec = $("#gridlivematch").grid({
        dataSource: { url: '/LiveScore/LiveTeamScoreComparison', data: { MyTeamId: MYTID, TournamentId: TID, OtherTeamId: OTID, MatchId: MID } },
        notFoundText: "No players found!",
        responsive: true,
        uiLibrary: "bootstrap",
        fixedHeader: true,
        height: 380,
        minWidth: 500,
        bodyRowHeight: 6,
        columns: [
            {
                field: "MyPlayerName", title: powerPlay, width: "26%", align: "left", cssClass: "MyPlayerName", "renderer": MyPnameRendere
                //renderer: function (value, record) { return record.MyPlayerSelected == 'True' ? '<b>' + record.MyPlayerName + '</b>' : '<i>' + record.MyPlayerName+ '</i>'; }
            },
            { field: "MyTotalPoints", title: "", width: "16%", align: "center", cssClass: "MyTotalPoints", "renderer": MyPnameRendere },
            { field: "totapoints", title: "", width: "16%", align: "center", cssClass: "totapoints" },
            { field: "OtherTotalPoints", title: "", width: "16%", align: "center", cssClass: "OtherTotalPoints", "renderer": OPnameRendere },
            { field: "OtherPlayerName", width: "26%", title: opowerPlay, align: "right", cssClass: "OtherPlayerName", "renderer": OPnameRendere },
        ],
       });
    gridLiveScorec.on("rowDataBound", function (e, $row, id, record) {
        if (record.MyCapt == 'True') {
            $row.find('.MyPlayerName').html('<span  renderer="nameRenderer" >' + record.MyPlayerName + ' <img src="/Assets/Icon/Captain.png" height="20px" width="18px"/></span>');
        } else if (record.MyVCapt == 'True') {
            $row.find('.MyPlayerName').html(record.MyPlayerName + ' <img src="/Assets/Icon/ViceCaptain.png" height="20px" width="18px"/>');
        } else {
            $row.find('.MyPlayerName').html(record.MyPlayerName);
        }
        if (record.OtherCapt == 'True') {
            $row.find('.OtherPlayerName').html('<img src="/Assets/Icon/Captain.png" height="20px" width="18px"/> '+ record.OtherPlayerName );
        } else if (record.OtherVCapt == 'True') {
            $row.find('.OtherPlayerName').html('<img src="/Assets/Icon/ViceCaptain.png" height="20px" width="18px"/> '+ record.OtherPlayerName );
        } else {
            $row.find('.OtherPlayerName').html(record.OtherPlayerName);
        }
        if (record.MyPlayerSelected == 'True') {
            $row.find('.MyTotalPoints').html(record.MyTotalPoints);
        } else {
            record.MyTotalPoints = '';
            $row.find('.MyTotalPoints').html(record.MyTotalPoints);
        }
        if (record.OtherPlayerSelected == 'True') {
            $row.find('.OtherTotalPoints').html(record.OtherTotalPoints);
        } else {
            record.OtherTotalPoints = '';
            $row.find('.OtherTotalPoints').html(record.OtherTotalPoints);
        }
        
        var myPoint = record.MyTotalPoints;
        var oPoints = record.OtherTotalPoints;
        var total = Number(myPoint) - Number(oPoints);
        if (total > 0) {
            $row.find('.totapoints').html('<span style="color:green  ;font-weight:500"> +' + total + '</spna>');
        } else if (total < 0) {
            $row.find('.totapoints').html('<span style="color:red; font-weight:500"> ' + total + '</spna>');
        } else if (record.OtherPlayerSelected == 'True' || record.MyPlayerSelected == 'True') {
            $row.find('.totapoints').html('<span> '+ total +'</spna>');
        } else {
            $row.find('.totapoints').html('<span> - </spna>');
        }
        MyTotalPoints1 += Number(record.MyTotalPoints);
        OtherTotalPoints1 += Number(record.OtherTotalPoints);
        $('#mytotalpoints').html(MyTotalPoints1);
        $('#othertotalpoints').html(OtherTotalPoints1);
   //     console.log("Mytotal"+MyTotalPoints1);
      //  console.log("othertotal" + OtherTotalPoints1);
        console.log(record);
        $('#MyPrediction').html(record.MyPrediction);
        $('#OtherPrediction').html(record.OtherPrediction);
      
        if (id == 11) {
            Matchtotalpointcheck = Number(MyTotalPoints1) - Number(OtherTotalPoints1);
            if (Number(Matchtotalpointcheck) < 0) {
                //  alert("Minus" + localStorage.getItem("Matchtotalpointcheck"));
                $('#Matchtotalpointminus').html(Matchtotalpointcheck);
                localStorage.setItem("Matchtotalpointcheck", Matchtotalpointcheck);
            } else {
                //   alert("plus" + localStorage.getItem("Matchtotalpointcheck"));
                $('#Matchtotalpointplus1').html('+' + Matchtotalpointcheck);
                localStorage.setItem("Matchtotalpointcheck", Matchtotalpointcheck);

            }

            if (localStorage.getItem("Matchtotalpointcheck") > 0) {
                var content = '<p style="color: #000;" class="" id="">You are  <span style="font-weight: 600;">AHEAD OF ' + TName + ' BY <span class="" style="color: green;">' + localStorage.getItem("Matchtotalpointcheck") + '</span>  POINTS</span> in this match</p>';
            } else if (localStorage.getItem("Matchtotalpointcheck") < 0) {
                var content = '<p style="color: #000;" class="" id="">You are  <span style="font-weight: 600;">BEHIND  ' + TName + ' BY <span class=""style="color: red;" >' + localStorage.getItem("Matchtotalpointcheck").substring(1) + '</span>  POINTS</span> in this match</p>';
            } else if (localStorage.getItem("Matchtotalpointcheck") == 0) {
                var content = '<p style="color: #000;" class="" id="">You are  <span style="font-weight: 600;">SAME POINTS AS <span class="">' + TName + '</span> </span> in this match</p>';
            }
            $('#content').html(content);
        }
      
     //   console.log("match"+Matchtotalpointcheck);
    });
   
    
}
var lineChart;

function TeamPointsComparison(MyTid, OTID, TID, TName) {
    if (lineChart) lineChart.destroy();
    
    var uurl = '/LiveScore/TeamPointsComparison';
    var pData = { MyTeamId: MyTid, OtherTeamId: OTID, TournamentId: TID };
    var jsonData = JSON.stringify(pData);
    $.ajax({
        url: uurl,
        type: 'POST',
        data: jsonData,
        dataType: 'json',
        async: true,
        cache: false,
        contentType: "application/json; charset=utf-8",
        success: function (data) {
           
            //console.log(data);
            const lastItem = data[data.length - 1]
            $('#myteampoints').html(lastItem.MyMatchTotalPoints);
            $('#otherteampoints').html(lastItem.OtherMatchTotalPoints);
            $('#MyMatchTotalPoints1').html(lastItem.MyMatchTotalPoints);
            $('#OtherMatchTotalPoints1').html(lastItem.OtherMatchTotalPoints);
            localStorage.setItem("MyMatchTotalPoints1", lastItem.MyMatchTotalPoints);
            localStorage.setItem("OtherMatchTotalPoints1", lastItem.OtherMatchTotalPoints);
            
            var contentg = '';
            var totalpointg = Number(lastItem.MyMatchTotalPoints) - Number(lastItem.OtherMatchTotalPoints);
            if (totalpointg > 0) {
                var contentg = '<p class="dec">You are AHEAD by</span> <span style="font-weight: 600;color: green;">' + totalpointg + ' </span>ponts.</p>';
            } else if (totalpointg < 0) {
                var contentg = '<p class="dec">You are BEHIND by</span> <span style="font-weight: 600;color: red;">' + totalpointg.toString().substring(1) + ' </span> points.</p>';
            } else if (totalpointg == 0) {
                var contentg = '<p class="dec">Both Teams Are on SAME Points</span> <span style="font-weight: 600;">' + totalpointg + ' </span></p>';
            }


            $('#contentg').html(contentg);
          
            if (data.length > 0) {
                var MatchNo = [' '];
                var MyMatchTotalPoints = [0];
                var OtherMatchTotalPoints = [0];
            
                $.each(data, function (index, value) {
                    MatchNo.push('M'+value.MatchNo);
                    MyMatchTotalPoints.push(value.MyMatchTotalPoints);
                    OtherMatchTotalPoints.push(value.OtherMatchTotalPoints);
                });
               // console.log(OtherMatchTotalPoints);
                var speedCanvas = document.getElementById("speedChart");
                Chart.defaults.global.defaultFontFamily = "Montserrat";
                Chart.defaults.global.defaultFontSize = 12;
                Chart.defaults.scale.gridLines.display = false;
                var dataFirst = {
                    label: localStorage.getItem("myTeamname"),
                    data: MyMatchTotalPoints,
                    lineTension: 0,
                    fill: false,
                    borderColor: '#f96767'
                };
                var dataSecond = {
                    label: $('#goteam').text(),
                    data: OtherMatchTotalPoints,
                    lineTension: 0,
                    fill: false,
                    borderColor: '#9932CC'
                };
                var speedData = {
                    labels: MatchNo,
                    datasets: [dataFirst, dataSecond]
                };
                var chartOptions = {
                    legend: {
                        display: true,
                        position: 'bottom',
                        labels: {
                            boxWidth: 30,
                            fontColor: 'black'
                        }
                    }
                };
                 lineChart = new Chart(speedCanvas, {
                    type: 'line',
                    data: speedData,
                    options: chartOptions
                });
               
            }
            else {
               
            }
        },
        error: function (req, status, error) {
            toastr_warning("Opps! something went wrong. Try reload this page.");
            return false;
        }
    });
  



}
