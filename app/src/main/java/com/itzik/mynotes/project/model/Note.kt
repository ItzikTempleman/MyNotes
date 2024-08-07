package com.itzik.mynotes.project.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.itzik.mynotes.project.utils.Constants.NOTE_TABLE
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Parcelize
@Entity(tableName = NOTE_TABLE)
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var content: String,
    var time: String = getCurrentTime(),
    var isInTrash: Boolean = false,
    var isStarred: Boolean = false,
    var isPinned: Boolean = false,
    var fontColor: Int?=null,
    var fontSize: Int?=null
    ) : Parcelable {

    companion object {
        fun getCurrentTime(): String {
            val currentDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            return currentDateTime.format(formatter)
        }
    }
}
