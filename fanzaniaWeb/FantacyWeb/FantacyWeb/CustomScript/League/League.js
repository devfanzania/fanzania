function OnPageLoad() {
    $('#spinnerContainer').show();
    var tid = sessionStorage.getItem("UserSelectTournament");
    LoadUserTournament(tid);

    if(Session_LeagueId != 0 && Session_TournamentId != 0){
        LoadTeams(Session_LeagueId, Session_TournamentId);
    }
}
$('a[data-toggle="tooltip"]').tooltip({
    animated: 'fade',
    placement: 'bottom',
});
var TournementList = new Array();
var CurrentSelectTournamentNo = '';
var TName = '';var TStatus = '';
var preUrl = ''; var SelectedLeagueId ='';
var Session_UserId='';
var grid; var userPosition = ''; var userPointGap = ''; var TopperPoint = ''; var userPoint = '';

function FindUserPosition(TopperPoint, userPoint, userPosition) {
    var userPointGap = (parseInt(TopperPoint) - parseInt(userPoint));
    var memberCount = grid.count();
    if (userPointGap == 0 && userPosition == 0) {
        $('#userPositionDiv').html('Your points will be posted after you have played your first match.');
    } else if (userPointGap == 0 && userPosition == 1) {
       
   
     //   $('#userPositionDiv').html('You are in the 1st position');
        $('#userPositionDiv').html('Your Rank is 1st out of ' + memberCount + ' members.');
       
    } else if (userPosition == 2) {
      //  $('#userPositionDiv').html('You are in the 2nd position, ' + userPointGap + '  points behind the topper.');
        $('#userPositionDiv').html('Your Rank is 2nd out of ' + memberCount + ' members, ' + userPointGap + '  points behind the topper.');
    } else if (userPosition == 3) {
      //  $('#userPositionDiv').html('You are in the 3rd position, ' + userPointGap + '  points behind the topper.');
        $('#userPositionDiv').html('Your Rank is 3rd out of ' + memberCount + ' members, ' + userPointGap + '  points behind the topper.');
    } else {
     //   $('#userPositionDiv').html('You are in the ' + userPosition + 'th position, ' + userPointGap + '  points behind the topper.');
        $('#userPositionDiv').html('Your Rank is ' + userPosition + 'th out of ' + memberCount + ' members, ' + userPointGap + '  points behind the topper.');
    }
}

