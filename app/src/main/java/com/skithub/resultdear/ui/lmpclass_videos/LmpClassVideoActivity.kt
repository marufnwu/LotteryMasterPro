package com.skithub.resultdear.ui.lmpclass_videos

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.skithub.resultdear.adapter.LmpClassVideoAdapter
import com.skithub.resultdear.databinding.ActivityLmpClassVideoBinding
import com.skithub.resultdear.model.LmpVideo
import com.skithub.resultdear.model.response.lmpVideoResponse
import com.skithub.resultdear.ui.MyApplication
import com.skithub.resultdear.utils.AudioStatus
import com.skithub.resultdear.utils.Coroutines
import com.skithub.resultdear.utils.LoadingDialog
import com.skithub.resultdear.utils.MyExtensions.shortToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class LmpClassVideoActivity : AppCompatActivity() {
    lateinit var lmpClassVideoAdapter: LmpClassVideoAdapter
    lateinit var layoutManager: LinearLayoutManager
    private var lmpVideo : MutableList<Any> = mutableListOf<Any>()
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
    lateinit var binding : ActivityLmpClassVideoBinding
    var videoCallingType: VideoCallingType? = null

    enum class VideoCallingType{
        FACEBOOK_VIDEO,
        LMPCLASS_VIDEO
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLmpClassVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()


        setRecyclerViewAdapter()

        videoCallingType = VideoCallingType.FACEBOOK_VIDEO

        getYtVideos(PAGE)
    }

    fun choseCallingFunction(page: Int){
        if(videoCallingType == VideoCallingType.FACEBOOK_VIDEO){
            getYtVideos(page)
        }else if(videoCallingType == VideoCallingType.LMPCLASS_VIDEO){
            getVideos(page)
        }
    }

    private fun initViews() {
        loadingDialog = LoadingDialog(this)
        layoutManager = LinearLayoutManager(this)
        binding.recyVideo.layoutManager = layoutManager
        binding.recyVideo.setHasFixedSize(true)

        initRecyScrollListener()
    }

    private fun setRecyclerViewAdapter() {
        lmpClassVideoAdapter = LmpClassVideoAdapter(this, lmpVideo)
        binding.recyVideo.adapter = lmpClassVideoAdapter
    }


    private fun initRecyScrollListener() {
        binding.recyVideo.addOnScrollListener(
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
                                        choseCallingFunction(page = PAGE)
                                    } else {
                                        isLoading = false
                                        //postListProgress.setVisibility(View.GONE)
                                        if(videoCallingType == VideoCallingType.FACEBOOK_VIDEO){
                                            videoCallingType = VideoCallingType.LMPCLASS_VIDEO
                                            PAGE = 1
                                            TOTAL_PAGE = 0
                                            choseCallingFunction(PAGE)
                                        }
                                    }
                                }
                            } else {
                                if(videoCallingType == VideoCallingType.FACEBOOK_VIDEO){
                                    videoCallingType = VideoCallingType.LMPCLASS_VIDEO
                                    PAGE = 1
                                    TOTAL_PAGE = 0
                                    choseCallingFunction(PAGE)
                                }else{
                                    Log.d("Pagination", "End of page")
                                }
                                //postListProgress.setVisibility(View.GONE);

                            }
                        } else {
                            Log.d("Pagination", "Loading")
                        }
                    }
                }
            })
    }

    private fun getVideos(page: Int) {
        if(page==1){
            //loadingDialog.show()
        }
        isLoading = true
        (application as MyApplication).myApi
            .getLmpClassVideo(page)
            .enqueue( object : Callback<lmpVideoResponse> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(call: Call<lmpVideoResponse>, response: Response<lmpVideoResponse>) {
                    isLoading = false
                    if(response.isSuccessful && response.body()!=null){
                        loadingDialog.hide()
                        val lmpVideoResponse = response.body()!!
                        if(!lmpVideoResponse.error){
                            TOTAL_PAGE = lmpVideoResponse.totalPages
                            lmpVideoResponse.lmpVideos?.let {
                                Log.d("Dataaa", Gson().toJson(it))


                                lmpVideo.addAll(it)
                                lmpClassVideoAdapter.notifyDataSetChanged()
                                Log.d("scrollstate", binding.recyVideo.scrollState.toString())

                            }
                        }
                    }
                }

                override fun onFailure(call: Call<lmpVideoResponse>, t: Throwable) {
                    loadingDialog.hide()
                    isLoading = false
                }

            }
            )
    }

    private fun getYtVideos(page :Int) {
        if(page==1){
            loadingDialog.show()
        }
        isLoading = true

        Coroutines.main {
            try {
                //val response=viewModel.getVideo("")
                val response=(application as MyApplication).myApi.getFacebookVideoListWithCountPaging(page)
                if (response.isSuccessful && response.code() == 200) {
                    if (response.body()!=null) {
                        loadingDialog.hide()
                        isLoading = false

                        if (response.body()?.status.equals("success",true)) {
                            val list = response.body()?.data!!
                            lmpVideo.addAll(list)
                            lmpClassVideoAdapter.notifyDataSetChanged()
                            TOTAL_PAGE = response.body()?.totalPage!!
                            if(list.size<10){

                                loadingDialog.hide()
                                isLoading = false

                                videoCallingType = VideoCallingType.LMPCLASS_VIDEO
                                PAGE = 1
                                TOTAL_PAGE = 0
                                choseCallingFunction(PAGE)
                            }

                        } else {
                            loadingDialog.hide()
                            isLoading = false

                            videoCallingType = VideoCallingType.LMPCLASS_VIDEO
                            PAGE = 1
                            TOTAL_PAGE = 0
                            choseCallingFunction(PAGE)

                        }
                    }else{
                        loadingDialog.hide()
                        isLoading = false
                    }
                } else {
                    loadingDialog.hide()
                    isLoading = false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                loadingDialog.hide()
                isLoading = false
            }
        }
    }
}