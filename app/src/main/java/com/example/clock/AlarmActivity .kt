package com.example.clock

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class AlarmActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the activity to be shown over the lock screen
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
        // Set the layout for your lock screen activity
        setShowWhenLocked(true)
        setTurnScreenOn(true);
        setContentView(R.layout.activity_lock_screen)

        val mediaPlayer = MediaPlayer.create(this, R.raw.alarm)
        mediaPlayer.start()

        // Xử lý sự kiện khi người dùng click vào nút tắt
        val buttonDismiss = findViewById<Button>(R.id.button_dismiss)
        buttonDismiss.setOnClickListener {
            // Tắt báo thức và kết thúc Lock Screen Activity
            stopAlarm()
            finish()
        }
    }

    private fun stopAlarm() {
        // Code để tắt âm thanh báo thức tại đây
    }
}
