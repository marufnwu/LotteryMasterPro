package com.skithub.resultdear.ui.importent_tips

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.skithub.resultdear.R
import com.skithub.resultdear.adapter.VideoTutorialAdapter
import com.skithub.resultdear.database.network.ApiInterface
import com.skithub.resultdear.database.network.RetrofitClient
import com.skithub.resultdear.database.network.api.SecondServerApi
import com.skithub.resultdear.databinding.ActivityImportentTipsBinding
import com.skithub.resultdear.model.VideoTutorModel
import com.skithub.resultdear.ui.MyApplication
import com.skithub.resultdear.ui.middle_number.MiddleNumberViewModel
import com.skithub.resultdear.ui.middle_number.MiddleNumberViewModelFactory
import com.skithub.resultdear.utils.CommonMethod
import com.skithub.resultdear.utils.Constants
import com.skithub.resultdear.utils.Coroutines
import com.skithub.resultdear.utils.SharedPreUtils

class ImportentTipsActivity : AppCompatActivity() {

    private lateinit var secondServerApi: SecondServerApi
    private lateinit var binding: ActivityImportentTipsBinding
    private var apiInterface: ApiInterface? = null
    val CUSTOM_PREF_NAME = "User_data_extra"
    private lateinit var viewModel: MiddleNumberViewModel

    private var list: MutableList<VideoTutorModel> = arrayListOf()
    private lateinit var adapter: VideoTutorialAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityImportentTipsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val factory= MiddleNumberViewModelFactory((application as MyApplication).myApi)
        viewModel= ViewModelProvider(this,factory).get(MiddleNumberViewModel::class.java)
        supportActionBar?.title = getString(R.string.importanttips)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        secondServerApi = (application as MyApplication).secondServerApi

        apiInterface = RetrofitClient.getApiClient().create(ApiInterface::class.java)
        //getPremiumStatus()

        setupRecyclerView()
        getVideos()

    }

    private fun getPremiumStatus() {
        Coroutines.main {
            try {
                val response=viewModel.getPaidForContact("4",
                    SharedPreUtils.getStringFromStorageWithoutSuspend(this,Constants.userIdKey,Constants.defaultUserId).toString())
                if (response.isSuccessful && response.code()==200) {
                    if (response.body()!=null) {
                        binding.spinKit.visibility= View.GONE
                        binding.standerdLayout.visibility = View.VISIBLE

                        binding.pnOne.text = response.body()?.phone_one
                        binding.pnTwo.text = response.body()?.phone_two
                        binding.pnThree.text = response.body()?.phone_three

                        Glide.with(this@ImportentTipsActivity).load(response.body()?.video_thumbail).placeholder(R.drawable.loading_placeholder).fitCenter().into(binding.ytthumbail)
                        binding.ytthumbail.setOnClickListener {
                            val webIntent: Intent= Intent(Intent.ACTION_VIEW,Uri.parse(response.body()?.video_link))
                            startActivity(Intent.createChooser(webIntent,"Choose one:"))
                        }
                        binding.PhoneOne.setOnClickListener {
                            val dialIntent = Intent(Intent.ACTION_DIAL)
                            dialIntent.data = Uri.parse("tel:" + response.body()?.phone_one)
                            startActivity(dialIntent)
                        }
                        binding.PhoneTwo.setOnClickListener {
                            val dialIntent = Intent(Intent.ACTION_DIAL)
                            dialIntent.data = Uri.parse("tel:" + response.body()?.phone_two)
                            startActivity(dialIntent)
                        }
                        binding.PhoneThree.setOnClickListener {
                            val dialIntent = Intent(Intent.ACTION_DIAL)
                            dialIntent.data = Uri.parse("tel:" + response.body()?.phone_three)
                            startActivity(dialIntent)
                        }

                        binding.whatsAppBtn.setOnClickListener {
                            try {
                                val mobile = response.body()?.whats_app
                                val msg = ""
                                startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://api.whatsapp.com/send?phone=$mobile&text=$msg")
                                    )
                                )
                            } catch (e: java.lang.Exception) {
                                Toast.makeText(this@ImportentTipsActivity, "WhatsApp not Installed", Toast.LENGTH_SHORT).show()
                            }

                        }



                        if (response.body()?.banner_image!!.length > 6){
                            binding.adUpArrowBtn.setImageResource(R.drawable.ic_arrow_down_icon)
                            Glide.with(this@ImportentTipsActivity).load(response.body()?.banner_image).placeholder(R.drawable.loading_placeholder).into(binding.imageBanner)
                            binding.imageBanner.setOnClickListener {
                                val webIntent: Intent= Intent(Intent.ACTION_VIEW,Uri.parse(response.body()?.target_link))
                                startActivity(Intent.createChooser(webIntent,"Choose one:"))
                            }
                            /*val hide: Animation =
                                AnimationUtils.loadAnimation(this@MainActivity, R.anim.bottom_top)
                            binding.adLayout.startAnimation(hide)*/
                            binding.adLayout.visibility = View.VISIBLE
                            binding.adUpArrowBtn.visibility = View.VISIBLE

                            binding.adUpArrowBtn.setOnClickListener {
                                binding.adUpArrowBtn.visibility = View.GONE
                                binding.adDownArrowBtn.visibility = View.VISIBLE
                                binding.imageBanner.visibility = View.GONE
                                /*val hide: Animation =
                                    AnimationUtils.loadAnimation(this@MainActivity, R.anim.top_bottom)
                                binding.adLayout.startAnimation(hide)*/
                            }
                            binding.adDownArrowBtn.setOnClickListener {
                                binding.adUpArrowBtn.visibility = View.VISIBLE
                                binding.adDownArrowBtn.visibility = View.GONE
                                binding.imageBanner.visibility = View.VISIBLE
                                /*val hide: Animation =
                                    AnimationUtils.loadAnimation(this@MainActivity, R.anim.bottom_top)
                                binding.adLayout.startAnimation(hide)*/
                            }
                        }

                    }
                } else {
                    binding.spinKit.visibility= View.GONE
                }
            } catch (e: Exception) {
                binding.spinKit.visibility= View.GONE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)


        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.option_share -> {
                CommonMethod.shareAppLink(this)
            }
            R.id.option_moreApps -> {
                CommonMethod.openConsoleLink(this, Constants.consoleId)
            }
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView() {
        adapter= VideoTutorialAdapter(this,list)
        layoutManager= LinearLayoutManager(this)
        binding.recyclerView.layoutManager=layoutManager
        binding.recyclerView.adapter=adapter
    }

    private fun getVideos() {
        Coroutines.main {
            try {
                binding.spinKit.visibility= View.VISIBLE
                val response=secondServerApi.getVideoList("")
                if (response.isSuccessful && response.code()==200) {
                    binding.spinKit.visibility= View.GONE
                    if (response.body()!=null) {
                        if (response.body()?.status.equals("success",true)) {
                            list.addAll(response.body()?.data!!)
                            adapter.notifyDataSetChanged()
                        } else {
                            //shortToast("message:- ${response.body()?.message}")
                            //Log.d(Constants.TAG,"message:- ${response.body()?.message}")
                            binding.coomingSoon.visibility = View.VISIBLE
                            binding.recyclerView.visibility = View.GONE
                        }
                    }
                } else {
                    binding.spinKit.visibility= View.GONE
                    binding.coomingSoon.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                }
            } catch (e: Exception) {
                binding.spinKit.visibility= View.GONE
            }
        }
    }

}