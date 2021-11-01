package com.skithub.resultdear.ui.get_help

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.skithub.resultdear.R
import com.skithub.resultdear.databinding.ActivityToGetHelpBinding
import com.skithub.resultdear.utils.CommonMethod
import com.skithub.resultdear.utils.Constants
import java.util.HashMap

class To_Get_HelpActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityToGetHelpBinding
    //val CUSTOM_PREF_NAME = "User_data_extra"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityToGetHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.contact_us)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        initAll()
        /*val Liveuserdb = FirebaseDatabase.getInstance().getReference("ActiveUsers")
        val prefs = RegisterActivity.PreferenceHelper.customPreference(this, CUSTOM_PREF_NAME)
        val map: HashMap<String, Any?> = HashMap()
        map["phone"] = prefs.userPhone
        map["activity"] = getString(R.string.contact_us)
        Liveuserdb.child(prefs.userToken!!).setValue(map)*/


    }


    private fun initAll() {
        binding.mailAddressTextView.text="Email:- ${resources.getString(R.string.support_email)}"
        binding.sendMailButton.setOnClickListener(this)
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
                CommonMethod.openConsoleLink(this, Constants.consoleId)
            }
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun sendMail() {
        val mailingIntent=Intent(Intent.ACTION_SEND)
        mailingIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(resources.getString(R.string.support_email)))
        mailingIntent.putExtra(Intent.EXTRA_SUBJECT,resources.getString(R.string.support_email_subject))
        mailingIntent.putExtra(Intent.EXTRA_TEXT,resources.getString(R.string.support_email_message))
        mailingIntent.type="message/rfc822"
        startActivity(Intent.createChooser(mailingIntent,"Choose an Email client :"))
    }

    override fun onClick(v: View?) {
        v?.let {
            when (it.id) {
                R.id.sendMailButton -> sendMail()
            }
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        if (newBase!=null) {
            super.attachBaseContext(CommonMethod.updateLanguage(newBase))
        } else {
            super.attachBaseContext(newBase)
        }
    }


}