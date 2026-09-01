
$(document).ready(function () {
    var tid = sessionStorage.getItem("UserSelectTournament");
    $('#spinnerContainer').show();
    LoadUserTournament(tid);
    $('#spinnerContainer').hide();
});
var TournementList = new Array();
var CurrentSelectTournamentNo = '';

function LoadUserTournament(UserSelect_tid) {

    var uurl = '/Team/TournamentList';
    $.ajax({
        url: uurl,
        type: 'POST',
        dataType: 'json',
        async: true,
        cache: false,
        contentType: "application/json; charset=utf-8",

        success: function (data) {
            if (data.length >= 0) {
                var defaultindex;
                var divs = '';
                var hasTournament = false; var activeclass = ''; var itemCnt = 0;
                $.each(data, function (index, value) {
                    TournementList[itemCnt] = new Array(itemCnt, value.TournamentId, value.TournamentStatus, value.UserTeamId, value.UserTeamName, value.TournamentName);
                    hasTournament = true; var cBox = '';
                    if (UserSelect_tid == 0 && index == 0) {
                        defaultindex = value.TournamentId;
                        $('#UserteamId').val(value.UserTeamId);
                        $('#TournamentStatus').val(value.TournamentStatus);
                        LoadTeamInfo(defaultindex, value.TournamentStatus, value.UserTeamId, value.UserTeamName, value.TournamentName);
                        activeclass = 'active';
                        CurrentSelectTournamentNo = itemCnt + 1;
                    }
                    else if (UserSelect_tid == value.TournamentId) {
                        defaultindex = value.TournamentId;
                        $('#UserteamId').val(value.UserTeamId);
                        $('#TournamentStatus').val(value.TournamentStatus);
                        LoadTeamInfo(defaultindex, value.TournamentStatus, value.UserTeamId, value.UserTeamName, value.TournamentName);
                        activeclass = 'active';
                        CurrentSelectTournamentNo = itemCnt + 1;
                    }
                    else { activeclass = ''; }
                    cBox += '<div class="item ' + activeclass + '"><div class="slide"><h1>' + value.TournamentName + '</h1><h3>' + value.TournamentStartDate + ' - ' + value.TournamentEndDate + '</h3></div></div>';
                    itemCnt = itemCnt + 1;
                    divs += cBox;
                });
                $('#UserTournamentBox').html(divs);
                $('#hdnTourCnt').val(itemCnt);
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

function MatchDetails(Tid) {
    var uurl = '/Team/ShowMatchDetails';
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
            var defaultindex;
            var divs = '';
            var cnt = 0; var flag = 0; var hasTournament = false; var activeclass = ''; var itemCnt = 0; var defaultFlag = false; var ActiveSlideNo = 1;
            var slideId = 1;
            var tStatus = $('#TournamentStatus').val();
            $.each(data, function (index, value) {
                var userteamId = $('#UserteamId').val();
                itemCnt = itemCnt + 1;
                hasTournament = true; var cBox = '';
                if (value.MatchStatus == 'COMPLETE' && tStatus == 'COMPLETE' && defaultFlag == false) {
                    LoadTeamPlayerInfo(userteamId, value.MatchId, Tid, itemCnt, value.MatchStatus);
                    activeclass = 'active';
                    defaultFlag = true;
                    ActiveSlideNo = itemCnt;
                }
                else if ((value.MatchStatus == 'UPCOMING' || value.MatchStatus == 'INPROGRESS') && defaultFlag == false) {
                    activeclass = 'active';
                    LoadTeamPlayerInfo(userteamId, value.MatchId, Tid, itemCnt, value.MatchStatus);
                    defaultFlag = true;
                    ActiveSlideNo = itemCnt;
                }
                else if (((value.MatchStatus == 'Live' && tStatus == 'INPROGRESS') || (value.MatchStatus == 'UPCOMING' && tStatus == 'BREAK'))  && defaultFlag == false) {
                    activeclass = 'active';
                    LoadTeamPlayerInfo(userteamId, value.MatchId, Tid, itemCnt, value.MatchStatus);
                    defaultFlag = true;
                    ActiveSlideNo = itemCnt;
                }
                else { activeclass = ''; }
                if (cnt == 0 && index == 0) {
                    cBox += '<div id="slide_' + slideId + '" class="item">' //active
                    flag = 1; slideId = parseInt(slideId) + 1;
                } else if (cnt == 0) {
                    cBox += '<div id="slide_' + slideId + '" class="item">'
                    flag = 1; slideId = parseInt(slideId) + 1;
                }
                if (cnt < 4) {
                    cBox += '<div class="col-md-3"><a href="#1" onclick="LoadTeamPlayerInfo(\'' + userteamId + '\',\'' + value.MatchId + '\',\'' + value.TournamentId + '\',\'' + itemCnt + '\',\'' + value.MatchStatus + '\')";>'
                    cBox += '<div id="match_' + itemCnt + '" class="itemMatch ' + activeclass + '"><div  class="category-icon-item"><div class="icon-box">'
                    cBox += '<h5>' + value.Team1ShortName + ' v ' + value.Team2ShortName + '</h5>'
                    if (value.MatchStatus == 'COMPLETE' || value.MatchStatus == 'FINISH') {
                        cBox += '<p><img src="/Assets/Icon/tick.png" alt="" height="15px" width="25px"/>' + value.MatchDate.replace("-", " ") + ' | ' + value.Venue + '</p>';
                    }
                    else if (value.MatchStatus == 'UPCOMING') {
                        cBox += '<p>' + value.MatchDate.replace("-", " ") + ' | ' + value.Venue + '</p>';
                    }
                    else {
                        cBox += '<p><img src="/Assets/Icon/circle_green.png" class="blink-image" alt="" height="15px" width="15px"/>  Live</p>';
                    }
                    cBox += '</div></div></div></a></div>'
                    cnt = cnt + 1;
                }
                if (cnt == 4) {
                    cBox += '</div>'
                    flag = 0;
                    cnt = 0;
                }
                divs += cBox;
            });
            if (flag == 1) {
                divs += '</div>'
            }
            $('#MatchDetails').html(divs);
            $('#hdnMatchCnt').val(itemCnt);
            var slideClick = 0;
            var res = parseInt(ActiveSlideNo) % 4;
            var res1 = (parseInt(ActiveSlideNo) - res) / 4;
            if (res > 0) {
                slideClick = parseInt(res1) + 1;
            }
            else if (res == 0) {
                slideClick = parseInt(res1);
            }
            $('#slide_' + slideClick + ' ').addClass("active");
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
        var tourName = TournementList[CurrentSelectTournamentNo - 1][5];
        var tourStatus = TournementList[CurrentSelectTournamentNo - 1][2];
        var userTeamId = TournementList[CurrentSelectTournamentNo - 1][3];
        var teamName = TournementList[CurrentSelectTournamentNo - 1][4];
        LoadTeamInfo(tourId, tourStatus, userTeamId, teamName, tourName)
    }
}



function LoadTeamInfo(tid, TournamentStatus, UserTeamId, TeamName, TourName) {
    sessionStorage.setItem("UserSelectTournament", tid);
    sessionStorage.setItem("UserTourName", TeamName);
    sessionStorage.setItem("UserTeamName", TourName);
    $('#spinnerContainer').show();
    $('#UserteamId').val(UserTeamId);
    $('#teamname').html(TeamName);
    $('#TournamentStatus').val(TournamentStatus);
    $('#TournamentName').val(TourName);
    MatchDetails(tid);
}

function LoadTeamPlayerInfo(UtId, MatchId, tid, match_No, match_Stat) {
    if (match_No != 0) {
        var matchcnt = $('#hdnMatchCnt').val();
        var i = 1;
        for (i = 1; i <= matchcnt; i++) {
            $('#match_' + i).removeClass('active');
        }
        $('#match_' + match_No).addClass('active');
    }
    var manageteam = ''; var teamstat = '';
    var uurl = '';
    var TourName = $('#TournamentName').val();
    var tStat = $('#TournamentStatus').val();
    if (tStat == 'COMPLETE' && match_Stat == 'COMPLETE') {
        uurl = '/Team/TeamPlayerInfoCompleteMatch';
    } else if (tStat != 'COMPLETE' && match_Stat == 'COMPLETE') {
        manageteam += '<a href="#" class="btn3" onclick="ManageTeam(\'' + UtId + '\',\'' + tid + '\',\'' + TourName + '\',\'' + tStat + '\')";>Team Manager <img src="/Assets/Icon/Team Manager - Gear.png" /></a>'
        uurl = '/Team/TeamPlayerInfoCompleteMatch';
    } else if (tStat != 'COMPLETE') {
        manageteam += '<a href="#" class="btn3" onclick="ManageTeam(\'' + UtId + '\',\'' + tid + '\',\'' + TourName + '\',\'' + tStat + '\')";>Team Manager <img src="/Assets/Icon/Team Manager - Gear.png" /></a>'
        uurl = '/Team/TeamPlayerInfo';
    }
    teamstat = '<a href="#" class="btn3" onclick="ShowTeamStat(\'' + UtId + '\',\'' + tid + '\')";>Team Stats <img src="/Assets/Icon/Stats Icon.png" /></a>';
    $('#teamstat').html(teamstat);
    $('#manageTeam').html(manageteam);
    var pData = { TournamentId: tid, UserTeamId: UtId, MatchId: MatchId, TournamentStatus: tStat };
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
            var Player_cnt = 0;
            var divs = '';
            var wkrow = '';
            var batrow = '';
            var bwlrow = '';
            var allrow = ''; var TeamPoints = ''; var PowerPlay = ''; var tp_head = '';
            $.each(data, function (index, value) {
                if (index == 0) {
                    debugger;
                    if (tStat == 'COMPLETE') {
                        tp_head = 'My Points | Avg Points';
                        if (value.MatchTotalPoints != 0) {
                            TeamPoints = value.MatchTotalPoints + ' | ' + value.AveragePoints;
                        } else {
                            TeamPoints = '0';
                        }
                        if (value.NitroMultiplier > 0) { PowerPlay = 'Nitro'; } else
                            if (value.PainKillerUsed == 'True') { PowerPlay = 'Pain Killer'; } else
                                if (value.AutoPilotUsed == 'True') { PowerPlay = 'Auto Captain'; } else { PowerPlay = '-'; }
                    } else {
                        if (match_Stat == 'COMPLETE') {
                            tp_head = 'My Points | Avg Points';
                            if (value.MatchTotalPoints != 0) {
                                TeamPoints = value.MatchTotalPoints + ' | ' + value.AveragePoints;
                            } else {
                                TeamPoints = '0';
                            }
                            if (value.NitroMultiplier > 0) { PowerPlay = 'Nitro'; } else
                                if (value.PainKillerUsed == 'True') { PowerPlay = 'Pain Killer'; } else
                                    if (value.AutoPilotUsed == 'True') { PowerPlay = 'Auto Captain'; } else { PowerPlay = '-'; }
                        } else {
                            TeamPoints = value.SubsLeft;
                            tp_head = 'Transfer';
                            if (value.NitroUsed == 'True') { PowerPlay = 'Nitro'; } else
                                if (value.PainKillerUsed == 'True') { PowerPlay = 'Pain Killer'; } else
                                    if (value.AutoPilotUsed == 'True') { PowerPlay = 'Auto Captain'; } else { PowerPlay = '-'; }
                        }
                    }
                }
                Player_cnt = Player_cnt + 1;
                var pPrice = value.PlayerValue;
                if (value.PlayerSpeciality == 'wicketkeeper') {
                    wkrow += '<div class="playericonholder"><div class="playericon"><div class="player-left">';
                    if (value.PlayerId == value.TeamCapt) {
                        wkrow += '<img src="/Assets/Icon/Captain.png" alt="" height="20px" width="20px" />'
                    }
                    if (value.PlayerId == value.TeamVCapt) {
                        wkrow += '<img src="/Assets/Icon/ViceCaptain.png" alt="" height="20px" width="20px" />';
                    }
                    wkrow += '</div><div class="player-center"><img src="https://www.fanzania.com/FantasyCricketRest/ImagePath/image/cricket/teams/' + value.TeamImage + '"  onerror="this.src=\'/Assets/Icon/default-image.png\';" alt="" /></div>';
                    wkrow += '<div class="player-right"></div></div>';
                    if (value.PlayerType == 'overseas') {
                        wkrow += '<div class="playername">' + value.PlayerShortName + '</div>';
                    } else {
                        wkrow += '<div class="playername">' + value.PlayerShortName + '</div>';
                    }
                    if (match_Stat == 'COMPLETE') {
                        wkrow += '<div class="playerprice">' + value.PlayerPoints + '</div>';
                    } else {
                        wkrow += '<div class="playerprice">' + pPrice + ' K</div>';
                    }
                    wkrow += '</div>';
                }
                if (value.PlayerSpeciality == 'batsman') {
                    batrow += '<div class="playericonholder"><div class="playericon"><div class="player-left">';
                    if (value.PlayerId == value.TeamCapt) {
                        batrow += '<img src="/Assets/Icon/Captain.png" alt="" height="20px" width="20px" />'
                    }
                    if (value.PlayerId == value.TeamVCapt) {
                        batrow += '<img src="/Assets/Icon/ViceCaptain.png" alt="" height="20px" width="20px" />';
                    }
                    batrow += '</div><div class="player-center"><img src="https://www.fanzania.com/FantasyCricketRest/ImagePath/image/cricket/teams/' + value.TeamImage + '"  onerror="this.src=\'/Assets/Icon/default-image.png\';" alt="" /></div>';
                    batrow += '<div class="player-right"></div></div>';
                    if (value.PlayerType == 'overseas') {
                        batrow += '<div class="playername">' + value.PlayerShortName + '</div>';
                    } else {
                        batrow += '<div class="playername">' + value.PlayerShortName + '</div>';
                    }
                    if (match_Stat == 'COMPLETE') {
                        batrow += '<div class="playerprice">' + value.PlayerPoints + '</div>';
                    } else {
                        batrow += '<div class="playerprice">' + pPrice + ' K</div>';
                    }
                    batrow += '</div>';
                }
                if (value.PlayerSpeciality == 'bowler') {
                    bwlrow += '<div class="playericonholder"><div class="playericon"><div class="player-left">';
                    if (value.PlayerId == value.TeamCapt) {
                        bwlrow += '<img src="/Assets/Icon/Captain.png" alt="" height="20px" width="20px" />'
                    }
                    if (value.PlayerId == value.TeamVCapt) {
                        bwlrow += '<img src="/Assets/Icon/ViceCaptain.png" alt="" height="20px" width="20px" />';
                    }
                    bwlrow += '</div><div class="player-center"><img src="https://www.fanzania.com/FantasyCricketRest/ImagePath/image/cricket/teams/' + value.TeamImage + '"  onerror="this.src=\'/Assets/Icon/default-image.png\';" alt="" /></div>';
                    bwlrow += '<div class="player-right"></div></div>';
                    if (value.PlayerType == 'overseas') {
                        bwlrow += '<div class="playername">' + value.PlayerShortName + '</div>';
                    } else {
                        bwlrow += '<div class="playername">' + value.PlayerShortName + '</div>';
                    }
                    if (match_Stat == 'COMPLETE') {
                        bwlrow += '<div class="playerprice">' + value.PlayerPoints + '</div>';
                    } else {
                        bwlrow += '<div class="playerprice">' + pPrice + ' K</div>';
                    }
                    bwlrow += '</div>';
                }
                if (value.PlayerSpeciality == 'allrounder') {
                    allrow += '<div class="playericonholder"><div class="playericon"><div class="player-left">';
                    if (value.PlayerId == value.TeamCapt) {
                        allrow += '<img src="/Assets/Icon/Captain.png" alt="" height="20px" width="20px" />'
                    }
                    if (value.PlayerId == value.TeamVCapt) {
                        allrow += '<img src="/Assets/Icon/ViceCaptain.png" alt="" height="20px" width="20px" />';
                    }
                    allrow += '</div><div class="player-center"><img src="https://www.fanzania.com/FantasyCricketRest/ImagePath/image/cricket/teams/' + value.TeamImage + '"  onerror="this.src=\'/Assets/Icon/default-image.png\';" alt="" /></div>';
                    allrow += '<div class="player-right"></div></div>';
                    if (value.PlayerType == 'overseas') {
                        allrow += '<div class="playername">' + value.PlayerShortName + '</div>';
                    } else {
                        allrow += '<div class="playername">' + value.PlayerShortName + '</div>';
                    }
                    if (match_Stat == 'COMPLETE') {
                        allrow += '<div class="playerprice">' + value.PlayerPoints + '</div>';
                    } else {
                        allrow += '<div class="playerprice">' + pPrice + ' K</div>';
                    }
                    allrow += '</div>';
                }
            });
            if (Player_cnt == 0) {
                var i = 0;
                for (i = 0; i < 1 ; i++) {
                    wkrow += '<div class="playericonholder"><div class="playericon"><div class="player-left">';
                    wkrow += '</div><div class="player-center"><img src="/Assets/Icon/create-player.png" alt="" /></div>';
                    wkrow += '<div class="player-right"></div></div><div class="playername"></div><div class="playerprice"></div></div>';
                }
                var j = 0;
                for (j = 0; j < 4 ; j++) {
                    batrow += '<div class="playericonholder"><div class="playericon"><div class="player-left">';
                    batrow += '</div><div class="player-center"><img src="/Assets/Icon/create-player.png" alt="" /></div>';
                    batrow += '<div class="player-right"></div></div><div class="playername"></div><div class="playerprice"></div></div>';
                }
                var k = 0;
                for (k = 0; k < 3 ; k++) {
                    allrow += '<div class="playericonholder"><div class="playericon"><div class="player-left">';
                    allrow += '</div><div class="player-center"><img src="/Assets/Icon/create-player.png" alt="" /></div>';
                    allrow += '<div class="player-right"></div></div><div class="playername"></div><div class="playerprice"></div></div>';
                }
                var l = 0;
                for (l = 0; l < 3 ; l++) {
                    bwlrow += '<div class="playericonholder"><div class="playericon"><div class="player-left">';
                    bwlrow += '</div><div class="player-center"><img src="/Assets/Icon/create-player.png" alt="" /></div>';
                    bwlrow += '<div class="player-right"></div></div><div class="playername"></div><div class="playerprice"></div></div>';
                }
            }
            divs += '<div class="playerrow">' + wkrow + '</div>';
            divs += '<div class="playerrow">' + batrow + '</div>';
            divs += '<div class="playerrow">' + bwlrow + '</div>';
            divs += '<div class="playerrow">' + allrow + '</div>';
            if (tStat == 'UPCOMING' || tStat == 'BREAK' || (tStat == 'INPROGRESS' && tp_head == '')) {
                tp_head = 'Transfer';
                TeamPoints = '<img class="img-infinity" src="/Assets/Icon/infinity.png" alt=""/>';
                if(PowerPlay =='')
                PowerPlay = '-';
            }

            if (PowerPlay == 'Nitro') {
                PowerPlay = 'Nitro <img src="/Assets/Icon/Nitro.png" />';
            } else if (PowerPlay == 'Pain Killer') {
                PowerPlay = 'Pain Killer <img src="/Assets/Icon/Painkiller.png" />';
            } else if (PowerPlay == 'Auto Captain') {
                PowerPlay = 'Auto Captain <img src="/Assets/Icon/Autocaptain.png" />';
            }

            $('#Tp_heading').html(tp_head);
            $('#Players').html(divs);
            $('#teampoint').html(TeamPoints);
            $('#powerplay').html(PowerPlay);
            $('#spinnerContainer').hide();
        },
        error: function (req, status, error) {
            $('#spinnerContainer').hide();
            toastr_warning("Opps! something went wrong. Try reload this page.");
            var divs = '';
            divs += '<div class="playerrowtitle">WICKET-KEEPER</div><div class="playerrow"></div>';
            divs += '<div class="playerrowtitle">BATSMAN</div><div class="playerrow"></div>';
            divs += '<div class="playerrowtitle">BOWLER</div><div class="playerrow"></div>';
            divs += '<div class="playerrowtitle">ALL-ROUNDERS</div><div class="playerrow"></div>';
            $('#playerList').html(divs);
            return false;
        }
    });
}

function ManageTeam(TeamId, Tid, TourName, Tstat) {
    location.href = '/Team/ManageTeam?utid=' + TeamId + '&tid=' + Tid + '&tname=' + TourName + '&tstat=' + Tstat;
}

function ShowTeamStat(UtId, TId) {
    location.href = '/Team/TeamStat?utid=' + UtId + '&tid=' + TId;
}


// Set the date we're counting down to
function startTimer(idNo, matchDate, MDate) {
    //// 
    var countDownDate = new Date(matchDate).getTime();

    // Update the count down every 1 second
    var x = setInterval(function () {

        // Get todays date and time
        var now = new Date().getTime();

        // Find the distance between now and the count down date
        var distance = countDownDate - now;

        // Time calculations for days, hours, minutes and seconds
        //var days = Math.floor(distance / (1000 * 60 * 60 * 24));days + "d " +
        var hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
        var seconds = Math.floor((distance % (1000 * 60)) / 1000);

        // Display the result in the element with id="demo"
        document.getElementById("cnt_" + idNo).innerHTML = hours + "h : " + minutes + "m : " + seconds + "s ";

        // If the count down is finished, write some text
        if (distance < 0) {
            clearInterval(x);
            document.getElementById("cnt_" + idNo).innerHTML = '<img src="/Assets/Icon/circle_green.png" alt="" height="15px" width="15px"/> Live';
        }
    }, 1000);
}

function openTeamNameChangeModal() {
    $('#txtTeamNewName').val('');
    $("#ModalChangeName").modal('show');
}

function CheckNameAvailable() {
    var Tid = TournementList[CurrentSelectTournamentNo - 1][1];
    var TeamName = $('#txtTeamNewName').val();
    if (TeamName.length > 2) {
        var pData = { TournamentId: Tid, UserTeamName: TeamName };
        var jsonData = JSON.stringify(pData);
        var uurl = '/Team/CheckAvailable';
        $.ajax({
            url: uurl,
            type: 'POST',
            data: jsonData,
            dataType: 'json',
            async: true,
            cache: false,
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                var userTeamId = $('#UserteamId').val();
                if (data.status == "success") {
                    var cData = { TournamentId: Tid, UserTeamName: TeamName, UserTeamId: userTeamId };
                    var jsoncData = JSON.stringify(cData);
                    var uurl = '/Team/ChangeTeamName';
                    $.ajax({
                        url: uurl,
                        type: 'POST',
                        data: jsoncData,
                        dataType: 'json',
                        async: true,
                        cache: false,
                        contentType: "application/json; charset=utf-8",
                        success: function (data) {
                            $("#ModalChangeName").modal('hide');
                            $('#teamname').html(data[0]["UserTeamName"]);
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
