$("input:checkbox").on('click', function () {
    // in the handler, 'this' refers to the box clicked on
    var $box = $(this);
    if ($box.is(":checked")) {
        // the name of the box is retrieved using the .attr() method
        // as it is assumed and expected to be immutable
        var group = "input:checkbox[name='" + $box.attr("name") + "']";
        // the checked state of the group/box on the other hand will change
        // and the current value is retrieved using .prop() method
        $(group).prop("checked", false);
        $box.prop("checked", true);
    } else {
        $box.prop("checked", false);
    }
});


function OnPageLoad() {
    $('#spinnerContainer').show();
    LoadTeamSelectionRules();
    
    ShowPlayerList();
    

    $('#SelectedPlayerCnt').val(Session_SelectecPlayerCount);
    $('#SelectedBat').val(Session_SelectedBatsmanCount);
    $('#Selectedwk').val(Session_SelectedWicketkeeperCount);
    $('#SelectedBowl').val(Session_SelectedBowlerCount);
    $('#SelectedAll').val(Session_SelectedAllrounderCount);
    $('#div_bottom').focus();
    var matchDetails = sessionStorage.getItem('MatchDetail');
    $('.team1').html(matchDetails.split('|')[0]);
    $('.team2').html(matchDetails.split('|')[1]);
    $('#team11').val(matchDetails.split('|')[0]);
    $('#team22').val(matchDetails.split('|')[1]);
    $('#matchDate').html((matchDetails.split('|')[2]).split('T')[0]);
    $('#matchVenue').html(matchDetails.split('|')[3]);

    
    $('#spinnerContainer').hide();

}

var AllotedBudget = 0;
var Plist = [];
var Nitro_Cnt = 0;
var PainKiller_Cnt = 0;
var AutoPilot_Cnt = 0;
var TeamDet = new Array();

function OpenCaptModal(playerid,playerName,playerType){
    $('#PlayerId_CapVs').val(playerid);
    $('#PlayerName_CapVs').val(playerName);
    $('#Modal_Playername').html(playerName);
    $('#Chk_captain').prop( "checked", false );
    $('#Chk_vcaptain').prop( "checked", false );
    if(playerType == 'C'){
        $('#Chk_captain').prop( "checked", true );
    }else if(playerType == 'VC'){
        $('#Chk_vcaptain').prop( "checked", true );
    }
    $("#ModalCapVCap").modal('show');
}

function OpenTeamFilterModal() {
    
    Loadteamforfilter();
    $("#ModalTeamFilter").modal('show');
}

function Loadteamforfilter(){
    var uurl = '/DailyGame/TeamD/ShowTeamFilter';

    $.ajax({
        url: uurl,
        type: 'POST',
        //dataType: 'json',
        async: true,
        cache: false,
        contentType: "application/json; charset=utf-8",

        success: function (data) {
            
            if (data.length > 0) {
                $('#filtersteam').empty();
                $.each(data, function (index, value) {
                    $('#filtersteam').append($('<div id="ck-button-filter"><label><input type="checkbox" value=' + value.ParticipationTeamId + ' name=' + value.ParticipationTeamName + ' /><span>' + value.TeamShortName + '</span></label></div>'));
                });
            }
        },
        error: function (req, status, error) {
            toastr_warning("Opps! something went wrong. Try reload this page.");
            return false;
        }
    });
}

function ClearFilter(){
    $("#filtersteam input").each(function () {
        $this = $(this);
        if ($this.prop("checked")) {
            $this.prop( "checked", false );
        }
    });
    $('#filter_team').removeClass('filter_team');
    ////var Tid = Session_TournamentId;
    var teamname ='';
    grid.reload({ page: 1, MatchId: Session_MatchId, MatchType: Session_MatchType, FilterTeams: teamname });
}

function FilterTeam(){
    
    //$('#spinnerContainer').show();
    var $checkboxes = $('#filtersteam input');
    var checked_flag = 0;
    var filters = [];
    $checkboxes.filter(':checked').each(function(){
        filters.push( this.value );
        checked_flag = 1;
    });
    filters = filters.join(',');
    if(checked_flag == 1){
        $('#filter_team').addClass('filter_team');
    }
    else{
        $('#filter_team').removeClass('filter_team');
    }
    grid.reload({ page: 1, MatchId: Session_MatchId, MatchType: Session_MatchType, FilterTeams: filters });
    $("#ModalTeamFilter").modal('hide');
    //$('#spinnerContainer').hide();
    }

function SaveCaptainVsCaptain(){
    //$('#spinnerContainer').show();
    
    var PlayerId = $('#PlayerId_CapVs').val();
    var PlayerSelectas = '';
    if(Chk_captain.checked){
        PlayerSelectas = 'Captain';
    }
    if(Chk_vcaptain.checked){
        PlayerSelectas = 'ViceCaptain';
    }
    var uurl = '/DailyGame/TeamD/SaveCaptainVsCaptain';
    var pData = { PlayerId: PlayerId, PlayerSelectAs: PlayerSelectas};
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
            if(data.status='Success'){
                var pName = $('#PlayerName_CapVs').val();
                if(PlayerSelectas == 'Captain'){
                    $('#CaptName').val(pName);
                }
                else{
                    if($('#CaptName').val() ==  pName){
                        $('#CaptName').val('');
                    }
                }
                if (PlayerSelectas == 'ViceCaptain') {
                    $('#VCaptName').val(pName);
                }
                else {
                    if ($('#VCaptName').val() == pName) {
                        $('#VCaptName').val('');
                    }
                }

                $("#ModalCapVCap").modal('hide');
                $('#Chk_captain').prop( "checked", false );
                $('#Chk_vcaptain').prop( "checked", false );
                LoadSelectedPlayers();
                //$('#spinnerContainer').hide();
            }
        },
        error: function (req, status, error) {
            //$('#spinnerContainer').hide();
            toastr_warning("Opps! something went wrong. Try reload this page.");
            return false;
        }
    });

}

