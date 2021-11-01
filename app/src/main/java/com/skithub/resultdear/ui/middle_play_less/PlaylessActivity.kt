package com.skithub.resultdear.ui.middle_play_less

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.JsonElement
import com.skithub.resultdear.R
import com.skithub.resultdear.adapter.DuplicateLotteryNumberRecyclerAdapter
import com.skithub.resultdear.database.network.ApiInterface
import com.skithub.resultdear.database.network.RetrofitClient
import com.skithub.resultdear.databinding.ActivityCommonNumberBinding
import com.skithub.resultdear.databinding.ActivityMiddleNumberBinding
import com.skithub.resultdear.databinding.ActivityPlaylessBinding
import com.skithub.resultdear.databinding.ConnectionCheckDialogBinding
import com.skithub.resultdear.model.LotteryNumberModel
import com.skithub.resultdear.ui.MyApplication
import com.skithub.resultdear.ui.common_number.CommonNumberViewModel
import com.skithub.resultdear.ui.common_number.CommonNumberViewModelFactory
import com.skithub.resultdear.utils.CommonMethod
import com.skithub.resultdear.utils.Constants
import com.skithub.resultdear.utils.Coroutines
import com.skithub.resultdear.utils.MyExtensions.shortToast
import com.skithub.resultdear.utils.SharedPreUtils
import com.skyfishjy.library.RippleBackground
import org.json.JSONArray
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class PlaylessActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlaylessBinding
    private lateinit var viewModel: MiddleLessViewModel
    private var list: MutableList<LotteryNumberModel> = arrayListOf()
    private lateinit var adapter: DuplicateLotteryNumberRecyclerAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var page_number: Int=1
    private var item_count: Int=30
    private lateinit var connectivityManager: ConnectivityManager
    //val CUSTOM_PREF_NAME = "User_data_extra"
    private var connectionAlertDialog: AlertDialog?=null
    private lateinit var connectionDialogBinding: ConnectionCheckDialogBinding
    private var license_check: String? = null

    private var apiInterface: ApiInterface? = null
    val CUSTOM_PREF_NAME = "User_data_extra"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPlaylessBinding.inflate(layoutInflater)
        val factory= MiddleLessViewModelFactory((application as MyApplication).myApi)
        viewModel= ViewModelProvider(this,factory).get(MiddleLessViewModel::class.java)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.these_middle_play_less)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        apiInterface = RetrofitClient.getApiClient().create(ApiInterface::class.java)
       license_check = intent.getStringExtra("license_position").toString()

        initAll()

        if (CommonMethod.haveInternet(connectivityManager)) {

                binding.recyclerView.visibility = View.VISIBLE
                binding.standerdLayout.visibility = View.GONE
                setupRecyclerView()
                loadDuplicateLotteryNumber()

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
                if (!license_check.equals("1")){
                    getPremiumStatus()
                    binding.recyclerView.visibility = View.GONE
                    binding.standerdLayout.visibility = View.VISIBLE
                }else{
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.standerdLayout.visibility = View.GONE
                    setupRecyclerView()
                    loadDuplicateLotteryNumber()
                }
                connectionAlertDialog?.dismiss()
            }
        }
        val builder=AlertDialog.Builder(this@PlaylessActivity)
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
        adapter= DuplicateLotteryNumberRecyclerAdapter(this,list)
        layoutManager= LinearLayoutManager(this)
        binding.recyclerView.layoutManager=layoutManager
        binding.recyclerView.adapter=adapter
        binding.recyclerView.setHasFixedSize(false)
        list.sortBy { it.lotteryNumber }
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
//                            loadDuplicateLotteryNumber()
//                            isLoading = true
//                        }
//                    }
//                }
//            }
//        })
    }

    private fun getPremiumStatus() {
        Coroutines.main {
            try {
                val response=viewModel.getPaidForContact("3",
                    SharedPreUtils.getStringFromStorageWithoutSuspend(this,Constants.userIdKey,Constants.defaultUserId).toString())
                if (response.isSuccessful && response.code()==200) {
                    if (response.body()!=null) {
                        binding.spinKit.visibility= View.GONE
                        binding.standerdLayout.visibility = View.VISIBLE

                        binding.pnOne.text = response.body()?.phone_one
                        binding.pnTwo.text = response.body()?.phone_two
                        binding.pnThree.text = response.body()?.phone_three

                        binding.content.startRippleAnimation()
                        Glide.with(this@PlaylessActivity).load(response.body()?.video_thumbail).placeholder(R.drawable.loading_placeholder).fitCenter().into(binding.ytthumbail)
                        binding.ytthumbail.setOnClickListener {
                            val webIntent: Intent= Intent(Intent.ACTION_VIEW,Uri.parse(response.body()?.video_link))
                            startActivity(Intent.createChooser(webIntent,"Choose one:"))
                        }
                        binding.PhoneOne.setOnClickListener {
                            val dialIntent = Intent(Intent.ACTION_DIAL)
                            dialIntent.data = Uri.parse("tel:" + response.body()?.phone_one)
                            startActivity(dialIntent)
                        }
                        binding.PhoneTwo.setOnClickListener {
                            val dialIntent = Intent(Intent.ACTION_DIAL)
                            dialIntent.data = Uri.parse("tel:" + response.body()?.phone_two)
                            startActivity(dialIntent)
                        }
                        binding.PhoneThree.setOnClickListener {
                            val dialIntent = Intent(Intent.ACTION_DIAL)
                            dialIntent.data = Uri.parse("tel:" + response.body()?.phone_three)
                            startActivity(dialIntent)
                        }

                        binding.whatsAppBtn.setOnClickListener {
                            try {
                                val mobile = response.body()?.whats_app
                                val msg = ""
                                startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://api.whatsapp.com/send?phone=$mobile&text=$msg")
                                    )
                                )
                            } catch (e: java.lang.Exception) {
                                Toast.makeText(this@PlaylessActivity, "WhatsApp not Installed", Toast.LENGTH_SHORT).show()
                            }

                        }



                        if (response.body()?.banner_image!!.length > 6){
                            binding.adUpArrowBtn.setImageResource(R.drawable.ic_arrow_down_icon)
                            Glide.with(this@PlaylessActivity).load(response.body()?.banner_image).placeholder(R.drawable.loading_placeholder).into(binding.imageBanner)
                            binding.imageBanner.setOnClickListener {
                                val webIntent: Intent= Intent(Intent.ACTION_VIEW,Uri.parse(response.body()?.target_link))
                                startActivity(Intent.createChooser(webIntent,"Choose one:"))
                            }
                            /*val hide: Animation =
                                AnimationUtils.loadAnimation(this@MainActivity, R.anim.bottom_top)
                            binding.adLayout.startAnimation(hide)*/
                            binding.adLayout.visibility = View.VISIBLE
                            binding.adUpArrowBtn.visibility = View.VISIBLE

                            binding.adUpArrowBtn.setOnClickListener {
                                binding.adUpArrowBtn.visibility = View.GONE
                                binding.adDownArrowBtn.visibility = View.VISIBLE
                                binding.imageBanner.visibility = View.GONE
                                /*val hide: Animation =
                                    AnimationUtils.loadAnimation(this@MainActivity, R.anim.top_bottom)
                                binding.adLayout.startAnimation(hide)*/
                            }
                            binding.adDownArrowBtn.setOnClickListener {
                                binding.adUpArrowBtn.visibility = View.VISIBLE
                                binding.adDownArrowBtn.visibility = View.GONE
                                binding.imageBanner.visibility = View.VISIBLE
                                /*val hide: Animation =
                                    AnimationUtils.loadAnimation(this@MainActivity, R.anim.bottom_top)
                                binding.adLayout.startAnimation(hide)*/
                            }
                        }

                    }
                } else {
                    binding.spinKit.visibility= View.GONE
                }
            } catch (e: Exception) {
                binding.spinKit.visibility= View.GONE
            }
        }
    }

    private fun loadDuplicateLotteryNumber() {
        Coroutines.main {
            try {
                binding.spinKit.visibility= View.VISIBLE
                val response=viewModel.getMiddleLessNumberList(page_number.toString(),item_count.toString())
                if (response.isSuccessful && response.code()==200) {
                    binding.spinKit.visibility= View.GONE
                    if (response.body()!=null) {
                        if (response.body()?.status.equals("success",true)) {
                            val temporaryList=response.body()?.data!!
                            list.addAll(temporaryList)
                            adapter.notifyDataSetChanged()
                            generateFinalList(temporaryList)
                            /*val Liveuserdb = FirebaseDatabase.getInstance().getReference("ActiveUsers")
                            val prefs = RegisterActivity.PreferenceHelper.customPreference(this, CUSTOM_PREF_NAME)
                            val map: HashMap<String, Any?> = HashMap()
                            map["phone"] = prefs.userPhone
                            map["activity"] = getString(R.string.middle_number)
                            Liveuserdb.child(prefs.userToken!!).setValue(map)*/
                        } else {
                            shortToast("message:- ${response.body()?.message}")
                        }
                    }
                } else {
                    binding.spinKit.visibility= View.GONE
                }
            } catch (e: Exception) {
                binding.spinKit.visibility= View.GONE
            }
        }
    }

    private fun generateFinalList(temporaryList: MutableList<LotteryNumberModel>) {
        list.clear()
//        for (i in 0 until temporaryList.size) {
//            if (list.isNullOrEmpty()) {
//                val lotteryNumberModel=LotteryNumberModel(temporaryList[i].id,temporaryList[i].lotteryNumber!!.substring(0,temporaryList[i].lotteryNumber!!.length-2),temporaryList[i].lotterySerialNumber,temporaryList[i].winType,temporaryList[i].winDate,temporaryList[i].winTime,temporaryList[i].winDayName)
//                list.add(lotteryNumberModel)
//                Log.d(Constants.TAG,"check:- ${CommonMethod.subStringFromString(temporaryList[i].lotterySerialNumber!!,2)}")
//            }
//            try {
//                for (j in 0 until list.size) {
//                    if (!CommonMethod.subStringFromString(temporaryList[i].lotteryNumber!!,2).equals(list[j].lotteryNumber)) {
//                        val lotteryNumberModel=LotteryNumberModel(temporaryList[i].id,temporaryList[i].lotteryNumber!!.substring(0,temporaryList[i].lotteryNumber!!.length-2),temporaryList[i].lotterySerialNumber,temporaryList[i].winType,temporaryList[i].winDate,temporaryList[i].winTime,temporaryList[i].winDayName)
//                        list.add(lotteryNumberModel)
//                        Log.d(Constants.TAG,"condition true")
//                        break
//                    } else {
//                        Log.d(Constants.TAG,"condition false")
//                    }
//                }
//            } catch (e: Exception) {
//                Log.d(Constants.TAG,"generating error:- ${e.message}")
//            }
//        }


        temporaryList.forEach parentLoop@{

            if (!it.lotteryNumber.equals("74065") && !it.lotteryNumber.equals("53586") && !it.lotteryNumber.equals("44511")
                && !it.lotteryNumber.equals("97531") && !it.lotteryNumber.equals("50675") && !it.lotteryNumber.equals("86937")
                && !it.lotteryNumber.equals("48587") && !it.lotteryNumber.equals("80864")) {

                if (it.lotteryNumber!!.length > 4) {
                    val lotteryNumberModel = LotteryNumberModel(
                        it.id,
                        it.lotteryNumber!!.substring(1, it.lotteryNumber!!.length - 2),
                        it.lotterySerialNumber,
                        it.winType,
                        it.winDate,
                        it.winTime,
                        it.winDayName,
                        it.SlotId,
                        it.name

                    )
                    list.add(lotteryNumberModel)
                } else {
                    val lotteryNumberModel = LotteryNumberModel(
                        it.id,
                        it.lotteryNumber!!.substring(0, it.lotteryNumber!!.length - 2),
                        it.lotterySerialNumber,
                        it.winType,
                        it.winDate,
                        it.winTime,
                        it.winDayName,
                        it.SlotId,
                        it.name
                    )
                    list.add(lotteryNumberModel)
                }
            }
                Log.d(Constants.TAG,"check:- ${CommonMethod.subStringFromString(it.lotteryNumber!!,2)}")

        }
        adapter.notifyDataSetChanged()
        Log.d(Constants.TAG,"temporary list size:- ${temporaryList.size} ")
        Log.d(Constants.TAG,"final list size:- ${list.size} ")
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