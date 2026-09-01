function PageLoad(Tid) {
    $('#spinnerContainer').show();
    Load_GlobalTopTenPlayers(Tid);
    Load_GlobalTopLeagues(Tid);
    Load_GlobalTopTeams(Tid);
    $('#spinnerContainer').hide();
}

$.fn.responsiveTabs = function () {
    var container = this;
    container
        .on('show.bs.collapse', '.panel-collapse', function () {
            $(this).addClass('active')
                .siblings('.panel-collapse').removeClass('active').collapse('hide');
            container.find('.nav-tabs a[href="#' + $(this).attr('id') + '"]').parent().addClass('active')
                .siblings().removeClass('active');
        })
        .on('show.bs.tab', '.nav-tabs a', function () {
            $($(this).attr('href')).addClass('in')
                .siblings('.tab-pane').removeClass('in');
        });
};

var Grid_Global_Top_Ten_Players;
function Load_GlobalTopTenPlayers(TId) {

    Grid_Global_Top_Ten_Players = $("#Global_Top_Ten_Players").grid({
        dataSource: { url: '/Dashboard/GlobalTopPlayers', data: { TournamentId: TId } },
        notFoundText: "No data available!",
        responsive: true,
        uiLibrary: "bootstrap",
        fixedHeader: true,
        height: 500,
        minWidth: 600,
        columns: [

            { field: "PlayerRank", title: "Rank", width: "10%", align: "center", cssClass: "PlayerRank" },
            { field: "PlayerName", title: "Name", width: "30%", align: "left", cssClass: "PlayerName" },
            { field: "ParticipationTeamName", title: "Team", width: "30%", align: "left", cssClass: "ParticipationTeamName" },
            { field: "TotalPoints", title: "Points", width: "30%", align: "center", cssClass: "TotalPoints" }
        ],
    });
}

var Grid_GlobalTopLeagues;
function Load_GlobalTopLeagues(TId) {

    Grid_GlobalTopLeagues = $("#Global_Top_Leagues").grid({
        dataSource: { url: '/Dashboard/GlobalTopLeagues', data: { TournamentId: TId } },
        notFoundText: "No data available!",
        responsive: true,
        uiLibrary: "bootstrap",
        fixedHeader: true,
        height: 500,
        minWidth: 600,
        columns: [

            { field: "LeagueRank", title: "Rank", width: "10%", align: "center", cssClass: "LeagueRank" },
            { field: "LeagueName", title: "League", width: "30%", align: "center", cssClass: "LeagueName" },
            { field: "LeagueOwner", title: "Owner", width: "30%", align: "center", cssClass: "LeagueOwner" },
            { field: "LeaguePoints", title: "Points", width: "30%", align: "center", cssClass: "LeaguePoints" }
        ],
    });

}

var Grid_GlobalTopTeams;
function Load_GlobalTopTeams(TId) {

    Grid_GlobalTopTeams = $("#Global_Top_Teams").grid({
        dataSource: { url: '/Dashboard/GlobalTopTeams', data: { TournamentId: TId } },
        notFoundText: "No data available!",
        responsive: true,
        uiLibrary: "bootstrap",
        fixedHeader: true,
        height: 500,
        minWidth: 600,
        columns: [

            { field: "TeamRank", title: "Rank", width: "10%", align: "center", cssClass: "TeamRank" },
            { field: "", title: "Team", width: "30%", align: "center", cssClass: "UserTeamName" },
            //{ field: "", title: "Owner", width: "30%", align: "center", cssClass: "Owner" },
            { field: "TotalPoints", title: "Points", width: "30%", align: "center", cssClass: "TotalPoints" },
            { field: "", title: "", width: "10%", align: "center", cssClass: "teamdetails" }
        ],
    });
    Grid_GlobalTopTeams.on("rowDataBound", function (e, $row, id, record) {
        if (id == 1) {
            LoadTeamPlayer(record.UserTeamId, record.MatchId, TId);
        }
        $row.find('.teamdetails').html('<i class="fa fa-angle-right an-r grid-right" aria-hidden="true" onclick="LoadTeamPlayer(' + record.UserTeamId + ',' + record.MatchId + ',' + TId + ');"></i>');
        $row.find('.UserTeamName').html('<p class="OwnerTeam">' + record.UserTeamName + '</p><p class="OwnerName">' + record.Owner + '</p>');

    });

}

function LoadTeamPlayer(UtId, MatchId, tid) {
    $('#spinnerContainer').show();
    $('#Players').html('');
    var manageteam = ''; var teamstat = '';
    var uurl = '/Dashboard/TeamPlayerInfoCompleteMatch';
    var pData = { TournamentId: tid, UserTeamId: UtId, MatchId: MatchId };
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
            var allrow = ''; var nitroHtml = ''; var ppHtml = ''; var autocapHtml = '';
            $.each(data, function (index, value) {
                console.log(data);
                if (index == 0) {

                    $('#transferLeft').html(value.SubsLeft + '<br><span>Transfers</span>');
                    $('#lastMatchPoint').html(value.MatchTotalPoints + '<br><span><span style="text-transform: none;">' + value.LastMatchTeams + '</span> Points</span>');
                    if (value.NitroLeft > 0)
                    {
                        nitroHtml = '<img data-toggle="tooltip" class="pp" title="Nitro" src="/Assets/Icon/Nitro_red.png" />';
                    }
                    else {
                            if (value.NitroMultiplier == 0){
                                nitroHtml = '<img data-toggle="tooltip" class="pp" title="Nitro" src="/Assets/Icon/Nitro_grey.png" />';
                            }
                            else{
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
                        autocapHtml = '<img data-toggle="tooltip" class="pp" title="Auto Captain" src="/Assets/Icon/Autocaptain_red.png" />';
                    }
                    else {
                        if (value.AutoPilotUsed == 'False') {
                            autocapHtml = '<img data-toggle="tooltip" class="pp" title="Auto Captain" src="/Assets/Icon/Autocaptain_grey.png" />';
                        }
                        else {
                            autocapHtml = '<img data-toggle="tooltip" class="pp" title="Auto Captain" src="/Assets/Icon/Autocaptain_green.png" />';
                        }
                    }
                   
                    
                    $('#powerPlay').html(nitroHtml + ppHtml + autocapHtml + '<br><span>Power Plays</span>');
                    if (value.WinnerPredictionStatus == true) {
                        $('#winnerPrediction').html('<span>' + value.WinnerPrediction + '&nbsp;&nbsp;</span><i class="fa fa-check" aria-hidden="true" style="font-size: 14px;color: #17a517;"></i><br><span>Prediction</span>');
                    } else {
                        $('#winnerPrediction').html('<span>' + value.WinnerPrediction + '&nbsp;&nbsp;</span><i class="fa fa-times" aria-hidden="true" style="font-size: 14px;color: #fb5036;"></i><br><span>Prediction</span>');
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
            
            divs += '<div class="playerrow">' + wkrow + '</div>';
            divs += '<div class="playerrow">' + batrow + '</div>';
            divs += '<div class="playerrow">' + bwlrow + '</div>';
            divs += '<div class="playerrow">' + allrow + '</div>';
            
            $('#Players').html(divs);
            $('#spinnerContainer').hide();
        },
        error: function (req, status, error) {
            $('#spinnerContainer').hide();
            toastr_warning("Opps! something went wrong. Try reload this page.");
            return false;
        }
    });
}