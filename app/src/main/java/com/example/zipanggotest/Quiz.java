package com.example.zipanggotest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class Quiz {
    private Context parent;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private Set<String> set;
    private String currentAcc;
    private String systemType;
    private List<String> reportList;
    private List<Kana> characterList;
    private List<String> soundType;
    private List<String> quizQuestion, correctAnswer, wrongAnswer;
    private List<List<String>> quizAnswer;
    private int correctNum, wrongNum, totalQuestion, currentQuestionNum;

    public Quiz(Context parent, List<Kana> characterList, List<String> soundType, String systemType) {
        this.parent = parent;
        this.characterList = characterList;
        this.soundType = soundType;
        this.systemType = systemType;
        sharedpreferences = parent.getSharedPreferences("Account", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        currentAcc = sharedpreferences.getString("currentAccount", null);
        set = sharedpreferences.getStringSet(currentAcc + "_quizreport_" + systemType, null);
        if(set != null) {
            reportList = new ArrayList<>(set);
        } else {
            reportList = new ArrayList<>();
        }
        quizQuestion = new ArrayList<>();
        quizAnswer = new ArrayList<>();
        correctAnswer = new ArrayList<>();
        wrongAnswer = new ArrayList<>();
        initQuiz();
    }

    public void initQuiz() {
        correctNum = 0;
        wrongNum = 0;
        currentQuestionNum = 1;

        Collections.shuffle(characterList);
        for (Iterator<Kana> iterator = characterList.iterator();
             iterator.hasNext(); )
        {
            Kana hc = iterator.next();
            if (hc.getCharacter() == null) {
                iterator.remove();
                continue;
            }
            quizQuestion.add(hc.getCharacter());
            quizAnswer.add(hc.getTransliterationList());
        }
        totalQuestion = quizQuestion.size();
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    public boolean onAnswer(String answer) {
        boolean answerBool = false;
        // Finds the answer within a List of answers
        // Some Kana have more than one transliteration (e.g. つ; it can be "tsu" or "tu", both are valid)
        //
        for (String answerList : quizAnswer.get(currentQuestionNum - 1)) {
            if (answer.equalsIgnoreCase(answerList)) {
                correctNum++;
                correctAnswer.add(quizQuestion.get(currentQuestionNum - 1));
                answerBool = true;
            }
        }
        // If the Kana has two transliteration and both did not match the answer
        // Or if the answer is just wrong to begin with, the condition will be met
        //
        if(!answerBool) {
            wrongNum++;
            wrongAnswer.add(quizQuestion.get(currentQuestionNum - 1));
            answerBool = false;
        }
        // Checks whether the index of the current question exceeds the size of the arraylist
        //
        if (currentQuestionNum < totalQuestion) {
            currentQuestionNum++;
        } else {
            // Executes when the quiz ends after the final question
            //
            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            Date date = new Date();

            String quizDate = dateFormatter.format(date);
            String quizTime = timeFormatter.format(date);
            String totalScore = String.valueOf(quizQuestion.size());
            String soundTypeStr = Arrays.toString(soundType.toArray()).replace("[", "").replace("]", "").replace(", ", " - ");

            String quizReportCSV = quizDate + "," + quizTime + "," + soundTypeStr + "," + totalScore + "," + correctNum + "," + wrongNum;

            reportList.add(quizReportCSV);
            set = new HashSet<>(reportList);
            editor.putStringSet(currentAcc + "_quizreport_" + systemType, set);
            editor.commit();

            View customTitle = ((QuizActivity) parent).getLayoutInflater()
                    .inflate(R.layout.gamemeover_dialog_header_layout, null);
            MaterialAlertDialogBuilder gameOverDialog = new MaterialAlertDialogBuilder(parent);
            View view =
                    ((QuizActivity) parent)
                            .getLayoutInflater()
                            .inflate(R.layout.quiz_finish_layout, null);
            gameOverDialog.setCustomTitle(customTitle);
            gameOverDialog.setView(view);

            gameOverDialog.setNeutralButton("MENU", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(parent, MenuActivity.class);
                    ((QuizActivity) parent).finish();
                    ((QuizActivity) parent).startActivity(intent);
                }
            });

            gameOverDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Intent intent = new Intent(parent, MenuActivity.class);
                    ((QuizActivity) parent).finish();
                    ((QuizActivity) parent).startActivity(intent);
                }
            });

            initDialogColor(view);
            QuizResult quizResult = new QuizResult(correctAnswer, wrongAnswer);
            TextView correctValue = view.findViewById(R.id.quiz_finish_tv_correct_value);
            TextView wrongValue = view.findViewById(R.id.quiz_finish_tv_wrong_value);
            TextView percentValue = view.findViewById(R.id.quiz_finish_tv_percent_value);
            TextView wrongList = view.findViewById(R.id.quiz_finish_tv_wronglist);
            correctValue.setText(Integer.toString(quizResult.getTotalCorrect()));
            wrongValue.setText(Integer.toString(quizResult.getTotalWrong()));
            percentValue.setText(String.format("%.2f", quizResult.getCorrectPercentage()));
            wrongList.setText(wrongAnswer.toString());
            gameOverDialog.show();
        }
        return answerBool;
    }

    public void initDialogColor(View endDialog) {
        Drawable endCorrectLayoutDrawable = endDialog.findViewById(R.id.quiz_finish_rl_correct).getBackground();
        Drawable endWrongLayoutDrawable = endDialog.findViewById(R.id.quiz_finish_rl_wrong).getBackground();
        Drawable endPercentLayoutDrawable = endDialog.findViewById(R.id.quiz_finish_rl_percent).getBackground();
        Drawable endCorrectLayoutHeaderDrawable = endDialog.findViewById(R.id.quiz_finish_rl_correct_header).getBackground();
        Drawable endWrongLayoutHeaderDrawable = endDialog.findViewById(R.id.quiz_finish_rl_wrong_header).getBackground();
        Drawable endPercentLayoutHeaderDrawable = endDialog.findViewById(R.id.quiz_finish_rl_percent_header).getBackground();
        GradientDrawable endCorrectDrawable = (GradientDrawable) endCorrectLayoutDrawable;
        GradientDrawable endWrongDrawable = (GradientDrawable) endWrongLayoutDrawable;
        GradientDrawable endPercentDrawable = (GradientDrawable) endPercentLayoutDrawable;
        GradientDrawable endCorrectHeaderDrawable = (GradientDrawable) endCorrectLayoutHeaderDrawable;
        GradientDrawable endWrongHeaderDrawable = (GradientDrawable) endWrongLayoutHeaderDrawable;
        GradientDrawable endPercentHeaderDrawable = (GradientDrawable) endPercentLayoutHeaderDrawable;
        endCorrectDrawable.setColor(ContextCompat.getColor(parent, R.color.light_correct_green));
        endWrongDrawable.setColor(ContextCompat.getColor(parent, R.color.light_wrong_red));
        endPercentDrawable.setColor(ContextCompat.getColor(parent, R.color.light_percent_grey));
        endCorrectHeaderDrawable.setColor(ContextCompat.getColor(parent, R.color.dark_correct_green));
        endWrongHeaderDrawable.setColor(ContextCompat.getColor(parent, R.color.dark_wrong_red));
        endPercentHeaderDrawable.setColor(ContextCompat.getColor(parent, R.color.dark_percent_grey));
    }
    
    public String getCurrentPosition() {
        return currentQuestionNum + "/" + totalQuestion;
    }

    public int getCorrectNum() {
        return correctNum;
    }

    public int getWrongNum() {
        return wrongNum;
    }

    public int getTotalQuestion() {
        return totalQuestion;
    }

    public int getCurrentQuestionNum() {
        return currentQuestionNum;
    }

    public List<String> getQuizQuestion() {
        return quizQuestion;
    }
}
