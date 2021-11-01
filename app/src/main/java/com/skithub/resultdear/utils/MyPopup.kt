package com.skithub.resultdear.utils

import android.widget.PopupWindow

import android.R
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.*

import android.widget.ImageView
import com.bumptech.glide.Glide

class MyPopup(var activity: Activity) {
    var dialog: Dialog? = null
    private var loadImageGif: Int = com.skithub.resultdear.R.drawable.loading
    private var cancelable = false

    init {
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
            dialog!!.setContentView(com.skithub.resultdear.R.layout.custom_loading_dialog_layout)
            //setup cancelable, default=false
            dialog!!.setCancelable(cancelable)
            //get imageview to use in Glide
            val imageView = dialog!!.findViewById<ImageView>(com.skithub.resultdear.R.id.custom_loading_imageView)

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
        if (dialog != null && !dialog!!.isShowing) {
            dialog!!.show()
        }
    }

    fun hide() {
        if (dialog != null && dialog!!.isShowing) {
            if(!activity.isFinishing){
                dialog!!.dismiss()
            }
        }
    }

    val isLoading: Boolean
        get() = dialog!!.isShowing


}