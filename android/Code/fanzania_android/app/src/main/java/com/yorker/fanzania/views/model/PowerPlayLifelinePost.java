package com.yorker.fanzania.views.model;

public class PowerPlayLifelinePost {
    public int UserTeamId;
    public int UserId;
    public int TournamentId;
    public int NitroUserTeamMatchPointId;
    public int PainKillerUserTeamMatchPointId;

    public int getUserTeamId() {
        return UserTeamId;
    }

    public void setUserTeamId(int userTeamId) {
        UserTeamId = userTeamId;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getTournamentId() {
        return TournamentId;
    }

    public void setTournamentId(int tournamentId) {
        TournamentId = tournamentId;
    }

    public int getNitroUserTeamMatchPointId() {
        return NitroUserTeamMatchPointId;
    }

    public void setNitroUserTeamMatchPointId(int nitroUserTeamMatchPointId) {
        NitroUserTeamMatchPointId = nitroUserTeamMatchPointId;
    }

    public int getPainKillerUserTeamMatchPointId() {
        return PainKillerUserTeamMatchPointId;
    }

    public void setPainKillerUserTeamMatchPointId(int painKillerUserTeamMatchPointId) {
        PainKillerUserTeamMatchPointId = painKillerUserTeamMatchPointId;
    }

    public int getAutoPilotUserTeamMatchPointId() {
        return AutoPilotUserTeamMatchPointId;
    }

    public void setAutoPilotUserTeamMatchPointId(int autoPilotUserTeamMatchPointId) {
        AutoPilotUserTeamMatchPointId = autoPilotUserTeamMatchPointId;
    }

    public boolean isNitroSelect() {
        return NitroSelect;
    }

    public void setNitroSelect(boolean nitroSelect) {
        NitroSelect = nitroSelect;
    }

    public boolean isPainKillerSelect() {
        return PainKillerSelect;
    }

    public void setPainKillerSelect(boolean painKillerSelect) {
        PainKillerSelect = painKillerSelect;
    }

    public boolean isAutoPilotSelect() {
        return AutoPilotSelect;
    }

    public void setAutoPilotSelect(boolean autoPilotSelect) {
        AutoPilotSelect = autoPilotSelect;
    }

    public int getNitroPoints() {
        return NitroPoints;
    }

    public void setNitroPoints(int nitroPoints) {
        NitroPoints = nitroPoints;
    }

    public int getPainKillerPoints() {
        return PainKillerPoints;
    }

    public void setPainKillerPoints(int painKillerPoints) {
        PainKillerPoints = painKillerPoints;
    }

    public int getAutoPilotPoints() {
        return AutoPilotPoints;
    }

    public void setAutoPilotPoints(int autoPilotPoints) {
        AutoPilotPoints = autoPilotPoints;
    }

    public int AutoPilotUserTeamMatchPointId;
    public boolean NitroSelect;
    public boolean PainKillerSelect;
    public boolean AutoPilotSelect;
    public int NitroPoints;
    public int PainKillerPoints;
    public int AutoPilotPoints;
}
