package com.pdhameliya.remindme_sample.helper.landmark

import com.google.gson.annotations.SerializedName


data class OpeningHours (

  @SerializedName("open_now" ) var openNow : Boolean? = null

)