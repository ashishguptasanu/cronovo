package com.amit.cronovolibrary;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    private static SharedPreferences mSharedPref;

    private SharedPref() {

    }

    static void init(Context context) {
        if (mSharedPref == null)
            mSharedPref = context.getSharedPreferences(context.getPackageName(), context.MODE_PRIVATE);
    }


    public static Integer read(String key, int defValue) {
        return mSharedPref.getInt(key, defValue);
    }

    public static void write(String key, Integer value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putInt(key, value).apply();
    }
}