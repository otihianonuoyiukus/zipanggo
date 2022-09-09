package com.example.zipanggotest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.sufficientlysecure.htmltextview.HtmlAssetsImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.lang.reflect.Field;

public class ReadingExerciseActivity extends AppCompatActivity {
    //Default value of extraTitle
    private String extraTitle = "readingexercise_momotaro";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_exercise);

        HtmlTextView html = findViewById(R.id.readingexercise_rl_htmltv_content);
        extraTitle = getStringExtra(savedInstanceState, "STORYTITLE_STR");
        int stringId = 0;
        try {
            Class res = R.string.class;
            Field field = res.getField(extraTitle);
            stringId = field.getInt(null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        html.setHtml(getResources().getString(stringId), new HtmlAssetsImageGetter(html));
    }

    private String getStringExtra(Bundle savedInstanceState, String key) {
        String stringExtra = "";

        if (savedInstanceState == null) {
            Bundle extra = getIntent().getExtras();
            if(extra == null) {
                stringExtra = null;
            } else {
                stringExtra = extra.getString("STORYTITLE_STR");
            }
        } else {
            stringExtra = (String) savedInstanceState.getSerializable("STORYTITLE_STR");
        }

        return stringExtra;
    }
}