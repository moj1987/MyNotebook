package com.mynotebook.englishonthego.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "notesTable")
data class NoteModel(
        @PrimaryKey(autoGenerate = true)
//   long & int 0 not-set || Long & Integer null not-set
        var id: Int = 0,
        var date: Date,
        var title: String,
        var text: String) {
    constructor(date: Date, title: String, text: String) : this(0, date, title, text)
}