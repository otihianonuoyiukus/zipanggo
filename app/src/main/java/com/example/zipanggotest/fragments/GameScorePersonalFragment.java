package com.example.zipanggotest.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.zipanggotest.R;
import com.example.zipanggotest.adapters.GameScorePersonalListViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameScorePersonalFragment extends Fragment {
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private String currentAcc;
    private String gameKeyName;
    private String scoreCSV;

    private String gameName;
    private String systemType;
    private String difficulty;
    private ListView scoreListView;
    private List<String> scoreList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_score_personal, container, false);
        scoreListView = view.findViewById(R.id.game_score_personal_lv_scorelist);

        sharedpreferences = getActivity().getSharedPreferences("Account", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        currentAcc = sharedpreferences.getString("currentAccount", null);

        gameName = getStringExtra(savedInstanceState, "GAMENAME_STR");

        switch (gameName) {
            case "Roma2Kana Speed":
                systemType = (getStringExtra(savedInstanceState, "SYSTEMTYPE_STR")).toLowerCase();
                gameKeyName = currentAcc + "_speed_" + systemType;
                scoreCSV = sharedpreferences.getString(gameKeyName, null);
                if(scoreCSV != null) {
                    scoreList = new ArrayList<>(Arrays.asList(scoreCSV.split(",")));
                } else {
                    scoreList = new ArrayList<>();
                }
                break;
            case "Roma2Kana Match":
                systemType = (getStringExtra(savedInstanceState, "SYSTEMTYPE_STR")).toLowerCase();
                gameKeyName = currentAcc + "_match_" + systemType;
                scoreCSV = sharedpreferences.getString(gameKeyName, null);
                if(scoreCSV != null) {
                    scoreList = new ArrayList<>(Arrays.asList(scoreCSV.split(",")));
                } else {
                    scoreList = new ArrayList<>();
                }
                break;
            default:
                difficulty = (getStringExtra(savedInstanceState, "DIFFICULTY_STR")).toLowerCase();
                gameKeyName = currentAcc + "_missing_" + difficulty;
                scoreCSV = sharedpreferences.getString(gameKeyName, null);
                if(scoreCSV != null) {
                    scoreList = new ArrayList<>(Arrays.asList(scoreCSV.split(",")));
                } else {
                    scoreList = new ArrayList<>();
                }
        }

        GameScorePersonalListViewAdapter gameScorePersonalListViewAdapter =
                new GameScorePersonalListViewAdapter(getActivity(), scoreList);
        scoreListView.setAdapter(gameScorePersonalListViewAdapter);

        return view;
    }

    private String getStringExtra(Bundle savedInstanceState, String key) {
        String stringExtra = "";

        if (savedInstanceState == null) {
            Bundle extra = getActivity().getIntent().getExtras();
            if(extra == null) {
                stringExtra = null;
            } else {
                switch (key) {
                    case "GAMENAME_STR":
                        stringExtra = extra.getString("GAMENAME_STR");
                        break;
                    case "SYSTEMTYPE_STR":
                        stringExtra = extra.getString("SYSTEMTYPE_STR");
                        break;
                    case "DIFFICULTY_STR":
                        stringExtra = extra.getString("DIFFICULTY_STR");
                        break;
                }
            }
        } else {
            stringExtra = (String) savedInstanceState.getSerializable("GAMENAME_STR");
        }

        return stringExtra;
    }
}