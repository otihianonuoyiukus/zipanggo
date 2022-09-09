package com.example.zipanggotest.adapters;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.zipanggotest.Kana;
import com.example.zipanggotest.R;
import com.example.zipanggotest.games.RomaKanaSpeedGame;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class RomaKanaSpeedGridViewAdapter extends BaseAdapter {
    private static final int GRID_MAXSIZE = 25;

    private final Context context;
    private final LayoutInflater layoutInflater;
    private RomaKanaSpeedGame romaKanaSpeedGame;
    private List<Kana> kanaList;

    public RomaKanaSpeedGridViewAdapter(
            Context context,
            List<Kana> kanaList)
    {
        this.context = context;
        this.kanaList = kanaList;
        layoutInflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return GRID_MAXSIZE;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = layoutInflater.inflate(R.layout.romakana_speed_card_layout, null);

        MaterialButton card = view.findViewById(R.id.romakana_speed_rl_b_card);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                romaKanaSpeedGame
                        .onCardClick(v, kanaList.get(position).getTransliterationList().get(0));
                romaKanaSpeedGame
                        .getCardFadeOutAnim().addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) { }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (romaKanaSpeedGame.isCardGridEmpty()) {
                            romaKanaSpeedGame.generateCards();
                            kanaList = null;
                            kanaList = romaKanaSpeedGame.getKanaList();
                            for(int i = 0; i < GRID_MAXSIZE; i++) {
                                View layout = parent.getChildAt(i);
                                //layout.setVisibility(View.VISIBLE);
                                MaterialButton button =
                                        layout.findViewById(R.id.romakana_speed_rl_b_card);
                                Kana kana = kanaList.get(i);
                                AnimatorSet cardFadeInAnim;
                                cardFadeInAnim = (AnimatorSet) AnimatorInflater.loadAnimator(
                                        context,
                                        R.animator.card_fade_in
                                );
                                cardFadeInAnim.setTarget(button);
                                cardFadeInAnim.start();
                                button.setText(kana.getCharacter());
                            }
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) { }

                    @Override
                    public void onAnimationRepeat(Animator animation) { }
                });
            }
        });

        Kana kana = kanaList.get(position);
        card.setText(kana.getCharacter());

        return view;
    }

    public void setRomaKanaSpeedGame(RomaKanaSpeedGame romaKanaSpeedGame) {
        this.romaKanaSpeedGame = romaKanaSpeedGame;
    }
}
