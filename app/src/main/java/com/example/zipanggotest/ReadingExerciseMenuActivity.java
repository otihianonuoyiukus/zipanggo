package com.example.zipanggotest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.example.zipanggotest.adapters.LessonStaggeredAdapter;

import java.util.ArrayList;
import java.util.List;

public class ReadingExerciseMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_exercise_menu);

        // Reusing staggered view from the Lesson activity lol lmao
        RecyclerView recyclerView = findViewById(R.id.readingexercise_rl_tv_staggeredview);
        recyclerView.setHasFixedSize(true);
        List<StaggeredButton> buttonList = new ArrayList<>();
        //TODO: Somehow, make this dynamic instead of manual, I guess?
        buttonList.add(new StaggeredButton(getResources().getString(R.string.menu_readingexercise_title_0), R.drawable.ic_readingexercise_button_momotaro));
        buttonList.add(new StaggeredButton("", R.drawable.japanese_fuji_bg));
        buttonList.add(new StaggeredButton("", R.drawable.japanese_sakuratree_bg));
        buttonList.add(new StaggeredButton("", R.drawable.ic_menu_button_flashcard));
        buttonList.add(new StaggeredButton("", R.drawable.ic_menu_button_flashcard));
        buttonList.add(new StaggeredButton("", R.drawable.ic_menu_button_flashcard));
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        LessonStaggeredAdapter lsa = new LessonStaggeredAdapter(buttonList, new LessonStaggeredAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(StaggeredButton item) {
                Intent intent = new Intent(ReadingExerciseMenuActivity.this, ReadingExerciseActivity.class);
                String lessonName = item.getText();
                switch(lessonName) {
                    case "Momotaro":
                        intent.putExtra("STORYTITLE_STR", "readingexercise_momotaro");
                        break;
                }
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(lsa);
    }
}