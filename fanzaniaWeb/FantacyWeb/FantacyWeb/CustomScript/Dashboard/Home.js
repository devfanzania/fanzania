
$(document).ready(function () {
    var tid = sessionStorage.getItem("UserSelectTournament");
    if (tid == '' || tid == null) {
        tid = 0;
    }
    //$('#spinnerContainer').show();
    var section1 = document.getElementById("TournamentSlider");
    var section2 = document.getElementById("TeamNameBar");
    var section3 = document.getElementById("TournamentInfo");
    var section4 = document.getElementById("DashboardMsg");
    var section5 = document.getElementById("DashboardMsg2");
    var section6 = document.getElementById("UserTournamentStatus");
    var section7 = document.getElementById("TopTenTeamsTable");

    section1.style.display = "block";
    section2.style.display = "block";
    section3.style.display = "block";
    section4.style.display = "none";
    section5.style.display = "none";
    section6.style.display = "block";
    section7.style.display = "block";

    LoadUserTournament(tid);
    LoadUpcommingTournament();
    GetDashboardTopFact();

});

var TournementList = new Array();
var CurrentSelectTournamentNo = '';

function LoadUserTournament(UserSelect_Tid) {
    var uurl = '/Dashboard/LoadUserActiveTournament';
    $.ajax({
        url: uurl,
        type: 'POST',
        dataType: 'json',
        async: true,
        cache: false,
        contentType: "application/json; charset=utf-8",

        success: function (data) {

            var defaultindex;

            var cnt = 0; var hasTournament = false; var activeclass = ''; var itemCnt = 0;
            var cBox = '';
            var divs = '';
            $.each(data, function (index, value) {

                TournementList[itemCnt] = new Array(itemCnt, value.TournamentId, value.LastMatchPoints, value.TournamentStatus);

                hasTournament = true;

                if (UserSelect_Tid == 0) {
                    if (index == 0) {
                        CurrentSelectTournamentNo = itemCnt + 1;
                        defaultindex = value.TournamentId;
                        UserStanding(defaultindex, value.LastMatchPoints, value.TournamentStatus);
                        activeclass = 'active';
                        var StatBtn = '<a id="btn_stat" href="#" class="btn1" onclick="ShowStat(\'' + value.TournamentId + '\')";>Tournament Stats <img src="/Assets/Icon/Stats Icon.png" /></a>';
                        $('#tourStat').html(StatBtn);
                        Load_TopTenUsers(defaultindex);
                    } else { activeclass = ''; }
                } else {
                    if (value.TournamentId == UserSelect_Tid) {
                        CurrentSelectTournamentNo = itemCnt + 1;
                        defaultindex = value.TournamentId;
                        UserStanding(defaultindex, value.LastMatchPoints, value.TournamentStatus);
                        activeclass = 'active';
                        var StatBtn = '<a id="btn_stat" href="#" class="btn1" onclick="ShowStat(\'' + value.TournamentId + '\')";>Tournament Stats <img src="/Assets/Icon/Stats Icon.png" /></a>';
                        $('#tourStat').html(StatBtn);
                        Load_TopTenUsers(defaultindex);
                    } else { activeclass = ''; }
                }
                cBox += '<div class="item ' + activeclass + '"><div class="slide"><h1>' + value.TournamentName + '</h1><h3>' + value.TournamentStartDate + ' - ' + value.TournamentEndDate + '</h3></div></div>';
                itemCnt = itemCnt + 1;
            });

            divs += cBox;
            $('#TournamentBox').html(divs);

            var section1 = document.getElementById("TournamentSlider");
            var section2 = document.getElementById("TeamNameBar");
            var section3 = document.getElementById("TournamentInfo");
            var section4 = document.getElementById("DashboardMsg");
            var section5 = document.getElementById("DashboardMsg2");
            var section6 = document.getElementById("UserTournamentStatus");
            var section7 = document.getElementById("TopTenTeamsTable");
            if (hasTournament == true) {
                //showLeagueDetails(defaultindex);
                section1.style.display = "block";
                section2.style.display = "block";
                section3.style.display = "block";
                section4.style.display = "none";
                section5.style.display = "block";
                section6.style.display = "block";
                section7.style.display = "block";
            }
            else {
               
                section1.style.display = "none";
                section2.style.display = "none";
                section3.style.display = "none";
                section4.style.display = "block";
                section5.style.display = "none";
                section6.style.display = "none";
                section7.style.display = "none";
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
        var LMP = TournementList[CurrentSelectTournamentNo - 1][2];
        var tStatus = TournementList[CurrentSelectTournamentNo - 1][3];
        showLeagueDetails_Reload(tourId, LMP, tStatus);
        Load_TopTenUsers(tourId);
    }
}

function LoadUpcommingTournament() {
    var uurl = '/Dashboard/UserUpcomingTtournament';
    $.ajax({
        url: uurl,
        type: 'POST',
        dataType: 'json',
        async: true,
        cache: false,
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            var divs1 = '';
            var cnt = 0;
            if (data.length > 0) {
                $.each(data, function (index, value) {
                    var cBox = '';
                    var activeclass = '';
                    if (index == 0) {
                        activeclass = 'active';
                    } else {
                        activeclass = '';
                    }
                    cBox += '<div class="item ' + activeclass + '" onclick="JoinTournament(\'' + value.TournamentId + '\',\'' + value.TournamentName + '\',\'' + value.TournamentStatus + '\')";><div class="slide"><h1>' + value.TournamentName + '</h1></div></div>';
                    divs1 += cBox;
                });
                $('#div_JoinTour').show();
            }
            else {
                $('#div_JoinTour').hide();
            }
            $('#UpcommingTournamentBox').html(divs1);
            $('#spinnerContainer').hide();
        },
        error: function (req, status, error) {
            $('#spinnerContainer').hide();
            toastr_warning("Opps! something went wrong. Try reload this page.");
            return false;
        }
    });
}

function JoinTournament(Tid, Tname, Tstat) {
    $('#SelectUpcommingTournament').val(Tid);
    $('#SelectUpcommingTournamentName').val(Tname);
    $('#SelectUpcommingTournamentStatus').val(Tstat);
    $('#Tname_join').html(Tname);
    $("#ModalJoinTournament").modal('show');
}

function CheckAvailable() {
    var Tid = $('#SelectUpcommingTournament').val();
    var Tstatus = $('#SelectUpcommingTournamentStatus').val();
    var TeamName = $('#txtTeamName').val();
    if (TeamName.length > 2) {
        var pData = { TournamentId: Tid, UserTeamName: TeamName };
        var jsonData = JSON.stringify(pData);
        var uurl = '/Dashboard/CheckAvailable';
        $.ajax({
            url: uurl,
            type: 'POST',
            data: jsonData,
            dataType: 'json',
            async: true,
            cache: false,
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                var Tname = $('#SelectUpcommingTournamentName').val();
                if (data.status == "success") {
                    
                    var cData = { TournamentId: Tid, UserTeamName: TeamName, TournamentName: Tname, TournamentStatus: Tstatus };
                    var jsoncData = JSON.stringify(cData);
                    var uurl = '/Dashboard/CreateTeam';
                    $.ajax({
                        url: uurl,
                        type: 'POST',
                        data: jsoncData,
                        dataType: 'json',
                        async: true,
                        cache: false,
                        contentType: "application/json; charset=utf-8",
                        success: function (data) {
                            //var UTI = data[0]["UserTeamId"];
                            //var tstat = $('#SelectUpcommingTournamentStatus').val();
                            //location.href = '/Team/ManageTeam?utid=' + UTI + '&tid=' + Tid + '&tname=' + Tname + '&tstat=' + tstat;
                            location.href = data.Url;
                        },
                        error: function (req, status, error) {
                            alert('Opps! something went wrong. Try reload this page.');
                            return false;
                        }
                    });
                } else {
                    $('#checkmsg').html('Team Name Exist..');
                }
            },
            error: function (req, status, error) {
                toastr_warning("Opps! something went wrong. Try reload this page.");
                return false;
            }
        });
    } else {
        toastr_info("Your team name should have at least 3 characters.");
    }
}

function UserStanding(Tid, LMP, tStatus) {
    sessionStorage.setItem("UserSelectTournament", Tid);
    var uurl = '/Dashboard/TeamInfo';
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
            var teamname = '';
            var teampoint = '';
            var globalrank = '';
            var TeamPercentile = '';
            if (data.length > 0) {
                $.each(data, function (index, value) {
                    if (value.TournamentId == Tid) {
                        teamname = value.UserTeamName;
                        if (value.TotalPoints == 0) {
                            teampoint = '-';
                        } else {
                            teampoint = value.TotalPoints;
                        }
                        if (value.TeamGlobalRank == 0) {
                            globalrank = '-';
                        } else {
                            globalrank = value.TeamGlobalRank;
                        }
                        if (LMP == 0) {
                            TeamPercentile = '-';
                        } else {
                            TeamPercentile = LMP;
                        }
                    }
                });
                var section1 = document.getElementById("TournamentSlider");
                var section2 = document.getElementById("TeamNameBar");
                var section3 = document.getElementById("TournamentInfo");
                var section4 = document.getElementById("DashboardMsg");
                var section5 = document.getElementById("DashboardMsg2");
                var section6 = document.getElementById("UserTournamentStatus");
                var section7 = document.getElementById("TopTenTeamsTable");

                if (tStatus == 'UPCOMING') {
                    section1.style.display = "block";
                    section2.style.display = "block";
                    section3.style.display = "block";
                    section4.style.display = "none";
                    section5.style.display = "block";
                    section6.style.display = "none";
                    section7.style.display = "block";
                    $('#btn_stat').hide();
                }
                else {
                    section1.style.display = "block";
                    section2.style.display = "block";
                    section3.style.display = "block";
                    section4.style.display = "none";
                    section5.style.display = "none";
                    section6.style.display = "block";
                    section7.style.display = "block";
                    $('#btn_stat').show();
                }
            } else {
                teamname = '';
                teampoint = '';
                globalrank = '';
                TeamPercentile = '';
            }
            $('#teamname').html(teamname);
            $('#teampoint').html(teampoint);
            $('#globalrank').html(globalrank);
            $('#TeamPercentile').html(TeamPercentile);
        },
        error: function (req, status, error) {
            toastr_warning("Opps! something went wrong. Try reload this page.");
            return false;
        }
    });
}

