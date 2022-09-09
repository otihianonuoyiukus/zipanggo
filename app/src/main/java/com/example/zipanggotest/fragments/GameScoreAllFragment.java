package com.example.zipanggotest.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.zipanggotest.R;
import com.example.zipanggotest.adapters.GameScoreAllListViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class GameScoreAllFragment extends Fragment {
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private String gameKeyName;
    private String scoreCSV;

    private String gameName;
    private String systemType;
    private String difficulty;
    private ListView scoreListView;
    private Set<String> set;
    private List<String> accountList;
    private List<List<String>> accScoreList;
    private List<String> scoreList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_score_all, container, false);
        scoreListView = view.findViewById(R.id.game_score_all_lv_scorelist);
        sharedpreferences = getActivity().getSharedPreferences("Account", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        set = sharedpreferences.getStringSet("account", null);
        accScoreList = new ArrayList<>();
        accountList = new ArrayList<>(set);
        gameName = getStringExtra(savedInstanceState, "GAMENAME_STR");

        switch (gameName) {
            case "Roma2Kana Speed":
                systemType = (getStringExtra(savedInstanceState, "SYSTEMTYPE_STR")).toLowerCase();
                for (String accName : accountList) {
                    gameKeyName = accName + "_speed_" + systemType;
                    scoreCSV = sharedpreferences.getString(gameKeyName, null);
                    if(scoreCSV != null) {
                        scoreList = new ArrayList<>(Arrays.asList(scoreCSV.split(",")));
                    } else {
                        scoreList = new ArrayList<>();
                    }
                    for(String score : scoreList) {
                        accScoreList.add(new ArrayList<>(Arrays.asList(accName, score)));
                    }
                }
                break;
            case "Roma2Kana Match":
                systemType = (getStringExtra(savedInstanceState, "SYSTEMTYPE_STR")).toLowerCase();
                for (String accName : accountList) {
                    gameKeyName = accName + "_match_" + systemType;
                    scoreCSV = sharedpreferences.getString(gameKeyName, null);
                    if(scoreCSV != null) {
                        scoreList = new ArrayList<>(Arrays.asList(scoreCSV.split(",")));
                    } else {
                        scoreList = new ArrayList<>();
                    }
                    for(String score : scoreList) {
                        accScoreList.add(new ArrayList<>(Arrays.asList(accName, score)));
                    }
                }
                break;
            default:
                difficulty = (getStringExtra(savedInstanceState, "DIFFICULTY_STR")).toLowerCase();
                for (String accName : accountList) {
                    gameKeyName = accName + "_missing_" + difficulty;
                    scoreCSV = sharedpreferences.getString(gameKeyName, null);
                    if(scoreCSV != null) {
                        scoreList = new ArrayList<>(Arrays.asList(scoreCSV.split(",")));
                    } else {
                        scoreList = new ArrayList<>();
                    }
                    for(String score : scoreList) {
                        accScoreList.add(new ArrayList<>(Arrays.asList(accName, score)));
                    }
                }
        }

        Quicksort quicksort = new Quicksort();

        GameScoreAllListViewAdapter gameScoreAllListViewAdapter =
                new GameScoreAllListViewAdapter(getActivity(), quicksort.sort(accScoreList));
        scoreListView.setAdapter(gameScoreAllListViewAdapter);

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

    public class Quicksort  {
        private String[][] numbers;
        private int number;

        public List<List<String>> sort(List<List<String>> values) {
            // check for empty or null array
            if (values ==null || values.size()==0){
                return null;
            }

            String[][] array = new String[values.size()][];
            for (int i = 0; i < values.size(); i++) {
                List<String> row = values.get(i);
                array[i] = row.toArray(new String[row.size()]);
            }

            this.numbers = array;
            number = array.length;
            quicksort(0, number - 1);

            List<List<String>> arrayList2D = new ArrayList<>();
            for (int i = 0; i < numbers.length; i++) {
                List<String> eachRecord = Arrays.asList(numbers[i]);
                arrayList2D.add(eachRecord);
            }
            Collections.reverse(arrayList2D);

            return arrayList2D;
        }

        private void quicksort(int low, int high) {
            int i = low, j = high;
            // Get the pivot element from the middle of the list
            int pivot = Integer.parseInt(numbers[low + (high-low)/2][1]);

            // Divide into two lists
            while (i <= j) {
                // If the current value from the left list is smaller than the pivot
                // element then get the next element from the left list
                while (Integer.parseInt(numbers[i][1]) < pivot) {
                    i++;
                }
                // If the current value from the right list is larger than the pivot
                // element then get the next element from the right list
                while (Integer.parseInt(numbers[j][1]) > pivot) {
                    j--;
                }

                // If we have found a value in the left list which is larger than
                // the pivot element and if we have found a value in the right list
                // which is smaller than the pivot element then we exchange the
                // values.
                // As we are done we can increase i and j
                if (i <= j) {
                    exchange(i, j);
                    i++;
                    j--;
                }
            }
            // Recursion
            if (low < j)
                quicksort(low, j);
            if (i < high)
                quicksort(i, high);
        }

        private void exchange(int i, int j) {
            String tempAcc = numbers[i][0];
            int tempScore = Integer.parseInt(numbers[i][1]);
            numbers[i][0] = numbers[j][0];
            numbers[i][1] = numbers[j][1];
            numbers[j][0] = tempAcc;
            numbers[j][1] = String.valueOf(tempScore);
        }
    }
}