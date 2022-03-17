package com.skithub.resultdear.model.response


import com.google.gson.annotations.SerializedName

data class AudioTutorialResponse(
    @SerializedName("audios")
    var audios: List<AudioTutorial>? = listOf(),
    @SerializedName("currentPage")
    var currentPage: String? = "",
    @SerializedName("error")
    var error: Boolean? = false,
    @SerializedName("msg")
    var msg: String? = "",
    @SerializedName("totalPages")
    var totalPages: Int? = 0
)