package com.skithub.resultdear.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import com.skithub.resultdear.ui.importent_tips.ImportentTipsActivity

class TryAgainAlert(private val context: Context) {
    private  var builder: AlertDialog.Builder? = null
    private var title = "Something Wrong!!"
    private var messgae = "Message"
    private var onTryAgainClick :OnTryAgainClick? = null
    private var tryAgainText:String = "Try Again"

    interface OnTryAgainClick{
        fun onClick()
    }

    fun create(): TryAgainAlert {
        builder = AlertDialog.Builder(context)
        return this
    }

    fun setTryAgainButtonText(text:String?, listener:OnTryAgainClick): TryAgainAlert {
        if(text!=null){
            this.tryAgainText = text
        }
        this.onTryAgainClick = listener

        return this
    }

    fun setTitle(title:String): TryAgainAlert {
        this.title = title
        return this
    }

    fun setDescription(desc:String): TryAgainAlert {
        this.messgae = desc
        return this
    }

    fun show(){

        if (builder == null) create()

        builder?.setTitle(title)
        builder?.setMessage(messgae)



        builder?.setPositiveButton(tryAgainText) { dialog, which ->
            onTryAgainClick?.onClick()
        }
        Log.d("AlertDialog", "Called")

        if(!(context  as Activity).isFinishing && !(context as Activity).isDestroyed ){
            builder?.show()
        }

    }

}