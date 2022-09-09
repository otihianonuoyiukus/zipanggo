package com.example.zipanggotest;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    private List<Kana> kanaCharacterList;
    private Quiz quiz;
    private TextView character, progress;
    private LinearProgressIndicator progressBar;
    private TextInputEditText answer;
    private AnimatorSet quizCorrectAnim, quizWrongAnim, quizCharFadeInAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        character = findViewById(R.id.activity_quiz_tv_character);
        progress = findViewById(R.id.activity_quiz_tv_progress);
        progressBar = findViewById(R.id.activity_quiz_pi_progressbar);
        answer = findViewById(R.id.activity_quiz_tf_input);
        answer.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE && !v.getText().toString().isEmpty()) {
                    if (quiz.onAnswer(v.getText().toString())) {
                        // If the answer is correct, do this
                        //
                        quizCorrectAnim.setTarget(character);
                        quizCorrectAnim.start();
                    } else {
                        // If the answer is wrong, do this
                        //
                        quizWrongAnim.setTarget(character);
                        quizWrongAnim.start();
                    }
                    v.setText("");
                    //onAnswerChange();
                    return true;
                }
                return false;
            }
        });

        quizCorrectAnim = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),
                R.animator.quiz_character_correct);
        quizCorrectAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onAnimationEnd(Animator animation) {
                onAnswerChange();
                quizCharFadeInAnim.setTarget(character);
                quizCharFadeInAnim.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        quizWrongAnim = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),
                R.animator.quiz_character_wrong);
        quizWrongAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onAnimationEnd(Animator animation) {
                onAnswerChange();
                quizCharFadeInAnim.setTarget(character);
                quizCharFadeInAnim.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        quizCharFadeInAnim = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),
                R.animator.quiz_character_fadein);

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

        quiz = new Quiz(QuizActivity.this, kanaCharacterList, Arrays.asList(soundTypeArr), extraSystem.toLowerCase());
            onAnswerChange();
    }

    // Updates the user interface after an answer has been inputted
    //
    public void onAnswerChange() {
        character.setText(quiz.getQuizQuestion().get(quiz.getCurrentQuestionNum() - 1));
        progress.setText(quiz.getCurrentPosition());
        progressBar.setMax(quiz.getTotalQuestion());
        progressBar.setProgress(quiz.getCurrentQuestionNum());
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