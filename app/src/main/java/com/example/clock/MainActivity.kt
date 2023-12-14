package com.example.clock

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.clock.databinding.ActivityMainBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.time.LocalDateTime
import java.util.*

private lateinit var binding: ActivityMainBinding
var list_baothuc = ArrayList<BaoThuc>()

class MainActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null
//    private lateinit var alarmManager: AlarmManager
//    private lateinit var alarmIntent: PendingIntent


    private lateinit var alarmIntent: PendingIntent

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val alarmManager =
//            this.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
//        val pendingIntent =
//            PendingIntent.getService(
//                this, 0, intent,
//                PendingIntent.FLAG_NO_CREATE
//            )
//        if (pendingIntent != null && alarmManager != null) {
//            alarmManager.cancel(pendingIntent)
//        }
//        alarmIntent = Intent(this, AlarmReceiver::class.java).let { intent ->
//            PendingIntent.getBroadcast(this, 0, intent, 0)
//        }


        val navController = findNavController(R.id.fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.baoThucFrag, R.id.bamGioFrag)
        )
        binding.bottomNavigation.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}