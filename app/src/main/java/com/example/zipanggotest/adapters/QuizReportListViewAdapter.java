package com.example.zipanggotest.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zipanggotest.R;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.Arrays;
import java.util.List;

public class QuizReportListViewAdapter extends RecyclerView.Adapter<QuizReportListViewAdapter.ViewHolder> {
    private Activity activity;
    private List<String> reportList;

    public QuizReportListViewAdapter(Activity activity, List<String> reportList) {
        this.activity = activity;
        this.reportList = reportList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View reportBtn = layoutInflater.inflate(R.layout.quizreport_button_lv_layout, parent, false);
        return new ViewHolder(reportBtn);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        List<String> quizReport = Arrays.asList(reportList.get(position).split(","));
        holder.quizDate.setText(quizReport.get(0));
        holder.quizTime.setText(quizReport.get(1));
        holder.quizTotal.setText("Total: " + quizReport.get(3));
        holder.quizSoundType.setText(quizReport.get(2));
        holder.quizCorrect.setText("Correct: " + quizReport.get(4));
        holder.quizWrong.setText("Wrong: " + quizReport.get(5));
        holder.quizProgress.setMax(Integer.parseInt(quizReport.get(3)));
        holder.quizProgress.setProgress(Integer.parseInt(quizReport.get(4)));
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView quizDate, quizTime, quizTotal, quizSoundType, quizCorrect, quizWrong;
        LinearProgressIndicator quizProgress;

        public ViewHolder(View itemView) {
            super(itemView);
            this.quizDate = itemView.findViewById(R.id.quizreport_lv_tv_date);
            this.quizTime = itemView.findViewById(R.id.quizreport_lv_tv_time);
            this.quizTotal = itemView.findViewById(R.id.quizreport_lv_tv_total);
            this.quizSoundType = itemView.findViewById(R.id.quizreport_lv_tv_soundtype);
            this.quizCorrect = itemView.findViewById(R.id.quizreport_lv_tv_correct);
            this.quizWrong = itemView.findViewById(R.id.quizreport_lv_tv_wrong);
            this.quizProgress = itemView.findViewById(R.id.quizreport_lv_pi_progress);
        }
    }
}
