package com.example.roomdatabasedemo.data

import androidx.room.*
import com.example.roomdatabasedemo.model.Repository

@Entity(tableName = "user")
@Dao
interface DatabaseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUser(user: Repository)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMultipleUser(listOfUser:List<Repository>)

    @Query("SELECT * FROM user")
    fun getAllData(): List<Repository>

    @Query("DELETE FROM user")
    fun deleteAllUSer()

    @Query("DELETE FROM user WHERE userId=:user_id")
        fun deleteUser(user_id:Int)

    @Update(onConflict = OnConflictStrategy.IGNORE)
     fun update(user:Repository)

}