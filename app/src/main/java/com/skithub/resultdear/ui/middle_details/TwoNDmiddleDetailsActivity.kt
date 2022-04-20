package com.skithub.resultdear.ui.middle_details

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.skithub.resultdear.R
import com.skithub.resultdear.adapter.DuplicateLotteryNumberRecyclerAdapter
import com.skithub.resultdear.adapter.LotteryNumberRecyclerAdapter
import com.skithub.resultdear.databinding.ActivityCommonNumberDetailsBinding
import com.skithub.resultdear.databinding.ActivityMiddleDetailsBinding
import com.skithub.resultdear.databinding.ActivityTwoNdmiddleDetailsBinding
import com.skithub.resultdear.databinding.ConnectionCheckDialogBinding
import com.skithub.resultdear.model.LotteryNumberModel
import com.skithub.resultdear.ui.MyApplication
import com.skithub.resultdear.ui.common_number.CommonNumberViewModel
import com.skithub.resultdear.ui.common_number.CommonNumberViewModelFactory
import com.skithub.resultdear.ui.common_number_details.CommonNumberDetailsViewModel
import com.skithub.resultdear.ui.common_number_details.CommonNumberDetailsViewModelFactory
import com.skithub.resultdear.utils.CommonMethod
import com.skithub.resultdear.utils.Constants
import com.skithub.resultdear.utils.Coroutines
import com.skithub.resultdear.utils.MyExtensions.shortToast
import com.skithub.resultdear.utils.TryAgainAlert
import com.skithub.resultdear.utils.admob.MyInterstitialAd
import java.util.HashMap

class TwoNDmiddleDetailsActivity : AppCompatActivity() {
    lateinit var myInterstitialAd: MyInterstitialAd
    private lateinit var binding: ActivityTwoNdmiddleDetailsBinding
    private lateinit var viewModel: MiddleDetailsViewModel
    private var list: MutableList<LotteryNumberModel> = arrayListOf()
    private lateinit var adapter: LotteryNumberRecyclerAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var lotteryNumber: String="1234"
    private lateinit var connectivityManager: ConnectivityManager
    //val CUSTOM_PREF_NAME = "User_data_extra"
    private var connectionAlertDialog: AlertDialog?=null
    private lateinit var connectionDialogBinding: ConnectionCheckDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityTwoNdmiddleDetailsBinding.inflate(layoutInflater)
        val factory= MiddleDetailsViewModelFactory((application as MyApplication).myApi)
        viewModel= ViewModelProvider(this,factory).get(MiddleDetailsViewModel::class.java)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        myInterstitialAd = MyInterstitialAd(this)

        val bundle=intent.extras
        if (bundle!=null) {
            lotteryNumber=bundle.getString(Constants.lotteryNumberKey,"1234")
        }
        supportActionBar?.title = getString(R.string.middle_details_first)+" "+lotteryNumber+" "+getString(R.string.middle_details_last)
        initAll()



        if (CommonMethod.haveInternet(connectivityManager)) {
            setupRecyclerView()
            loadLotteryNumberList()
        }else{
            noInternetDialog(getString(R.string.no_internet),getString(R.string.no_internet_message))
        }

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
                setupRecyclerView()
                loadLotteryNumberList()
                connectionAlertDialog?.dismiss()
            }
        }
        val builder=AlertDialog.Builder(this@TwoNDmiddleDetailsActivity)
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
        binding.spinKit.visibility= View.GONE
    }

    private fun setupRecyclerView() {
        adapter= LotteryNumberRecyclerAdapter(this,list)
        layoutManager= LinearLayoutManager(this)
        binding.recyclerView.layoutManager=layoutManager
        binding.recyclerView.adapter=adapter
    }

    private fun loadLotteryNumberList() {

        val tryAgain = TryAgainAlert(this)
            .create()
            .setTryAgainButtonText(null, object: TryAgainAlert.OnTryAgainClick {
                override fun onClick() {
                    loadLotteryNumberList()
                }

            })

        Coroutines.main {
            try {
                binding.spinKit.visibility= View.VISIBLE
                val response=viewModel.get2ndMiddleListUsingLotteryNumber(lotteryNumber)
                if (response.isSuccessful && response.code()==200) {
                    binding.spinKit.visibility= View.GONE
                    list.clear()
                    adapter.notifyDataSetChanged()
                    /*val Liveuserdb = FirebaseDatabase.getInstance().getReference("ActiveUsers")
                    val prefs = RegisterActivity.PreferenceHelper.customPreference(this, CUSTOM_PREF_NAME)
                    val map: HashMap<String, Any?> = HashMap()
                    map["phone"] = prefs.userPhone
                    map["activity"] = getString(R.string.view_details)
                    Liveuserdb.child(prefs.userToken!!).setValue(map)*/
                    if (response.body()!=null) {
                        if (response.body()?.status.equals("success",true)) {
                            list.addAll(response.body()?.data!!)
                            adapter.notifyDataSetChanged()
                            /*val Liveuserdb = FirebaseDatabase.getInstance().getReference("ActiveUsers")
                            val prefs = RegisterActivity.PreferenceHelper.customPreference(this, CUSTOM_PREF_NAME)
                            val map: HashMap<String, Any?> = HashMap()
                            map["phone"] = prefs.userPhone
                            map["activity"] = getString(R.string.view_details)
                            Liveuserdb.child(prefs.userToken!!).setValue(map)*/
                        } else {
                            shortToast("message:- ${response.body()?.message}")
                            Log.d(Constants.TAG,"message:- ${response.body()?.message}")
                        }
                    }
                } else {
                    binding.spinKit.visibility= View.GONE
                    tryAgain.show()
                }
            } catch (e: Exception) {
                binding.spinKit.visibility= View.GONE
                tryAgain.show()
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

    override fun onBackPressed() {
        myInterstitialAd.onBackPress()
    }



}