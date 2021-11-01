package com.skithub.resultdear.ui.yes_vs_pre

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.skithub.resultdear.R
import com.skithub.resultdear.databinding.ActivityYesVsPreBinding
import com.skithub.resultdear.ui.lottery_result_info.LotteryResultInfoActivity
import com.skithub.resultdear.utils.CommonMethod
import com.skithub.resultdear.utils.Constants
import java.util.*

class YesVsPreActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityYesVsPreBinding
    //val CUSTOM_PREF_NAME = "User_data_extra"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityYesVsPreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.yesterday_and_pre)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)




        initAll()

        /*val Liveuserdb = FirebaseDatabase.getInstance().getReference("ActiveUsers")
        val prefs = RegisterActivity.PreferenceHelper.customPreference(this, CUSTOM_PREF_NAME)
        val map: HashMap<String, Any?> = HashMap()
        map["phone"] = prefs.userPhone
        map["activity"] = getString(R.string.yesterday_and_pre)
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


    private fun initAll() {
        binding.morningButton.setOnClickListener(this)
        binding.eveningButton.setOnClickListener(this)
        binding.nightButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        v?.let {
            var pdfInfoIntent: Intent
            when (it.id) {
                R.id.morningButton -> {
                    pdfInfoIntent= Intent(this, LotteryResultInfoActivity::class.java)
                    pdfInfoIntent.putExtra(Constants.resultDateKey, CommonMethod.increaseDecreaseDaysUsingValue(-1, Locale.ENGLISH))
                    pdfInfoIntent.putExtra(Constants.resultTimeKey, Constants.noonTime)
                    pdfInfoIntent.putExtra(Constants.isVersusResultKey,true)
                    startActivity(pdfInfoIntent)
                }

                R.id.eveningButton -> {
                    pdfInfoIntent= Intent(this, LotteryResultInfoActivity::class.java)
                    pdfInfoIntent.putExtra(Constants.resultDateKey, CommonMethod.increaseDecreaseDaysUsingValue(-1, Locale.ENGLISH))
                    pdfInfoIntent.putExtra(Constants.resultTimeKey, Constants.eveningTime)
                    pdfInfoIntent.putExtra(Constants.isVersusResultKey,true)
                    startActivity(pdfInfoIntent)
                }

                R.id.nightButton -> {
                    pdfInfoIntent= Intent(this, LotteryResultInfoActivity::class.java)
                    pdfInfoIntent.putExtra(Constants.resultDateKey, CommonMethod.increaseDecreaseDaysUsingValue(-1, Locale.ENGLISH))
                    pdfInfoIntent.putExtra(Constants.resultTimeKey, Constants.nightTime)
                    pdfInfoIntent.putExtra(Constants.isVersusResultKey,true)
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