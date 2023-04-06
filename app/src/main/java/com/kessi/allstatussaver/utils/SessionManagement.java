package com.kessi.allstatussaver.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagement {
    SharedPreferences pref;
    Context _context;
    int PRIVATE_MODE = 0;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    private static final String PREF_NAME = "AndroidHivePref";
    private static final String IS_LOGIN = "IsLoggedIn";
    public SessionManagement(Context context) {
        this._context = context;
        if(!(_context == null)) {
            pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
            editor = pref.edit();
        }
    }
    public void setString(String key, String string) {
        editor.putString(key, string);
        editor.commit();
    }

    public String getString(String key) {
        return pref.getString(key, null);
    }
    public boolean isLoggedIn(){

        return pref.getBoolean(IS_LOGIN, false);
    }
    public void clearallPref(){
        editor.clear().commit();
    }
    public boolean checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {

            return false;
        } else {
            return true;
        }

    }
}
