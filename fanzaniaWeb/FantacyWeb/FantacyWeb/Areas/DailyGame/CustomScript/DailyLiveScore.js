
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
    ShowLiveLeague(Tid,Mid)
}

function ScoreRefreshClick() {
    var Mid = $('#MatchId').val();
    var Tid = $('#TournamemtId').val();
    var UTId = $('#UserTeamId').val();
    var Uid = $('#UserId').val();
    gridLiveScore.reload({ page: 1, TournamentId: Tid, MatchId: Mid, UserTeamId: UTId, UserId: Uid });
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

function MatchDetails() {

    var uurl = '/DailyGame/LiveScoreD/GetDailyMyMatch';
    
    $.ajax({
        url: uurl,
        type: 'POST',
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
                MatchList[itemCnt] = new Array(itemCnt, value.MatchId, value.TournamentId, value.Team1, value.Team2, value.Venue, ChageDatFormat((value.MatchScheduledDate).split('T')[0]), value.Team1ShortName, value.Team2ShortName, value.Team1Image, value.Team2Image, value.Team1Score, value.Team2Score);
                itemCnt = itemCnt + 1;
                hasTournament = true; var cBox = '';
                    if (index == 0) {
                        GetDetailInfo(value.MatchId, value.TournamentId, value.Team1, value.Team2);
                        CurrentSelectMatchNo = itemCnt;
                        cBox += '<div class="item active">'
                        if (value.ShowScore == false) {
                            $('.a_liveScore').hide();
                        } else { $('.a_liveScore').show(); }
                        $('#t1s').html('<img class="imgLeft" src="https://www.fanzania.com/FantasyCricketRest/ImagePath/image/cricket/teams/' + value.Team1Image + '" alt=""/><span>' + (value.Team1Score).replace("~", " ").replace(" ~", " ").replace("~ ", " ") + '</span>');
                        $('#t2s').html('<img class="imgLeft" src="https://www.fanzania.com/FantasyCricketRest/ImagePath/image/cricket/teams/' + value.Team2Image + '" alt=""/><span>' + (value.Team2Score).replace("~", " ").replace(" ~", " ").replace("~ ", " ") + '</span>');
                        $('#MatchId').val(value.MatchId);
                        $('#MatchVenue').val(value.Venue);
                        $('#MatchDate').val(ChageDatFormat((value.MatchScheduledDate).split('T')[0]));
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
                    cBox += '<div class="matchSmallscreen"><span>' + value.Team2ShortName + ' <img class="imgLeft" src="https://www.fanzania.com/FantasyCricketRest/ImagePath/image/cricket/teams/' + value.Team1Image + '" alt=""  height="20px" width="40px"/></span> v <span><img class="imgRight" src="https://www.fanzania.com/FantasyCricketRest/ImagePath/image/cricket/teams/' + value.Team2Image + '" alt="" height="20px" width="40px"/> ' + value.Team2ShortName + '</span></div>'
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
    ShowLiveLeague(Tid, Mid);
}

var gridTeam1;
function ShowTeam1(Mid, Tid, Team) {
     
    gridTeam1 = $("#team1").grid({
        dataSource: { url: '/DailyGame/LiveScoreD/ShowMatch_Score', data: { MatchId: Mid, TournamentId: Tid, Team: Team } },
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
            
            $('#teamname').html(record.UserTeamName);
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
            $('#powerplay').html(powerPlay);
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
        dataSource: { url: '/DailyGame/LiveScoreD/ShowMatch_Score', data: { MatchId: Mid, TournamentId: Tid, Team: Team } },
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

var gridLiveleagueUser;
function ShowLiveLeague(TId, MId) {
   
    if (gridLiveleagueUser != null) {
        gridLiveleagueUser.reload({ page: 1, TournamentId: TId, MatchId: MId });
    } else {
        gridLiveleagueUser = $("#gridLiveleagueUser").grid({
            dataSource: { url: '/DailyGame/LiveScoreD/DailyLiveLeagueUsers', data: { TournamentId: TId, MatchId: MId} },
            notFoundText: "No User found!",
            responsive: true,
            uiLibrary: "bootstrap",
            fixedHeader: true,
            height: 350,
            minWidth: 500,
            columns: [
                { field: "TeamNewStanding", title: "Rank", width: "20%", align: "center", cssClass: "TeamNewStanding" },
                { field: "UserTeamName", title: "Team", width: "20%", align: "left", cssClass: "UserTeamName" },
                { field: "CurrentMatchPoints", title: "Match Point", width: "18%", align: "center", cssClass: "ThisMatch" },
                { field: "", title: "", width: "5%", align: "center", cssClass: "Action" }
            ],
        });
        gridLiveleagueUser.on("rowDataBound", function (e, $row, id, record) {
            if (record.UserId == Session_usedId) {
                $('#rank').html(record.TeamNewStanding);
                $('#tPoints').html(record.CurrentMatchPoints);
            }
            if (record.UserId == Session_usedId) {
                $row.css('background-color', '#e2dada'); $row.css('color', '#000');
            }
            else {
                $row.css('background-color', '#fff'); $row.css('color', '#000');
            }
            $row.find('.UserTeamName').html('<p class="OwnerTeamL">' + record.UserName + '</p>');
                        
            var MatchId = $('#MatchId').val();
            var TournamemtId = $('#TournamemtId').val();
            $row.find('.Action').html('<i class="fa fa-angle-right an-r" aria-hidden="true" title="View Live Score" onclick="GotoLiveScore(\'' + record.UserTeamId + '\',\'' + record.UserId + '\',\'' + TournamemtId + '\',\'' + MatchId + '\')";></i>');
            if (id == 1) {
                GotoLiveScore(record.UserTeamId, record.UserId, TournamemtId,  MatchId);
            }
        });
    }
}

var gridLiveScore;
function GotoLiveScore(UTId, Uid, TId, MId) {
    $('#UserTeamId').val(UTId);
    $('#UserId').val(Uid);
    if (gridLiveScore != null) {
        gridLiveScore.reload({ page: 1, TournamentId: TId, MatchId: MId, UserTeamId: UTId, UserId: Uid });
    } else {
        gridLiveScore = $("#gridLiveScore").grid({
            dataSource: { url: '/DailyGame/LiveScoreD/LiveScore', data: { TournamentId: TId, MatchId: MId, UserTeamId: UTId, UserId: Uid } },
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
            //if (record.PlayerSelected == 'True') {
            //    $row.css('background-color', '#e2dada'); $row.css('color', '#000');
            //}
            //else {
            //    $row.css('background-color', '#fff'); $row.css('color', '#000');
            //}
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
    for (i = 1; i <= months.length; i++) {
        if (i == parseInt(arr[1])) {
            mon = months[i - 1];
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

function ScoreBoardInng1(inngs) {
    var pData = { MatchId: $('#MatchId').val(), Inning: inngs };
    var jsonData = JSON.stringify(pData);
    $.ajax({
        url: '/DailyGame/LiveScoreD/LiveScoreBoard',
        type: 'POST',
        data: jsonData,
        dataType: 'json',
        async: true,
        cache: false,
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            if (data.length > 0) {
                var Bat = ''; var Bowl = ''; var extras = '0'; var RR = ''; var yetToBat = ''; var t1s = ''; var t2s = ''; var totalScore = '';
                $.each(data, function (index, value) {
                    if (index == 0) {
                        t1s = value.Team1Score;
                        t2s = value.Team2Score;

                        if (value.TeamSortName == $('#T1').val()) {
                            $('#team1tab').html($('#T1').val() + ' Innings');
                            $('#team2tab').html($('#T2').val() + ' Innings');
                            totalScore = (t1s.trim().split(' ')[1]) + ' ov ' + t1s.trim().split(' ')[3]

                        } else {
                            $('#team2tab').html($('#T1').val() + ' Innings');
                            $('#team1tab').html($('#T2').val() + ' Innings');
                            totalScore = (t2s.trim().split(' ')[1]) + ' ov ' + t2s.trim().split(' ')[3]
                        }

                        if (t1s == '') {
                            $('#Team1Score').html('To Bat');
                            $('#Team1over').html('...');
                        }
                        else {
                            $('#Team1Score').html(t1s.trim().split(' ')[1].replace("~ ", " "));
                            $('#Team1over').html(t1s.trim().split(' ')[3].replace("~ ", " ") + ' Ov');
                        }
                        if (t2s == '') {
                            $('#Team2Score').html('To Bat');
                            $('#Team2over').html('...');
                        } else {
                            $('#Team2Score').html(t2s.trim().split(' ')[1].replace("~ ", " "));
                            $('#Team2over').html(t2s.trim().split(' ')[3].replace("~ ", " ") + ' Ov');
                        }
                        $('#Team2Score').html();
                        $('#Team2over').html();
                        $('#matchHeadline').html(value.MatchSummary);
                        if (value.Team1Extras != null)
                        extras = value.Team1Extras;
                        RR = value.Team1RR;
                    }
                    if (value.InningDesc == 'Batting') {
                        if (value.BTdismissalinfo == null) {
                            if (yetToBat == '')
                                yetToBat += value.PlayerName;
                            else
                                yetToBat += ',' + value.PlayerName;
                        }
                        else {
                            Bat += '<tr><td class="td-desc">' + value.PlayerName;
                            if (value.PlayerIndicator == 'c') {
                                Bat += '(c)';
                            }
                            Bat += '<p>' + value.BTdismissalinfo + '</p></td>';
                            Bat += '<td class="td-run">' + value.BTrunScored + '</td>';
                            //totalRun += parseInt(value.BTrunScored);
                            Bat += '<td>' + value.BTballfaced + '</td>';
                            Bat += '<td>' + value.BTrun6s + '</td>';
                            Bat += '<td>' + value.BTrun4s + '</td>';
                            Bat += '<td>' + value.BTstrikerate + '</td></tr>';
                        }
                    }
                    if (value.InningDesc == 'Bowling') {
                        
                        Bowl += '<tr><td class="td-desc">' + value.PlayerName + '</td>';
                        Bowl += '<td>' + value.BLover + '</td>';
                        Bowl += '<td>' + value.BLmaiden + '</td>';
                        Bowl += '<td>' + value.BLrun + '</td>';
                        Bowl += '<td class="td-run">' + value.BLwicket + '</td>';
                        Bowl += '<td>' + value.BLecon + '</td></tr>';
                    }
                    
                });
                Bat += '<tr><td class="td-desc"><strong>Extras</strong></td><td class="td-extra" colspan="5">' + extras + '</td></tr>';
                Bat += '<tr><td class="td-desc"><strong>Total</strong></td><td colspan="2" class="td-run">' + totalScore + '</td><td>RR ' + RR + '</td></tr>';
                if (yetToBat != '')
                Bat += '<tr><td class="td-desc" colspan="6">Did not bat <span>' + yetToBat + '</span> </td></tr>';
                $('#score_batsman_inng1').html(Bat);
                $('#score_bowler_inng1').html(Bowl);
            }
            
        },
        error: function (req, status, error) {
            toastr_warning("Opps! something went wrong. Try reload this page.");
            return false;
        }
    });
}

function ScoreBoardInng2(inngs) {
    var pData = { MatchId: $('#MatchId').val(), Inning: inngs };
    var jsonData = JSON.stringify(pData);
    $.ajax({
        url: '/DailyGame/LiveScoreD/LiveScoreBoard',
        type: 'POST',
        data: jsonData,
        dataType: 'json',
        async: true,
        cache: false,
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            if (data.length > 0) {
                var Bat = ''; var Bowl = ''; var extras = '0'; var totalRun = 0; var RR = ''; var yetToBat = ''; var t1s = ''; var t2s = ''; var totalScore = '0'
                $.each(data, function (index, value) {
                    if (index == 0) {
                        $('#matchHeadline').html(value.MatchSummary);
                        
                        if(value.Team2Extras != null)
                        extras = value.Team2Extras;
                        RR = value.Team2RR;
                        t1s = value.Team1Score;
                        t2s = value.Team2Score;
                        if (value.TeamSortName == $('#T2').val()) {
                            if (t2s == '') {
                                totalScore = 0; RR = 0;
                            } else {
                                totalScore = (t2s.trim().split(' ')[1]) + ' ov ' + t2s.trim().split(' ')[3]
                            }
                        } else {
                            if (t1s != '')
                                totalScore = (t1s.trim().split(' ')[1]) + ' ov ' + t1s.trim().split(' ')[3]
                        }
                    }
                    if (value.InningDesc == 'Batting') {
                        if (value.BTdismissalinfo == null) {
                            if (yetToBat == '')
                                yetToBat += value.PlayerName;
                            else
                                yetToBat += ',' + value.PlayerName;
                        }
                        else {
                            Bat += '<tr><td class="td-desc">' + value.PlayerName;
                            if (value.PlayerIndicator == 'c') {
                                Bat += '(c)';
                            }
                            Bat += '<p>' + value.BTdismissalinfo + '</p></td>';
                            Bat += '<td class="td-run">' + value.BTrunScored + '</td>';
                            totalRun += parseInt(value.BTrunScored);
                            Bat += '<td>' + value.BTballfaced + '</td>';
                            Bat += '<td>' + value.BTrun6s + '</td>';
                            Bat += '<td>' + value.BTrun4s + '</td>';
                            Bat += '<td>' + value.BTstrikerate + '</td></tr>';
                        }
                    }
                    if (value.InningDesc == 'Bowling') {

                        Bowl += '<tr><td class="td-desc">' + value.PlayerName + '</td>';
                        Bowl += '<td>' + value.BLover + '</td>';
                        Bowl += '<td>' + value.BLmaiden + '</td>';
                        Bowl += '<td>' + value.BLrun + '</td>';
                        Bowl += '<td class="td-run">' + value.BLwicket + '</td>';
                        Bowl += '<td>' + value.BLecon + '</td></tr>';
                    }

                });
                Bat += '<tr><td class="td-desc"><strong>Extras</strong></td><td class="td-extra" colspan="5">' + extras + '</td></tr>';
                Bat += '<tr><td class="td-desc"><strong>Total</strong></td><td colspan="2" class="td-run">' + totalScore + '</td><td>RR ' + RR + '</td></tr>';
                if (yetToBat != '')
                Bat += '<tr><td class="td-desc" colspan="6">Did not bat <span>' + yetToBat + '</span> </td></tr>';
                $('#score_batsman_inng2').html(Bat);
                $('#score_bowler_inng2').html(Bowl);
            }

        },
        error: function (req, status, error) {
            toastr_warning("Opps! something went wrong. Try reload this page.");
            return false;
        }
    });
}