function SavePlayers_Confirm(){
    var NoOfPlayer =  $('#SelectedPlayerCnt').val();
    var NoOf_minWk =  $('#Rule_MinWicketKeeper').val();
    var NoOf_minBat =  $('#Rule_MinBatsman').val();
    var NoOf_minBwl =  $('#Rule_MinBowler').val();
    var NoOf_minAll =  $('#Rule_MinAllrounder').val();

    var Selectedwk =  $('#Selectedwk').val();
    var SelectedBat =  $('#SelectedBat').val();
    var SelectedBowl =  $('#SelectedBowl').val();
    var SelectedAll =  $('#SelectedAll').val();

    if(NoOfPlayer == 11){

        if (parseInt(NoOf_minWk) > parseInt(Selectedwk)){
            //$('#spinnerContainer').hide();
            toastr_info('You have to select ' + NoOf_minWk + ' wicketkeeper in your team.');
        }
        else{
            if(parseInt(NoOf_minBat) > parseInt(SelectedBat)){
                //$('#spinnerContainer').hide();
                toastr_info('You have to select at least ' + NoOf_minBat + ' batsmen in your team.');
            }
            else{
                if(parseInt(NoOf_minBwl) > parseInt(SelectedBowl)){
                    //$('#spinnerContainer').hide();
                    toastr_info('You have to select at least ' + NoOf_minBwl + ' bowlers in your team.');
                }
                else{
                    if(parseInt(NoOf_minAll) > parseInt(SelectedAll)){
                        //$('#spinnerContainer').hide();
                        toastr_info('You have to select at least ' + NoOf_minAll + ' allrounder in your team.');
                    }
                    else{
                        var CName = $('#CaptName').val();
                        var VCName = $('#VCaptName').val();
                        if (CName == '') {
                            toastr_info('Please pick your team’s captain.');
                        }
                        else if (VCName == '') {
                            toastr_info('Please pick your team’s vice captain.');
                        }
                        else {
                                                             
                            $('#CaptainName').html('<b>' + CName + '</b>');
                            $('#VicecaptainName').html('<b>' + VCName + '</b>');
                            $("#ModalSaveConfirm").modal('show');
                        }
                    }
                }
            }
        }
    }
    else {
        //$('#spinnerContainer').hide();
        toastr_info('Please select all 11 players.');
    }
}

function SavePlayers(){
    $('#spinnerContainer').show();
    $("#ModalSaveConfirm").modal('hide');

    if (document.getElementById('team11').checked) {
        var team = document.getElementById('team11').value;
    } else if (document.getElementById('team22').checked) {
        var team = document.getElementById('team22').value;
    }
    var getvalue1 = $("#basic-switch-value").text();
    var uurl = '/DailyGame/TeamD/SavePlayer';
    var pData = { WinnerPrediction: getvalue1 };
    var jsonData = JSON.stringify(pData);
    $.ajax({
        url: uurl,
        type: 'POST',
        data: jsonData,
        async: true,
        cache: false,
        contentType: "application/json; charset=utf-8",

        success: function (data) {
            if (data.status = 'Success') {
                sessionStorage.setItem("MatchId", Session_MatchId);
                toastr_success('Your team has been saved. Go get those points!');
                location.href = '/DailyGame/HomeD/Index';
            }
        },
        error: function (req, status, error) {
            $('#spinnerContainer').hide();
            toastr_warning("Opps! something went wrong. Try reload this page.");
            return false;
        }
    });

}


function ShowPlayerList() {
    $('#spinnerContainer').show();
    LoadPlayerlist();
    $('#playerlistcontainer').show();
    $('#spinnerContainer').hide();
    //Loadteamforfilter();
}

var grid;
function LoadPlayerlist() {
    grid = $("#playergrid").grid({
        dataSource: { url: '/DailyGame/TeamD/GetPlayerlist', data: { MatchId: Session_MatchId, MatchType: Session_MatchType } },
        notFoundText: "No players found!",
        uiLibrary: "bootstrap",
        fixedHeader: true,
        height: 420,
        columns: [
            { field: "PlayerSpeciality", title: "Role", width: "10%", sortable: false, align: "center", cssClass: "Role" },
            { field: "", title: "", width: "3%", sortable: false, align: "center", cssClass: "liveP" },
            { field: "", title: "Player", width: "33%", sortable: false, align: "left", cssClass: "Player", headerCssClass: 'grid-header-player' },
            { field: "TeamShortName", title: "Team", headerCssClass: "listTeam", width: "15%", align: "center", sortable: false, cssClass: "Team", headerCssClass: 'grid-header-player' },
            { field: "PlayerValue", title: 'Price', width: "15%", sortable: true, cssClass: "Price", type: 'int' },
            { field: "TotalPoints", title: 'Point', width: "12%", sortable: true, cssClass: "Points", type: 'int' },
            { field: "", title: "Action", width: "12%", sortable: false, align: "center", cssClass: "Action" },
        

        ],
    });
    grid.on("rowDataBound", function (e, $row, id, record) {
      //  console.log(record);
        // return
     //   console.log(Session_MatchId);
  //      return;
        if (record.PlayingInd == true) {
            $row.find('.liveP').html('<img src="/Assets/Icon/circle_green.png" alt="" height="15px" width="15px"/>');
        } else {
            $row.find('.liveP').html('');
        }
        if(record.PlayerType == 'overseas'){
            $row.find('.Player').html('<a  href="#" onclick="return showplayer(\'' + record.PlayerId + '\',\'' + record.ParticipationTeamId + '\');"><span style="color: blue;" class="plyer-list-name" data-toggle="tooltip" data-placement="top" title="' + record.PlayerDesc + '">' + record.PlayerShortName + '</span><img src="/Assets/Icon/Overseas.png" alt="" height="20px" width="20px"/></a>');
        }else{
            $row.find('.Player').html('<a  href="#" onclick="return showplayer(\'' + record.PlayerId + '\',\'' + record.ParticipationTeamId + '\');"><span style="color: blue;"  class="plyer-list-name" data-toggle="tooltip" data-placement="top" title="' + record.PlayerDesc + '">' + record.PlayerShortName + '</span></a>');
        }
        $row.find('.Action').html('<a href="#" title="Add" onclick="return SelectOrRemove(\'' + record.PlayerId + '\',\'A\',\'' + record.PlayerSpeciality + '\',\'' + record.PlayerValue + '\',\'' + record.ParticipationTeamName + '\',\'' + record.PlayerType + '\',\'' + record.TeamCapt + '\',\'' + record.TeamVCapt + '\');" ><img src="/Assets/Icon/plus.png" alt="" height="18px" width="18px"/></a>');
       
        if (record.PlayerSpeciality == 'batsman') {
            $row.find('.Role').html('<img src="/Assets/Icon/BatsmanBlackCircle.png" alt="" height="20px" width="20px"/>');
        }
        else if (record.PlayerSpeciality == 'wicketkeeper') {
            $row.find('.Role').html('<img src="/Assets/Icon/WicketkeeperBlackCircle.png" alt="" height="20px" width="20px"/>');
        }
        else if (record.PlayerSpeciality == 'allrounder') {
            $row.find('.Role').html('<img src="/Assets/Icon/AllrounderBlackCircle.png" alt="" height="20px" width="20px"/>');
        }
        else if (record.PlayerSpeciality == 'bowler') {
            $row.find('.Role').html('<img src="/Assets/Icon/BowlerBlackCircle.png" alt="" height="20px" width="20px"/>');
        }
    });
    //Loadteamforfilter();
}

