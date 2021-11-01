package com.skithub.resultdear.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.skithub.resultdear.R
import com.skithub.resultdear.databinding.AdsImageViewLayoutBinding
import com.skithub.resultdear.databinding.LotteryResultRecyclerViewModelLayoutBinding
import com.skithub.resultdear.model.AdsImageModel
import com.skithub.resultdear.model.LotteryNumberModel
import com.skithub.resultdear.model.LotteryResultRecyclerModel
import com.skithub.resultdear.utils.CommonMethod
import com.skithub.resultdear.utils.Constants

class LotteryResultRecyclerAdapter(val context: Context, val list: MutableList<LotteryResultRecyclerModel>, val adsImageList: MutableList<AdsImageModel>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val lotteryViewType: Int=0
    private val imageViewType: Int=1
    private val adsImagePosition: Int=3
    private val lotteryNumberColumnCount: Int=5
    private val lotteryNumberVerticalSpanCount: Int=20



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType==lotteryViewType) {
            val binding=LotteryResultRecyclerViewModelLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return LotteryResultRecyclerViewHolder(binding)
        } else {
            val binding=AdsImageViewLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return LotteryResultRecyclerImageViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position<adsImagePosition) {
            (holder as LotteryResultRecyclerViewHolder).bind(list[position])
        } else if (position==adsImagePosition) {
            (holder as LotteryResultRecyclerImageViewHolder).bind()
        } else if (position>adsImagePosition){
            (holder as LotteryResultRecyclerViewHolder).bind(list[position-1])
        }
    }

    override fun getItemCount(): Int {
        if (list.size>0 && !adsImageList.isNullOrEmpty()) {
            return list.size+1
        }
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position==adsImagePosition) {
            imageViewType
        } else {
            lotteryViewType
        }
    }

    inner class LotteryResultRecyclerViewHolder(val binding: LotteryResultRecyclerViewModelLayoutBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LotteryResultRecyclerModel) {
            try {
                binding.resultTypeTextView.text="${item.winType} Prize \u20B9 ${getPrizeAmount(item.winType)}"
                var layoutManager: GridLayoutManager
                if (item.winType.equals(Constants.winTypeFifth)) {
                    layoutManager= GridLayoutManager(context,lotteryNumberVerticalSpanCount,GridLayoutManager.HORIZONTAL,false)
                } else {
                    layoutManager= GridLayoutManager(context,lotteryNumberColumnCount)
                }
                val childList: MutableList<LotteryNumberModel> =item.data!!
                childList.sortBy {
                    it.lotteryNumber
                }
                val adapter: LotteryResultChildRecyclerAdapter=LotteryResultChildRecyclerAdapter(context,childList,lotteryNumberColumnCount)
                binding.resultChildRecyclerView.layoutManager=layoutManager
                binding.resultChildRecyclerView.adapter=adapter
            } catch (e: Exception) {

            }
        }
    }

    inner class LotteryResultRecyclerImageViewHolder(val binding: AdsImageViewLayoutBinding): RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {

        fun bind() {
            try {
                if (adsImageList[0].activeStatus.isNullOrEmpty() || adsImageList[0].activeStatus.equals("false",true)) {
                    binding.lotteryImageView.visibility=View.GONE
                } else {
                    binding.lotteryImageView.visibility=View.VISIBLE
                    Glide
                        .with(context)
                        .load(adsImageList[0].imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .placeholder(R.drawable.loading_placeholder)
                        .into(binding.lotteryImageView)
                    binding.lotteryImageView.setOnClickListener(this)
                }
            } catch (e: Exception) {

            }
        }

        override fun onClick(v: View?) {
            v?.let {
                when (it.id) {
                    R.id.lotteryImageView -> {
                        val targetIntent=Intent(Intent.ACTION_VIEW, Uri.parse(adsImageList[0].targetUrl))
                        context.startActivity(Intent.createChooser(targetIntent,"Choose one:"))
                    }
                }
            }
        }
    }


    private fun getPrizeAmount(type: String?): String {
        if (type.isNullOrEmpty()) {
            return "Amount not detectable"
        } else {
            if (type.equals(Constants.winTypeFirst)) {
                return "1 Crore"
            } else if (type.equals(Constants.winTypeSecond)) {
                return "9,000/-"
            } else if (type.equals(Constants.winTypeThird)) {
                return "450/-"
            } else if (type.equals(Constants.winTypeFourth)) {
                return "250/-"
            } else if (type.equals(Constants.winTypeFifth)) {
                return "120/-"
            }
        }
        return "Amount not detectable"
    }


}