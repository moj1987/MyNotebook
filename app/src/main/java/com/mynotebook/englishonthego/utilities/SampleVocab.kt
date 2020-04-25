package com.mynotebook.englishonthego.utilities

import com.mynotebook.englishonthego.model.VocabModel

object SampleVocab {

    var vocab1: String = "consider"
    var definition1: String = "deem to be"
    var example1: String = "At the moment, artemisinin-based therapies are considered the best treatment, " +
            "but cost about $10 per dose - far too much for impoverished communities.\n" +
            "Seattle Times (Feb 16, 2012)"


    var vocab2: String = "minute"
    var definition2: String = "infinitely or immeasurably small"
    var example2: String = "The minute stain on the document was not visible to the naked eye."

    var vocab3: String = "accord"
    var definition3: String = "concurrence of opinion"
    var example3: String = "The committee worked in accord on the bill, and it eventually passed."


    fun getAllVocab(): List<VocabModel> {
        val list = arrayListOf<VocabModel>()
        list.add(VocabModel(vocab1, definition1, example1))
        list.add(VocabModel(vocab2, definition2, example2))
        list.add(VocabModel(vocab3, definition3, example3))
        return list
    }
}