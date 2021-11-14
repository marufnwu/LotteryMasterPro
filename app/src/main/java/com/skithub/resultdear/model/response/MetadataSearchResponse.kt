package com.skithub.resultdear.model.response


import com.google.gson.annotations.SerializedName
import com.skithub.resultdear.model.response.SearchUser

data class MetadataSearchResponse(
    @SerializedName("error")
    var error: Boolean? = null,
    @SerializedName("msg")
    var msg: String? = null,
    @SerializedName("searchUsers")
    var searchUsers: List<SearchUser>? = null
)