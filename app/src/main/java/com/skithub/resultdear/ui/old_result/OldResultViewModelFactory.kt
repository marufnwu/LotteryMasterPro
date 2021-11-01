package com.skithub.resultdear.ui.old_result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.skithub.resultdear.database.network.MyApi

class OldResultViewModelFactory(private val myApi: MyApi): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OldResultViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OldResultViewModel(myApi) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }



}