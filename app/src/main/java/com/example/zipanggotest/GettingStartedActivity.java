package com.example.zipanggotest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.zipanggotest.adapters.MenuListViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GettingStartedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getting_started);

        ListView buttonList = findViewById(R.id.tutorial_lv_buttonList);
        buttonList.setAdapter(initTutorialMenu(buttonList));
        buttonList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            // Passes extended data (name of the lesson + topic no.) to the TopicActivity after clicking a topic button
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                MenuButton menuButton = (MenuButton) parent.getItemAtPosition(position);
//                Intent intentMenu = menuButton.getIntent();
//                intentMenu.putExtra("TOPICNAME_STR", extraLesson + "_t" + position);
//                startActivity(intentMenu);
            }
        });
    }

    private void addButton(List<MenuButton> buttonList, Class<?> cls, int resId, String header, String description) {
        Intent intent = new Intent(GettingStartedActivity.this, cls);
        Drawable icon = AppCompatResources.getDrawable(GettingStartedActivity.this, resId);
        MenuButton button = new MenuButton(GettingStartedActivity.this, intent, header, description, icon);
        buttonList.add(button);
    }

    private MenuListViewAdapter initTutorialMenu(ListView buttonList) {
        List<MenuButton> menuButtonList = new ArrayList<>();
        List<String> topicList;
        String [] topicDetail;
        topicList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.getting_started_list)));
        for(String topic : topicList) {
            topicDetail = topic.split("/");
            addButton(menuButtonList, TopicActivity.class, R.drawable.ic_lesson_topic_button,
                    topicDetail[0],
                    topicDetail[1]
            );
        }

        return new MenuListViewAdapter(GettingStartedActivity.this, menuButtonList);
    }
}