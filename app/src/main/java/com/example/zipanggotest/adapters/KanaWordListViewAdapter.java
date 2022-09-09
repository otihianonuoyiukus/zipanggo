package com.example.zipanggotest.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import androidx.annotation.NonNull;

import com.example.zipanggotest.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class KanaWordListViewAdapter extends ArrayAdapter<List<String>> {
    private final LayoutInflater layoutInflater;
    private final List<List<String>> wordListArray;

    public KanaWordListViewAdapter(@NonNull @NotNull Context context,
                                   @NonNull @NotNull List<List<String>> wordListArray) {
        super(context, R.layout.kana_card_front_lv_vocabulary_layout, wordListArray);
        this.wordListArray = wordListArray;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return wordListArray.size();
    }

    @Override
    public List<String> getItem(int position) {
        return null;
    }

    @SuppressLint({"SetTextI18n", "ViewHolder"})
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = layoutInflater.inflate(R.layout.kana_card_front_lv_vocabulary_layout, null);
        List<String> wordList = wordListArray.get(position);
        TextView index = view.findViewById(R.id.kana_card_front_lv_tv_index);
        TextView jpWord = view.findViewById(R.id.kana_card_front_lv_tv_jpword);
        TextView romWord = view.findViewById(R.id.kana_card_front_lv_tv_romword);
        TextView filWord = view.findViewById(R.id.kana_card_front_lv_tv_filword);

        index.setText((wordListArray.indexOf(wordList) + 1) + ".");
        jpWord.setText(wordList.get(0));
        romWord.setText(wordList.get(1));
        filWord.setText(wordList.get(2));

        return view;
    }
}
