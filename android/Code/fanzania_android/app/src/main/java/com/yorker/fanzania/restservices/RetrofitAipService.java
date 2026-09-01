package com.yorker.fanzania.restservices;

import android.widget.LinearLayout;

import com.google.gson.JsonObject;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.views.model.PowerPlayLifelinePost;
import com.yorker.fanzania.views.model.casefee.AddMoneyRequest;
import com.yorker.fanzania.views.model.casefee.AddMoneyResponse;
import com.yorker.fanzania.views.model.casefee.CasefeeResponse;
import com.yorker.fanzania.views.model.casefee.CashfreeResponse;
import com.yorker.fanzania.views.model.casefee.PaymentGatewayRequest;
import com.yorker.fanzania.views.screens.league.LeagueSubscriptionPost;

import org.json.JSONArray;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RetrofitAipService {

    @POST("sign-in")
    Call<JsonObject> Login(@Header(Constants.RETROFIT_HEADER) String content_type,
                           @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                           @Header(Constants.RETROFIT_HEADER1) String token,
                           @Body Map<String, Object> userMap);

    @POST("country-list")
    Call<JsonObject> CountryList(@Header(Constants.RETROFIT_HEADER) String content_type,
                                 @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                 @Header(Constants.RETROFIT_HEADER1) String token);

    @POST("verify-user")
    Call<JsonObject> EmailChecking(@Header(Constants.RETROFIT_HEADER) String content_type,
                                   @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                   @Header(Constants.RETROFIT_HEADER1) String token,
                                   @Body Map<String, Object> userMap);

    @POST("sign-up")
    Call<JsonObject> Registration(@Header("Content-Type") String content_type,
                                  @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                  @Header(Constants.RETROFIT_HEADER1) String token,
                                  @Body Map<String, Object> userMap);

    @POST("email-verification-code")
    Call<JsonObject> getVerficationCode(@Header(Constants.RETROFIT_HEADER) String content_type,
                                        @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                        @Header(Constants.RETROFIT_HEADER1) String token,
                                        @Body Map<String, Object> userMap);

    @POST("forget-password")
    Call<JsonObject> forgotPassword(@Header(Constants.RETROFIT_HEADER) String content_type,
                                    @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                    @Header(Constants.RETROFIT_HEADER1) String token,
                                    @Body Map<String, Object> userMap);

    @POST("email-verified")
    Call<JsonObject> setEmailVerified(@Header(Constants.RETROFIT_HEADER) String content_type,
                                      @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                      @Header(Constants.RETROFIT_HEADER1) String token,
                                      @Body Map<String, Object> userMap);

    @POST("log-out")
    Call<JsonObject> Logout(@Header(Constants.RETROFIT_HEADER) String content_type,
                            @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                            @Header(Constants.RETROFIT_HEADER1) String token,
                            @Body Map<String, Object> userMap);

    @POST("external-sign-in")
    Call<JsonObject> SocialRegistration(@Header(Constants.RETROFIT_HEADER) String content_type,
                                        @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                        @Header(Constants.RETROFIT_HEADER1) String token,
                                        @Body Map<String, Object> userMap);

    @POST("save-new-password")
    Call<JsonObject> ChangePassword(@Header(Constants.RETROFIT_HEADER) String content_type,
                                    @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                    @Header(Constants.RETROFIT_HEADER1) String token,
                                    @Body Map<String, Object> userMap);

    @POST("fetch-league-subscription")
    Call<JsonObject> LeagueSubscriptions(@Header(Constants.RETROFIT_HEADER) String content_type,
                                         @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                         @Header(Constants.RETROFIT_HEADER1) String token,
                                         @Body Map<String, Object> userMap);

    @POST("update-league-subscription")
    Call<JsonObject> UpdateLeagueSubscriptions(@Header(Constants.RETROFIT_HEADER) String content_type,
                                               @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                               @Header(Constants.RETROFIT_HEADER1) String token,
                                               @Body List<LeagueSubscriptionPost> userMap);

    @POST("update-user-powerplay")
    Call<JsonObject> UpdateUserPowerPlay(@Header(Constants.RETROFIT_HEADER) String content_type,
                                         @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                         @Header(Constants.RETROFIT_HEADER1) String token,
                                         @Body PowerPlayLifelinePost userMap);

    @POST("user-tournament-details")
    Call<JsonObject> UserTournaments(@Header(Constants.RETROFIT_HEADER) String content_type,
                                     @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                     @Header(Constants.RETROFIT_HEADER1) String token,
                                     @Header(Constants.RETROFIT_HEADER2) String userid,
                                     @Body Map<String, Object> userMap);

    @POST("user-all-leagues")
    Call<JsonObject> UserAllLeague(@Header(Constants.RETROFIT_HEADER) String content_type,
                                   @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                   @Header(Constants.RETROFIT_HEADER1) String token,
                                   @Header(Constants.RETROFIT_HEADER2) String userid,
                                   @Body Map<String, Object> userMap);

    @POST("user-upcoming-tournament")
    Call<JsonObject> UpcommingAllTournament(@Header(Constants.RETROFIT_HEADER) String content_type,
                                            @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                            @Header(Constants.RETROFIT_HEADER1) String token,
                                            @Header(Constants.RETROFIT_HEADER2) String userid,
                                            @Body Map<String, Object> userMap);

    @POST("league-teams")
    Call<JsonObject> LeagueTeams(@Header(Constants.RETROFIT_HEADER) String content_type,
                                 @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                 @Header(Constants.RETROFIT_HEADER1) String token,
                                 @Header(Constants.RETROFIT_HEADER2) String userid,
                                 @Body Map<String, Object> userMap);

    @POST("fetch-profile")
    Call<JsonObject> FetchProfileDetails(@Header(Constants.RETROFIT_HEADER) String content_type,
                                         @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                         @Header(Constants.RETROFIT_HEADER1) String token,
                                         @Header(Constants.RETROFIT_HEADER2) String userid,
                                         @Body Map<String, Object> userMap);

    @POST("save-profile")
    Call<JsonObject> UpdateProfileDetails(@Header(Constants.RETROFIT_HEADER) String content_type,
                                          @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                          @Header(Constants.RETROFIT_HEADER1) String token,
                                          @Header(Constants.RETROFIT_HEADER2) String userid,
                                          @Body Map<String, Object> userMap);

    @POST("create-league")
    Call<JsonObject> CreateLeague(@Header(Constants.RETROFIT_HEADER) String content_type,
                                  @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                  @Header(Constants.RETROFIT_HEADER1) String token,
                                  @Header(Constants.RETROFIT_HEADER2) String userid,
                                  @Body Map<String, Object> userMap);

    @POST("change-league-name")
    Call<JsonObject> RenameLeague(@Header(Constants.RETROFIT_HEADER) String content_type,
                                  @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                  @Header(Constants.RETROFIT_HEADER1) String token,
                                  @Header(Constants.RETROFIT_HEADER2) String userid,
                                  @Body Map<String, Object> userMap);

    @POST("reset-league-pin")
    Call<JsonObject> CreateLeaguePin(@Header(Constants.RETROFIT_HEADER) String content_type,
                                     @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                     @Header(Constants.RETROFIT_HEADER1) String token,
                                     @Header(Constants.RETROFIT_HEADER2) String userid,
                                     @Body Map<String, Object> userMap);

    @POST("join-league")
    Call<JsonObject> JoineLeague(@Header(Constants.RETROFIT_HEADER) String content_type,
                                 @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                 @Header(Constants.RETROFIT_HEADER1) String token,
                                 @Header(Constants.RETROFIT_HEADER2) String userid,
                                 @Body Map<String, Object> userMap);

    @POST("exit-league")
    Call<JsonObject> ExitLeague(@Header(Constants.RETROFIT_HEADER) String content_type,
                                @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                @Header(Constants.RETROFIT_HEADER1) String token,
                                @Header(Constants.RETROFIT_HEADER2) String userid,
                                @Body Map<String, Object> userMap);

    @POST("all-matches")
    Call<JsonObject> getTournamentMatches(@Header(Constants.RETROFIT_HEADER) String content_type,
                                          @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                          @Body Map<String, Object> userMap);

    @POST("all-future-matches")
    Call<JsonObject> getUpcomingMatches(@Header(Constants.RETROFIT_HEADER) String content_type,
                                        @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                        @Header(Constants.RETROFIT_HEADER1) String token,
                                        @Header(Constants.RETROFIT_HEADER2) String userid,
                                        @Body Map<String, Object> userMap);

    @POST("user-team-match-details-with-players")
    Call<JsonObject> UserMatchDetailsWithPlayers(@Header(Constants.RETROFIT_HEADER) String content_type,
                                                 @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                                 @Header(Constants.RETROFIT_HEADER1) String token,
                                                 @Header(Constants.RETROFIT_HEADER2) String userid,
                                                 @Body Map<String, Object> userMap);

    @POST("user-team-players-with-details")
    Call<JsonObject> UserTeamPlayersWithDetails(@Header(Constants.RETROFIT_HEADER) String content_type,
                                                @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                                @Header(Constants.RETROFIT_HEADER1) String token,
                                                @Header(Constants.RETROFIT_HEADER2) String userid,
                                                @Body Map<String, Object> userMap);

    @POST("user-team-players-details-with-powerplay")
    Call<JsonObject> UserTeamPlayersWithPowerPlay(@Header(Constants.RETROFIT_HEADER) String content_type,
                                                  @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                                  @Header(Constants.RETROFIT_HEADER1) String token,
                                                  @Header(Constants.RETROFIT_HEADER2) String userid,
                                                  @Body Map<String, Object> userMap);

    @POST("modify-user-team")
    Call<JsonObject> UpdateUserTeamName(@Header(Constants.RETROFIT_HEADER) String content_type,
                                        @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                        @Header(Constants.RETROFIT_HEADER1) String token,
                                        @Header(Constants.RETROFIT_HEADER2) String userid,
                                        @Body Map<String, Object> userMap);

    @POST("fetch-user-powerplay")
    Call<JsonObject> GetPowerplayLine(@Header(Constants.RETROFIT_HEADER) String content_type,
                                      @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                      @Header(Constants.RETROFIT_HEADER1) String token,
                                      @Header(Constants.RETROFIT_HEADER2) String userid,
                                      @Body Map<String, Object> userMap);

    @POST("user-team-players-details-with-stealthmode")
    Call<JsonObject> UserTeamPlayersWithStealthMode(@Header(Constants.RETROFIT_HEADER) String content_type,
                                                    @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                                    @Header(Constants.RETROFIT_HEADER1) String token,
                                                    @Header(Constants.RETROFIT_HEADER2) String userid,
                                                    @Body Map<String, Object> userMap);

    @POST("live-team-score-comparison")
    Call<JsonObject> LiveMatchScoreComparison(@Header(Constants.RETROFIT_HEADER) String content_type,
                                              @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                              @Header(Constants.RETROFIT_HEADER1) String token,
                                              @Header(Constants.RETROFIT_HEADER2) String userid,
                                              @Body Map<String, Object> userMap);

    @POST("team-points-comparison")
    Call<JsonObject> TeamPointsComparison(@Header(Constants.RETROFIT_HEADER) String content_type,
                                          @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                          @Header(Constants.RETROFIT_HEADER1) String token,
                                          @Header(Constants.RETROFIT_HEADER2) String userid,
                                          @Body Map<String, Object> userMap);

    @POST("get-tournament-players")
    Call<JsonObject> getTournamentPlayers(@Header(Constants.RETROFIT_HEADER) String content_type,
                                          @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                          @Header(Constants.RETROFIT_HEADER1) String token,
                                          @Header(Constants.RETROFIT_HEADER2) String userid,
                                          @Body Map<String, Object> userMap);

    @POST("user-team-players-last-cutoff")
    Call<JsonObject> getTeamLastPlayers(@Header(Constants.RETROFIT_HEADER) String content_type,
                                        @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                        @Header(Constants.RETROFIT_HEADER1) String token,
                                        @Header(Constants.RETROFIT_HEADER2) String userid,
                                        @Body Map<String, Object> userMap);

    @POST("user-team")
    Call<JsonObject> getUserTeamInfo(@Header(Constants.RETROFIT_HEADER) String content_type,
                                     @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                     @Header(Constants.RETROFIT_HEADER1) String token,
                                     @Header(Constants.RETROFIT_HEADER2) String userid,
                                     @Body Map<String, Object> userMap);

    @POST("team-selection-rule")
    Call<JsonObject> getTeamSelectionRule(@Header(Constants.RETROFIT_HEADER) String content_type,
                                          @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                          @Body Map<String, Object> userMap);

    @POST("fetch-static-urls")
    Call<JsonObject> getStaticURL(@Header(Constants.RETROFIT_HEADER) String content_type,
                                  @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype);

    @POST("save-team-selection")
    Call<JsonObject> saveMangeTeam(@Header(Constants.RETROFIT_HEADER) String content_type,
                                   @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                   @Header(Constants.RETROFIT_HEADER1) String token,
                                   @Header(Constants.RETROFIT_HEADER2) String userid,
                                   @Body Map<String, Object> userMap);

    @POST("verify-team-name")
    Call<JsonObject> VerifyTeamName(@Header(Constants.RETROFIT_HEADER) String content_type,
                                    @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                    @Header(Constants.RETROFIT_HEADER1) String token,
                                    @Body Map<String, Object> userMap);

    @POST("create-user-team")
    Call<JsonObject> CreateUserTeamName(@Header(Constants.RETROFIT_HEADER) String content_type,
                                        @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                        @Header(Constants.RETROFIT_HEADER1) String token,
                                        @Header(Constants.RETROFIT_HEADER2) String userid,
                                        @Body Map<String, Object> userMap);

    @POST("auto-select-team")
    Call<JsonObject> AutoFillTeam(@Header(Constants.RETROFIT_HEADER) String content_type,
                                  @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                  @Header(Constants.RETROFIT_HEADER1) String token,
                                  @Header(Constants.RETROFIT_HEADER2) String userid,
                                  @Body Map<String, Object> userMap);

    @POST("approve-league-users")
    Call<JsonObject> ApproveLeagueUser(@Header(Constants.RETROFIT_HEADER) String content_type,
                                       @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                       @Header(Constants.RETROFIT_HEADER1) String token,
                                       @Header(Constants.RETROFIT_HEADER2) String userid,
                                       @Body Map<String, Object> userMap);

    @POST("unapprove-league-users")
    Call<JsonObject> RemoveLeagueUser(@Header(Constants.RETROFIT_HEADER) String content_type,
                                      @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                      @Header(Constants.RETROFIT_HEADER1) String token,
                                      @Header(Constants.RETROFIT_HEADER2) String userid,
                                      @Body Map<String, Object> userMap);

    //------- User or Team stats -----------//
    @POST("user-stats-user-top-players")
    Call<JsonObject> getUserStatsTopPlayers(@Header(Constants.RETROFIT_HEADER) String content_type,
                                            @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                            @Header(Constants.RETROFIT_HEADER1) String token,
                                            @Header(Constants.RETROFIT_HEADER2) String userid,
                                            @Body Map<String, Object> userMap);

    @POST("user-stats-captain-points")
    Call<JsonObject> getUserStatsCaptainPoints(@Header(Constants.RETROFIT_HEADER) String content_type,
                                               @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                               @Header(Constants.RETROFIT_HEADER1) String token,
                                               @Header(Constants.RETROFIT_HEADER2) String userid,
                                               @Body Map<String, Object> userMap);

    @POST("league-stats-top-teams-top-perform")
    Call<JsonObject> getLeagueStatsTopPlayers(@Header(Constants.RETROFIT_HEADER) String content_type,
                                              @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                              @Header(Constants.RETROFIT_HEADER1) String token,
                                              @Header(Constants.RETROFIT_HEADER2) String userid,
                                              @Body Map<String, Object> userMap);

    @POST("league-stats-top-teams-top-favorite")
    Call<JsonObject> getLeagueStastTopTeams(@Header(Constants.RETROFIT_HEADER) String content_type,
                                            @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                            @Header(Constants.RETROFIT_HEADER1) String token,
                                            @Header(Constants.RETROFIT_HEADER2) String userid,
                                            @Body Map<String, Object> userMap);

    @POST("league-stats-global-top-leagues")
    Call<JsonObject> getTournamentStatsTopLeagues(@Header(Constants.RETROFIT_HEADER) String content_type,
                                                  @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                                  @Header(Constants.RETROFIT_HEADER1) String token,
                                                  @Header(Constants.RETROFIT_HEADER2) String userid,
                                                  @Body Map<String, Object> userMap);

    @POST("user-stats-global-top-teams")
    Call<JsonObject> getTournamentStastTopTeams(@Header(Constants.RETROFIT_HEADER) String content_type,
                                                @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                                @Header(Constants.RETROFIT_HEADER1) String token,
                                                @Header(Constants.RETROFIT_HEADER2) String userid,
                                                @Body Map<String, Object> userMap);

    @POST("user-stats-global-top-players")
    Call<JsonObject> getTournamentStastTopPlayers(@Header(Constants.RETROFIT_HEADER) String content_type,
                                                  @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                                  @Header(Constants.RETROFIT_HEADER1) String token,
                                                  @Header(Constants.RETROFIT_HEADER2) String userid,
                                                  @Body Map<String, Object> userMap);

    @POST("live-tournament-details")
    Call<JsonObject> getLiveTournamentList(@Header(Constants.RETROFIT_HEADER) String content_type,
                                           @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                           @Header(Constants.RETROFIT_HEADER1) String token,
                                           @Header(Constants.RETROFIT_HEADER2) String userid,
                                           @Body Map<String, Object> userMap);

    @POST("live-matches")
    Call<JsonObject> getLiveMatchList(@Header(Constants.RETROFIT_HEADER) String content_type,
                                      @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                      @Header(Constants.RETROFIT_HEADER1) String token,
                                      @Header(Constants.RETROFIT_HEADER2) String userid,
                                      @Body Map<String, Object> userMap);

    @POST("live-match-score")
    Call<JsonObject> getLiveMatchScore(@Header(Constants.RETROFIT_HEADER) String content_type,
                                       @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                       @Header(Constants.RETROFIT_HEADER1) String token,
                                       @Header(Constants.RETROFIT_HEADER2) String userid,
                                       @Body Map<String, Object> userMap);

    @POST("live-league-users")
    Call<JsonObject> getLiveLeagueUsers(@Header(Constants.RETROFIT_HEADER) String content_type,
                                        @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                        @Header(Constants.RETROFIT_HEADER1) String token,
                                        @Header(Constants.RETROFIT_HEADER2) String userid,
                                        @Body Map<String, Object> userMap);

    @POST("live-user-team-score")
    Call<JsonObject> getLiveTeamUsers(@Header(Constants.RETROFIT_HEADER) String content_type,
                                      @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                      @Header(Constants.RETROFIT_HEADER1) String token,
                                      @Header(Constants.RETROFIT_HEADER2) String userid,
                                      @Body Map<String, Object> userMap);

    @POST("distinct-team-filter")
    Call<JsonObject> getFilterTeamList(@Header(Constants.RETROFIT_HEADER) String content_type,
                                       @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                       @Body Map<String, Object> userMap);

    @Multipart
    @POST("upload-profile-image")
    Call<JsonObject> uploadProfileImage(@Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                        @Header(Constants.RETROFIT_HEADER1) String authorization,
                                        @Header(Constants.RETROFIT_HEADER2) String userid,
                                        @Part MultipartBody.Part multiparts
    );


    //--------------- Daily matches --------------------------//
    @POST("user-upcoming-daily-matches")
    Call<JsonObject> getUpcomingDailMatches(@Header(Constants.RETROFIT_HEADER) String content_type,
                                            @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                            @Header(Constants.RETROFIT_HEADER1) String authorization,
                                            @Header(Constants.RETROFIT_HEADER2) String userid,
                                            @Body Map<String, Object> userMap);

    @POST("user-daily-matches")
    Call<JsonObject> getDailyMatches(@Header(Constants.RETROFIT_HEADER) String content_type,
                                     @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                     @Header(Constants.RETROFIT_HEADER1) String authorization,
                                     @Header(Constants.RETROFIT_HEADER2) String userid,
                                     @Body Map<String, Object> userMap);

    @POST("daily-tournament-list")
    Call<JsonObject> getDailyTournamentList(@Header(Constants.RETROFIT_HEADER) String content_type,
                                            @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                            @Header(Constants.RETROFIT_HEADER1) String authorization,
                                            @Header(Constants.RETROFIT_HEADER2) String userid,
                                            @Body Map<String, Object> userMap);

    @POST("daily-league-teams")
    Call<JsonObject> getDailyLeagueList(@Header(Constants.RETROFIT_HEADER) String content_type,
                                        @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                        @Header(Constants.RETROFIT_HEADER1) String authorization,
                                        @Header(Constants.RETROFIT_HEADER2) String userid,
                                        @Body Map<String, Object> userMap);

    @POST("user-daily-team-players")
    Call<JsonObject> getDailyPlayers(@Header(Constants.RETROFIT_HEADER) String content_type,
                                     @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                     @Header(Constants.RETROFIT_HEADER1) String authorization,
                                     @Header(Constants.RETROFIT_HEADER2) String userid,
                                     @Body Map<String, Object> userMap);

    @POST("user-daily-team-players-with-points")
    Call<JsonObject> getDailyTeamPlayersWithPoints(@Header(Constants.RETROFIT_HEADER) String content_type,
                                                   @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                                   @Header(Constants.RETROFIT_HEADER1) String authorization,
                                                   @Header(Constants.RETROFIT_HEADER2) String userid,
                                                   @Body Map<String, Object> userMap);

    @POST("daily-matches-player-list")
    Call<JsonObject> getMatchPlayers(@Header(Constants.RETROFIT_HEADER) String content_type,
                                     @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                     @Header(Constants.RETROFIT_HEADER1) String authorization,
                                     @Header(Constants.RETROFIT_HEADER2) String userid,
                                     @Body Map<String, Object> userMap);

    @POST("daily-matches-team-selection-rules")
    Call<JsonObject> getMatchTeamRule(@Header(Constants.RETROFIT_HEADER) String content_type,
                                      @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                      @Header(Constants.RETROFIT_HEADER1) String authorization,
                                      @Header(Constants.RETROFIT_HEADER2) String userid,
                                      @Body Map<String, Object> userMap);

    @POST("save-daily-team-selection")
    Call<JsonObject> saveDailyTeam(@Header(Constants.RETROFIT_HEADER) String content_type,
                                   @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                   @Header(Constants.RETROFIT_HEADER1) String token,
                                   @Header(Constants.RETROFIT_HEADER2) String userid,
                                   @Body Map<String, Object> userMap);

    @POST("daily-live-league-users")
    Call<JsonObject> dailyLiveLeagueUsers(@Header(Constants.RETROFIT_HEADER) String content_type,
                                          @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                          @Header(Constants.RETROFIT_HEADER1) String token,
                                          @Header(Constants.RETROFIT_HEADER2) String userid,
                                          @Body Map<String, Object> userMap);


    @POST("fetch-wallet-info")
    Call<JsonObject> fetchWalletInfo(@Header(Constants.RETROFIT_HEADER) String content_type,
                                     @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                     @Header(Constants.RETROFIT_HEADER1) String token,
                                     @Header(Constants.RETROFIT_HEADER2) String userid,
                                     @Body Map<String, Object> userMap);

    @POST("fetch-notification-message")
    Call<JsonObject> fetchNotifications(@Header(Constants.RETROFIT_HEADER) String content_type,
                                        @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                        @Header(Constants.RETROFIT_HEADER1) String token,
                                        @Header(Constants.RETROFIT_HEADER2) String userid,
                                        @Body Map<String, Object> userMap);

    @POST("ack-notification-message")
    Call<JsonObject> deleteNotification(@Header(Constants.RETROFIT_HEADER) String content_type,
                                        @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                        @Header(Constants.RETROFIT_HEADER1) String token,
                                        @Header(Constants.RETROFIT_HEADER2) String userid,
                                        @Body Map<String, Object> userMap);

    @POST("fetch-total-rewards")
    Call<JsonObject> fetchTotalRewardsInfo(@Header(Constants.RETROFIT_HEADER) String content_type,
                                           @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                           @Header(Constants.RETROFIT_HEADER1) String token,
                                           @Header(Constants.RETROFIT_HEADER2) String userid,
                                           @Body Map<String, Object> userMap);

    @POST("fetch-total-claims")
    Call<JsonObject> fetchTotalClaimsInfo(@Header(Constants.RETROFIT_HEADER) String content_type,
                                          @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                          @Header(Constants.RETROFIT_HEADER1) String token,
                                          @Header(Constants.RETROFIT_HEADER2) String userid,
                                          @Body Map<String, Object> userMap);

    @POST("fetch-notification-count")
    Call<JsonObject> fetchNotificationCount(@Header(Constants.RETROFIT_HEADER) String content_type,
                                            @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                            @Header(Constants.RETROFIT_HEADER1) String token,
                                            @Header(Constants.RETROFIT_HEADER2) String userid,
                                            @Body Map<String, Object> userMap);

    @POST("get-fun-fact")
    Call<JsonObject> fetchFunFact(@Header(Constants.RETROFIT_HEADER) String content_type,
                                  @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                  @Header(Constants.RETROFIT_HEADER1) String token,
                                  @Header(Constants.RETROFIT_HEADER2) String userid);

    @POST("fetch-match-score")
    Call<JsonObject> fetchScoreCard(@Header(Constants.RETROFIT_HEADER) String content_type,
                                    @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                    @Header(Constants.RETROFIT_HEADER1) String token,
                                    @Header(Constants.RETROFIT_HEADER2) String userid,
                                    @Body Map<String, Object> userMap);

    @POST("fetch-referral-code")
    Call<JsonObject> fetchReferalCode(@Header(Constants.RETROFIT_HEADER) String content_type,
                                      @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                      @Header(Constants.RETROFIT_HEADER1) String token,
                                      @Header(Constants.RETROFIT_HEADER2) String userid,
                                      @Body Map<String, Object> userMap);

    @POST("last-match-top-performer-user")
    Call<JsonObject> fetchLastMatchTopPerformerUser(@Header(Constants.RETROFIT_HEADER) String content_type,
                                                    @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                                    @Header(Constants.RETROFIT_HEADER1) String token,
                                                    @Header(Constants.RETROFIT_HEADER2) String userid,
                                                    @Body Map<String, Object> userMap);

    @POST("fetch-player-stats")
    Call<JsonObject> getPlayerDetail(@Header(Constants.RETROFIT_HEADER) String content_type,
                                     @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
//                                        @Header(Constants.RETROFIT_HEADER1) String token,
//                                        @Header(Constants.RETROFIT_HEADER2) String userid,
                                     @Body Map<String, Object> userMap);


    @POST("save-login-preference")
    Call<JsonObject> saveLoginPreference(@Header(Constants.RETROFIT_HEADER) String content_type,
                                         @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                         @Header(Constants.RETROFIT_HEADER1) String token,
                                         @Header(Constants.RETROFIT_HEADER2) String userid,
                                         @Body Map<String, Object> userMap);

    @POST("orders")
    Call<JsonObject> initiateOrder(@Header(Constants.RETROFIT_HEADER) String content_type,
                                   @Header("Authorization") String authHeader,
                                   @Body Map<String, Object> map);

    @POST("fetch-kyc-details")
    Call<JsonObject> fetchKYCDetails(@Header(Constants.RETROFIT_HEADER) String content_type,
                                     @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                     @Header(Constants.RETROFIT_HEADER2) String userid,
                                     @Body Map<String, Object> map);

    @POST("fetch-bank-details")
    Call<JsonObject> fetchBankDetails(@Header(Constants.RETROFIT_HEADER) String content_type,
                                      @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                      @Header(Constants.RETROFIT_HEADER2) String userid,
                                      @Body Map<String, Object> map);

    @POST("update-kyc-details")
    Call<JsonObject> updateKYCDetails(@Header(Constants.RETROFIT_HEADER) String content_type,
                                      @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                      @Header(Constants.RETROFIT_HEADER2) String userid,
                                      @Body Map<String, Object> map);

    @POST("update-kyc-status")
    Call<JsonObject> updateKYCUploadStatus(@Header(Constants.RETROFIT_HEADER) String content_type,
                                           @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                           @Header(Constants.RETROFIT_HEADER2) String userid,
                                           @Body Map<String, Object> map);

    @Multipart
    @POST("upload-profile-image")
    Call<JsonObject> uploadKYCImage(@Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                    @Header(Constants.RETROFIT_HEADER_DOC_TYPE) String kycDoc,
                                    @Header(Constants.RETROFIT_HEADER1) String authorization,
                                    @Header(Constants.RETROFIT_HEADER2) String userid,
                                    @Part MultipartBody.Part multiparts);


    @POST("get-subscription-details")
    Call<JsonObject> fetchSubscriptionDetails(@Header(Constants.RETROFIT_HEADER) String content_type,
                                              @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                              @Header(Constants.RETROFIT_HEADER2) String userid,
                                              @Body Map<String, Object> map);

    @POST("update-subscription-details")
    Call<JsonObject> updateSubscriptionDetails(@Header(Constants.RETROFIT_HEADER) String content_type,
                                               @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                               @Header(Constants.RETROFIT_HEADER2) String userid,
                                               @Body Map<String, Object> map);

    @POST("send-verification-code")
    Call<JsonObject> sendVerificationCode(@Header(Constants.RETROFIT_HEADER) String content_type,
                                          @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                          @Header(Constants.RETROFIT_HEADER2) String userid,
                                          @Body Map<String, Object> map);

    @POST("update-bank-details")
    Call<JsonObject> updateBankDetails(@Header(Constants.RETROFIT_HEADER) String content_type,
                                       @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                       @Header(Constants.RETROFIT_HEADER2) String userid,
                                       @Body Map<String, Object> map);

    @POST("verify-otp")
    Call<JsonObject> verifyVerificationCode(@Header(Constants.RETROFIT_HEADER) String content_type,
                                            @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                            @Header(Constants.RETROFIT_HEADER2) String userid,
                                            @Body Map<String, Object> map);

    @POST("transfer-funds")
    Call<JsonObject> transferFund(@Header(Constants.RETROFIT_HEADER) String content_type,
                                  @Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,
                                  @Header(Constants.RETROFIT_HEADER2) String userid,
                                  @Body Map<String, Object> map);
    @POST("fetch-payment-gateway-details")
    Call<CasefeeResponse> fetchPaymentGatewayForCaseFree(@Header(Constants.RETROFIT_HEADER) String content_type,
                                                         @Body Map<String, Object> map);

    @POST("fetch-payment-gateway-details")
    Call<CashfreeResponse> fetchPaymentGatewayDetails(@Header(Constants.RETROFIT_HEADER) String content_type,@Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,@Body PaymentGatewayRequest request);

    @POST("add-money")
    Call<AddMoneyResponse> addMoney(@Header(Constants.RETROFIT_HEADER) String content_type,@Header(Constants.RETROFIT_HEADER_DEVICETYPE) String dtype,@Body AddMoneyRequest request);
}






