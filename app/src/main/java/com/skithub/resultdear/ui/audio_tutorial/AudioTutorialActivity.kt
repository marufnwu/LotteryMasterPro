package com.skithub.resultdear.ui.audio_tutorial

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.skithub.resultdear.R
import com.skithub.resultdear.adapter.AudioTutorialAdapter
import com.skithub.resultdear.databinding.ActivityAudioTutorialBinding
import com.skithub.resultdear.databinding.ActivityTutorialVideoBinding
import com.skithub.resultdear.model.response.AudioTutorial
import com.skithub.resultdear.model.response.AudioTutorialResponse
import com.skithub.resultdear.ui.MyApplication
import com.skithub.resultdear.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class AudioTutorialActivity : AppCompatActivity(), MediaPlayerUtils.Listener {
    var state: Parcelable?  = null
    lateinit var binding : ActivityAudioTutorialBinding
    lateinit var audioTutorialAdapter : AudioTutorialAdapter
    lateinit var layoutManager: LinearLayoutManager
    private var audioList : MutableList<AudioTutorial> = mutableListOf<AudioTutorial>()
    var audioStatusList: MutableList<AudioStatus> = ArrayList<AudioStatus>()
    var pastVisibleItem : Int =0
    var visibleItemCount = 0
    var totalItemCount=0
    var previousTotal = 0

    private var PAGE = 1
    private var PAGE_SIZE= 30
    private var TOTAL_PAGE = 0
    var isLoading = true
    lateinit var loadingDialog: LoadingDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioTutorialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingDialog = LoadingDialog(this)
        initViews()


        setRecyclerViewAdapter()




        Coroutines.main {
            getBanner()
        }
        getAudios(PAGE)
    }

    suspend fun getBanner() {
        val res =(application as MyApplication).myApi.getBanner("AudioTutorialActivity")
        if(res.isSuccessful && res.body()!=null){
            val banner = res.body()!!
            if(!banner.error){
                if (banner.imageUrl != null) {

                }
            }
        }
    }

    private fun getAudios(page: Int) {
        if(page==1){
            loadingDialog.show()
        }
        isLoading = true
        (application as MyApplication).myApi
            .getAudioTutorial(page)
            .enqueue( object : Callback<AudioTutorialResponse> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(call: Call<AudioTutorialResponse>, response: Response<AudioTutorialResponse>) {
                    isLoading = false
                    if(response.isSuccessful && response.body()!=null){
                        loadingDialog.hide()
                        val audioTutorialResponse = response.body()!!
                        if(!audioTutorialResponse.error!!){
                            TOTAL_PAGE = audioTutorialResponse.totalPages!!
                            audioTutorialResponse.audios?.let {
                                Log.d("Dataaa", Gson().toJson(it))

                                it.forEach { audioTutorial ->
                                    audioStatusList.add(AudioStatus(AudioStatus.AUDIO_STATE.IDLE.ordinal, 0))
                                }


                                audioList.addAll(it)
                                audioTutorialAdapter.notifyDataSetChanged()      }
                        }
                    }
                }

                override fun onFailure(call: Call<AudioTutorialResponse>, t: Throwable) {
                    loadingDialog.hide()
                    isLoading = false
                }

            }
            )
    }

    private fun initViews() {
        layoutManager = LinearLayoutManager(this)
        binding.recyAudio.layoutManager = layoutManager
        binding.recyAudio.setHasFixedSize(true)

        initRecyScrollListener()
    }


    private fun initRecyScrollListener() {
        binding.recyAudio.addOnScrollListener(
            object: RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    visibleItemCount = layoutManager.getChildCount()
                    totalItemCount = layoutManager.getItemCount()
                    pastVisibleItem = layoutManager.findFirstVisibleItemPosition()

                    if (dy > 0) {
                        Log.d("Pagination", "Scrolled")
                        if (!isLoading) {
                            if (PAGE <= TOTAL_PAGE) {
                                Log.d("Pagination", "Total Page $TOTAL_PAGE")
                                Log.d("Pagination", "Page $PAGE")
                                if (visibleItemCount + pastVisibleItem >= totalItemCount) {
                                    //postListProgress.setVisibility(View.VISIBLE)
                                    isLoading = true
                                    Log.v("...", "Last Item Wow !")
                                    //Do pagination.. i.e. fetch new data
                                    PAGE++
                                    if (PAGE <= TOTAL_PAGE) {
                                        getAudios(PAGE)
                                    } else {
                                        isLoading = false
                                        //postListProgress.setVisibility(View.GONE)
                                    }
                                }
                            } else {

                                //postListProgress.setVisibility(View.GONE);
                                Log.d("Pagination", "End of page")
                            }
                        } else {
                            Log.d("Pagination", "Loading")
                        }
                    }
                }
            })
    }
    override fun onPause() {
        super.onPause()
        // Store its state
        state = binding.recyAudio.layoutManager?.onSaveInstanceState()
    }

    override fun onResume() {
        super.onResume()
        // Main position of RecyclerView when loaded again
        if (state != null) {
            binding.recyAudio.layoutManager?.onRestoreInstanceState(state)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        MediaPlayerUtils.releaseMediaPlayer()
    }

    override fun onAudioComplete() {
        // Store its state

        // Store its state
        state = binding.recyAudio.layoutManager?.onSaveInstanceState()

        audioStatusList.clear()
        for (i in audioList.indices) {
            audioStatusList.add(AudioStatus(AudioStatus.AUDIO_STATE.IDLE.ordinal, 0))
        }
        setRecyclerViewAdapter()

        // Main position of RecyclerView when loaded again

        // Main position of RecyclerView when loaded again
        if (state != null) {
            binding.recyAudio.getLayoutManager()?.onRestoreInstanceState(state)
        }
    }

    private fun setRecyclerViewAdapter() {
        audioTutorialAdapter = AudioTutorialAdapter(this, audioList)
        binding.recyAudio.adapter = audioTutorialAdapter
    }

    override fun onAudioUpdate(currentPosition: Int) {

        var playingAudioPosition = -1
        for (i in audioStatusList.indices) {
            val audioStatus = audioStatusList[i]
            if (audioStatus.audioState == AudioStatus.AUDIO_STATE.PLAYING.ordinal) {
                playingAudioPosition = i
                break
            }
        }
        Log.d("playingAudioPosition", playingAudioPosition.toString())
        if (playingAudioPosition != -1) {
            Log.d("seekPostion", currentPosition.toString())

            val holder: AudioTutorialAdapter.ViewHolder =
                binding.recyAudio.findViewHolderForAdapterPosition(playingAudioPosition) as AudioTutorialAdapter.ViewHolder
            holder.binding.seekBarAudio.progress = currentPosition
            Log.d("seekPostion", holder.binding.seekBarAudio.max.toString())

        }
    }

}