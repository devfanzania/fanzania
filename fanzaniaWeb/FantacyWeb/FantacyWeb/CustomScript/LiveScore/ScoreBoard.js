$(document).ready(function () {
    
    var scoreData = JSON.parse(sessionStorage.getItem('ScoreBoardData'));

    $('#T1_p').attr("src", "https://www.fanzania.com/FantasyCricketRest/ImagePath/image/cricket/teams/" + scoreData.T1Img);
    $('#T2_p').attr("src", "https://www.fanzania.com/FantasyCricketRest/ImagePath/image/cricket/teams/" + scoreData.T2Img);

    $('#matchBtw').html(scoreData.T1 + ' v ' + scoreData.T2);
    $('#mDate').html(scoreData.MatchDate);
    $('#mVenue').html(scoreData.Venue);

    $('#T1').val(scoreData.T1);
    $('#T2').val(scoreData.T2);

    ScoreBoardInng1(scoreData.MatchId,1);
    ScoreBoardInng2(scoreData.MatchId,2);
});

function RefreshScore() {
    
    var scoreData = JSON.parse(sessionStorage.getItem('ScoreBoardData'));
    ScoreBoardInng1(scoreData.MatchId, 1);
    ScoreBoardInng2(scoreData.MatchId, 2);
}

function ScoreBoardInng1(matchId, inngs) {
   
    var pData = { MatchId: matchId, Inning: inngs };
    var jsonData = JSON.stringify(pData);
    $.ajax({
        url: '/LiveScore/LiveScoreBoard',
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

function ScoreBoardInng2(matchId,inngs) {
    var pData = { MatchId: matchId, Inning: inngs };
    var jsonData = JSON.stringify(pData);
    $.ajax({
        url: '/LiveScore/LiveScoreBoard',
        type: 'POST',
        data: jsonData,
        dataType: 'json',
        async: true,
        cache: false,
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            if (data.length > 0) {
                var Bat = ''; var Bowl = ''; var extras = '0'; var totalRun = 0; var RR = ''; var yetToBat = ''; var t1s = ''; var t2s = ''; var totalScore = '0';
                $.each(data, function (index, value) {
                    if (index == 0) {
                        $('#matchHeadline').html(value.MatchSummary);
                        if (value.Team2Extras != null)
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