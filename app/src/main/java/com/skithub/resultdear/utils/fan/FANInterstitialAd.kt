package com.skithub.resultdear.utils.fan

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast

import com.google.android.gms.ads.LoadAdError
import com.skithub.resultdear.utils.CommonMethod
import com.skithub.resultdear.utils.SharedPreUtils
import com.skithub.resultdear.utils.admob.MyInterstitialAd


class FANInterstitialAd(val context: Context) {
//    private lateinit var interstitialAdListener: InterstitialAdListener
//    private lateinit var fanInterstitialAdListener: FanInterstitialAdListener
//
//    interface FanInterstitialAdListener{
//        fun onAdDismissedFullScreenContent()
//        fun onAdFailedToShowFullScreenContent(adError: com.google.android.gms.ads.AdError?)
//        fun onAdShowedFullScreenContent()
//        fun onAdFailedToLoad(adError: LoadAdError?)
//        fun onAdLoaded(interstitialAd: com.google.android.gms.ads.interstitial.InterstitialAd)
//    }
//
//    companion object{
//        public var interstitialAd : InterstitialAd? = null
//        const val placementId = "1000235817291984_1000238193958413"
//        const val TAG = "FAN_INTERSTITIAL_AD"
//        private var mAdIsLoading: Boolean = false
//
//        val AD_SHOW_HOUR = 24
//        val AD_SIZE = 4
//
//        fun isAdAvailable(): Boolean {
//            if(interstitialAd!!.isAdLoaded){
//                return true
//            }
//
//            return false
//        }
//    }
//
//    init {
//        interstitialAd = InterstitialAd(context, placementId)
//        interstitialAdListener = object : InterstitialAdListener {
//            override fun onInterstitialDisplayed(ad: Ad) {
//                // Interstitial ad displayed callback
//                Log.e(TAG, "Interstitial ad displayed.")
//                SharedPreUtils.setLastFanAdTimeToStorage(context)
//            }
//
//            override fun onInterstitialDismissed(ad: Ad) {
//                // Interstitial dismissed callback
//                Log.e(TAG, "Interstitial ad dismissed.")
//                finishActivity()
//            }
//
//            override fun onError(ad: Ad?, adError: AdError) {
//                // Ad error callback
//                mAdIsLoading = false
//                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage())
//                Toast.makeText(context, adError.getErrorMessage(), Toast.LENGTH_LONG).show()
//            }
//
//            override fun onAdLoaded(ad: Ad) {
//                // Interstitial ad is loaded and ready to be displayed
//                mAdIsLoading = false
//                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!")
//                // Show the ad
//
//            }
//
//            override fun onAdClicked(ad: Ad) {
//                // Ad clicked callback
//                Log.d(TAG, "Interstitial ad clicked!")
//            }
//
//            override fun onLoggingImpression(ad: Ad) {
//                // Ad impression logged callback
//                Log.d(TAG, "Interstitial ad impression logged!")
//            }
//        }
//    }
//
//    fun loadAd(){
//        if(!interstitialAd!!.isAdLoaded && !mAdIsLoading){
//            load()
//        }
//    }
//
//    private fun load(){
//        if(!interstitialAd!!.isAdLoaded && isAdShownAllowed()){
//            mAdIsLoading = true
//            interstitialAd?.loadAd(
//                interstitialAd?.buildLoadAdConfig()
//                    ?.withAdListener(interstitialAdListener)
//                    ?.build())
//        }
//    }
//
//    fun show(){
//        if(interstitialAd!!.isAdLoaded){
//            interstitialAd!!.show()
//        }
//    }
//
//    fun onBackPress(){
//        if(isAdAvailable()){
//            show()
//        }else{
//            //loadingDialog.show()
//            //load()
//            //Toast.makeText(context, "isAdAvailable not", Toast.LENGTH_SHORT).show()
//            finishActivity()
//        }
//    }
//
//    @SuppressLint("LongLogTag")
//    private fun finishActivity(){
//        Log.d("LotterySerialCheckActivity", "finishCalled")
//        //loadingDialog.hide()
//        (context as Activity).finish()
//        //goToMainactivity()
//    }
//
//    @SuppressLint("SimpleDateFormat")
//    fun isAdShownAllowed(): Boolean{
//        if (CommonMethod.accountAge!=null){
//            if(CommonMethod.accountAge!!.toInt()<=4){
//                return  false
//            }
//        }else{
//            return false
//        }
//        val currentTime =  System.currentTimeMillis()
//
//        val lastAdTime = SharedPreUtils.getLastFanAdTimeWithoutSuspend(context)
//
//        val adCount = SharedPreUtils.getFanAdCountWithoutSuspend(context)
//
//        val hour = CommonMethod.getHoursDifBetweenToTime(currentTime, lastAdTime)
//
//        return if(hour >= AD_SHOW_HOUR){
//            true
//        }else{
//            adCount< AD_SIZE
//        }
//    }
}