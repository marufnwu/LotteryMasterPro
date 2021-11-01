package com.skithub.resultdear.ui.register_activity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.skithub.resultdear.BuildConfig
import com.skithub.resultdear.R
import com.skithub.resultdear.databinding.ActivityLoginConfirmBinding
import com.skithub.resultdear.databinding.ActivityRegisterBinding
import com.skithub.resultdear.databinding.ConnectionCheckDialogBinding
import com.skithub.resultdear.ui.MyApplication
import com.skithub.resultdear.ui.main.MainActivity
import com.skithub.resultdear.ui.main.MainViewModel
import com.skithub.resultdear.ui.main.MainViewModelFactory
import com.skithub.resultdear.ui.privacy_policy.PrivacyPolicyActivity
import com.skithub.resultdear.utils.CommonMethod
import com.skithub.resultdear.utils.Constants
import com.skithub.resultdear.utils.Coroutines
import com.skithub.resultdear.utils.SharedPreUtils
import java.util.*

class LoginConfirmActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginConfirmBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var connectionDialogBinding: ConnectionCheckDialogBinding
    private var connectionAlertDialog: AlertDialog?=null

    private var ISSUE: String? = "Didn't try"
    private var LANG: String? = "Unselected"
    private var PHONE: String? = null
    private var THUM_IMAGE: String? = null
    private var THUM_URL: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginConfirmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        connectivityManager=getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

        val factory= MainViewModelFactory((application as MyApplication).myApi)
        viewModel= ViewModelProvider(this,factory).get(MainViewModel::class.java)



        LANG = intent.getStringExtra("lang")
        THUM_IMAGE = intent.getStringExtra("t_image")
        THUM_URL = intent.getStringExtra("t_url")

        if (!THUM_IMAGE.isNullOrEmpty() && !THUM_URL.isNullOrEmpty()){
            Glide.with(this).load(THUM_IMAGE).placeholder(R.drawable.loading_placeholder).fitCenter().into(binding.ytthumbail)
            binding.ytthumbail.setOnClickListener {
                val webIntent: Intent= Intent(Intent.ACTION_VIEW,Uri.parse(THUM_URL))
                startActivity(Intent.createChooser(webIntent,"Choose one:"))
            }
            binding.content.startRippleAnimation()
            }

        binding.whatsAppBtn.setOnClickListener {
            try {
                val mobile = "918100316072"
                val msg = "${"Issue: "+ISSUE} ${"\nPhone: "+binding.phoneNumberText.text} ${"\nLanguage: "+ LANG} ${"\nVersion: "+ BuildConfig.VERSION_NAME} ${"\n.............\n"}${getString(R.string.common_login_issue)}"
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://api.whatsapp.com/send?phone=$mobile&text=$msg")
                    )
                )
            } catch (e: java.lang.Exception) {
                Toast.makeText(this@LoginConfirmActivity, "WhatsApp not Installed", Toast.LENGTH_SHORT).show()
            }
        }




        binding.tramsBtn.setOnClickListener {
            startActivity(Intent(this, PrivacyPolicyActivity::class.java))
        }


        binding.countryname.setOnCountryChangeListener {
            binding.countryCodeText.setText(binding.countryname.selectedCountryCodeWithPlus.toString())
        }

        binding.countryCodeText.setText(binding.countryname.selectedCountryCodeWithPlus.toString())




        binding.numberSubmitbtn.setOnClickListener {


            if (binding.countryname.selectedCountryNameCode.toString().equals("IN")) {
                if (binding.phoneNumberText.text.toString()
                        .trim().length < 10 || binding.phoneNumberText.text.toString()
                        .trim().length.equals(11)
                ) {
                    binding.phoneNumberText.error = getString(R.string.login_status_phone)
                } else {

                    if (binding.phoneNumberText.text.toString().trim().length.equals(12)) {
                        PHONE = binding.phoneNumberText.text.toString().trim()!!.substring(
                            2,
                            binding.phoneNumberText.text.toString().trim()!!.length - 0
                        )
                    } else if (binding.phoneNumberText.text.toString().trim().length.equals(13)) {
                        PHONE = binding.phoneNumberText.text.toString().trim()!!.substring(
                            3,
                            binding.phoneNumberText.text.toString().trim()!!.length - 0
                        )
                    } else {
                        PHONE = binding.phoneNumberText.text.toString().trim()
                    }

                    if (!PHONE.isNullOrEmpty()) {

                        registerPhone()
                    }
                }
            } else {
                if (binding.phoneNumberText.text.toString().trim().length < 8) {
                    binding.phoneNumberText.error = getString(R.string.login_status_phone)
                } else {
                    PHONE =  binding.phoneNumberText.text.toString().trim()
                    registerPhone()
                }
            }
        }


        }

    private fun registerPhone() {

        binding.numberSubmitbtn.isEnabled = false
        binding.loginprogressbar.visibility = View.VISIBLE

        try {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    val registrationDate: String=CommonMethod.increaseDecreaseDaysUsingValue(0, Locale.ENGLISH)
                    val activeStatus: String="false"
                    if (token!=null) {
                        Coroutines.main {
                            try {
                                ISSUE = "Loading"
                                val response=viewModel.uploadUserInfo(token,PHONE.toString(),registrationDate,activeStatus,binding.countryname.selectedCountryNameCode.toString())
                                if (response.isSuccessful && response.code()==200) {
                                    if (response.body()!=null) {
                                        if (response.body()?.status.equals("clear")){
                                            SharedPreUtils.setStringToStorage(applicationContext,Constants.fcmTokenKey,token)
                                            Constants.premiumActivationStatus="false"
                                            Constants.phone=binding.phoneNumberText.text.toString()
                                            Constants.registrationDate=registrationDate
                                            SharedPreUtils.setStringToStorage(applicationContext,Constants.userIdKey,response.body()?.message.toString())
                                            var intent= Intent(this, MainActivity::class.java)
                                            startActivity(intent)
                                            finish()
                                        }else{
                                            binding.phoneNumberText.error = getString(R.string.login_status_check)
                                            binding.whatsAppBtn.visibility = View.VISIBLE
                                            binding.numberSubmitbtn.visibility = View.GONE
                                            binding.loginprogressbar.visibility = View.GONE

                                        }
                                    }
                                }
                                FirebaseMessaging.getInstance().subscribeToTopic(Constants.userTypeFree)
                            }catch (e: Exception){
                                ISSUE = "Week Internet"
                                binding.numberSubmitbtn.isEnabled = true
                                binding.whatsAppBtn.visibility = View.VISIBLE
                                noInternetDialog(getString(R.string.weak_internet),getString(R.string.weak_internet_message))
                            }
                        }
                    }else{
                        ISSUE = "DeviceToken Not working"
                        binding.numberSubmitbtn.isEnabled = true
                        binding.whatsAppBtn.visibility = View.VISIBLE
                        noInternetDialog(getString(R.string.weak_internet),getString(R.string.weak_internet_message))
                    }
                }
            })
        } catch (e: Exception) {
            ISSUE = "Week Internet"
            binding.numberSubmitbtn.isEnabled = true
            binding.whatsAppBtn.visibility = View.VISIBLE
            noInternetDialog(getString(R.string.weak_internet),getString(R.string.weak_internet_message))

        }
    }





    private fun noInternetDialog(til: String, msg: String) {
        connectionDialogBinding= ConnectionCheckDialogBinding.inflate(layoutInflater)
        connectionDialogBinding.connectionTitle.text = til
        connectionDialogBinding.connectionMessage.text = msg
        connectionDialogBinding.tryAgainBtn.setOnClickListener {
            if (CommonMethod.haveInternet(connectivityManager)) {
                registerPhone()
                connectionAlertDialog?.dismiss()
            }
        }
        val builder= AlertDialog.Builder(this@LoginConfirmActivity)
            .setCancelable(true)
            .setView(connectionDialogBinding.root)
        connectionAlertDialog=builder.create()
        if (connectionAlertDialog?.window!=null) {
            connectionAlertDialog?.window!!.attributes.windowAnimations=R.style.DialogTheme
        }
        if (!isFinishing) {
            connectionAlertDialog?.show()
        }

    }

    override fun onBackPressed() {
        val gridIntent = Intent(applicationContext, RegisterActivity::class.java)
        gridIntent.putExtra("lang", LANG)
        startActivity(gridIntent)
        this.overridePendingTransition(R.anim.anim_slide_in_right,
            R.anim.anim_slide_out_right)
        finish()
    }

    override fun attachBaseContext(newBase: Context?) {
        if (newBase!=null) {
            super.attachBaseContext(CommonMethod.updateLanguage(newBase))
        } else {
            super.attachBaseContext(newBase)
        }
    }

}