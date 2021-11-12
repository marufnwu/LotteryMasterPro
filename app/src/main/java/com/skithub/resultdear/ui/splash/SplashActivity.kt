package com.skithub.resultdear.ui.splash

import android.content.Intent
import android.content.IntentSender
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.skithub.resultdear.R
import com.skithub.resultdear.databinding.ActivitySplashBinding
import com.skithub.resultdear.ui.main.MainActivity
import okhttp3.ResponseBody




class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private var appUpdate : AppUpdateManager? = null
    private val REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        appUpdate = AppUpdateManagerFactory.create(this)
        startActivity(Intent(this, MainActivity::class.java));
        //checkUpdate()
    }



    override fun onResume() {
        super.onResume()
        inProgressUpdate()
    }

    private fun checkUpdate(){
        Log.d("UpdateChecker", "Inside check update")
        appUpdate?.appUpdateInfo?.addOnSuccessListener{ updateInfo->

            if (updateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && updateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)){
                Log.d("UpdateChecker", "Update Available version"+updateInfo.availableVersionCode())

                try{
                    appUpdate?.startUpdateFlowForResult(updateInfo,
                        AppUpdateType.IMMEDIATE,this,REQUEST_CODE)
                }catch (e : IntentSender.SendIntentException){

                }
            }else{
                Log.d("UpdateChecker", "App up to date")

                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }

        }
    }

    private fun inProgressUpdate(){
        appUpdate?.appUpdateInfo?.addOnSuccessListener{ updateInfo->

            if (updateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS){
                appUpdate?.startUpdateFlowForResult(updateInfo,AppUpdateType.IMMEDIATE,this,REQUEST_CODE)
            }

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                Log.e("MY_APP", "Update flow failed! Result code: $resultCode")
                // If the update is cancelled or fails,
                // you can request to start the update again.
                checkUpdate()
            }
        }
    }
}