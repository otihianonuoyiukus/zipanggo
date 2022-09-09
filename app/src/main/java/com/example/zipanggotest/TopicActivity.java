package com.example.zipanggotest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import org.sufficientlysecure.htmltextview.HtmlAssetsImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.lang.reflect.Field;

public class TopicActivity extends AppCompatActivity {
    //Default value of extraTopic
    private String extraTopic = "lesson_japanese_internet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        HtmlTextView html = findViewById(R.id.topic_rl_htmltv_content);
        extraTopic = getStringExtra(savedInstanceState, "TOPICNAME_STR");
        int stringId = 0;
        try {
            Class res = R.string.class;
            Field field = res.getField(extraTopic);
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
                stringExtra = extra.getString("TOPICNAME_STR");
            }
        } else {
            stringExtra = (String) savedInstanceState.getSerializable("TOPICNAME_STR");
        }

        return stringExtra;
    }
}