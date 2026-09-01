package com.yorker.fanzania.views.screens.tournament.fragments.profilefragment.model;

public class ProfileModel {
    private String UserId;
    private String Name;
    private String Email;
    private String CountryId;
    private String Country;
    private String DOB;
    private String PhoneNumber;
    private String UserRoleId;
    private String SessionId;
    private String SessionActive;
    private String ProfileImage;
    private String ReferralCode;
    private int ReferralCount;
    private boolean CommPreference;
    private String BackgroundTheme;

    public String getBackgroundTheme() {
        return BackgroundTheme;
    }

    public String getCountry() {
        return Country;
    }

    public String getUserId() {
        return UserId;
    }

    public String getName() {
        return Name;
    }

    public String getEmail() {
        return Email;
    }

    public String getCountryId() {
        return CountryId;
    }

    public String getDOB() {
        return DOB;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public String getUserRoleId() {
        return UserRoleId;
    }

    public String getSessionId() {
        return SessionId;
    }

    public String getSessionActive() {
        return SessionActive;
    }

    public String getProfileImage() {
        return ProfileImage;
    }

    public int getReferralCount() {
        return ReferralCount;
    }

    public String getReferralCode() {
        return ReferralCode;
    }

    public boolean isCommPreference() {
        return CommPreference;
    }
}
