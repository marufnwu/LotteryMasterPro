package com.skithub.resultdear.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skithub.resultdear.R
import com.skithub.resultdear.databinding.TutorialModelBinding
import com.skithub.resultdear.model.VideoTutorModel

import com.skithub.resultdear.databinding.LayoutAudioItemBinding
import com.skithub.resultdear.model.response.AudioTutorial
import com.skithub.resultdear.ui.audio_tutorial.AudioTutorialActivity
import com.skithub.resultdear.ui.main.MainActivity
import com.skithub.resultdear.utils.AudioStatus
import com.skithub.resultdear.utils.MediaPlayerUtils
import me.jagar.chatvoiceplayerlibrary.VoicePlayerView
import pl.droidsonroids.gif.GifDrawable
import java.io.IOException


class AudioTutorialAdapter(val context: Context, val list: MutableList<AudioTutorial>): RecyclerView.Adapter<AudioTutorialAdapter.ViewHolder>() {

    private var audioTutorialActivity: AudioTutorialActivity = context as AudioTutorialActivity


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding=LayoutAudioItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(list[position])

    }

    override fun getItemCount(): Int {
        return list.size
    }


    inner class ViewHolder(val binding: LayoutAudioItemBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(item: AudioTutorial) {
            Glide.with(context).load(item.thumbnail).placeholder(R.drawable.loading_placeholder).fitCenter().into(binding.Thumbail)
            binding.videoTitle.text = item.tittle

            binding.audioLayout.bringToFront()

            if (audioTutorialActivity.audioStatusList.get(position).getAudioState() == AudioStatus.AUDIO_STATE.IDLE.ordinal
                || audioTutorialActivity.audioStatusList.get(position)
                    ?.getAudioState() == AudioStatus.AUDIO_STATE.PAUSED.ordinal
            ) {
                binding.btnPlay.background = AppCompatResources.getDrawable(context, R.drawable.play)
            } else {
                binding.btnPlay.background = AppCompatResources.getDrawable(context, R.drawable.pause)
            }
            binding.seekBarAudio.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    if (fromUser) MediaPlayerUtils.applySeekBarValue(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {

                }
                override fun onStopTrackingTouch(seekBar: SeekBar) {

                }
            })

            if (audioTutorialActivity.audioStatusList[position].audioState != AudioStatus.AUDIO_STATE.IDLE.ordinal) {
                binding.seekBarAudio.setMax(
                    audioTutorialActivity.audioStatusList[position].totalDuration
                )
                binding.seekBarAudio.setProgress(
                    audioTutorialActivity.audioStatusList[position].currentValue
                )
                binding.seekBarAudio.isEnabled = true
                val gifDrawable = binding.gifView.drawable as GifDrawable
                gifDrawable.start()
            } else {
                binding.seekBarAudio.progress = 0
                binding.seekBarAudio.isEnabled = false

                val gifDrawable = binding.gifView.drawable as GifDrawable
                gifDrawable.stop()
            }



            binding.btnPlay.setOnClickListener(View.OnClickListener {
                val position = adapterPosition

                // Check if any other audio is playing
                if (audioTutorialActivity.audioStatusList.get(position).getAudioState() == AudioStatus.AUDIO_STATE.IDLE.ordinal) {

                    // Reset media player
                    val listener: MediaPlayerUtils.Listener = context as MediaPlayerUtils.Listener
                    listener.onAudioComplete()
                }
                val audioPath: String = list[position].audio!!
                val audioStatus: AudioStatus = audioTutorialActivity.audioStatusList.get(position)
                val currentAudioState = audioStatus.audioState
                if (currentAudioState == AudioStatus.AUDIO_STATE.PLAYING.ordinal) {
                    // If mediaPlayer is playing, pause mediaPlayer
                    binding.btnPlay.background = AppCompatResources.getDrawable(context, R.drawable.play)
                    MediaPlayerUtils.pauseMediaPlayer()
                    audioStatus.audioState = AudioStatus.AUDIO_STATE.PAUSED.ordinal
                    audioTutorialActivity.audioStatusList.set(position, audioStatus)

                    val gifDrawable = binding.gifView.drawable as GifDrawable
                    gifDrawable.stop()

                } else if (currentAudioState == AudioStatus.AUDIO_STATE.PAUSED.ordinal) {
                    // If mediaPlayer is paused, play mediaPlayer
                    binding.btnPlay.background = AppCompatResources.getDrawable(context, R.drawable.pause)
                    MediaPlayerUtils.playMediaPlayer()
                    audioStatus.audioState = AudioStatus.AUDIO_STATE.PLAYING.ordinal
                    audioTutorialActivity.audioStatusList.set(position, audioStatus)

                    val gifDrawable = binding.gifView.drawable as GifDrawable
                    gifDrawable.start()

                } else {
                    // If mediaPlayer is in idle state, start and play mediaPlayer
                    binding.btnPlay.background = AppCompatResources.getDrawable(context, R.drawable.pause)
                    audioStatus.audioState = AudioStatus.AUDIO_STATE.PLAYING.ordinal
                    audioTutorialActivity.audioStatusList[position] = audioStatus
                    try {
                        MediaPlayerUtils.startAndPlayMediaPlayer(
                            audioPath,
                            context as MediaPlayerUtils.Listener
                        )
                        audioStatus.totalDuration = MediaPlayerUtils.getTotalDuration()
                        Log.d("TotalDuration", audioStatus.totalDuration.toString())
                        audioTutorialActivity.audioStatusList.set(position, audioStatus)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            })

    }

    }}
