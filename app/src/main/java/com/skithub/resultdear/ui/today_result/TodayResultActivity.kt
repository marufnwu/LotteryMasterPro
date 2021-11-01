package com.skithub.resultdear.ui.today_result

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.FirebaseDatabase
import com.skithub.resultdear.R
import com.skithub.resultdear.adapter.ResultSlotAdapter
import com.skithub.resultdear.database.network.MyApi
import com.skithub.resultdear.databinding.ActivityTodayResultBinding
import com.skithub.resultdear.model.response.LotterySlotResponse
import com.skithub.resultdear.ui.MyApplication
import com.skithub.resultdear.ui.lottery_result_info.LotteryResultInfoActivity
import com.skithub.resultdear.ui.main.MainActivity
import com.skithub.resultdear.utils.CommonMethod
import com.skithub.resultdear.utils.Constants
import com.skithub.resultdear.utils.LoadingDialog
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class TodayResultActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var myApi: MyApi
    private lateinit var binding: ActivityTodayResultBinding
    //val CUSTOM_PREF_NAME = "User_data_extra"
    private lateinit var loadingDialog : LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodayResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.today_result)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        myApi = (application as MyApplication).myApi
        loadingDialog = LoadingDialog(this)




        initAll()
        getResultTimeSlot()


        /*val Liveuserdb = FirebaseDatabase.getInstance().getReference("ActiveUsers")
        val prefs = RegisterActivity.PreferenceHelper.customPreference(this, CUSTOM_PREF_NAME)
        val map: HashMap<String, Any?> = HashMap()
        map["phone"] = prefs.userPhone
        map["activity"] = getString(R.string.today_result)
        Liveuserdb.child(prefs.userToken!!).setValue(map)*/




    }

    private fun getResultTimeSlot() {
        loadingDialog.show()
        myApi.getLotteryResultTime(true)
            .enqueue(
                object :
                    Callback<LotterySlotResponse>{
                    override fun onResponse(
                        p0: Call<LotterySlotResponse>,
                        res: Response<LotterySlotResponse>
                    ) {
                        loadingDialog.hide()
                        if(res.isSuccessful){
                            res.body()!!.let {
                                val lotterySlotResponse  = it
                                if(!lotterySlotResponse.error!! && lotterySlotResponse.resultSlots!!.size>0){
                                    val adapter = ResultSlotAdapter(lotterySlotResponse.resultSlots!!, this@TodayResultActivity)

                                    adapter.onButtonClickListener={resultSlot->
                                        var intent= Intent(this@TodayResultActivity, LotteryResultInfoActivity::class.java)
                                        intent.putExtra(
                                            Constants.resultDateKey,
                                            CommonMethod.increaseDecreaseDaysUsingValue(0, Locale.ENGLISH))
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


    private fun initAll() {
        binding.morningButton.setOnClickListener(this)
        binding.eveningButton.setOnClickListener(this)
        binding.nightButton.setOnClickListener(this)
        binding.bumperNightButton.setOnClickListener(this)

        binding.rvResultSlots.layoutManager = LinearLayoutManager(this)
        binding.rvResultSlots.setHasFixedSize(true)
    }

    override fun onClick(v: View?) {
        v?.let {
            var pdfInfoIntent: Intent
            when (it.id) {
                R.id.morningButton -> {
                    pdfInfoIntent= Intent(this, LotteryResultInfoActivity::class.java)
                    pdfInfoIntent.putExtra(Constants.resultDateKey,CommonMethod.increaseDecreaseDaysUsingValue(0, Locale.ENGLISH))
                    pdfInfoIntent.putExtra(Constants.resultTimeKey,Constants.noonTime)
                    pdfInfoIntent.putExtra(Constants.isVersusResultKey,false)
                    startActivity(pdfInfoIntent)
                }

                R.id.eveningButton -> {
                    pdfInfoIntent= Intent(this, LotteryResultInfoActivity::class.java)
                    pdfInfoIntent.putExtra(Constants.resultDateKey,CommonMethod.increaseDecreaseDaysUsingValue(0, Locale.ENGLISH))
                    pdfInfoIntent.putExtra(Constants.resultTimeKey,Constants.eveningTime)
                    pdfInfoIntent.putExtra(Constants.isVersusResultKey,false)
                    startActivity(pdfInfoIntent)
                }

                R.id.nightButton -> {
                    pdfInfoIntent= Intent(this, LotteryResultInfoActivity::class.java)
                    pdfInfoIntent.putExtra(Constants.resultDateKey,CommonMethod.increaseDecreaseDaysUsingValue(0, Locale.ENGLISH))
                    pdfInfoIntent.putExtra(Constants.resultTimeKey,Constants.nightTime)
                    pdfInfoIntent.putExtra(Constants.isVersusResultKey,false)
                    startActivity(pdfInfoIntent)
                }

                R.id.bumperNightButton -> {
                    pdfInfoIntent= Intent(this, LotteryResultInfoActivity::class.java)
                    pdfInfoIntent.putExtra(Constants.resultDateKey,CommonMethod.increaseDecreaseDaysUsingValue(0, Locale.ENGLISH))
                    pdfInfoIntent.putExtra(Constants.resultTimeKey,Constants.bumperTime)
                    pdfInfoIntent.putExtra(Constants.isVersusResultKey,false)
                    startActivity(pdfInfoIntent)
                }
            }
        }
    }

    override fun onBackPressed() {
        if (isTaskRoot) {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        } else {
            super.onBackPressed()
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


    override fun onDestroy() {
        super.onDestroy()
        loadingDialog.hide()
    }

}