function FilterByType(PType, btnId) {

    if (btnId != 0) {
        var i = 1;
        for (i = 1; i < btnId + 1; i++) {
            $('#btn_Type_' + i).removeClass('active');
        }
        $('#btn_Type_' + btnId).addClass('active');
    }
    
    grid.reload({ page: 1, MatchId: Session_MatchId, MatchType: Session_MatchType, PlayerType: PType });

}


$("#SearchPlayer").keyup(function (event) {
        var searchText = $("#SearchPlayer").val();
        
        grid.reload({ page: 1, MatchId: Session_MatchId, MatchType: Session_MatchType, PlayerSearch: searchText });
});


function CalculateSameTeamPlayer( PSelect, PTeam) {
    if(PSelect == 'R'){
        for (var i = 0; i < TeamDet.length; i++) {
            if (TeamDet[i][0] === PTeam) {
                var cnt_flag = TeamDet[i][1];
                cnt_flag= cnt_flag - 1;
                TeamDet[i][1] = cnt_flag;
            }
        }
    }
    else {
        var teamcnt = TeamDet.length;
        var flag = 0;
        if(teamcnt == 0){
            TeamDet[teamcnt] = new Array(PTeam,1);
            flag=1;
        }
        else{
            for (var i = 0; i < TeamDet.length; i++) {
                if (TeamDet[i][0] === PTeam) {
                    var cnt_flag = TeamDet[i][1];
                    cnt_flag= cnt_flag + 1;
                    TeamDet[i][1] = cnt_flag;
                    flag=1;
                }
            }
        }
        if(flag == 0){
            TeamDet[teamcnt] = new Array(PTeam,1);
        }

    }
}
function showplayer(PlayerId,ParticipationTeamId) {
    var pData = { ParticipationTeamId: ParticipationTeamId, PlayerId: PlayerId, APIPId: 272450, MatchId:Session_MatchId};
  //  console.log(pData);
    //return;
    var jsonData = JSON.stringify(pData);
    $.ajax({
        url: '/DailyGame/TeamD/FetchPlayerStats',
        type: 'POST',
        data: jsonData,
        dataType: 'json',
        async: true,
        cache: false,
        async: true,
        contentType: "application/json; charset=utf-8",

        success: function (data) {
       //    console.log(data);
          
            $('.bs-example-modal-lg').modal('show');
         //   console.log(data.playerName); 
         //   return;
           var playername = '';
           var ponts = '';
           var selectedby = '';
           var totalPoints = '';
           var outof = '';
            if (data.playerRuns1 =="") {
                data.playerRuns1 = 0;
            }
            if (data.playerRuns2 == "") {
                data.playerRuns2 = 0;
            }
            if (data.playerRuns3 == "") {
                data.playerRuns3 = 0;
            }
            if (data.playerRuns4 == "") {
                data.playerRuns4 = 0;
            }
            if (data.playerRuns5 == "") {
                data.playerRuns5 = 0;
            }
            if (data.playerPoints1 == "") {
                data.playerPoints1 = 0;
            }
            if (data.playerPoints2 == "") {
                data.playerPoints2 = 0;
            }
            if (data.playerPoints3 == "") {
                data.playerPoints3 = 0;
            }
            if (data.playerPoints4 == "") {
                data.playerPoints4 = 0;
            }
            if (data.playerPoints5 == "") {
                data.playerPoints5 = 0;
            }
            if (data.playerPoints5 == "") {
                data.playerPoints5 = 0;
            }
            if (data.playerWickets1 == "") {
                data.playerWickets1 = 0;
            }
            if (data.playerWickets2 == "") {
                data.playerWickets2 = 0;
            }
            if (data.playerWickets3 == "") {
                data.playerWickets3 = 0;
            }
            if (data.playerWickets4 == "") {
                data.playerWickets4 = 0;
            }
            if (data.playerWickets5 == "") {
                data.playerWickets5 = 0;
            }
            $("#tname").text(data.tournamentName)
            playername += '<strong>' + data.playerName + '</strong>  (' + data.teamShortName + ') | ' + data.PlayerSpeciality + '| ' + data.playerValue+'K';
            $('#playername').html(playername);
            ponts += ' <i class="fa viratPic"><img style="height: 80px;border-radius: 48px;width: 80px;" src="' + data.imageURL+'"></i>';
            ponts += '<span class="black">' + data.playerTotalPoints + '</span> Points | <span class="black">' + data.playerRank+'</span> Overall Rank';
            $('#ponts').html(ponts);
            selectedby += '<p class="titleTop"><span class="red">|</span> Selected By</p><p class="normalFont" >'+data.selectedBy+' % Teams</p >'
            $('#selecetedby').html(selectedby);
            totalPoints += '<p class="titleTop"><span class="red">|</span> Form (Recent Match Last)</p><p class="normalFont">Total Points: ' + data.playerPoints1 + ', ' + data.playerPoints2 + ', ' + data.playerPoints3 + ', ' + data.playerPoints4 + ', ' + data.playerPoints5 +'</p>';
            totalPoints += '<p class="smallFont">Runs Scored:  ' + data.playerRuns1 + ', ' + data.playerRuns2 + ', ' + data.playerRuns3 + ', ' + data.playerRuns4 + ', ' + data.playerRuns5 + '</p>';
            totalPoints += '<p class="smallFont">Wicket Taken: ' + data.playerWickets1 + ', ' + data.playerWickets2 + ', ' + data.playerWickets3 + ', ' + data.playerWickets4 + ', ' + data.playerWickets5 + '</p>';
            $('#totalPoints').html(totalPoints);   
            outof += '<p class="titleTop"><span class="red">|</span> Value Rank (Points Per Budget)</p><p class="normalFont" > ' + data.playerValueRank + ' out of ' + data.totalPlayers +' players</p >'
            $('#outof').html(outof);   
        },
        error: function (req, status, error) {
            toastr_warning("Opps! something went wrong. Try reload this page.");
            return false;
        }
    });
}


