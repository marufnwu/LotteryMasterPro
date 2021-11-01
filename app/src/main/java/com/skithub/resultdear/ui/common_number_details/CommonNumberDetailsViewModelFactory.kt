package com.skithub.resultdear.ui.common_number_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.skithub.resultdear.database.network.MyApi

class CommonNumberDetailsViewModelFactory(private val myApi: MyApi): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CommonNumberDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CommonNumberDetailsViewModel(myApi) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }



}