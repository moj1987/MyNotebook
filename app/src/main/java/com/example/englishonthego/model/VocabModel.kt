package com.example.englishonthego.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "vocabsTable")
data class VocabModel(
        @PrimaryKey(autoGenerate = true)
        val id: Int?,

        val vocab: String?,
        val definition: String?,
        val example: String?) {

    constructor(vocab: String, definition: String, example: String) : this(vocab.toLowerCase(Locale.US).hashCode(), vocab, definition, example) {

    }
//    @Ignore
//    constructor() : this(null, null, null, null)
//    constructor(vocab: String, definition: String, example: String) : this() {

}

//    @Ignore
//    constructor(vocab: String, definition: String, example: String) : this(null, vocab, definition, example)
