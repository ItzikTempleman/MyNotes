package com.itzik.mynotes.project.utils

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.itzik.mynotes.project.model.User


@ProvidedTypeConverter
class Converters {
    @TypeConverter
    fun fromUser(user: User): String = Gson().toJson(user)

    @TypeConverter
    fun toUser(userString: String): User = Gson().fromJson(userString, object : TypeToken<User>() {}.type)

    @TypeConverter
    fun fromStringToListOfImages(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromListOfImagesToString(list: List<String>): String {
        return Gson().toJson(list)
    }
}