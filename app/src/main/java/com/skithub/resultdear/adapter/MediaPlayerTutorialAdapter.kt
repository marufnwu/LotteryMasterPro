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
import androidx.core.content.ContextCompat


class MediaPlayerTutorialAdapter(val context: Context, val list: MutableList<Video>): RecyclerView.Adapter<MediaPlayerTutorialAdapter.OldResultRecyclerViewHolder>() {



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


    inner class OldResultRecyclerViewHolder(val binding: TutorialModelBinding): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        fun bind(item: Video) {
            Glide.with(context)
                .load(item.thumbnail)
                .placeholder(R.drawable.loading_placeholder)
                .fitCenter()
                .into(binding.Thumbail)
            binding.videoTitle.text = item.title
            binding.videoRootlayout.setOnClickListener {

//                try{
//                    val intent  = context.packageManager.getLaunchIntentForPackage("com.lmp.video")
//
//                    intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                    intent.putExtra("videoId", item.fileName)
//                    context.startActivity(intent)
//                }catch (e : Exception){
//                    e.printStackTrace()
//                }

//                val mainIntent = Intent(Intent.ACTION_MAIN, null)
//                mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
//                for (info in context.packageManager.queryIntentActivities(mainIntent, 0)) {
//                    if (info.loadLabel(context.packageManager) == "4k Video Player") {
//                        val launchIntent: Intent? =
//                            context.packageManager.getLaunchIntentForPackage(info.activityInfo.applicationInfo.packageName)
//                        launchIntent!!.putExtra("videoId", item.fileName)
//                        context.startActivity(launchIntent)
//                        return@setOnClickListener
//                    }
//                }


                try {
                    val i: Intent? =
                        context.packageManager.getLaunchIntentForPackage("com.lmp.video")
                    if(i!=null){
                        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        i.putExtra("videoId", item.fileName)
                        context.startActivity(i)
                    }else{
                        CommonMethod.openMediaPlayerApp(context)
                    }
                } catch (e: PackageManager.NameNotFoundException) {
                    CommonMethod.openMediaPlayerApp(context)
                }

            /*binding.dateTextView.text=item.winDate
            binding.timeTextView.text=item.winTime
            binding.dayNameTextView.text=item.winDayName*/
            //binding.oldResultRootLayout.setOnClickListener(this)
        }


    }

        override fun onClick(v: View?) {
            TODO("Not yet implemented")
        }
    }}
