package com.skithub.resultdear.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skithub.resultdear.databinding.TutorialModelBinding
import com.skithub.resultdear.model.FacebookVideoModel
import com.skithub.resultdear.model.LmpVideo
import com.skithub.resultdear.model.VideoTutorModel
import com.skithub.resultdear.ui.webview.WebViewActivity
import java.util.*

class LmpClassVideoAdapter(val context: Context, val videoList : MutableList<Any>) : RecyclerView.Adapter<LmpClassVideoAdapter.ViewHolder>() {


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
        fun bind(obj : Any){

            if(obj is LmpVideo){
                val value = LmpVideo::class.java.cast(obj)
                binding.videoTitle.text = value.title
                Glide.with(context)
                    .load(value.thumbnail)
                    .into(binding.Thumbail)

                binding.root.setOnClickListener {
                    context.startActivity(Intent(context, WebViewActivity::class.java).putExtra("url", value.videoUrl))
                }
            }else if(obj is FacebookVideoModel){
                binding.layoutViewCount.visibility = View.VISIBLE
                val value = FacebookVideoModel::class.java.cast(obj)!!

                binding.txtViewCount.setText("Views: "+value.totalViews)

                binding.videoTitle.text = value.videoTitle
                Glide.with(context)
                    .load(value.thumbail)
                    .into(binding.Thumbail)

                binding.root.setOnClickListener {
                    context.startActivity(Intent(context, WebViewActivity::class.java)
                        .putExtra("videoId", value.id!!)
                        .putExtra("url", "https://lmpclass.sikderithub.com/watch.php?link="+value.videoLink))
                }
            }


        }
    }
}