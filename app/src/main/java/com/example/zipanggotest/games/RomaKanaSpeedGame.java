package com.example.zipanggotest.games;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zipanggotest.Kana;
import com.example.zipanggotest.KanaDataXMLParser;
import com.example.zipanggotest.MenuActivity;
import com.example.zipanggotest.R;
import com.example.zipanggotest.RomaKanaSpeedActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class RomaKanaSpeedGame {
    private static final int DEFAULT_TIME = 60000;
    private static final int TIME_INTERVAL = 1000;
    private static final int ADD_TIME = 30000;
    private static final int KANA_MAXSIZE = 25;

    private final Context context;
    private final String systemType;
    private final TextView scoreTextView, timerTextView, transTextView;
    private final AnimatorSet cardFadeOutAnim;
    private final List<String> transliterationList;

    private int score;
    private int currentTime;
    private String transliteration;
    private List<Kana> kanaList;
    private CountDownTimer countDownTimer = null;

    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private String currentAcc;
    private String gameName;
    private List<String> scoreList;
    private List<Integer> scoreListInt;
    private List<String> scoreListBest;


    public RomaKanaSpeedGame(Context context, List<Kana> kanaList, String systemType) {
        this.context = context;
        this.scoreTextView =
                ((RomaKanaSpeedActivity) context).findViewById(R.id.romakana_speed_rl_tv_score);
        this.transTextView =
                ((RomaKanaSpeedActivity) context).findViewById(R.id.romakana_speed_rl_tv_romaji);
        this.timerTextView =
                ((RomaKanaSpeedActivity) context).findViewById(R.id.romakana_speed_rl_tv_timer);
        this.kanaList = kanaList;
        this.systemType = systemType;
        this.transliterationList = new ArrayList<>(KANA_MAXSIZE);
        for(Kana kana : kanaList) {
            transliterationList.add(kana.getTransliterationList().get(0));
        }
        this.cardFadeOutAnim =
                (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_fade_out);

        sharedpreferences = context.getSharedPreferences("Account", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        currentAcc = sharedpreferences.getString("currentAccount", null);
        gameName = currentAcc + "_speed_" + systemType;
        String scoreCSV = sharedpreferences.getString(gameName, null);
        if(scoreCSV != null) {
            scoreList = new ArrayList<>(Arrays.asList(scoreCSV.split(",")));
        } else {
            scoreList = new ArrayList<>();
        }

        initGame();
    }

    @SuppressLint("SetTextI18n")
    private void initGame() {
        score = 0;
        currentTime = DEFAULT_TIME / 1000;
        scoreTextView.setText(Integer.toString(score));
        timerTextView.setText(Integer.toString(DEFAULT_TIME / 1000));
        setNewTransliteration();
    }

    @SuppressLint("SetTextI18n")
    public void onCardClick(View view, String transliteration) {
        MaterialButton card = (MaterialButton) view;

        // Creates and starts the timer if the condition is met
        //
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(currentTime * 1000, TIME_INTERVAL) {
                public void onTick(long millisUntilFinished) {
                    currentTime = (int) millisUntilFinished / 1000;
                    timerTextView.setText(Integer.toString(currentTime));
                }

                public void onFinish() {
                    timerTextView.setText(Integer.toString(0));
                    if(!((RomaKanaSpeedActivity) context).isFinishing())
                        endGame();
                }
            }.start();
        }

        if(transliteration.equalsIgnoreCase(this.transliteration)) {
            cardFadeOutAnim.setTarget(card);
            cardFadeOutAnim.start();
            transliterationList.remove(transliteration);
            if (transliterationList.size() > 0)
                setNewTransliteration();
            scoreTextView.setText(Integer.toString(++score));

            if (score >= 100) {
                editor.putBoolean(currentAcc + "_roma2kanaspeedadvanced", true).commit();
                initAchievementDialog("Roma2Kana Speed Advanced");
            } else if (score >= 50) {
                editor.putBoolean(currentAcc + "_roma2kanaspeedintermediate", true).commit();
                initAchievementDialog("Roma2Kana Speed Intermediate");
            } else if (score >= 25 && !sharedpreferences.getBoolean(currentAcc + "_roma2kanaspeedbeginner", false)) {
                editor.putBoolean(currentAcc + "_roma2kanaspeedbeginner", true).commit();
                initAchievementDialog("Roma2Kana Speed Beginner");
            }
        }
    }

    private void setNewTransliteration() {
        transliteration =
                transliterationList.get(new Random().nextInt(transliterationList.size()));
        transTextView.setText(transliteration);
    }

    @SuppressLint("SetTextI18n")
    public void generateCards() {
        // Pauses and adds additional 30 seconds to the timer
        //
        countDownTimer.cancel();
        countDownTimer = null;
        currentTime += ADD_TIME / 1000;
        timerTextView.setText(Integer.toString(currentTime));

        // Creates a new list for 25 Kana characters and shuffles them
        //
        kanaList = null;
        kanaList = new ArrayList<>();
        KanaDataXMLParser kanaXML = new KanaDataXMLParser(systemType, new String[] {"gojuuon"});
        try {
            kanaList =
                    new ArrayList<>(
                            kanaXML.kanaReadFeed(context.getResources().getXml(R.xml.kana_data))
                    );
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
        for(Kana kana : kanaList) {
            transliterationList.add(kana.getTransliterationList().get(0));
        }
        setNewTransliteration();
    }

    @SuppressLint({"SetTextI18n", "InflateParams"})
    private void endGame() {
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

        View customTitle = ((RomaKanaSpeedActivity) context).getLayoutInflater()
                .inflate(R.layout.gamemeover_dialog_header_layout, null);
        MaterialAlertDialogBuilder gameOverDialog = new MaterialAlertDialogBuilder(context);
        View view =
                ((RomaKanaSpeedActivity) context)
                        .getLayoutInflater()
                                .inflate(R.layout.gameover_dialog_message_layout, null);
        gameOverDialog.setCustomTitle(customTitle);
        gameOverDialog.setView(view);

        TextView finalScore = view.findViewById(R.id.gameover_dialog_rl_tv_finalscore);
        finalScore.setText(Integer.toString(score));

        TextView bestScore = view.findViewById(R.id.gameover_dialog_ll_tv_scorebest);
        gameName = currentAcc + "_speed_" + systemType;
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
                Intent intent = new Intent(context, MenuActivity.class);
                ((RomaKanaSpeedActivity) context).finish();
                context.startActivity(intent);
            }
        });
        gameOverDialog.setNegativeButton("RETRY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(context, RomaKanaSpeedActivity.class);
                ((RomaKanaSpeedActivity) context).finish();
                context.startActivity(intent);
            }
        });
        gameOverDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Intent intent = new Intent(context, MenuActivity.class);
                ((RomaKanaSpeedActivity) context).finish();
                context.startActivity(intent);
            }
        });

        gameOverDialog.show();
    }

    private void initAchievementDialog(String achievementName) {
        View customTitle = ((RomaKanaSpeedActivity) context).getLayoutInflater()
                .inflate(R.layout.gamemeover_dialog_header_layout, null);
        MaterialAlertDialogBuilder achievementDialog = new MaterialAlertDialogBuilder(context);
        View view =
                ((RomaKanaSpeedActivity) context)
                        .getLayoutInflater()
                        .inflate(R.layout.achievement_dialog_message_layout, null);
        achievementDialog.setCustomTitle(customTitle);
        achievementDialog.setView(view);

        achievementDialog.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        TextView achievement = view.findViewById(R.id.achievement_dialog_ll_tv_achievementname);
        achievement.setText(achievementName);
        achievementDialog.show();
    }

    public boolean isCardGridEmpty() {
        return !(transliterationList.size() > 0);
    }

    public List<Kana> getKanaList() {
        return kanaList;
    }

    public AnimatorSet getCardFadeOutAnim() {
        return cardFadeOutAnim;
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