package com.samprama.mobileapp.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.samprama.mobileapp.MainActivity
import com.samprama.mobileapp.R

class SplashActivity : AppCompatActivity() {
    var imageView: ImageView? = null
    var top: Animation? = null
    var bottom:Animation? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.rajput_logo_color)
        }

        imageView = findViewById(R.id.imageView)

        supportActionBar!!.hide()
        top = AnimationUtils.loadAnimation(this, R.anim.top)
        bottom = AnimationUtils.loadAnimation(this, R.anim.bottom)
        imageView?.setAnimation(top)
       // imageView.animation

        // Hide the action bar
        //        textView1.setAnimation(bottom);
//        textView2.setAnimation(bottom);

        // Hide the action bar
        val actionBar = actionBar
        actionBar?.hide()
        Handler().postDelayed({
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2500)
    }
}