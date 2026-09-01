package com.yorker.fanzania.helper.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.yorker.fanzania.constants.Constants;

public class SharedPrefManager {

    private Context mCtx;

    private static SharedPrefManager mInstance;

    public SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //--------------Player Tooltip preference ---------------//

    public int getPTooltip(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_TOOLTIP_DIALOG, Context.MODE_PRIVATE);
        return  sharedPreferences.getInt("value",0);
    }

    public boolean setPTooltip(int value){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_TOOLTIP_DIALOG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("value", value);
        editor.apply();
        return true;
    }

    //--------------Header preference ---------------//

    public int getHeaderPref(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_Header, Context.MODE_PRIVATE);
        return  sharedPreferences.getInt("value",0);
    }

    public boolean setHeaderPref(int value){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_Header, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("value", value);
        editor.apply();
        return true;
    }

    public void claerHeaderPref() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_Header, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    //--------------League Tooltip preference ---------------//

    public int getLTooltip(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_LTOOLTIP_DIALOG, Context.MODE_PRIVATE);
        return  sharedPreferences.getInt("value",0);
    }

    public boolean setLTooltip(int value){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_LTOOLTIP_DIALOG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("value", value);
        editor.apply();
        return true;
    }

    //--------------Mc PLayerSelection Tooltip preference ---------------//

    public int getMcPTooltip(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_MCPTOOLTIP_DIALOG, Context.MODE_PRIVATE);
        return  sharedPreferences.getInt("value",0);
    }

    public boolean setMcpTooltip(int value){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_MCPTOOLTIP_DIALOG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("value", value);
        editor.apply();
        return true;
    }

    //-------------- Update Dialog preference ---------------//

    public Boolean getUpdateDialog(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_DIALOG, Context.MODE_PRIVATE);
        return  sharedPreferences.getBoolean("value",false);
    }

    public boolean setUpdateDialog(Boolean value){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_DIALOG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("value", value);
        editor.apply();
        return true;
    }

    //------------ League Pref ------------------//

    public void saveLeagueId(String value) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_LEAGUE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.TAG_LEAGUEID, value);
        editor.apply();
    }

    public String getLeagueId() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_LEAGUE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.TAG_LEAGUEID, null);
    }

    public void claerLeagueId() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_LEAGUE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    //------------ Tournament Pref ------------------//

    public void saveTournamentId(String value) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_TOURNAMENT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.TAG_TOURNAMENTID, value);
        editor.apply();
    }

    public String getTournamentId() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_TOURNAMENT, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.TAG_TOURNAMENTID, null);
    }

    public void claerTournamentId() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.TAG_TOURNAMENTID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    //------------ Static URL ------------------//

    public void saveURL(String value, String tag) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_STATIC_URL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(tag, value);
        editor.apply();
    }

    public String getURL(String tag) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_STATIC_URL, Context.MODE_PRIVATE);
        return sharedPreferences.getString(tag, null);
    }

    //------------ Auth token ------------------//

    public void saveAuthToken(String value) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.SHARED_TOKEN, value);
        editor.apply();
    }

    public String getAuthToken() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_LOGIN, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.SHARED_TOKEN, null);
    }

    //------------ Auth MODE ------------------//

    public void saveAuthMode(String value) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.SHARED_MODE, value);
        editor.apply();
    }

    public String getAuthMode() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_LOGIN, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.SHARED_MODE, null);
    }

    //--------------User Information---------------//

    public void saveCustomer_Id(String customer_id) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.TAG_ID, customer_id);
        editor.apply();
    }

    public void saveCustomerName(String name) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.TAG_NAME, name);
        editor.apply();
    }

    public void saveCustomer_COnnectionID(String connectionID) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.TAG_CONNECTIONID, connectionID);
        editor.apply();
    }

    public void saveCustomer_Email(String email) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.TAG_EMAIL, email);
        editor.apply();
    }

    public void saveCustomer_Profile(String profile) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.TAG_PROFILE_IMAGE, profile);
        editor.apply();
    }

    public void saveCustomer_Phone(String phoneNumber) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.TAG_PHONENUMBER, phoneNumber);
        editor.apply();
    }

    public void saveCustomer_LoginPreference(String email) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.TAG_LOGIN_PREFERENCE, email);
        editor.apply();
    }

    public void saveCustomer_Password(String password) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.TAG_PASS, password);
        editor.apply();
    }

    public String getCustomer_Email() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_LOGIN, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.TAG_EMAIL, null);
    }

    public String getCustomer_Profile() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_LOGIN, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.TAG_PROFILE_IMAGE, null);
    }

    public String getCustomer_Phone() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_LOGIN, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.TAG_PHONENUMBER, null);
    }

    public String getCustomer_LoginPreference() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_LOGIN, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.TAG_LOGIN_PREFERENCE, null);
    }

    public String getCustomer_ConnectionID() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_LOGIN, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.TAG_CONNECTIONID, null);
    }

    public String getCustomer_Password() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_LOGIN, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.TAG_PASS, null);
    }

    public String getCustomer_Id() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_LOGIN, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.TAG_ID, null);
    }

    public String getCustomerName() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_LOGIN, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.TAG_NAME, null);
    }

    public void claerCustomerData() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    //-------------- Device Token--------------//

    public void saveDeviceTokenno(String value) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_DEVICE_TOKEN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.SHARED_PREF_NAME, value);
        editor.apply();
        editor.commit();
    }

    public String getDeviceToken() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_DEVICE_TOKEN, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.SHARED_PREF_NAME, null);
    }

    //------------------ Tooltip --------------//
    public boolean saveDialog(Boolean value){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_TOOLTIP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constants.SHARED_TOOLTIP, value);
        editor.apply();
        return true;
    }

    public Boolean getDialog(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_TOOLTIP, Context.MODE_PRIVATE);
        return  sharedPreferences.getBoolean(Constants.SHARED_TOOLTIP,false);
    }

}
