package com.skithub.resultdear.ui.old_result

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.skithub.resultdear.R
import com.skithub.resultdear.adapter.OldResultRecyclerAdapter
import com.skithub.resultdear.databinding.ActivityOldResultBinding
import com.skithub.resultdear.databinding.ConnectionCheckDialogBinding
import com.skithub.resultdear.model.LotteryNumberModel
import com.skithub.resultdear.model.LotteryPdfModel
import com.skithub.resultdear.ui.MyApplication
import com.skithub.resultdear.utils.CommonMethod
import com.skithub.resultdear.utils.Constants
import com.skithub.resultdear.utils.Coroutines
import java.util.HashMap

class OldResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOldResultBinding
    private lateinit var viewModel: OldResultViewModel
    private lateinit var adapter: OldResultRecyclerAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var list: MutableList<LotteryNumberModel> = arrayListOf()
    private var page_number: Int=1
    private var item_count: Int=30
    private lateinit var connectivityManager: ConnectivityManager
//    private var past_visible_item: Int =0
//    private var visible_item_count: Int =0
//    private var total_item_count: Int =0
//    private var previous_total: Int =0
//    private var isLoading: Boolean=true
    //val CUSTOM_PREF_NAME = "User_data_extra"
    private var connectionAlertDialog: AlertDialog?=null
    private lateinit var connectionDialogBinding: ConnectionCheckDialogBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOldResultBinding.inflate(layoutInflater)
        val factory: OldResultViewModelFactory = OldResultViewModelFactory((application as MyApplication).myApi)
        viewModel= ViewModelProvider(this,factory).get(OldResultViewModel::class.java)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.old_result)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)




        initAll()



        if (CommonMethod.haveInternet(connectivityManager)) {
            setupRecyclerView()
            loadAllLotteryResult()
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
                CommonMethod.openConsoleLink(this, Constants.consoleId)
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
                loadAllLotteryResult()
                connectionAlertDialog?.dismiss()
            }
        }
        val builder=AlertDialog.Builder(this@OldResultActivity)
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
        binding.spinKit.visibility=View.GONE
    }

    private fun setupRecyclerView() {
        adapter= OldResultRecyclerAdapter(this,list)
        layoutManager= LinearLayoutManager(this)
        binding.recyclerView.layoutManager=layoutManager
        binding.recyclerView.adapter=adapter
//        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//
//                visible_item_count=layoutManager.childCount
//                total_item_count=layoutManager.itemCount
//                past_visible_item=layoutManager.findFirstVisibleItemPosition()
//                if (dy>0) {
//                    if (isLoading) {
//                        if (total_item_count > previous_total) {
//                            isLoading = false
//                            previous_total = total_item_count
//                        }
//                        if (!isLoading && (total_item_count - visible_item_count) <= (past_visible_item + item_count)) {
//                            page_number++
//                            loadAllLotteryResult()
//                            isLoading = true
//                        }
//                    }
//                }
//            }
//        })
    }

    private fun loadAllLotteryResult() {
        Coroutines.main {
            try {
                binding.spinKit.visibility=View.VISIBLE
                val response=viewModel.lotteryResultList(page_number.toString(),item_count.toString())
                binding.spinKit.visibility=View.GONE
                if (response.isSuccessful && response.code()==200) {
                    if (response.body()!=null) {
                        if (response.body()!!.status.equals("success",true)) {
                            list.clear()
                            list.addAll(response.body()!!.data!!)
                            adapter.notifyDataSetChanged()
                            /*val Liveuserdb = FirebaseDatabase.getInstance().getReference("ActiveUsers")
                            val prefs = RegisterActivity.PreferenceHelper.customPreference(this, CUSTOM_PREF_NAME)
                            val map: HashMap<String, Any?> = HashMap()
                            map["phone"] = prefs.userPhone
                            map["activity"] = getString(R.string.old_result)
                            Liveuserdb.child(prefs.userToken!!).setValue(map)*/
                        }else if (response.body()!!.status.equals("failed",true)) {
                            binding.buttonActiveNotice.visibility = View.VISIBLE
                        }
                    }
                }
            } catch (e: Exception) {
                binding.spinKit.visibility=View.GONE
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