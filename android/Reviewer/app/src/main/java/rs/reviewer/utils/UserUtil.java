package rs.reviewer.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class UserUtil {

    public static final String USER_ID = "user_id";

    public static void setLogInUser(String value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_ID, value);
        editor.commit();
    }

    public static String getLogInUser(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(USER_ID, null);
    }
}
