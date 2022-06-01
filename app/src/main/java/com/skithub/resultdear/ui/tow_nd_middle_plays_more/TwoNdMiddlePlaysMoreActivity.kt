package com.skithub.resultdear.ui.tow_nd_middle_plays_more

import android.content.Context
import android.content.Intent
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
import com.skithub.resultdear.R
import com.skithub.resultdear.adapter.DuplicateLotteryNumberRecyclerAdapter
import com.skithub.resultdear.model.LotteryNumberModel
import com.skithub.resultdear.ui.MyApplication
import com.skithub.resultdear.utils.MyExtensions.shortToast

import android.graphics.drawable.Drawable
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.view.WindowManager
import android.widget.ImageView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.skithub.resultdear.adapter.CustomerCareNumAdapter
import com.skithub.resultdear.database.network.ApiInterface
import com.skithub.resultdear.database.network.MyApi
import com.skithub.resultdear.database.network.RetrofitClient
import com.skithub.resultdear.databinding.ActivityTwoNdMiddlePlaysMoreBinding
import com.skithub.resultdear.databinding.ConnectionCheckDialogBinding
import com.skithub.resultdear.model.response.AudioResponse
import com.skithub.resultdear.model.response.ContactListBannerResponse
import com.skithub.resultdear.ui.buy_button_rule.ButtonBuyRuleActivity
import com.skithub.resultdear.utils.*
import com.skyfishjy.library.RippleBackground
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TwoNdMiddlePlaysMoreActivity : ScreenShotDetectWithBase() {
    private lateinit var audioLoadingDialog: AudioLoadingDialog
    private var isPause: Boolean = false
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var binding: ActivityTwoNdMiddlePlaysMoreBinding
    private lateinit var viewModel: TwoNdNumberViewModel
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
    //Toast.makeText(this@MiddleNumberActivity, android_id, Toast.LENGTH_SHORT).show()
    private var apiInterface: ApiInterface? = null
    val CUSTOM_PREF_NAME = "User_data_extra"
    private lateinit var myApi : MyApi
    private  var mediaPlayer: MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        binding= ActivityTwoNdMiddlePlaysMoreBinding.inflate(layoutInflater)
        myApi = (application as MyApplication).myApi
        val factory= TwoNdMiddleNumberViewModelFactory(myApi)
        viewModel= ViewModelProvider(this,factory).get(TwoNdNumberViewModel::class.java)

        CommonMethod.disableScreenCapture(this)


        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.two_nd_middle_body)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        apiInterface = RetrofitClient.getApiClient().create(ApiInterface::class.java)
        loadingDialog = LoadingDialog(this)
        audioLoadingDialog = AudioLoadingDialog(activity = this, cancelable = false)


        license_check = intent.getStringExtra("license_position").toString()

        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )

        }

        mediaPlayer!!.setOnPreparedListener {
            it.start()
            loadingDialog.hide()
            audioLoadingDialog.show()
        }

        mediaPlayer!!.setOnErrorListener { p0, p1, p2 ->
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
            audioLoadingDialog.hide()
            true
        }

        mediaPlayer!!.setOnCompletionListener {

            audioLoadingDialog.hide()
        }

        initAll()

        if (CommonMethod.haveInternet(connectivityManager)) {
            if (license_check.equals("0")){
                getContactInformation(false)
            }else if (license_check.equals("2")){
                binding.coomingSoon.visibility = View.VISIBLE
                binding.standerdLayout.visibility = View.GONE
            }else{
                binding.recyclerView.visibility = View.VISIBLE
                binding.standerdLayout.visibility = View.GONE
                checkBannerAd()
                setupRecyclerView()
                loadDuplicateLotteryNumber()
                getContactInformation(true)
                loadPremiumBanner()
            }
        }else{
            noInternetDialog(getString(R.string.no_internet),getString(R.string.no_internet_message))
        }

    }

    private fun loadPremiumBanner() {

        Coroutines.main {
            CommonMethod.getBanner("VipPremium2", binding.ivPremBanner,myApi, applicationContext)
        }



    }

    private fun checkBannerAd(){
        Coroutines.main {

            try {
                val res =  myApi.getBanner("VipMemBannerAd")
                if(res.isSuccessful && res.body()!=null){
                    res.body()!!.let { banner ->
                        if(!banner!!.error){
                            if (banner.imageUrl != null) {
                                binding.adLayout.visibility = View.VISIBLE

                                binding.adUpArrowBtn.setImageResource(R.drawable.ic_arrow_down_icon)
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

                                Glide.with(applicationContext)
                                    .load(banner.imageUrl)
                                    .thumbnail(Glide.with(applicationContext).load(R.drawable.placeholder))
                                    .into(binding.imageBanner)
                                binding.imageBanner.setOnClickListener(View.OnClickListener {
                                    if (banner.actionType === 1) {
                                        //open url
                                        if (banner.actionUrl != null) {
                                            val url: String = banner.actionUrl!!
                                            val linkHost = Uri.parse(url).host
                                            val uri = Uri.parse(url)
                                            if (linkHost == null) {
                                                return@OnClickListener
                                            }
                                            if (linkHost == "play.google.com") {
                                                val appId = uri.getQueryParameter("id")
                                                val intent = Intent(Intent.ACTION_VIEW)
                                                intent.data = Uri.parse("market://details?id=$appId")
                                                startActivity(intent)
                                            } else if (linkHost == "www.youtube.com") {
                                                try {
                                                    val intent = Intent(Intent.ACTION_VIEW, uri)
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                    intent.setPackage("com.google.android.youtube")
                                                    startActivity(intent)
                                                }catch (e : Exception){
                                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                    startActivity(intent)
                                                }
                                            } else if (url != null && (url.startsWith("http://") || url.startsWith(
                                                    "https://"
                                                ))
                                            ) {
                                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                startActivity(intent)
                                            }
                                        }
                                    } else if (banner.actionType === 2) {
                                        //open activity
                                    }
                                })

                            }else{
                                binding.adLayout.visibility = View.GONE
                            }
                        }else{
                            binding.adLayout.visibility = View.GONE
                            Log.d("Banner", banner.msg!!)
                        }
                    }
                }

            }catch (e : Exception){

            }


        }
    }

    private fun getAudioFile() {
        loadingDialog.show()
        try {
            myApi.getAudio("AudioFileVip")
                .enqueue(object : Callback<AudioResponse>{
                    override fun onResponse(call: Call<AudioResponse>, response: Response<AudioResponse>){
                        loadingDialog.hide()
                        if(response.isSuccessful && response.body()!=null){
                            if(!response.body()!!.error!!){
                                val audio = response.body()!!.audio
                                if(mediaPlayer!=null){
                                    if( audio!!.audioUrl!=null){
                                        mediaPlayer!!.setDataSource(audio.audioUrl)
                                        mediaPlayer!!.prepareAsync()
                                        loadingDialog.show()
                                    }
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<AudioResponse>, t: Throwable) {
                        loadingDialog.hide()
                    }

                })

        }catch (e : Exception){
            loadingDialog.hide()
        }
    }
    private fun getContactInformation(isPremium : Boolean) {
        Coroutines.main {
            try {
                loadingDialog.show()
                val response=viewModel.getPaidForContact("2",
                    SharedPreUtils.getStringFromStorageWithoutSuspend(this,Constants.userIdKey,Constants.defaultUserId).toString())


                myApi.getCustContactNumberWithBanner("Vip2", "VipBefore2")
                    .enqueue(
                        object: Callback<ContactListBannerResponse> {
                            override fun onResponse(
                                call: Call<ContactListBannerResponse>,
                                response: Response<ContactListBannerResponse>
                            ) {
                                if (response.isSuccessful && response.code()==200) {
                                    if (response.body()!=null) {
                                        val bannerRes = response.body()!!.banner
                                        val contactRes = response.body()!!.contacts

                                        loadingDialog.hide()
                                        binding.spinKit.visibility= View.GONE
                                        binding.standerdLayout.visibility = View.VISIBLE

                                        if(isPremium){
                                            binding.content.visibility = View.GONE
                                            binding.tvInstruction.visibility = View.GONE
                                            contactRes?.let { res->
                                                res.error?.let { err->
                                                    if(!err){

                                                        if(!contactRes.whatsapp.isNullOrEmpty()){
                                                            binding.whatsAppBtn.visibility = View.VISIBLE

                                                            CommonMethod.openWhatsapp(this@TwoNdMiddlePlaysMoreActivity, binding.whatsAppBtn, contactRes.whatsapp!!)
                                                        }

                                                    }
                                                }
                                            }

                                        }else{
                                            binding.whatsAppBtn.visibility = View.GONE
                                            val rippleBackground = findViewById<View>(R.id.content) as RippleBackground
                                            rippleBackground.startRippleAnimation()

                                            loadingDialog.show()

                                            bannerRes?.let { banner->
                                                if(!banner.error){
                                                    if(!isFinishing && !isDestroyed){
                                                        Glide.with(this@TwoNdMiddlePlaysMoreActivity)
                                                            .load(response.body()!!.banner!!.imageUrl)

                                                            .placeholder(R.drawable.loading_placeholder)
                                                            .fitCenter()

                                                            .listener(
                                                                object : RequestListener<Drawable> {
                                                                    override fun onLoadFailed(
                                                                        e: GlideException?,
                                                                        model: Any?,
                                                                        target: Target<Drawable>?,
                                                                        isFirstResource: Boolean
                                                                    ): Boolean {
                                                                        loadingDialog.hide()
                                                                        return true
                                                                    }

                                                                    override fun onResourceReady(
                                                                        resource: Drawable?,
                                                                        model: Any?,
                                                                        target: Target<Drawable>?,
                                                                        dataSource: DataSource?,
                                                                        isFirstResource: Boolean
                                                                    ): Boolean {

                                                                        loadingDialog.hide()
                                                                        return false
                                                                    }

                                                                }
                                                            ).into(binding.ytthumbail)
                                                    }
                                                    CommonMethod.setShakeAnimation(binding.ytthumbail, this@TwoNdMiddlePlaysMoreActivity)
                                                    binding.ytthumbail.setOnClickListener {
                                                        CommonMethod.openLink(this@TwoNdMiddlePlaysMoreActivity, banner.actionUrl!!);
                                                    }
                                                }else{
                                                    binding.thumbnailLayout.visibility = View.GONE
                                                }
                                            }
                                            Coroutines.main {
                                                val whatsAppRes = myApi.getWhatsapp("Vip");
                                                if(whatsAppRes.isSuccessful && whatsAppRes.body()!=null){
                                                    if(!whatsAppRes.body()!!.error!!) {
                                                        if (whatsAppRes.body()!!.number != null) {
                                                            binding.whatsAppBtn.visibility = View.VISIBLE

                                                            CommonMethod.openWhatsapp(this@TwoNdMiddlePlaysMoreActivity, binding.whatsAppBtn, whatsAppRes.body()!!.number!!)

                                                        }
                                                    }
                                                }}

                                            getAudioFile()


                                        }
                                        contactRes?.let { res->
                                            res.error?.let { err->
                                                if(!err){
                                                    binding.recyCustNumbers.layoutManager = LinearLayoutManager(this@TwoNdMiddlePlaysMoreActivity)
                                                    binding.recyCustNumbers.setHasFixedSize(true)

                                                    val contactAdapter = CustomerCareNumAdapter(this@TwoNdMiddlePlaysMoreActivity, contactRes.numberList!!.toMutableList())
                                                    binding.recyCustNumbers.adapter = contactAdapter
                                                }
                                            }
                                        }




//                        if (response.body()?.banner_image!!.length > 6){
//                            binding.adUpArrowBtn.setImageResource(R.drawable.ic_arrow_down_icon)
//                            Glide.with(this@TwoNdMiddlePlaysMoreActivity).load(response.body()?.banner_image).placeholder(R.drawable.loading_placeholder).into(binding.imageBanner)
//                            binding.imageBanner.setOnClickListener {
//                                val webIntent: Intent= Intent(Intent.ACTION_VIEW,Uri.parse(response.body()?.target_link))
//                                startActivity(Intent.createChooser(webIntent,"Choose one:"))
//                            }
//                            /*val hide: Animation =
//                                AnimationUtils.loadAnimation(this@MainActivity, R.anim.bottom_top)
//                            binding.adLayout.startAnimation(hide)*/
//                            binding.adLayout.visibility = View.VISIBLE
//                            binding.adUpArrowBtn.visibility = View.VISIBLE
//
//                            binding.adUpArrowBtn.setOnClickListener {
//                                binding.adUpArrowBtn.visibility = View.GONE
//                                binding.adDownArrowBtn.visibility = View.VISIBLE
//                                binding.imageBanner.visibility = View.GONE
//                                /*val hide: Animation =
//                                    AnimationUtils.loadAnimation(this@MainActivity, R.anim.top_bottom)
//                                binding.adLayout.startAnimation(hide)*/
//                            }
//                            binding.adDownArrowBtn.setOnClickListener {
//                                binding.adUpArrowBtn.visibility = View.VISIBLE
//                                binding.adDownArrowBtn.visibility = View.GONE
//                                binding.imageBanner.visibility = View.VISIBLE
//                                /*val hide: Animation =
//                                    AnimationUtils.loadAnimation(this@MainActivity, R.anim.bottom_top)
//                                binding.adLayout.startAnimation(hide)*/
//                            }
//                        }
                                    }
                                } else {
                                    loadingDialog.hide()
                                    binding.spinKit.visibility= View.GONE
                                }
                            }

                            override fun onFailure(
                                call: Call<ContactListBannerResponse>,
                                t: Throwable
                            ) {

                            }

                        })


            } catch (e: Exception) {
                loadingDialog.hide()
                binding.spinKit.visibility= View.GONE
            }
        }

    }


    private suspend fun loadWhatsApp(place: String, whatsAppBtn: ImageView){
        val whatsAppRes = myApi.getWhatsapp(place);
        if(whatsAppRes.isSuccessful && whatsAppRes.body()!=null){
            if(!whatsAppRes.body()!!.error!!) {
                if (whatsAppRes.body()!!.number != null) {
                    whatsAppBtn.visibility = View.VISIBLE
                    whatsAppBtn.setOnClickListener {
                        try {
                            val mobile = whatsAppRes.body()!!.number
                            val msg = ""
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://api.whatsapp.com/send?phone=$mobile&text=$msg")
                                )
                            )
                        } catch (e: java.lang.Exception) {
                            Toast.makeText(
                                this,
                                "WhatsApp not Installed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
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
                    getContactInformation(false)
                    binding.recyclerView.visibility = View.GONE
                    binding.standerdLayout.visibility = View.VISIBLE
                }else{
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.standerdLayout.visibility = View.GONE
                    setupRecyclerView()
                    loadDuplicateLotteryNumber()
                    getContactInformation(true)

                }
                connectionAlertDialog?.dismiss()
            }
        }
        val builder=AlertDialog.Builder(this@TwoNdMiddlePlaysMoreActivity)
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

        binding.btnButtonBuyRule.setOnClickListener {
            startActivity(Intent(this, ButtonBuyRuleActivity::class.java))
        }
    }

    private fun setupRecyclerView() {
        adapter= DuplicateLotteryNumberRecyclerAdapter(this,list)
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
//                            loadDuplicateLotteryNumber()
//                            isLoading = true
//                        }
//                    }
//                }
//            }
//        })
    }

    private fun loadDuplicateLotteryNumber() {
        Coroutines.main {
            try {
                binding.spinKit.visibility= View.VISIBLE
                val response=viewModel.getMiddlePlaysMoreNumberList(page_number.toString(),item_count.toString())
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


//CommonMethod.subStringFromString(it.lotteryNumber!!,2)


            if (!it.lotteryNumber.equals("00576")) {

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


    override fun attachBaseContext(newBase: Context?) {
        if (newBase!=null) {
            super.attachBaseContext(CommonMethod.updateLanguage(newBase))
        } else {
            super.attachBaseContext(newBase)
        }
    }
    override fun onResume() {
        super.onResume()
        if(isPause){
            mediaPlayer!!.start()
        }
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer!!.isPlaying.let {
            if(it){
                isPause = true
                mediaPlayer!!.pause()
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
        loadingDialog.hide()
    }

}