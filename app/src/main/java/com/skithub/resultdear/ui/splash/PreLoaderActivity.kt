package com.skithub.resultdear.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class PreLoaderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(Intent(this, SplashActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        finish()
    }
}