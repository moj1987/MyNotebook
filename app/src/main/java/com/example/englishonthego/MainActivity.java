package com.example.englishonthego;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.englishonthego.databinding.ActivityMainBinding;
import com.example.englishonthego.networking.RetrofitManager;
import com.example.englishonthego.networking.SearchFeed;
import com.example.englishonthego.networking.HappiApi;
import com.example.englishonthego.networking.Responses;
import com.example.englishonthego.ui.FragmentCatagoryAdapter;
import com.example.englishonthego.ui.LyricSearchAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity  {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding binding;



    /**
     * Fragment objects declaration
     */
    private ViewPager2 viewPager;
    private FragmentStateAdapter fragmentStateAdapter;
    String[] tabsName = {"Lyric search", "Personal dictionary"};

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
        fragmentStateAdapter = new FragmentCatagoryAdapter(this);
        viewPager.setAdapter(fragmentStateAdapter);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) ->
                tab.setText(tabsName[position]))
                .attach();

//        To Test
//        to be deleted:
        Intent intent = new Intent(getApplicationContext(), VocabEditorActivity.class);
        startActivity(intent);
    }
}

