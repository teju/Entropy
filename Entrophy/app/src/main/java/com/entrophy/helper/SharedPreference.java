package com.entrophy.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by tejaswini on 29/03/2019.
 */

public class SharedPreference {
    private static SharedPreferences.Editor getEditor(Context context) {
        return SharedPreference.getSharedPref(context).edit();
    }

    public static SharedPreferences getSharedPref(Context context) {
        return context.getSharedPreferences("WIN_FERTILITY_DATA", Context.MODE_PRIVATE);
    }

    public static void setString(Context context, String key, String value) {
        SharedPreferences.Editor editor = SharedPreference.getEditor(context);
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(Context context, String key) {
        try {
            return SharedPreference.getSharedPref(context).getString(key, "");
        }
        catch(Exception ex) {
            System.out.println("SharedPreferences1234 Exception "+ex.toString());

        }
        return "";
    }
    public static void setInt(Context context, String key, int value) {
        SharedPreferences.Editor editor = SharedPreference.getEditor(context);
        editor.putInt(key, value);
        editor.apply();
        System.out.println("SharedPreferences1234 setInt key "+key+" value "+value);

    }

    public static void setBool(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = SharedPreference.getEditor(context);
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBool(Context context, String key) {
        try {
            return SharedPreference.getSharedPref(context).getBoolean(key, false);
        }
        catch(Exception ex) {
        }
        return false;
    }

    public static int getInt(Context context, String key) {
        try {
            return SharedPreference.getSharedPref(context).getInt(key, 0);
        }
        catch(Exception ex) {
        }
        return 0;
    }
    public static void clear(Context context) {
        SharedPreferences.Editor editor = SharedPreference.getEditor(context);
        editor.clear();
        editor.apply();
    }
    public static void clearKEy(Context context,String Key) {
            SharedPreferences.Editor editor = SharedPreference.getEditor(context);
        editor.remove(Key);

        editor.commit();

        }

}
