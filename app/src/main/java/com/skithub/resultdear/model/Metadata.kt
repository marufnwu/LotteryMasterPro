package com.skithub.resultdear.model

import android.os.Build
import com.skithub.resultdear.BuildConfig

class Metadata {
    var manufacturer : String? = null

    init {
        manufacturer = Build.MANUFACTURER
    }

    fun print() {

    }
}