package com.skithub.resultdear.ui.special_or_bumper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.skithub.resultdear.database.network.MyApi

class SpecialOrBumperViewModelFactory(private val myApi: MyApi): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SpecialOrBumperViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SpecialOrBumperViewModel(myApi) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }



}