package com.skithub.resultdear.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Insets
import android.media.Image
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.Glide
import com.skithub.resultdear.R
import com.skithub.resultdear.database.network.MyApi
import com.skithub.resultdear.model.response.Banner
import com.skithub.resultdear.ui.PlayerActivity
import com.skithub.resultdear.ui.webview.WebViewActivity
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


object CommonMethod {

    var accountAge : String? = null

    fun increaseDecreaseDaysUsingValue(days: Int, locale: Locale): String {
        val calendar: Calendar=Calendar.getInstance()
        val simpleDateFormat=SimpleDateFormat(Constants.dayFormat,locale)
        calendar.set(Calendar.DAY_OF_YEAR,calendar.get(Calendar.DAY_OF_YEAR)+days)
        return simpleDateFormat.format(calendar.time)
    }

    fun getCurrentTimeUsingFormat(format: String,locale: Locale): String {
        val dateFormat: SimpleDateFormat = SimpleDateFormat(format, locale)
        return dateFormat.format(Date()).toString()
    }

    fun getCurrentTime(): String {
        val date = Date()
        return date.time.toString()
    }

    fun getDayNameUsingDate(date: String,locale: Locale): String {
        try {
            val format=SimpleDateFormat(Constants.dayFormat, locale)
            val formattedDate=format.parse(date)
            val simpleDateFormat=SimpleDateFormat("EEEE", locale)
            return simpleDateFormat.format(formattedDate)
        } catch (e: Exception) {
            return "day name not found"
        }
    }

    fun getHoursDifBetweenToTime(startTime: String, endTime: String): String? {
        return try {
            val date1 = startTime.toLong()
            val date2 = endTime.toLong()
            val difference = date2 - date1
            val differenceDates = difference / (60 * 60 * 1000)
            differenceDates.toString()
        } catch (exception: Exception) {
            null
        }
    }
    fun getHoursDifBetweenToTime(startTime: Long, endTime: Long): Long {
        val date1 = startTime.toLong()
        val date2 = endTime.toLong()
        val difference = date1-date2
        val d = TimeUnit.MILLISECONDS.toHours(difference)
        Log.d("c", d.toString())
        return d
    }


    fun getMinuteDifBetweenToTime(startTime: String, endTime: String): String? {
        return try {
            val date1 = startTime.toLong()
            val date2 = endTime.toLong()
            val difference = date2 - date1
            val differenceDates = difference / (60 * 1000)
            differenceDates.toString()
        } catch (exception: Exception) {
            null
        }
    }

    fun getMimeTypeFromUrl(url: String): String? {
        var type: String?=null
        val typeExtension: String=MimeTypeMap.getFileExtensionFromUrl(url)
        if (!typeExtension.isNullOrEmpty()) {
            type=MimeTypeMap.getSingleton().getMimeTypeFromExtension(typeExtension)
        }
        return type
    }

