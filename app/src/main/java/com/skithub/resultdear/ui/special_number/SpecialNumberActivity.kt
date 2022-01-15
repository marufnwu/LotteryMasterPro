package com.skithub.resultdear.ui.special_number

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.skithub.resultdear.R
import com.skithub.resultdear.database.network.ApiInterface
import com.skithub.resultdear.database.network.MyApi
import com.skithub.resultdear.database.network.RetrofitClient
import com.skithub.resultdear.databinding.ActivitySpecialNumberBinding
import com.skithub.resultdear.model.response.AudioResponse
import com.skithub.resultdear.ui.MyApplication
import com.skithub.resultdear.ui.PlayerActivity
import com.skithub.resultdear.ui.middle_number.MiddleNumberViewModel
import com.skithub.resultdear.ui.middle_number.MiddleNumberViewModelFactory
import com.skithub.resultdear.utils.*
import com.skyfishjy.library.RippleBackground
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SpecialNumberActivity : AppCompatActivity() {
    private var isPause: Boolean = false
    private  var mediaPlayer: MediaPlayer? = null
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var audioLoadingDialog: AudioLoadingDialog
    private lateinit var binding: ActivitySpecialNumberBinding
    private var apiInterface: ApiInterface? = null
    val CUSTOM_PREF_NAME = "User_data_extra"
    private lateinit var viewModel: MiddleNumberViewModel
    private var license_check: String? = null
    private lateinit var myApi : MyApi
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySpecialNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        myApi = (application as MyApplication).myApi
        val factory= MiddleNumberViewModelFactory(myApi)
        viewModel= ViewModelProvider(this,factory).get(MiddleNumberViewModel::class.java)
        supportActionBar?.title = getString(R.string.guaranteed_footer)
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
            Toast.makeText(this@SpecialNumberActivity, "error", Toast.LENGTH_SHORT).show()
            audioLoadingDialog.hide()
            loadingDialog.hide()
            true
        }

        mediaPlayer!!.setOnCompletionListener {

            audioLoadingDialog.hide()
        }


        if (license_check.equals("0")){
            getContctInformation(false)
        }else if (license_check.equals("2")){
            binding.coomingSoon.visibility = View.VISIBLE
            binding.standerdLayout.visibility = View.GONE
        }else{
            getDataLoad()
            getContctInformation(true)
            loadPremiumBanner()
        }
    }

    private fun loadPremiumBanner() {
        Coroutines.main {
            //CommonMethod.getBanner("ProPremium", binding.ivPremBanner,myApi, applicationContext)
            getBanner("ProPremium", binding.ivPremBanner,myApi, applicationContext)
        }
    }

    suspend fun  getBanner(bannerName:String, imageView: ImageView, myApi: MyApi, context: Context) {
        try {
            val res = myApi.getBanner(bannerName)

            if(res.isSuccessful && res.body()!=null){
                val banner = res.body()
                if(!banner!!.error){
                    if (banner.imageUrl != null) {
                        imageView.visibility = View.VISIBLE

                        Glide.with(context)
                            .load(banner.imageUrl)
                            .thumbnail(Glide.with(context).load(R.drawable.placeholder))
                            .into(imageView)
                        imageView.setOnClickListener(View.OnClickListener {
                            if (banner.actionType == 1) {
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
                                        context.startActivity(intent)
                                    } else if (linkHost == "www.youtube.com") {
                                        try {
                                            val intent = Intent(Intent.ACTION_VIEW, uri)
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            intent.setPackage("com.google.android.youtube")
                                            context.startActivity(intent)
                                        }catch (e : Exception){
                                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            context.startActivity(intent)
                                        }
                                    }else if(url.endsWith(".mp4") || url.endsWith(".mpeg") || url.endsWith(".mpd") ||
                                            url.startsWith("https://lmpclass.sikderithub.com/embed") || url.startsWith("http://lmpclass.sikderithub.com/embed")){
                                        val intent = Intent(this, PlayerActivity::class.java)
                                        intent.putExtra("url", url)
                                        startActivity(intent)
                                    }
                                    else if ( (url.startsWith("http://") || url.startsWith(
                                            "https://"
                                        ))
                                    ) {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        context.startActivity(intent)
                                    }
                                }
                            } else if (banner.actionType === 2) {
                                //open activity
                            }
                        })

                    }
                }else{
                    Log.d("Banner", banner.msg!!)
                }
            }
        }catch (e : Exception){
            Toast.makeText(context, "Something went wrong, related to your network issue.", Toast.LENGTH_LONG).show()
        }
    }


    private fun getContctInformation(isPremium : Boolean) {
        Coroutines.main {
            try {
                loadingDialog.show()
                val response=viewModel.getPaidForContact("4",
                    SharedPreUtils.getStringFromStorageWithoutSuspend(this,Constants.userIdKey,Constants.defaultUserId).toString())
                if (response.isSuccessful && response.code()==200) {
                    loadingDialog.hide()
                    if (response.body()!=null) {
                        binding.spinKit.visibility= View.GONE
                        binding.standerdLayout.visibility = View.VISIBLE
                        if(isPremium){
                            binding.content.visibility = View.GONE
                            binding.tvInstruction.visibility = View.GONE
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
                                    Toast.makeText(
                                        this,
                                        "WhatsApp not Installed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                        }else{
                            binding.whatsAppBtn.visibility = View.GONE
                            val rippleBackground = findViewById<View>(R.id.content) as RippleBackground
                            rippleBackground.startRippleAnimation()
                            loadingDialog.show()

                            Glide.with(this)
                                .load(response.body()?.video_thumbail)

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
                            CommonMethod.setShakeAnimation(binding.ytthumbail, this)
                            binding.ytthumbail.setOnClickListener {
                                val webIntent: Intent= Intent(Intent.ACTION_VIEW,Uri.parse(response.body()?.video_link))
                                startActivity(Intent.createChooser(webIntent,"Choose one:"))
                            }

                            val whatsAppRes = myApi.getWhatsapp("Pro");
                            if(whatsAppRes.isSuccessful && whatsAppRes.body()!=null){
                                if(!whatsAppRes.body()!!.error!!) {
                                    if (whatsAppRes.body()!!.number != null) {
                                        binding.whatsAppBtn.visibility = View.VISIBLE
                                        binding.whatsAppBtn.setOnClickListener {
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

                            getAudioFile()

                        }

                        binding.pnOne.text = response.body()?.phone_one
                        binding.pnTwo.text = response.body()?.phone_two
                        binding.pnThree.text = response.body()?.phone_three


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

                    }
                } else {
                    loadingDialog.hide()
                    binding.spinKit.visibility= View.GONE
                }
            } catch (e: Exception) {
                loadingDialog.hide()
                binding.spinKit.visibility= View.GONE
            }
        }
    }

    private fun getAudioFile() {
        loadingDialog.show()
        try {
            myApi.getAudio("AudioFilePro")
                .enqueue(object : Callback<AudioResponse>{
                    override fun onResponse(call: Call<AudioResponse>,response: Response<AudioResponse>){
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
                           }else{
                               loadingDialog.hide()
                           }
                       }else{
                           loadingDialog.hide()
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


    private fun getDataLoad(){
        Coroutines.main {
            try {
                val response=viewModel.getSpecialNumber(SharedPreUtils.getStringFromStorageWithoutSuspend(this,Constants.userIdKey,Constants.defaultUserId).toString())
                if (response.isSuccessful && response.code()==200) {
                    if (response.body()!=null) {
                        binding.spinKit.visibility= View.GONE
                        binding.specialNumberLayout.visibility = View.VISIBLE

                        binding.numberOne.text = response.body()?.number_one
                        binding.numberTwo.text = response.body()?.number_two
                        binding.numberThree.text = response.body()?.number_three
                        binding.numberFour.text = response.body()?.number_four
                        binding.numberFive.text = response.body()?.number_five

                        val num6= response.body()?.number_six!!.trim()
                        val num7 = response.body()?.number_seven!!.trim()
                        val num8 = response.body()?.number_eight!!.trim()
                        val num9 = response.body()?.number_nine!!.trim()
                        val num10 = response.body()?.number_ten!!.trim()

                        if(!num6.isNullOrEmpty()){
                            binding.numberSix.text = num6
                        }else{
                            binding.numberSix.visibility = View.GONE
                        }
                        if(!num7.isNullOrEmpty()){
                            binding.numberSeven.text = response.body()?.number_seven

                        }else{
                            binding.numberSeven.visibility = View.GONE

                        }
                        if(!num8.isNullOrEmpty()){
                            binding.numberEight.text = response.body()?.number_eight

                        }else{
                            binding.numberEight.visibility = View.GONE

                        }
                        if(!num9.isNullOrEmpty()){
                            binding.numberNine.text = response.body()?.number_nine
                        }else{
                            binding.numberNine.visibility = View.GONE

                        }
                        if(!num10.isNullOrEmpty()){
                            binding.numberTen.text = response.body()?.number_ten
                        }else{
                            binding.numberTen.visibility = View.GONE

                        }


                        binding.uploadDate.text = response.body()?.upload_date


                    }
                } else {
                    binding.spinKit.visibility= View.GONE
                }
            } catch (e: Exception) {
                binding.spinKit.visibility= View.GONE
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
                CommonMethod.openConsoleLink(this, Constants.consoleId)
            }
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        if(isPause && mediaPlayer!=null){
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