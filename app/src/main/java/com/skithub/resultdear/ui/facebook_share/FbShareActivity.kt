package com.skithub.resultdear.ui.facebook_share

import android.gesture.GestureLibraries
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.share.Sharer
import com.facebook.share.model.ShareHashtag
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareDialog
import com.skithub.resultdear.R
import com.skithub.resultdear.databinding.ActivityFbShareBinding
import com.skithub.resultdear.model.FbShareContent
import com.skithub.resultdear.model.response.FbShareContentResponse
import com.skithub.resultdear.ui.MyApplication
import com.skithub.resultdear.utils.CommonMethod
import com.skithub.resultdear.utils.LoadingDialog
import com.skyfishjy.library.RippleBackground
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.facebook.share.model.SharePhoto
import com.facebook.share.model.SharePhotoContent


class FbShareActivity : AppCompatActivity() {

    lateinit var rippleBackground: RippleBackground
    lateinit var shareDialog: ShareDialog
    lateinit var binding : ActivityFbShareBinding
    lateinit var loadingDialog: LoadingDialog

    lateinit var content: ShareLinkContent


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFbShareBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingDialog = LoadingDialog(this)

        rippleBackground = binding.imageRoot.findViewById<RippleBackground>(R.id.content)
        rippleBackground.startRippleAnimation()

        val callbackManager = CallbackManager.Factory.create()
        shareDialog =  ShareDialog(this)
        // this part is optional
        shareDialog.registerCallback(callbackManager, object: FacebookCallback<Sharer.Result> {
            override fun onCancel() {

                Toast.makeText(this@FbShareActivity, "Sharing Cancel", Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: FacebookException) {

                shareDialog.show(content, ShareDialog.Mode.WEB)

            }

            override fun onSuccess(result: Sharer.Result) {
                Toast.makeText(this@FbShareActivity, "onSuccess", Toast.LENGTH_SHORT).show()

            }

        })

        fetchContent()
    }

    private fun fetchContent() {
        loadingDialog.show()
        (application as MyApplication).myApi
            .fbShareContent()
            .enqueue(object : Callback<FbShareContentResponse> {
                override fun onResponse(call: Call<FbShareContentResponse>, response: Response<FbShareContentResponse>) {
                    loadingDialog.hide()
                    if(response.isSuccessful ){
                        response.body()?.let { fbShareContentResponse->
                            if(!fbShareContentResponse.error){

                                fbShareContentResponse.banner?.let { banner ->
                                    if(!banner.error && banner.visible!! && !banner.imageUrl.isNullOrEmpty()){
                                        if(!isFinishing){
                                            Glide.with(this@FbShareActivity)
                                                .load(banner.imageUrl)
                                                .into(binding.imageRoot.findViewById(R.id.tutorialImageView))
                                        }

                                        binding.imageRoot.setOnClickListener {
                                            CommonMethod.openLink(this@FbShareActivity, banner.actionUrl!!)
                                        }
                                    }else{
                                        rippleBackground.stopRippleAnimation()
                                        binding.imageRoot.visibility  = View.GONE
                                    }
                                }

                                fbShareContentResponse.fbShareContent?.let { fbShareContent ->
                                    if(fbShareContent.instraction.isNullOrEmpty()){
                                        binding.tvInstruction.visibility = View.GONE

                                    }else{
                                        binding.tvInstruction.visibility = View.VISIBLE
                                        binding.tvInstruction.text = fbShareContent.instraction
                                    }



                                    buildShareContent(fbShareContent)

                                    binding.btnSHare.setOnClickListener {
                                        shareDialog.show(content, ShareDialog.Mode.AUTOMATIC)
                                    }


                                }
                            }
                        }


                    }
                }

                override fun onFailure(call: Call<FbShareContentResponse>, t: Throwable) {
                    loadingDialog.hide()

                }

            })
    }

    private fun buildShareContent(fbShareContent: FbShareContent) {

        content = ShareLinkContent.Builder()
            .setContentUrl(Uri.parse(fbShareContent.sharelink))
            .setQuote(fbShareContent.qoute)
            .setShareHashtag(ShareHashtag.Builder().setHashtag(fbShareContent.hashtag).build())
            .build()

    }
}