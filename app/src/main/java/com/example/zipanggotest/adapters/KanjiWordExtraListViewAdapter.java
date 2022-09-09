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

public class KanjiWordExtraListViewAdapter extends ArrayAdapter<List<String>> {
    private final LayoutInflater layoutInflater;
    private final List<List<String>> wordListArray;

    public KanjiWordExtraListViewAdapter(@NonNull @NotNull Context context,
                                         @NonNull @NotNull List<List<String>> wordListArray) {
        super(context, R.layout.kanji_card_back_vocabulary_layout, wordListArray);
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
        view = layoutInflater.inflate(R.layout.kanji_card_back_vocabulary_layout, null);
        List<String> wordList = wordListArray.get(position);
        TextView vocabIndex = view.findViewById(R.id.kanji_card_back_vocabulary_index);
        TextView vocabHiragana = view.findViewById(R.id.kanji_card_back_vocabulary_hiragana);
        TextView vocabFil = view.findViewById(R.id.kanji_card_back_vocabulary_fil);

        vocabIndex.setText((wordListArray.indexOf(wordList) + 1) + ". ");
        vocabHiragana.setText(wordList.get(1));
        vocabFil.setText(wordList.get(2));

        return view;
    }
}
