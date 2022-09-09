package com.example.zipanggotest.fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zipanggotest.AchievementActivity;
import com.example.zipanggotest.GameScoreActivity;
import com.example.zipanggotest.MenuButton;
import com.example.zipanggotest.QuizReportActivity;
import com.example.zipanggotest.adapters.MenuListViewAdapter;
import com.example.zipanggotest.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class MenuStatisticFragment extends Fragment {
    private String gameName = "Roma2Kana Soeed";
    private String kanaType = "Hiragana";
    private String diffLevel = "Easy";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_statistic, container, false);
        ListView buttonList = view.findViewById(R.id.menu_statistic_lv_buttonList);
        buttonList.setAdapter(initMenuStatistic(buttonList));
        buttonList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MenuButton menuButton = (MenuButton) parent.getItemAtPosition(position);
                Intent intentMenu = menuButton.getIntent();
                String header = menuButton.getHeader();
                switch (header) {
                    case "Quiz Reports":
                        initKanaDialog(intentMenu);
                        break;
                    case "Game Scores":
                        initGameDialog(intentMenu);
                        break;
                    default:
                        startActivity(intentMenu);
                        break;
                }
            }
        });

        return view;
    }

    private MenuListViewAdapter initMenuStatistic(ListView buttonList) {
        List<MenuButton> menuButtonList = new ArrayList<>();

        // Quiz Reports Button
        addButton(menuButtonList, QuizReportActivity.class, R.drawable.ic_menu_button_quizreport,
                getResources().getString(R.string.menu_statistic_button_quizreport_header),
                getResources().getString(R.string.menu_statistic_button_quizreport_desc)
        );

        // Achievements Button
        addButton(menuButtonList, AchievementActivity.class, R.drawable.ic_menu_button_achievement,
                getResources().getString(R.string.menu_statistic_button_achievement_header),
                getResources().getString(R.string.menu_statistic_button_achievement_desc)
        );

        // Game Scores Button
        addButton(menuButtonList, GameScoreActivity.class, R.drawable.ic_menu_button_gamescore,
                getResources().getString(R.string.menu_statistic_button_gamescore_header),
                getResources().getString(R.string.menu_statistic_button_gamescore_desc)
        );

        buttonList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MenuButton menuButton = (MenuButton) parent.getItemAtPosition(position);
                Intent intentMenu = menuButton.getIntent();
//                String header = menuButton.getHeader();
//                intentMenu.putExtra("SYSTEMTYPE_STR", "hiragana");
                startActivity(intentMenu);
            }
        });

        return new MenuListViewAdapter(requireActivity(), menuButtonList);
    }

    private void addButton(List<MenuButton> buttonList, Class<?> cls, int resId, String header, String description) {
        Intent intent = new Intent(getActivity(), cls);
        Drawable icon = AppCompatResources.getDrawable(requireActivity(), resId);
        MenuButton button = new MenuButton(requireActivity(), intent, header, description, icon);
        buttonList.add(button);
    }

    private void initGameDialog(Intent intent) {
        String title = "Choose a Game";
        gameName = "Roma2Kana Speed";
        View customTitle = getLayoutInflater().inflate(R.layout.menu_cd_header_layout, null);
        TextView titleView = customTitle.findViewById(R.id.menu_cd_header_tv_title);
        MaterialAlertDialogBuilder gameDialog = new MaterialAlertDialogBuilder(requireActivity());
        View view = getLayoutInflater().inflate(R.layout.menu_game_cd_layout, null);
        gameDialog.setView(view);

        // Gets the game name based on the selected radio button
        //
        RadioGroup gameGroup = view.findViewById(R.id.menu_game_cd_rg_gamegroup);
        gameGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton gameButton =
                        view.findViewById(gameGroup.getCheckedRadioButtonId());
                gameName = (String) gameButton.getText();
            }
        });

        // Dialog box details
        //
        titleView.setText(title);
        gameDialog.setCustomTitle(customTitle);
        String positiveBtnTitle;
        positiveBtnTitle = getResources().getString(R.string.choice_confirm_text);

        // Puts the game name string into the intent after confirming
        //
        gameDialog.setPositiveButton(positiveBtnTitle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                intent.putExtra("GAMENAME_STR", gameName);

                switch(gameName) {
                    case "Roma2Kana Speed":
                        initKanaDialog(intent);
                        break;
                    case "Roma2Kana Match":
                        initKanaDialog(intent);
                        break;
                    case "Missing Kanji":
                        initKanjiDialog(intent);
                        break;
                    default:
                }
            }
        });

        // Shows the dialog box
        //
        gameDialog.setNeutralButton(getResources().getString(R.string.choice_cancel_text), null);
        gameDialog.show();
    }

    private void initKanaDialog(Intent intent) {
        MaterialAlertDialogBuilder kanaDialog = new MaterialAlertDialogBuilder(requireActivity());
        View kanaView = getLayoutInflater().inflate(R.layout.menu_game_kana_cd_layout, null);
        kanaDialog.setView(kanaView);

        RadioGroup kanaGroup = kanaView.findViewById(R.id.menu_game_kana_rg_kanagroup);
        kanaGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton kanaButton =
                        kanaView.findViewById(kanaGroup.getCheckedRadioButtonId());
                kanaType = (String) kanaButton.getText();
            }
        });

        String positiveBtnTitle;
        positiveBtnTitle = getResources().getString(R.string.choice_confirm_text);

        kanaDialog.setPositiveButton(positiveBtnTitle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                intent.putExtra("SYSTEMTYPE_STR", kanaType);
                startActivity(intent);
            }
        });

        kanaDialog.setNeutralButton(getResources().getString(R.string.choice_cancel_text), null);

        kanaDialog.show();
    }

    private void initKanjiDialog(Intent intent) {
        diffLevel = "easy";
        MaterialAlertDialogBuilder kanjiDialog = new MaterialAlertDialogBuilder(requireActivity());
        View view = getLayoutInflater().inflate(R.layout.menu_game_difficulty_cd_layout, null);
        kanjiDialog.setView(view);

        // Gets the difficulty based on the selected radio button
        //
        RadioGroup diffGroup = view.findViewById(R.id.menu_game_diff_rg_diffgroup);
        diffGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton diffButton =
                        view.findViewById(diffGroup.getCheckedRadioButtonId());
                diffLevel = (String) diffButton.getText();
            }
        });

        // Dialog box details
        //
        String positiveBtnTitle;
        positiveBtnTitle = getResources().getString(R.string.choice_confirm_text);

        // Puts the difficulty string into the intent after confirming
        //
        kanjiDialog.setPositiveButton(positiveBtnTitle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                intent.putExtra("DIFFICULTY_STR", diffLevel);
                startActivity(intent);
            }
        });

        // Shows the dialog box
        //
        kanjiDialog.setNeutralButton(getResources().getString(R.string.choice_cancel_text), null);
        kanjiDialog.show();
    }
}