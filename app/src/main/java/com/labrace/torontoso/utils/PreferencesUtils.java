package com.labrace.torontoso.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Dung on 4/7/2016.
 */
public class PreferencesUtils {
    private static final String PREF_NAME = "TorsoCAD";

    public static void saveData(Activity activity, String key, String value) {
        SharedPreferences sharedPref = activity.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String loadData(Activity activity, String key) {
        SharedPreferences sharedPref = activity.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPref.getString(key, "0");
    }
}
