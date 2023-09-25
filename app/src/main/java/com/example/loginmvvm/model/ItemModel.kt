package com.example.loginmvvm.model

import com.google.gson.annotations.SerializedName

data class ItemModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updateAt: String,
    @SerializedName("name")
    val name: String,
    var selected: Boolean = false
)
