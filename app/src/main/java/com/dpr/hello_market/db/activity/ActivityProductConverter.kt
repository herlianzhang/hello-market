package com.dpr.hello_market.db.activity

import androidx.room.TypeConverter
import com.dpr.hello_market.db.cart.CartDbModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ActivityProductConverter {

    @TypeConverter
    fun fromProductToJson(stat: List<CartDbModel>): String {
        return Gson().toJson(stat)
    }

    @TypeConverter
    fun toProductList(jsonProduct: String): List<CartDbModel> {
        val notesType = object : TypeToken<List<CartDbModel>>() {}.type
        return Gson().fromJson(jsonProduct, notesType)
    }
}