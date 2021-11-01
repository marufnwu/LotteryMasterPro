package com.skithub.resultdear.ui.yesterday_result

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.FirebaseDatabase
import com.skithub.resultdear.R
import com.skithub.resultdear.adapter.ResultSlotAdapter
import com.skithub.resultdear.database.network.MyApi
import com.skithub.resultdear.databinding.ActivityYesterdayResultBinding
import com.skithub.resultdear.databinding.ConnectionCheckDialogBinding
import com.skithub.resultdear.model.response.LotterySlotResponse
import com.skithub.resultdear.ui.MyApplication
import com.skithub.resultdear.ui.lottery_result_info.LotteryResultInfoActivity
import com.skithub.resultdear.utils.CommonMethod
import com.skithub.resultdear.utils.Constants
import com.skithub.resultdear.utils.LoadingDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class YesterdayResultActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityYesterdayResultBinding
    private lateinit var connectivityManager: ConnectivityManager

    private var connectionAlertDialog: AlertDialog?=null
    private lateinit var connectionDialogBinding: ConnectionCheckDialogBinding

    private lateinit var myApi: MyApi
    private lateinit var loadingDialog : LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityYesterdayResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.yesterday_result)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        myApi = (application as MyApplication).myApi
        loadingDialog = LoadingDialog(this)

        initAll()



        if (CommonMethod.haveInternet(connectivityManager)) {
            updateUi()
        }else{
            noInternetDialog(getString(R.string.no_internet),getString(R.string.no_internet_message))
        }

        /*val Liveuserdb = FirebaseDatabase.getInstance().getReference("ActiveUsers")
        val prefs = RegisterActivity.PreferenceHelper.customPreference(this, CUSTOM_PREF_NAME)
        val map: HashMap<String, Any?> = HashMap()
        map["phone"] = prefs.userPhone
        map["activity"] = getString(R.string.yesterday_result)
        Liveuserdb.child(prefs.userToken!!).setValue(map)*/


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)


        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.option_share -> {
                CommonMethod.shareAppLink(this)
            }
            R.id.option_moreApps -> {
                CommonMethod.openConsoleLink(this,Constants.consoleId)
            }
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun noInternetDialog(til: String, msg: String) {
        connectionDialogBinding= ConnectionCheckDialogBinding.inflate(layoutInflater)
        connectionDialogBinding.connectionTitle.text = til
        connectionDialogBinding.connectionMessage.text = msg
        connectionDialogBinding.tryAgainBtn.setOnClickListener {
            if (CommonMethod.haveInternet(connectivityManager)) {
                updateUi()
                connectionAlertDialog?.dismiss()
            }
        }
        val builder=AlertDialog.Builder(this@YesterdayResultActivity)
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

    private fun initAll() {
        connectivityManager=getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        binding.morningButton.setOnClickListener(this)
        binding.eveningButton.setOnClickListener(this)
        binding.nightButton.setOnClickListener(this)

        binding.rvResultSlots.layoutManager = LinearLayoutManager(this)
        binding.rvResultSlots.setHasFixedSize(true)
    }

    private fun updateUi() {
        binding.yesterdayTimeTextView.text="Yesterday Result ${CommonMethod.increaseDecreaseDaysUsingValue(-1, Locale.getDefault())} ${CommonMethod.getDayNameUsingDate(CommonMethod.increaseDecreaseDaysUsingValue(-1,Locale.getDefault()),Locale.getDefault())}"
        getResultTimeSlot()

    }

    private fun getResultTimeSlot() {
        loadingDialog.show()
        myApi.getLotteryResultTime(true)
            .enqueue(
                object :
                    Callback<LotterySlotResponse> {
                    override fun onResponse(
                        p0: Call<LotterySlotResponse>,
                        res: Response<LotterySlotResponse>
                    ) {
                        loadingDialog.hide()
                        if(res.isSuccessful){
                            res.body()!!.let {
                                val lotterySlotResponse  = it
                                if(!lotterySlotResponse.error!! && lotterySlotResponse.resultSlots!!.size>0){
                                    val adapter = ResultSlotAdapter(lotterySlotResponse.resultSlots!!, this@YesterdayResultActivity)

                                    adapter.onButtonClickListener={resultSlot->
                                        var intent= Intent(this@YesterdayResultActivity, LotteryResultInfoActivity::class.java)
                                        intent.putExtra(Constants.resultDateKey, CommonMethod.increaseDecreaseDaysUsingValue(-1, Locale.ENGLISH))
                                        intent.putExtra(Constants.resultTimeKey, resultSlot.time)
                                        intent.putExtra(Constants.isVersusResultKey,false)
                                        intent.putExtra(Constants.resultSlotIdKey,resultSlot.id)
                                        startActivity(intent)
                                    }
                                    binding.rvResultSlots.adapter = adapter


                                }
                            }

                        }
                    }

                    override fun onFailure(p0: Call<LotterySlotResponse>, p1: Throwable) {
                        loadingDialog.hide()
                    }

                })
    }

    override fun onClick(v: View?) {
        v?.let {
            var pdfInfoIntent: Intent
            when (it.id) {
                R.id.morningButton -> {
                    pdfInfoIntent= Intent(this, LotteryResultInfoActivity::class.java)
                    pdfInfoIntent.putExtra(Constants.resultDateKey, CommonMethod.increaseDecreaseDaysUsingValue(-1, Locale.ENGLISH))
                    pdfInfoIntent.putExtra(Constants.resultTimeKey, Constants.noonTime)
                    pdfInfoIntent.putExtra(Constants.isVersusResultKey,false)
                    pdfInfoIntent.putExtra(Constants.resultSlotIdKey,Constants.noonSlotId)
                    startActivity(pdfInfoIntent)
                }

                R.id.eveningButton -> {
                    pdfInfoIntent= Intent(this, LotteryResultInfoActivity::class.java)
                    pdfInfoIntent.putExtra(Constants.resultDateKey, CommonMethod.increaseDecreaseDaysUsingValue(-1, Locale.ENGLISH))
                    pdfInfoIntent.putExtra(Constants.resultTimeKey, Constants.eveningTime)
                    pdfInfoIntent.putExtra(Constants.isVersusResultKey,false)
                    pdfInfoIntent.putExtra(Constants.resultSlotIdKey,Constants.eveningSlotId)

                    startActivity(pdfInfoIntent)
                }

                R.id.nightButton -> {
                    pdfInfoIntent= Intent(this, LotteryResultInfoActivity::class.java)
                    pdfInfoIntent.putExtra(Constants.resultDateKey, CommonMethod.increaseDecreaseDaysUsingValue(-1, Locale.ENGLISH))
                    pdfInfoIntent.putExtra(Constants.resultTimeKey, Constants.nightTime)
                    pdfInfoIntent.putExtra(Constants.isVersusResultKey,false)
                    pdfInfoIntent.putExtra(Constants.resultSlotIdKey,Constants.nightSlotId)

                    startActivity(pdfInfoIntent)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun attachBaseContext(newBase: Context?) {
        if (newBase!=null) {
            super.attachBaseContext(CommonMethod.updateLanguage(newBase))
        } else {
            super.attachBaseContext(newBase)
        }
    }



}