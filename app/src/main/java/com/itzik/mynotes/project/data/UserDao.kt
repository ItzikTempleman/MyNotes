package com.itzik.mynotes.project.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import androidx.room.Update
import com.itzik.mynotes.project.model.User
import com.itzik.mynotes.project.utils.Constants
import com.itzik.mynotes.project.utils.Constants.USER_TABLE
import com.itzik.mynotes.project.utils.Converters

@Dao
@TypeConverters(Converters::class)
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT*FROM ${Constants.USER_TABLE} WHERE isLoggedIn=1")
    suspend fun fetchLoggedInUsers(): MutableList<User>

    @Query("SELECT*FROM ${Constants.USER_TABLE} WHERE email = :email AND password = :password")
    suspend fun getUserFromEmailAndPassword(email: String, password: String): User

    @Query("SELECT * FROM ${Constants.USER_TABLE} WHERE userId = :userId")
    suspend fun getUserById(userId: String): User

    @Update
    suspend fun updateIsLoggedIn(user: User)

    @Update
    suspend fun updateProfileImage(user: User)

    @Update
    suspend fun updateWallpaper(user: User)

    @Query("UPDATE $USER_TABLE SET isViewGrid = :isViewGrid WHERE userId=:userId")
    suspend fun updateViewType(userId:String, isViewGrid:Boolean)

    @Query("SELECT isViewGrid FROM $USER_TABLE WHERE userId=:userId")
    suspend fun fetchViewType(userId:String):Boolean

}