function SelectOrRemove(Id, PSelect, Pspciality, Pvalue, PTeam, Ptype, IsCaptain, IsVicecaptain) {
    if(Id == IsCaptain){
        $('#CaptName').val('');
    }
    if (Id == IsVicecaptain) {
        $('#VCaptName').val('');
    }
    
    var MaxSameTeamPlayer = $('#Rule_MaxSameTeamPlayer').val()
    var maxlimit_flag = 0;
    if(TeamDet.length > 0 && PSelect == 'A'){
        for (var i = 0; i < TeamDet.length; i++) {
            if (TeamDet[i][1] == MaxSameTeamPlayer && TeamDet[i][0] === PTeam) {
                maxlimit_flag = 1;
            }
        }
    }
    if(maxlimit_flag === 1){
        toastr_info('You cannot have more than 6 players for the same team.');
    }
    else{
        var NoOfPlayer =  $('#SelectedPlayerCnt').val();
        if (NoOfPlayer >= 11 && PSelect == 'A') {
            toastr_info('You already have 11 players in your team.');
        }
        else if(PSelect == 'R'){
            CalculateSameTeamPlayer( PSelect, PTeam);
            SelectionOrRejection(Id, PSelect,NoOfPlayer,Pspciality, Pvalue);
        }
        else {
            var Total_Budget = $('#AllotedBudget').val();
            var  Calculated_Budget = $('#Budget_Calc').val();
            var overseas = $('#OverSeasPlayer').val();
            var Rule_overseas =$('#Rule_MaxOverseasPlayer').val();
            if(Ptype == 'overseas'){ overseas = parseInt(overseas) + 1;}
            if(parseInt(overseas) > parseInt(Rule_overseas)){
                toastr_info('You cannot have more than ' + Rule_overseas +' overseas players in your team.');
            }
            else {
                
                if((parseInt(Calculated_Budget) - parseInt(Pvalue)) >= 0)
                {
                    var Selectedwk =  $('#Selectedwk').val();
                    var SelectedBat =  $('#SelectedBat').val();
                    var SelectedBowl =  $('#SelectedBowl').val();
                    var SelectedAll =  $('#SelectedAll').val();
                    var Rule_MinWicketKeeper = $('#Rule_MinWicketKeeper').val();
                    var Rule_MaxWicketKeeper = $('#Rule_MaxWicketKeeper').val();
                    var Rule_MaxBatsman = $('#Rule_MaxBatsman').val();
                    var Rule_MinBatsman = $('#Rule_MinBatsman').val();
                    var Rule_MaxBowler = $('#Rule_MaxBowler').val();
                    var Rule_MinBowler = $('#Rule_MinBowler').val();
                    var Rule_MaxAllrounder = $('#Rule_MaxAllrounder').val();
                    var Rule_MinAllrounder =$('#Rule_MinAllrounder').val();
                    if(Pspciality == 'wicketkeeper'){ Selectedwk = parseInt(Selectedwk) + 1;}
                    else if(Pspciality == 'batsman'){ SelectedBat = parseInt(SelectedBat) + 1;}
                    else if(Pspciality == 'bowler'){ SelectedBowl = parseInt(SelectedBowl) + 1;}
                    else if(Pspciality == 'allrounder'){ SelectedAll = parseInt(SelectedAll) + 1;}
                    if (Selectedwk > Rule_MaxWicketKeeper){
                        toastr_info('You cannot have more than ' + Rule_MaxWicketKeeper + ' wicketkeeper in your team.');
                    } else
                        if(SelectedBat > Rule_MaxBatsman){
                            toastr_info('You cannot have more than ' + Rule_MaxBatsman +' batsmen in your team.');
                        } else
                            if(SelectedBowl > Rule_MaxBowler){
                                toastr_info('You cannot have more than ' + Rule_MaxBowler +' bowlers in your team.');
                            } else
                                if(SelectedAll > Rule_MaxAllrounder){
                                    toastr_info('You cannot have more than ' + Rule_MaxAllrounder +' allrounders in your team.');
                                }else
                                {
                                    SelectionOrRejection(Id, PSelect,NoOfPlayer,Pspciality, Pvalue);
                                }
                }
                else{
                    toastr_info('You don’t have enough budget left to pick this player.');
                }
            }
        }
    }
}