function LoadTeams(LId,TId) {
    grid = $("#Teamgrid").grid({
        dataSource: { url: '/League/GetLeagueTeams', data: { LeagueiD: LId, TournamentId: TId } },
        notFoundText: "No team available!",
        responsive: true,
        uiLibrary: "bootstrap",
        fixedHeader: true,
        height: 350,
        minWidth :800,
        columns: [
            
            { field: "", title: "Rank", width: "10%", align: "left", cssClass: "Teamleaguerank" },
            { field: "", title: "Team", width: "20%", align: "left", cssClass: "UserTeamName" },
            { field: "", title: "Transfers", width: "15%", align: "center", cssClass: "SubsLeft" },
            { field: "TotalPoints", title: "Total Points", width: "15%", align: "center", cssClass: "TotalPoints" },
            { field: "LastMatchPoints", title: "Last Match", width: "15%", align: "center", cssClass: "LastMatch" },
            { field: "", title: "Action", width: "15%", align: "center", cssClass: "Action" },
            { field: "", title: "", width: "10%", align: "center", cssClass: "teamdetails" }
            ],
    });

    grid.on("rowDataBound", function (e, $row, id, record) {
        $row.css('background-color', '#CCCCCC');
     
     //   console.log(record);
      // return;
        var image = '';
        var temimage = '';
        if (record.UserTier == 1) {
            image += '';
           // image += '<i class="fa fa-circle color1" style="font-size: 17px; margin-right: 8px; color:#f2830b;"></i>';
            image += '<img src="/Assets/Icon/B.png" alt=""  height="35px" width="50px"/>';
        } else if (record.UserTier == 2) {
            image += '<img src="/Assets/Icon/S.png" alt=""    height="35px" width="50px"  />';
          //  image += '<i class="fa fa-circle color2" style="font-size: 17px; color: #ccc;"></i>';
         //   image += '<img src="/Assets/Icon/SILVERT.png" alt="" height="15px" width="20px" style=" margin-right: 8px;" />';
        } else if (record.UserTier == 3) {
            image += '<img src="/Assets/Icon/G.png" alt=""  height="35px" width="50px"/>';
          //  image += '<i class="fa fa-circle color3" style="font-size: 17px;  color: #e4b006;"></i>';
         //   image += '<img src="/Assets/Icon/GOLDT.png" alt="" height="15px" width="20px"  style=" margin-right: 8px;"/>';
        } else if (record.UserTier == 4) {
            image += '<img src="/Assets/Icon/P.png" alt=""   height="35px" width="50px" />';
          //  image += '<i class="fa fa-circle color4" style="font-size: 17px;  color: #897777;"></i>';
            //image += '<img src="/Assets/Icon/PLATINUMT.png" alt="" height="15px" width="20px" style=" margin-right: 8px;" />';
        }
        if (id == 1) {
            TopperPoint = record.TotalPoints;
        }
        if (record.SupportedTeam != null) {
            temimage += '<img src="https://www.fanzania.com/FantasyCricketRest/ImagePath/image/cricket/teams/' + record.SupportedTeam + '" alt="" height="25px" width="35px" />';
        }
        $row.find('.UserTeamName').html('<div><div class=" row" ><div  class="col-lg-8 OwnerTeam" >' + record.UserTeamName + '</div><div  class="col-lg-4" style="text-align: center;" >' + temimage + '</div></div><div  class=" row" ><div  class="col-lg-8 OwnerTeam" ><small>' + record.FullName + '</small></div><div  class="col-lg-4"  style="text-align: center;">' + image  + '</div></div>' );
        if(TStatus == 'INPROGRESS'){
            $row.find('.SubsLeft').html('<span>' + record.SubsLeft + '</span>');
        }
        else{
            $row.find('.SubsLeft').html('<img src="/Assets/Icon/infinity.png" alt="" height="15px" width="20px" />');
        }
     
        if (parseInt(record.TeamCurrentStanding) == 0) {
            $row.find('.Teamleaguerank').html('<i class="fa fa-minus"></i>');
        }
        else if (parseInt(record.TeamCurrentStanding) < parseInt(record.TeamOldStanding) && parseInt(record.TeamOldStanding) > 0 && parseInt(record.TeamCurrentStanding) > 0) {
            $row.find('.Teamleaguerank').html('<i class="green-arrow fa fa-caret-up"></i><small>' + record.TeamCurrentStanding + '</small><small>(+' + (parseInt(record.TeamOldStanding) - parseInt(record.TeamCurrentStanding)) + ')</small>');
        }
        else if (parseInt(record.TeamCurrentStanding) > parseInt(record.TeamOldStanding) && parseInt(record.TeamOldStanding) > 0 && parseInt(record.TeamCurrentStanding) > 0) {
            $row.find('.Teamleaguerank').html('<i class="red-arrow fa fa-caret-down"></i><small>' + record.TeamCurrentStanding + '</small><small>(-' + (parseInt(record.TeamCurrentStanding) - parseInt(record.TeamOldStanding)) + ')</small>');
        }
        else {
            $row.find('.Teamleaguerank').html('<i class="grey-circle fa fa-circle"></i><small>' + record.TeamCurrentStanding + '</small><small></small>');
        }
        var TeamName = record.UserTeamName.replace(/\'/g,"");
        $row.find('.teamdetails').html('<i class="fa fa-angle-right an-r" aria-hidden="true" style="cursor: pointer;" onclick="Showteam(' + record.UserTeamId + ',\'' + TeamName + '\');"></i>');
        var UType =$("#hdnFlag").val();
        var Lid = $('#hdnLeagueId').val();
        var x = record.FullName;
        var y = UType;
        $row.css('background-color',  'transparent');$row.css('color',  '#000');
        if(UType == 'owner'){
            if(record.IsLeagueLeader == 'Y')
            {
                $row.find('.Action').html('<img src="/Assets/Icon/Admin.png" class="admin"/>');
                if (record.UserId == Session_UserId) {
                    userPoint = record.TotalPoints;
                    userPosition = record.TeamCurrentStanding;
                    FindUserPosition(TopperPoint, userPoint, userPosition);
                    $row.css('background-color',  '#eeeeee');$row.css('color',  '#ff8f00');
                }else{
                }
            }else if(record.Status == 'Approved')
            {
                $row.find('.Action').html('<a href="" title="Remove from list" onclick="ActionAdmin(\'' + 'R' + '\',\'' + record.UserLeagueId + '\',\'' + record.UserId + '\',\'' + SelectedLeagueId + '\',\'' + TId + '\')";><img src="/Assets/Icon/cancel.png" alt="" height="18px" width="18px" style="margin-left:5px;" /></a>');
            }
            else if(record.Status == 'Pending')
            {
                $row.find('.Action').html('<a href="" title="Approve User" onclick="ActionAdmin(\'' + 'A' + '\',\'' + record.UserLeagueId + '\',\'' + record.UserId + '\',\'' + SelectedLeagueId + '\',\'' + TId + '\')";><img src="/Assets/Icon/approve.png" alt="" height="18px" width="18px" style="margin-right:5px;" /></a><a href="" title="Reject User" onclick="ActionAdmin(\'' + 'R' + '\',\'' + record.UserLeagueId + '\',\'' + record.UserId + '\',\'' + SelectedLeagueId + '\',\'' + TId + '\')"><img src="/Assets/Icon/cancel.png" alt="" height="18px" width="18px" style="margin-left:5px;" /></a>');
            }
        }
        else{
            if (record.UserId == Session_UserId) {
                userPoint = record.TotalPoints;
                userPosition = record.TeamCurrentStanding;
                FindUserPosition(TopperPoint, userPoint, userPosition);
                $row.css('background-color',  '#eeeeee');$row.css('color',  '#ff8f00');
                if (record.IsLeagueLeader == 'Y') {
                    $row.find('.Action').html('<img src="/Assets/Icon/Admin.png" class="admin"/>');
                }else{
                    $row.find('.Action').html('<i class="fa fa-sign-out" aria-hidden="true" title="Exit League"  onclick="ExitLeague(\'' + Lid + '\')"; style="cursor:pointer;"></i>');
                }
            }else{
                if(record.IsLeagueLeader == 'Y'){
                    $row.find('.Action').html('<img src="/Assets/Icon/Admin.png" class="admin"/>');
                } else {
                    $row.find('.Action').html('<i class="fa fa-minus" aria-hidden="true"></i>');
                }
            }
        }
    });
}

function TeamViewTab(tabType) {
    var Url = '';
    var tId = $('#hdnModalTourmentId').val();
    var uTid = $('#hdnModalUserTeamId').val();
    var teamName = $('#hdnModalTeamName').val();
    var pData = '';
    if (tabType == 'LMP') {
        Url = '/League/ShowLastPlayedTeam';
        pData = { TournamentId: tId, UserTeamId: uTid, MatchId:'0' };
    } else if (tabType == 'CT') {
        pData = { TournamentId: tId, UserTeamId: uTid };
        Url = '/League/ShowCurrentTeam';
    }
    LoadPlayersOnGround(uTid, tId, teamName, Url, pData, tabType);
}

function Showteam(uTid, teamName) {
    var Tid = $('#TournamentId').val();
    $('#hdnModalTourmentId').val(Tid);
    $('#hdnModalUserTeamId').val(uTid);
    $('#hdnModalTeamName').val(teamName);
    var url = '/League/ShowCurrentTeam';
    var pData = { TournamentId: Tid, UserTeamId: uTid };
    $('#tabLMP').removeClass('active');
    $('#tabCT').addClass('active');
    $('#menu1').removeClass('in active');
    $('#menu2').addClass('in active');
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
            var Player_cnt = 0;
            var divs = '';
            var wkrow = '';
            var batrow = '';
            var bwlrow = '';
            var allrow = '';
            var winnerpre = '';
            var lastwinn = '';
            var winpred = '';
            if (data.length > 0) {
              //  console.log(data);
                $.each(data, function (index, value) {
                    if (index == 0) {
                        if (tabType == 'LMP') {
                            if (TStatus == 'INPROGRESS') {
                                $('#modalLastMatchPoint').html('0');
                                $('#modalMatchTeams').html(' ');
                            }
                            else {
                                $('#modalLastMatchPoint').html(value.MatchTotalPoints);
                                $('#modalMatchTeams').html(value.LastMatchTeams + ' Points');
                            }
                            if (value.NitroMultiplier == 1) { $('#powerPlayContainer').html('<span class="cart au">  <p style="padding-top: 0px !important;" data-toggle="tooltip" title="Nitro"><img style="width: 24px;" src="/Assets/Icon/Nitro.png" /></p></span>'); } else
                                if (value.PainKillerUsed == 'True') { $('#powerPlayContainer').html('<span class="cart au">  <p style="padding-top: 0px !important;" data-toggle="tooltip" title="Painkiller"><img  style="width: 24px;" src="/Assets/Icon/Painkiller.png" /></p></span>'); } else
                                    if (value.AutoPilotUsed == 'True') { $('#powerPlayContainer').html('<span class="cart au">  <p style="padding-top: 0px !important;" data-toggle="tooltip" title="Ultra Captain"><img  style="width: 24px;" src="/Assets/Icon/Autocaptain.png" /></p></span>'); }
                                    else {
                                        $('#powerPlayContainer').html('');
                                    }
                            if (value.WinnerPredictionStatus == false) {
                                winpred += '<i class="fa fa-times" aria-hidden="true" style="font-size: 14px;"></i>';
                            } else {
                                winpred += '<i class="fa fa-check" aria-hidden="true" style="font-size: 14px;color: #17a517;"></i>';
                            }
                            lastwinn = '<div class="" ><span class="trans" style="padding: 0px 0px !important;"><h5 id="WinnerPrediction">' + value.WinnerPrediction + '&nbsp;&nbsp;' + winpred +'</h5> </span> <p>Prediction</p></div >';
                            $('#lastwinn').html(lastwinn);
                        }
                        else {
                            var TStatus = $('#TournamentStatus').val();
                            if (TStatus == 'INPROGRESS' || TStatus == 'COMPLETE') {
                                $('#modalTransfersLeft').html(value.SubsLeft);
                            } else {
                                $('#modalTransfersLeft').html('<img src="/Assets/Icon/infinity.png" />');
                            }
                            var nitroPos = ''; autocaptainPos = ''; painkillerPos = '';
                            if (value.NitroLeft == 0) {
                                nitroPos = '<span class="cart">  <p data-toggle="tooltip" title="Nitro"><img src="/Assets/Icon/Nitro_grey.png" /></p> </span>';
                            } else {
                                if (value.NitroUsed == 'False') {
                                    nitroPos = '<span class="cart">  <p data-toggle="tooltip" title="Nitro"><img src="/Assets/Icon/Nitro_red.png" /></p> </span>';
                                }
                                else {
                                    nitroPos = '<span class="cart">  <p data-toggle="tooltip" title="Nitro"><img src="/Assets/Icon/Nitro_green.png" /> </p></span>';
                                }
                            }
                            $('#nitroPosition').html(nitroPos);
                            if (value.PainKillerLeft == 0) {
                                painkillerPos = '<span  class="cart">  <p data-toggle="tooltip" title="Painkiller"><img src="/Assets/Icon/Painkiller_grey.png" /></p> </span>';
                            } else {
                                if (value.PainKillerUsed == 'False') {
                                    painkillerPos = '<span class="cart">  <p data-toggle="tooltip" title="Painkiller"><img src="/Assets/Icon/Painkiller_red.png" /></p> </span>';
                                }
                                else {
                                    painkillerPos = '<span class="cart">  <p data-toggle="tooltip" title="Painkiller"><img src="/Assets/Icon/Painkiller_green.png" /></p> </span>';
                                }
                            }
                            $('#painkillerPosition').html(painkillerPos);
                            if (value.AutoPilotLeft == 0) {
                                autocaptainPos = '<span class="cart">  <p data-toggle="tooltip" title="Ultra Captain"> <img src="/Assets/Icon/Autocaptain_grey.png" /></p> </span>';
                            } else {
                                if (value.AutoPilotUsed == 'False') {
                                    autocaptainPos = '<span  class="cart"><p data-toggle="tooltip" title="Ultra Captain"><img src="/Assets/Icon/Autocaptain_red.png" /> </p></span>';
                                }
                                else {
                                    autocaptainPos = '<span class="cart">  <p data-toggle="tooltip" title="Ultra Captain"> <img src="/Assets/Icon/Autocaptain_green.png" /> </p></span>';
                                }
                            }
                            $('#autocaptainPosition').html(autocaptainPos);
                            winnerpre = '<div class=""> <span class="trans" style="padding: 0px 0px !important;"><h5 id="WinnerPrediction">' + value.WinnerPrediction + '</h5> </span> <p>Prediction</p></div>';
                            $('#WinnerPrediction').html(winnerpre);
                        }
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
                        wkrow += '<div class="player-right"><img src="/Assets/Icon/WicketkeeperBlackCircle.png" alt=""/></div></div>';
                        wkrow += '<div class="playername">' + value.PlayerShortName + '</div>';

                        if (tabType == 'LMP') {
                            wkrow += '<div class="playerprice">' + value.PlayerPoints + '</div>';
                        } else {
                            wkrow += '<div class="playerprice">' + pPrice + ' K</div>';
                        }
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
                        batrow += '<div class="player-right"><img src="/Assets/Icon/BatsmanBlackCircle.png" alt=""/></div></div>';
                        batrow += '<div class="playername">' + value.PlayerShortName + '</div>';
                        if (tabType == 'LMP') {
                            batrow += '<div class="playerprice">' + value.PlayerPoints + '</div>';
                        } else {
                            batrow += '<div class="playerprice">' + pPrice + ' K</div>';
                        }
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
                        bwlrow += '<div class="player-right"><img src="/Assets/Icon/BowlerBlackCircle.png" alt=""/></div></div>';
                        bwlrow += '<div class="playername">' + value.PlayerShortName + '</div>';
                        if (tabType == 'LMP') {
                            bwlrow += '<div class="playerprice">' + value.PlayerPoints + '</div>';
                        } else {
                            bwlrow += '<div class="playerprice">' + pPrice + ' K</div>';
                        }
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
                        allrow += '<div class="player-right"><img src="/Assets/Icon/AllrounderBlackCircle.png" alt=""/></div></div>';
                        allrow += '<div class="playername">' + value.PlayerShortName + '</div>';
                        if (tabType == 'LMP') {
                            allrow += '<div class="playerprice">' + value.PlayerPoints + '</div>';
                        } else {
                            allrow += '<div class="playerprice">' + pPrice + ' K</div>';
                        }
                        allrow += '</div>';
                    }
                });
            }
            else {
                $('#modalTransfersLeft').html('-');
                $('#nitroPosition').html('<span class="cart">  <p data-toggle="tooltip" title="Nitro"><img src="/Assets/Icon/Nitro_grey.png" /> </p></span>');
                $('#painkillerPosition').html('<span class="cart">  <p data-toggle="tooltip" title="Painkiller"><img src="/Assets/Icon/Painkiller_grey.png" /></p> </span>');
                $('#autocaptainPosition').html('<span class="cart">  <p data-toggle="tooltip" title="Ultra Captain"><img src="/Assets/Icon/Autocaptain_grey.png" /></p> </span>');
                $('#powerPlayContainer').html(' ');
                $('#modalLastMatchPoint').html('-');
                $('#modalMatchTeams').html('No Team');
            }

            divs += '<div class="playerrowtitle"></div><div class="playerrow">' + wkrow + '</div>';
            divs += '<div class="playerrowtitle"></div><div class="playerrow">' + batrow + '</div>';
            divs += '<div class="playerrowtitle"></div><div class="playerrow">' + bwlrow + '</div>';
            divs += '<div class="playerrowtitle"></div><div class="playerrow">' + allrow + '</div>';

            $('#Players').html(divs);
            $('#Modal_Teamname').html(teamname);
            $("#ModalShowteam").modal('show');
        },
        error: function (req, status, error) {
            toastr_warning("Opps! something went wrong. Try reload this page.");
            return false;
        }
    });
}

