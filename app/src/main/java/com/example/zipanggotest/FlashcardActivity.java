package com.example.zipanggotest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.example.zipanggotest.adapters.FlashcardPagerAdapter;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class FlashcardActivity extends AppCompatActivity {

    private List<Kana> kanaCharacterList;
    private ViewPager2 vp2;
    private FlashcardPagerAdapter flashcardAdapter;
    private int prevPosition = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);
        String extraSystem = "Hiragana";
        extraSystem = getStringExtra(savedInstanceState, "SYSTEMTYPE_STR");
        String extraSound = getStringExtra(savedInstanceState, "SOUNDTYPE_STR");
        String[] soundTypeArr = extraSound.split(",");

        // Parses XML data which contains the kana characters and their attributes
        //
        KanaDataXMLParser kanaXML = new KanaDataXMLParser(extraSystem.toLowerCase(), soundTypeArr);
        try {
            kanaCharacterList = kanaXML.kanaReadFeed(getResources().getXml(R.xml.kana_data));
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.shuffle(kanaCharacterList);
        for (Iterator<Kana> iterator = kanaCharacterList.iterator();
             iterator.hasNext(); )
        {
            Kana hc = iterator.next();
            if (hc.getCharacter() == null) {
                iterator.remove();
            }
        }
        flashcardAdapter = new FlashcardPagerAdapter(FlashcardActivity.this, kanaCharacterList);
        vp2 = findViewById(R.id.kana_flashcard_vp2_viewpager);
        vp2.setAdapter(flashcardAdapter);
        vp2.setOffscreenPageLimit(kanaCharacterList.size());
        // Executes when the user swipes to the next/previous flashcard view in the ViewPager
        //
        vp2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                View cardFrontLayout, cardBackLayout;
                AnimatorSet cardOutAnim, cardInAnim;
                cardOutAnim = (AnimatorSet) AnimatorInflater.loadAnimator(
                        FlashcardActivity.this,
                        R.animator.card_flip_out_instant);
                cardInAnim = (AnimatorSet) AnimatorInflater.loadAnimator(
                        FlashcardActivity.this,
                        R.animator.card_flip_in_instant);
                // This makes sure that the next or previous flashcard return to its regular state
                // after it has been flipped; it makes it so that the front layout shows instead
                //
                try {
                    RecyclerView rv = (RecyclerView) vp2.getChildAt(0);
                    if(prevPosition < position) {
                        flashcardAdapter.getCardOutAnim().cancel();
                        flashcardAdapter.getCardInAnim().cancel();
                        cardFrontLayout =
                                Objects.requireNonNull(
                                        rv.findViewHolderForAdapterPosition(position - 1)).
                                                itemView.findViewById(R.id.kana_flashcard_front);
                        cardBackLayout =
                                Objects.requireNonNull(
                                        rv.findViewHolderForAdapterPosition(position - 1)).
                                                itemView.findViewById(R.id.kana_flashcard_back);
                        cardOutAnim.setTarget(cardBackLayout);
                        cardInAnim.setTarget(cardFrontLayout);
                        cardOutAnim.start();
                        cardInAnim.start();
                    } else {
                        flashcardAdapter.getCardOutAnim().cancel();
                        flashcardAdapter.getCardInAnim().cancel();
                        cardFrontLayout =
                                Objects.requireNonNull(
                                        rv.findViewHolderForAdapterPosition(position + 1))
                                                .itemView.findViewById(R.id.kana_flashcard_front);
                        cardBackLayout =
                                Objects.requireNonNull(
                                        rv.findViewHolderForAdapterPosition(position + 1))
                                                .itemView.findViewById(R.id.kana_flashcard_back);
                        cardOutAnim.setTarget(cardBackLayout);
                        cardInAnim.setTarget(cardFrontLayout);
                        cardOutAnim.start();
                        cardInAnim.start();
                    }
                } catch (NullPointerException ex) {}
                flashcardAdapter.setBack(false);
                prevPosition = position;
            }
        });
    }

    private String getStringExtra(Bundle savedInstanceState, String key) {
        String stringExtra = "";

        if (savedInstanceState == null) {
            Bundle extra = getIntent().getExtras();
            if(extra == null) {
                stringExtra = null;
            } else {
                switch (key) {
                    case "SOUNDTYPE_STR":
                        stringExtra = extra.getString("SOUNDTYPE_STR");
                        break;
                    case "SYSTEMTYPE_STR":
                        stringExtra = extra.getString("SYSTEMTYPE_STR");
                        break;
                }
            }
        } else {
            stringExtra = (String) savedInstanceState.getSerializable("SOUNDTYPE_STR");
        }

        return stringExtra;
    }
}