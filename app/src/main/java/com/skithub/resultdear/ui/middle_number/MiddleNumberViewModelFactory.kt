package com.skithub.resultdear.ui.middle_number

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.skithub.resultdear.database.network.MyApi

class MiddleNumberViewModelFactory(private val myApi: MyApi): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MiddleNumberViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MiddleNumberViewModel(myApi) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }



}