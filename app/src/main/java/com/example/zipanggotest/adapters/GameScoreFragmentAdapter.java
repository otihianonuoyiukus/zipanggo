package com.example.zipanggotest.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.zipanggotest.fragments.GameScoreAllFragment;
import com.example.zipanggotest.fragments.GameScorePersonalFragment;
import com.example.zipanggotest.fragments.MenuStatisticFragment;

import org.jetbrains.annotations.NotNull;

public class GameScoreFragmentAdapter extends FragmentStateAdapter {

    private final Context context;

    public GameScoreFragmentAdapter(
            Context context,
            @NonNull @NotNull FragmentManager fragmentManager,
            @NonNull @NotNull Lifecycle lifecycle)
    {
        super(fragmentManager, lifecycle);
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch(position) {
            case 0:
                return new GameScorePersonalFragment();
            default:
                return new GameScoreAllFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
