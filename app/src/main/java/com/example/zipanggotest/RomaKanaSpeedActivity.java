package com.example.zipanggotest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zipanggotest.adapters.RomaKanaSpeedGridViewAdapter;
import com.example.zipanggotest.games.RomaKanaSpeedGame;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class RomaKanaSpeedActivity extends AppCompatActivity {
    private final int KANA_MAXSIZE = 25;
    private String systemType = "hiragana";

    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private String currentAcc;
    private String gameName;
    private List<String> scoreList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roma_kana_speed);

        sharedpreferences = getSharedPreferences("Account", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        currentAcc = sharedpreferences.getString("currentAccount", null);

        TextView scoreLabel = findViewById(R.id.game_menu_rl_tv_bestscore);
        TextView scoreValue = findViewById(R.id.game_menu_rl_tv_bestscore_value);
        TextView gameTitle = findViewById(R.id.game_menu_rl_tv_title);
        gameTitle.setText(R.string.menu_game_button_romakanaspeed_header);

        gameName = currentAcc + "_speed_" + systemType;
        String scoreCSV = sharedpreferences.getString(gameName, null);
        if(scoreCSV != null) {
            scoreList = new ArrayList<>(Arrays.asList(scoreCSV.split(",")));
            scoreValue.setText(scoreList.get(0));
        } else {
            scoreList = new ArrayList<>();
            scoreValue.setText("0");
        }

        TextView gameDesc = findViewById(R.id.game_menu_rl_tv_description);
        gameDesc.setText(R.string.menu_game_button_romakanaspeed_desc);

        RadioGroup systemGroup = findViewById(R.id.game_menu_rl_rg_radiogroup);
        systemGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton systemButton = findViewById(systemGroup.getCheckedRadioButtonId());
                systemType = ((String) systemButton.getText()).toLowerCase();
                scoreLabel.setText(systemType.toUpperCase() + " BEST: ");
                gameName = currentAcc + "_speed_" + systemType;
                String scoreCSV = sharedpreferences.getString(gameName, null);
                if(scoreCSV != null) {
                    scoreList = new ArrayList<>(Arrays.asList(scoreCSV.split(",")));
                    scoreValue.setText(scoreList.get(0));
                } else {
                    scoreList = new ArrayList<>();
                    scoreValue.setText("0");
                }
            }
        });

        ViewSwitcher viewSwitcher = findViewById(R.id.romakana_speed_rl_vs_viewswitcher);
        Animation in = AnimationUtils.loadAnimation(
                            RomaKanaSpeedActivity.this,
                            android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(
                            RomaKanaSpeedActivity.this,
                            android.R.anim.slide_out_right);
        viewSwitcher.setInAnimation(in);
        viewSwitcher.setOutAnimation(out);

        Button playButton = findViewById(R.id.game_menu_rl_b_playbutton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Kana> kanaList = new ArrayList<>();
                KanaDataXMLParser kanaXML = new KanaDataXMLParser(systemType, new String[] {"gojuuon"});
                try {
                    kanaList =
                            new ArrayList<>(kanaXML.kanaReadFeed(getResources().getXml(R.xml.kana_data)));
                    for (Iterator<Kana> iterator = kanaList.iterator(); iterator.hasNext(); ) {
                        Kana hc = iterator.next();
                        if (hc.getCharacter() == null)
                            iterator.remove();
                    }
                    Collections.shuffle(kanaList);
                    kanaList = kanaList.subList(0, KANA_MAXSIZE);
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                GridView kanaGrid = findViewById(R.id.romakana_speed_rl_gv_kanagrid);
                RomaKanaSpeedGridViewAdapter romaKanaSpeedGridViewAdapter =
                        new RomaKanaSpeedGridViewAdapter(RomaKanaSpeedActivity.this, kanaList);
                kanaGrid.setAdapter(romaKanaSpeedGridViewAdapter);
                RomaKanaSpeedGame romaKanaSpeedGame =
                        new RomaKanaSpeedGame(RomaKanaSpeedActivity.this, kanaList, systemType);
                romaKanaSpeedGridViewAdapter.setRomaKanaSpeedGame(romaKanaSpeedGame);

                viewSwitcher.showNext();
            }
        });
    }
}