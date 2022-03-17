package com.skithub.resultdear.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skithub.resultdear.databinding.TutorialModelBinding
import com.skithub.resultdear.model.LmpVideo
import com.skithub.resultdear.ui.webview.WebViewActivity

class LmpClassVideoAdapter(val context: Context, val videoList : MutableList<LmpVideo>) : RecyclerView.Adapter<LmpClassVideoAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LmpClassVideoAdapter.ViewHolder {
        return ViewHolder(TutorialModelBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: LmpClassVideoAdapter.ViewHolder, position: Int) {
        holder.bind(videoList[position])
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    inner class ViewHolder(val binding: TutorialModelBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(video : LmpVideo){
            binding.videoTitle.text = video.title
            Glide.with(context)
                .load(video.thumbnail)
                .into(binding.Thumbail)

            binding.root.setOnClickListener {
                context.startActivity(Intent(context, WebViewActivity::class.java).putExtra("url", video.videoUrl))
            }
        }
    }
}