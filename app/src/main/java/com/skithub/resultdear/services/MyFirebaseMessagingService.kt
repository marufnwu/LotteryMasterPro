package com.skithub.resultdear.services
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.skithub.resultdear.R
import com.skithub.resultdear.notification.NotificationData
import com.skithub.resultdear.notification.NotificationUtil
import com.skithub.resultdear.ui.MyApplication
import com.skithub.resultdear.ui.today_result.TodayResultActivity
import com.skithub.resultdear.utils.Constants

class MyFirebaseMessagingService: FirebaseMessagingService() {

    private var notificationManager: NotificationManager?=null

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        try {
            if (notificationManager==null) {
                notificationManager=getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            }
            if (p0.notification?.title.isNullOrEmpty()) {
                showDataNotification(p0.data)
            } else if (p0.notification!=null) {
                showPayloadNotification(p0.notification!!)
            }
        } catch (e: Exception) {}
    }


    private fun showPayloadNotification(notification: RemoteMessage.Notification) {
        val notificationIntent: Intent
        if (notification.title.equals(getString(R.string.notify_title_day)) || notification.title.equals(getString(R.string.notify_title_evening)) || notification.title.equals(getString(R.string.notify_title_morning))) {
            notificationIntent = Intent(this,TodayResultActivity::class.java)
        } else {
            notificationIntent= Intent(Intent.ACTION_VIEW, Uri.parse(notification.link.toString()))
        }
        val notificationPendingIntent: PendingIntent=PendingIntent.getActivity(this,Constants.notificationRequestCode,notificationIntent,PendingIntent.FLAG_ONE_SHOT)
        val defaultNotificationUri=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder: NotificationCompat.Builder=NotificationCompat
            .Builder(this,(applicationContext as MyApplication).MY_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(notification.title)
            .setContentText(notification.body)
            .setContentIntent(notificationPendingIntent)
            .setAutoCancel(true)
            .setSound(defaultNotificationUri)
        notificationManager?.notify(Constants.notificationRequestCode,notificationBuilder.build())
    }

    private fun showDataNotification(notificationData: MutableMap<String,String>) {
        val notificationIntent: Intent
        if (notificationData[Constants.notificationTargetUrlKey].isNullOrEmpty()) {
            notificationIntent = Intent(this,TodayResultActivity::class.java)
        } else {
            notificationIntent= Intent(Intent.ACTION_VIEW, Uri.parse(notificationData[Constants.notificationTargetUrlKey]))
        }

        if (notificationData.get("tittle") != null && notificationData.get("description") != null && notificationData.get("imgUrl") != null && notificationData.get(
                "notiClearAble"
            ) != null && notificationData.get("action") != null && notificationData.get("notiType") != null
        ) {

           val nData : NotificationData = NotificationData()

            nData.tittle = notificationData["tittle"]
            nData.description = notificationData["description"]
            nData.imgUrl = notificationData["imgUrl"]
            nData.notiClearAble = notificationData["notiClearAble"]!!.toInt()
            nData.action = notificationData["action"]!!.toInt()
            nData.notiType = notificationData["notiType"]!!.toInt()
            nData.actionUrl = notificationData["actionUrl"]
            nData.actionActivity = notificationData["actionActivity"]

            val notificationUtil = NotificationUtil(applicationContext)
            notificationUtil.displayNotification(nData)

            return
        }
        val notificationPendingIntent: PendingIntent=PendingIntent.getActivity(this,Constants.notificationRequestCode,notificationIntent,PendingIntent.FLAG_ONE_SHOT)
        val defaultNotificationUri=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder: NotificationCompat.Builder=NotificationCompat
            .Builder(this,(applicationContext as MyApplication).MY_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(notificationData[Constants.notificationTitleKey])
            .setContentText(notificationData[Constants.notificationBodyKey])
            .setContentIntent(notificationPendingIntent)
            .setAutoCancel(notificationData[Constants.notificationCancelableKey].equals("true",true))
            .setSound(defaultNotificationUri)
        notificationManager?.notify(Constants.notificationRequestCode,notificationBuilder.build())
    }




}