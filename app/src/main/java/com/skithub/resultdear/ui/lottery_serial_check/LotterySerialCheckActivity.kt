package com.skithub.resultdear.ui.lottery_serial_check

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.skithub.resultdear.R
import com.skithub.resultdear.adapter.LotterySerialListAdapter
import com.skithub.resultdear.databinding.ActivityLotteryNumberCheckBinding
import com.skithub.resultdear.databinding.ActivityLotterySerialCheckBinding
import com.skithub.resultdear.model.LotteryNumberModel
import com.skithub.resultdear.model.LotterySerialModel
import com.skithub.resultdear.model.response.Banner
import com.skithub.resultdear.model.response.LotterySerialCheckInfoResponse
import com.skithub.resultdear.model.response.LotterySerialCheckResponse
import com.skithub.resultdear.ui.MyApplication
import com.skithub.resultdear.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class LotterySerialCheckActivity : AppCompatActivity() {
    lateinit var binding : ActivityLotterySerialCheckBinding
    lateinit var loadingDialog: LoadingDialog
    lateinit var adapter : LotterySerialListAdapter
    private  var mediaPlayer: MediaPlayer? = null
    private lateinit var audioLoadingDialog: AudioLoadingDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLotterySerialCheckBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingDialog = LoadingDialog(this)
        audioLoadingDialog = AudioLoadingDialog(this, false)

        binding.recyList.layoutManager = LinearLayoutManager(this)
        binding.recyList.setHasFixedSize(true)


        initAudioPlayer()



        getInfo()
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

    private fun getInfo() {
        loadingDialog.show()
        (application as MyApplication).myApi
            .lotterySerialCheckInfo(SharedPreUtils.getStringFromStorageWithoutSuspend(
                this, Constants.userIdKey, Constants.defaultUserId)!!)
            .enqueue(object: Callback<LotterySerialCheckInfoResponse> {
                override fun onResponse(
                    call: Call<LotterySerialCheckInfoResponse>,
                    response: Response<LotterySerialCheckInfoResponse>
                ) {
                    loadingDialog.hide()
                    if(response.isSuccessful && response.body()!=null){
                        if(!response.body()!!.error){
                            setData(response.body()!!)
                        }
                    }
                }

                override fun onFailure(call: Call<LotterySerialCheckInfoResponse>, t: Throwable) {
                    loadingDialog.hide()

                }

            })
    }

    private fun setData(res: LotterySerialCheckInfoResponse) {
        val banner: Banner? = res.banner
        banner?.let { ban ->

            if(!ban.error){
                binding.imgBanner.visibility = View.VISIBLE

                Glide.with(this)
                    .load(ban.imageUrl)
                    .placeholder(R.drawable.loading_placeholder)
                    .into(binding.imgBanner)

                binding.imgBanner.setOnClickListener {
                    CommonMethod.openLink(this, ban.actionUrl!!)
                }
            }


        }

        binding.btnSearch.setOnClickListener {
            if(res.isLicensed){
                search()
            }else{
                playAudio(res.audioUrl)
            }
        }


    }

    private fun playAudio(audioUrl: String?) {
        audioUrl?.let {
            try {
                mediaPlayer!!.reset()
                mediaPlayer!!.setDataSource(audioUrl)
                mediaPlayer!!.prepareAsync()
                loadingDialog.show()
            }catch (e:Exception){

            }
        }
    }

    private fun search() {

        val startNumber = binding.edtStartNum.text.toString()
        val endNumber = binding.edtEndNum.text.toString()

        if(startNumber.length<4){
            Toast.makeText(this, "Start Number can't empty", Toast.LENGTH_LONG).show()
            return
        }

        if(endNumber.length<4){
            Toast.makeText(this, "End Number can't empty", Toast.LENGTH_LONG).show()
            return
        }

        startNumber.toCharArray(startNumber.length-4)

        if(CommonMethod.getLotteryMiddle(endNumber) != CommonMethod.getLotteryMiddle(startNumber)){
            Toast.makeText(this, getString(R.string.middle_not_matched), Toast.LENGTH_LONG).show()
            return
        }

        if(CommonMethod.getLast2digit(endNumber).toInt()<CommonMethod.getLast2digit(startNumber).toInt()){
            Toast.makeText(this, "End number must not less than start number", Toast.LENGTH_LONG).show()
            return
        }

        loadingDialog.show()

        (application as MyApplication).myApi
            .get10DaysOldNumber(startNumber, endNumber)
            .enqueue(object: Callback<LotterySerialCheckResponse> {
                override fun onResponse(call: Call<LotterySerialCheckResponse>, response: Response<LotterySerialCheckResponse>) {
                    loadingDialog.hide()
                    if(response.isSuccessful && response.body()!=null){
                        if(!response.body()!!.error!!){
                            buildListWithColor(response.body()!!.number!!.toMutableList())
                        }
                    }
                }

                override fun onFailure(call: Call<LotterySerialCheckResponse>, t: Throwable) {
                    loadingDialog.hide()
                }

            })


    }


    private fun buildListWithColor(numbers : MutableList<LotterySerialModel>){
        val size = numbers.size

        val colorSize = (size*30)/100

        for(i in 0 until size-1){

            Log.d("Looop", "i=> "+i)

            if(i%3==0){
                Log.d("Looop", "i/3==0")

                val rnds = (1..2).random()
                var pos = i+rnds

                if(pos>-1&& pos<size){
                    Log.d("Looop", "i= $i, rds= $rnds, pos= $pos")

                    numbers[pos].select =true
                }
            }
        }

        adapter = LotterySerialListAdapter(this@LotterySerialCheckActivity, numbers)
        binding.recyList.adapter = adapter
    }
}