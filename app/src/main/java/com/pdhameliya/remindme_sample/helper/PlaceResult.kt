package com.pdhameliya.remindme_sample.helper

import com.google.gson.annotations.SerializedName
import com.pdhameliya.remindme_sample.helper.landmark.Results

data class PlaceResult(
    @SerializedName("html_attributions") var htmlAttributions: ArrayList<String> = arrayListOf(),
    @SerializedName("next_page_token") var nextPageToken: String? = null,
    @SerializedName("results") var results: ArrayList<Results> = arrayListOf(),
    @SerializedName("status") var status: String? = null
)