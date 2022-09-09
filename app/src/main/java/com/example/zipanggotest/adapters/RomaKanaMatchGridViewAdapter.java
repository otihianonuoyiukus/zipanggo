package com.example.zipanggotest.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.zipanggotest.Kana;
import com.example.zipanggotest.R;
import com.example.zipanggotest.games.RomaKanaMatchGame;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RomaKanaMatchGridViewAdapter extends BaseAdapter {
    private static final int GRID_MAXSIZE = 16;

    private final Context context;
    private final LayoutInflater layoutInflater;
    private final RomaKanaMatchGame romaKanaMatchGame;
    private final List<View> viewList;

    private List<Kana> kanaList;
    private List<String> cardList;

    public RomaKanaMatchGridViewAdapter(Context context, String systemType)
    {
        this.context = context;
        this.viewList = new ArrayList<>(16);
        for(int i = 0; i < GRID_MAXSIZE; i++)
            viewList.add(null);
        this.romaKanaMatchGame = new RomaKanaMatchGame(context, systemType);
        romaKanaMatchGame.setGameAdapter(RomaKanaMatchGridViewAdapter.this);
        this.layoutInflater = (LayoutInflater.from(context));
        this.kanaList = romaKanaMatchGame.getKanaList();
        this.initGenerateCards();
    }

    @Override
    public int getCount() {
        return GRID_MAXSIZE;
    }

    @Override
    public View getItem(int position) {
        return viewList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = layoutInflater.inflate(R.layout.romakana_match_card_layout, null);
        if(viewList.get(15) == null)
            viewList.set(position, view);
        MaterialButton frontCard = view.findViewById(R.id.romakana_match_rl_b_frontcard);
        frontCard.setText(cardList.get(position));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                romaKanaMatchGame.onCardClick(v, position);
            }
        });

        return view;
    }

    private void initGenerateCards() {
        List<String> characterList = new ArrayList<>(8);
        List<String> transliterationList = new ArrayList<>(8);
        for(int i = 0; i < (this.kanaList.size() / 2); i++) {
            characterList.add(this.kanaList.get(i).getCharacter());
            transliterationList.add(this.kanaList.get(i).getTransliterationList().get(0));
        }
        this.cardList = new ArrayList<>(characterList);
        this.cardList.addAll(transliterationList);
        long systemMilli = System.currentTimeMillis();
        Collections.shuffle(this.kanaList, new Random(systemMilli));
        Collections.shuffle(this.cardList, new Random(systemMilli));
    }
}
