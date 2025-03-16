package org.tidy.feature_clients.data.local

import androidx.room.TypeConverter

class ListStringConverter {
    @TypeConverter
    fun fromList(list: List<String>): String = list.joinToString(separator = ",")

    @TypeConverter
    fun toList(data: String): List<String> = if (data.isEmpty()) emptyList() else data.split(",")
}