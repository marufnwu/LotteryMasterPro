package com.skithub.resultdear.utils

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.akexorcist.screenshotdetection.ScreenshotDetectionDelegate
import com.skithub.resultdear.ui.importent_tips.ImportentTipsActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

open class ScreenShotDetectWithBase : AppCompatActivity(), ScreenshotDetectionDelegate.ScreenshotDetectionListener {
    val screenshotDetectionDelegate : ScreenshotDetectionDelegate = ScreenshotDetectionDelegate(this, this)


    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun onStart() {
        super.onStart()
        screenshotDetectionDelegate.startScreenshotDetection()
    }

    override fun onStop() {
        super.onStop()
        screenshotDetectionDelegate.stopScreenshotDetection()
    }



    override fun onScreenCaptured(path: String) {
        //Toast.makeText(this, path, Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, ImportentTipsActivity::class.java))
    }

    override fun onScreenCapturedWithDeniedPermission() {
        Toast.makeText(this, "denied", Toast.LENGTH_SHORT).show()
    }
}