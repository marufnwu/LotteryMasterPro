package com.skithub.resultdear.ui.splash

import android.content.Intent
import android.content.IntentSender
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.skithub.resultdear.BuildConfig
import com.skithub.resultdear.R
import com.skithub.resultdear.databinding.ActivitySplashBinding
import com.skithub.resultdear.ui.main.MainActivity
import okhttp3.ResponseBody
import java.lang.Exception


class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private var appUpdate : AppUpdateManager? = null
    private val REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        appUpdate = AppUpdateManagerFactory.create(this)
        //startActivity(Intent(this, MainActivity::class.java));
        //checkUpdate()
//        if(BuildConfig.DEBUG){
//           startActivity(Intent(this, MainActivity::class.java));
//        }else{
//            checkUpdate()
//        }

        binding.logo.alpha = 1f
        binding.logo.animate().translationYBy(-100f)
            .scaleXBy(1f)
            .scaleYBy(1f)
            .rotation(360F)
            //?.alpha(0f)
            ?.setDuration(1000)
            ?.withEndAction(Runnable {
                binding.appName.animate()
                    .alpha(1f)
                    .setDuration(1000)
                    .setInterpolator(DecelerateInterpolator())
                    .withEndAction {
                        if(BuildConfig.DEBUG){
                           startActivity(Intent(this, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                            finish()
                        }else{
                            checkUpdate()
                        }
                    }
                    //.rotationBy(10f)
                    .start()

            })
            ?.setInterpolator(DecelerateInterpolator())
            ?.start()


        //animation()
    }

    fun animation(){
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.interpolator = DecelerateInterpolator() //add this
        fadeIn.duration = 1000

        val fadeOut = AlphaAnimation(1f, 0f)
        fadeOut.interpolator = AccelerateInterpolator() //and this
        fadeOut.startOffset = 1000
        fadeOut.duration = 1000

        val animation = AnimationSet(false) //change to false
        //animation.addAnimation(fadeIn)
        animation.addAnimation(fadeOut)
        binding.logo.animation = animation
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

        }!!.addOnFailureListener {
            Log.d("UpdateChecker", it.message!!)

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun inProgressUpdate(){
       try{
           appUpdate?.appUpdateInfo?.addOnSuccessListener{ updateInfo->

               if (updateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS){
                   appUpdate?.startUpdateFlowForResult(updateInfo,AppUpdateType.IMMEDIATE,this,REQUEST_CODE)
               }

           }
       }catch (e:Exception){

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