function SelectionOrRejection(Id, PSelect, NoOfPlayer,Pspciality, Pvalue) {
    $('#spinnerContainer').show();
    var uurl = '/DailyGame/TeamD/SelectOrRemove';
    var pData = { PId: Id, PSelect: PSelect };
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
            if(data.result.status == "Success"){
                LoadSelectedPlayers();
                grid.reload({ page: 1, MatchId: Session_MatchId, MatchType: Session_MatchType });
                var CalcBudget = 0;
                
                if(PSelect == 'A'){
                    var cnt = parseInt(NoOfPlayer) + 1;
                    $('#SelectedPlayerCnt').val(cnt);
                    var precalcBudget = $('#Budget_Calc').val();
                    CalcBudget = parseInt(precalcBudget) + parseInt(Pvalue);
                    if(Pspciality == 'wicketkeeper'){
                        var Selectedwk =  $('#Selectedwk').val();
                        Selectedwk = parseInt(Selectedwk) + 1;
                        $('#Selectedwk').val(Selectedwk);
                    }
                    else if(Pspciality == 'batsman'){
                        var SelectedBat =  $('#SelectedBat').val();
                        SelectedBat = parseInt(SelectedBat) + 1;
                        $('#SelectedBat').val(SelectedBat);
                    }
                    else if(Pspciality == 'bowler'){
                        var SelectedBowl =  $('#SelectedBowl').val();
                        SelectedBowl = parseInt(SelectedBowl) + 1;
                        $('#SelectedBowl').val(SelectedBowl);
                    }
                    else if(Pspciality == 'allrounder'){
                        var SelectedAll =  $('#SelectedAll').val();
                        SelectedAll = parseInt(SelectedAll) + 1;
                        $('#SelectedAll').val(SelectedAll);
                    }
                }else{
                    var cnt =parseInt(NoOfPlayer) - 1;
                    $('#SelectedPlayerCnt').val(cnt);
                    var precalcBudget = $('#Budget_Calc').val();
                    CalcBudget = parseInt(precalcBudget) - parseInt(Pvalue);
                    if(Pspciality == 'wicketkeeper'){
                        var Selectedwk =  $('#Selectedwk').val();
                        Selectedwk = parseInt(Selectedwk) - 1;
                        $('#Selectedwk').val(Selectedwk);
                    }
                    else if(Pspciality == 'batsman'){
                        var SelectedBat =  $('#SelectedBat').val();
                        SelectedBat = parseInt(SelectedBat) - 1;
                        $('#SelectedBat').val(SelectedBat);
                    }
                    else if(Pspciality == 'bowler'){
                        var SelectedBowl =  $('#SelectedBowl').val();
                        SelectedBowl = parseInt(SelectedBowl) - 1;
                        $('#SelectedBowl').val(SelectedBowl);
                    }
                    else if(Pspciality == 'allrounder'){
                        var SelectedAll =  $('#SelectedAll').val();
                        SelectedAll = parseInt(SelectedAll) - 1;
                        $('#SelectedAll').val(SelectedAll);
                    }
                }
                var pcnt = $('#SelectedPlayerCnt').val();
                $('#spinnerContainer').hide();
            }
            else if(data.result.status == "LimitCross"){
                //$('#spinnerContainer').hide();
                toastr_info(data.result.statusMessage);
                return false;
            }
        },
        error: function (req, status, error) {
            $('#spinnerContainer').hide();
            toastr_warning("Opps! something went wrong. Try reload this page.");
            return false;
        }
    });


}



function LoadSelectedPlayers() {

    TeamDet.length=0;
    var uurl = '/DailyGame/TeamD/LoadSelectedPlayers';
    $.ajax({
        url: uurl,
        type: 'POST',
        dataType: 'json',
        async: true,
        cache: false,
        contentType: "application/json; charset=utf-8",

        success: function (data) {
           // console.log(data);
            LoadPlayersOnGround(data);
            $('#spinnerContainer').hide();

        },
        error: function (req, status, error) {
            //toastr_info('Select your players');
            var divs = '';
            divs += '<div class="playerrow"></div>';
            divs += '<div class="playerrow"></div>';
            divs += '<div class="playerrow"></div>';
            divs += '<div class="playerrow"></div>';
            $('#playerList').html(divs);
            return false;
        }
    });
}


