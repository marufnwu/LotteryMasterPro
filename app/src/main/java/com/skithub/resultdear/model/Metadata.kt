package com.skithub.resultdear.model

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.os.Build.VERSION_CODES
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import android.view.WindowManager
import com.skithub.resultdear.BuildConfig
import com.skithub.resultdear.ui.MyApplication
import com.skithub.resultdear.ui.main.MainActivity
import com.skithub.resultdear.utils.Constants
import com.skithub.resultdear.utils.Coroutines
import com.skithub.resultdear.utils.SharedPreUtils

class Metadata(val context: Context) {

    val versionCode : Int = BuildConfig.VERSION_CODE
    val versionName : String = BuildConfig.VERSION_NAME
    var androidVersion : String? = null
    val device : String = Build.MODEL
    val manufacturer : String = Build.MANUFACTURER
    val deviceType : String = Build.PRODUCT
    var screenDensity : String? = null
    var screenSize : String? = null
    //val deviceLanguage : String? = null

    init {
        screenDensity = context.resources.displayMetrics.densityDpi.toString()+" dpi"
        screenSize = getSize()


        val sdk = Build.VERSION.SDK_INT
        androidVersion = when(sdk){
            Build.VERSION_CODES.M -> "Android 6"
            Build.VERSION_CODES.N -> "Android 7"
            Build.VERSION_CODES.N_MR1 -> "Android 7.1"
            Build.VERSION_CODES.O ->  "Android 8"
            Build.VERSION_CODES.O_MR1 ->  "Android 8.1"
            Build.VERSION_CODES.P ->  "Android 9"
            Build.VERSION_CODES.Q -> "Android 10"
            Build.VERSION_CODES.S -> "Android 12"
            else -> "Unknown android version"
        }

    }

    fun print(){
        Log.d("versionCode", versionCode.toString())
        Log.d("versionName", versionName)
        Log.d("androidVersion", androidVersion!!)
        Log.d("device", device)
        Log.d("deviceType", deviceType)
        Log.d("manufacturer", manufacturer)
        Log.d("screenDensity", screenDensity!!)
        Log.d("screenSize", screenSize!!)
    }

    fun getSize():String{
        val w: WindowManager = (context as MainActivity).getWindowManager()
        val d: Display = w.defaultDisplay
        val metrics = DisplayMetrics()
        d.getMetrics(metrics)

        var  widthPixels = metrics.widthPixels
        var  heightPixels = metrics.heightPixels

        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17) try {
            widthPixels = Display::class.java.getMethod("getRawWidth").invoke(d) as Int
            heightPixels = Display::class.java.getMethod("getRawHeight").invoke(d) as Int
        } catch (ignored: java.lang.Exception) {
            return "Not Found"
        }

        if (Build.VERSION.SDK_INT >= 17) try {
            val realSize = Point()
            Display::class.java.getMethod("getRealSize", Point::class.java).invoke(d, realSize)
            widthPixels = realSize.x
            heightPixels = realSize.y
        } catch (ignored: java.lang.Exception) {
            return "Not Found"
        }

        return heightPixels.toString()+" x "+widthPixels.toString()
    }



}