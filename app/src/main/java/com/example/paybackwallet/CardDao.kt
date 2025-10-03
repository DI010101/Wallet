package com.example.paybackwallet

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CardDao {
    @Query("SELECT * FROM cards ORDER BY id DESC")
    fun getAllSync(): List<LoyaltyCard>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(card: LoyaltyCard)
}
