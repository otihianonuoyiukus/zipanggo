package com.example.zipanggotest.games;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.zipanggotest.MenuActivity;
import com.example.zipanggotest.MissingKanjiActivity;
import com.example.zipanggotest.R;
import com.example.zipanggotest.RomaKanaMatchActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MissingKanjiGame {
    private static final int DEFAULT_TIME = 60000;
    private static final int TIME_INTERVAL = 1000;
    private static final int ADD_TIME = 5000;
    private static final int MINUS_TIME = 3000;
    private static final int MAX_CHOICE_SIZE = 6;

    private final Activity activity;
    private final AnimatorSet gameCorrectAnim, gameWrongAnim;
    private final String difficulty;

    private List<String> wordList;
    private TextView timerTextView, scoreTextView, furiganaTextView, kanjiTextView, meaningTextView;
    private MaterialButton kanjiBtn0, kanjiBtn1, kanjiBtn2, kanjiBtn3, kanjiBtn4, kanjiBtn5;
    private List<MaterialButton> kanjiBtnList = new ArrayList<>(6);

    private int score, currentTime;
    private CountDownTimer countDownTimer;
    private String currentWord, currentAnswer;
    private List<String> randomKanjiList;

    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private String currentAcc;
    private String gameName;
    private List<String> scoreList;
    private List<Integer> scoreListInt;
    private List<String> scoreListBest;

    public MissingKanjiGame(Activity activity, List<String> wordList, String difficulty) {
        this.activity = activity;
        this.wordList = wordList;
        this.difficulty = difficulty;
        this.timerTextView = activity.findViewById(R.id.missingkanji_tv_timer);
        this.scoreTextView = activity.findViewById(R.id.missingkanji_tv_score);
        this.furiganaTextView = activity.findViewById(R.id.missingkanji_tv_kanjifurigana);
        this.kanjiTextView = activity.findViewById(R.id.missingkanji_tv_kanjiword);
        this.meaningTextView = activity.findViewById(R.id.missingkanji_tv_kanjimeaning);
        this.kanjiBtn0 = activity.findViewById(R.id.missingkanji_mb_kanjibtn0);
        this.kanjiBtn0.setOnClickListener(new ListenerClass());
        this.kanjiBtn1 = activity.findViewById(R.id.missingkanji_mb_kanjibtn1);
        this.kanjiBtn1.setOnClickListener(new ListenerClass());
        this.kanjiBtn2 = activity.findViewById(R.id.missingkanji_mb_kanjibtn2);
        this.kanjiBtn2.setOnClickListener(new ListenerClass());
        this.kanjiBtn3 = activity.findViewById(R.id.missingkanji_mb_kanjibtn3);
        this.kanjiBtn3.setOnClickListener(new ListenerClass());
        this.kanjiBtn4 = activity.findViewById(R.id.missingkanji_mb_kanjibtn4);
        this.kanjiBtn4.setOnClickListener(new ListenerClass());
        this.kanjiBtn5 = activity.findViewById(R.id.missingkanji_mb_kanjibtn5);
        this.kanjiBtn5.setOnClickListener(new ListenerClass());
        this.kanjiBtnList.add(this.kanjiBtn0);
        this.kanjiBtnList.add(this.kanjiBtn1);
        this.kanjiBtnList.add(this.kanjiBtn2);
        this.kanjiBtnList.add(this.kanjiBtn3);
        this.kanjiBtnList.add(this.kanjiBtn4);
        this.kanjiBtnList.add(this.kanjiBtn5);
        this.randomKanjiList = new ArrayList<>(Arrays.asList(activity.getResources().getStringArray(R.array.missingkanji_randomkanji)));
        this.countDownTimer = null;
        gameCorrectAnim = (AnimatorSet) AnimatorInflater.loadAnimator(activity,
                R.animator.missingkanji_kanjicharacter_correct);

        gameWrongAnim = (AnimatorSet) AnimatorInflater.loadAnimator(activity,
                R.animator.missingkanji_kanjicharacter_wrong);

        sharedpreferences = activity.getSharedPreferences("Account", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        currentAcc = sharedpreferences.getString("currentAccount", null);
        gameName = currentAcc + "_missing_" + difficulty;
        String scoreCSV = sharedpreferences.getString(gameName, null);
        if(scoreCSV != null) {
            scoreList = new ArrayList<>(Arrays.asList(scoreCSV.split(",")));
        } else {
            scoreList = new ArrayList<>();
        }

        initGame();
    }

    @SuppressLint("SetTextI18n")
    public void initGame() {
        score = 0;
        currentTime = DEFAULT_TIME / 1000;
        scoreTextView.setText(Integer.toString(score));
        timerTextView.setText(Integer.toString(DEFAULT_TIME / 1000));
        generateQuestion();
        initTimer();
    }

    public void initTimer() {
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(currentTime * 1000, TIME_INTERVAL) {
                @SuppressLint("SetTextI18n")
                public void onTick(long millisUntilFinished) {
                    currentTime = (int) millisUntilFinished / 1000;
                    timerTextView.setText(Integer.toString(currentTime));
                }
                public void onFinish() {
                    timerTextView.setText(Integer.toString(0));
                    if(!activity.isFinishing())
                        endGame();
                }
            }.start();
        }
    }

    public void generateQuestion() {
        String newWord;
        List<String> currentWordList = null;
        if(currentWord == null)
            currentWord = wordList.get((int) Math.floor(Math.random()*(wordList.size()) + 0));
        else {
            do newWord = wordList.get((int) Math.floor(Math.random()*(wordList.size()) + 0));
            while(currentWord.equals(newWord));
            currentWord = newWord;
        }
        currentWordList = new ArrayList<>(Arrays.asList(currentWord.split(",")));
        currentAnswer = currentWordList.get(4);
        furiganaTextView.setText(currentWordList.get(2));
        kanjiTextView.setText(currentWordList.get(3));
        meaningTextView.setText(currentWordList.get(0));
        Collections.shuffle(randomKanjiList);
        List<String> randomKanjiShortList = new ArrayList<>(randomKanjiList);
        randomKanjiShortList.subList(MAX_CHOICE_SIZE - 1, randomKanjiList.size()).clear();
        randomKanjiShortList.add(currentWordList.get(4));
        Collections.shuffle(randomKanjiShortList);
        //TODO: Make sure there's no duplicate kanji characters in the choices
        for (int i = 0; i < MAX_CHOICE_SIZE; i++) {
            kanjiBtnList.get(i).setText(randomKanjiShortList.get(i));
        }
        initTimer();
    }

    @SuppressLint({"SetTextI18n", "InflateParams"})
    public void endGame() {
        try {
            countDownTimer.cancel();
        } catch (NullPointerException e) {
            //e.printStackTrace();
        }
        countDownTimer = null;

        scoreList.add(Integer.toString(score));
        scoreListInt = getIntegerArray(scoreList);
        Collections.sort(scoreListInt);
        scoreList = getStringArray(scoreListInt);
        Collections.reverse(scoreList);
        if(scoreList.size() > 10) {
            scoreList = scoreList.subList(0, 10);
        }
        String scoreCSV = TextUtils.join(",", scoreList);
        editor.putString(gameName, scoreCSV).commit();

        View customTitle = activity.getLayoutInflater()
                .inflate(R.layout.gamemeover_dialog_header_layout, null);
        MaterialAlertDialogBuilder gameOverDialog = new MaterialAlertDialogBuilder(activity);
        View view = activity.getLayoutInflater().inflate(R.layout.gameover_dialog_message_layout, null);
        gameOverDialog.setCustomTitle(customTitle);
        gameOverDialog.setView(view);

        TextView finalScore = view.findViewById(R.id.gameover_dialog_rl_tv_finalscore);
        finalScore.setText(Integer.toString(score));

        TextView bestScore = view.findViewById(R.id.gameover_dialog_ll_tv_scorebest);
        gameName = currentAcc + "_missing_" + difficulty;
        String scoreListCSV = sharedpreferences.getString(gameName, null);
        if(scoreListCSV != null) {
            scoreListBest = new ArrayList<>(Arrays.asList(scoreCSV.split(",")));
            bestScore.setText(scoreListBest.get(0));
        } else {
            scoreListBest = new ArrayList<>();
            bestScore.setText("0");
        }

        gameOverDialog.setNeutralButton("MENU", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(activity, MenuActivity.class);
                activity.finish();
                activity.startActivity(intent);
            }
        });
        gameOverDialog.setNegativeButton("RETRY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(activity, MissingKanjiActivity.class);
                activity.finish();
                activity.startActivity(intent);
            }
        });
        gameOverDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Intent intent = new Intent(activity, MenuActivity.class);
                activity.finish();
                activity.startActivity(intent);
            }
        });

        gameOverDialog.show();
    }

    //TODO: Probably give it a better name?
    private class ListenerClass implements View.OnClickListener{
        @SuppressLint("SetTextI18n")
        @Override
        public void onClick(View v) {
            TextView kanjiBtn = (TextView) v;
            if(kanjiBtn.getText().equals(currentAnswer)) {
                score++;
                scoreTextView.setText(Integer.toString(score));
                countDownTimer.cancel();
                countDownTimer = null;
                currentTime += ADD_TIME / 1000;
                timerTextView.setText(Integer.toString(currentTime));
                gameCorrectAnim.setTarget(kanjiTextView);
                gameCorrectAnim.start();
            } else {
                score--;
                scoreTextView.setText(Integer.toString(score));
                countDownTimer.cancel();
                countDownTimer = null;
                currentTime -= MINUS_TIME / 1000;
                timerTextView.setText(Integer.toString(currentTime));
                gameWrongAnim.setTarget(kanjiTextView);
                gameWrongAnim.start();
            }
            generateQuestion();
        }
    }

    private List<Integer> getIntegerArray(List<String> stringArray) {
        List<Integer> result = new ArrayList<>();
        for(String stringValue : stringArray) {
            try {
                //Convert String to Integer, and store it into integer array list.
                result.add(Integer.parseInt(stringValue));
            } catch(NumberFormatException nfe) {
                //System.out.println("Could not parse " + nfe);
            }
        }
        return result;
    }

    private List<String> getStringArray(List<Integer> intArray) {
        List<String> result = new ArrayList<>();
        for(Integer intValue : intArray) {
            //Convert Integer to String, and store it into integer array list.
            result.add(Integer.toString(intValue));
        }
        return result;
    }
}
