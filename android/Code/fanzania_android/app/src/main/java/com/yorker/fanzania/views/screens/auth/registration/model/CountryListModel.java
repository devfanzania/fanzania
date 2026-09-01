package com.yorker.fanzania.views.screens.auth.registration.model;

public class CountryListModel {

    private String CountryId;
    private String Country;
    private Boolean Active;
    private Boolean isSelected;

    public CountryListModel(String countryId, String country, Boolean active, Boolean isselected) {
        CountryId = countryId;
        Country = country;
        Active = active;
        isSelected = isselected;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public String getCountryId() {
        return CountryId;
    }

    public String getCountry() {
        return Country;
    }

    public Boolean getActive() {
        return Active;
    }
}
