package com.example.zipanggotest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.zipanggotest.adapters.AchievementListViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AchievementActivity extends AppCompatActivity {
    private RecyclerView achievementListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);

        achievementListView = findViewById(R.id.achievement_rv_achievementlist);

        List<List<String>> achievementList = new ArrayList<>();
        List<String> tempAchievementList = Arrays.asList(getResources().getStringArray(R.array.achievement_name));
        for (String achievement : tempAchievementList) {
            achievementList.add(Arrays.asList(achievement.split(",")));
        }
        AchievementListViewAdapter achievementListViewAdapter =
                new AchievementListViewAdapter(AchievementActivity.this, achievementList);
        achievementListView.setAdapter(achievementListViewAdapter);
        achievementListView.setLayoutManager(new LinearLayoutManager(this));
        achievementListView.setAdapter(achievementListViewAdapter);
    }
}