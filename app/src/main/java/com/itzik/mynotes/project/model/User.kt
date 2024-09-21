package com.itzik.mynotes.project.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.itzik.mynotes.project.utils.Constants.USER_TABLE
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
@Entity(tableName = USER_TABLE)
data class User(
    @PrimaryKey
    var userId: String = UUID.randomUUID().toString(),
    val userName: String,
    val email: String,
    val password: String,
    var isLoggedIn: Boolean = false,
    var phoneNumber: Long,
    var profileImage: String = "",
    var gender:Gender,
    var dateOfBirth:String,
    var isViewGrid: Boolean = false,
    val selectedWallpaper:String = ""
) : Parcelable


enum class Gender{
    MALE, FEMALE, OTHER
}