package com.skithub.resultdear.ui.common_number

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.skithub.resultdear.database.network.MyApi

class CommonNumberViewModelFactory(private val myApi: MyApi): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CommonNumberViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CommonNumberViewModel(myApi) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }



}