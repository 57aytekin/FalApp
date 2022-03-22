package com.example.sadekahvefal.model

import com.google.gson.annotations.SerializedName

data class DiffDate (
    @SerializedName("y")
    val year : Int,
    @SerializedName("m")
    val month : Int,
    @SerializedName("d")
    val day : Int,
    @SerializedName("h")
    val hour : Int,
    @SerializedName("i")
    val minute : Int,
    @SerializedName("s")
    val second : Int
)