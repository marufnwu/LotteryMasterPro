package com.skithub.resultdear.ui.special_or_bumper

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.FirebaseDatabase
import com.skithub.resultdear.R
import com.skithub.resultdear.databinding.ActivitySplOrBumperBinding
import com.skithub.resultdear.model.LotteryNumberModel
import com.skithub.resultdear.model.LotteryPdfModel
import com.skithub.resultdear.ui.MyApplication
import com.skithub.resultdear.utils.CommonMethod
import com.skithub.resultdear.utils.Constants
import com.skithub.resultdear.utils.Coroutines
import com.skithub.resultdear.utils.MyExtensions.longToast
import com.skithub.resultdear.utils.MyExtensions.shortToast
import java.util.HashMap

class SplOrBumperActivity : AppCompatActivity() {


    private lateinit var binding: ActivitySplOrBumperBinding
    private lateinit var viewModel: SpecialOrBumperViewModel
//    private lateinit var adapter: OldResultRecyclerAdapter
//    private lateinit var layoutManager: LinearLayoutManager
//    private var list: MutableList<LotteryNumberModel> = arrayListOf()
//    private var page_number: Int=1
//    private var item_count: Int=30
//    private var past_visible_item: Int =0
//    private var visible_item_count: Int =0
//    private var total_item_count: Int =0
//    private var previous_total: Int =0
//    private var isLoading: Boolean=true
    //val CUSTOM_PREF_NAME = "User_data_extra"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplOrBumperBinding.inflate(layoutInflater)
        val factory: SpecialOrBumperViewModelFactory = SpecialOrBumperViewModelFactory((application as MyApplication).myApi)
        viewModel= ViewModelProvider(this,factory).get(SpecialOrBumperViewModel::class.java)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.special_or_bumper)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)





        initAll()

        /*val Liveuserdb = FirebaseDatabase.getInstance().getReference("ActiveUsers")
        val prefs = RegisterActivity.PreferenceHelper.customPreference(this, CUSTOM_PREF_NAME)
        val map: HashMap<String, Any?> = HashMap()
        map["phone"] = prefs.userPhone
        map["activity"] = getString(R.string.special_or_bumper)
        Liveuserdb.child(prefs.userToken!!).setValue(map)*/

//        setupRecyclerView()
//
//        loadAllLotteryResult()

        longToast(resources.getString(R.string.not_implemented_message))


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


    private fun initAll() {
        binding.spinKit.visibility= View.GONE
    }

//    private fun setupRecyclerView() {
//        adapter= OldResultRecyclerAdapter(this,list)
//        layoutManager= LinearLayoutManager(this)
//        binding.recyclerView.layoutManager=layoutManager
//        binding.recyclerView.adapter=adapter
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
//    }
//
//    private fun loadAllLotteryResult() {
//        Coroutines.main {
//            try {
//                binding.spinKit.visibility= View.VISIBLE
//                val response=viewModel.bumperLotteryResultList(page_number.toString(),item_count.toString())
//                if (response.isSuccessful && response.code()==200) {
//                    binding.spinKit.visibility= View.GONE
//                    if (response.body()!=null) {
//                        if (response.body()?.status.equals("success",true)) {
//                            list.addAll(response.body()?.data!!)
//                            adapter.notifyDataSetChanged()
//                        }
//                    }
//                } else {
//                    binding.spinKit.visibility= View.GONE
//                }
//            } catch (e: Exception) {
//                binding.spinKit.visibility= View.GONE
//            }
//        }
//    }

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