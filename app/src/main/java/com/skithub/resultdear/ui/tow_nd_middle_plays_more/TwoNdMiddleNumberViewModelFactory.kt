package com.skithub.resultdear.ui.tow_nd_middle_plays_more

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.skithub.resultdear.database.network.MyApi

class TwoNdMiddleNumberViewModelFactory(private val myApi: MyApi): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TwoNdNumberViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TwoNdNumberViewModel(myApi) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }



}