package com.example.zipanggotest.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zipanggotest.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Arrays;
import java.util.List;

public class AchievementListViewAdapter extends RecyclerView.Adapter<AchievementListViewAdapter.ViewHolder> {
    private Activity activity;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private List<List<String>> achievementList;
    private String currentAcc;

    public AchievementListViewAdapter(Activity activity, List<List<String>> achievementList) {
        this.activity = activity;
        this.achievementList = achievementList;
        sharedpreferences = activity.getSharedPreferences("Account", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        currentAcc = sharedpreferences.getString("currentAccount", null);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View reportBtn = layoutInflater.inflate(R.layout.achievement_button_lv_layout, parent, false);
        return new ViewHolder(reportBtn);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        List<String> achievement = achievementList.get(position);
        try {
            if (sharedpreferences.getBoolean(currentAcc + "_" + (achievement.get(0)).toLowerCase().replaceAll("\\s", ""), false)) {
                holder.achievementIcon.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_achievement_unlocked));
            } else {
                holder.achievementIcon.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_achievement_locked));
                holder.achievementLayout.setAlpha(0.5f);
            }
        } catch (NullPointerException ex) {
            holder.achievementIcon.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_achievement_locked));
            holder.achievementLayout.setAlpha(0.5f);
        }
        holder.achievementName.setText(achievement.get(0));
        holder.achievementDesc.setText(achievement.get(1));
    }

    @Override
    public int getItemCount() {
        return achievementList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout achievementLayout;
        ShapeableImageView achievementIcon;
        TextView achievementName, achievementDesc;

        public ViewHolder(View itemView) {
            super(itemView);
            this.achievementLayout = itemView.findViewById(R.id.achievement_button_rl_layout);
            this.achievementIcon = itemView.findViewById(R.id.achievement_button_siv_icon);
            this.achievementName = itemView.findViewById(R.id.achievement_button_tv_header);
            this.achievementDesc = itemView.findViewById(R.id.achievement_button_tv_desc);
        }
    }
}
