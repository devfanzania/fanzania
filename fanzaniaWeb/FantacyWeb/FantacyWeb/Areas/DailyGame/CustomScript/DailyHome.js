$(document).ready(function () {
    $('#spinnerContainer').show();
    GetGunFact();
    GetDailyMyMatch('All');
    sessionStorage.setItem("TournamentStatus", "ALL");
    $('#tourFilter').css('display', 'none');
    $('#spinnerContainer').hide();
});
var MatchListCnt = 0;
function SelectMyMatch() {
    $('#SelectMyMatch').removeClass("blue-btn");
    $('#SelectMyMatch').addClass("red-btn");
    $('#MatchFilter').show();
    GetDailyMyMatch('All');
    $('#SelectJoinMatch').removeClass("red-btn");
    $('#SelectJoinMatch').addClass("blue-btn");
    $('#tourFilter').css('display', 'none');
    $('#MakeYourTeam').hide();
    $('#MyTeamView').show();
    $('#HomeMsg').hide();
}
function SelectJoinMatch() {
    
    $('#SelectJoinMatch').removeClass("blue-btn");
    $('#SelectJoinMatch').addClass("red-btn");
    $('#MatchFilter').hide();
    GetAllDailyMatch();
    $('#SelectMyMatch').removeClass("red-btn");
    $('#SelectMyMatch').addClass("blue-btn");
    $('#tourFilter').css('display', 'block');
    //$('#MakeYourTeam').show();
    $('#MyTeamView').hide();
}

function GetAllDailyMatch() {
    
    $('#spinnerContainer').show();
    var Tstatus = sessionStorage.getItem("TournamentStatus");
    var pData = { TournamentFilter: Tstatus };
    var jsonData = JSON.stringify(pData);
    var uurl = '/DailyGame/HomeD/GetAllDailyMatch';
    $.ajax({
        url: uurl,
        type: 'POST',
        dataType: 'json',
        data: jsonData,
        async: true,
        cache: false,
        contentType: "application/json; charset=utf-8",

        success: function (data) {
            
            if (data.length > 0) {
                var div = '';
                $.each(data, function (index, value) {
                   
                    MatchListCnt = index + 1;
                    if (index == 0) {
                        JoinNewMatch(index, value.Team1ShortName, value.Team2ShortName, value.Team1Image, value.Team2Image, value.TournamentId, value.MatchId, value.MatchDate, value.MatchType, value.MatchScheduledTime, value.Venue, value.Team1, value.Team2);
                    }
                    div += '  <div id="divMatchList_' + index + '" class="grey-box" onclick= "JoinNewMatch(' + index + ',\'' + value.Team1ShortName + '\',\'' + value.Team2ShortName + '\',\'' + value.Team1Image + '\',\'' + value.Team2Image + '\',\'' + value.TournamentId + '\',\'' + value.MatchId + '\', \'' + value.MatchDate + '\', \'' + value.MatchType + '\',\'' + value.MatchScheduledTime + '\',\'' + value.Venue + '\')">';
                    div +='    <div class="col-md-12">';
                    div += '         <p  class="t-name"><small class="text-left">' + value.TournamentName + '</small></p>';
                    div +='         <div class="row"><div class="col-md-2 col-xs-2 text-center">';
                    div += '                 <img class="js-size js-size-left" src="https://www.fanzania.com/FantasyCricketRest/ImagePath/image/cricket/teams/' + value.Team1Image + '" />';
                    div +='             </div><div class="col-md-2 col-xs-2 text-center">';
                    div += '                 <p><strong>' + value.Team1ShortName + '</strong></p>';
                    div += '             </div><div class="col-md-4 col-xs-4 text-center">';
                    if (value.TournamentStatus == 'UPCOMING') {
                        div += '                 <p class="">STARTS IN</p>';
                       
                        var countDownDate = new Date(value.MatchDate).getTime();
                        var now = new Date().getTime();
                        
                        var distance = countDownDate - now;
                        if (distance < 86400000) {
                            if (distance < 0) {
                                //div += '                <span><small class="Upcomming_Time_' + index + '">' + ChageDatFormat((value.MatchDate).split('T')[0]) + '</small></span>';
                                div += '                <span><small class="Upcomming_Time_AM_' + index + '">' + ChageDatFormat(value.MatchDate) + '</small></span>';
                            } else {
                                div += '                <span><small class="Upcomming_Time_AM_' + index + '">' + startTimer('Upcomming_Time_AM_' + index, value.MatchDate) + '</small></span>';
                            }
                        }
                        else {
                            //div += '                <span><small class="Upcomming_Time_' + index + '">' + ChageDatFormat((value.MatchDate).split('T')[0]) + '</small></span>';
                            div += '                <span><small class="Upcomming_Time_AM_' + index + '">' + ChageDatFormat(value.MatchDate) + '</small></span>';
                        }
                    }
                   
                    div +='            </div><div class="col-md-2 col-xs-2 text-center">';
                    div += '                <p><strong>' + value.Team2ShortName + '</strong></p>';
                    div +='              </div><div class="col-md-2 col-xs-2 text-center">';
                    div += '                 <img class="js-size js-size-right" src="https://www.fanzania.com/FantasyCricketRest/ImagePath/image/cricket/teams/' + value.Team2Image + '" />';
                    div += '            </div></div></div></div>';
                });
                $('#DailyMatchList').html(div);
                $('#HomeMsg').hide();
            }
            else {
                $('#DailyMatchList').html('');
                $('#HomeMsg').show();
            }
          
        },
        error: function (req, status, error) {
            toastr_warning("Opps! something went wrong. Try reload this page.");
            return false;
        }
    });
    $('#spinnerContainer').hide();
}


