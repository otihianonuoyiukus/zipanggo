package com.example.zipanggotest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.zipanggotest.adapters.QuizReportListViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class QuizReportActivity extends AppCompatActivity {
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private Set<String> set;
    private String currentAcc;
    private String systemType;
    private String reportName;
    private List<String> reportList;
    private RecyclerView reportListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_report);

        reportListView = findViewById(R.id.quizreport_rv_reportlist);

        systemType = (getStringExtra(savedInstanceState, "SYSTEMTYPE_STR")).toLowerCase();

        sharedpreferences = getSharedPreferences("Account", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        currentAcc = sharedpreferences.getString("currentAccount", null);
        reportName = currentAcc + "_quizreport_" + systemType;
        set = sharedpreferences.getStringSet(reportName, null);
        if(set != null) {
            reportList = new ArrayList<>(set);
        } else {
            reportList = new ArrayList<>();
        }

        QuizReportListViewAdapter quizReportListViewAdapter =
                new QuizReportListViewAdapter(QuizReportActivity.this, reportList);
        reportListView.setLayoutManager(new LinearLayoutManager(this));
        reportListView.setAdapter(quizReportListViewAdapter);
    }

    private String getStringExtra(Bundle savedInstanceState, String key) {
        String stringExtra = "";

        if (savedInstanceState == null) {
            Bundle extra = getIntent().getExtras();
            if(extra == null) {
                stringExtra = null;
            } else {
                if ("SYSTEMTYPE_STR".equals(key)) {
                    stringExtra = extra.getString("SYSTEMTYPE_STR");
                }
            }
        } else {
            stringExtra = (String) savedInstanceState.getSerializable("SYSTEMTYPE_STR");
        }

        return stringExtra;
    }
}