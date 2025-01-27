package org.tidy.feature_clients.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken



class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromListString(value: List<String>?): String {
        return value?.joinToString(",") ?: "" // 🔥 Agora salvamos como string separada por vírgulas
    }

    @TypeConverter
    fun toListString(value: String?): List<String> {
        return value?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() } ?: emptyList() // 🔥 Agora tratamos corretamente
    }
}