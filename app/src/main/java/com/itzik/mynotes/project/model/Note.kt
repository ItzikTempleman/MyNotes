package com.itzik.mynotes.project.model

import android.os.Parcelable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.itzik.mynotes.project.utils.Constants.NOTE_TABLE
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Parcelize
@Entity(
    tableName = NOTE_TABLE,
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["userId"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["userId"])]
)

data class Note(
    @PrimaryKey(autoGenerate = true)
    var noteId: Int = 0,
    var content: String,
    var time: String = getCurrentTime(),
    var isInTrash: Boolean = false,
    var isStarred: Boolean = false,
    var isPinned: Boolean = false,
    var fontColor: Int = Color.Black.toArgb(),
    var fontSize: Int = 20,
    var userId: String,
) : Parcelable {

    companion object {
        fun getCurrentTime(): String {
            val currentDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            return currentDateTime.format(formatter)
        }
    }
}
