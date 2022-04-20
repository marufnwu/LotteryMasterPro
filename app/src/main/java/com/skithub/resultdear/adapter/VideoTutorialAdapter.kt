package com.skithub.resultdear.adapter

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skithub.resultdear.R
import com.skithub.resultdear.databinding.TutorialModelBinding
import com.skithub.resultdear.model.LotteryNumberModel
import com.skithub.resultdear.model.LotteryPdfModel
import com.skithub.resultdear.model.Video
import com.skithub.resultdear.model.VideoTutorModel
import com.skithub.resultdear.ui.lottery_result_info.LotteryResultInfoActivity
import com.skithub.resultdear.utils.CommonMethod
import com.skithub.resultdear.utils.Constants
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.ContextCompat.startActivity

import android.content.pm.ResolveInfo
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.skithub.resultdear.ui.webview.WebViewActivity


class VideoTutorialAdapter(val context: Context, val list: MutableList<VideoTutorModel>): RecyclerView.Adapter<VideoTutorialAdapter.OldResultRecyclerViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OldResultRecyclerViewHolder {
        val binding=TutorialModelBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return OldResultRecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OldResultRecyclerViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }


    inner class OldResultRecyclerViewHolder(val binding: TutorialModelBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(item: VideoTutorModel) {
            Log.d("bbbbbbb", item.video_title)
            Glide.with(context).load(item.thumbail).placeholder(R.drawable.loading_placeholder).fitCenter().into(binding.Thumbail)
            binding.videoTitle.text = item.video_title
            binding.videoRootlayout.setOnClickListener {
//                val webIntent: Intent= Intent(Intent.ACTION_VIEW, Uri.parse(item.video_link))
//                context.startActivity(Intent.createChooser(webIntent,"Choose one:"))

                context.startActivity(Intent(context, WebViewActivity::class.java).putExtra("url", "https://lmpclass.sikderithub.com/watch.php?link="+item.video_link))
        }


    }

    }}
