package com.example.zipanggotest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.example.zipanggotest.games.MissingKanjiGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MissingKanjiActivity extends AppCompatActivity {
    private String difficulty = "easy";
    private List<String> wordList;

    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private String currentAcc;
    private String gameName;
    private List<String> scoreList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missing_kanji);

        sharedpreferences = getSharedPreferences("Account", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        currentAcc = sharedpreferences.getString("currentAccount", null);

        TextView scoreLabel = findViewById(R.id.missingkanji_tv_bestscore);
        TextView scoreValue = findViewById(R.id.missingkanji_tv_bestscore_value);
        TextView gameTitle = findViewById(R.id.missingkanji_tv_title);
        gameTitle.setText(R.string.menu_game_button_missingkanji_header);

        gameName = currentAcc + "_missing_" + difficulty;
        String scoreCSV = sharedpreferences.getString(gameName, null);
        if(scoreCSV != null) {
            scoreList = new ArrayList<>(Arrays.asList(scoreCSV.split(",")));
            scoreValue.setText(scoreList.get(0));
        } else {
            scoreList = new ArrayList<>();
            scoreValue.setText("0");
        }

        TextView gameDesc = findViewById(R.id.missingkanji_tv_description);
        gameDesc.setText(R.string.menu_game_button_missingkanji_desc);

        RadioGroup difficultyGroup = findViewById(R.id.missingkanji_rg_radiogroup);
        difficultyGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton difficultyButton = findViewById(difficultyGroup.getCheckedRadioButtonId());
                difficulty = ((String) difficultyButton.getText()).toLowerCase();
                scoreLabel.setText(difficulty.toUpperCase() + " BEST: ");
                gameName = currentAcc + "_missing_" + difficulty;
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

        ViewSwitcher viewSwitcher = findViewById(R.id.missingkanji_vs_viewswitcher);
        Animation in = AnimationUtils.loadAnimation(
                MissingKanjiActivity.this,
                android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(
                MissingKanjiActivity.this,
                android.R.anim.slide_out_right);
        viewSwitcher.setInAnimation(in);
        viewSwitcher.setOutAnimation(out);

        Activity activity = this;

        Button playButton = findViewById(R.id.missingkanji_b_playbutton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Add more words
                switch(difficulty) {
                    case "easy":
                        wordList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.missingkanji_easy_words)));
                        viewSwitcher.showNext();
                        break;

                    case "medium":
                        viewSwitcher.showNext();
                        break;

                    case "hard":
                        viewSwitcher.showNext();
                        break;

                    default:
                        viewSwitcher.showNext();
                        break;
                }
                MissingKanjiGame missingKanjiGame = new MissingKanjiGame(activity, wordList, difficulty);
            }
        });
    }
}