package com.skithub.resultdear.ui.special_number

import android.content.Intent
import android.graphics.drawable.Drawable
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.MediaTimestamp
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.JsonElement
import com.skithub.resultdear.R
import com.skithub.resultdear.database.network.ApiInterface
import com.skithub.resultdear.database.network.MyApi
import com.skithub.resultdear.database.network.RetrofitClient
import com.skithub.resultdear.databinding.ActivityMiddleNumberBinding
import com.skithub.resultdear.databinding.ActivitySpecialNumberBinding
import com.skithub.resultdear.model.response.AudioResponse
import com.skithub.resultdear.ui.MyApplication
import com.skithub.resultdear.ui.middle_number.MiddleNumberViewModel
import com.skithub.resultdear.ui.middle_number.MiddleNumberViewModelFactory
import com.skithub.resultdear.utils.*
import com.skyfishjy.library.RippleBackground
import org.json.JSONArray
import org.json.JSONException
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
            true
        }

        mediaPlayer!!.setOnCompletionListener {

            audioLoadingDialog.hide()
        }


        if (license_check.equals("0")){
            getPremiumStatus(false)
        }else if (license_check.equals("2")){
            binding.coomingSoon.visibility = View.VISIBLE
            binding.standerdLayout.visibility = View.GONE
        }else{
            getDataLoad()
            getPremiumStatus(true)
            loadPremiumBanner()
        }
    }

    private fun loadPremiumBanner() {
        Coroutines.main {
            CommonMethod.getBanner("ProPremium", binding.ivPremBanner,myApi, applicationContext)
        }
    }

    private fun getPremiumStatus(isPremium : Boolean) {
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
                        }else{
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
                                Toast.makeText(this@SpecialNumberActivity, "WhatsApp not Installed", Toast.LENGTH_SHORT).show()
                            }

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

                        binding.numberSix.text = response.body()?.number_six
                        binding.numberSeven.text = response.body()?.number_seven
                        binding.numberEight.text = response.body()?.number_eight
                        binding.numberNine.text = response.body()?.number_nine
                        binding.numberTen.text = response.body()?.number_ten

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