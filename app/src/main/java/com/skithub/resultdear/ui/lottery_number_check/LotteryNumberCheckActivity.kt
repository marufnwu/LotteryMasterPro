package com.skithub.resultdear.ui.lottery_number_check

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.skithub.resultdear.R
import com.skithub.resultdear.adapter.LotteryNumberRecyclerAdapter
import com.skithub.resultdear.database.network.api.SecondServerApi
import com.skithub.resultdear.databinding.ActivityLotteryNumberCheckBinding
import com.skithub.resultdear.databinding.ConnectionCheckDialogBinding
import com.skithub.resultdear.databinding.NotFoundBinding
import com.skithub.resultdear.model.LotteryNumberModel
import com.skithub.resultdear.ui.MyApplication
import com.skithub.resultdear.utils.CommonMethod
import com.skithub.resultdear.utils.Constants
import com.skithub.resultdear.utils.Coroutines
import com.skithub.resultdear.utils.LoadingDialog
import com.skithub.resultdear.utils.MyExtensions.shortToast
import com.skithub.resultdear.utils.admob.MyInterstitialAd

class LotteryNumberCheckActivity : AppCompatActivity(), View.OnClickListener{

    private var isClickedBackButton: Boolean = false
    private lateinit var secodServerApi: SecondServerApi
    private lateinit var binding: ActivityLotteryNumberCheckBinding
    private lateinit var viewModel: LotteryNumberCheckViewModel
    private var list: MutableList<LotteryNumberModel> = arrayListOf()
    private lateinit var adapter: LotteryNumberRecyclerAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var inputMethodManager: InputMethodManager
    private lateinit var connectivityManager: ConnectivityManager
    private var connectionAlertDialog: AlertDialog?=null
    private var notfoundAlertDialog: AlertDialog?=null
    private lateinit var connectionDialogBinding: ConnectionCheckDialogBinding
    private lateinit var notfoundDialogBinding: NotFoundBinding
    private var page_number: Int=1
    private var item_count: Int=30

    lateinit var loadingDialog : LoadingDialog
    lateinit var myInterstitialAd: MyInterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLotteryNumberCheckBinding.inflate(layoutInflater)
        val factory: LotteryNumberCheckViewModelFactory = LotteryNumberCheckViewModelFactory((application as MyApplication).myApi)
        viewModel= ViewModelProvider(this,factory).get(LotteryNumberCheckViewModel::class.java)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.search_number)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        loadingDialog = LoadingDialog(this)
        myInterstitialAd = MyInterstitialAd(this)



        secodServerApi  = (application as MyApplication).secondServerApi

        initAll()




        if (CommonMethod.haveInternet(connectivityManager)) {
            setUpRecyclerView()
        }else{
            noInternetDialog(getString(R.string.no_internet),getString(R.string.no_internet_message))
        }


    }

    private fun noInternetDialog(til: String, msg: String) {
        connectionDialogBinding= ConnectionCheckDialogBinding.inflate(layoutInflater)
        connectionDialogBinding.connectionTitle.text = til
        connectionDialogBinding.connectionMessage.text = msg
        connectionDialogBinding.tryAgainBtn.setOnClickListener {
            if (CommonMethod.haveInternet(connectivityManager)) {
                setUpRecyclerView()
                connectionAlertDialog?.dismiss()
            }
        }
        val builder=AlertDialog.Builder(this@LotteryNumberCheckActivity)
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

    private fun notFoundDialog() {
        notfoundDialogBinding= NotFoundBinding.inflate(layoutInflater)
        notfoundDialogBinding.connectionMessage.text = "${getString(R.string.not_found_description)}${"\n(According to our database information)"}"
        notfoundDialogBinding.tryAgainBtn.setOnClickListener {
            if (CommonMethod.haveInternet(connectivityManager)) {
                notfoundAlertDialog?.dismiss()
            }
        }
        val builder=AlertDialog.Builder(this@LotteryNumberCheckActivity)
            .setCancelable(false)
            .setView(notfoundDialogBinding.root)
        notfoundAlertDialog=builder.create()
        if (notfoundAlertDialog?.window!=null) {
            notfoundAlertDialog?.window!!.attributes.windowAnimations=R.style.DialogTheme
        }
        if (!isFinishing) {
            notfoundAlertDialog?.show()
        }

    }

    private fun initAll() {
        connectivityManager=getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        inputMethodManager=getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        binding.spinKit.visibility= View.GONE
        binding.recyclerView.visibility= View.GONE
        binding.lotteryNumberCheckButton.setOnClickListener(this)
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

    private fun setUpRecyclerView() {
        layoutManager= LinearLayoutManager(this)
        adapter= LotteryNumberRecyclerAdapter(this,list)
        binding.recyclerView.layoutManager=layoutManager
        binding.recyclerView.adapter=adapter
    }

    private fun checkNumber() {
        Coroutines.main {
            try {
                hideKeyBoard()
                val lotteryNumber: String=binding.lotteryNumberEditText.text.toString()
                binding.spinKit.visibility= View.VISIBLE
                list.clear()
                adapter.notifyDataSetChanged()
                val response=viewModel.findLotteryInfoUsingLotteryNumber(page_number.toString(),item_count.toString(),lotteryNumber)
                //val response=secodServerApi.findSimilarLotteryNumberList(page_number.toString(),item_count.toString(),lotteryNumber)
                if (response.isSuccessful && response.code()==200) {
                    binding.spinKit.visibility= View.GONE
                    if (response.body()?.status.equals("success",true)) {
                        list.addAll(response.body()?.data!!)
                        adapter.notifyDataSetChanged()
                        if (list.size>0) {
                            binding.recyclerView.visibility=View.VISIBLE
                        } else {
                            binding.recyclerView.visibility=View.GONE
                            shortToast("Sorry, no data found")
                        }
                    } else {
                        //shortToast(response.body()?.message!!)
                        notFoundDialog()
                    }
                } else {
                    binding.spinKit.visibility= View.GONE
                    shortToast("failed for:- ${response.errorBody()?.toString()}")
                }
            } catch (e: Exception) {

                binding.spinKit.visibility= View.GONE
            }
        }
    }

    private fun hideKeyBoard() {
        inputMethodManager.hideSoftInputFromWindow(binding.lotteryNumberEditText.windowToken,0)
    }

    override fun onClick(v: View?) {
        v?.let {
            when (it.id) {
                R.id.lotteryNumberCheckButton -> {
                    if (binding.lotteryNumberEditText.length() >= 2){
                        checkNumber()
                        //showInterstitial()
                    }else{
                       Toast.makeText(this,"Give at least 2 digits",Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onPause() {
        super.onPause()
//        binding.particleView.pause()
    }

    override fun attachBaseContext(newBase: Context?) {
        if (newBase!=null) {
            super.attachBaseContext(CommonMethod.updateLanguage(newBase))
        } else {
            super.attachBaseContext(newBase)
        }
    }




    @SuppressLint("LongLogTag")
    private fun finishActivity(){
        Log.d("LotterySerialCheckActivity", "finishCalled")
        loadingDialog.hide()
        finish()
        //goToMainactivity()
    }


    override fun onBackPressed() {
        myInterstitialAd.onBackPress()
    }

}