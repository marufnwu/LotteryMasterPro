package com.skithub.resultdear.ui.register_activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.skithub.resultdear.BuildConfig
import com.skithub.resultdear.R
import com.skithub.resultdear.databinding.ActivityRegisterBinding
import com.skithub.resultdear.databinding.ConnectionCheckDialogBinding
import com.skithub.resultdear.ui.MyApplication
import com.skithub.resultdear.ui.main.MainActivity
import com.skithub.resultdear.ui.main.MainViewModel
import com.skithub.resultdear.ui.main.MainViewModelFactory
import com.skithub.resultdear.ui.middle_details.MiddleDetailsViewModel
import com.skithub.resultdear.ui.middle_details.MiddleDetailsViewModelFactory
import com.skithub.resultdear.ui.middle_number.MiddleNumberViewModel
import com.skithub.resultdear.ui.middle_number.MiddleNumberViewModelFactory
import com.skithub.resultdear.ui.privacy_policy.PrivacyPolicyActivity
import com.skithub.resultdear.utils.CommonMethod
import com.skithub.resultdear.utils.Constants
import com.skithub.resultdear.utils.Coroutines
import com.skithub.resultdear.utils.MyExtensions.shortToast
import com.skithub.resultdear.utils.SharedPreUtils
import java.util.*
import android.text.Editable

import android.text.TextWatcher
import com.skithub.resultdear.ui.common_number.CommonNumberActivity


class RegisterActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityRegisterBinding
    private var LANG: String? = "Unselected"
    private var THUM_IMAGE: String? = null
    private var THUM_URL: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory= MainViewModelFactory((application as MyApplication).myApi)
        viewModel= ViewModelProvider(this,factory).get(MainViewModel::class.java)

        getPremiumStatus()

        LANG = intent.getStringExtra("lang")

        if (LANG.equals("English")){
            binding.rglangen.isChecked = true
        }else if (LANG.equals("বাংলা")){
            binding.rglangbn.isChecked = true
        }else if (LANG.equals("हिंदी")){
            binding.rglanghi.isChecked = true
        }else if (LANG.equals("বাংলা")){
            binding.rglangbn2.isChecked = true
        }

        binding.rglangen.setOnClickListener {
            LANG = "English"
            changeLocale("en_US")

        }

        binding.rglangbn.setOnClickListener {
            LANG = "বাংলা"
            changeLocale("bn")

        }

        binding.rglangbn2.setOnClickListener {
            LANG = "বাংলা"
            changeLocale("bn")

        }

        binding.rglanghi.setOnClickListener {
            LANG = "हिंदी"
            changeLocale("hi")

        }

    }


    private fun changeLocale(lanCode: String) {
        Coroutines.io {
            SharedPreUtils.setStringToStorage(applicationContext, Constants.appLanguageKey,lanCode)
            SharedPreUtils.setBooleanToStorage(applicationContext,
                Constants.appLanguageStatusKey,true)
        }
        val gridIntent = Intent(applicationContext, LoginConfirmActivity::class.java)
        gridIntent.putExtra("lang", LANG)
        gridIntent.putExtra("t_image", THUM_IMAGE)
        gridIntent.putExtra("t_url", THUM_URL)
        startActivity(gridIntent)
        this.overridePendingTransition(R.anim.anim_slide_in_left,
            R.anim.anim_slide_out_left)
        finish()
    }


    override fun attachBaseContext(newBase: Context?) {
        if (newBase!=null) {
            super.attachBaseContext(CommonMethod.updateLanguage(newBase))
        } else {
            super.attachBaseContext(newBase)
        }
    }






    private fun getPremiumStatus() {
        Coroutines.main {
            try {
                val response=viewModel.getPaidForContact("0","0")
                if (response.isSuccessful && response.code()==200) {
                    if (response.body()!=null) {
                        binding.content.startRippleAnimation()
                        THUM_IMAGE = response.body()?.video_thumbail
                        THUM_URL = response.body()?.video_link
                        Glide.with(this@RegisterActivity).load(response.body()?.video_thumbail).placeholder(R.drawable.loading_placeholder).fitCenter().into(binding.ytthumbail)
                        binding.ytthumbail.setOnClickListener {
                            val webIntent: Intent= Intent(Intent.ACTION_VIEW,Uri.parse(response.body()?.video_link))
                            startActivity(Intent.createChooser(webIntent,"Choose one:"))
                        }

                    }
                } else {
                   //binding.spinKit.visibility= View.GONE
                }
            } catch (e: Exception) {
                //binding.spinKit.visibility= View.GONE
            }
        }

    }

}