function GetDailyMyMatch(type) {
    $('#spinnerContainer').show();
    var prevMatchId = sessionStorage.getItem("MatchId");
    
    var pData = { FilterType: type };
    var jsonData = JSON.stringify(pData);
    var uurl = '/DailyGame/HomeD/GetDailyMyMatch';
    $.ajax({
        url: uurl,
        type: 'POST',
        data: jsonData,
        dataType: 'json',
        async: true,
        cache: false,
        contentType: "application/json; charset=utf-8",

        success: function (data) {
          //  console.log(data);
            if (data.length > 0) {
                var div = ''; var cssAdd = '';
                $.each(data, function (index, value) {
                    if (prevMatchId != null && prevMatchId == value.MatchId) {
                        
                        MyMatchDetails(index, value.Team1ShortName, value.Team2ShortName, value.Team1Image, value.Team2Image, value.TournamentId, prevMatchId, value.UserTeamId, value.MatchType, value.MatchStatus, value.TeamRank, value.MatchScheduledTime, value.MatchDate, value.Venue, value.Team1, value.Team2);
                        cssAdd = 'active';
                    } else {
                        cssAdd = '';
                        if (index == 0 && prevMatchId == null) {
                            cssAdd = 'active';
                            
                            MyMatchDetails(index, value.Team1ShortName, value.Team2ShortName, value.Team1Image, value.Team2Image, value.TournamentId, value.MatchId, value.UserTeamId, value.MatchType, value.MatchStatus, value.TeamRank, value.MatchScheduledTime, value.MatchDate, value.Venue,value.Team1, value.Team2 );
                        }
                    }
                    MatchListCnt = index + 1;
                    div += '  <div id="divMatchList_' + index + '" class="grey-box ' + cssAdd + '" onclick= "MyMatchDetails(' + index + ',\'' + value.Team1ShortName + '\',\'' + value.Team2ShortName + '\',\'' + value.Team1Image + '\',\'' + value.Team2Image + '\',\'' + value.TournamentId + '\',\'' + value.MatchId + '\',\'' + value.UserTeamId + '\',\'' + value.MatchType + '\',\'' + value.MatchStatus + '\',\'' + value.TeamRank + '\',\'' + value.MatchScheduledTime + '\',\'' + value.MatchDate + '\',\'' + value.Venue + '\',\'' + value.Team1 + '\',\'' + value.Team2 + '\')">';
                    div += '    <div class="col-md-12">';
                    div += '         <p class="t-name"><small class="text-left">' + value.TournamentName + '</small></p>';
                    div += '         <div class="row"><div class="col-md-2 col-xs-2 text-center">';
                    div += '                 <img class="js-size js-size-left" src="https://www.fanzania.com/FantasyCricketRest/ImagePath/image/cricket/teams/' + value.Team1Image + '" />';
                    div += '             </div><div class="col-md-2 col-xs-2 text-center">';
                    div += '                 <p><strong>' + value.Team1ShortName + '</strong></p>';
                    div += '             </div><div class="col-md-4 col-xs-4 text-center">';
                    if (value.MatchStatus == 'UPCOMING') {
                        div += '                 <p class="">STARTS IN</p>';
                        var countDownDate = new Date(value.MatchDate).getTime();
                        var now = new Date().getTime();
                       
                        var distance = countDownDate - now;
                        if (distance < 86400000) {
                            if (distance < 0) {
                                //div += '                <span><small class="Upcomming_Time_' + index + '">' + ChageDatFormat((value.MatchDate).split('T')[0]) + '</small></span>';
                                div += '                <span><small class="Upcomming_Time_MM_' + index + '">' + ChageDatFormat(value.MatchDate) + '</small></span>';
                            } else {
                                div += '                <span><small class="Upcomming_Time_MM_' + index + '">' + startTimer('Upcomming_Time_MM_' + index, value.MatchDate) + '</small></span>';
                            }
                        }
                        else {
                            //div += '                <span><small class="Upcomming_Time_' + index + '">' + ChageDatFormat((value.MatchDate).split('T')[0]) + '</small></span>';
                            div += '                <span><small class="Upcomming_Time_MM_' + index + '">' + ChageDatFormat(value.MatchDate) + '</small></span>';
                        }
                    }
                    else if (value.MatchStatus == 'Live') {
                        div += '                 <p  class=""><i class="fa fa-circle"></i>  ' + value.MatchStatus + '</p>';
                        //div += '                <span><small> You have ' + value.TotalPoints + ' pts</small></span>';
                    }
                    else if (value.MatchStatus == 'COMPLETE') {
                        div += '                 <p class="">' + value.MatchStatus + '</p>';
                        div += '                <p><small> ' + ChageDatFormat(value.MatchDate) + ' </small></p>';
                        div += '                <span><small> You got ' + value.TotalPoints + ' pts</small></span>';
                    }
                    div += '            </div><div class="col-md-2 col-xs-2 text-center">';
                    div += '                <p><strong>' + value.Team2ShortName + '</strong></p>';
                    div += '              </div><div class="col-md-2 col-xs-2 text-center">';
                    div += '                 <img class="js-size js-size-right" src="https://www.fanzania.com/FantasyCricketRest/ImagePath/image/cricket/teams/' + value.Team2Image + '" />';
                    div += '            </div></div></div></div>';
                });
                $('#DailyMatchList').html(div);

            }
            else {
                if (type == 'All') {
                    SelectJoinMatch();
                }
                else {
                    $('#DailyMatchList').html('<div class="grey-box"><div class="col-md-12 col-xs-12 text-center"><p class="">No Match Available</p></div></div>');
                    $('#Players').html('');
                }
            }
           
        },
        error: function (req, status, error) {
            toastr_warning("Opps! something went wrong. Try reload this page.");
            return false;
        }
    });
    $('#spinnerContainer').hide();
}

