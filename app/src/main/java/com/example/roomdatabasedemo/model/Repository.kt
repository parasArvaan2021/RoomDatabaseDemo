package com.example.roomdatabasedemo.model

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "user")
data class Repository(

    @PrimaryKey @ColumnInfo(name = "userId") val userId: Int?,
    @ColumnInfo(name = "userName") val userName: String,
    @ColumnInfo(name = "userEmail") val userEmail: String
)
