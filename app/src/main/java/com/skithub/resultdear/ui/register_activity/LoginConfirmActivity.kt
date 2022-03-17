package com.skithub.resultdear.ui.register_activity

import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.skithub.resultdear.BuildConfig
import com.skithub.resultdear.R
import com.skithub.resultdear.databinding.ActivityLoginConfirmBinding
import com.skithub.resultdear.databinding.ConnectionCheckDialogBinding
import com.skithub.resultdear.databinding.DialogMultipleLoginContactBinding
import com.skithub.resultdear.databinding.ServerIssueDialogBinding
import com.skithub.resultdear.ui.MyApplication
import com.skithub.resultdear.ui.main.MainActivity
import com.skithub.resultdear.ui.main.MainViewModel
import com.skithub.resultdear.ui.main.MainViewModelFactory
import com.skithub.resultdear.ui.privacy_policy.PrivacyPolicyActivity
import com.skithub.resultdear.utils.CommonMethod
import com.skithub.resultdear.utils.Constants
import com.skithub.resultdear.utils.Coroutines
import com.skithub.resultdear.utils.SharedPreUtils
import java.io.FileInputStream
import java.util.*


class LoginConfirmActivity : AppCompatActivity() {
    private var isActivityPause : Boolean = false
    private var isPause: Boolean = false
    private lateinit var binding: ActivityLoginConfirmBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var connectionDialogBinding: ConnectionCheckDialogBinding
    private var connectionAlertDialog: AlertDialog?=null

     var media :MediaPlayer? = null

    private var ISSUE: String? = "Didn't try"
    private var LANG: String? = "Unselected"
    private var PHONE: String? = null
    private var THUM_IMAGE: String? = null
    private var THUM_URL: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginConfirmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        media = MediaPlayer.create(this, R.raw.serverissue)

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
                                            //binding.whatsAppBtn.visibility = View.VISIBLE
                                            binding.numberSubmitbtn.visibility = View.VISIBLE
                                            binding.numberSubmitbtn.isEnabled = true

                                            binding.loginprogressbar.visibility = View.GONE

                                            //show dialog for multiple login issue
                                            getContactDetails()
                                        }
                                    }
                                }
                                FirebaseMessaging.getInstance().subscribeToTopic(Constants.userTypeFree)
                            }catch (e: Exception){
                                ISSUE = "Week Internet"
                                binding.numberSubmitbtn.isEnabled = true
                                //binding.whatsAppBtn.visibility = View.VISIBLE
                                serverIssueDialog("Server Down",getString(R.string.server_issue_msg))

                            }
                        }
                    }else{
                        ISSUE = "DeviceToken Not working"
                        binding.numberSubmitbtn.isEnabled = true
                        //binding.whatsAppBtn.visibility = View.VISIBLE
                        serverIssueDialog("Server Down",getString(R.string.server_issue_msg))

                    }
                }
            })
        } catch (e: Exception) {
            ISSUE = "Week Internet"
            binding.numberSubmitbtn.isEnabled = true
            //binding.whatsAppBtn.visibility = View.VISIBLE
            serverIssueDialog("Server Down",getString(R.string.server_issue_msg))


        }
    }

    private fun getContactDetails() {
        Coroutines.main {
            val res =  viewModel.getPaidForContact("7", "null")
            if(res.isSuccessful && res.body()!=null){
                val paidForContactModel = res.body()!!

                val multipleLoginDialogBinding = DialogMultipleLoginContactBinding.inflate(layoutInflater)

                val builder= AlertDialog.Builder(this@LoginConfirmActivity)
                    .setCancelable(true)
                    .setView(multipleLoginDialogBinding.root)
                val multipleLoginDialog = builder.create()

                if (multipleLoginDialog.window!=null) {
                    multipleLoginDialog.window!!.attributes.windowAnimations=R.style.DialogTheme
                }

                multipleLoginDialogBinding.pnOne.text = paidForContactModel.phone_one!!
                multipleLoginDialogBinding.pnTwo.text = paidForContactModel.phone_two!!
                multipleLoginDialogBinding.pnThree.text = paidForContactModel.phone_three!!

                Glide.with(this).load(paidForContactModel.video_thumbail)
                    .placeholder(R.drawable.loading_placeholder)
                    .fitCenter()
                    .into(multipleLoginDialogBinding.ytthumbail)

                multipleLoginDialogBinding.ytthumbail.setOnClickListener {
                    val webIntent: Intent= Intent(Intent.ACTION_VIEW,Uri.parse(paidForContactModel.video_link))
                    startActivity(Intent.createChooser(webIntent,"Choose one:"))
                }

                multipleLoginDialogBinding.PhoneOne.setOnClickListener {
                    dial(paidForContactModel.phone_one)
                }
                multipleLoginDialogBinding.PhoneTwo.setOnClickListener {
                    dial(paidForContactModel.phone_two)
                }
                multipleLoginDialogBinding.PhoneThree.setOnClickListener {
                    dial(paidForContactModel.phone_three)
                }

                if(!isFinishing){
                    multipleLoginDialog.show()
                }
            }
        }
    }

    private fun dial(num:String){
        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data = Uri.parse("tel:" + num)
        startActivity(dialIntent)
    }


    private fun serverIssueDialog(til: String, msg: String) {



        var serverIssueDialogBinding  = ServerIssueDialogBinding.inflate(layoutInflater)
        serverIssueDialogBinding.connectionTitle.text = til
        serverIssueDialogBinding.connectionMessage.text = msg

        if(!isActivityPause){
            if(media!=null){
                media!!.start()
                serverIssueDialogBinding.tryAgainBtn.visibility = View.INVISIBLE
            }
        }



        serverIssueDialogBinding.imgThumb.setOnClickListener {
            val url = "https://www.youtube.com/watch?v=xf8x1V1JscQ"
            val webIntent: Intent= Intent(Intent.ACTION_VIEW,Uri.parse(url))
            startActivity(Intent.createChooser(webIntent,"Choose one:"))
        }
        val builder= AlertDialog.Builder(this@LoginConfirmActivity)
                .setCancelable(false)
                .setView(serverIssueDialogBinding.root)

        var serverIssueAlertDialog=builder.create()
        if (serverIssueAlertDialog.window!=null) {
            serverIssueAlertDialog.window!!.attributes.windowAnimations=R.style.DialogTheme
        }

        serverIssueDialogBinding.tryAgainBtn.setOnClickListener {
            if (CommonMethod.haveInternet(connectivityManager)) {
                registerPhone()
                serverIssueAlertDialog.dismiss()
            }
        }

        if (!isFinishing && !isDestroyed) {
            serverIssueAlertDialog.show()
        }

        serverIssueAlertDialog.setOnDismissListener {
            if(media!=null){
                if(media!!.isPlaying){
                    media!!.stop()
                }
            }
        }

        if(media!=null){
            media!!.setOnErrorListener { p0, p1, p2 ->
                serverIssueDialogBinding.tryAgainBtn.visibility =View.VISIBLE
                true
            }
        }

        if(media!=null){
            media!!.setOnCompletionListener {
                serverIssueDialogBinding.tryAgainBtn.visibility =View.VISIBLE
            }
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

    override fun onResume() {
        super.onResume()
        isActivityPause = false
        if(isPause && media!=null){
            media!!.start()
        }
    }

    override fun onPause() {
        super.onPause()
        isActivityPause = true
        if(media!=null){
            media!!.isPlaying.let {
                if(it){
                    isPause = true
                    media!!.pause()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(media!=null){
            media?.release()
            media = null
        }
    }

}