function LoadUserTournament(UserSelect_tid) {
    var uurl = '/League/UserActiveTournament';
    $.ajax({
        url: uurl,
        type: 'POST',
        dataType: 'json',
        async: true,
        cache: false,
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            if(data.length > 0){
                var defaultindex;
                var divs = '';
                var cBox = '';
                var cnt = 0; var flag = 0; var hasTournament = false; var activeclass = ''; var itemCnt = 0;
                $.each(data, function (index, value) {
                    TournementList[itemCnt] = new Array(itemCnt, value.TournamentId, value.TournamentName , value.TournamentStatus);
                    hasTournament = true;
                    if (UserSelect_tid == 0 && index == 0) {
                        defaultindex = value.TournamentId;
                        LoadLeagueInfo(defaultindex,value.TournamentName,value.TournamentStatus);
                        CurrentSelectTournamentNo = itemCnt + 1;
                        activeclass ='active';
                        TName = value.TournamentName;
                        sessionStorage.setItem("UserTourName",value.TournamentName);
                        $('#TournamentId').val(defaultindex);
                        $('#TournamentStatus').val(value.TournamentStatus);
                        if(value.TournamentStatus != "COMPLETE"){
                            $('#btn_join').show();
                            $('#btn_create').show();
                        }else{
                            $('#btn_join').hide();
                            $('#btn_create').hide();
                        }
                    }
                    else if (value.TournamentId == UserSelect_tid) {
                        CurrentSelectTournamentNo = itemCnt + 1;
                        defaultindex = value.TournamentId;
                        LoadLeagueInfo(defaultindex,value.TournamentName,value.TournamentStatus);
                        activeclass ='active';
                        TName = value.TournamentName;
                        sessionStorage.setItem("UserTourName",value.TournamentName);
                        $('#TournamentId').val(defaultindex);
                        $('#TournamentStatus').val(value.TournamentStatus);
                        if(value.TournamentStatus != "COMPLETE"){
                            $('#btn_join').show();
                            $('#btn_create').show();
                        }else{
                            $('#btn_join').hide();
                            $('#btn_create').hide();
                        }
                    }
                    else{
                        activeclass ='';
                    }
                    cBox += '<div class="item ' + activeclass + '"><div class="slide"><h1>' + value.TournamentName + '</h1><h3>' + value.TournamentStartDate + ' - ' + value.TournamentEndDate + '</h3></div></div>';
                    itemCnt = itemCnt + 1;
                });
                divs += cBox;
                $('#UserTournamentBox').html(divs);
                $('#hdnTourCnt').val(itemCnt);
                $('#spinnerContainer').hide();
            }
            else{
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
        $('#TournamentStatus').val(tourStatus);
        LoadLeagueInfo(tourId , tourName , tourStatus);
    }
}

function LoadLeagueInfo(tid,Tname,tstatus) {
    sessionStorage.setItem("UserSelectTournament", tid);
    sessionStorage.setItem("UserTourName",Tname);
    TName = Tname; TStatus=tstatus;
    $('#TournamentId').val(tid);
    $('#spinnerContainer').show();
    if(TStatus != "COMPLETE"){
        $('#btn_join').show();
        $('#btn_create').show();
        $('#btn_stat').show();
    }
    else{
        $('#btn_join').hide();
        $('#btn_create').hide();
        $('#btn_stat').hide();
    }
    var uurl = '/League/LoadUserActiveLeagueInfo';
    var pData = { TournamentId: tid };
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
         //   console.log(data);
            var teamName = '';
            var sharecode = '';
            var defaultLieagueId;
            var divs = '';
            var activeclass = '';var itemCnt = 0;
            if(data == ''){
                $('#leagueInfoContainer').hide();
                $('#LeagueMsg').show();
                $('#ChnageAbutton').hide();
                $('#ChnageDbutton').show();
                $('#txtLeagueupdatedname').hide();
                $('#txtLeagueidreadonly').show();
            }
            else {
                $('#leagueInfoContainer').show();
                $('#LeagueMsg').hide();
                $.each(data, function (index, value) {
                    itemCnt = itemCnt +1;
                    var cBox = '';
                    if (index == 0) {
                        if(grid != null){
                            ReloadLeagueTeam(value.LeagueId,tid ,value.LeagueLeaderId,itemCnt);
                            UpdateActionType(value.LeagueId,tid,value.LeaguePin);
                        }else{
                            defaultLieagueId= value.LeagueId;
                            GetOwner(value.LeagueLeaderId);
                            LoadTeams(defaultLieagueId, tid);
                            
                            UpdateActionType(value.LeagueId,tid,value.LeaguePin);
                        }
                        SelectedLeagueId = value.LeagueId;
                        sharecode = value.LeaguePin;
                        sessionStorage.setItem("UserTeamName", value.UserTeamName);
                        teamName = value.UserTeamName + '\'s League';
                        activeclass = 'active';
                        if (Session_UserId == value.LeagueLeaderId) {
                            $('#txtLeagueupdatedname').val(value.LeagueName);
                            $('#txtLeagueid').val(value.LeagueId);
                            $('#ChnageDbutton').hide();
                            $('#ChnageAbutton').show();
                            $('#txtLeagueidreadonly').hide();
                            $('#txtLeagueupdatedname').show();
                        } else {
                            $('#ChnageAbutton').hide();
                            $('#ChnageDbutton').show();
                            $('#txtLeagueidreadonly').show();
                            $('#txtLeagueupdatedname').hide();
                        }
                    } else{ activeclass =''; }
                    cBox += '<li id="Litem_' + itemCnt + '" class="' + activeclass + '"><a href="#" onclick="ReloadLeagueTeam(\'' + value.LeagueId + '\',\'' + tid + '\',\'' + value.LeagueLeaderId + '\',\'' + itemCnt + '\',\'' + value.LeagueName + '\')";>' + value.LeagueName + '<br/><span>Rank : ' + value.LeagueRank + '</span></a></li>';
                    divs += cBox;
                });
            }
            $('#UserLeagueBox').html(divs);
            $('#hdnLeagueCnt').val(itemCnt);
            $('#userTeamName').html(teamName);
            $('#hdnLeagueCode').val(sharecode);
            $('#spinnerContainer').hide();
        },
        error: function (req, status, error) {
            $('#spinnerContainer').hide();
            toastr_warning("Opps! something went wrong. Try reload this page.");
            return false;
        }
    });
}

