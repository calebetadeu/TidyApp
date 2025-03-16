package org.tidy.feature_clients.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [ClientEntity::class], version = 16)
@TypeConverters(ListStringConverter::class)
abstract class ClientDatabase : RoomDatabase() {
    abstract fun clientDao(): ClientDao
}