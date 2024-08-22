package com.itzik.mynotes.project.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.itzik.mynotes.project.utils.Constants.USER_TABLE
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = USER_TABLE)
data class User(
    @PrimaryKey
    var id: String = nextUserId(),
    val userName: String,
    val email: String,
    val password: String,
    var isLoggedIn: Boolean = false,
    var phoneNumber: Long,
    var profileImage: String = ""
) : Parcelable {
    companion object {

        private var lastId: Long = 100000

        private fun nextUserId(): String {
            lastId += 1
            return "user_id_$lastId"
        }
    }
}