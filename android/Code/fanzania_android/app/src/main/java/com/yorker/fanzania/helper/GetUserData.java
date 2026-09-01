package com.yorker.fanzania.helper;

import com.google.gson.Gson;
import com.yorker.fanzania.views.screens.tournament.fragments.profilefragment.model.ProfileModel;
import com.yorker.fanzania.views.shared.model.UserDetailsModel;

public class GetUserData {

    private Gson gson;

    public GetUserData() {
        gson = new Gson();
    }

    public UserDetailsModel getUserData(String data) {

        return gson.fromJson(data, UserDetailsModel.class);
    }

    public ProfileModel getProfileData(String data) {

        return gson.fromJson(data, ProfileModel.class);
    }

}
