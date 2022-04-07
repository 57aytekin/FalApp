package com.falApp.sadekahvefal.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioAttributes
import android.os.Build
import android.provider.Settings
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.falApp.sadekahvefal.R
import com.falApp.sadekahvefal.ui.activity.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val channelId = "notification_channel"
const val channelName = "com.falApp.sadekahvefal.notification"
class FcmMessagingService : FirebaseMessagingService() {
    private val MY_CHANNEL_ID = "19465"
    private val notifyId = 1
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        //showNotification(p0.data["message"])
        generateNotification(resources.getString(R.string.app_name), remoteMessage.data["message"]!!)
    }

    fun getRemoteView(title: String, message: String): RemoteViews {
        val remoteView = RemoteViews("com.falApp.sadekahvefal", R.layout.notification)
        remoteView.setTextViewText(R.id.title, title)
        remoteView.setTextViewText(R.id.message, message)
        remoteView.setImageViewResource(R.id.icon, R.drawable.ic_avatar)

        return remoteView
    }
    fun generateNotification(title: String, message: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, 0 , intent , PendingIntent.FLAG_IMMUTABLE)

        var builder : NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_baseline_star_24)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 1000, 10000, 1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .setCustomBigContentView(getRemoteView(title, message))

        builder = builder.setContent(getRemoteView(title, message))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(0, builder.build())
    }

    private fun showNotification(s: String?) {
        val notificationManager: NotificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val title = resources.getString(R.string.app_name)
        val name = getString(R.string.app_name)
        val soundUri = Settings.System.DEFAULT_NOTIFICATION_URI

        val mLong: LongArray = longArrayOf(1000,1000)
        val intent = Intent(this, MainActivity::class.java)
        val importance = NotificationManager.IMPORTANCE_HIGH


        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or  Intent.FLAG_ACTIVITY_NEW_TASK
        val pendingIntent = PendingIntent.getActivity(this, 0 , intent, PendingIntent.FLAG_IMMUTABLE )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(
                MY_CHANNEL_ID, name, importance
            )
            mChannel.lightColor = Color.GRAY
            mChannel.enableLights(true)
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            mChannel.setSound(soundUri, audioAttributes)
            notificationManager.createNotificationChannel(mChannel)
        }


        val builder = NotificationCompat.Builder(this,MY_CHANNEL_ID)
        builder.let {
            it.setAutoCancel(true)
            it.setContentTitle(title)
            it.setContentText(s)


            it.setSmallIcon(R.drawable.ic_avatar)
            it.setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_baseline_star_24))
            it.setContentIntent(pendingIntent)
            it.setVibrate(mLong)
            it.setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            it.setChannelId(MY_CHANNEL_ID)
        }
        notificationManager.notify(notifyId,builder.build())
    }
}