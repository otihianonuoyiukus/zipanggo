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

public class KanjiDictionaryMenuActivity extends AppCompatActivity {
    private String jlptLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kanji_dictionary_menu);
        jlptLevel = getStringExtra(savedInstanceState, "JLPTLEVEL_STR");
        ListView buttonList = findViewById(R.id.kanjidictionary_menu_lv_buttonList);
        buttonList.setAdapter(initKanjiDictionaryMenu(buttonList));
        buttonList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            // Passes extended data (button header) to the KanjiDictionaryActivity after clicking a button
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MenuButton menuButton = (MenuButton) parent.getItemAtPosition(position);
                Intent intentMenu = menuButton.getIntent();
                intentMenu.putExtra("CATEGORYNAME_STR", menuButton.getHeader());
                intentMenu.putExtra("JLPTLEVEL_STR", jlptLevel);
                startActivity(intentMenu);
            }
        });
    }

    // Initializes the topic buttons of the topic menu
    private MenuListViewAdapter initKanjiDictionaryMenu(ListView buttonList) {
        List<MenuButton> menuButtonList = new ArrayList<>();
        List<String> categoryList;
        String [] categoryDetail;
        switch(jlptLevel) {
            case "N5":
                categoryList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.kanjidictionary_n5_category)));
                for(String topic : categoryList) {
                    categoryDetail = topic.split("/");
                    addButton(menuButtonList, KanjiDictionaryActivity.class, R.drawable.ic_kanjidictionary_menu_button,
                            categoryDetail[0],
                            categoryDetail[1]
                    );
                }
                break;
        }
        return new MenuListViewAdapter(KanjiDictionaryMenuActivity.this, menuButtonList);
    }

    private void addButton(List<MenuButton> buttonList, Class<?> cls, int resId, String header, String description) {
        Intent intent = new Intent(KanjiDictionaryMenuActivity.this, cls);
        Drawable icon = AppCompatResources.getDrawable(KanjiDictionaryMenuActivity.this, resId);
        MenuButton button = new MenuButton(KanjiDictionaryMenuActivity.this, intent, header, description, icon);
        buttonList.add(button);
    }

    private String getStringExtra(Bundle savedInstanceState, String key) {
        String stringExtra = "";

        if (savedInstanceState == null) {
            Bundle extra = getIntent().getExtras();
            if(extra == null) {
                stringExtra = null;
            } else {
                stringExtra = extra.getString("JLPTLEVEL_STR");
            }
        } else {
            stringExtra = (String) savedInstanceState.getSerializable("JLPTLEVEL_STR");
        }
        return stringExtra;
    }
}