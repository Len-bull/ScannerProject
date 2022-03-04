package com.tdqc.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;

/**
 * SharedPreference相关操作类。
 * Created by chenyen on 2016/12/8.
 */
public class SpUtils {

    private static Context appContext;

    protected static void init(Context context){
        appContext = context.getApplicationContext();
    }

    public static SharedPreferences getSharedPreferences(String name){
        return appContext.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static void putString(String name, String key, String value){
        SharedPreferences.Editor editor = getSharedPreferences(name).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(String name, String key, String defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreferences(name);
        return sharedPreferences.getString(key, defaultValue);
    }

    public static void putInt(String name, String key, int value) {
        SharedPreferences.Editor editor = getSharedPreferences(name).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getInt(String name, String key, int defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreferences(name);
        return sharedPreferences.getInt(key, defaultValue);
    }

    public static void putLong(String name, String key, long value) {
        SharedPreferences.Editor editor = getSharedPreferences(name).edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static long getLong(String name, String key, long defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreferences(name);
        return sharedPreferences.getLong(key, defaultValue);
    }

    public static void putFloat(String name, String key, float value) {
        SharedPreferences.Editor editor = getSharedPreferences(name).edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public static float getFloat(String name, String key, float defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreferences(name);
        return sharedPreferences.getFloat(key, defaultValue);
    }

    public static void putBoolean(String name, String key, boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences(name).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBoolean(String name, String key, boolean defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreferences(name);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public static boolean hasKey(String name,final String key) {
        return getSharedPreferences(name).contains(key);
    }

    public static void remove(String name, String key){
        SharedPreferences.Editor editor = getSharedPreferences(name).edit();
        editor.remove(key).apply();
    }

    public static void clearPreference(@NonNull final SharedPreferences p) {
        p.edit().clear().apply();
    }

    public static void clearPreference(@NonNull String name){
        clearPreference(getSharedPreferences(name));
    }

    /**DefaultSharedPreferences操作*/
    public static void putString(final String key, final String value) {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(appContext);
        sharedPreferences.edit().putString(key, value).apply();
    }

    /**DefaultSharedPreferences操作*/
    public static String getString(String key, final String defaultValue) {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(appContext);
        return sharedPreferences.getString(key, defaultValue);
    }

    /**DefaultSharedPreferences操作*/
    public static void putBoolean(final String key, final boolean value) {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(appContext);
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    /**DefaultSharedPreferences操作*/
    public static boolean getBoolean(final String key, final boolean defaultValue) {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(appContext);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    /**DefaultSharedPreferences操作*/
    public static void putInt(final String key, final int value) {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(appContext);
        sharedPreferences.edit().putInt(key, value).apply();
    }

    /**DefaultSharedPreferences操作*/
    public static int getInt(final String key, final int defaultValue) {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(appContext);
        return sharedPreferences.getInt(key, defaultValue);
    }

    /**DefaultSharedPreferences操作*/
    public static void putFloat(final String key, final float value) {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(appContext);
        sharedPreferences.edit().putFloat(key, value).apply();
    }

    /**DefaultSharedPreferences操作*/
    public static float getFloat(final String key, final float defaultValue) {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(appContext);
        return sharedPreferences.getFloat(key, defaultValue);
    }

    /**DefaultSharedPreferences操作*/
    public static void putLong(final String key, final long value) {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(appContext);
        sharedPreferences.edit().putLong(key, value).apply();
    }

    /**DefaultSharedPreferences操作*/
    public static long getLong(final String key, final long defaultValue) {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(appContext);
        return sharedPreferences.getLong(key, defaultValue);
    }

    /**DefaultSharedPreferences操作*/
    public static boolean hasKey(final String key) {
        return PreferenceManager.getDefaultSharedPreferences(appContext).contains(key);
    }

    /**DefaultSharedPreferences操作*/
    public static void remove(String key){
        PreferenceManager.getDefaultSharedPreferences(appContext).edit().remove(key).apply();
    }

}
