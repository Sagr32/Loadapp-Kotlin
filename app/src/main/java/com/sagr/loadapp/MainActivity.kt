package com.sagr.loadapp

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.sagr.loadapp.utils.Constants.FAILED
import com.sagr.loadapp.utils.Constants.GLIDE_URL
import com.sagr.loadapp.utils.Constants.RETROFIT_URL
import com.sagr.loadapp.utils.Constants.SUCCESS
import com.sagr.loadapp.utils.Constants.UDACITY_URL
import com.sagr.loadapp.utils.NotificationHelper
import com.sagr.loadapp.utils.RequestStatus
import com.sagr.loadapp.utils.sendNotification
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private var selectedRadioButton: String? = null
    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private var url: String = GLIDE_URL
    private lateinit var requestStatus: RequestStatus

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        NotificationHelper.createChannel(this)
        requestStatus = RequestStatus.Failed
        custom_button.setOnClickListener {
            if (selectedRadioButton != null) {
                custom_button.buttonState = ButtonState.Clicked
                custom_button.buttonState = ButtonState.Loading
                download()
            } else {
                Toast.makeText(this, getString(R.string.select_file), Toast.LENGTH_SHORT)
                    .show()
            }


        }
        radio_group.setOnCheckedChangeListener { _, radioId ->
//            selectedRadioButton =
            when (radioId) {
                R.id.rb_glide -> {
                    url = GLIDE_URL
                    selectedRadioButton = "Glide"
                }
                R.id.rb_retrofit -> {
                    url = RETROFIT_URL
                    selectedRadioButton = "Retrofit"
                }
                else -> {
                    url = UDACITY_URL
                    selectedRadioButton = "Udacity"
                }
            }

        }


    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            val notificationManager = ContextCompat.getSystemService(
                this@MainActivity,
                NotificationManager::class.java
            ) as NotificationManager
            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val downloadStatus = getDownloadStatus()
            Log.e("BroadCast", "status $downloadStatus")
            when (downloadStatus) {
                DownloadManager.STATUS_SUCCESSFUL -> {
                    requestStatus = RequestStatus.Success
                    custom_button.buttonState = ButtonState.Completed
                    notificationManager.sendNotification(
                        selectedRadioButton!!,
                        this@MainActivity,
                        SUCCESS
                    )
                    Toast.makeText(this@MainActivity, "Download SUCCESS", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    requestStatus = RequestStatus.Failed
                    custom_button.buttonState = ButtonState.Completed
                    notificationManager.sendNotification(
                        selectedRadioButton!!,
                        this@MainActivity,
                        FAILED
                    )
                    Toast.makeText(this@MainActivity, "Download FAILED", Toast.LENGTH_SHORT).show()

                }

            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.

    }

    @SuppressLint("Range")
    private fun getDownloadStatus(): Int {
        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val cursor = downloadManager.query(DownloadManager.Query().setFilterById(downloadID))
        var status = 0
        if (cursor.moveToFirst()) {
            status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
        }
        return status
    }

}
