package org.wit.recyclingTracker.views.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import org.wit.recyclingTracker.databinding.ActivitySplashBinding
import org.wit.recyclingTracker.views.login.LoginView

class SplashView : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, LoginView::class.java))
            finish()
        }, 2000)
    }
}