function ShowStat(TId) {
    location.href = '/Dashboard/TournamentStat?TId=' + TId;
}


/*
var grid;
function showLeagueDetails(TId) {
    grid = $("#grid").grid({
        dataSource: { url: '/Dashboard/LoadUserActiveLeagueInfo', data: { TournamentId: TId } },
        notFoundText: "No league available",
        responsive: true,
        uiLibrary: "bootstrap",
        fixedHeader: true,
        height: 200,
        minWidth: 400,
        columns: [

            { field: "LeagueName", title: "Leagues", width: "33%", align: "left", cssClass: "LeagueName" },
            { field: "LeagueRank", title: "League Rank", width: "33%", align: "center", cssClass: "LeagueRank" },
            { field: "TeamStanding", title: "My Standing", width: "33%", align: "center", cssClass: "TeamStanding" }
        ],
    });
    grid.on("rowDataBound", function (e, $row, id, record) {
        var lname = $row.find(".LeagueName").html();
        $row.find('.LeagueName').html('<a href="../League/Index?lId=' + record.LeagueId + '&tid=' + TId + '&type=' + record.LeagueLeaderId + '" title="Get League" >' + lname + '</a>');
        if (record.TeamStanding == 0) {
            $row.find('.TeamStanding').html('<i class="fa fa-minus" aria-hidden="true"></i>')
        } else {
            $row.find('.TeamStanding').html(record.TeamStanding)
        }
        if (record.LeagueRank == null) {
            $row.find('.LeagueRank').html('<i class="fa fa-minus" aria-hidden="true"></i>')
        } else {
            $row.find('.LeagueRank').html(record.LeagueRank)
        }
    });

}
*/

