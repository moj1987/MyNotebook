package com.mynotebook.englishonthego.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.mynotebook.englishonthego.tabs.DictionaryFragment;
import com.mynotebook.englishonthego.tabs.LyricsFragment;
import com.mynotebook.englishonthego.tabs.NotesFragment;

public class FragmentCategoryAdapter extends FragmentStateAdapter {

    private static final int NUM_FRAGMENTS = 3;

    public FragmentCategoryAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new DictionaryFragment();
            case 2:
                return new NotesFragment();
            default:
                return new LyricsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return NUM_FRAGMENTS;
    }
}
