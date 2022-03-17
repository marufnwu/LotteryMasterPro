package com.skithub.resultdear.model.response


import com.google.gson.annotations.SerializedName
import com.skithub.resultdear.model.Contacts

data class ButtonBuyRuleResponse(
    @SerializedName("audio")
    var audioResponse: AudioResponse? = AudioResponse(),
    @SerializedName("banner")
    var banner: Banner? = Banner(),
    @SerializedName("contacts")
    var contacts: Contacts? = Contacts(),
    @SerializedName("error")
    var error: Boolean = false,
    @SerializedName("msg")
    var msg: String? = ""
)