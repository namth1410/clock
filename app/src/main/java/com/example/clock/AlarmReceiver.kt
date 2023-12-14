package com.example.clock

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // Xử lý sự kiện báo thức ở đây, ví dụ: phát âm thanh báo thức, hiển thị thông báo, vv.
//        Toast.makeText(context, "Alarm triggered!", Toast.LENGTH_SHORT).show()
//        println("baothukeu")

        val notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Tạo kênh thông báo cho Android 8.0 (API level 26) trở lên
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "alarm_channel"
            val channelName = "Alarm Channel"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(channelId, channelName, importance)
            notificationManager.createNotificationChannel(channel)
        }

        // Xây dựng thông báo
        val notificationBuilder = NotificationCompat.Builder(context, "alarm_channel")
            .setSmallIcon(R.drawable.baseline_access_alarm_24)
            .setContentTitle("Báo thức")
            .setContentText("Báo thức đã được kích hoạt")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val soundUri = Uri.parse("android.resource://" + context.packageName + "/" + R.raw.alarm)
//        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

        notificationBuilder.setSound(soundUri)


        val notificationId = 1
        notificationManager.notify(notificationId, notificationBuilder.build())

        val alarmIntent = Intent(context, AlarmActivity::class.java)
        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_SINGLE_TOP)
        context.startActivity(alarmIntent)

        val mediaPlayer = MediaPlayer.create(context, R.raw.alarm)
        mediaPlayer.start()
    }
}
