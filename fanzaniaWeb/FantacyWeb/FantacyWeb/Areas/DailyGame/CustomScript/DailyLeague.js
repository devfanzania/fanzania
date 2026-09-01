function OnPageLoad() {
    $('#spinnerContainer').show();
    LoadPastMatch();
    GetGunFact()
}

var MatchList = new Array();
var CurrentSelectMatchNo = '';
var grid; var matchType = '';

function LoadPastMatch() {
    var uurl = '/DailyGame/LeagueD/GetAllDailyMatch';
    $.ajax({
        url: uurl,
        type: 'POST',
        async: true,
        cache: false,
        contentType: "application/json; charset=utf-8",

        success: function (data) {
           
            if (data.length > 0) {
                $('#LeagueMsg').hide();
                $('#leagueInfoContainer').show();
                $('#CarouselMatch_left').show();
                $('#CarouselMatch_right').show();
                
                var divs = '';
                var cnt = 0; var flag = 0; var activeclass = ''; var itemCnt = 0; var ActiveSlideNo = 1;
                var slideId = 1;
                var tStatus = $('#TournamentStatus').val();
                $.each(data, function (index, value) {
                    var userteamId = $('#UserteamId').val();
                    itemCnt = itemCnt + 1;
                    var cBox = '';

                    if (index == 0) {
                        activeclass = 'active';
                        //LeagueTeam(value.TournamentId, value.MatchId);
                        LoadTeams(value.TournamentId, value.MatchId);
                        matchType = value.MatchType;
                    }
                    else { activeclass = ''; }

                    if (cnt == 0 && index == 0) {
                        cBox += '<div id="slide_' + slideId + '" class="item">' //active
                        flag = 1; slideId = parseInt(slideId) + 1;
                    } else if (cnt == 0) {
                        cBox += '<div id="slide_' + slideId + '" class="item">'
                        flag = 1; slideId = parseInt(slideId) + 1;
                    }
                    if (cnt < 3) {
                        cBox += '<div class="col-md-4"><a href="#1" onclick="OnChangeMatchSlide(\'' + itemCnt + '\',\'' + value.TournamentId + '\',\'' + value.MatchId + '\',\'' + value.MatchType + '\')";>'
                        cBox += '<div id="match_' + itemCnt + '" class="itemMatch ' + activeclass + '"><div  class="category-icon-item"><img src="/Assets/Icon/tick.png" alt=""/><div class="icon-box">'
                        cBox += '<h5>' + value.Team1ShortName + ' v ' + value.Team2ShortName + '</h5>'
                        cBox += '<p>' + ChageDatFormat((value.MatchScheduledDate).split('T')[0]) + ' | ' + value.Venue + '</p>';
                        
                        cBox += '</div></div></div></a></div>'
                        cnt = cnt + 1;
                    }
                    if (cnt == 3) {
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
                var res = parseInt(ActiveSlideNo) % 3;
                var res1 = (parseInt(ActiveSlideNo) - res) / 3;
                if (res > 0) {
                    slideClick = parseInt(res1) + 1;
                }
                else if (res == 0) {
                    slideClick = parseInt(res1);
                }
                $('#slide_' + slideClick + ' ').addClass("active");
                if (itemCnt < 4) {
                    $('#CarouselMatch_right').css("display", "none");
                    $('#CarouselMatch_left').css("display", "none");
                }
                else {
                    $('#CarouselMatch_right').css("display", "block");
                    $('#CarouselMatch_left').css("display", "block");
                }
            }
            else {
                $('#LeagueMsg').show();
                $('#leagueInfoContainer').hide();
                $('#CarouselMatch_left').hide();
                $('#CarouselMatch_right').hide();

            }
            $('#spinnerContainer').hide();
        },
        error: function (req, status, error) {
            $('#spinnerContainer').hide();
            toastr_warning("Opps! something went wrong. Try reload this page.");
            return false;
        }
    });
}

function OnChangeMatchSlide(itemCnt, TournamentId, MatchId, MatchType) {
    if (itemCnt != 0) {
        var matchcnt = $('#hdnMatchCnt').val();
        var i = 1;
        for (i = 1; i <= matchcnt; i++) {
            $('#match_' + i).removeClass('active');
        }
        $('#match_' + itemCnt).addClass('active');
    }
    matchType = MatchType;
    LoadTeams(TournamentId, MatchId);
    
}

//function LeagueTeam(tid, mid) {
//    LoadTeams(tid, mid);
//    //grid.reload({ page: 1, MatchId: mid, TournamentId: tid });
//}

function FindUserPosition(userPosition, totalteam, points) {
    
    $('#rank').html(userPosition + '<br><span>RANK</span>');
    $('#tTeam').html(totalteam + '<br><span>Total Team</span>');
    $('#tPoints').html(points + '<br><span>My Points</span>');
}

function LoadTeams(tId, mId) {
    $('#spinnerContainer').show();
    if (grid != null) {
        grid.reload({ page: 1, MatchId: mId, TournamentId: tId });
    } else {
        grid = $("#Teamgrid").grid({
            dataSource: { url: '/DailyGame/LeagueD/DailyLeagueTeams', data: { MatchId: mId, TournamentId: tId } },
            notFoundText: "No team available!",
            responsive: true,
            uiLibrary: "bootstrap",
            fixedHeader: true,
            columns: [

                { field: "TeamRank", title: "Rank", width: "10%", align: "center", cssClass: "TeamRank" },
                { field: "", title: "Team", width: "20%", align: "center", cssClass: "UserTeamName" },
                { field: "LastMatchPoints", title: "Total Points", width: "15%", align: "center", cssClass: "TotalPoints" },
                { field: "", title: "", width: "10%", align: "center", cssClass: "teamdetails" }
            ],
        });

        grid.on("rowDataBound", function (e, $row, id, record) {
            if (id == 1) {
                TopperPoint = record.TotalPoints;
            }
            $row.find('.UserTeamName').html('<p class="OwnerTeam">' + record.Name + '</p>');

            var TeamName = record.Name.replace(/\'/g, "");
            $row.find('.teamdetails').html('<i class="fa fa-angle-right an-r" aria-hidden="true" style="cursor: pointer;" onclick="Showteam(' + record.UserTeamId + ',' + record.TournamentId + ',' + record.MatchId + ',\'' + TeamName + '\');"></i>');
            var UType = $("#hdnFlag").val();
            var Lid = $('#hdnLeagueId').val();
            var x = record.FullName;
            var y = UType;
           // $row.css('background-color', 'transparent'); $row.css('color', '#000');
            if (record.OwnerTeam == "True") {
                Showteam(record.UserTeamId, record.TournamentId, record.MatchId, TeamName);
                FindUserPosition(record.TeamRank, record.TotalTeams, record.LastMatchPoints);
                //$row.css('background-color', '#000'); $row.css('color', '#ff8f00');
            }

        });
    }
    $('#spinnerContainer').hide();
}

function Showteam(UtId, tid, mid, teamname) {
    //$('#ModalShowteam').modal('show');
    //$('#Modal_Teamname').html(teamname);
    $('#Players').html('');
    var pData = { TournamentId: tid, UserTeamId: UtId, MatchType: matchType, MatchId: mid };
    var jsonData = JSON.stringify(pData);
    $.ajax({
        url: '/DailyGame/LeagueD/UserDailyTeamPlayersWithPoints',
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
            var allrow = '';
            $.each(data, function (index, value) {
               
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
                   
                    wkrow += '<div class="playerprice">' + value.PlayerPoints + '</div>';
                   
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
                    
                    batrow += '<div class="playerprice">' + value.PlayerPoints + '</div>';
                    
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
                    
                    bwlrow += '<div class="playerprice">' + value.PlayerPoints + '</div>';
                    
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
                    
                    allrow += '<div class="playerprice">' + value.PlayerPoints + '</div>';
                    
                    allrow += '</div>';
                }
            });

            divs += '<div class="playerrowtitle"></div><div class="playerrow">' + wkrow + '</div>';
            divs += '<div class="playerrowtitle"></div><div class="playerrow">' + batrow + '</div>';
            divs += '<div class="playerrowtitle"></div><div class="playerrow">' + bwlrow + '</div>';
            divs += '<div class="playerrowtitle"></div><div class="playerrow">' + allrow + '</div>';

            $('#Players').html(divs);
            //$('#Modal_Teamname').html(teamname);
            //$("#ModalShowteam").modal('show');
        },
        error: function (req, status, error) {
            toastr_warning("Opps! something went wrong. Try reload this page.");
            return false;
        }
    });
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
    var formatddate = arr[2] + ' ' + mon;// + ' ' + arr[0];
    return formatddate;
}

function GetGunFact() {
    var uurl = '/DailyGame/LeagueD/GetGunFact';
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


