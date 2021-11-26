package gst.tranningcourse.musicplayermockproject.service


import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import gst.tranningcourse.musicplayermockproject.utils.Constains

class MyApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        createChannelNotification()
    }
    private fun createChannelNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel: NotificationChannel = NotificationChannel(
                Constains.CHANNEL_ID,
                "Channel Foreground Service",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.setSound(null,null)
            val manager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}