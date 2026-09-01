
$(document).ready(function () {
    var tid = sessionStorage.getItem("UserSelectTournament");
    //console.log('tid', tid);
    $('#spinnerContainer').show();
    LoadUserTournament(tid);
    $('#spinnerContainer').hide();
});
var TournementList = new Array();
var CurrentSelectTournamentNo = '';

function LoadUserTournament(UserSelect_tid) {
    if (UserSelect_tid == null)
        UserSelect_tid = 0;

    var uurl = '/Team/TournamentList';
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
                $('#TournamentSlide_prev').hide();
                $('#TournamentSlide_next').hide();
                $('#CarouselMatch_left').hide();
                $('#CarouselMatch_right').hide();
                location.href = '/Dashboard/Index';
            }
            if (itemCnt > 1) {
                $('#TournamentSlide_prev').show();
                $('#TournamentSlide_next').show();
            }
            else {
                $('#TournamentSlide_prev').hide();
                $('#TournamentSlide_next').hide();
            }
        },
        error: function (req, status, error) {
            toastr_warning("Opps! something went wrong. Try reload this page.");
            return false;
        }
    });
}

function MatchDetails(Tid) {
   // alert(Tid);
    var uurl = '/Team/ShowMatchDetails';
    var pData = { TournamentId: Tid };
    var jsonData = JSON.stringify(pData);
  //  console.log(jsonData);

    $.ajax({
        url: uurl,
        type: 'POST',
        data: jsonData,
        dataType: 'json',
        async: true,
        cache: false,
        contentType: "application/json; charset=utf-8",

        success: function (data) {
           
      //   console.log(data);
          // return;
            var defaultindex;
            var divs = '';
            var cnt = 0; var flag = 0; var hasTournament = false; var activeclass = ''; var itemCnt = 0; var defaultFlag = false; var ActiveSlideNo = 1;
            var slideId = 1;
            var BattingTeam1 = '';
            var BattingTeam2 = '';
            //var BattingTeam = 'KXIP';
            var tStatus = $('#TournamentStatus').val();
            var datacount = data.length;
            
            $.each(data, function (index, value) {
                var userteamId = $('#UserteamId').val();
                itemCnt = itemCnt + 1;
                hasTournament = true; var cBox = ''; var  seticon = '';
                //console.log(itemCnt,tStatus, userteamId, value.MatchId, Tid, value.MatchStatus);
                if (tStatus == 'COMPLETE' && defaultFlag == false) {
                    //console.log('1');
                    LoadTeamPlayerInfo(userteamId, value.MatchId, Tid, itemCnt, value.MatchStatus);
                    activeclass = 'active';
                    defaultFlag = true;
                    ActiveSlideNo = itemCnt;
                }
                else if ((value.MatchStatus == 'UPCOMING' || value.MatchStatus == 'Live' || value.MatchStatus == 'FINISH' || value.MatchStatus == '1stInng') && defaultFlag == false) {
                    //console.log('2');
                    activeclass = 'active';
                    LoadTeamPlayerInfo(userteamId, value.MatchId, Tid, itemCnt, value.MatchStatus);
                    defaultFlag = true;
                    ActiveSlideNo = itemCnt;
                }
				else if ((value.MatchStatus == 'COMPLETE' && tStatus=="BREAK") && defaultFlag == false) {
				    //console.log('3');
				    activeclass = 'active';
                    LoadTeamPlayerInfo(userteamId, value.MatchId, Tid, itemCnt, value.MatchStatus);
                    defaultFlag = true;
                    ActiveSlideNo = itemCnt;
                }
                else {
                  
                    if ((datacount == index + 1) && defaultFlag == false) {
                       // console.log(datacount);
                       // console.log(index + 1);
                        activeclass = 'active';
                        LoadTeamPlayerInfo(userteamId, value.MatchId, Tid, itemCnt, value.MatchStatus);
                        defaultFlag = true;
                        ActiveSlideNo = itemCnt;
                    }
                    else {
                        activeclass = '';
                         }
                    
                }
                
				
                if (cnt == 0 && index == 0) {
                    cBox += '<div id="slide_' + slideId + '" class="item">' //active
                    flag = 1; slideId = parseInt(slideId) + 1;
                } else if (cnt == 0) {
                    cBox += '<div id="slide_' + slideId + '" class="item">'
                    flag = 1; slideId = parseInt(slideId) + 1;
                }
                if (cnt < 4) {
                  
                    if (value.Weather == "rain") {
                        seticon += '<a  href="#" onclick="ShowWeatherDetails()";><img src="/Assets/Icon/rain.png" alt=""  style="float: right;top: -3px; position: absolute;right: 18px; width: 28px;" /></a>';
                    } else if (value.Weather == "sunny") {
                        seticon += '<a href="#"  onclick="ShowWeatherDetails()"; ><img src="/Assets/Icon/sunny.png" alt=""  style="float: right;top: -3px; position: absolute;right: 18px; width: 28px;"  /></a>';
                    } else if (value.Weather == "snow") {
                        seticon += '<a href="#"  onclick="ShowWeatherDetails()"; ><img src="/Assets/Icon/snow.png" alt=""  style="float: right;top: -3px; position: absolute;right: 18px; width: 28px;"  /></a>';
                    }
                    else if (value.Weather == "cloudy") {
                        seticon += '<a href="#" onclick="ShowWeatherDetails()"; ><img src="/Assets/Icon/cloudy.png" alt=""  style="float:right;top: -3px; position: absolute;right: 18px; width: 28px;"  /></a>';
                    } else if (value.Weather == "thunderstorms") {
                        seticon += '<a href="#"  onclick="ShowWeatherDetails()"; ><img src="/Assets/Icon/thunderstorms.png" alt=""  style="float:right;top: -3px; position: absolute;right: 18px; width: 28px;"  /></a>';
                    }
                    else if (value.Weather == "") {
                        seticon += '';
                    }
                    //if (value.BattingTeam == value.Team1) {
                    //    BattingTeam1 = '<img src="/Assets/Icon/bat_.png" class="blink-image" alt="" height="20px" width="20px"/> ';
                    //} else if (value.BattingTeam == value.Team2) {
                    //    BattingTeam2 = '<img src="/Assets/Icon/bat_.png" class="blink-image" alt="" height="20px" width="20px"/>';
                    //} else {
                    //    BattingTeam1 = '';
                    //    BattingTeam2 = '';
                    //}
                    cBox += '<div class="col-md-3"><a href="#1" onclick="LoadTeamPlayerInfo(\'' + userteamId + '\',\'' + value.MatchId + '\',\'' + value.TournamentId + '\',\'' + itemCnt + '\',\'' + value.MatchStatus + '\')";>'
                    cBox += '<div id="match_' + itemCnt + '" class="itemMatch ' + activeclass + '"><div  class="category-icon-item"><div class="icon-box">'
                    cBox += '<p style="font-weight: 500;line-height: 1.1;"> #' + value.MatchNo + ' ' + BattingTeam1 + value.Team1ShortName + '  v  ' + value.Team2ShortName + '  ' +BattingTeam2 + '</p>' + seticon + ''
                    if (value.MatchStatus == 'COMPLETE' || value.MatchStatus == 'FINISH') {

                     
                        if (value.BattingTeam == value.Team1) {
                            cBox += '<p><img src="/Assets/Icon/circle_green.png" class="blink-image" alt="" height="15px" width="15px"/>  ' + value.Team1ShortName + ' to BAT</p>';
                        }
                        else if (value.BattingTeam == value.Team2) {
                            cBox += '<p><img src="/Assets/Icon/circle_green.png" class="blink-image" alt="" height="15px" width="15px"/>  ' + value.Team2ShortName + ' to BAT</p>';
                        }
                        else{
                            cBox += '<p><img src="/Assets/Icon/tick.png" alt="" height="15px" width="25px"/>' + value.MatchDate.replace("-", " ") + ' | ' + value.Venue + '</p>';
                        }

                    }
                    else if (value.MatchStatus == 'UPCOMING') {
                       

                        if (value.BattingTeam == value.Team1) {
                            cBox += '<p><img src="/Assets/Icon/circle_green.png" class="blink-image" alt="" height="15px" width="15px"/>  ' + value.Team1ShortName + ' to BAT</p>';
                        }
                        else if (value.BattingTeam == value.Team2) {
                            cBox += '<p><img src="/Assets/Icon/circle_green.png" class="blink-image" alt="" height="15px" width="15px"/>  ' + value.Team2ShortName + ' to BAT</p>';
                        }
                        else{
                            cBox += '<p>' + value.MatchDate.replace("-", " ") + ' | ' + value.Venue + '</p>';
                           }
                    }
                    else {
                        cBox += '<p><img src="/Assets/Icon/circle_green.png" class="blink-image" alt="" height="15px" width="15px"/>  Live</p>';
                    }
                    cBox += '</div></div></div></a></div>'
                    //if (value.BattingTeam == value.Team2) {
                    //    cBox += '<p><img src="/Assets/Icon/circle_green.png" class="blink-image" alt="" height="15px" width="15px"/>  ' + value.Team2 + 'to bat</p>';
                    //} if (value.BattingTeam == value.Team1) {
                    //    cBox += '<p><img src="/Assets/Icon/circle_green.png" class="blink-image" alt="" height="15px" width="15px"/>  ' + value.Team2 + 'to bat</p>';
                    //} else {
                    //    cBox += '<p>' + value.MatchDate.replace("-", " ") + ' | ' + value.Venue + '</p>';
                    //}
                 //   cBox += '<div class="col-md-1" ><p class="itemMatch"> ' + seticon +'</p></div>'
                    cnt = cnt + 1;
                }
                if (cnt == 4) {
                    cBox += '</div>'
                    flag = 0;
                    cnt = 0;
                }
                divs += cBox;
            });
        //    console.log(data);
          //  return;
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
//$("#myHref").on('click', function () {
//    alert("inside onclick");
//    window.location = "http://www.google.com";
//});
function ShowWeatherDetails() {
 //   alert('ddd');
    $("#modelweathericon").modal('show');
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
    //console.log(UtId, MatchId, tid, match_No, match_Stat);
    if (match_No != 0) {
        var matchcnt = $('#hdnMatchCnt').val();
        var i = 1;
        for (i = 1; i <= matchcnt; i++) {
            $('#match_' + i).removeClass('active');
        }
        $('#match_' + match_No).addClass('active');
    }
    var manageteam = ''; var teamstat = '';
    var powerplay = '';
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
    powerplay = '<button type="button" style="border: 1px solid #fff;" class="btn3" onclick="ShowPowerPlay()";> PowerPlay Lifeline <img src="/Assets/Icon/Powerplay_Lifeline.png" /></button>';
    $('#powerplaybutton').html(powerplay);
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
            console.log(data);
          
            var Player_cnt = 0;
            var divs = '';
            var wkrow = '';
            var batrow = '';
            var bwlrow = '';
            var allrow = ''; var TeamPoints = ''; var PowerPlay = ''; var tp_head = '';
            
            $.each(data, function (index, value) {
                if (value.PlayingInd == true)
                    playerActive = 'player-active';
                else
                    playerActive = '';
                if (index == 0) {
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
                    wkrow += '</div><div class="player-center"><img class = "' + playerActive + '" src="https://www.fanzania.com/FantasyCricketRest/ImagePath/image/cricket/teams/' + value.TeamImage + '"  onerror="this.src=\'/Assets/Icon/default-image.png\';" alt="" /></div>';
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
                    batrow += '</div><div class="player-center"><img class = "' + playerActive + '" src="https://www.fanzania.com/FantasyCricketRest/ImagePath/image/cricket/teams/' + value.TeamImage + '"  onerror="this.src=\'/Assets/Icon/default-image.png\';" alt="" /></div>';
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
                    bwlrow += '</div><div class="player-center"><img class = "' + playerActive + '" src="https://www.fanzania.com/FantasyCricketRest/ImagePath/image/cricket/teams/' + value.TeamImage + '"  onerror="this.src=\'/Assets/Icon/default-image.png\';" alt="" /></div>';
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
                    allrow += '</div><div class="player-center"><img class = "' + playerActive + '" src="https://www.fanzania.com/FantasyCricketRest/ImagePath/image/cricket/teams/' + value.TeamImage + '"  onerror="this.src=\'/Assets/Icon/default-image.png\';" alt="" /></div>';
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
                PowerPlay = 'Ultra Captain <img src="/Assets/Icon/Autocaptain.png" />';
            }

            $('#Tp_heading').html(tp_head);
            $('#Players').html(divs);
            $('#teampoint').html(TeamPoints);
            if (data.length > 0) {
                if (match_Stat == 'COMPLETE') {
                   
                    if (data[0].WinnerPrediction == '') {
                        $('#winnerPredictionteam').html('-');
                    } else {
                        if (data[0].WinnerPredictionStatus == false) {
                            $('#winnerPredictionteam').html(data[0].WinnerPrediction + '&nbsp;&nbsp;&nbsp;<i class="fa fa-times" aria-hidden="true" style="font-size: 14px;color: #fff;"></i>');
                        }
                        else {
                            $('#winnerPredictionteam').html(data[0].WinnerPrediction + '&nbsp;&nbsp;&nbsp;<i class="fa fa-check" aria-hidden="true" style="font-size: 14px;color: #17a517;"></i>');
                            }
                        }
                }
                else {
                    if (data[0].WinnerPrediction == '') {
                        $('#winnerPredictionteam').html('-');
                    } else {
                        $('#winnerPredictionteam').html(data[0].WinnerPrediction);
                    }
                   }
            } else {
                $('#winnerPredictionteam').html('-');
            }
            $('#powerplay').html(PowerPlay);
            $('#spinnerContainer').hide();
           // $('#winnerPredictionteam').html(sessionWinnerPrediction);
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
    //location.href = '/Team/ManageTeam?utid=' + TeamId + '&tid=' + Tid + '&tname=' + TourName + '&tstat=' + Tstat;

    var pData = { TournamentId: Tid, UserTeamId: TeamId, TournamentName: TourName, TournamentStatus: Tstat };
    var jsonData = JSON.stringify(pData);
    $.ajax({
        url: '/Team/RedirectToManageTeam',
        type: 'POST',
        data: jsonData,
        dataType: 'json',
        async: true,
        cache: false,
        contentType: "application/json; charset=utf-8",

        success: function (data) { 
            //debugger;
            window.location.href = data.Url;
        },
        error: function (req, status, error) {
           
            toastr_warning("Opps! something went wrong. Try reload this page.");
            return false;
        }
    });
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
    //   var pData = { TournamentId: Tid, UserTeamName: TeamName };
     //   var jsonData = JSON.stringify(pData);
     //   var uurl = '/Team/CheckAvailable';
     //   $.ajax({
         //   url: uurl,
          //  type: 'POST',
          //  data: jsonData,
           // dataType: 'json',
          //  async: true,
          //  cache: false,
         //   contentType: "application/json; charset=utf-8",
           // success: function (data) {
                var userTeamId = $('#UserteamId').val();
              //  if (data.status == "success") {
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
                                if (data != '') {
                                    $("#ModalChangeName").modal('hide');
                                    $('#teamname').html(data[0]["UserTeamName"]);
                                }
                                else {
                                 
                                    $('#checkmsg').html(data.statusMessage);
                                 }
                        },
                        error: function (req, status, error) {
                            alert('Opps! something went wrong. Try reload this page.');
                            return false;
                        }
                    });
               // } else {
                   // $('#checkmsg').html('Team Name Exist..');
               // }
          //  },
            //error: function (req, status, error) {
               // toastr_warning("Opps! something went wrong. Try reload this page.");
               // return false;
           // }
        //});
    } else {
        toastr_info("Your team name should have at least 3 characters.");
    }
}

function ShowPowerPlay() {

    //alert('test');
    $('#cubtnnitro').hide();
    $('#tabtoselectbtnnitro').hide();
    $('#btnusednitro').hide();

    $('#tabtoselectbtnpc').hide();
    $('#usedpk').hide();
    $('#cubtnpk').hide();

    $('#usedcu').hide();
    $('#cubtncu').hide();
    $('#tabtoselectbtnuc').hide();

    $('.TabToSelectUltraselected').hide();
    $('.TabToSelectPainselected').hide();
    $('.TabToSelectNitroselected').hide();
    $('.tabtoselectmessage').hide();
    $('#savebtn').hide();
    
    var userTeamId = $('#UserteamId').val();
    var tid = sessionStorage.getItem("UserSelectTournament");
    ///alert(tid);
    //alert(userTeamId);
  //alert(Session_usedId);
    //  if (data.status == "success") {
    var cData = {
        UserTeamId: userTeamId, UserId: Session_usedId, TournamentId: tid
    }
    var jsoncData = JSON.stringify(cData);
    var uurl = '/Team/FetchUserPowerPlay';
    $.ajax({
        url: uurl,
        type: 'POST',
        data: jsoncData,
        dataType: 'json',
        async: true,
        cache: false,
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            if (data != '') {
              // console.log(data.data[0]);
              //  console.log(data.data[0]["NitroEnable"]);
                $('.notselectmessage').show();
                $('#disablebtn').show();
                
                $('#NitroUserTeamMatchPointId').val(data.data[0]["NitroUserTeamMatchPointId"]);
                $('#PainKillerUserTeamMatchPointId').val(data.data[0]["PainKillerUserTeamMatchPointId"]);
                $('#AutoPilotUserTeamMatchPointId').val(data.data[0]["AutoPilotUserTeamMatchPointId"]);

                $('#NitroPoints').val(data.data[0]["NitroPoints"]);
                $('#PainKillerPoints').val(data.data[0]["PainKillerPoints"]);
                $('#AutoPilotPoints').val(data.data[0]["AutoPilotPoints"]);

                if (data.data[0]["NitroEnable"] == true) {
                    $('#availablenitro').show();
                    $('#NitroPointsshow').html(data.data[0]["NitroPoints"] + " bonus points ");
                    $('#notavailablenitro').hide();
                    $('#usednitro').hide();      

                    //button
                 
                    $('#tabtoselectbtnnitro').show();
                    $('#btnusednitro').hide();
                    $('#cubtnnitro').hide();

                }
                if (data.data[0]["NitroEnable"] == false) {
                  //  $('#NitroPoints').html(data.data[0]["NitroPoints"] + " bouns points");
                    $('#availablenitro').hide();
                    $('#notavailablenitro').show();
                    $('#usednitro').hide();   

                    //buttons
                    $('#cubtnnitro').show();
                    $('#tabtoselectbtnnitro').hide();
                    $('#btnusednitro').hide();



                    
                }
                if (data.data[0]["NitroUsed"] == true && data.data[0]["NitroEnable"] == false) {
                    //  $('#NitroPoints').html(data.data[0]["NitroPoints"] + " bouns points");
                    $('#availablenitro').hide();
                    $('#notavailablenitro').hide();
                    $('#usednitro').show();

                    //buttons 
                    $('#btnusednitro').show();
                    $('#cubtnnitro').hide();
                    $('#tabtoselectbtnnitro').hide();
                   
                }

             //  PAINKILLER
                if (data.data[0]["PainKillerEnable"] == true) {
                    $('#PainKillerPointsshow').html(data.data[0]["PainKillerPoints"] + " bonus points ");
                    $('#availablepk').show();
                    $('#notavailablepain').hide();
                    $('#usedpain').hide();

                    //button
                    $('#tabtoselectbtnpc').show();
                    $('#usedpk').hide();
                    $('#cubtnpk').hide();

                }
                if (data.data[0]["PainKillerEnable"] == false) {
                    //  $('#NitroPoints').html(data.data[0]["NitroPoints"] + " bouns points");
                    $('#availablepk').hide();
                    $('#notavailablepain').show();
                    $('#usedpain').hide();


                    //buttons
                    $('#cubtnpk').show();
                    $('#tabtoselectbtnpc').hide();
                    $('#usedpk').hide();


                }
                if (data.data[0]["PainKillerUsed"] == true && data.data[0]["PainKillerEnable"] == false ) {
                    //  $('#NitroPoints').html(data.data[0]["NitroPoints"] + " bouns points");
                    $('#availablepk').hide();
                    $('#notavailablepain').hide();
                    $('#usedpain').show();

                    //buttons
                    $('#usedpk').show();
                    $('#cubtnpk').hide();
                    $('#tabtoselectbtnpc').hide();

                }

                // ULTRACAPTAIN
                if (data.data[0]["AutoPilotEnable"] == true) {
                    $('#AutoPilotPointsshow').html(data.data[0]["AutoPilotPoints"] + " bonus points ");
                    $('#availableuc').show();
                    $('#notavailableultra').hide();
                    $('#usedultra').hide();

                    //buttons
                    $('#tabtoselectbtnuc').show();
                    $('#usedcu').hide();
                    $('#cubtncu').hide();
                }

                if (data.data[0]["AutoPilotEnable"] == false ) {
                 
                    $('#availableuc').hide();
                    $('#notavailableultra').show();
                    $('#usedultra').hide();

                    //buttons
                    $('#cubtncu').show();
                    $('#tabtoselectbtnuc').hide();
                    $('#usedcu').hide();
                 

                }
                if (data.data[0]["AutoPilotUsed"] == true && data.data[0]["AutoPilotEnable"] == false ) {
                    $('#availableuc').hide();
                    $('#notavailableultra').hide();
                    $('#usedultra').show();

                    //buttons
                    $('#usedcu').show();
                    $('#cubtncu').hide();
                    $('#tabtoselectbtnuc').hide();
                    

                }                
               
            }
            else {

            //  $('#checkmsg').html(data.statusMessage);
            }
        },
        error: function (req, status, error) {
            alert('Opps! something went wrong. Try reload this page.');
            return false;
        }
    });
    $("#power-play-modal").modal('show');

} function TabToSelectNitro(NitroEnable) {
    //alert('dd');
    $('.TabToSelectNitroselected').show();
    $('.TabToSelectNitro').hide();
    $("#NitroEnable").val(NitroEnable);
    NitroEnablevalue = $("#NitroEnable").val()
    PainKillerEnablevalue = $("#PainKillerEnable").val()
    AutoPilotEnablevalue = $("#AutoPilotEnable").val()
    if (PainKillerEnablevalue == 'false' && AutoPilotEnablevalue == 'false') {
      
        $('#savebtn').show();
        $('#disablebtn').hide();
        $('.tabtoselectmessage').show();
        $('.notselectmessage').hide();
    }

}
function TabToUnSelectNitro(NitroEnable) {
 //  alert('dd');
    $('.TabToSelectNitroselected').hide();
    $('.TabToSelectNitro').show();
    $("#NitroEnable").val(NitroEnable);
    NitroEnablevalue = $("#NitroEnable").val()
    PainKillerEnablevalue = $("#PainKillerEnable").val()
    AutoPilotEnablevalue = $("#AutoPilotEnable").val()
    if (PainKillerEnablevalue == 'false' && AutoPilotEnablevalue == 'false') {
       // alert(NitroEnablevalue);
        $('#savebtn').hide();
        $('#disablebtn').show();
    } else {
        $('#savebtn').show();
        $('#disablebtn').hide();

        $('.tabtoselectmessage').show();
        $('.notselectmessage').hide();
    }

}

function TabToSelectPain(PainKillerEnable) {
    //alert('dd');
    $('.TabToSelectPainselected').show();
    $('.TabToSelectPain').hide();
   
    $("#PainKillerEnable").val(PainKillerEnable);
    NitroEnablevalue = $("#NitroEnable").val()
    PainKillerEnablevalue = $("#PainKillerEnable").val()
    AutoPilotEnablevalue = $("#AutoPilotEnable").val()
    if (NitroEnablevalue == 'false' && AutoPilotEnablevalue == 'false') {
     
        $('#savebtn').show();
        $('#disablebtn').hide();
        $('.tabtoselectmessage').show();
        $('.notselectmessage').hide();
    }

}
function TabToUnSelectPain(PainKillerEnable) {
   // alert('dd');
    $('.TabToSelectPainselected').hide();
    $('.TabToSelectPain').show();
    $("#PainKillerEnable").val(PainKillerEnable);
    NitroEnablevalue = $("#NitroEnable").val()
    PainKillerEnablevalue = $("#PainKillerEnable").val()
    AutoPilotEnablevalue = $("#AutoPilotEnable").val()
    if (NitroEnablevalue == 'false' && AutoPilotEnablevalue == 'false') {
     
        $('#savebtn').hide();
        $('#disablebtn').show();
    } else {
        $('#savebtn').show();
        $('#disablebtn').hide();
        $('.tabtoselectmessage').show();
        $('.notselectmessage').hide();
    }

}
function TabToSelectUltra(AutoPilotEnable) {
    //alert('dd');
    $('.TabToSelectUltraselected').show();
    $('.TabToSelectUltra').hide();
    $("#AutoPilotEnable").val(AutoPilotEnable);
    NitroEnablevalue = $("#NitroEnable").val()
    PainKillerEnablevalue = $("#PainKillerEnable").val()
    AutoPilotEnablevalue = $("#AutoPilotEnable").val()
    if (NitroEnablevalue == 'false' && PainKillerEnablevalue == 'false') {
       
        $('#savebtn').show();
        $('#disablebtn').hide();
        $('.tabtoselectmessage').show();
        $('.notselectmessage').hide();
        
    }

}
function TabToUnSelectUltra(AutoPilotEnable) {
 
    $('#disablebtn').show();
    $('.TabToSelectUltraselected').hide();
    $('.TabToSelectUltra').show();
    $("#AutoPilotEnable").val(AutoPilotEnable);
    NitroEnablevalue = $("#NitroEnable").val()
    PainKillerEnablevalue = $("#PainKillerEnable").val()
    AutoPilotEnablevalue = $("#AutoPilotEnable").val()
    if (NitroEnablevalue == 'false' && PainKillerEnablevalue == 'false') {
      
        $('#savebtn').hide();
        $('#disablebtn').show();
    } else {
        $('#savebtn').show();
        $('#disablebtn').hide();
        $('.tabtoselectmessage').show();
        $('.notselectmessage').hide();
    }
}
function UpdatePowerPlay() {
    $('#disablebtn').show();
    $('#savebtn').hide();
   
    var userTeamId = $('#UserteamId').val();
    var tid = sessionStorage.getItem("UserSelectTournament");
    //  if (data.status == "success") {
    NitroUserTeamMatchPointId = $("#NitroUserTeamMatchPointId").val()
    PainKillerUserTeamMatchPointId = $("#PainKillerUserTeamMatchPointId").val()
    AutoPilotUserTeamMatchPointId = $("#AutoPilotUserTeamMatchPointId").val()

    NitroPoints = $("#NitroPoints").val()
    PainKillerPoints = $("#PainKillerPoints").val()
    AutoPilotPoints = $("#AutoPilotPoints").val()

    NitroEnable = $("#NitroEnable").val()
    PainKillerEnable = $("#PainKillerEnable").val()
    AutoPilotEnable = $("#AutoPilotEnable").val()


    var cData = {
        UserTeamId: userTeamId, UserId: Session_usedId, TournamentId: tid,
        NitroUserTeamMatchPointId: NitroUserTeamMatchPointId, PainKillerUserTeamMatchPointId: PainKillerUserTeamMatchPointId, AutoPilotUserTeamMatchPointId: AutoPilotUserTeamMatchPointId,
        NitroSelect: NitroEnable, PainKillerSelect: PainKillerEnable, AutoPilotSelect: AutoPilotEnable,
        NitroPoints: NitroPoints, PainKillerPoints: PainKillerPoints, AutoPilotPoints: AutoPilotPoints
    };
    
    var jsoncData = JSON.stringify(cData);
 //console.log(jsoncData);
  // return;
    var uurl = '/Team/UpdateUserPowerplay';
    $.ajax({
        url: uurl,
        type: 'POST',
        data: jsoncData,
        dataType: 'json',
        async: true,
        cache: false,
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            if (data.status == "success") {
                toastr_success(data.statusMessage);
                location.href = '/Team';
               // $('#disablebtn').hide();
               // $('#savebtn').show();
            }
            else {
                $('#disablebtn').hide();
                $('#savebtn').show();
                toastr_warning(data.statusMessage);
            }
        },
        error: function (req, status, error) {
            alert('Opps! something went wrong. Try reload this page.');
            return false;
        }
    });

}
