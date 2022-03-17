package com.skithub.resultdear.ui.buy_button_rule

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.skithub.resultdear.adapter.CustomerCareNumAdapter
import com.skithub.resultdear.databinding.ActivityButtonBuyRuleBinding
import com.skithub.resultdear.model.response.ButtonBuyRuleResponse
import com.skithub.resultdear.model.Contacts
import com.skithub.resultdear.ui.MyApplication
import com.skithub.resultdear.utils.AudioLoadingDialog
import com.skithub.resultdear.utils.CommonMethod
import com.skithub.resultdear.utils.LoadingDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class ButtonBuyRuleActivity : AppCompatActivity() {

    lateinit var binding:ActivityButtonBuyRuleBinding
    lateinit var loadingDialog: LoadingDialog

    private  var mediaPlayer: MediaPlayer? = null
    private lateinit var audioLoadingDialog: AudioLoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = ActivityButtonBuyRuleBinding.inflate(layoutInflater)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        setContentView(binding.root)

        loadingDialog = LoadingDialog(this)
        audioLoadingDialog = AudioLoadingDialog(this, false)


        initAudioPlayer()
        getInfo()
    }
    private fun playAudio(audioUrl: String?) {
        audioUrl?.let {
            try {
                mediaPlayer!!.reset()
                mediaPlayer!!.setDataSource(audioUrl)
                mediaPlayer!!.prepareAsync()
                loadingDialog.show()
            }catch (e: Exception){

            }
        }
    }
    private fun initAudioPlayer() {
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
            audioLoadingDialog.show()
        }

        mediaPlayer!!.setScreenOnWhilePlaying(true)
        mediaPlayer!!.setOnBufferingUpdateListener { mp, percent ->
            Log.d("Percentage", percent.toString())
            if(percent>99){
                loadingDialog.hide()
            }
        }

        mediaPlayer!!.setOnErrorListener { p0, p1, p2 ->
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
            audioLoadingDialog.hide()
            loadingDialog.hide()
            true
        }

        mediaPlayer!!.setOnCompletionListener {

            audioLoadingDialog.hide()
        }
    }


    fun getInfo(){
        loadingDialog.show()
        (application as MyApplication).myApi
            .getButtonBuyInfo()
            .enqueue(object : Callback<ButtonBuyRuleResponse> {
                override fun onResponse(
                    call: Call<ButtonBuyRuleResponse>, response: Response<ButtonBuyRuleResponse>) {
                    loadingDialog.hide()
                    if(response.isSuccessful){
                        response.body()?.let { buttonBuyRuleResponse ->
                            if(!buttonBuyRuleResponse.error){
                                buttonBuyRuleResponse.banner?.let { banner ->
                                    if(!banner.error){
                                        CommonMethod.loadBannerWithClick(this@ButtonBuyRuleActivity, banner, binding.imgBanner)
                                    }
                                }
                            }
                            
                            buttonBuyRuleResponse.audioResponse?.let {audioResponse ->  
                                if(!audioResponse.error){
                                    audioResponse.audio?.audioUrl?.let { url->
                                        playAudio(url)
                                    }
                                }
                            }

                            buttonBuyRuleResponse.contacts?.let { contacts ->
                                if(!contacts.error!!){
                                    loadRecycler(contacts)
                                }
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<ButtonBuyRuleResponse>, t: Throwable) {
                    loadingDialog.hide()
                }
            })
    }

    private fun loadRecycler(contacts: Contacts) {
        contacts.numberList?.let {numberList->
            binding.recyCustNumbers.setHasFixedSize(true)
            binding.recyCustNumbers.layoutManager = LinearLayoutManager(this)

            val adapter = CustomerCareNumAdapter(this, numberList.toMutableList())
            binding.recyCustNumbers.adapter = adapter


        }

        contacts.whatsapp?.let { num->
            binding.whatsAppBtn.visibility = View.VISIBLE
            CommonMethod.openWhatsapp(this, binding.whatsAppBtn, num)
        }
    }
}