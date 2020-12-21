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
    var title: String = "",

    @ColumnInfo(name = "date_time")
    var dateTime: String = "",

    @ColumnInfo(name = "subtitle")
    var subtitle: String = "",

    @ColumnInfo(name = "note_text")
    var noteText: String = "",

    @ColumnInfo(name = "image_path")
    var imagePath: String = "",

    @ColumnInfo(name = "color")
    var color: String = "#333333",

    @ColumnInfo(name = "web_link")
    var webLink: String = ""
) : Serializable {
    override fun toString(): String {
        return "$title : $dateTime"
    }
}