function Showteam(Tid,uTid, teamName) {
    
    var url = '/Dashboard/TeamPlayerInfoCompleteMatch';
    var pData = { TournamentId: Tid, UserTeamId: uTid };
    LoadPlayersOnGround(uTid, Tid, teamName, url, pData, 'CT');
}

function LoadPlayersOnGround(UtId, tid, teamname, Url, jData, tabType) {
    var jsonData = JSON.stringify(jData);
    $.ajax({
        url: Url,
        type: 'POST',
        data: jsonData,
        dataType: 'json',
        async: true,
        cache: false,
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            console.log(data);
            var Player_cnt = 0;
            var divs = '';
            var wkrow = '';
            var batrow = '';
            var bwlrow = '';
            var allrow = '';
            var nitroHtml = '';
            var ppHtml = '';
            var autocapHtml = '';
            var winnerPrediction = '';
            var winnerPredictionStatus = '';
            $('#transferLeft').html( '<br><span>Transfers</span>');
            $('#lastMatchPoint').html( '<br><span><span style="text-transform: none;"></span> Points</span>');

            if (data.length > 0) {

                $.each(data, function (index, value) {
                    if (index == 0) {
                        $('#transferLeft').html(value.SubsLeft + '<br><span>Transfers</span>');
                        $('#lastMatchPoint').html(value.MatchTotalPoints + '<br><span><span style="text-transform: none;">' + value.LastMatchTeams + '</span> Points</span>');
                        if (value.NitroLeft > 0) {
                            nitroHtml = '<img data-toggle="tooltip" class="pp" title="Nitro" src="/Assets/Icon/Nitro_red.png" />';
                        }
                        else {
                            if (value.NitroMultiplier == 0) {
                                nitroHtml = '<img data-toggle="tooltip" class="pp" title="Nitro" src="/Assets/Icon/Nitro_grey.png" />';
                            }
                            else {
                                nitroHtml = '<img data-toggle="tooltip" class="pp" title="Nitro" src="/Assets/Icon/Nitro_green.png" />';
                            }
                        }
                        if (value.PainKillerLeft > 0) {
                            ppHtml = '<img data-toggle="tooltip" class="pp" title="Pain Killer" src="/Assets/Icon/Painkiller_red.png" />';
                        }
                        else {
                            if (value.PainKillerUsed == 'False') {
                                ppHtml = '<img data-toggle="tooltip" class="pp" title="Pain Killer" src="/Assets/Icon/Painkiller_grey.png" />';
                            }
                            else {
                                ppHtml = '<img data-toggle="tooltip" class="pp" title="Pain Killer" src="/Assets/Icon/Painkiller_green.png" />';
                            }
                        }
                        if (value.AutoPilotLeft > 0) {
                            autocapHtml = '<img data-toggle="tooltip" class="pp" title="Ultra Captain" src="/Assets/Icon/Autocaptain_red.png" />';
                        }
                        else {
                            if (value.AutoPilotUsed == 'False') {
                                autocapHtml = '<img data-toggle="tooltip" class="pp" title="Ultra Captain" src="/Assets/Icon/Autocaptain_grey.png" />';
                            }
                            else {
                                autocapHtml = '<img data-toggle="tooltip" class="pp" title="Ultra Captain" src="/Assets/Icon/Autocaptain_green.png" />';
                            }
                        }
                        $('#powerPlay').html(nitroHtml + ppHtml + autocapHtml + '<br><span>Power Plays</span>');
                    }

                    Player_cnt = Player_cnt + 1;
                    var pPrice = value.PlayerValue;
                    if (value.PlayerSpeciality == 'wicketkeeper') {
                        wkrow += '<div class="playericonholder"><div class="playericon"><div class="player-left">';
                        if (value.PlayerId == value.TeamCapt) {
                            wkrow += '<img src="/Assets/Icon/Captain.png" alt=""/>'
                        }
                        if (value.PlayerId == value.TeamVCapt) {
                            wkrow += '<img src="/Assets/Icon/ViceCaptain.png" alt=""/>';
                        }
                        wkrow += '</div><div class="player-center"><img src="https://www.fanzania.com/FantasyCricketRest/ImagePath/image/cricket/teams/' + value.TeamImage + '" alt="" /></div>';
                        wkrow += '<div class="player-right"><img src="" alt=""/></div></div>';
                        wkrow += '<div class="playername">' + value.PlayerShortName + '</div>';

                        wkrow += '<div class="playerprice">' + value.PlayerPoints + '</div>';
                        wkrow += '</div>';
                    }
                    if (value.PlayerSpeciality == 'batsman') {
                        batrow += '<div class="playericonholder"><div class="playericon"><div class="player-left">';
                        if (value.PlayerId == value.TeamCapt) {
                            batrow += '<img src="/Assets/Icon/Captain.png" alt=""/>'
                        }
                        if (value.PlayerId == value.TeamVCapt) {
                            batrow += '<img src="/Assets/Icon/ViceCaptain.png" alt=""/>';
                        }
                        batrow += '</div><div class="player-center"><img src="https://www.fanzania.com/FantasyCricketRest/ImagePath/image/cricket/teams/' + value.TeamImage + '" alt="" /></div>';
                        batrow += '<div class="player-right"><img src="" alt=""/></div></div>';
                        batrow += '<div class="playername">' + value.PlayerShortName + '</div>';
                        batrow += '<div class="playerprice">' + value.PlayerPoints + '</div>';
                        batrow += '</div>';
                    }
                    if (value.PlayerSpeciality == 'bowler') {

                        bwlrow += '<div class="playericonholder"><div class="playericon"><div class="player-left">';
                        if (value.PlayerId == value.TeamCapt) {
                            bwlrow += '<img src="/Assets/Icon/Captain.png" alt=""/>'
                        }
                        if (value.PlayerId == value.TeamVCapt) {
                            bwlrow += '<img src="/Assets/Icon/ViceCaptain.png" alt=""/>';
                        }
                        bwlrow += '</div><div class="player-center"><img src="https://www.fanzania.com/FantasyCricketRest/ImagePath/image/cricket/teams/' + value.TeamImage + '" alt="" /></div>';
                        bwlrow += '<div class="player-right"><img src="" alt=""/></div></div>';
                        bwlrow += '<div class="playername">' + value.PlayerShortName + '</div>';

                        bwlrow += '<div class="playerprice">' + value.PlayerPoints + '</div>';
                        bwlrow += '</div>';
                    }
                    if (value.PlayerSpeciality == 'allrounder') {

                        allrow += '<div class="playericonholder"><div class="playericon"><div class="player-left">';
                        if (value.PlayerId == value.TeamCapt) {
                            allrow += '<img src="/Assets/Icon/Captain.png" alt=""/>'
                        }
                        if (value.PlayerId == value.TeamVCapt) {
                            allrow += '<img src="/Assets/Icon/ViceCaptain.png" alt=""/>';
                        }
                        allrow += '</div><div class="player-center"><img src="https://www.fanzania.com/FantasyCricketRest/ImagePath/image/cricket/teams/' + value.TeamImage + '" alt="" /></div>';
                        allrow += '<div class="player-right"><img src="" alt=""/></div></div>';
                        allrow += '<div class="playername">' + value.PlayerShortName + '</div>';
                        allrow += '<div class="playerprice">' + value.PlayerPoints + '</div>';
                        allrow += '</div>';
                        winnerPrediction = value.WinnerPrediction;
                        if (value.WinnerPredictionStatus == true) {
                            winnerPredictionStatus = '<i class="fa fa-check" aria-hidden="true" style="font-size: 14px;color: #17a517;"></i>';
                        }
                        else {
                            winnerPredictionStatus = '<i class="fa fa-times" aria-hidden="true" style="font-size: 14px;" ></i>';
                        }
                    }
                });
            }
            divs += '<div class="playerrowtitle"></div><div class="playerrow">' + wkrow + '</div>';
            divs += '<div class="playerrowtitle"></div><div class="playerrow">' + batrow + '</div>';
            divs += '<div class="playerrowtitle"></div><div class="playerrow">' + bwlrow + '</div>';
            divs += '<div class="playerrowtitle"></div><div class="playerrow">' + allrow + '</div>';
          
            $('#powerPlay').html(nitroHtml + ppHtml + autocapHtml + '<br><span>Power Plays</span>');
            $('#WinnerPredictionWithstatus').html('<span>' + winnerPrediction + '&nbsp;&nbsp</span>' + winnerPredictionStatus + '<br><span>Prediction</span>');
            $('#Players').html(divs);
        },
        error: function (req, status, error) {
            toastr_warning("Opps! something went wrong. Try reload this page.");
            return false;
        }
    });
}
var Grid_TopTenUsers;
function Load_TopTenUsers(TId) {
    $('#TopTenTeamsTable').show();
    Grid_TopTenUsers = $("#TopTenUserTable").grid({
        dataSource: { url: '/Dashboard/TopTenUsers', data: { TournamentId: TId } },
        notFoundText: $('#TopTenTeamsTable').hide(),
        responsive: true,
        uiLibrary: "bootstrap",
        fixedHeader: true,
        height: 500,
        minWidth: 600,
        columns: [

            { field: "TeamRank", title: "Rank", width: "10%", align: "center", cssClass: "TeamRank" },
            { field: "", title: "Team", width: "30%", align: "center", cssClass: "UserTeamName" },
            { field: "", title: "Owner", width: "30%", align: "center", cssClass: "TeamOwner" },
            { field: "TotalPoints", title: "Points", width: "20%", align: "center", cssClass: "TotalPoints" },
            { field: "", title: "", width: "10%", align: "center", cssClass: "teamdetails" }
        ],
    });
    Grid_TopTenUsers.on("rowDataBound", function (e, $row, id, record) {
        
        if (id == 1) {
            Showteam(TId, record.UserTeamId, record.UserTeamName);
           // $row.addClass('active');
        }
        $row.find('.teamdetails').html('<i class="fa fa-angle-right an-r grid-right" aria-hidden="true" onclick="Showteam(' + TId + ',' + record.UserTeamId + ',' + '\'' + record.UserTeamName + '\');"></i>');
        $row.find('.UserTeamName').html('<p class="OwnerTeam">' + record.UserTeamName + '</p>');
        $row.find('.TeamOwner').html('<p class="OwnerTeam">' + record.Name + '</p>');
        $row.find('.TotalPoints').html('<p class="OwnerTeam">' + record.LastMatchPoints + '</p>');
        $row.find('.TeamRank').html('<p class="OwnerTeam">' + record.Rank + '</p>');
        $('#TopTenUserHeader').html(record.MatchDetails + ' Top 10 Teams');
    });

}

