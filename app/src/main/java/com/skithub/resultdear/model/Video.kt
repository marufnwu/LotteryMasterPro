package com.skithub.resultdear.model


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

class Video() : Parcelable{
     @SerializedName("active")
     var active: String? = ""
     @SerializedName("description")
     var description: String? = ""
     @SerializedName("duration")
     var duration: String? = ""
     @SerializedName("file_name")
     var fileName: String? = ""
     @SerializedName("id")
     var id: String? = ""
     @SerializedName("processing")
     var processing: String? = ""
     @SerializedName("thumbnail")
     var thumbnail: String? = ""
     @SerializedName("title")
     var title: String? = ""
     @SerializedName("p_240"       ) var p240        : String? = null
     @SerializedName("p_360"       ) var p360        : String? = null
     @SerializedName("p_480"       ) var p480        : String? = null
     @SerializedName("p_720"       ) var p720        : String? = null
     @SerializedName("p_1080"      ) var p1080       : String? = null

     constructor(parcel: Parcel) : this() {
          active = parcel.readString()
          description = parcel.readString()
          duration = parcel.readString()
          fileName = parcel.readString()
          id = parcel.readString()
          processing = parcel.readString()
          thumbnail = parcel.readString()
          title = parcel.readString()
          p240 = parcel.readString()
          p360 = parcel.readString()
          p480 = parcel.readString()
          p720 = parcel.readString()
          p1080 = parcel.readString()
     }

     override fun writeToParcel(parcel: Parcel, flags: Int) {
          parcel.writeString(active)
          parcel.writeString(description)
          parcel.writeString(duration)
          parcel.writeString(fileName)
          parcel.writeString(id)
          parcel.writeString(processing)
          parcel.writeString(thumbnail)
          parcel.writeString(title)
          parcel.writeString(p240)
          parcel.writeString(p360)
          parcel.writeString(p480)
          parcel.writeString(p720)
          parcel.writeString(p1080)
     }

     override fun describeContents(): Int {
          return 0
     }

     companion object CREATOR : Parcelable.Creator<Video> {
          override fun createFromParcel(parcel: Parcel): Video {
               return Video(parcel)
          }

          override fun newArray(size: Int): Array<Video?> {
               return arrayOfNulls(size)
          }
     }


}