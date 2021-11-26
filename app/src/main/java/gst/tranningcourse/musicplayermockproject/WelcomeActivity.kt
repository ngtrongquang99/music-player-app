package gst.tranningcourse.musicplayermockproject

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.appcompat.app.AppCompatActivity
import gst.tranningcourse.musicplayermockproject.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity(), Runnable {
    private lateinit var binding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Thread(this).start()

        val animation = RotateAnimation(
            0f,
            360f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        ).apply {
            duration = 1000
            repeatMode = Animation.RESTART
            repeatCount = Animation.INFINITE
        }

        binding.imgLoading.startAnimation(animation)
    }

    override fun run() {
        Thread.sleep(2000)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}