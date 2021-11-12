package com.skithub.resultdear.adapter

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skithub.resultdear.R
import com.skithub.resultdear.databinding.LayoutBannerAdBinding
import com.skithub.resultdear.databinding.LotteryResultChildRecyclerViewModelLayoutBinding
import com.skithub.resultdear.databinding.LotteryResultRecyclerViewModelLayoutBinding
import com.skithub.resultdear.model.LotteryNumberModel
import com.skithub.resultdear.model.response.BannerResponse
import com.skithub.resultdear.utils.CommonMethod
import com.skithub.resultdear.utils.Constants
import com.skithub.resultdear.utils.MyExtensions.shortToast

class FifthLotteryResultChildRecyclerAdapter(val context: Context, val list: MutableList<LotteryNumberModel>, val bannerAd:BannerResponse, val columnCount: Int): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val clipBoardManager: ClipboardManager=context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val BANNER_VIEW_POSITION: Int = 20
    val LOTTERY_VIEW: Int = 1

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LotteryResultChildRecyclerViewHolder {
//        if(viewType==BANNER_VIEW_POSITION){
//            val  bannerBinding = LayoutBannerAdBinding.inflate(LayoutInflater.from(context), parent, false)
//            return BannerViewHolder(bannerBinding)
//        }
//        val binding= LotteryResultChildRecyclerViewModelLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
//        return LotteryResultChildRecyclerViewHolder(binding)
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if(viewType==BANNER_VIEW_POSITION){
            val  bannerBinding = LayoutBannerAdBinding.inflate(LayoutInflater.from(context), parent, false)
            return BannerViewHolder(bannerBinding)
        }else{
            val binding= LotteryResultChildRecyclerViewModelLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return LotteryResultChildRecyclerViewHolder(binding)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val view = getItemViewType(position)
        if(view==BANNER_VIEW_POSITION){
           ( holder as BannerViewHolder).bind(bannerAd)
        }else{
            (holder as LotteryResultChildRecyclerViewHolder).bind(list[position])
        }

       //(holder as LotteryResultChildRecyclerViewHolder).bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(position==BANNER_VIEW_POSITION){
            BANNER_VIEW_POSITION
        }else{
            LOTTERY_VIEW
        }
    }

    inner class LotteryResultChildRecyclerViewHolder(val binding: LotteryResultChildRecyclerViewModelLayoutBinding): RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {

        fun bind(item: LotteryNumberModel) {
            try {
                binding.lotteryNumberTextView.text="${item.lotteryNumber}"
                binding.lotteryNumberRecyclerRootLayout.setOnClickListener(this)

                val layoutParams=binding.lotteryNumberRecyclerRootLayout.layoutParams
                val oneItemWidth: Int=(CommonMethod.getScreenWidth(context as Activity)/columnCount)
                layoutParams.width=(oneItemWidth-(oneItemWidth/(columnCount*2)))
                binding.lotteryNumberRecyclerRootLayout.layoutParams=layoutParams
            } catch (e: Exception) {

            }
        }

        override fun onClick(v: View?) {
            v?.let {
                when (it.id) {
                    R.id.lotteryNumberRecyclerRootLayout -> {
                        val number: String?=list[adapterPosition].lotteryNumber
                        val clipData=ClipData.newPlainText(Constants.TAG,"$number")
                        clipBoardManager.setPrimaryClip(clipData)
                        context.shortToast("$number ${context.resources.getString(R.string.copied_successfully)}")
                    }
                }
            }
        }
    }


    inner class BannerViewHolder(val binding: LayoutBannerAdBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(banner : BannerResponse){


//            Glide.with(context)
//                .load(banner.imageUrl)
//                .into(binding.ivAd)
        }
    }


}