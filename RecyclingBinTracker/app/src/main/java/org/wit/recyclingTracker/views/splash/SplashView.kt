package org.wit.recyclingTracker.views.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import org.wit.recyclingTracker.views.login.LoginView

class SplashView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)


        startActivity(Intent(this, LoginView::class.java))
        finish()
    }
}
