package com.skithub.resultdear.notification

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.gson.Gson
import com.skithub.resultdear.BuildConfig
import com.skithub.resultdear.R
import com.skithub.resultdear.ui.main.MainActivity
import com.skithub.resultdear.ui.middle_number.MiddleNumberActivity
import com.skithub.resultdear.ui.splash.SplashActivity
import com.skithub.resultdear.utils.Constants.ACTIVITY
import com.skithub.resultdear.utils.Constants.ACTIVITY_CREATED_BY_NOTI
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class NotificationUtil(val context: Context) {

    private val CHANNEL_ID = "Lmp_Notification_Chanel"
    private val CHANNEL_NAME = "Lmp_Notification"
    private var NOTIFICATION_ID = 20001
    private var notification: Notification? = null



    fun displayNotification(notificationData: NotificationData) {
        val tittle = notificationData.tittle
        val message = notificationData.description
        val iconUrl = notificationData.imgUrl
        var iconBitmap: Bitmap? = null
        val resultPendingIntent: PendingIntent
        Log.d("intentSelect", Gson().toJson(notificationData))
        if (notificationData.action == 1) {
            //open url
            Log.d("intentSelect", "Url")
            val notificationIntent = Intent(Intent.ACTION_VIEW)
            notificationIntent.data = Uri.parse(notificationData.actionUrl)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                resultPendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    notificationIntent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
            }else{
                resultPendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    notificationIntent,
                     PendingIntent.FLAG_UPDATE_CURRENT
                )
            }
        } else if (notificationData.action == 2) {
            //open activity
            Log.d("intentSelect", "Activity")
            val activity = notificationData.actionActivity
            var resultIntent: Intent? = null
            if (activity != null) {
                try {
                    val c = Class.forName(BuildConfig.APPLICATION_ID+activity)
                    Log.d("IntentPending", BuildConfig.APPLICATION_ID+activity)
                    resultIntent = Intent(context, c)
                }catch (ignored: ClassNotFoundException){
                    resultIntent = Intent(context, MainActivity::class.java)
                }

            }


            //resultIntent.putExtra("id", notificationData.id);
            // Create the TaskStackBuilder and add the intent, which inflates the back stack
            val stackBuilder = TaskStackBuilder.create(context)
            stackBuilder.addNextIntentWithParentStack(resultIntent)
            // Get the PendingIntent containing the entire back stack

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
            }else{
                resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            }
        } else {
            Log.d("intentSelect", "Splash Activity")
            val resultIntent = Intent(context, SplashActivity::class.java)
            // Create the TaskStackBuilder and add the intent, which inflates the back stack
            val stackBuilder = TaskStackBuilder.create(context)
            stackBuilder.addNextIntentWithParentStack(resultIntent)
            // Get the PendingIntent containing the entire back stack
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
            }else{
                resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            }

        }
        if (notificationData.notiType == 2) {
            iconBitmap = getBitmapFromURL(iconUrl)
        }
        val icon: Int = R.mipmap.ic_launcher
        val mBuilder = NotificationCompat.Builder(
            context!!, CHANNEL_ID
        )
        if (notificationData.notiClearAble == 0) {
            mBuilder.setContentIntent(resultPendingIntent).setOngoing(true)
        } else {
            mBuilder.setContentIntent(resultPendingIntent).setOngoing(false)
        }
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        mBuilder.setSmallIcon(getNotificationIcon(mBuilder))
        notification = if (iconBitmap == null) {
            Log.d("NotificationLog", "iconBitmap null")

            //show without image
            mBuilder
                .setContentTitle(tittle)
                .setContentText(message)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(message)
                )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build()
            /* if (notificationData.notiClearAble==0){
                   notification.flags |= Notification.FLAG_AUTO_CANCEL;
               }*/
        } else {
            Log.d("NotificationLog", "iconBitmap not null")
            mBuilder
                .setTicker(tittle)
                .setWhen(0)
                .setContentTitle(tittle)
                .setContentText(message)
                .setStyle(
                    NotificationCompat.BigPictureStyle()
                        .setBigContentTitle(tittle)
                        .bigPicture(iconBitmap)
                )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setLargeIcon(iconBitmap)
                .setAutoCancel(true)
                .build()
            /*  if (notificationData.notiClearAble==0){
                    notification.flags |= Notification.FLAG_AUTO_CANCEL;
                }*/
        }
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = CHANNEL_NAME
            val description = "Ps Guide Notification"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            channel.enableLights(true)
            channel.enableVibration(true)
            channel.setSound(soundUri, audioAttributes)
            notificationManager.createNotificationChannel(channel)
        } else {
            mBuilder.setSound(soundUri)
        }
        val m = (Date().time / 1000L % Int.MAX_VALUE).toInt()
        NOTIFICATION_ID = m

        //notificationManager.notify(NOTIFICATION_ID, notification)


        try{
            notificationManager.notify(NOTIFICATION_ID, notification)
        }catch (e:Exception){
            Log.d("NotificationError", e.message!!)
        }
    }

    private fun getBitmapFromURL(strURL: String?): Bitmap? {
        return try {
            val url = URL(strURL)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
    }

    private fun getNotificationIcon(notificationBuilder: NotificationCompat.Builder): Int {
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            //notificationBuilder.setColor(context.getResources().getColor(R.color.OrangeRed));
            R.drawable.ic_not_icon
        } else R.drawable.bell
    }
}