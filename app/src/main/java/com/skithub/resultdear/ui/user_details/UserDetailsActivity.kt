package com.skithub.resultdear.ui.user_details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.skithub.resultdear.R
import com.skithub.resultdear.databinding.ActivityUserDetailsBinding
import com.skithub.resultdear.model.response.AddUserInfoDataResponse
import com.skithub.resultdear.model.response.GenericResponse
import com.skithub.resultdear.ui.MyApplication
import com.skithub.resultdear.utils.CommonMethod
import com.skithub.resultdear.utils.Constants
import com.skithub.resultdear.utils.LoadingDialog
import com.skithub.resultdear.utils.MyExtensions.shortToast
import com.skithub.resultdear.utils.SharedPreUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailsActivity : AppCompatActivity() {
    lateinit var binding : ActivityUserDetailsBinding
    lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        loadingDialog = LoadingDialog(this)

        binding.btnSubmit.setOnClickListener {
            submitUserDetails()
        }


        getInfo()


    }

    private fun getInfo() {
        loadingDialog.show()
        (application as MyApplication)
            .myApi
            .getAddUserInfoBanner("AddUserInfoActivity")
            .enqueue(object : Callback<AddUserInfoDataResponse> {
                override fun onResponse(
                    call: Call<AddUserInfoDataResponse>, response: Response<AddUserInfoDataResponse>) {
                    loadingDialog.hide()
                    response.isSuccessful.apply {
                        if (this){
                            response.body()?.let { it ->

                                if(!it.text.isEmpty()){
                                   binding.txt.visibility = View.VISIBLE
                                   binding.txt.setText(it.text)
                                }

                                it.banner?.let { banner->
                                    if(!banner.error){
                                        binding.thumbnailLayout.visibility =View.VISIBLE
                                        Glide.with(this@UserDetailsActivity)
                                            .load(banner.imageUrl)
                                            .into(binding.ytthumbail)

                                        binding.ytthumbail.setOnClickListener {
                                            CommonMethod.openLink(this@UserDetailsActivity, banner.actionUrl!!)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<AddUserInfoDataResponse>, t: Throwable) {
                    loadingDialog.hide()
                }

            })
    }

    private fun submitUserDetails() {

        val userId = SharedPreUtils.getStringFromStorageWithoutSuspend(this, Constants.userIdKey, Constants.defaultUserId)!!


        val name = binding.edtName.text.toString()
        val zila = binding.edtZila.text.toString()
        val thana = binding.edtThana.text.toString()
        val postOffice = binding.edtPostOffice.text.toString()
        val pinCode = binding.edtPinCode.text.toString()
        val village = binding.edtVillage.text.toString()
        val phone = binding.edtPhone.text.toString()
        val whatsapp = binding.edtWhatsappNum.text.toString()

        if(name.isEmpty() or zila.isEmpty() or thana.isEmpty() or postOffice.isEmpty()
            or pinCode.isEmpty() or village.isEmpty() or phone.isEmpty() or whatsapp.isEmpty()){
            shortToast("All Field's are required")

            return
        }


        loadingDialog.show()

        (application as MyApplication)
            .myApi
            .addUserDetails(userId, name, zila, thana, village, postOffice, pinCode, phone, whatsapp)
            .enqueue(object: Callback<GenericResponse> {
                override fun onResponse(
                    call: Call<GenericResponse>, response: Response<GenericResponse>) {
                    loadingDialog.hide()
                    response.body()?.msg?.let {
                        Toast.makeText(this@UserDetailsActivity, it, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                    loadingDialog.hide()
                }

            })





    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}