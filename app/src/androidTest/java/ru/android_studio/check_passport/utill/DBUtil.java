package ru.android_studio.check_passport.utill;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Утилиты для работы с БД в тестах
 */
public class DBUtil {

    /*
    * Очистка Базы данных
    * */
    public static void clearAppInfo(Activity activity) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        preferences.edit().clear().apply();
        activity.deleteDatabase("fms-service.db");
    }
}
