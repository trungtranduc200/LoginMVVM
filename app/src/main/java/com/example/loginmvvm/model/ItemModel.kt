package com.example.loginmvvm.model

data class ItemModel(val id:Int, val createdAt:String, val updateAt:String, val name:String,
                     var selected:Boolean = false)
