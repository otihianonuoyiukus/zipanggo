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

public class TopicMenuActivity extends AppCompatActivity {
    //Default value of extraLesson
    private String extraLesson = "lesson_japanese_internet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_menu);
        extraLesson = getStringExtra(savedInstanceState, "LESSONNAME_STR");
        ListView buttonList = findViewById(R.id.lesson_topic_lv_buttonList);
        buttonList.setAdapter(initTopicMenu(buttonList));
        buttonList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            // Passes extended data (name of the lesson + topic no.) to the TopicActivity after clicking a topic button
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MenuButton menuButton = (MenuButton) parent.getItemAtPosition(position);
                Intent intentMenu = menuButton.getIntent();
                intentMenu.putExtra("TOPICNAME_STR", extraLesson + "_t" + position);
                startActivity(intentMenu);
            }
        });
    }

    // Initializes the topic buttons of the topic menu
    private MenuListViewAdapter initTopicMenu(ListView buttonList) {
        List<MenuButton> menuButtonList = new ArrayList<>();
        List<String> topicList;
        String [] topicDetail;
        switch(extraLesson) {
            case "lesson_japanese_internet":
                topicList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.lesson_japanese_internet)));
                for(String topic : topicList) {
                    topicDetail = topic.split("/");
                    addButton(menuButtonList, TopicActivity.class, R.drawable.ic_lesson_topic_button,
                            topicDetail[0],
                            topicDetail[1]
                    );
                }
                break;
        }

        return new MenuListViewAdapter(TopicMenuActivity.this, menuButtonList);
    }

    private void addButton(List<MenuButton> buttonList, Class<?> cls, int resId, String header, String description) {
        Intent intent = new Intent(TopicMenuActivity.this, cls);
        Drawable icon = AppCompatResources.getDrawable(TopicMenuActivity.this, resId);
        MenuButton button = new MenuButton(TopicMenuActivity.this, intent, header, description, icon);
        buttonList.add(button);
    }

    private String getStringExtra(Bundle savedInstanceState, String key) {
        String stringExtra = "";

        if (savedInstanceState == null) {
            Bundle extra = getIntent().getExtras();
            if(extra == null) {
                stringExtra = null;
            } else {
                stringExtra = extra.getString("LESSONNAME_STR");
            }
        } else {
            stringExtra = (String) savedInstanceState.getSerializable("LESSONNAME_STR");
        }

        return stringExtra;
    }
}