package com.skithub.resultdear.ui.last_digit_first_prize

import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.skithub.resultdear.R
import com.skithub.resultdear.database.network.MyApi
import com.skithub.resultdear.databinding.ActivityLastDigitFirstPrizeBinding
import com.skithub.resultdear.databinding.ConnectionCheckDialogBinding
import com.skithub.resultdear.ui.MyApplication
import com.skithub.resultdear.utils.CommonMethod
import com.skithub.resultdear.utils.Coroutines

class LastDigitFirstPrizeActivity : AppCompatActivity() {
    private lateinit var myApi: MyApi
    private lateinit var binding: ActivityLastDigitFirstPrizeBinding
    private lateinit var connectionDialogBinding: ConnectionCheckDialogBinding
    private lateinit var connectivityManager: ConnectivityManager
    private var connectionAlertDialog: AlertDialog?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLastDigitFirstPrizeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myApi = (application as MyApplication).myApi
        supportActionBar?.title = getString(R.string.firstDigitFirstPrize)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        connectivityManager=getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

        if (CommonMethod.haveInternet(connectivityManager)) {

            loadBaner();


        }else{
            noInternetDialog(getString(R.string.no_internet),getString(R.string.no_internet_message))
        }
    }

    private fun loadBaner() {
        Coroutines.main {
            CommonMethod.getBanner("LastDigitFirstPrize", binding.ivPremBanner,myApi, applicationContext)
        }
    }

    private fun noInternetDialog(til: String, msg: String) {
        connectionDialogBinding= ConnectionCheckDialogBinding.inflate(layoutInflater)
        connectionDialogBinding.connectionTitle.text = til
        connectionDialogBinding.connectionMessage.text = msg
        connectionDialogBinding.tryAgainBtn.setOnClickListener {
            if (CommonMethod.haveInternet(connectivityManager)) {

               connectionAlertDialog.let {
                   it?.dismiss()
               }
            }
        }
        val builder= AlertDialog.Builder(this)
            .setCancelable(false)
            .setView(connectionDialogBinding.root)
        connectionAlertDialog=builder.create()
        if (connectionAlertDialog?.window!=null) {
            connectionAlertDialog?.window!!.attributes.windowAnimations=R.style.DialogTheme
        }
        if (!isFinishing) {
            connectionAlertDialog?.show()
        }

    }
}