function UpdateActionType(LId,TId,LPin) {
    var ActionType =''; var ActionType_stat= '';
    var hdnFlagVal = $("#hdnFlag").val();
    $('#hdnLeagueId').val(LId);
    if(TStatus != "COMPLETE"){
        if(hdnFlagVal == "owner"){
            ActionType = '<a href="#" onclick="changeCode(\'' + LId + '\',\'' + TId + '\')"; class="btn3">Change Code <i class="fa fa-cog" aria-hidden="true"></i></a>';
        }
        else{
            ActionType = '<a href="#" class="btn3" onclick="ExitLeague(\'' + LId + '\')";>Exit League  <img src="/Assets/Icon/Exit League.png" /></a>';
        }
    }
    ActionType_stat = '<a href="#" class="btn3" onclick="ShowStat(\'' + LId + '\',\'' + TId + '\')";>League Stats <img src="/Assets/Icon/Stats Icon.png" /></a>';
    var url = '/League/LegueSubscription?tid=' + TId + '&lid=' + LId;
    LeagueSubscription = '<a id="btn_leguesub" href=' + url+' class="btn2">League Subscription</a>';
    $('#LeagueStatus').html(ActionType_stat);
    $('#ExitOrChangeCode').html(ActionType);
    $('#LeagueSubscription').html(LeagueSubscription);
}

