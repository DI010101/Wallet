package com.example.paybackwallet

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cards")
data class LoyaltyCard(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val code: String
)
