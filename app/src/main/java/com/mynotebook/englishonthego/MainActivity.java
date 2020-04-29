package com.mynotebook.englishonthego;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;

import com.mynotebook.englishonthego.databinding.ActivityMainBinding;
import com.mynotebook.englishonthego.ui.FragmentCategoryAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity  {

    private ActivityMainBinding binding;

    /**
     * Fragment objects declaration
     */
    private ViewPager2 viewPager;
    private FragmentStateAdapter fragmentStateAdapter;
    String[] tabsName = {"Lyric Search", "My Dictionary", "My Notes"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        /**
         * Fragment related instantiations
         */
        viewPager = findViewById(R.id.pager);
        fragmentStateAdapter = new FragmentCategoryAdapter(this);
        viewPager.setAdapter(fragmentStateAdapter);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) ->
                tab.setText(tabsName[position]))
                .attach();
    }
}

