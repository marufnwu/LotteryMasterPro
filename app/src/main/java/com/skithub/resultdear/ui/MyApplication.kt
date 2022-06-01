package com.skithub.resultdear.ui

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.akexorcist.screenshotdetection.ScreenshotDetectionDelegate
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.skithub.resultdear.R
import com.skithub.resultdear.database.network.MyApi
import com.skithub.resultdear.database.network.api.RetrofitClient
import com.skithub.resultdear.database.network.api.SecondServerApi
import com.skithub.resultdear.utils.CommonMethod

class MyApplication : Application() {

    lateinit var firebaseAnalytics: FirebaseAnalytics
    val MY_NOTIFICATION_CHANNEL_ID: String="MY_NOTIFICATION_CHANNEL_ID"
    private lateinit var notificationManager: NotificationManager
    val myApi by lazy {
        MyApi.invoke()
    }

    val secondServerApi by lazy {
        SecondServerApi.invoke()
    }

    public val iRetrofitApiCall by lazy {
        RetrofitClient.invoke()
    }

    override fun onCreate() {
        super.onCreate()


        initFirebaseServices()

        createNotificationChannel()




    }


    private fun initFirebaseServices() {
        FirebaseApp.initializeApp(this)
        firebaseAnalytics = Firebase.analytics
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        Firebase.messaging.isAutoInitEnabled = true
    }

    private fun createNotificationChannel() {
        notificationManager=getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            val notificationChannel: NotificationChannel= NotificationChannel(MY_NOTIFICATION_CHANNEL_ID,resources.getString(R.string.app_name),NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    override fun attachBaseContext(base: Context?) {
        if (base!=null) {
            super.attachBaseContext(CommonMethod.updateLanguage(base))
        } else {
            super.attachBaseContext(base)
        }
    }


}