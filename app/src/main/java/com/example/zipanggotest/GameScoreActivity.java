package com.example.zipanggotest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.zipanggotest.adapters.GameScoreFragmentAdapter;
import com.google.android.material.tabs.TabLayout;

public class GameScoreActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_score);

        tabLayout = findViewById(R.id.game_score_tl_tab);
        viewPager2 = findViewById(R.id.game_score_vp2_viewpager);
        viewPager2.setOffscreenPageLimit(2);

        FragmentManager fragmentManager = getSupportFragmentManager();
        GameScoreFragmentAdapter gameScoreFragmentAdapter =
                new GameScoreFragmentAdapter(GameScoreActivity.this, fragmentManager, getLifecycle());
        viewPager2.setAdapter(gameScoreFragmentAdapter);

        initTab();
    }

    protected void initTab() {
        tabLayout.addTab(tabLayout.newTab().setText("Personal"));
        tabLayout.addTab(tabLayout.newTab().setText("All"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }
}