function FilterMatch(type) {
    $('#MyTeamDetail').html('');
    $('#MatchFilter div').removeClass('active');
    $('#' + type).addClass('active');
    GetDailyMyMatch(type);
}

function JoinNewMatch(index, Team1, Team2, Team1Img, Team2Img, tournamentId, matchId, MatchDate, MatchType, MatchScheduledTime, matchVenue, teamfull1, teamfull2) {
    
    $('#MakeYourTeam').show();
    for (var i = 0; i < MatchListCnt; i++) {
        $('#divMatchList_' + i).removeClass('active');
    }
    $('#divMatchList_' + index).addClass('active');

    sessionStorage.setItem('MatchDetail', Team1 + '|' + Team2 + '|' + MatchDate + '|' + matchVenue + '|' + teamfull1 + '|' + teamfull2);
    $('#team1').html(Team1 + '<img class="imgLeft" src="https://www.fanzania.com/FantasyCricketRest/ImagePath/image/cricket/teams/' + Team1Img + '" alt="" height="20px" width="40px" />')
    $('#team2').html('<img class="imgRight" src="https://www.fanzania.com/FantasyCricketRest/ImagePath/image/cricket/teams/' + Team2Img + '" alt="" height="20px" width="40px" />' + Team2);
    
    $("#StartsIn").html('');
    $("#StartsIn").html('<span class="StartsIn' + index + '">' + MatchScheduledTime + '</span>');
    
    var strhtml = '<a class="orange-btn" onclick="redirectToManageTeam(\'new\',\'' + tournamentId + '\',\'' + matchId + '\',\'' + MatchType + '\')">MAKE YOUR TEAM</a>';
    $('#makeYourTeam').html(strhtml);

    var pData = { TournamentId: tournamentId, MatchId: matchId };
    var jsonData = JSON.stringify(pData);
    var uurl = '/DailyGame/HomeD/DailyLeagueTeams';
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
                $('#TotalTeam').html(data[0]["TotalTeams"]);
            }
            else {
                $('#TotalTeam').html('0');
            }
            
        },
        error: function (req, status, error) {
            toastr_warning("Opps! something went wrong. Try reload this page.");
            return false;
        }
    });
}

