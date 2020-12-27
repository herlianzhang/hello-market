package com.dpr.hello_market.ui.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dpr.hello_market.MainActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mIntent = Intent(this,  MainActivity::class.java)
        startActivity(mIntent)
        finish()
    }
}