package com.skithub.resultdear.ui.careful

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.skithub.resultdear.R
import com.skithub.resultdear.databinding.ActivityCarefulBinding
import com.skithub.resultdear.ui.MyApplication
import com.skithub.resultdear.utils.CommonMethod
import com.skithub.resultdear.utils.Coroutines
import com.skithub.resultdear.utils.LoadingDialog

class CarefulActivity : AppCompatActivity() {
    lateinit var binding : ActivityCarefulBinding
    lateinit var loading : LoadingDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarefulBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        loading = LoadingDialog(this)

        getBanner()

    }

    fun getBanner(){
        binding.content.startRippleAnimation()
        Coroutines.main {
            CommonMethod.getBanner("BeCareful", binding.tutorialImageView, (application as MyApplication).myApi, this)
        }
    }
}