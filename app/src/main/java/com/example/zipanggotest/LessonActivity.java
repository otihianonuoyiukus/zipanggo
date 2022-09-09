package com.example.zipanggotest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.zipanggotest.adapters.LessonStaggeredAdapter;

import java.util.ArrayList;
import java.util.List;

public class LessonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        RecyclerView recyclerView = findViewById(R.id.lesson_rl_tv_staggeredview);
        recyclerView.setHasFixedSize(true);
        List<StaggeredButton> buttonList = new ArrayList<>();
        //TODO: Somehow, make this dynamic instead of manual, I guess?
        buttonList.add(new StaggeredButton(getResources().getString(R.string.menu_lesson_title_0), R.drawable.ic_lesson_button_internet));
        buttonList.add(new StaggeredButton("", R.drawable.japanese_fuji_bg));
        buttonList.add(new StaggeredButton("", R.drawable.japanese_sakuratree_bg));
        buttonList.add(new StaggeredButton("", R.drawable.ic_menu_button_flashcard));
        buttonList.add(new StaggeredButton("", R.drawable.ic_menu_button_flashcard));
        buttonList.add(new StaggeredButton("", R.drawable.ic_menu_button_flashcard));
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        LessonStaggeredAdapter lsa = new LessonStaggeredAdapter(buttonList, new LessonStaggeredAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(StaggeredButton item) {
                Intent intent = new Intent(LessonActivity.this, TopicMenuActivity.class);
                String lessonName = item.getText();
                switch(lessonName) {
                    case "Japanese Internet":
                        intent.putExtra("LESSONNAME_STR", "lesson_japanese_internet");
                    break;
                }
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(lsa);
    }
}