package com.mynotebook.englishonthego.utilities

import android.content.Context

class SharedPrefUtils {

    companion object {

        private const val SHARED_PREF_KEY =
                "com.mynotebook.englishonthego.utilities.SharedPrefUtils.SHARED_PREF_KEY"

        private const val SHARED_PREF_TAB_POSITION_KEY =
                "com.mynotebook.englishonthego.utilities.SharedPrefUtils.SHARED_PREF_TAB_POSITION_KEY"

        private const val DEFAULT_TAB_POSITION = 0

        fun setLastTabPosition(context: Context, position: Int) {
            context.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)
                    .edit()
                    .putInt(SHARED_PREF_TAB_POSITION_KEY, position)
                    .apply()
        }

        fun getLastTabPosition(context: Context): Int {
            return context.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)
                    .getInt(SHARED_PREF_TAB_POSITION_KEY, DEFAULT_TAB_POSITION)
        }

    }
}