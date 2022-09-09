package com.example.zipanggotest.games;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zipanggotest.Kana;
import com.example.zipanggotest.KanaDataXMLParser;
import com.example.zipanggotest.MenuActivity;
import com.example.zipanggotest.R;
import com.example.zipanggotest.RomaKanaMatchActivity;
import com.example.zipanggotest.RomaKanaSpeedActivity;
import com.example.zipanggotest.adapters.RomaKanaMatchGridViewAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class RomaKanaMatchGame {
    private enum CardPosition {
        FIRST, SECOND;
    }

    private static final int DEFAULT_TIME = 60000;
    private static final int TIME_INTERVAL = 1000;
    private static final int ADD_TIME = 30000;
    private static final int KANA_MAXSIZE = 16;

    private final Context context;
    private final String systemType;
    private final TextView scoreTextView, timerTextView;
    private final List<List<MaterialButton>> flippedCardList;
    private final List<String> keyList;
    private final AnimatorSet firstCardOutSelectAnim, firstCardInSelectAnim,
                            secondCardOutSelectAnim, secondCardInSelectAnim;
    private final AnimatorSet firstCardOutUnselectAnim, firstCardInUnselectAnim,
                            secondCardOutUnselectAnim, secondCardInUnselectAnim;
    private final AnimatorSet firstCardFadeOutAnim, secondCardFadeOutAnim;

    private int score, currentTime, counter;
    private List<Kana> kanaList;
    private List<View> cardList;
    private MaterialButton previousCard;
    private CountDownTimer countDownTimer;
    private RomaKanaMatchGridViewAdapter adapter;

    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private String currentAcc;
    private String gameName;
    private List<String> scoreList;
    private List<Integer> scoreListInt;
    private List<String> scoreListBest;

    public RomaKanaMatchGame(Context context, String systemType) {
        this.context = context;
        this.kanaList = new ArrayList<>();
        this.systemType = systemType;
        this.scoreTextView =
                ((RomaKanaMatchActivity) context).findViewById(R.id.romakana_match_rl_tv_score);
        this.timerTextView =
                ((RomaKanaMatchActivity) context).findViewById(R.id.romakana_match_rl_tv_timer);
        this.flippedCardList = new ArrayList<>(2);
        this.keyList = new ArrayList<>(2);
        this.countDownTimer = null;
        // God in Heaven, holy be thy name...
        //
        this.firstCardOutSelectAnim = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_out);
        this.firstCardInSelectAnim = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_in);
        this.secondCardOutSelectAnim = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_out);
        this.secondCardInSelectAnim = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_in);
        this.firstCardOutUnselectAnim = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_out);
        this.firstCardInUnselectAnim = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_in);
        this.secondCardOutUnselectAnim = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_out);
        this.secondCardInUnselectAnim = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_in);
        this.firstCardFadeOutAnim = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_fade_out);
        this.secondCardFadeOutAnim = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_fade_out);

        sharedpreferences = context.getSharedPreferences("Account", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        currentAcc = sharedpreferences.getString("currentAccount", null);
        gameName = currentAcc + "_match_" + systemType;
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
        generateCards();
    }

    //つらっ
    @SuppressLint("SetTextI18n")
    public void onCardClick(View view, int position) {
        MaterialButton frontCard = view.findViewById(R.id.romakana_match_rl_b_frontcard);
        MaterialButton backCard = view.findViewById(R.id.romakana_match_rl_b_backcard);

        // Creates and starts the timer if the condition is met
        //
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(currentTime * 1000, TIME_INTERVAL) {
                @SuppressLint("SetTextI18n")
                public void onTick(long millisUntilFinished) {
                    currentTime = (int) millisUntilFinished / 1000;
                    timerTextView.setText(Integer.toString(currentTime));
                }
                public void onFinish() {
                    timerTextView.setText(Integer.toString(0));
                    if(!((RomaKanaMatchActivity) context).isFinishing())
                        endGame();
                }
            }.start();
        }
        List<MaterialButton> cardSides = new ArrayList<>();
        if(flippedCardList.size() == 1) {
            cardSides.add(frontCard);
            cardSides.add(backCard);
            flippedCardList.add(cardSides);
            keyList.add(kanaList.get(position).getCharacter());
            if(previousCard.equals(frontCard)) {
                onUnselectCard(frontCard, backCard, CardPosition.SECOND);
                cardSides.clear();
                flippedCardList.clear();
                keyList.clear();
            } else {
                onSelectCard(frontCard, backCard, CardPosition.SECOND);
            }
        } else if(flippedCardList.size() == 0) {
            cardSides.add(frontCard);
            cardSides.add(backCard);
            flippedCardList.add(cardSides);
            keyList.add(kanaList.get(position).getCharacter());
            previousCard = flippedCardList.get(0).get(0);
            onSelectCard(frontCard, backCard, CardPosition.FIRST);
        }

        firstCardOutSelectAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                for(int i = 0; i < adapter.getCount(); i++)
                    adapter.getItem(i).setClickable(false);
            }
        });

        firstCardInSelectAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                for(int i = 0; i < adapter.getCount(); i++)
                    adapter.getItem(i).setClickable(true);
            }
        });

        firstCardOutUnselectAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                for(int i = 0; i < adapter.getCount(); i++)
                    adapter.getItem(i).setClickable(false);
            }
        });

        firstCardInUnselectAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                for(int i = 0; i < adapter.getCount(); i++)
                    adapter.getItem(i).setClickable(true);
            }
        });

        secondCardOutUnselectAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                for(int i = 0; i < adapter.getCount(); i++)
                    adapter.getItem(i).setClickable(false);
            }
        });

        secondCardInUnselectAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                for(int i = 0; i < adapter.getCount(); i++)
                    adapter.getItem(i).setClickable(true);

                if(counter == (KANA_MAXSIZE / 2))
                    endRound();
            }
        });

        secondCardOutSelectAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                for(int i = 0; i < adapter.getCount(); i++)
                    adapter.getItem(i).setClickable(false);
            }
        });

        secondCardInSelectAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(flippedCardList.size() >= 2){
                    onUnselectCard(flippedCardList.get(0).get(0), flippedCardList.get(0).get(1), CardPosition.FIRST);
                    onUnselectCard(flippedCardList.get(1).get(0), flippedCardList.get(1).get(1), CardPosition.SECOND);
                    if(isCardSame(keyList.get(0), keyList.get(1))) {
                        score++;
                        counter++;
                        scoreTextView.setText(Integer.toString(score));
                        View firstViewLayout = (View) flippedCardList.get(0).get(0).getParent();
                        View secondViewLayout = (View) flippedCardList.get(1).get(0).getParent();
                        firstViewLayout.setEnabled(false);
                        secondViewLayout.setEnabled(false);
                        firstCardFadeOutAnim.setTarget(firstViewLayout);
                        secondCardFadeOutAnim.setTarget(secondViewLayout);
                        firstCardFadeOutAnim.start();
                        secondCardFadeOutAnim.start();
                    }
                    cardSides.clear();
                    flippedCardList.clear();
                    keyList.clear();
                }
                for(int i = 0; i < adapter.getCount(); i++)
                    adapter.getItem(i).setClickable(true);
            }
        });
    }

    private void onSelectCard(MaterialButton frontCard, MaterialButton backCard, CardPosition position) {
        if(position == CardPosition.FIRST){
            firstCardOutSelectAnim.setTarget(backCard);
            firstCardInSelectAnim.setTarget(frontCard);
            firstCardOutSelectAnim.start();
            firstCardInSelectAnim.start();
        } else {
            secondCardOutSelectAnim.setTarget(backCard);
            secondCardInSelectAnim.setTarget(frontCard);
            secondCardOutSelectAnim.start();
            secondCardInSelectAnim.start();
        }
    }

    private void onUnselectCard(MaterialButton frontCard, MaterialButton backCard, CardPosition position) {
        if(position == CardPosition.FIRST){
            firstCardOutUnselectAnim.setTarget(frontCard);
            firstCardInUnselectAnim.setTarget(backCard);
            firstCardOutUnselectAnim.start();
            firstCardInUnselectAnim.start();
        } else {
            secondCardOutUnselectAnim.setTarget(frontCard);
            secondCardInUnselectAnim.setTarget(backCard);
            secondCardOutUnselectAnim.start();
            secondCardInUnselectAnim.start();
        }
    }

    @SuppressLint("SetTextI18n")
    private void generateCards() {
        if(countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
            currentTime += ADD_TIME / 1000;
            timerTextView.setText(Integer.toString(currentTime));
        }

        KanaDataXMLParser kanaXML = new KanaDataXMLParser(systemType, new String[] {"gojuuon"});
        try {
            kanaList =
                    kanaXML.kanaReadFeed(context.getResources().getXml(R.xml.kana_data));
            for (Iterator<Kana> iterator = kanaList.iterator(); iterator.hasNext(); ) {
                Kana k = iterator.next();
                if (k.getCharacter() == null)
                    iterator.remove();
            }
            Collections.shuffle(kanaList);
            kanaList = kanaList.subList(0, KANA_MAXSIZE / 2);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        kanaList.addAll(new ArrayList<>(kanaList));
    }

    private void endRound() {
        counter = 0;
        generateCards();
        List<String> characterList = new ArrayList<>(8);
        List<String> transliterationList = new ArrayList<>(8);
        for(int i = 0; i < (kanaList.size() / 2); i++) {
            characterList.add(kanaList.get(i).getCharacter());
            transliterationList.add(kanaList.get(i).getTransliterationList().get(0));
        }
        List<String> cardList = new ArrayList<>(characterList);
        cardList.addAll(transliterationList);
        long systemMilli = System.currentTimeMillis();
        Collections.shuffle(kanaList, new Random(systemMilli));
        Collections.shuffle(cardList, new Random(systemMilli));

        for(int i = 0; i < KANA_MAXSIZE; i++) {
            View view = adapter.getItem(i);
            MaterialButton button = (MaterialButton) ((RelativeLayout) view).getChildAt(0);
            button.setText(cardList.get(i));
            view.setEnabled(true);
            AnimatorSet cardFadeInAnim = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_fade_in);
            cardFadeInAnim.setTarget(view);
            cardFadeInAnim.start();
        }
    }

    @SuppressLint("SetTextI18n")
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

        View customTitle = ((RomaKanaMatchActivity) context).getLayoutInflater()
                .inflate(R.layout.gamemeover_dialog_header_layout, null);
        MaterialAlertDialogBuilder gameOverDialog = new MaterialAlertDialogBuilder(context);
        View view =
                ((RomaKanaMatchActivity) context)
                        .getLayoutInflater()
                        .inflate(R.layout.gameover_dialog_message_layout, null);
        gameOverDialog.setCustomTitle(customTitle);
        gameOverDialog.setView(view);

        TextView finalScore = view.findViewById(R.id.gameover_dialog_rl_tv_finalscore);
        finalScore.setText(Integer.toString(score));

        TextView bestScore = view.findViewById(R.id.gameover_dialog_ll_tv_scorebest);
        gameName = currentAcc + "_match_" + systemType;
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
                ((RomaKanaMatchActivity) context).finish();
                context.startActivity(intent);
            }
        });
        gameOverDialog.setNegativeButton("RETRY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(context, RomaKanaMatchActivity.class);
                ((RomaKanaMatchActivity) context).finish();
                context.startActivity(intent);
            }
        });
        gameOverDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Intent intent = new Intent(context, MenuActivity.class);
                ((RomaKanaMatchActivity) context).finish();
                context.startActivity(intent);
            }
        });

        gameOverDialog.show();
    }

    public List<Kana> getKanaList() {
        return kanaList;
    }

    private boolean isCardSame(String key1, String key2) {
        return key1.equalsIgnoreCase(key2);
    }

    public void setCardList(List<View> cardList) {
        this.cardList = cardList;
    }

    public void setGameAdapter(RomaKanaMatchGridViewAdapter adapter) {
        this.adapter = adapter;
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
