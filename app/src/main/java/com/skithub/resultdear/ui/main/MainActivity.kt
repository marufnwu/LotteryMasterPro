package com.skithub.resultdear.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.google.gson.JsonElement
import com.skithub.resultdear.BuildConfig
import com.skithub.resultdear.R
import com.skithub.resultdear.database.network.ApiInterface
import com.skithub.resultdear.database.network.MyApi
import com.skithub.resultdear.database.network.RetrofitClient
import com.skithub.resultdear.databinding.*
import com.skithub.resultdear.model.AdsImageModel
import com.skithub.resultdear.model.Metadata
import com.skithub.resultdear.model.response.LotterySlotResponse
import com.skithub.resultdear.ui.MyApplication
import com.skithub.resultdear.ui.common_number.CommonNumberActivity
import com.skithub.resultdear.ui.get_help.To_Get_HelpActivity
import com.skithub.resultdear.ui.got_a_prize.GotPrizeActivity
import com.skithub.resultdear.ui.importent_tips.ImportentTipsActivity
import com.skithub.resultdear.ui.last_digit_first_prize.LastDigitFirstPrizeActivity
import com.skithub.resultdear.ui.live_support.LiveSupportActivity
import com.skithub.resultdear.ui.lottery_number_check.LotteryNumberCheckActivity
import com.skithub.resultdear.ui.middle_number.MiddleNumberActivity
import com.skithub.resultdear.ui.middle_play_less.PlaylessActivity
import com.skithub.resultdear.ui.old_result.OldResultActivity
import com.skithub.resultdear.ui.paid_service.ServiceInfoActivity
import com.skithub.resultdear.ui.privacy_policy.PrivacyPolicyActivity
import com.skithub.resultdear.ui.register_activity.RegisterActivity
import com.skithub.resultdear.ui.special_number.SpecialNumberActivity
import com.skithub.resultdear.ui.special_or_bumper.SplOrBumperActivity
import com.skithub.resultdear.ui.today_result.TodayResultActivity
import com.skithub.resultdear.ui.tow_nd_middle_plays_more.OneStMiddleNumberActivity
import com.skithub.resultdear.ui.tow_nd_middle_plays_more.TwoNdMiddlePlaysMoreActivity
import com.skithub.resultdear.ui.winning_number.WinningNumberActivity
import com.skithub.resultdear.ui.yes_vs_pre.YesVsPreActivity
import com.skithub.resultdear.ui.yesterday_result.YesterdayResultActivity
import com.skyfishjy.library.RippleBackground
import org.json.JSONArray
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

import android.view.*
import android.media.AudioManager

import android.media.AudioAttributes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.skithub.resultdear.utils.*
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.skithub.resultdear.model.response.ActivityDialog
import com.skithub.resultdear.model.response.ActivityDialogResponse
import com.skithub.resultdear.ui.PlayerActivity

import com.skithub.resultdear.model.response.AudioTutorial
import com.skithub.resultdear.ui.audio_tutorial.AudioTutorialActivity
import com.skithub.resultdear.ui.buy_button_rule.ButtonBuyRuleActivity
import com.skithub.resultdear.ui.facebook_share.FbShareActivity
import com.skithub.resultdear.ui.lmpclass_videos.LmpClassVideoActivity
import com.skithub.resultdear.ui.lmpclass_videos.SpecialVideoActivity
import com.skithub.resultdear.ui.user_details.UserDetailsActivity
import com.skithub.resultdear.utils.admob.MyInterstitialAd


enum class AudioPlayingType{
    homeAudio,
    serverIssueAudio
}

class MainActivity : AppCompatActivity(){

    companion object {
        private const val REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION = 3009
    }

    lateinit var serverIssueDialogBinding: ServerIssueDialogBinding
    lateinit var audioPlayingType: AudioPlayingType
    lateinit var player: ExoPlayer
    private var isAudioDialogShwoing: Boolean = false;
    private  var mediaPlayer: MediaPlayer? = null
    private var isMpPause = false
    private lateinit var audioLoadingDialog: AudioLoadingDialog
    private lateinit var loadingDialog: LoadingDialog

