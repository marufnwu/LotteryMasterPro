package com.skithub.resultdear.model.response


import com.google.gson.annotations.SerializedName

data class AudioTutorial(
    @SerializedName("active")
    var active: String? = "",
    @SerializedName("audio")
    var audio: String? = "",
    @SerializedName("id")
    var id: String? = "",
    @SerializedName("thumbnail")
    var thumbnail: String? = "",
    @SerializedName("tittle")
    var tittle: String? = ""
)