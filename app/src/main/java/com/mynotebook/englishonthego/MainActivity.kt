package com.mynotebook.englishonthego

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy
import com.mynotebook.englishonthego.ui.FragmentCategoryAdapter
import com.mynotebook.englishonthego.utilities.SharedPrefUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var tabsName = arrayOf("Lyric Search", "Lyrics", "Dictionary", "Notes")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initTabs()
    }

    override fun onPause() {
        SharedPrefUtils.setLastTabPosition(this, pager.currentItem)
        super.onPause()
    }

    private fun initTabs() {
        val fragmentStateAdapter = FragmentCategoryAdapter(this)
        pager.adapter = fragmentStateAdapter
        pager.currentItem = SharedPrefUtils.getLastTabPosition(this)

        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        pager.let {
            TabLayoutMediator(
                    tabLayout,
                    it,
                    TabConfigurationStrategy { tab: TabLayout.Tab, position: Int -> tab.text = tabsName[position] }
            ).attach()
        }
    }
}