function ShowStat(LId,TId){
    location.href = '/League/LeagueStat?tid=' + TId + '&lid=' + LId ;
}

function ReloadLeagueTeam(LId, TId, Type, itemNo, Lname) {
  //  console.log(Type);
   // console.log(Session_UserId);
    if (Session_UserId == Type) {
        $('#txtLeagueupdatedname').val(Lname);
        $('#txtLeagueid').val(LId);
        $('#ChnageDbutton').hide();
        $('#ChnageAbutton').show();
    } else {
        $('#ChnageAbutton').hide();
        $('#ChnageDbutton').show();
    }
   
    if(itemNo != 0){
        var leaguecnt =  $('#hdnLeagueCnt').val();
        var i =1;
        for(i=1; i <= leaguecnt; i++){
            $('#Litem_' + i).removeClass('active');
        }
        $('#Litem_' + itemNo).addClass('active');
    }
    SelectedLeagueId =LId;
    GetOwner(Type);
    LeagueStanding(TId,LId);
    grid.reload({ page: 1, LeagueiD: LId, TournamentId: TId });
}

function GetOwner(Type){
    var hdnFlagVal = $("#hdnFlag").val();
    if (Session_UserId == Type) {
        $("#hdnFlag").val("owner");
    }else{
        $("#hdnFlag").val("other");
    }
}

