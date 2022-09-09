package com.example.zipanggotest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Toast;

import com.example.zipanggotest.adapters.KanjiListViewAdapter;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class KanjiBookmarkActivity extends AppCompatActivity {
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private Set<String> set;
    private String currentAcc;
    private String bookmarkName;
    private List<String> bookmarkList;
    private List<Kanji> kanjiCharacterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kanji_bookmark);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        sharedpreferences = getSharedPreferences("Account", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        currentAcc = sharedpreferences.getString("currentAccount", null);
        bookmarkName = currentAcc + "_bookmark";
        set = sharedpreferences.getStringSet(bookmarkName, null);
        if(set != null) {
            bookmarkList = new ArrayList<>(set);
        } else {
            bookmarkList = new ArrayList<>();
        }

        RecyclerView kanjiRecyclerList = findViewById(R.id.kanjibookmark_rv_kanjilist);

        KanjiDataXMLParser kanjiXML = new KanjiDataXMLParser(bookmarkList);
        try {
            kanjiCharacterList = kanjiXML.kanjiReadFeedwCharacter(getResources().getXml(R.xml.kanji_data));
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        KanjiListViewAdapter kanjiListViewAdapter = new KanjiListViewAdapter(KanjiBookmarkActivity.this, kanjiCharacterList);
        kanjiRecyclerList.setLayoutManager(new LinearLayoutManager(this));
        kanjiRecyclerList.setAdapter(kanjiListViewAdapter);
    }
}