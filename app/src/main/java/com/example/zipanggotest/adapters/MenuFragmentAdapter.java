package com.example.zipanggotest.adapters;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.zipanggotest.fragments.MenuGameFragment;
import com.example.zipanggotest.fragments.MenuStatisticFragment;
import com.example.zipanggotest.fragments.MenuStudyFragment;

import org.jetbrains.annotations.NotNull;

public class MenuFragmentAdapter extends FragmentStateAdapter {

    private final Context context;

    public MenuFragmentAdapter(
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
                return new MenuStudyFragment();
            case 1:
                return new MenuGameFragment();
            default:
                return new MenuStatisticFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