function showLeagueDetails_Reload(TId, LMP, tStatus) {
    $('#spinnerContainer').show();
    var StatBtn = '<a id="btn_stat" href="#" class="btn1" onclick="ShowStat(\'' + TId + '\')";>Tournament Stats <img src="/Assets/Icon/Stats Icon.png" /></a>';
    $('#tourStat').html(StatBtn);
    var section1 = document.getElementById("TournamentSlider");
    var section2 = document.getElementById("TeamNameBar");
    var section3 = document.getElementById("TournamentInfo");
    var section4 = document.getElementById("DashboardMsg");
    var section5 = document.getElementById("DashboardMsg2");
    var section7 = document.getElementById("TopTenTeamsTable");
    if (tStatus == 'UPCOMING') {
        section1.style.display = "block";
        section2.style.display = "block";
        section3.style.display = "block";
        section7.style.display = "block";
        section4.style.display = "none";
        section5.style.display = "block";
        $('#btn_stat').hide();
    }
    else {
        section1.style.display = "block";
        section2.style.display = "block";
        section3.style.display = "block";
        section7.style.display = "block";
        section4.style.display = "none";
        section5.style.display = "none";
        $('#btn_stat').show();
    }
    UserStanding(TId, LMP);
    //grid.reload({ page: 1, TournamentId: TId });
    $('#spinnerContainer').hide();
}

function GetDashboardTopFact() {
    var uurl = '/Dashboard/GetDashboardTopFact';
    $.ajax({
        url: uurl,
        async: true,
        cache: false,
        contentType: "application/json; charset=utf-8",

        success: function (data) {
            if (data.length > 0) {
                $('#DashFact').html(data[0].FunMessage);
            }
        },
        error: function (req, status, error) {
            toastr_warning("Opps! something went wrong. Try reload this page.");
            return false;
        }
    });
}