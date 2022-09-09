package com.example.zipanggotest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.example.zipanggotest.adapters.KanjiListViewAdapter;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.lang.Character;

public class KanjiDictionaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kanji_dictionary);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        RecyclerView kanjiRecyclerList = findViewById(R.id.kanjidictionary_rl_rv_recyclerlist);
        List<Kanji> kanjiCharacterList = new ArrayList<>();

        int jlptLevel = Integer.parseInt(Character.toString(getStringExtra(savedInstanceState, "JLPTLEVEL_STR").charAt(1)));
        String categoryStr = getStringExtra(savedInstanceState, "CATEGORYNAME_STR");

        KanjiDataXMLParser kanjiXML = new KanjiDataXMLParser(jlptLevel, categoryStr);
        try {
            kanjiCharacterList = kanjiXML.kanjiReadFeedwCategory(getResources().getXml(R.xml.kanji_data));
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        KanjiListViewAdapter kanjiListViewAdapter = new KanjiListViewAdapter(KanjiDictionaryActivity.this, kanjiCharacterList);
        kanjiRecyclerList.setLayoutManager(new LinearLayoutManager(this));
        kanjiRecyclerList.setAdapter(kanjiListViewAdapter);
    }

    private String getStringExtra(Bundle savedInstanceState, String key) {
        String stringExtra;

        if (savedInstanceState == null) {
            Bundle extra = getIntent().getExtras();
            if(extra == null) {
                stringExtra = null;
            } else {
                stringExtra = extra.getString(key);
            }
        } else {
            stringExtra = (String) savedInstanceState.getSerializable(key);
        }
        return stringExtra;
    }
}