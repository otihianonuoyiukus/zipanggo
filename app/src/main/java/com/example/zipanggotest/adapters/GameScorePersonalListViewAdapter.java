package com.example.zipanggotest.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.zipanggotest.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GameScorePersonalListViewAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final LayoutInflater layoutInflater;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private final List<String> scoreList;

    public GameScorePersonalListViewAdapter(@NonNull @NotNull Context context,
                                            @NonNull @NotNull List<String> scoreList)
    {
        super(context, R.layout.game_score_lv_layout, scoreList);
        this.context = context;
        this.scoreList = scoreList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return scoreList.size();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return super.getItem(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        view = layoutInflater.inflate(R.layout.game_score_lv_layout, null);
        String currentAccount;
        String scoreVal = scoreList.get(position);
        TextView accName = view.findViewById(R.id.game_score_playername);
        TextView accScore = view.findViewById(R.id.game_score_scorevalue);
        sharedpreferences = context.getSharedPreferences("Account", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        currentAccount = sharedpreferences.getString("currentAccount", null);

        accName.setText(currentAccount);
        accScore.setText(scoreVal);

        return view;
    }
}
