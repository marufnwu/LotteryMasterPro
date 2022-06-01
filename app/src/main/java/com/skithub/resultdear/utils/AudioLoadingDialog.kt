package com.skithub.resultdear.utils

import android.app.Activity
import android.app.Dialog
import android.util.Log
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.skithub.resultdear.R

class AudioLoadingDialog(var activity: Activity, cancelable:Boolean = false) {
    var dialog: Dialog? = null
    private var loadImageGif: Int = R.drawable.sound_effect
    private var cancelable = false

    init {
        this.cancelable = cancelable
        createDialog()

    }


    fun setCancelable(state: Boolean) {
        cancelable = state
    }

    private fun setGif(gif: Int){
        loadImageGif = gif

    }


    private fun createDialog(){
        if (loadImageGif != 0) {
            dialog = Dialog(activity)
            dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            //inflate the layout
            dialog!!.setContentView(R.layout.custom_loading_dialog_layout)
            //setup cancelable, default=false
            dialog!!.setCancelable(cancelable)
            //get imageview to use in Glide
            val imageView = dialog!!.findViewById<ImageView>(R.id.custom_loading_imageView)

            //load gif and callback to imageview
            Glide.with(activity)
                .load(loadImageGif)
                .placeholder(loadImageGif)
                .centerCrop()
                .into(imageView)
        } else {
            Log.e(
                "LoadingDialog",
                "Erro, missing drawable of imageloading (gif), please, use setLoadImage(R.drawable.name)."
            )
        }
    }

    fun show() {
        if (dialog != null && !dialog!!.isShowing && !activity.isDestroyed) {
            dialog!!.show()
        }
    }

    fun hide() {
        if (dialog != null && dialog!!.isShowing && !activity.isDestroyed) {
           if(!activity.isFinishing){
               dialog!!.dismiss()
           }
        }
    }

    val isLoading: Boolean
        get() = dialog!!.isShowing


}