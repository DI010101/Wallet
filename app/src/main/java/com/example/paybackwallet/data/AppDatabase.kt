package com.example.paybackwallet.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.paybackwallet.LoyaltyCard
import com.example.paybackwallet.CardDao

@Database(entities = [LoyaltyCard::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun get(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val inst = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "payback_wallet_db").build()
                INSTANCE = inst
                inst
            }
        }
    }
}
