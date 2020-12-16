package com.jdeveloperapps.noteapp.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "notes")
data class Note (

    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "date_time")
    val dateTime: String,

    @ColumnInfo(name = "subtitle")
    val subtitle: String,

    @ColumnInfo(name = "note_text")
    val noteText: String,

    @ColumnInfo(name = "image_path")
    val imagePath: String = "",

    @ColumnInfo(name = "color")
    val color: String = "",

    @ColumnInfo(name = "web_link")
    val webLink: String = ""
) : Serializable {
    override fun toString(): String {
        return "$title : $dateTime"
    }
}