function LeagueStanding(Tid,LId) {
    $('#spinnerContainer').show();
    var uurl = '/League/LoadUserActiveLeagueInfo';
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
            var sharecode = '';
            $.each(data, function (index, value) {
                if (value.LeagueId == LId) {
                    sharecode = value.LeaguePin;
                    sessionStorage.setItem("UserTeamName",value.UserTeamName);
                    UpdateActionType(value.LeagueId,Tid,value.LeaguePin);
                }
            });
            $('#hdnLeagueCode').val(sharecode);
            $('#spinnerContainer').hide();
        },
        error: function (req, status, error) {
            $('#spinnerContainer').hide();
            toastr_warning("Opps! something went wrong. Try reload this page.");
            return false;
        }
    });
}
function CreateLeague() {
    $('#txtLeagueName').val('');
    $("#ModalCreateLeague").modal('show');
    $("#Tname").html(TName);
}


function LeagueManager() {
    $('#txtLeagueName').val('');
    $("#ModalLeagueManager").modal('show');
    $("#Tname").html(TName);
}

function OpenJoinLeague() {
    $('#txtLeaguePin').val('');
    $('#checkJoinmsg').html('');
    $("#ModalJoinLeague").modal('show');
    $("#Tname_J").html(TName);
}

function CheckAvailable() {
    var Tid = $('#TournamentId').val();
    var LeagueName = $('#txtLeagueName').val();
    if(LeagueName.length>2){
        var pData = { TournamentId: Tid, LeagueName: LeagueName };
        var jsonData = JSON.stringify(pData);
        var uurl = '/League/CheckLeagueName';
        $.ajax({
            url: uurl,
            type: 'POST',
            data: jsonData,
            dataType: 'json',
            async: true,
            cache: false,
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                if (data.status == "success") {
                    var cData = { TournamentId: Tid, LeagueName: LeagueName };
                    var jsoncData = JSON.stringify(cData);
                    var uurl = '/League/CreateLeague';
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
                                toastr_success("Your league has been created. Time to invite your friends.");
                                location.href = '/League/Index';
                            }
                        },
                        error: function (req, status, error) {
                            toastr_warning("Opps! something went wrong. Try reload this page.");
                            return false;
                        }
                    });
                } else {
                    $('#checkmsg').html('League Name Exist..');
                }
            },
            error: function (req, status, error) {
                toastr_warning("Opps! something went wrong. Try reload this page.");
                return false;
            }
        });
    }else{
        toastr_info("Your league name should have at least 3 characters.");
    }
}

