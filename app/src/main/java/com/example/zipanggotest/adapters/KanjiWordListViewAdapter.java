package com.example.zipanggotest.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
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

public class KanjiWordListViewAdapter extends ArrayAdapter<List<String>> {
    private final LayoutInflater layoutInflater;
    private final List<List<String>> wordListArray;

    public KanjiWordListViewAdapter(@NonNull @NotNull Context context,
                                   @NonNull @NotNull List<List<String>> wordListArray) {
        super(context, R.layout.kanji_card_front_vocabulary_layout, wordListArray);
        this.wordListArray = wordListArray;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return wordListArray.size();
    }

    @Nullable
    @Override
    public List<String> getItem(int position) {
        return super.getItem(position);
    }

    @SuppressLint({"ViewHolder", "SetTextI18n", "InflateParams"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        view = layoutInflater.inflate(R.layout.kanji_card_front_vocabulary_layout, null);
        List<String> wordList = wordListArray.get(position);
        TextView kanjiIndex = view.findViewById(R.id.kanji_card_front_tv_index);
        TextView kanjiWord = view.findViewById(R.id.kanji_card_front_tv_vocabulary);

        kanjiIndex.setText((wordListArray.indexOf(wordList) + 1) + ". ");
        kanjiWord.setText(wordList.get(0));

        return view;
    }
}