    fun openAppLink(context: Context) {
        val appPackageName: String=context.applicationContext.packageName
        try {
            val appIntent: Intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName"))
            appIntent.setPackage("com.android.vending")
            context.startActivity(appIntent)
        } catch (e: ActivityNotFoundException) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
        }
    }

    fun shareAppLink(context: Context) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=${context.applicationContext.packageName}")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }

    fun openConsoleLink(context: Context,consoleId: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_VIEW
            data=Uri.parse("https://play.google.com/store/apps/developer?id=$consoleId")
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }


    fun openVideo(context: Context, videoId: String) {
        val appIntent: Intent= Intent(Intent.ACTION_VIEW,Uri.parse("vnd.youtube:$videoId"))
        try {
            context.startActivity(appIntent)
        } catch (e: Exception) {
            val webIntent: Intent= Intent(Intent.ACTION_VIEW,Uri.parse("http://www.youtube.com/watch?v=$videoId"))
            context.startActivity(Intent.createChooser(webIntent,"Choose one:"))
        }
    }

    fun haveInternet(connectivityManager: ConnectivityManager?): Boolean {
        return when {
            connectivityManager==null -> {
                false
            }
            Build.VERSION.SDK_INT >= 23 -> {
                val network = connectivityManager.activeNetwork
                val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || capabilities.hasTransport(
                    NetworkCapabilities.TRANSPORT_WIFI
                )
            }
            else -> {
                (connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isAvailable
                        && connectivityManager.activeNetworkInfo!!.isConnected)
            }
        }
    }

    fun getScreenWidth(activity: Activity): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = activity.windowManager.currentWindowMetrics
            val insets: Insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.width() - insets.left - insets.right
        } else {
            val displayMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.widthPixels
        }
    }

    fun getScreenHeight(activity: Activity): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = activity.windowManager.currentWindowMetrics
            val insets: Insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.width() - insets.left - insets.right
        } else {
            val displayMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.heightPixels
        }
    }

    fun updateLanguage(context: Context): Context {
        val lanCode: String= SharedPreUtils.getStringFromStorageWithoutSuspend(context,Constants.appLanguageKey,Constants.appDefaultLanCode)!!
        val local: Locale= Locale(lanCode)
        val res: Resources=context.resources
        val config: Configuration=res.configuration

        Locale.setDefault(local)
        config.setLocale(local)
        config.setLayoutDirection(local)
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N) {
            return context.createConfigurationContext(config)
        } else {
            config.locale=local
            res.updateConfiguration(config,res.displayMetrics)
            return context
        }
    }

    fun dpToPix(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,context.resources.displayMetrics).toInt()
    }

    fun subStringFromString(value: String, length: Int): String {
        return value.substring(0,value.length-length)
    }

    suspend fun  getBanner(bannerName:String, imageView: ImageView, myApi: MyApi, context: Context) {
      try {
          val res = myApi.getBanner(bannerName)

          if(res.isSuccessful && res.body()!=null){
              val banner = res.body()
              if(!banner!!.error){
                  if (banner.imageUrl != null) {
                      imageView.visibility = View.VISIBLE

                      Glide.with(context)
                          .load(banner.imageUrl)
                          .thumbnail(Glide.with(context).load(R.drawable.placeholder))
                          .into(imageView)
                      imageView.setOnClickListener(View.OnClickListener {
                          if (banner.actionType == 1) {
                              //open url
                              if (banner.actionUrl != null) {
                                  val url: String = banner.actionUrl!!
                                  val linkHost = Uri.parse(url).host
                                  val uri = Uri.parse(url)
                                  if (linkHost == null) {
                                      return@OnClickListener
                                  }
                                  if (linkHost == "play.google.com") {
                                      val appId = uri.getQueryParameter("id")
                                      val intent = Intent(Intent.ACTION_VIEW)
                                      intent.data = Uri.parse("market://details?id=$appId")
                                      context.startActivity(intent)
                                  } else if (linkHost == "www.youtube.com") {
                                      try {
                                          val intent = Intent(Intent.ACTION_VIEW, uri)
                                          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                          intent.setPackage("com.google.android.youtube")
                                          context.startActivity(intent)
                                      }catch (e : Exception){
                                          val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                          context.startActivity(intent)
                                      }
                                  }else if(url.endsWith(".mp4") || url.endsWith(".mpeg") || url.endsWith(".mpd") ||
                                      url.startsWith("https://lmpclass.sikderithub.com/embed") || url.startsWith("http://lmpclass.sikderithub.com/embed")){
                                      val intent = Intent(context, PlayerActivity::class.java)
                                      intent.putExtra("url", url)
                                      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                      context.startActivity(intent)
                                  } else if(url.startsWith("https://lmpclass.sikderithub.com") || url.startsWith("http://lmpclass.sikderithub.com")){
                                      val intent = Intent(context, WebViewActivity::class.java)
                                      intent.putExtra("url", url)
                                      context.startActivity(intent)
                                  }else if (url != null && (url.startsWith("http://") || url.startsWith(
                                          "https://"
                                      ))
                                  ) {
                                      val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                      context.startActivity(intent)
                                  }
                              }
                          } else if (banner.actionType === 2) {
                              //open activity
                          }
                      })

                  }
              }else{
                  Log.d("Banner", banner.msg!!)
              }
          }
      }catch (e : Exception){
          Toast.makeText(context, "Something went wrong, related to your network issue.", Toast.LENGTH_LONG).show()
      }
    }
    fun setShakeAnimation(img: ImageView, context: Context?) {
        img.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake))
    }

    fun openWhatsapp(ctx:Context, whatsAppBtn:View, mobile:String){
       whatsAppBtn.setOnClickListener {
            try {
                val msg = ""
                ctx.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://api.whatsapp.com/send?phone=$mobile&text=$msg")
                    )
                )
            } catch (e: java.lang.Exception) {
                Toast.makeText(
                    ctx,
                    "WhatsApp not Installed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun openLink(context: Context, url:String){
        val linkHost = Uri.parse(url).host
        val uri = Uri.parse(url)
        if (linkHost == null) {
            return
        }
        if (linkHost == "play.google.com") {
            val appId = uri.getQueryParameter("id")
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("market://details?id=$appId")
            context.startActivity(intent)
        } else if (linkHost == "www.youtube.com") {
            try {
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.setPackage("com.google.android.youtube")
                context.startActivity(intent)
            }catch (e : Exception){
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
        }else if(url.endsWith(".mp4") || url.endsWith(".mpeg") || url.endsWith(".mpd") ||
            url.startsWith("https://lmpclass.sikderithub.com/embed") || url.startsWith("http://lmpclass.sikderithub.com/embed")){
            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra("url", url)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }else if(url.startsWith("https://lmpclass.sikderithub.com") || url.startsWith("http://lmpclass.sikderithub.com")){
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra("url", url)
            context.startActivity(intent)
        }
        else if (url != null && (url.startsWith("http://") || url.startsWith(
                "https://"
            ))
        ) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    fun keepScreenOn(ctx:Activity){
        ctx.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    fun isVideoAppInstalled(context: Context): Boolean {
        val uri = "com.lmp.video"
        val pm = context.packageManager
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return false
    }

    fun openMediaPlayerApp(ctx:Context){
        val intent =  Intent(android.content.Intent.ACTION_VIEW);

        intent.data = Uri.parse("https://play.google.com/store/apps/details?id=com.lmp.video");

        ctx.startActivity(intent);
    }

    fun getLotteryMiddle(number:String): String{
        return number.toCharArray(number.length-4, (number.length-4)+2).joinToString("")
    }

    fun  getLast2digit(number:String) : String{
        return number.toCharArray(number.length-2, (number.length)).joinToString("")
    }

    fun loadBannerWithClick(activity:Activity, banner : Banner, imageView:ImageView){
       if(!activity.isFinishing && !activity.isDestroyed){
           Glide.with(activity)
               .load(banner.imageUrl)
               .thumbnail(Glide.with(activity).load(R.drawable.placeholder))
               .into(imageView)

           imageView.setOnClickListener {
               banner.actionUrl?.let {url->
                   openLink(activity, url)
               }
           }
       }
    }

    fun getAccountAgeInDays(){

    }



}