function LoadPlayersOnGround(data) {
    var TotalPlayerValue = 0;
    var divs = '';
    var wkrow = '';
    var batrow = '';
    var bwlrow = '';
    var allrow = '';
    var Overseas = 0;
    var pcnt = 0; var wkcnt = 0; var batcnt = 0; var ballcnt = 0; var allcnt = 0;
    
    var teamfirst = document.getElementById('team11').value;
    var teamsecond = document.getElementById('team22').value;
    var team1 = document.getElementById('team11').value;
    var team2 = document.getElementById('team22').value;
    
    var teamthird = data[0].WinnerPrediction;
    if (teamthird == null || teamthird == "") {
        $('#winn').val('-');
    } else {
        $('#winn').val(teamthird);
    }
  //  alert(data[0].WinnerPrediction);
  
    $('#team1').val(team1);
    $('#team2').val(team2);
    var teamthird = teamthird;
    if (team1 == teamthird) {
        $("#basic-switch").setTheSwitch({
            bgOn: '#21a544',
            bgNoSet: '#f1f1f1',
            bgOff: '#21a544',
            width: 90,
            height: 34,
            hsize: 21,
            porcent: false,
            disabled: false,
            steps: 2,
            action: 'off',
            onSet: function (e) {
                $("#basic-switch").getTheSwitchValue(value => {
                    $("#basic-switch-value").html(team1);
                });
            },
            onClickOn: function (e) {
                var element = $(e).attr('id');

                $("#basic-switch").getTheSwitchValue(value => {
                    $("#basic-switch-value").html(team2);
                    //var getvalue = $("#basic-switch-value").text();
                    // alert(getvalue);
                });
                $("#basic-switch-actions").val('RCB');

            },
            onClickOff: function (e) {
                var element = $(e).attr('id');

                $("#basic-switch").getTheSwitchValue(value => {
                    $("#basic-switch-value").html(team1);
                    //   var getvalue = $("#basic-switch-value").text();
                    //  alert(getvalue);
                });

            }
        });

    } else if (team2 == teamthird) {
        $("#basic-switch").setTheSwitch({
            bgOn: '#21a544',
            bgNoSet: '#f1f1f1',
            bgOff: '#21a544',
            width: 90,
            height: 34,
            hsize: 21,
            porcent: false,
            disabled: false,
            steps: 2,
            action: 'on',
            onSet: function (e) {
                $("#basic-switch").getTheSwitchValue(value => {
                    $("#basic-switch-value").html(team2);
                });
            },
            onClickOn: function (e) {
                var element = $(e).attr('id');
                $("#basic-switch").getTheSwitchValue(value => {
                    $("#basic-switch-value").html(team2);
                });
            },
            onClickOff: function (e) {
                var element = $(e).attr('id');
                $("#basic-switch").getTheSwitchValue(value => {
                    $("#basic-switch-value").html(team1);
                    //var getvalue = $("#basic-switch-value").text();
                    //  alert(getvalue);
                });
                $("#basic-switch-actions").val('MI');
            }
        });
    } else {
        $("#basic-switch").setTheSwitch({
            bgOn: '#21a544',
            bgNoSet: '#f1f1f1',
            bgOff: '#21a544',
            width: 90,
            height: 34,
            hsize: 21,
            porcent: false,
            disabled: false,
            action: 'noset',
            onSet: function (e) {
                $("#basic-switch").getTheSwitchValue(value => {
                    $("#basic-switch-value").html();
                });
            },
            onClickOn: function (e) {

                var element = $(e).attr('id');
                $("#basic-switch").getTheSwitchValue(value => {
                    $("#basic-switch-value").html(team2);
                    // var getvalue = $("#basic-switch-value").text();
                    //  alert(getvalue);
                });
            },
            onClickNoSet: function (e) {
                $("#basic-switch").getTheSwitchValue(value => {
                    $("#basic-switch-value").html('');
                    //  var getvalue = $("#basic-switch-value").text();
                    //  alert(getvalue);
                });
            },
            onClickOff: function (e) {
                var element = $(e).attr('id');
                $("#basic-switch").getTheSwitchValue(value => {
                    $("#basic-switch-value").html(team1);
                    // var getvalue = $("#basic-switch-value").text();
                    // alert(getvalue);
                });
            }
        });
    }
    //  $('.team1').val(team1);
    //  $('.team2').val(team2);
    $('#teamspan1').html(team1);
    $('#teamspan2').html(team2);
    //console.log(data[0]);
    var BattingTeam = data[0].BattingTeam;
    //var BattingTeam = 'TKR';
    var matchDetails1 = sessionStorage.getItem('MatchDetail');
    //console.log(matchDetails1);
    var batteam1=matchDetails1.split('|')[4];
    var batteam2 = matchDetails1.split('|')[5];
  
    if (batteam1 == BattingTeam) {
        $('#imageteam2').hide();
        $('#imageteam1').show();
        $('#greenimage').show();
        $('#teambat').html(team1 + ' to BAT');
        $('#showdate').hide();

    } else if (batteam2 == BattingTeam) {
        $('#imageteam1').hide();
        $('#imageteam2').show();
        $('#greenimage').show();
        $('#teambat').html(team2 + ' to BAT');
        $('#showdate').hide();
        
    } else {
         $('#imageteam2').hide();
        $('#imageteam1').hide();
        $('#greenimage').hide();
        $('#teambat').hide();
        $('#showdate').show();
    }
   

    $.each(data, function (index, value) {
        
        var pPrice = value.PlayerValue; var pt = '';
        if (value.PlayingInd == true)
            playerActive = 'player-active';
        else
            playerActive = '';
        if (value.PlayerSpeciality == 'wicketkeeper') {

            wkrow += '<div class="playericonholder"><div class="playericon"><div class="player-left">';
            if (value.PlayerId == value.TeamCapt) {
                wkrow += '<img src="/Assets/Icon/Captain.png" alt="" height="20px" width="20px" />';
                pt = 'C';
                $('#CaptName').val(value.PlayerShortName);
            }
            if (value.PlayerId == value.TeamVCapt) {
                wkrow += '<img src="/Assets/Icon/ViceCaptain.png" alt="" height="20px" width="20px" />';
                pt = 'VC';
                $('#VCaptName').val(value.PlayerShortName);
            }
            wkrow += '</div><div class="player-center"><img class = "' + playerActive + '" src="https://www.fanzania.com/FantasyCricketRest/ImagePath/image/cricket/teams/' + value.TeamImage + '" onerror="this.src=\'/Assets/Icon/default-image.png\';" alt="" onclick="OpenCaptModal(\'' + value.PlayerId + '\',\'' + value.PlayerShortName + '\',\'' + pt + '\');" /></div>';
            wkrow += '<div class="player-right"><a href="#" title="Remove" onclick="return SelectOrRemove(\'' + value.PlayerId + '\',\'R\',\'' + value.PlayerSpeciality + '\',\'' + value.PlayerValue + '\',\'' + value.ParticipationTeamName + '\',\'' + value.PlayerType + '\',\'' + value.TeamCapt + '\',\'' + value.TeamVCapt + '\');" ><img src="/Assets/Icon/cancel.png" alt="" height="15px" width="15px"/></a></div></div>';
            if (value.PlayerType == 'overseas') {
                wkrow += '<div class="playername ">' + value.PlayerShortName + '</div>';
            } else {
                wkrow += '<div class="playername">' + value.PlayerShortName + '</div>';
            }
            wkrow += '<div class="playerprice">' + pPrice + ' K</div>';
            wkrow += '</div>';
            wkcnt = wkcnt + 1;
            TotalPlayerValue += parseInt(pPrice);
        }
        if (value.PlayerSpeciality == 'batsman') {
            batrow += '<div class="playericonholder"><div class="playericon"><div class="player-left">';
            if (value.PlayerId == value.TeamCapt) {
                batrow += '<img src="/Assets/Icon/Captain.png" alt="" height="20px" width="20px" />';
                pt = 'C';
                $('#CaptName').val(value.PlayerShortName);
            }
            if (value.PlayerId == value.TeamVCapt) {
                batrow += '<img src="/Assets/Icon/ViceCaptain.png" alt="" height="20px" width="20px" />';
                pt = 'VC';
                $('#VCaptName').val(value.PlayerShortName);
            }
            batrow += '</div><div class="player-center"><img class = "' + playerActive + '" src="https://www.fanzania.com/FantasyCricketRest/ImagePath/image/cricket/teams/' + value.TeamImage + '" onerror="this.src=\'/Assets/Icon/default-image.png\';" alt="" onclick="OpenCaptModal(\'' + value.PlayerId + '\',\'' + value.PlayerShortName + '\',\'' + pt + '\');"/></div>';
            batrow += '<div class="player-right"><a href="#" title="Remove" onclick="return SelectOrRemove(\'' + value.PlayerId + '\',\'R\',\'' + value.PlayerSpeciality + '\',\'' + value.PlayerValue + '\',\'' + value.ParticipationTeamName + '\',\'' + value.PlayerType + '\',\'' + value.TeamCapt + '\',\'' + value.TeamVCapt + '\');" ><img src="/Assets/Icon/cancel.png" alt="" height="15px" width="15px"/></a></div></div>';
            if (value.PlayerType == 'overseas') {
                batrow += '<div class="playername ">' + value.PlayerShortName + '</div>';
            } else {
                batrow += '<div class="playername">' + value.PlayerShortName + '</div>';
            }
            batrow += '<div class="playerprice">' + pPrice + ' K</div>';
            batrow += '</div>';
            batcnt = batcnt + 1;
            TotalPlayerValue += parseInt(pPrice);
        }
        if (value.PlayerSpeciality == 'bowler') {

            bwlrow += '<div class="playericonholder"><div class="playericon"><div class="player-left">';
            if (value.PlayerId == value.TeamCapt) {
                bwlrow += '<img src="/Assets/Icon/Captain.png" alt="" height="20px" width="20px" />'
                pt = 'C';
                $('#CaptName').val(value.PlayerShortName);
            }
            if (value.PlayerId == value.TeamVCapt) {
                bwlrow += '<img src="/Assets/Icon/ViceCaptain.png" alt="" height="20px" width="20px" />';
                pt = 'VC';
                $('#VCaptName').val(value.PlayerShortName);
            }
            bwlrow += '</div><div class="player-center"><img class = "' + playerActive + '" src="https://www.fanzania.com/FantasyCricketRest/ImagePath/image/cricket/teams/' + value.TeamImage + '" onerror="this.src=\'/Assets/Icon/default-image.png\';" alt="" onclick="OpenCaptModal(\'' + value.PlayerId + '\',\'' + value.PlayerShortName + '\',\'' + pt + '\');"/></div>';
            bwlrow += '<div class="player-right"><a href="#" title="Remove" onclick="return SelectOrRemove(\'' + value.PlayerId + '\',\'R\',\'' + value.PlayerSpeciality + '\',\'' + value.PlayerValue + '\',\'' + value.ParticipationTeamName + '\',\'' + value.PlayerType + '\',\'' + value.TeamCapt + '\',\'' + value.TeamVCapt + '\');" ><img src="/Assets/Icon/cancel.png" alt="" height="15px" width="15px"/></a></div></div>';
            if (value.PlayerType == 'overseas') {
                bwlrow += '<div class="playername ">' + value.PlayerShortName + '</div>';
            } else { bwlrow += '<div class="playername">' + value.PlayerShortName + '</div>'; }
            bwlrow += '<div class="playerprice">' + pPrice + ' K</div>';
            bwlrow += '</div>';
            ballcnt = ballcnt + 1;
            TotalPlayerValue += parseInt(pPrice);
        }
        if (value.PlayerSpeciality == 'allrounder') {

            allrow += '<div class="playericonholder"><div class="playericon"><div class="player-left">';
            if (value.PlayerId == value.TeamCapt) {
                allrow += '<img src="/Assets/Icon/Captain.png" alt="" height="20px" width="20px" />';
                pt = 'C';
                $('#CaptName').val(value.PlayerShortName);
            }
            if (value.PlayerId == value.TeamVCapt) {
                allrow += '<img src="/Assets/Icon/ViceCaptain.png" alt="" height="20px" width="20px" />';
                pt = 'VC';
                $('#VCaptName').val(value.PlayerShortName);
            }
            allrow += '</div><div class="player-center"><img class = "' + playerActive + '" src="https://www.fanzania.com/FantasyCricketRest/ImagePath/image/cricket/teams/' + value.TeamImage + '" onerror="this.src=\'/Assets/Icon/default-image.png\';" alt="" onclick="OpenCaptModal(\'' + value.PlayerId + '\',\'' + value.PlayerShortName + '\',\'' + pt + '\');"/></div>';
            allrow += '<div class="player-right"><a href="#" title="Remove" onclick="return SelectOrRemove(\'' + value.PlayerId + '\',\'R\',\'' + value.PlayerSpeciality + '\',\'' + value.PlayerValue + '\',\'' + value.ParticipationTeamName + '\',\'' + value.PlayerType + '\',\'' + value.TeamCapt + '\',\'' + value.TeamVCapt + '\');" ><img src="/Assets/Icon/cancel.png" alt="" height="15px" width="15px"/></a></div></div>';
            if (value.PlayerType == 'overseas') {
                allrow += '<div class="playername ">' + value.PlayerShortName + '</div>';
            } else { allrow += '<div class="playername">' + value.PlayerShortName + '</div>'; }
            allrow += '<div class="playerprice">' + pPrice + '  K</div>';
            allrow += '</div>';

            allcnt = allcnt + 1;
            TotalPlayerValue += parseInt(pPrice);
        }
        if (value.PlayerType == 'overseas') {
            Overseas = Overseas + 1;
        }
        pcnt = pcnt + 1;

        CalculateSameTeamPlayer('A', value.ParticipationTeamName);
    });


    divs += '<div class="playerrow">' + wkrow + '</div>';
    divs += '<div class="playerrow">' + batrow + '</div>';
    divs += '<div class="playerrow">' + bwlrow + '</div>';
    divs += '<div class="playerrow">' + allrow + '</div>';

    $('#SelectedPlayerCnt').val(pcnt);
    $('#playerList').html(divs);
    
    var alltedBudget = $('#AllotedBudget').val();
    TotalPlayerValue = parseInt(alltedBudget) - parseInt(TotalPlayerValue);
    if (parseInt(TotalPlayerValue) == 0 || TotalPlayerValue == 'NaN') {
        TotalPlayerValue = 0;
    }
    $('#TotalBudget').html(TotalPlayerValue + 'K');//.toFixed(2)
    $('#Budget_Calc').val(TotalPlayerValue);

    $('#SelectedPlayerCnt').val(pcnt);
    $('#Selectedwk').val(wkcnt);
    $('#SelectedBat').val(batcnt);
    $('#SelectedBowl').val(ballcnt);
    $('#SelectedAll').val(allcnt);
    $('#OverSeasPlayer').val(Overseas);

    
    $('#showTotalPlayerCnt').html(pcnt);

    var MinWicketKeeper = $('#Rule_MinWicketKeeper').val();
    var MaxWicketKeeper = $('#Rule_MaxWicketKeeper').val();
    var MaxBatsman = $('#Rule_MaxBatsman').val();
    var MinBatsman = $('#Rule_MinBatsman').val();
    var MaxBowler = $('#Rule_MaxBowler').val();
    var MinBowler = $('#Rule_MinBowler').val();
    var MaxAllrounder = $('#Rule_MaxAllrounder').val();
    var MinAllrounder = $('#Rule_MinAllrounder').val();
    if (pcnt == 11) {
        $('#showTotalPlayerCnt').removeClass('red');
        $('#showTotalPlayerCnt').addClass('green');
    }
    else {
        $('#showTotalPlayerCnt').addClass('red');
        $('#showTotalPlayerCnt').removeClass('green');
    }
    $('#showTotalWKCnt').html(wkcnt);
    if (wkcnt >= MinWicketKeeper && wkcnt <= MaxWicketKeeper) {
        $('#showTotalWKCnt').removeClass('red');
        $('#showTotalWKCnt').addClass('green');
    }
    else {
        $('#showTotalWKCnt').addClass('red');
        $('#showTotalWKCnt').removeClass('green');
    }
    $('#showTotalBatsmanCnt').html(batcnt);
    if (batcnt >= MinBatsman && batcnt <= MaxBatsman) {
        $('#showTotalBatsmanCnt').removeClass('red');
        $('#showTotalBatsmanCnt').addClass('green');
    }
    else {
        $('#showTotalBatsmanCnt').addClass('red');
        $('#showTotalBatsmanCnt').removeClass('green');
    }
    $('#showTotalBowlerCnt').html(ballcnt);
    if (ballcnt >= MinBowler && ballcnt <= MaxBowler) {
        $('#showTotalBowlerCnt').removeClass('red');
        $('#showTotalBowlerCnt').addClass('green');
    }
    else {
        $('#showTotalBowlerCnt').addClass('red');
        $('#showTotalBowlerCnt').removeClass('green');
    }
    $('#showTotalARCnt').html(allcnt);
    if (allcnt >= MinAllrounder && allcnt <= MaxAllrounder) {
        $('#showTotalARCnt').removeClass('red');
        $('#showTotalARCnt').addClass('green');
    }
    else {
        $('#showTotalARCnt').addClass('red');
        $('#showTotalARCnt').removeClass('green');
    }

}



