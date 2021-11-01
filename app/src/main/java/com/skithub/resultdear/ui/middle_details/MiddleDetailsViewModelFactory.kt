package com.skithub.resultdear.ui.middle_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.skithub.resultdear.database.network.MyApi

class MiddleDetailsViewModelFactory(private val myApi: MyApi): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MiddleDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MiddleDetailsViewModel(myApi) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }



}