package com.example.zipanggotest.adapters;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zipanggotest.Kana;
import com.example.zipanggotest.PaintView;
import com.example.zipanggotest.R;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class CharacterGridViewAdapter extends BaseAdapter {
    private final Context context;
    private final LayoutInflater layoutInflater;
    private final List<Kana> kanaList;
    private MediaPlayer mediaPlayer;
    private boolean isBack = false,
            characterVisible = true,
            gridVisible = true;

    public CharacterGridViewAdapter(Context applicationContext, List<Kana> kanaList) {
        this.context = applicationContext;
        this.kanaList = kanaList;
        layoutInflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return kanaList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = layoutInflater.inflate(R.layout.grid_cell_layout, null);
        Kana kana = kanaList.get(position);
        MaterialButton button = view.findViewById(R.id.grid_cell_rl_mb_characterbutton);
        TextView characterText = view.findViewById(R.id.grid_cell_rl_tv_charactertext);
        TextView transliterationText = view.findViewById(R.id.grid_cell_rl_tv_transliteration);
        RelativeLayout layout = view.findViewById(R.id.gridLayout);

        if (kana.getCharacter() != null) {
            characterText.setText(kanaList.get(position).getCharacter());
            transliterationText.setText(kanaList.get(position).getTransliterationList().get(0));
        } else { layout.setVisibility(View.INVISIBLE); }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initCardDialog(parent.getContext(), kana);
            }
        });

        return view;
    }

    private void initCardDialog(Context parentContext, Kana kana) {
        String strokeCount = " " + kana.getStrokeCount();
        String character = kana.getCharacter();
        List<String> transliteration = kana.getTransliterationList();
        String mnemonicText = kana.getMnemonicTxt();
        String mnemonicImgName = kana.getMnemonicImg();
        List<List<String>> wordListArray = kana.getWordList();

        // Creates a pop-up dialog after a kana button has been clicked
        //
        Dialog cardDialog = new Dialog(parentContext);
        cardDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cardDialog.setContentView(R.layout.kana_card_wmnemonic_layout);
        cardDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cardDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) { isBack = false; }
        });

        // Initialization of the front and back layout of the card along with its animations
        //
        View cardFrontLayout = cardDialog.findViewById(R.id.kana_card_front);
        View cardBackLayout = cardDialog.findViewById(R.id.kana_card_back);
        AnimatorSet cardOutAnim =
                (AnimatorSet) AnimatorInflater.loadAnimator(parentContext, R.animator.card_flip_out);
        AnimatorSet cardInAnim =
                (AnimatorSet) AnimatorInflater.loadAnimator(parentContext, R.animator.card_flip_in);
        float scale = context.getResources().getDisplayMetrics().density * 8000;
        cardFrontLayout.setCameraDistance(scale);
        cardBackLayout.setCameraDistance(scale);

        // Setting of the front components of the card
        //
        ((TextView) cardDialog.findViewById(R.id.kana_card_front_tv_stroke_count))
                .setText(strokeCount);
        ((TextView) cardDialog.findViewById(R.id.kana_card_front_tv_character))
                .setText(character);
        ((TextView) cardDialog.findViewById(R.id.kana_card_front_tv_transliteration))
                .setText(transliteration.get(0));

            // Initialization of the ListView which includes Japanese words with its transliteration
            // and translation to Filipino
            //
        ListView vocabulary = cardDialog.findViewById(R.id.kana_card_front_lv_vocabulary);
        KanaWordListViewAdapter kanaWordListViewAdapter =
                new KanaWordListViewAdapter(parentContext, wordListArray);
        vocabulary.setAdapter(kanaWordListViewAdapter);

            // Setting of the GridView adapter which displays the stroke order for a specific kana/kanji character
            //
        GridView strokeGrid = cardDialog.findViewById(R.id.kana_card_front_gv_stroke);
        StrokeOrderGridViewAdapter strokeOrderGridViewAdapter =
                new StrokeOrderGridViewAdapter(parentContext, kana.getStrokeOrderList());
        strokeGrid.setAdapter(strokeOrderGridViewAdapter);


        ImageButton drawBtn = cardDialog.findViewById(R.id.kana_card_front_ib_draw);
        drawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog drawingCard = new Dialog(parentContext);
                drawingCard.requestWindowFeature(Window.FEATURE_NO_TITLE);
                drawingCard.setContentView(R.layout.kana_card_drawingpractice_layout);
                drawingCard.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                PaintView paintView = drawingCard.findViewById(R.id.kana_card_drawingpractice_pv_canvas);
                DisplayMetrics metrics = new DisplayMetrics();
                ((Activity) parentContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);
                paintView.init(metrics);
                TextView kanjiChar = drawingCard.findViewById(R.id.kana_card_drawingpractice_tv_character);
                ImageView grid = drawingCard.findViewById(R.id.kana_card_drawingpractice_iv_grid);
                ImageButton clearBtn = drawingCard.findViewById(R.id.kana_card_drawingpractice_ib_clear);
                ImageButton visibilityBtn = drawingCard.findViewById(R.id.kana_card_drawingpractice_ib_visiblity);
                ImageButton gridBtn = drawingCard.findViewById(R.id.kana_card_drawingpractice_ib_grid);

                // Setting of the components of the drawing practice card
                //
                kanjiChar.setText(character);
                // Sets the adapter for the stroke order GridView
                //
                GridView strokeGrid = drawingCard.findViewById(R.id.kana_card_drawingpractice_gv_stroke);
                StrokeOrderGridViewAdapter strokeOrderGridViewAdapter =
                        new StrokeOrderGridViewAdapter(context, kana.getStrokeOrderList());
                strokeGrid.setAdapter(strokeOrderGridViewAdapter);

                clearBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(paintView.getPaths().size() != 0)
                            Toast.makeText(context, "Canvas cleared!", Toast.LENGTH_SHORT).show();
                        paintView.clear();
                    }
                });

                visibilityBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (characterVisible) {
                            kanjiChar.setVisibility(View.INVISIBLE);
                            visibilityBtn.setBackgroundResource(R.drawable.ic_outline_visibility_50);
                            characterVisible = false;
                        } else {
                            kanjiChar.setVisibility(View.VISIBLE);
                            visibilityBtn.setBackgroundResource(R.drawable.ic_outline_visibility_off_50);
                            characterVisible = true;
                        }
                    }
                });

                gridBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (gridVisible) {
                            grid.setVisibility(View.INVISIBLE);
                            gridBtn.setBackgroundResource(R.drawable.ic_baseline_grid_on_50);
                            gridVisible = false;
                        } else {
                            grid.setVisibility(View.VISIBLE);
                            gridBtn.setBackgroundResource(R.drawable.ic_baseline_grid_off_50);
                            gridVisible = true;
                        }
                    }
                });
                drawingCard.show();
            }
        });


            // Initialization of the button for the audio and the audio it plays
            //
        ImageButton audioBtn = cardDialog.findViewById(R.id.kana_card_front_ib_listen);
        String audioName = kana.getAudio();
        String audioUri = "@raw/" + audioName;
        int audioResource =
                context.getResources().
                        getIdentifier(audioUri, null, context.getPackageName());
        audioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(audioResource == 0) { return; }
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                    mediaPlayer = MediaPlayer.create(parentContext, audioResource);
                    mediaPlayer.start();
                } else {
                    mediaPlayer = MediaPlayer.create(parentContext, audioResource);
                    mediaPlayer.start();
                }
            }
        });

            // Initialization of the button that flips the card (front)
            //
        ImageButton flipBtnFront = cardDialog.findViewById(R.id.kana_card_front_ib_flip);
        flipBtnFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isBack) {
                    cardOutAnim.setTarget(cardFrontLayout);
                    cardInAnim.setTarget(cardBackLayout);
                    cardOutAnim.start();
                    cardInAnim.start();
                    isBack = true;
                } else {
                    cardOutAnim.setTarget(cardBackLayout);
                    cardInAnim.setTarget(cardFrontLayout);
                    cardOutAnim.start();
                    cardInAnim.start();
                    isBack = false;
                }
            }
        });

        // Setting of the back components of the card
        //
        ((TextView) cardDialog.findViewById(R.id.kana_card_back_tv_character))
                .setText(character);
        ((TextView) cardDialog.findViewById(R.id.kana_card_back_tv_mnemonictxt))
                .setText(mnemonicText);

        try {
            String imgUri = "@drawable/" + mnemonicImgName;
            int imageResource =
                    context.getResources().getIdentifier(imgUri, null, context.getPackageName());
            ImageView mnemonicImg = cardDialog.findViewById(R.id.kana_card_back_iv_mnemonic);
            Drawable res = context.getResources().getDrawable(imageResource);
            mnemonicImg.setImageDrawable(res);
        } catch (Resources.NotFoundException ex) { ex.printStackTrace(); }

            // Initialization of the button that flips the card (back)
            //
        ImageButton flipBtnBack = cardDialog.findViewById(R.id.kana_card_back_ib_flip);
        flipBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isBack) {
                    cardOutAnim.setTarget(cardFrontLayout);
                    cardInAnim.setTarget(cardBackLayout);
                    cardOutAnim.start();
                    cardInAnim.start();
                    isBack = true;
                } else {
                    cardOutAnim.setTarget(cardBackLayout);
                    cardInAnim.setTarget(cardFrontLayout);
                    cardOutAnim.start();
                    cardInAnim.start();
                    isBack = false;
                }
            }
        });

        cardDialog.show();
    }
}