function ExitLeague(LeagueId) {

    if (confirm('Are you sure want to Exit ?')) {
        var Tid = $('#TournamentId').val();
        var uurl = '/League/ExitLeague';
        var pData = {LeagueId: LeagueId};
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
                LoadUserTournament(Tid);
                
                //$('#spinnerContainer').hide();
            },
            error: function (req, status, error) {
                //$('#spinnerContainer').hide();
                //alert('Oops. We are unable to connect to our servers. Please try again.3!');
                toastr_warning("Opps! something went wrong. Try reload this page.");
                return false;
            }
        });
    }
}

function ChangeLeagueName() {
    var Tid = $('#TournamentId').val();
    var Lid = $('#txtLeagueid').val();
    var LeagueName = $('#txtLeagueupdatedname').val();
    var uurl = '/League/ChangeLeagueName';
    if (LeagueName.length > 2) {
    var pData = { LeagueName: LeagueName, LeagueId: Lid };
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
         //   console.log(data);
            if (data.status == 'success') {
                $("#ModalLeagueManager").modal('hide');
                toastr_info(data.statusMessage);
                location.href = '/League/Index';

            } else {

               toastr_info(data.statusMessage);
            }
        },
        error: function (req, status, error) {
            toastr_warning("Opps! something went wrong. Try reload this page.");
            return false;
        }
    });
    } else {
        toastr_info("Your league name should have at least  minimum 3 characters.");
    }
}
function JoinLeague() {
    var Tid = $('#TournamentId').val();
    var LeaguePin = $('#txtLeaguePin').val();
    if (LeaguePin == '') {
        toastr_info("Please enter league pin.");
        return;
    }
    var uurl = '/League/JoinLeague';
    var pData = {TournamentId: Tid, LeaguePin:LeaguePin};
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
           // console.log(data);
            if(data.status=='success'){
                $("#ModalLeagueManager").modal('hide');
                toastr_info(data.statusMessage);
                LoadUserTournament(Tid);
                
            }else{
             //   $('#checkJoinmsg').html('Sorry !! No league found against your pin.');
                toastr_info('Sorry !! No league found against your pin.');
            }
        },
        error: function (req, status, error) {
            toastr_warning("Opps! something went wrong. Try reload this page.");
            return false;
        }
    });
}

