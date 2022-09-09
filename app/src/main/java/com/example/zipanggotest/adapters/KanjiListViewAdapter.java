package com.example.zipanggotest.adapters;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zipanggotest.Kanji;
import com.example.zipanggotest.PaintView;
import com.example.zipanggotest.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class KanjiListViewAdapter extends RecyclerView.Adapter<KanjiListViewAdapter.ViewHolder> {
    private Activity activity;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private Set<String> set;
    private String currentAcc;
    private String bookmarkName;
    private List<String> bookmarkList;
    private List<Kanji> kanjiCharacterList;
    private boolean isBack = false,
        characterVisible = true,
        gridVisible = true,
        bookmarkAdded = false;

    public KanjiListViewAdapter(Activity activity, List<Kanji> kanjiCharacterList) {
        this.activity = activity;
        this.kanjiCharacterList = kanjiCharacterList;
        sharedpreferences = activity.getSharedPreferences("Account", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        currentAcc = sharedpreferences.getString("currentAccount", null);
        bookmarkName = currentAcc + "_bookmark";
        set = sharedpreferences.getStringSet(bookmarkName, null);
        if(set != null) {
            bookmarkList = new ArrayList<>(set);
        } else {
            bookmarkList = new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View kanjiButton = layoutInflater.inflate(R.layout.kanjidictionary_menu_kanji_lv_button_layout, parent, false);
        return new ViewHolder(kanjiButton);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Context context = holder.itemView.getContext();
        Kanji kanjiData = kanjiCharacterList.get(position);
        holder.buttonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initCardDialog(activity, kanjiData);
            }
        });
        holder.kanji.setText(kanjiData.getCharacter());
        holder.stroke.setText(Integer.toString(kanjiData.getStrokeCount()));
        holder.meaning.setText(kanjiData.getMeaningList().toString());
        holder.onyomi.setText("On: " + kanjiData.getOnyomi());
        holder.kunyomi.setText("Kun: " + kanjiData.getKunyomi());
    }

    private void initCardDialog(Context context, Kanji kanji) {
        Kanji.JLPTLevel jlptLevel = kanji.getJlptLevel();
        String character = kanji.getCharacter();
//        int strokeCount = kanji.getStrokeCount();
        List<String> meaningList = kanji.getMeaningList();
        List<List<String>> vocabularyList = kanji.getVocabularyList();
        String kunyomi = kanji.getKunyomi();
        String onyomi = kanji.getOnyomi();
        String radical = kanji.getRadical();
        String part = kanji.getPart();
        String variant = kanji.getVariant();
        String mnemonicImg = kanji.getMnemonicImg();
        String mnemonicTxt = kanji.getMnemonicTxt();
        List<String> strokeOrderList = kanji.getStrokeOrderList();

        // Creates a pop-up dialog after a kanji button in the ListView has been clicked
        //
        Dialog cardDialog = new Dialog(activity);
        cardDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cardDialog.setContentView(R.layout.kanji_card_wmnemonic_layout);
        cardDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cardDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) { isBack = false; }
        });

        // Initialization of the front and back layout of the card along with its animations
        //
        View cardFrontLayout = cardDialog.findViewById(R.id.kanji_card_front);
        View cardBackLayout = cardDialog.findViewById(R.id.kanji_card_back);
        AnimatorSet cardOutAnim =
                (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_out);
        AnimatorSet cardInAnim =
                (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_in);
        float scale = context.getResources().getDisplayMetrics().density * 8000;
        cardFrontLayout.setCameraDistance(scale);
        cardBackLayout.setCameraDistance(scale);

        // Setting of the front components of the card
        //
        String jlptLevelStr;
        switch(jlptLevel) {
            case N5:
                jlptLevelStr = activity.getString(R.string.kanji_card_jlptlevel_n5);
                break;
            case N4:
                jlptLevelStr = activity.getString(R.string.kanji_card_jlptlevel_n4);
                break;
            case N3:
                jlptLevelStr = activity.getString(R.string.kanji_card_jlptlevel_n3);
                break;
            case N2:
                jlptLevelStr = activity.getString(R.string.kanji_card_jlptlevel_n2);
                break;
            default:
                jlptLevelStr = activity.getString(R.string.kanji_card_jlptlevel_n1);
        }
        ((TextView) cardDialog.findViewById(R.id.kanji_card_front_tv_jlptlevel)).setText(jlptLevelStr);
        ((TextView) cardDialog.findViewById(R.id.kanji_card_front_tv_kanjicharacter)).setText(character);
            // Removes the square brackets of the default toString() method
            //
        String kanjiMeaning = meaningList.toString();
        kanjiMeaning = kanjiMeaning.substring(1, kanjiMeaning.length() - 1);
        ((TextView) cardDialog.findViewById(R.id.kanji_card_front_tv_kanjimeaning)).setText(kanjiMeaning);
            // Finds and sets the correct image for the mnemonic illustration device
            //
        ((TextView) cardDialog.findViewById(R.id.kanji_card_front_tv_kanjimnemonictxt)).setText(mnemonicTxt);
        try {
            String imgUri = "@drawable/" + mnemonicImg;
            int imageResource =
                    context.getResources().getIdentifier(imgUri, null, context.getPackageName());
            ImageView mnemonicImgView = cardDialog.findViewById(R.id.kanji_card_front_tv_kanjimnemonicimg);
            Drawable res = context.getResources().getDrawable(imageResource);
            mnemonicImgView.setImageDrawable(res);
        } catch (Resources.NotFoundException ex) { ex.printStackTrace(); }
            // Sets the adapter for the vocabulary ListView (front)
            //
        ListView vocabulary = cardDialog.findViewById(R.id.kanji_card_front_lv_vocabularylist);
        KanjiWordListViewAdapter kanjiWordListViewAdapter =
                new KanjiWordListViewAdapter(context, vocabularyList);
        vocabulary.setAdapter(kanjiWordListViewAdapter);
            // Sets the adapter for the stroke order GridView
            //
        GridView strokeGrid = cardDialog.findViewById(R.id.kanji_card_front_gv_stroke);
        StrokeOrderGridViewAdapter strokeOrderGridViewAdapter =
                new StrokeOrderGridViewAdapter(context, strokeOrderList);
        strokeGrid.setAdapter(strokeOrderGridViewAdapter);
            // Initialization of the button that flips the card (front)
            //
        ImageButton flipBtnFront = cardDialog.findViewById(R.id.kanji_card_front_rl_ib_flip);
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

            // Listener for the drawing practice button (front)
            //
        ImageButton drawingBtnFront = cardDialog.findViewById(R.id.kanji_card_front_rl_ib_draw);
        drawingBtnFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog drawingCard = new Dialog(activity);
                drawingCard.requestWindowFeature(Window.FEATURE_NO_TITLE);
                drawingCard.setContentView(R.layout.kanji_card_drawingpractice_layout);
                drawingCard.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                PaintView paintView = drawingCard.findViewById(R.id.kanji_card_drawingpractice_pv_canvas);
                DisplayMetrics metrics = new DisplayMetrics();
                activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                paintView.init(metrics);
                TextView kanjiChar = drawingCard.findViewById(R.id.kanji_card_drawingpractice_tv_character);
                ImageView grid = drawingCard.findViewById(R.id.kanji_card_drawingpractice_iv_grid);
                ImageButton clearBtn = drawingCard.findViewById(R.id.kanji_card_drawingpractice_ib_clear);
                ImageButton visibilityBtn = drawingCard.findViewById(R.id.kanji_card_drawingpractice_ib_visiblity);
                ImageButton gridBtn = drawingCard.findViewById(R.id.kanji_card_drawingpractice_ib_grid);

                // Setting of the components of the drawing practice card
                //
                kanjiChar.setText(character);
                    // Sets the adapter for the stroke order GridView
                    //
                GridView strokeGrid = drawingCard.findViewById(R.id.kanji_card_drawingpractice_gv_stroke);
                StrokeOrderGridViewAdapter strokeOrderGridViewAdapter =
                        new StrokeOrderGridViewAdapter(context, strokeOrderList);
                strokeGrid.setAdapter(strokeOrderGridViewAdapter);

                clearBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(paintView.getPaths().size() != 0)
                            Toast.makeText(activity, "Canvas cleared!", Toast.LENGTH_SHORT).show();
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

            // Listener for the bookmark button (front)
            //
        ImageButton bookmarkBtnFront = cardDialog.findViewById(R.id.kanji_card_front_rl_ib_bookmark);
        if(!bookmarkList.contains(character)) {
            bookmarkAdded = false;
            bookmarkBtnFront.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
        } else {
            bookmarkAdded = true;
            bookmarkBtnFront.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
        }
        bookmarkBtnFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!bookmarkList.contains(character)) {
                    bookmarkList.add(character);
                    set = new HashSet<>(bookmarkList);
                    editor.putStringSet(bookmarkName, set).commit();
                    bookmarkAdded = true;
                    bookmarkBtnFront.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                    Toast.makeText(context, "Kanji character added to the bookmark", Toast.LENGTH_SHORT).show();
                } else {
                    //TODO: Once clicked again, removes kanji character from the bookmark
                    bookmarkList.remove(character);
                    set = new HashSet<>(bookmarkList);
                    editor.putStringSet(bookmarkName, set).commit();
                    bookmarkAdded = false;
                    bookmarkBtnFront.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                    Toast.makeText(context, "Kanji character removed from the bookmark", Toast.LENGTH_SHORT).show();
                    if(context.getClass().getSimpleName().equalsIgnoreCase("KanjiBookmarkActivity")){
                        kanjiCharacterList.remove(kanji);
                        notifyDataSetChanged();
                    }
                }
            }
        });

        // Setting of the back components of the card
        //
        ((TextView) cardDialog.findViewById(R.id.kanji_card_back_tv_jlptlevel)).setText(jlptLevelStr);
        ((TextView) cardDialog.findViewById(R.id.kanji_card_back_tv_kunyomi)).setText(kunyomi);
        ((TextView) cardDialog.findViewById(R.id.kanji_card_back_tv_onyomi)).setText(onyomi);
        ((TextView) cardDialog.findViewById(R.id.kanji_card_back_tv_meaning)).setText(kanjiMeaning);
        ((TextView) cardDialog.findViewById(R.id.kanji_card_back_tv_radical)).setText(radical);
        ((TextView) cardDialog.findViewById(R.id.kanji_card_back_tv_part)).setText(part);
        ((TextView) cardDialog.findViewById(R.id.kanji_card_back_tv_variant)).setText(variant);
            // Sets the adapter for the vocabulary ListView (back)
            //
        ListView vocabularyExtra = cardDialog.findViewById(R.id.kanji_card_back_lv_vocabularylist);
        KanjiWordExtraListViewAdapter kanjiWordExtraListViewAdapter =
                new KanjiWordExtraListViewAdapter(context, vocabularyList);
        vocabularyExtra.setAdapter(kanjiWordExtraListViewAdapter);

            // Initialization of the button that flips the card (back)
            //
        ImageButton flipBtnBack = cardDialog.findViewById(R.id.kanji_card_back_rl_ib_flip);
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

    @Override
    public int getItemCount() {
        return kanjiCharacterList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout buttonLayout;
        TextView kanji, stroke, meaning, onyomi, kunyomi;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.buttonLayout = itemView.findViewById(R.id.kanjidictionary_menu_kanji_rl_layout);
            this.kanji = itemView.findViewById(R.id.kanjidictionary_menu_kanji_rl_tv_character);
            this.stroke = itemView.findViewById(R.id.kanjidictionary_menu_kanji_rl_tv_stroke);
            this.meaning = itemView.findViewById(R.id.kanjidictionary_menu_kanji_rl_riv_meaning);
            this.onyomi = itemView.findViewById(R.id.kanjidictionary_menu_kanji_rl_riv_onyomi);
            this.kunyomi = itemView.findViewById(R.id.kanjidictionary_menu_kanji_rl_riv_kunyomi);
        }
    }
}
