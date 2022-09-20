package com.sagr.loadapp

import android.app.NotificationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import com.sagr.loadapp.utils.Constants
import com.sagr.loadapp.utils.Constants.SUCCESS
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        startAnimation()
        val notificationManager = ContextCompat.getSystemService(
            this,
            NotificationManager::class.java
        ) as NotificationManager
        notificationManager.cancelAll()
        val fileName = intent.getStringExtra(Constants.FILE_NAME_KEY)
        fileNameText.text = fileName

        val status = intent.getStringExtra(Constants.STATUS_KEY)
        statusText.text = status
        if (status == SUCCESS) {
            statusText.setTextColor(resources.getColor(R.color.green))
        } else {
            statusText.setTextColor(resources.getColor(R.color.red))

        }
        startAnimation()


        button.setOnClickListener {
            finish()
        }

    }

    private fun startAnimation() {
        motion_layout.transitionToStart()

    }

}
