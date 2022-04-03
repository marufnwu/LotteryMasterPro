package com.skithub.resultdear.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import com.skithub.resultdear.utils.admob.MyInterstitialAd
import java.text.SimpleDateFormat
import java.util.*

object SharedPreUtils {

    var sharedPreferences: SharedPreferences?=null
    val sharedPreferenceName: String="MyPreference"

    private fun initSharedPref(context: Context): SharedPreferences {
        if (sharedPreferences==null) {
            sharedPreferences=context.getSharedPreferences(sharedPreferenceName,MODE_PRIVATE)
        }
        return sharedPreferences!!
    }

    suspend fun setStringToStorage(context: Context,key: String, value: String) {
        val editor: SharedPreferences.Editor=initSharedPref(context).edit()
        editor.putString(key,value)
        editor.apply()
    }

    @SuppressLint("SimpleDateFormat")
     fun setLastAdTimeToStorage(context: Context) {
        var adCount = getAdCountWithoutSuspend(context)
        val editor: SharedPreferences.Editor=initSharedPref(context).edit()
        val currentTimesInMill = System.currentTimeMillis()


        if(CommonMethod.getHoursDifBetweenToTime(currentTimesInMill, getLastAdTimeWithoutSuspend(context)) < MyInterstitialAd.AD_SHOW_HOUR) {
            //time less than 4 hour
            //show ad

            if(adCount < MyInterstitialAd.AD_SIZE){
                //ad size expired
                //let update ad size => 0
                    adCount++
                editor.putInt("AdCount", adCount)
            }
        }else{

            editor.putInt("AdCount", 1)
            editor.putLong("AdmobAdTime",currentTimesInMill)
        }
        editor.apply()
    }



     fun getLastAdTimeWithoutSuspend(context: Context) : Long {
        return initSharedPref(context).getLong("AdmobAdTime",0)
    }

     fun getAdCountWithoutSuspend(context: Context) : Int {
         val adCount = initSharedPref(context).getInt("AdCount",0)
         Log.d("Ad Count", adCount.toString())
        return adCount
    }

    suspend fun setBooleanToStorage(context: Context,key: String, value: Boolean) {
        val editor: SharedPreferences.Editor=initSharedPref(context).edit()
        editor.putBoolean(key,value)
        editor.apply()
    }

    suspend fun setIntToStorage(context: Context,key: String, value: Int) {
        val editor: SharedPreferences.Editor=initSharedPref(context).edit()
        editor.putInt(key,value)
        editor.apply()
    }

    suspend fun setLongToStorage(context: Context,key: String, value: Long) {
        val editor: SharedPreferences.Editor=initSharedPref(context).edit()
        editor.putLong(key,value)
        editor.apply()
    }

    suspend fun getLongFromStorage(context: Context,key: String, defaultValue: Long): Long {
        return initSharedPref(context).getLong(key,defaultValue)
    }

    suspend fun getStringFromStorage(context: Context,key: String, defaultValue: String?) : String? {
        return initSharedPref(context).getString(key,defaultValue)
    }

    fun getStringFromStorageWithoutSuspend(context: Context,key: String, defaultValue: String?) : String? {
        return initSharedPref(context).getString(key,defaultValue)
    }

    suspend fun getBooleanFromStorage(context: Context,key: String, defaultValue: Boolean) : Boolean {
        return initSharedPref(context).getBoolean(key,defaultValue)
    }

    suspend fun getIntFromStorage(context: Context,key: String, defaultValue: Int) : Int {
        return initSharedPref(context).getInt(key,defaultValue)
    }



}