package com.samprama.mobileapp.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.samprama.mobileapp.viewmodel.Entry
import com.samprama.mobileapp.viewmodel.LoginData
import java.util.*

object PreferenceUtil {

    private const val TAG = "appDataPreferences"
    private const val appPreferences = "appPreferences"
    private const val appLoginPreferences = "appLoginPreferences"
    private const val appLoginPreferencesKey = "appLoginPreferencesKey"
    private const val appSettingPreferences = "appSettingPreferences"
    private const val appAutoJobPreferences = "appAutoJobPreferences"
    private const val appTotalCollectionDataPreferences = "appTotalCollectionDataPreferences"

    private var sharedLoginPreferences: SharedPreferences? = null
    private var sharedSettingPreferences: SharedPreferences? = null
    private var sharedAppPreferences: SharedPreferences? = null

    fun getAppSharedPreferences(context: Context): SharedPreferences {
        if (sharedAppPreferences == null) {
            sharedAppPreferences = context.getSharedPreferences(appPreferences, Context.MODE_PRIVATE)
        }
        return sharedAppPreferences!!
    }

    fun getLoginSharedPreferences(context: Context): SharedPreferences {
        if (sharedLoginPreferences == null) {
            sharedLoginPreferences = context.getSharedPreferences(appLoginPreferences, Context.MODE_PRIVATE)
        }
        return sharedLoginPreferences!!
    }

    fun getSettingSharedPreferences(context: Context): SharedPreferences {
        if (sharedSettingPreferences == null) {
            sharedSettingPreferences = context.getSharedPreferences(appSettingPreferences, Context.MODE_PRIVATE)
        }
        return sharedSettingPreferences!!
    }

    fun setLoginDataPreferences(context: Context, userModel: LoginData) {
        val gson = Gson()
        val data = gson.toJson(userModel)
        Log.i(TAG, "Set user model data")
        Log.i(TAG, data)
        val editor = getLoginSharedPreferences(context).edit()
        editor.putString(appLoginPreferencesKey, data)
        editor.apply()
    }

    fun getLoginDataPreferences(context: Context): LoginData? {
        val data = getLoginSharedPreferences(context).getString(appLoginPreferencesKey, null)
        val gson = Gson()
        val userModel = gson.fromJson(data, LoginData::class.java)
        Log.i(TAG, "get user model data")
        if (data != null) {
            Log.i(TAG, data)
        }
        return userModel
    }

    fun clearLoginDataPreferences(context: Context): Boolean {
        val editor = getLoginSharedPreferences(context).edit()
        editor.clear()
        editor.apply()
        Log.i(TAG, "Clear login Preferences")
        return true
    }

    fun clearDataPreferences(context: Context): Boolean {
        val editor = getAppSharedPreferences(context).edit()
        editor.clear()
        editor.apply()
        Log.i(TAG, "Clear login Preferences")
        return true
    }

    fun getTotalCollectionDataPreferences(context: Context): List<Entry> {
        val data = getAppSharedPreferences(context).getString(appTotalCollectionDataPreferences, "")
        val gson = Gson()
        val entry = Arrays.asList(*gson.fromJson(data, Array<Entry>::class.java))
        Log.i(TAG, "get found trays  data $data")
        return entry
    }

    fun setTotalCollectionDataPreferences(context: Context, entry: ArrayList<Entry>) {
        val gson = Gson()
        val data = gson.toJson(entry)
        Log.i(TAG, "Set found trays data")
        Log.i(TAG, data)
        val editor = getAppSharedPreferences(context).edit()
        editor.putString(appTotalCollectionDataPreferences, data)
        editor.apply()
    }
}
