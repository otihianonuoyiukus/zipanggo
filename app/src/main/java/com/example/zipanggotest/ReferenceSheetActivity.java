package com.example.zipanggotest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;

import com.example.zipanggotest.adapters.CharacterGridViewAdapter;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReferenceSheetActivity extends AppCompatActivity {

    private List<Kana> kanaCharacterList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reference_sheet);
        String extraSystem = "Hiragana";
        extraSystem = getStringExtra(savedInstanceState, "SYSTEMTYPE_STR");
        String extraSound = getStringExtra(savedInstanceState, "SOUNDTYPE_STR");
        String[] soundTypeArr = extraSound.split(",");

        // Parses XML data which contains the kana characters and their attributes
        //
        KanaDataXMLParser kanaXML = new KanaDataXMLParser(extraSystem.toLowerCase(), soundTypeArr);
        try {
            kanaCharacterList = kanaXML.kanaReadFeed(getResources().getXml(R.xml.kana_data));
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        GridView kanaGrid = findViewById(R.id.characterGrid);
        CharacterGridViewAdapter characterGridViewAdapter =
                new CharacterGridViewAdapter(getApplicationContext(), kanaCharacterList);
        kanaGrid.setAdapter(characterGridViewAdapter);
    }

    private String getStringExtra(Bundle savedInstanceState, String key) {
        String stringExtra = "";

        if (savedInstanceState == null) {
            Bundle extra = getIntent().getExtras();
            if(extra == null) {
                stringExtra = null;
            } else {
                switch (key) {
                    case "SOUNDTYPE_STR":
                        stringExtra = extra.getString("SOUNDTYPE_STR");
                        break;
                    case "SYSTEMTYPE_STR":
                        stringExtra = extra.getString("SYSTEMTYPE_STR");
                        break;
                }
            }
        } else {
            stringExtra = (String) savedInstanceState.getSerializable("SOUNDTYPE_STR");
        }

        return stringExtra;
    }
}