    private var isActivityPause: Boolean = false
    private var isPause: Boolean = false
    private lateinit var myApi: MyApi
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private var tog: ActionBarDrawerToggle? = null
    private var mBackPressed: Long = 0
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var telephonyManager: TelephonyManager
    private var tutorialInfo: AdsImageModel?=null
    private lateinit var verupdateDialogBinding: UpdateDialogBinding
    private lateinit var disibleDialogBinding: AcDisibledDialogBinding
    private lateinit var connectionDialogBinding: ConnectionCheckDialogBinding
    private var verupdateAlertDialog: AlertDialog?=null
    private var disibleAlertDialog: AlertDialog?=null
    private var connectionAlertDialog: AlertDialog?=null
    private lateinit var inputMethodManager: InputMethodManager
    var licensePosition: String = "turjo"
    var licensePositionPRO: String = "turjo"
    var updateAction: String = "0"
    private var apiInterface: ApiInterface? = null
    var media : MediaPlayer? = null

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val factory= MainViewModelFactory((application as MyApplication).myApi)
        viewModel= ViewModelProvider(this,factory).get(MainViewModel::class.java)


        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.home_activity_title)


        checkReadExternalStoragePermission()

        myApi = (application as MyApplication).myApi
        connectivityManager=getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        audioLoadingDialog = AudioLoadingDialog(activity = this, cancelable = true)
        loadingDialog = LoadingDialog(this)
         serverIssueDialogBinding  = ServerIssueDialogBinding.inflate(layoutInflater)
        val renderersFactory = DefaultRenderersFactory(this)
        val trackSelectionFactory = AdaptiveTrackSelection.Factory()
        val trackSelectSelector = DefaultTrackSelector(trackSelectionFactory)
        val loadControl = DefaultLoadControl()


        player = ExoPlayer.Builder(this)
            .setLoadControl(loadControl)
            .setRenderersFactory(renderersFactory)
            .setTrackSelector(trackSelectSelector)
            .build()

        audioLoadingDialog.dialog!!.setOnDismissListener {
            if(audioPlayingType == AudioPlayingType.homeAudio ){
                player.stop()
            }
        }

        player.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
            }

            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                Log.d("ExoError", error.message!!)
                try{
                    if(audioPlayingType == AudioPlayingType.homeAudio){
                        dismissHomeAudioDialog()
                    }else{
                        serverIssueDialogBinding.tryAgainBtn.visibility = View.VISIBLE
                    }
                }catch (e:Exception){
                    dismissHomeAudioDialog()
                    serverIssueDialogBinding.tryAgainBtn.visibility = View.VISIBLE
                }
            }

            override fun onIsLoadingChanged(isLoading: Boolean) {
                super.onIsLoadingChanged(isLoading)

            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)

                if(player.isPlaying){
                    if(isActivityPause){
                        isPause = true
                        player.pause()
                    }
                }

                if(playbackState == Player.STATE_BUFFERING){
                    //Player buffering
                    loadingDialog.show()
                    Log.d("ExoPlayer", "Buffering")
                }else{
                    Log.d("ExoPlayer", "Not Buffering")

                    if(audioPlayingType == AudioPlayingType.homeAudio && player.isPlaying){
                        audioLoadingDialog.show()
                    }

                    loadingDialog.hide()
                }

                if(playbackState == Player.STATE_IDLE){
                    //the state when the player is stopped
                }else if(playbackState == Player.STATE_ENDED){
                    //The player finished playing all media.
                    if(audioPlayingType == AudioPlayingType.homeAudio){
                        dismissHomeAudioDialog()
                    }else{
                        serverIssueDialogBinding.tryAgainBtn.visibility = View.VISIBLE
                    }
                }
            }
        })



        val deviceMetadata = Metadata(this)

        //deviceMetadata.print()

        if (CommonMethod.haveInternet(connectivityManager)) {
                if (!SharedPreUtils.getStringFromStorageWithoutSuspend(this,Constants.userIdKey,Constants.defaultUserId).equals("")) {
                    intil()
                    getPremiumStatus()
                    GetLanguage()
                    sendToServer(deviceMetadata)
                } else {
                    var intent = Intent(this, RegisterActivity::class.java)
                    startActivity(intent)
                    finish()
                }
        }else{
            noInternetDialog(getString(R.string.no_internet),getString(R.string.no_internet_message))
        }

        //serverIssueDialog("Test", "Test");

        checkDialog()



    }

    private fun checkDialog() {
        myApi.getDialogInfo("MainActivity")
            .enqueue(
                object : Callback<ActivityDialogResponse> {
                    override fun onResponse(
                        call: Call<ActivityDialogResponse>,
                        response: Response<ActivityDialogResponse>
                    ) {

                        if(response.isSuccessful && response.body()!=null){
                            val activityDialogResponse = response.body()!!
                            if(!activityDialogResponse.error!!){
                                val activityDialog : ActivityDialog = activityDialogResponse.activityDialog!!

                                val dialogBinding = DialogHomeBinding.inflate(layoutInflater)

                                val builder= AlertDialog.Builder(this@MainActivity)
                                    .setCancelable(true)
                                    .setView(dialogBinding.root)

                                val qualityDialog = builder.create()

                                qualityDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)

                                if(!isFinishing && !isDestroyed ){
                                    if(activityDialog.showImage!! && !activityDialog.imageUrl.isNullOrEmpty()){
                                    Glide.with(this@MainActivity)
                                        .load(activityDialog.imageUrl)
                                        .placeholder(R.drawable.loading_placeholder)
                                        .into(dialogBinding.imgThumb)

                                    dialogBinding.imgThumb.setOnClickListener {
                                        CommonMethod.openLink(this@MainActivity, activityDialog.actionUrl!!)
                                    }
                                }else{
                                    dialogBinding.imgThumb.visibility = View.GONE
                                }

                                if(!activityDialog.description.isNullOrEmpty()){
                                    dialogBinding.txtDesc.text = activityDialog.description
                                }else{
                                    dialogBinding.txtDesc.visibility = View.GONE
                                }

                                dialogBinding.imgCancel.setOnClickListener {
                                    qualityDialog.dismiss()
                                }

                                dialogBinding.btnContinue.setOnClickListener {
                                    qualityDialog.dismiss()
                                }

                                if(activityDialog.action!!){
                                    dialogBinding.rootLayout.setOnClickListener {
                                        CommonMethod.openLink(this@MainActivity, activityDialog.actionUrl!!)
                                    }

                                    dialogBinding.txtDesc.setOnClickListener {
                                        CommonMethod.openLink(this@MainActivity, activityDialog.actionUrl!!)
                                    }
                                }

                                qualityDialog.show()

                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<ActivityDialogResponse>, t: Throwable) {

                    }

                }
            )
    }

    private fun dismissHomeAudioDialog(){
        if(audioLoadingDialog.dialog!=null){
            if(audioLoadingDialog.isLoading){
                audioLoadingDialog.hide()
            }
        }
    }

    private fun playAudio(mediaItem: MediaItem){
        player.stop()
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }


    private  fun sendToServer(metadata: Metadata){

        myApi.addDeviceMetadata(
            SharedPreUtils.getStringFromStorageWithoutSuspend(this, Constants.userIdKey, Constants.defaultUserId)!!,
                       "",
                       metadata.versionCode,
                       metadata.versionName,
                       metadata.androidVersion!!,
                       metadata.device,
                       metadata.manufacturer,
                       metadata.screenDensity!!,
                       metadata.screenSize!!,
               ).enqueue(object : Callback<LotterySlotResponse>{
            override fun onResponse(
                call: Call<LotterySlotResponse>,
                response: Response<LotterySlotResponse>
            ) {

            }

            override fun onFailure(call: Call<LotterySlotResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.message!!, Toast.LENGTH_SHORT).show()
            }

        })
//
//       Coroutines.main {
//           try {
//
//                   myApi.addDeviceMetadata(
//                       SharedPreUtils.getStringFromStorageWithoutSuspend(this, Constants.userIdKey, Constants.defaultUserId)!!,
//                       "",
//                       metadata.versionCode,
//                       metadata.versionName,
//                       metadata.androidVersion!!,
//                       metadata.device,
//                       metadata.manufacturer,
//                       metadata.screenDensity!!,
//                       metadata.screenSize!!,
//                   )
//           }catch (e: Exception){
//               throw e
//           }
//       }

    }


    private fun postUpdateCheck(){
        if (CommonMethod.haveInternet(connectivityManager)) {

                if (!SharedPreUtils.getStringFromStorageWithoutSuspend(this,Constants.userIdKey,Constants.defaultUserId).equals("")) {
                    intil()
                    getPremiumStatus()
                    GetLanguage()
                } else {
                    var intent = Intent(this, RegisterActivity::class.java)
                    startActivity(intent)
                    finish()
                }

        }else{
            noInternetDialog(getString(R.string.no_internet),getString(R.string.no_internet_message))
        }
    }
    private fun intil(){

        val rippleBackground = findViewById<View>(R.id.content) as RippleBackground
        rippleBackground.startRippleAnimation()

        apiInterface = RetrofitClient.getApiClient().create(ApiInterface::class.java)

        telephonyManager=getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        inputMethodManager=getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        binding.spinKit.visibility=View.GONE
        setupNavigationBar()

        binding.todayResultCardView.setOnClickListener {
            val gridIntent= Intent(applicationContext, TodayResultActivity::class.java)
            startActivity(gridIntent)

        }
        binding.yesterDayResultCardView.setOnClickListener {
            val gridIntent= Intent(applicationContext, YesterdayResultActivity::class.java)
            startActivity(gridIntent)
        }
        binding.yesterDayVsPreResultCardView.setOnClickListener {
            val gridIntent= Intent(applicationContext, YesVsPreActivity::class.java)
            startActivity(gridIntent)
        }
        binding.oldResultCardView.setOnClickListener {
            val gridIntent= Intent(applicationContext, OldResultActivity::class.java)
            startActivity(gridIntent)
        }
        binding.specialOrBumperResultCardView.setOnClickListener {
            val gridIntent= Intent(applicationContext, SplOrBumperActivity::class.java)
            startActivity(gridIntent)
        }
        binding.lotteryNumberCheck.setOnClickListener {
            val gridIntent= Intent(applicationContext, LotteryNumberCheckActivity::class.java)
            startActivity(gridIntent)
        }
        binding.winingNumberCardView.setOnClickListener {
            val  gridIntent= Intent(applicationContext, WinningNumberActivity::class.java)
            startActivity(gridIntent)
        }
        binding.commonNumberButton.setOnClickListener {
            if (!licensePosition.equals("turjo")) {
                val gridIntent = Intent(applicationContext, CommonNumberActivity::class.java)
                gridIntent.putExtra("license_position", licensePosition)
                startActivity(gridIntent)
            }
        }
        binding.middleNumberButton.setOnClickListener {
            if (!licensePosition.equals("turjo")) {
                val  gridIntent = Intent(applicationContext, MiddleNumberActivity::class.java)
                gridIntent.putExtra("license_position", licensePosition)
                startActivity(gridIntent)
            }
        }
        binding.playlessNumberButton.setOnClickListener {
            if (!licensePosition.equals("turjo")) {
                val  gridIntent = Intent(applicationContext, PlaylessActivity::class.java)
                gridIntent.putExtra("license_position", licensePosition)
                startActivity(gridIntent)
            }
        }
        binding.twondMiddlePrize.setOnClickListener {
            if (!licensePosition.equals("turjo")) {
                val  gridIntent = Intent(applicationContext, TwoNdMiddlePlaysMoreActivity::class.java)
                gridIntent.putExtra("license_position", licensePosition)
                startActivity(gridIntent)
            }
        }
        binding.oneSTMiddlePrize.setOnClickListener {

            if (!licensePosition.equals("turjo")) {
                val  gridIntent = Intent(applicationContext, OneStMiddleNumberActivity::class.java)
                gridIntent.putExtra("license_position", licensePosition)
                startActivity(gridIntent)
            }

        }
        binding.view.setOnClickListener {
            if (!licensePositionPRO.equals("turjo")) {
                val  gridIntent = Intent(applicationContext, SpecialNumberActivity::class.java)
                gridIntent.putExtra("license_position", licensePositionPRO)
                startActivity(gridIntent)
            }
        }

      ///////////
        binding.videoTipsButton.setOnClickListener {
            val gridIntent = Intent(applicationContext, LmpClassVideoActivity::class.java)
            //val gridIntent = Intent(applicationContext, ImportentTipsActivity::class.java)
            startActivity(gridIntent)
        }

        binding.videoSpecialButton.setOnClickListener {
            val gridIntent = Intent(applicationContext, SpecialVideoActivity::class.java)
            startActivity(gridIntent)
        }

        binding.btnFirstDigitFirstPrize.setOnClickListener {
            val intent = Intent(applicationContext, LastDigitFirstPrizeActivity::class.java)
            startActivity(intent)
        }

        binding.btnGotAPrize.setOnClickListener {
                    val intent = Intent(applicationContext, GotPrizeActivity::class.java)
                    startActivity(intent)
        }

        binding.btnButtonBuyRule.setOnClickListener {
            startActivity(Intent(this, ButtonBuyRuleActivity::class.java))
        }


        binding.btnAudioTutorial.setOnClickListener {
            startActivity(Intent(this, AudioTutorialActivity::class.java))
        }




    }

    private fun GetLanguage(){
        binding.navigationView.visibility = View.VISIBLE
        val headerView: View = binding.navigationView.getHeaderView(0)
        val banglalang = headerView.findViewById<RadioButton>(R.id.banglaLanguageTextView)
        val hindilang = headerView.findViewById<RadioButton>(R.id.hindiLanguageTextView)
        val englishlang = headerView.findViewById<RadioButton>(R.id.englishLanguageTextView)
        val lanCode: String = SharedPreUtils.getStringFromStorageWithoutSuspend(this,Constants.appLanguageKey,Constants.appDefaultLanCode)!!
        if (lanCode.equals("bn")){
            banglalang.isChecked = true
            hindilang.isChecked = false
            englishlang.isChecked = false

            binding.rbBang.isChecked = true
            binding.rbEng.isChecked = false
            binding.rbHindi.isChecked = false

        }else if (lanCode.equals("hi")){
            banglalang.isChecked = false
            hindilang.isChecked = true
            englishlang.isChecked = false

            binding.rbBang.isChecked = false
            binding.rbEng.isChecked = false
            binding.rbHindi.isChecked = true

        }else if (lanCode.equals("en_US")){
            banglalang.isChecked = false
            hindilang.isChecked = false
            englishlang.isChecked = true

            binding.rbBang.isChecked = false
            binding.rbEng.isChecked = true
            binding.rbHindi.isChecked = false
        }
        banglalang.setOnClickListener {
            changeLocale("bn")
        }
        englishlang.setOnClickListener {
            changeLocale("en_US")
        }
        hindilang.setOnClickListener {
            changeLocale("hi")
        }

        binding.rbBang.setOnClickListener {
            changeLocale("bn")
        }

        binding.rbHindi.setOnClickListener {
            changeLocale("hi")
        }

        binding.rbEng.setOnClickListener {
            changeLocale("en_US")
        }

    }

    private fun getPremiumStatus() {
        val call: Call<JsonElement> = apiInterface!!.getDeshCount(SharedPreUtils.getStringFromStorageWithoutSuspend(this,Constants.userIdKey,Constants.defaultUserId),SharedPreUtils.getStringFromStorageWithoutSuspend(this,Constants.fcmTokenKey,Constants.defaultUserToken),BuildConfig.VERSION_NAME)
        call.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful && response.code() == 200) {
                    try {
                        val rootArray = JSONArray(response.body().toString())
                        for (i in 0 until rootArray.length()) {

                            val paidlicense = rootArray.getJSONObject(i).getString("paid_license")
                            val prolicense = rootArray.getJSONObject(i).getString("pro_license")
                            val dbtoken = rootArray.getJSONObject(i).getString("token")
                            val acposition = rootArray.getJSONObject(i).getString("ac_position")
                            val activestatus = rootArray.getJSONObject(i).getString("active_status")
                            val versioncode = rootArray.getJSONObject(i).getString("version_code")
                            val videothumbail = rootArray.getJSONObject(i).getString("video_thumbail")
                            val videolink = rootArray.getJSONObject(i).getString("video_link")
                            val description = rootArray.getJSONObject(i).getString("description")
                            val updatePosition = rootArray.getJSONObject(i).getString("update_position")
                            val bannerimage = rootArray.getJSONObject(i).getString("banner_image")
                            val targetlink = rootArray.getJSONObject(i).getString("target_link")
                            val ImageUrl = rootArray.getJSONObject(i).getString("ImageUrl")
                            val TargetUrl = rootArray.getJSONObject(i).getString("TargetUrl")
                            val TargetUrlStatus = rootArray.getJSONObject(i).getString("ActiveStatus")
                            val marqueeTxt = rootArray.getJSONObject(i).getString("text")
                            val homeAudioUrl = rootArray.getJSONObject(i).getString("homeAudioUrl")
                            CommonMethod.accountAge = rootArray.getJSONObject(i).getString("account_age")

                            if (!dbtoken.equals(SharedPreUtils.getStringFromStorageWithoutSuspend(this@MainActivity,Constants.fcmTokenKey,Constants.defaultUserToken)) || !activestatus.equals("1")){
                                logOutWithoutupdate()
                            }

                            if(videothumbail.length > 6){
                                if (updateAction.equals("0")){
                                    updateAction = "1"
                                checkMandatoryUpdate(versioncode,description,videothumbail,videolink,updatePosition)
                                }
                                }

                            if (bannerimage.length > 6){
                                binding.adUpArrowBtn.setImageResource(R.drawable.ic_arrow_down_icon)
                                Glide.with(applicationContext).load(bannerimage).placeholder(R.drawable.loading_placeholder).into(binding.imageBanner)
                                binding.imageBanner.setOnClickListener {
//                                    val webIntent: Intent= Intent(Intent.ACTION_VIEW,Uri.parse(targetlink))
//                                    startActivity(Intent.createChooser(webIntent,"Choose one:"))

                                    CommonMethod.openLink(this@MainActivity, targetlink)
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

                            if (!acposition.equals("0")){

                                licensePosition = paidlicense


                                licensePositionPRO = prolicense

                                //initAll()
                            }else{
                                AcDisibleDialog()
                            }

                            if (marqueeTxt.length > 6) {
                                binding.marqueeText.visibility=View.VISIBLE
                                binding.marqueeText.text = marqueeTxt
                                binding.marqueeText.setSelected(true)
                            }else{
                                binding.marqueeText.visibility=View.GONE
                            }

                            if (!ImageUrl.isNullOrEmpty()){
                                Glide.with(applicationContext).load(ImageUrl).placeholder(R.drawable.loading_placeholder).into(binding.tutorialImageView)
                            }

                            if (TargetUrlStatus.equals("true")){
                                binding.tutorialImageView.setOnClickListener{

                                    CommonMethod.openLink(this@MainActivity, TargetUrl)
                                }
                            }

                            if(!homeAudioUrl.isNullOrEmpty()){
                                showHomeAudioUrl(homeAudioUrl)
                            }




                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@MainActivity,
                            "error found:- " + e.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Log.d("RetrofitError", response.message())
                    Toast.makeText(
                        this@MainActivity,
                        "Unknown error occurred.",
                        Toast.LENGTH_SHORT
                    ).show()
                    serverIssueDialog("Server Down",getString(R.string.server_issue_msg))



                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                t.printStackTrace()
                serverIssueDialog("Server Down",getString(R.string.server_issue_msg))

                //noInternetDialog(getString(R.string.weak_internet),getString(R.string.weak_internet_message))
            }
        })
    }

    private fun showHomeAudioUrl(homeAudioUrl: String) {
        val mediaItem = MediaItem.fromUri(homeAudioUrl)
        playAudio(mediaItem)
        audioPlayingType = AudioPlayingType.homeAudio
        //audioLoadingDialog.show()
        //loadingDialog.show()
    }


    private fun AcDisibleDialog(){
        disibleDialogBinding= AcDisibledDialogBinding.inflate(layoutInflater)
        disibleDialogBinding.LogoutButton.setOnClickListener {
            logOut()
        }
        disibleDialogBinding.whatsAppBtn.setOnClickListener {
            try {
                val mobile = "919062686255"
                val msg = ""
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://api.whatsapp.com/send?phone=$mobile&text=$msg")
                    )
                )
            } catch (e: java.lang.Exception) {
                Toast.makeText(this@MainActivity, "WhatsApp not Installed", Toast.LENGTH_SHORT).show()
            }
        }
        val builder=AlertDialog.Builder(this@MainActivity)
            .setCancelable(false)
            .setView(disibleDialogBinding.root)
        disibleAlertDialog=builder.create()
        if (disibleAlertDialog?.window!=null) {
            disibleAlertDialog?.window!!.attributes.windowAnimations=R.style.DialogTheme
        }
        if (!isFinishing) {
            disibleAlertDialog?.show()
        }
    }




    private fun changeLocale(lanCode: String) {
        Coroutines.io {
            SharedPreUtils.setStringToStorage(applicationContext,Constants.appLanguageKey,lanCode)
            SharedPreUtils.setBooleanToStorage(applicationContext,Constants.appLanguageStatusKey,true)
        }
        val gridIntent = Intent(applicationContext, MainActivity::class.java)
        startActivity(gridIntent)

        finish()
    }


    private fun setupNavigationBar() {
        tog=ActionBarDrawerToggle(this,binding.drawerLayout,binding.toolbar,R.string.open,R.string.close)
        binding.drawerLayout.addDrawerListener(tog!!)
        tog?.syncState()




        binding.navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {

                R.id.menuLogout->{
                    logOut()
                }
                R.id.menuUserDetails->{
                    startActivity(Intent(this,UserDetailsActivity::class.java))
                }
                R.id.updateCheck->{
                    CommonMethod.openAppLink(this@MainActivity)
                }

                R.id.share -> {
                    CommonMethod.shareAppLink(this)
                }
                R.id.liveSupport -> {
                    startActivity(Intent(this,LiveSupportActivity::class.java))
                    //startActivity(Intent(this,PlayerActivity::class.java))
                }R.id.shareToFb -> {
                    startActivity(Intent(this,FbShareActivity::class.java))
                    //startActivity(Intent(this,PlayerActivity::class.java))
                }
                R.id.appPrivacy -> {
                    startActivity(Intent(this,PrivacyPolicyActivity::class.java))
                }
                R.id.contactUs -> {
                    startActivity(Intent(this, To_Get_HelpActivity::class.java))
                }
                R.id.moreApps -> {
                    CommonMethod.openConsoleLink(this,Constants.consoleId)
                }
                R.id.facebook -> {
                    try {
                        val intent =
                            Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/110262281375508"))
                        startActivity(intent)
                    } catch (e: java.lang.Exception) {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://facebook.com/lotterymasterpro")
                            )
                        )
                    }
                }
                R.id.youtube -> {
                    val webIntent: Intent= Intent(Intent.ACTION_VIEW,Uri.parse("https://youtube.com/channel/UCtFKXV1CTTPfnqsGne8Hpwg"))
                    startActivity(Intent.createChooser(webIntent,"Choose one:"))
                }
            }
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
            true
        }

    }
    private fun serverIssueDialog(til: String, msg: String) {
        serverIssueDialogBinding.connectionTitle.text = til
        serverIssueDialogBinding.connectionMessage.text = msg

        if(!isActivityPause){
            if(player!=null){
                val rawDataSource = RawResourceDataSource(this)
                // open the /raw resource file
                rawDataSource.open(DataSpec(RawResourceDataSource.buildRawResourceUri(R.raw.serverissue)))
                var mediaItem : MediaItem = MediaItem.fromUri(rawDataSource.uri!!)

                audioPlayingType = AudioPlayingType.serverIssueAudio
                playAudio(mediaItem)

                serverIssueDialogBinding.tryAgainBtn.visibility = View.INVISIBLE
            }
        }



        serverIssueDialogBinding.imgThumb.setOnClickListener {
            val url = "https://www.youtube.com/watch?v=xf8x1V1JscQ"
            val webIntent: Intent= Intent(Intent.ACTION_VIEW,Uri.parse(url))
            startActivity(Intent.createChooser(webIntent,"Choose one:"))
        }
            try{

                if(serverIssueDialogBinding.root.parent!=null){
                    (serverIssueDialogBinding.root.parent as ViewGroup).removeView(serverIssueDialogBinding.root)
                }


                val builder= AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setView(serverIssueDialogBinding.root)

                var serverIssueAlertDialog=builder.create()
                if (serverIssueAlertDialog.window!=null) {
                    serverIssueAlertDialog.window!!.attributes.windowAnimations=R.style.DialogTheme
                }

                serverIssueDialogBinding.tryAgainBtn.setOnClickListener {
                    if (CommonMethod.haveInternet(connectivityManager)) {
                        intil()
                        getPremiumStatus()
                        serverIssueAlertDialog.dismiss()
                    }
                }

                serverIssueAlertDialog.setOnDismissListener {
                    if(media!=null){
                        if(media!!.isPlaying){
                            media!!.stop()
                        }
                    }
                }
                if (!isFinishing) {
                    serverIssueAlertDialog.show()
                }
            }catch (e:Exception){
                e.printStackTrace()
            }

    }

    private fun noInternetDialog(til: String, msg: String) {
        connectionDialogBinding= ConnectionCheckDialogBinding.inflate(layoutInflater)
        connectionDialogBinding.connectionTitle.text = til
        connectionDialogBinding.connectionMessage.text = msg
        connectionDialogBinding.tryAgainBtn.setOnClickListener {
            if (CommonMethod.haveInternet(connectivityManager)) {
                intil()
                getPremiumStatus()
                connectionAlertDialog?.dismiss()
            }
        }
        val builder=AlertDialog.Builder(this)
            .setCancelable(false)
            .setView(connectionDialogBinding.root)
        connectionAlertDialog=builder.create()

        if (connectionAlertDialog?.window!=null) {
            connectionAlertDialog?.window!!.attributes.windowAnimations=R.style.DialogTheme
        }

        if (!isFinishing && !isDestroyed) {
            connectionAlertDialog?.show()
        }


    }



    private fun checkMandatoryUpdate(update_version: String,update_description: String,update_yt_thumbail: String,update_yt_url: String,update_position: String) {


                    if (BuildConfig.VERSION_NAME < update_version){

                        verupdateDialogBinding= UpdateDialogBinding.inflate(layoutInflater)
                        verupdateDialogBinding.upversioncode.text = "${ "Version : "} ${update_version}"
                        verupdateDialogBinding.updatedescription.text = update_description

                        Glide.with(this@MainActivity).load(update_yt_thumbail).placeholder(R.drawable.loading_placeholder).into(verupdateDialogBinding.utthumbail)

                        verupdateDialogBinding.utthumbail.setOnClickListener{
                            val webIntent: Intent= Intent(Intent.ACTION_VIEW,Uri.parse(update_yt_url))
                            startActivity(Intent.createChooser(webIntent,"Choose one:"))
                        }


                        verupdateDialogBinding.updateButton.setOnClickListener {
                            cacheDir.deleteRecursively()
                            CommonMethod.openAppLink(this@MainActivity)
                        }
                        if (update_position.equals("o")){
                            verupdateDialogBinding.updateCancel.isEnabled = true
                            verupdateDialogBinding.updateCancel.setTextColor(Color.parseColor("#FFFFFFFF"))
                            verupdateDialogBinding.updateCancel.setOnClickListener {
                                verupdateAlertDialog?.dismiss()
                                getPremiumStatus()
                            }
                        }

                        val builder=AlertDialog.Builder(this@MainActivity)
                            .setCancelable(false)
                            .setView(verupdateDialogBinding.root)
                        verupdateAlertDialog=builder.create()
                        if (verupdateAlertDialog?.window!=null) {
                            verupdateAlertDialog?.window!!.attributes.windowAnimations=R.style.DialogTheme
                        }
                        if (!isFinishing) {
                            verupdateAlertDialog?.show()
                        }
                    }

    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
           /* if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                super.onBackPressed()
            } else {
                Toast.makeText(baseContext, "Press Back again to leave!", Toast.LENGTH_SHORT).show()
            }
            mBackPressed = System.currentTimeMillis()*/
            super.onBackPressed()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)


        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    binding.drawerLayout.openDrawer(GravityCompat.START)
                }
                return true
            }
            R.id.option_share -> {
                CommonMethod.shareAppLink(this)
            }
            R.id.option_moreApps -> {
                CommonMethod.openConsoleLink(this,Constants.consoleId)
            }
            R.id.option_logout -> {
                logOut()
            }
            R.id.option_license -> {
                intent= Intent(applicationContext, ServiceInfoActivity::class.java)
                intent.putExtra("userID",SharedPreUtils.getStringFromStorageWithoutSuspend(this,Constants.userIdKey,Constants.defaultUserId))
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun logOut(){

        Coroutines.main {
            try {
                binding.spinKit.visibility=View.VISIBLE
                val response = viewModel.getLogout(SharedPreUtils.getStringFromStorageWithoutSuspend(this,Constants.userIdKey,Constants.defaultUserId).toString())
                if (response.isSuccessful && response.code() == 200) {
                    if (response.body() != null) {
                        if (response.body()?.status.equals("clear")) {
                            SharedPreUtils.setStringToStorage(applicationContext,Constants.userIdKey,"")
                            SharedPreUtils.setStringToStorage(applicationContext,Constants.fcmTokenKey,"")
                            intent = Intent(applicationContext, RegisterActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }catch (e: Exception) {
                noInternetDialog(getString(R.string.weak_internet),getString(R.string.weak_internet_message))
            }
        }
    }
    private fun logOutWithoutupdate(){
        Coroutines.main {
            SharedPreUtils.setStringToStorage(applicationContext, Constants.userIdKey, "")
            SharedPreUtils.setStringToStorage(applicationContext, Constants.fcmTokenKey, "")
            intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }






    override fun attachBaseContext(newBase: Context?) {
        if (newBase!=null) {
            super.attachBaseContext(CommonMethod.updateLanguage(newBase))
        } else {
            super.attachBaseContext(newBase)
        }
    }


    override fun onResume() {
        Log.d("State", "onResume")
        super.onResume()
        isActivityPause = false

        if(isPause && player!=null){
            player.play()
        }

    }

    override fun onPause() {
        Log.d("State", "onPause")

        super.onPause()
        isActivityPause = true
        if(player!=null){
            if(player.isPlaying){
                isPause = true
                player.pause()
            }
        }

    }

    override fun onDestroy() {
        Log.d("State", "onDestroy")

        super.onDestroy()
        if(player!=null){
            player.release()
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION -> {
                if (grantResults.getOrNull(0) == PackageManager.PERMISSION_DENIED) {
                    showReadExternalStoragePermissionDeniedMessage()
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun checkReadExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestReadExternalStoragePermission()
        }
    }

    private fun requestReadExternalStoragePermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION)
    }

    private fun showReadExternalStoragePermissionDeniedMessage() {
        Toast.makeText(this, "Read external storage permission has denied", Toast.LENGTH_SHORT).show()
    }

}