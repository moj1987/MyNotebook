package com.mynotebook.englishonthego.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vocabsTable")
data class VocabModel(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,
        var vocab: String?,
        var definition: String?,
        var example: String?) {
    constructor(vocab: String, definition: String, example: String) : this(0, vocab, definition, example)
}

