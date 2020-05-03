package com.mynotebook.englishonthego.networking

import com.google.gson.annotations.SerializedName

data class SearchFeed(@SerializedName("result") var callResult: List<LyricModel>) {
}