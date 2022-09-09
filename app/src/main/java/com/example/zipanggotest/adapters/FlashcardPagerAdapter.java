package com.example.zipanggotest.adapters;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zipanggotest.Kana;
import com.example.zipanggotest.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FlashcardPagerAdapter extends RecyclerView.Adapter<FlashcardPagerAdapter.ViewHolder> {
    private final Context parent;
    private final List<Kana> kanaCharacterList;
    private AnimatorSet cardOutAnim, cardInAnim;
    private boolean isBack = false;

    public FlashcardPagerAdapter(Context parent, List<Kana> kanaCharacterList) {
        this.parent = parent;
        this.kanaCharacterList = kanaCharacterList;
    }

    public AnimatorSet getCardOutAnim() {
        return cardOutAnim;
    }

    public AnimatorSet getCardInAnim() {
        return cardInAnim;
    }

    public boolean isBack() {
        return isBack;
    }

    public void setBack(boolean back) {
        isBack = back;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View flashcardFrontLayout, flashcardBackLayout;
        TextView character, transliteration;
        GridView strokeOrderGrid;
        ImageButton frontFlipBtn, backFlipBtn;
        float scale;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            // Initialization of the front and back layout of the flashcard along with its animations
            //
            flashcardFrontLayout = itemView.findViewById(R.id.kana_flashcard_front);
            flashcardBackLayout = itemView.findViewById(R.id.kana_flashcard_back);
            cardOutAnim = (AnimatorSet) AnimatorInflater.loadAnimator(parent, R.animator.card_flip_out);
            cardInAnim = (AnimatorSet) AnimatorInflater.loadAnimator(parent, R.animator.card_flip_in);
            scale = parent.getResources().getDisplayMetrics().density * 8000;
            flashcardFrontLayout.setCameraDistance(scale);
            flashcardBackLayout.setCameraDistance(scale);

            // Initialization of the front components of the flashcard
            //
            character = itemView.findViewById(R.id.kana_flashcard_front_tv_character);
            strokeOrderGrid = itemView.findViewById(R.id.kana_flashcard_front_gv_stroke);

            // Initialization of the button that flips the card (front)
            //
            frontFlipBtn = itemView.findViewById(R.id.kana_flashcard_front_ib_flip);
            frontFlipBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isBack) {
                        cardOutAnim.setTarget(flashcardFrontLayout);
                        cardInAnim.setTarget(flashcardBackLayout);
                        cardOutAnim.start();
                        cardInAnim.start();
                        isBack = true;
                    } else {
                        cardOutAnim.setTarget(flashcardBackLayout);
                        cardInAnim.setTarget(flashcardFrontLayout);
                        cardOutAnim.start();
                        cardInAnim.start();
                        isBack = false;
                    }
                }
            });

            // Initialization of the back components of the flashcard
            //
            transliteration = itemView.findViewById(R.id.kana_flashcard_back_tv_transliteration);

            // Initialization of the button that flips the card (back)
            //
            backFlipBtn = itemView.findViewById(R.id.kana_flashcard_back_ib_flip);
            backFlipBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isBack) {
                        cardOutAnim.setTarget(flashcardFrontLayout);
                        cardInAnim.setTarget(flashcardBackLayout);
                        cardOutAnim.start();
                        cardInAnim.start();
                        isBack = true;
                    } else {
                        cardOutAnim.setTarget(flashcardBackLayout);
                        cardInAnim.setTarget(flashcardFrontLayout);
                        cardOutAnim.start();
                        cardInAnim.start();
                        isBack = false;
                    }
                }
            });
        }
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kana_flashcard_layout,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FlashcardPagerAdapter.ViewHolder holder, int position) {
        Kana kana = kanaCharacterList.get(position);

        // Setting of the front and back components of the flashcard
        //
        holder.character.setText(kana.getCharacter());
        holder.transliteration.setText(kana.getTransliterationList().get(0));

        // Setting of the GridView adapter which displays the stroke order for a specific kana/kanji character
        //
        StrokeOrderGridViewAdapter csog;
        csog = new StrokeOrderGridViewAdapter(holder.itemView.getContext(), kana.getStrokeOrderList());
        holder.strokeOrderGrid.setAdapter(csog);
    }

    @Override
    public int getItemCount() {
        return kanaCharacterList.size();
    }
}
