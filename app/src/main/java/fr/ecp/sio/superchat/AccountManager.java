package fr.ecp.sio.superchat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by Michaël on 12/12/2014.
 */
public class AccountManager {

    private static final String PREF_API_TOKEN = "apiToken";
    private static final String PREF_API_HANDLE = "apiHandle";

    public static boolean isConnected(Context context) {

        return getUserToken(context) != null;
    }

    public static String getUserToken(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        System.out.println("TOKEN AccountManage" + pref.getString(PREF_API_TOKEN, null));
        return pref.getString(PREF_API_TOKEN, null);
    }

    public static String getUserHandle(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(PREF_API_HANDLE, null);
    }

    public static void login(Context context, String token, String handle) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        pref.edit()
                .putString(PREF_API_TOKEN, token)
                .putString(PREF_API_HANDLE, handle)
                .apply();
    }

    public static void logout(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        pref.edit()
                .remove(PREF_API_TOKEN)
                .remove(PREF_API_HANDLE)
                .apply();
        }


    }



