package com.example.englishonthego.utilities

import com.example.englishonthego.model.NoteModel
import java.util.*

object SampleNote {

    val title1: String = "note 1"
    val text1: String = "A simple note"

    val title2: String = "note 2"
    val text2: String = "a note with a \n" +
            "line feed"

    val title3: String = "note 3"
    val text3: String = "After creating my model class, "+
            "I want to create another class that defines sample data, that I can use to test \n" +
            " the app. I'll place this new class in a new package that I'll call utilities. And \n" +
            " then I'll create the class there, and I'll name it simply, SampleData. You could \n" +
            "name that anything you want. Now, I want three notes, and they each should\n" +
            " demonstrate something different. One note can be a single line of text, one note \n" +
            "could have a line feed, and one could be very long text. \n" +
            "Now rather than generating all that code from scratch, I've included a file in\n" +
            " this branch, I'll go to the project view, and then down here under resources, \n" +
            "under the code directory, I have a file called sample.java. I'll copy that code, \n" +
            "and then I'll go back to sample data, and I'll paste it into place. So I now have\n" +
            " three constants to work with, and they each serve a different testing purpose.\n" +
            " I'm going to wrap each of these values inside an instance of the note editing\n" +
            " class. "

    private fun getDate(diff: Int): Date {
        var cal = Calendar.getInstance()
        cal.add(Calendar.MILLISECOND, diff)
        return cal.time
    }

    fun getAllNotes(): List<NoteModel> {
        val list = arrayListOf<NoteModel>()
        list.add(NoteModel(0, getDate(0), title1, text1))
        list.add(NoteModel(0, getDate(10), title2, text2))
        list.add(NoteModel(0, getDate(30), title3, text3))
        return list
    }

}
