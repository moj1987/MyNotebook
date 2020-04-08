package com.example.englishonthego.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.englishonthego.tabs.DictionaryFragment;
import com.example.englishonthego.tabs.LyricsFragment;

public class FragmentCatagoryAdapter extends FragmentStateAdapter {

    private static final int NUM_FRAGMENTS = 2;

    public FragmentCatagoryAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new LyricsFragment();
            case 1:
                return new DictionaryFragment();
        }
        return new LyricsFragment();
    }

    @Override
    public int getItemCount() {
        return NUM_FRAGMENTS;
    }
}
