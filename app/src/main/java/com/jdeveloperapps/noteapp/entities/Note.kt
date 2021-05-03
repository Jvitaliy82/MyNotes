package com.jdeveloperapps.noteapp.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.text.DateFormat

@Entity(tableName = "notes")
data class Note (

    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    @ColumnInfo(name = "title")
    val title: String = "",

    @ColumnInfo(name = "date_time")
    val dateTime: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "subtitle")
    val subtitle: String = "",

    @ColumnInfo(name = "note_text")
    val noteText: String = "",

    @ColumnInfo(name = "image_path")
    val imagePath: String = "",

    @ColumnInfo(name = "color")
    val color: String = "#333333",

    @ColumnInfo(name = "web_link")
    val webLink: String = ""
) : Serializable {
    val createDateFormattedString: String
    get() = DateFormat.getDateInstance().format(dateTime)

    override fun toString(): String {
        return "$title : $dateTime"
    }
}