function ActionAdmin(action,UserLeagueId,UserId,Lid,Tid) {
    if(action == "A"){
        var uurl = '/League/ActionAdmin';
        var pData = {UserLeagueId: UserLeagueId, LeagueId: Lid,Type: action,UserId:UserId };
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
                toastr_success("User has been approved to play in your league.");
                grid.reload({ page: 1, LeagueId: LId , TournamentId:Tid});
            },
            error: function (req, status, error) {
                toastr_warning("Opps! something went wrong. Try reload this page.");
                return false;
            }
        });
    }
    else {
        if (confirm('Are you sure want to remove ?')) {
            var uurl = '/League/ActionAdmin';
            var pData = {UserLeagueId: UserLeagueId, LeagueId: Lid,Type: action,UserId:UserId };
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
                    toastr_success("User has been removed from your league.");
                    grid.reload({ page: 1, LeagueId: LId, TournamentId: Tid });
                },
                error: function (req, status, error) {
                    toastr_warning("Opps! something went wrong. Try reload this page.");
                    return false;
                }
            });
        }
    }
}

function changeCode(Lid,Tid) {
    var uurl = '/League/ResetShareCode';
    var pData = { TournamentId: Tid, LeagueId: Lid };
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
            var sharecode = data[0]["LeaguePin"];
            $('#hdnLeagueCode').val(sharecode);
            toastr_info("Your league code has been successfully changed.");
        },
        error: function (req, status, error) {
            toastr_warning("Opps! something went wrong. Try reload this page.");
            return false;
        }
    });
}

function CopyCode() {
   var copyText = document.getElementById("hdnLeagueCode");
    copyText.select();
    document.execCommand("copy");
    toastr_info("Copied code: " + copyText.value);
}