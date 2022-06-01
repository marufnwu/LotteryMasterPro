package com.skithub.resultdear.model

import com.google.gson.annotations.SerializedName

data class FacebookVideoModel(
    @SerializedName("id"           ) var id          : String? = null,
    @SerializedName("video_title"  ) var videoTitle  : String? = null,
    @SerializedName("thumbail"     ) var thumbail    : String? = null,
    @SerializedName("video_link"   ) var videoLink   : String? = null,
    @SerializedName("active"       ) var active      : String? = null,
    @SerializedName("random_views" ) var randomViews : String? = null,
    @SerializedName("views"        ) var views       : String? = null,
    @SerializedName("total_views"  ) var totalViews  : String? = null
)
