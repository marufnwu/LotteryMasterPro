package com.skithub.resultdear.ui

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.View
import android.view.WindowManager
import android.webkit.WebView
import android.widget.Toast
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionParameters
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util
import com.skithub.resultdear.R
import com.skithub.resultdear.databinding.ActivityPlayerBinding
import com.skithub.resultdear.databinding.CustomPlayerViewBinding
import com.skithub.resultdear.utils.CommonMethod
import com.skithub.resultdear.utils.MyExtensions.shortToast


class PlayerActivity : AppCompatActivity() {
    private val KEY_TRACK_SELECTION_PARAMETERS: String = "trackSelectorParameter"
    lateinit var  trackSelectionParameters: DefaultTrackSelector.Parameters
    lateinit var trackSelector: DefaultTrackSelector
    val KEY_PLAYER = "exoplayer"
    val KEY_PLAY_WHEN_READY = "playWhenReady"
    val KEY_CURRENT_WINDOW = "currentWindow"
    val KEY_PLAYBACK_POSITION = "playbackPosition"
    private var isShowingTrackSelectionDialog = false

    private val viewBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityPlayerBinding.inflate(layoutInflater)
    }

    private var player: SimpleExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CommonMethod.keepScreenOn(this)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE)
        setContentView(viewBinding.root)
        Log.d("Rotate=>", "onCreate")

        if(savedInstanceState!=null){

            // Restore as DefaultTrackSelector.Parameters in case ExoPlayer specific parameters were set.
            trackSelectionParameters = DefaultTrackSelector.Parameters.CREATOR.fromBundle(
                savedInstanceState.getBundle(KEY_TRACK_SELECTION_PARAMETERS)!!
            )

            playWhenReady = savedInstanceState.getBoolean(KEY_PLAY_WHEN_READY, playWhenReady)
            currentWindow = savedInstanceState.getInt(KEY_CURRENT_WINDOW)
            playbackPosition = savedInstanceState.getLong(KEY_PLAYBACK_POSITION, playbackPosition)
        }else{
            trackSelectionParameters =
                DefaultTrackSelector.ParametersBuilder( this).build()
        }

        CustomPlayerViewBinding.inflate(layoutInflater).exoFullscreen.setOnClickListener {
            Toast.makeText(this, "okkk", Toast.LENGTH_SHORT).show()
        }

        CustomPlayerViewBinding.inflate(layoutInflater).exoFullscreen.setOnClickListener {
            Toast.makeText(this, "okkk", Toast.LENGTH_SHORT).show()
        }

        viewBinding.selectTracksButton.setOnClickListener {
            selectTrack()
        }
        viewBinding.videoView.setControllerVisibilityListener {
                visibility-> viewBinding.controlsRoot.visibility = visibility
        }

        viewBinding.back.setOnClickListener {
            finish()
        }

        viewBinding.rotate.setOnClickListener {

            val orientation: Int = getResources().getConfiguration().orientation
            if(orientation== Configuration.ORIENTATION_PORTRAIT){
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE)
            }else if(orientation== Configuration.ORIENTATION_LANDSCAPE){
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
            }
        }


    }

    private fun selectTrack() {
        if (!isShowingTrackSelectionDialog && TrackSelectionDialog.willHaveContent(
                trackSelector
            )
        ) {
            isShowingTrackSelectionDialog = true
            val trackSelectionDialog = TrackSelectionDialog.createForTrackSelector(
                trackSelector
            )  /* onDismissListener= */
            { dismissedDialog -> isShowingTrackSelectionDialog = false }
            trackSelectionDialog.show(supportFragmentManager,  /* tag= */null)
        }
    }

    // User controls
    private fun updateButtonVisibility() {
        viewBinding.selectTracksButton.setEnabled(
            player != null && TrackSelectionDialog.willHaveContent(trackSelector)
        )
    }

    private fun updateTrackSelectorParameters() {
        if (player != null) {
            // Until the demo app is fully migrated to TrackSelectionParameters, rely on ExoPlayer to use
            // DefaultTrackSelector by default.
            trackSelectionParameters =
                player!!.trackSelectionParameters as DefaultTrackSelector.Parameters
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        updateTrackSelectorParameters()
        outState.putBundle(KEY_TRACK_SELECTION_PARAMETERS,
            trackSelectionParameters.toBundle()
        )

        outState.putBoolean(KEY_PLAY_WHEN_READY, playWhenReady)
        outState.putInt(KEY_CURRENT_WINDOW, currentWindow)
        outState.putLong(KEY_PLAYBACK_POSITION, playbackPosition)
    }

    private fun initializePlayer() {
         trackSelector = DefaultTrackSelector(this).apply {
            setParameters(buildUponParameters().setMaxVideoSizeSd())
        }

        //val url:String? = "http://192.168.0.103/lmp/convert/dash/1641478345228/file.mpd"
        val url:String? = intent.getStringExtra("url")
        if(url!=null){
            player = SimpleExoPlayer.Builder(this)
                .setTrackSelector(trackSelector)
                .build()
                .also { exoPlayer ->
                    viewBinding.videoView.player = exoPlayer

                    // mediaItem = MediaItem.fromUri("http://192.168.0.103/playtube/embed/TXmqng1O68ipWmJ")

                    // With this
                    val mediaItem = MediaItem.Builder()
                        .setUri(url)
                        .setMimeType(MimeTypes.APPLICATION_MPD)
                        .build()
                    exoPlayer.setMediaItem(mediaItem)
                    exoPlayer.trackSelectionParameters = trackSelectionParameters

                    exoPlayer.addListener(PlayerEventListener())

                    exoPlayer.playWhenReady = playWhenReady
                    exoPlayer.seekTo(currentWindow, playbackPosition)
                    exoPlayer.prepare()

                    updateButtonVisibility()

                }


        }else{
            shortToast("Video link error")
        }


    }



    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.d("Config", newConfig.orientation.toString())

        if(newConfig.orientation == 1){
            //portrait
            viewBinding.videoView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
        }else if(newConfig.orientation == 2){
            //landscape
            viewBinding.videoView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
        }
    }

    private fun releasePlayer() {
        player?.run {
            updateTrackSelectorParameters()
            playbackPosition = this.currentPosition
            currentWindow = this.currentWindowIndex
            playWhenReady = this.playWhenReady
            release()
        }

        player = null
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        viewBinding.videoView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    public override fun onStart() {
        super.onStart()
        Log.d("Rotate=>", "onStart")

        if (Util.SDK_INT >= 24) {
            initializePlayer()
        }
    }

    public override fun onResume() {
        super.onResume()
        Log.d("Rotate=>", "onResume")

        hideSystemUi()
        if ((Util.SDK_INT < 24 || player == null)) {
            initializePlayer()
        }
    }

    public override fun onPause() {
        super.onPause()
        Log.d("Rotate=>", "onPause")

        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }


    public override fun onStop() {
        super.onStop()
        Log.d("Rotate=>", "onStop")
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }

    inner class PlayerEventListener : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: @Player.State Int) {
            if (playbackState == Player.STATE_ENDED) {
                //showControls()
            }
            updateButtonVisibility()
        }

        override fun onPlayerError(error: PlaybackException) {
            if (error.errorCode == PlaybackException.ERROR_CODE_BEHIND_LIVE_WINDOW) {
                player!!.seekToDefaultPosition()
                player!!.prepare()
            } else {
                updateButtonVisibility()
            }
        }

        override fun onTracksInfoChanged(tracksInfo: TracksInfo) {
            updateButtonVisibility()

        }
    }


}