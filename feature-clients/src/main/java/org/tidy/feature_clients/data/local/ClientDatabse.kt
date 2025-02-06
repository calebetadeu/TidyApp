package org.tidy.feature_clients.data.local

import ClientEntity
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [ClientEntity::class], version = 11 , exportSchema = false)
@TypeConverters(Converters::class)
abstract class ClientDatabase : RoomDatabase() {
    abstract fun clientDao(): ClientDao


    companion object {
        @Volatile
        private var INSTANCE: ClientDatabase? = null

        fun getDatabase(context: Context): ClientDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ClientDatabase::class.java,
                    "client_database"
                ).fallbackToDestructiveMigration(false).build()
                INSTANCE = instance
                instance
            }
        }
    }
}