package com.skithub.resultdear.ui.got_a_prize

import android.content.Intent
import android.graphics.drawable.Drawable
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.skithub.resultdear.R
import com.skithub.resultdear.database.network.MyApi
import com.skithub.resultdear.databinding.ActivityGotPrizeBinding
import com.skithub.resultdear.model.response.AudioResponse
import com.skithub.resultdear.ui.MyApplication
import com.skithub.resultdear.utils.*
import com.skyfishjy.library.RippleBackground
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GotPrizeActivity : AppCompatActivity() {
    private var isPause: Boolean = false
    private  var mediaPlayer: MediaPlayer? = null
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var audioLoadingDialog: AudioLoadingDialog
    private lateinit var myApi: MyApi
    private lateinit var  binding : ActivityGotPrizeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGotPrizeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingDialog = LoadingDialog(this)
        audioLoadingDialog = AudioLoadingDialog(this)
        myApi = (application as MyApplication).myApi

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

        getAudioFile()
        loadBanner()
        getContactNum()
    }

    private fun loadBanner() {

        Coroutines.main {
            CommonMethod.getBanner("GotAPrice", binding.imageBanner,myApi, applicationContext)

        }

    }
    private fun getContactNum() {
        Coroutines.main {
            try {
                val response=myApi.getPaidContInfoList("1",
                    SharedPreUtils.getStringFromStorageWithoutSuspend(this,
                        Constants.userIdKey,
                        Constants.defaultUserId).toString())
                if (response.isSuccessful && response.code()==200) {
                    if (response.body()!=null) {

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
                                Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT).show()
                            }

                        }


                    }
                } else {

                }
            } catch (e: Exception) {
                loadingDialog.hide()
            }
        }

    }
    private fun getAudioFile() {
        loadingDialog.show()
        try {
            myApi.getAudio("AudioFileGotAPrice")
                .enqueue(object : Callback<AudioResponse> {
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