package com.skithub.resultdear.model.response

import com.google.gson.annotations.SerializedName
import com.skithub.resultdear.model.FacebookVideoModel
import com.skithub.resultdear.model.VideoTutorModel

data class FacebookVideoResponse(
    @SerializedName("status"    ) var status    : String?         = null,
    @SerializedName("currPage"  ) var currPage  : Int?            = null,
    @SerializedName("totalPage" ) var totalPage : Int?            = null,
    @SerializedName("message"   ) var message   : String?         = null,
    @SerializedName("data"   ) val data: MutableList<FacebookVideoModel>?=null
)