function redirectToManageTeam(OpType,Tid, MId, MType) {
    
    //location.href = '/DailyGame/TeamD/ManageTeam?type=new&tid=' + Tid + '&mtype=' + MType + '&mid=' + MId;

    var pData = { TournamentId: Tid, MatchId: MId, MatchType: MType, FilterType: OpType };
    var jsonData = JSON.stringify(pData);
    $.ajax({
        url: '/TeamD/RedirectToManageTeam',
        type: 'POST',
        data: jsonData,
        dataType: 'json',
        async: true,
        cache: false,
        contentType: "application/json; charset=utf-8",

        success: function (data) {
            
            window.location.href = data.Url;
        },
        error: function (req, status, error) {
            
            toastr_warning("Opps! something went wrong. Try reload this page.");
            return false;
        }
    });
}

function MyMatchDetails(index, Team1, Team2, Team1Img, Team2Img, tId, mId, utId, mType, mStatus, rank, MatchScheduledTime, MatchDate, MatchVenue, teamfull1, teamfull2) {
   
    $('#spinnerContainer').show();
   for (var i = 0; i < MatchListCnt; i++) {
        $('#divMatchList_' + i).removeClass('active');
    }
   $('#divMatchList_' + index).addClass('active');

   var pData = { TournamentId: tId, UserTeamId: utId, MatchType: mType, MatchId: mId };
   var jsonData = JSON.stringify(pData);
   var url = '';
   if (mStatus == 'COMPLETE') {
       url = '/DailyGame/HomeD/UserDailyTeamPlayersWithPoints';
   } else {
       url = '/DailyGame/HomeD/UserDailyTeamPlayers';
      
   }
    sessionStorage.setItem('MatchDetail', Team1 + '|' + Team2 + '|' + MatchDate + '|' + MatchVenue + '|' + teamfull1 + '|' + teamfull2);
    //console.log("settttt",sessionStorage.getItem('MatchDetail'));
   $.ajax({
       url: url,
       type: 'POST',
       data: jsonData,
       dataType: 'json',
       async: true,
       cache: false,
       contentType: "application/json; charset=utf-8",

       success: function (data) {
          // console.log(data);
           var Player_cnt = 0;
           var divs = '';
           var wkrow = '';
           var batrow = '';
           var bwlrow = '';
           var allrow = ''; var TeamPoints = '';
           var strPanel = '';
           var managebtn = '';
           $.each(data, function (index, value) {
               if (index == 0) {
                   if (mStatus == 'Live') {
                       strPanel += '<li class="text-uppercase">' + value.TotalTeams + '<br /><span>Total Teams</span></li>';
                   
                     //  strPanel += '<li class="text-uppercase">' + value.WinnerPrediction + '<br /><span>Winner Prediction</span></li>';
                       if (value.WinnerPrediction == null) {
                           strPanel += '<li class="text-uppercase"> - <br /><span>Winner Prediction</span></li>';

                       } else {
                           strPanel += '<li class="text-uppercase">' + value.WinnerPrediction + '<br /><span>Winner Prediction</span></li>';

                       }
                       strPanel += '<li class="text-uppercase"><a href="/DailyGame/LiveScoreD">LIVE SCORE</a></li>';
                   }
                   else if (mStatus == 'COMPLETE') {
                       strPanel += '<li class="text-uppercase">' + value.TotalTeams + '<br /><span>Total Teams</span></li>';
                       strPanel += '<li class="text-uppercase">' + rank + '<br /><span>RANK</span></li>';
                       
                   }
                   else if (mStatus == 'UPCOMING') {
                       
                       strPanel += '<li class="text-uppercase">' + value.TotalTeams + '<br /><span>Total Teams</span></li>';
                       strPanel += '<li class="text-uppercase">' + MatchScheduledTime + '<br /><span>Start Time</span></li>';
                       if (value.WinnerPrediction == null) {
                           strPanel += '<li class="text-uppercase"> - <br /><span>Winner Prediction</span></li>';

                       } else {
                           strPanel += '<li class="text-uppercase">' + value.WinnerPrediction + '<br /><span>Winner Prediction</span></li>';

                       }
                       //strPanel += '<li class="text-uppercase"><a href="/DailyGame/TeamD/ManageTeam?type=edit&tid=' + tId + '&mtype=' + mType + '&mid=' + mId + '"><i class="fa fa-gear"></i>Manage</a></li>';
                       //strPanel += '<li class="text-uppercase"><a onclick="redirectToManageTeam(\'edit\',\'' + tId + '\',\'' + mId + '\', \'' + mType + '\');"><i class="fa fa-gear"></i>Manage</a></li>';
                       managebtn = '<a style="padding: 8px 18px 8px 8px;background: #000; border-radius: 30px;color: #fff; vertical-align: super;cursor: pointer;font-size: 12px;font-weight: 700;line-height: 18px;text-transform: uppercase;" onclick="redirectToManageTeam(\'edit\',\'' + tId + '\',\'' + mId + '\', \'' + mType + '\');"><i class="fa fa-gear" style=" padding-right: 5px;color: #fff;font-size: 16px;"></i>Manage</a>';

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
                   if (mStatus == 'COMPLETE') {
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
                   if (mStatus == 'COMPLETE') {
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
                   if (mStatus == 'COMPLETE') {
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
                   if (mStatus == 'COMPLETE') {
                       allrow += '<div class="playerprice">' + value.PlayerPoints + '</div>';
                   } else {
                       allrow += '<div class="playerprice">' + pPrice + ' K</div>';
                   }
                   allrow += '</div>';
               }
           });
           
           divs += '<div class="playerrow">' + wkrow + '</div>';
           divs += '<div class="playerrow">' + batrow + '</div>';
           divs += '<div class="playerrow">' + bwlrow + '</div>';
           divs += '<div class="playerrow">' + allrow + '</div>';
                     
           //$('#Tp_heading').html(tp_head);
           $('#ManageButton').html(managebtn);  
           $('#Players').html(divs);
           $('#MyTeamDetail').html(strPanel);
           //$('#teampoint').html(TeamPoints);
           //$('#powerplay').html(PowerPlay);
           //$('#spinnerContainer').hide();
       },
       error: function (req, status, error) {
           $('#spinnerContainer').hide();
           toastr_warning("Opps! something went wrong. Try reload this page.");
           return false;
       }
   });
   $('#spinnerContainer').hide();
}

function OpenTournamentFilterModal() {
    LoadTournamentforfilter();
    $("#ModalTournametFilter").modal('show');
}
function LoadTournamentforfilter() {
    var uurl = '/DailyGame/HomeD/DailyTournamentList';
    
    $.ajax({
        url: uurl,
        type: 'POST',
        dataType: 'json',
        async: true,
        cache: false,
        contentType: "application/json; charset=utf-8",

        success: function (data) {
            if (data.length > 0) {
                $('#filterTournament').empty();
                $.each(data, function (index, value) {
                    $('#filterTournament').append($('<div id="ck-button-filter"><label><input type="checkbox" value=' + value.TournamentName + ' name=' + value.TournamentName + ' /><span>' + value.TournamentName + '</span></label></div>'));
                });
            }
        },
        error: function (req, status, error) {
            toastr_warning("Opps! something went wrong. Try reload this page.");
            return false;
        }
    });
}
function ClearFilter() {
    $("#filterTournament input").each(function () {
        $this = $(this);
        if ($this.prop("checked")) {
            $this.prop("checked", false);
        }
    });
    $('#filter_team').removeClass('filter_team');
    var Tid = Session_TournamentId;
    sessionStorage.setItem("TournamentStatus", "ALL");
    GetAllDailyMatch();
}

function FilterTournament() {

    //$('#spinnerContainer').show();
    var $checkboxes = $('#filterTournament input');
    var checked_flag = 0;
    var filters = [];
    $checkboxes.filter(':checked').each(function () {
        filters.push(this.value);
        checked_flag = 1;
    });
    filters = filters.join(',');
    if (checked_flag == 1) {
        $('#filter_team').addClass('filter_team');
    }
    else {
        $('#filter_team').removeClass('filter_team');
    }
    sessionStorage.setItem("TournamentStatus", filters);
    GetAllDailyMatch();
    $("#ModalTournametFilter").modal('hide');
    //$('#spinnerContainer').hide();
}


// Set the date we're counting down to
function startTimer(idNo, matchDate) {
  
    var countDownDate = new Date(matchDate).getTime();
   
    // Update the count down every 1 second
    var x = setInterval(function () {

        // Get todays date and time
        var now = new Date().getTime();
        
        // Find the distance between now and the count down date
        var distance = countDownDate - now;
        if (distance > 0) {
            //if (distance < 86400000) {
                // Time calculations for days, hours, minutes and seconds
                var days = Math.floor(distance / (1000 * 60 * 60 * 24));
                var hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
                var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
                var seconds = Math.floor((distance % (1000 * 60)) / 1000);
            //console.log(idNo + "-" + days + " d- " + hours + "h : " + minutes + "m : " + seconds + "s ")
                // Display the result in the element with id="demo"
                if (days > 0) {
                    $("." + idNo).html(days + " d- " + hours + "h : " + minutes + "m : " + seconds + "s ");
                }
                else {
                    $("." + idNo).html(hours + "h : " + minutes + "m : " + seconds + "s ");
                }
            //}
            //else
            //{
            //   // $("#" + idNo).html(ChageDatFormat(matchDate.split('T')[0]));
            //}

        }
        else {
            //$("#" + idNo).html(ChageDatFormat(matchDate.split('T')[0]));
        }

    }, 1000);
}

function ChageDatFormat(date) {
    
    var localDate = new Date(date).toDateString();
    var arr = localDate.split(' ');
    var formatddate = arr[2] + ' ' + arr[1] + ' ' + arr[3];
    return formatddate;

    //var arr = date.split('-');
    //var months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
    //var i = 1;
    //var mon = '';
    //for (i=1; i <= months.length; i++) {
    //    if (i == parseInt(arr[1])) {
    //        mon = months[i-1];
    //        break;
    //    }
    //}
    //var formatddate = arr[2] + ' ' + mon + ' ' + arr[0];
    //return formatddate;
}

function GetGunFact() {
    var uurl = '/DailyGame/HomeD/GetGunFact';
    $.ajax({
        url: uurl,
        type: 'POST',
        async: true,
        cache: false,
        contentType: "application/json; charset=utf-8",

        success: function (data) {
            if (data.length > 0) {
                $('#displayMsg').html(data[0].FunMessage);
            }
        },
        error: function (req, status, error) {
            $('#spinnerContainer').hide();
            toastr_warning("Opps! something went wrong. Try reload this page.");
            return false;
        }
    });
}
