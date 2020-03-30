package com.example.englishonthego

import com.google.gson.annotations.SerializedName

data class Feed(@SerializedName("result") var callResult: List<Responses>) {
}