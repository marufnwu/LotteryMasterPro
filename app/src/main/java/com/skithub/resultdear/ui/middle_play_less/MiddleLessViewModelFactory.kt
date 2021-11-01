package com.skithub.resultdear.ui.middle_play_less

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.skithub.resultdear.database.network.MyApi

class MiddleLessViewModelFactory(private val myApi: MyApi): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MiddleLessViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MiddleLessViewModel(myApi) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }



}