function LoadTeamSelectionRules() {
    var uurl = '/DailyGame/TeamD/PlayerSelectionRules';
    
    var pData = { MatchId: Session_MatchId, MatchType: Session_MatchType };
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
            var infoAll = ''; var infoBat = ''; var infoBall = ''; var infoWK = ''; var infoAR = '';
            $.each(data, function (index, value) {
                $('#Rule_MinWicketKeeper').val(value.WicketKeeper);
                $('#Rule_MaxWicketKeeper').val(value.MaxWicketKeeper);
                $('#Rule_MaxBatsman').val(value.MaxBatsman);
                $('#Rule_MinBatsman').val(value.MinBatsman);
                $('#Rule_MaxBowler').val(value.MaxBowler);
                $('#Rule_MinBowler').val(value.MinBowler);
                $('#Rule_MaxAllrounder').val(value.MaxAllrounder);
                $('#Rule_MinAllrounder').val(value.MinAllrounder);
                $('#AllotedBudget').val(value.TotalBudget);
                $('#TotalBudget').html(value.TotalBudget + 'K');
                $('#Budget_Calc').val(value.TotalBudget);
                $('#Rule_MaxSameTeamPlayer').val(value.MaxSameTeamPlayer);
                $('#Rule_MaxOverseasPlayer').val(value.MaxOverseasPlayer);

                infoAll = '11';
                infoBat = value.MinBatsman + '-' + value.MaxBatsman;
                infoBall = value.MinBowler + '-' + value.MaxBowler;
                infoAR = value.MinAllrounder + '-' + value.MaxAllrounder;
                infoWK = value.WicketKeeper + '-' + value.MaxWicketKeeper;

            });

            $('#TotalPlayersOnRules').html(infoAll);
            $('#TotalBatsmanOnRules').html(infoBat);
            $('#TotalBowlersOnRules').html(infoBall);
            $('#TotalAllrounderOnRules').html(infoAR);
            $('#TotalWKOnRules').html(infoWK);
            LoadSelectedPlayers();

        },
        error: function (req, status, error) {
            toastr_warning("Opps! something went wrong. Try reload this page.");
            return false;
        }
    });
}