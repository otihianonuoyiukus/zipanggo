package com.example.zipanggotest.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.zipanggotest.FlashcardActivity;
import com.example.zipanggotest.GettingStartedActivity;
import com.example.zipanggotest.KanjiBookmarkActivity;
import com.example.zipanggotest.KanjiDictionaryMenuActivity;
import com.example.zipanggotest.LessonActivity;
import com.example.zipanggotest.ReadingExerciseMenuActivity;
import com.example.zipanggotest.ReferenceSheetActivity;
import com.example.zipanggotest.MenuButton;
import com.example.zipanggotest.adapters.MenuListViewAdapter;
import com.example.zipanggotest.QuizActivity;
import com.example.zipanggotest.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class MenuStudyFragment extends Fragment {
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private String writingSystem;
    private String jlptLevel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sharedpreferences = getActivity().getSharedPreferences("Account", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        View view = inflater.inflate(R.layout.fragment_menu_study, container, false);
        ListView buttonList = view.findViewById(R.id.menu_study_lv_buttonList);
        buttonList.setAdapter(initMenuStudy(buttonList));
        buttonList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MenuButton menuButton = (MenuButton) parent.getItemAtPosition(position);
                Intent intentMenu = menuButton.getIntent();
                String header = menuButton.getHeader();
                switch (header) {
                    case "Reference Sheet":
                        initStudyDialog(intentMenu, getResources().getString(R.string.menu_study_cd_choice_referencesheet_header));
                        break;
                    case "Kanji Dictionary":
                        initKanjiDialog(intentMenu);
                        break;
                    case "Kanji Bookmark":
                        if(sharedpreferences.getString("currentAccount", null) == null) {
                            displayErrorDialog();
                            return;
                        }
                        startActivity(intentMenu);
                        break;
                    case "Flashcards":
                        initStudyDialog(intentMenu, getResources().getString(R.string.menu_study_cd_choice_flashcard_header));
                        break;
                    case "Quizzes":
                        initStudyDialog(intentMenu, getResources().getString(R.string.menu_study_cd_choice_quiz_header));
                        break;
                    default:
                        startActivity(intentMenu);
                        break;
                }
            }
        });
        return view;
    }

    private MenuListViewAdapter initMenuStudy(ListView buttonList) {
        List<MenuButton> menuButtonList = new ArrayList<>();

        // Getting Started Button
        addButton(menuButtonList, GettingStartedActivity.class, R.drawable.ic_gettingstarted_menu_button,
                getResources().getString(R.string.menu_study_button_gettingstarted_header),
                getResources().getString(R.string.menu_study_button_gettingstarted_desc)
        );

        // Reference Sheet Button
        addButton(menuButtonList, ReferenceSheetActivity.class, R.drawable.ic_menu_button_referencesheet,
                getResources().getString(R.string.menu_study_button_referencesheet_header),
                getResources().getString(R.string.menu_study_button_referencesheet_desc)
        );

        // Kanji Dictionary Button
        addButton(menuButtonList, KanjiDictionaryMenuActivity.class, R.drawable.ic_menu_button_kanjidictionary,
                getResources().getString(R.string.menu_study_button_kanjidictionary_header),
                getResources().getString(R.string.menu_study_button_kanjidictionary_desc)
        );

        // Bookmarks Button
        addButton(menuButtonList, KanjiBookmarkActivity.class, R.drawable.ic_menu_button_bookmark,
                getResources().getString(R.string.menu_study_button_bookmark_header),
                getResources().getString(R.string.menu_study_button_bookmark_desc)
        );

        // Flashcards Button
        addButton(menuButtonList, FlashcardActivity.class, R.drawable.ic_menu_button_flashcard,
                getResources().getString(R.string.menu_study_button_flashcard_header),
                getResources().getString(R.string.menu_study_button_flashcard_desc)
        );

        // Lessons Button
        addButton(menuButtonList, LessonActivity.class, R.drawable.ic_menu_button_lesson,
                getResources().getString(R.string.menu_study_button_lesson_header),
                getResources().getString(R.string.menu_study_button_lesson_desc)
        );

        // Reading Exercises Button
        addButton(menuButtonList, ReadingExerciseMenuActivity.class, R.drawable.ic_menu_button_readingexercise,
                getResources().getString(R.string.menu_study_button_readingexercise_header),
                getResources().getString(R.string.menu_study_button_readingexercise_desc)
        );

        // Quizzes Button
        addButton(menuButtonList, QuizActivity.class, R.drawable.ic_menu_button_quiz,
                getResources().getString(R.string.menu_study_button_quiz_header),
                getResources().getString(R.string.menu_study_button_quiz_desc)
        );

        return new MenuListViewAdapter(requireActivity(), menuButtonList);
    }

    private void addButton(List<MenuButton> buttonList, Class<?> cls, int resId, String header, String description) {
        Intent intent = new Intent(getActivity(), cls);
        Drawable icon = AppCompatResources.getDrawable(requireActivity(), resId);
        MenuButton button = new MenuButton(requireActivity(), intent, header, description, icon);
        buttonList.add(button);
    }

    // Creates and displays a dialog
    //
    private void initStudyDialog(Intent intent, String title) {
        writingSystem = "Hiragana";
        List<CheckBox> checkBoxList = new ArrayList<>();
        View customTitle = getLayoutInflater().inflate(R.layout.menu_cd_header_layout, null);
        TextView titleView =
                customTitle.findViewById(R.id.menu_cd_header_tv_title);
        MaterialAlertDialogBuilder flashcardDialog = new MaterialAlertDialogBuilder(requireActivity());
        View view = getLayoutInflater().inflate(R.layout.menu_study_cd_layout, null);
        flashcardDialog.setView(view);

        // Gets the writing system based on the selected radio button
        RadioGroup systemGroup = view.findViewById(R.id.menu_study_rl_rg_systemgroup);
        systemGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton systemButton =
                        view.findViewById(systemGroup.getCheckedRadioButtonId());
                writingSystem = (String) systemButton.getText();
            }
        });

        // Adds checkboxes to a list and makes sure that at least one checkbox is checked
        checkBoxList.add(view.findViewById(R.id.menu_study_cd_cb_basic));
        checkBoxList.add(view.findViewById(R.id.menu_study_cd_cb_dakuon));
        checkBoxList.add(view.findViewById(R.id.menu_study_cd_cb_handakuon));
        checkBoxList.add(view.findViewById(R.id.menu_study_cd_cb_combo));

        for(int i = 0; i <= 3; i++) {
            checkBoxList.get(i).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(!isChecked && !areAnyCheckboxChecked(checkBoxList)) {
                        buttonView.setChecked(true);
                    }
                }
            });
        }

        // Dialog box details
        //
        titleView.setText(title);
        flashcardDialog.setCustomTitle(customTitle);
        String positiveBtnTitle;
        if(title.equalsIgnoreCase(getResources().getString(R.string.menu_study_cd_choice_referencesheet_header))) {
            positiveBtnTitle = getResources().getString(R.string.choice_confirm_text);
        } else if(title.equalsIgnoreCase(getResources().getString(R.string.menu_study_cd_choice_quiz_header))) {
            positiveBtnTitle = getResources().getString(R.string.choice_take_text);
        } else {
            positiveBtnTitle = getResources().getString(R.string.choice_create_text);
        }

        // Puts the writing system string and sount type string array into the intent after confirming
        //
        flashcardDialog.setPositiveButton(positiveBtnTitle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringBuilder typeCSV = new StringBuilder();

                for (CheckBox cb : checkBoxList) {
                    if (cb.isChecked()) { typeCSV.append(((String) cb.getText()).toLowerCase()).append(","); }
                }
                intent.putExtra("SOUNDTYPE_STR", typeCSV.toString());
                switch (writingSystem) {
                    case "Hiragana":
                        intent.putExtra("SYSTEMTYPE_STR", "hiragana");
                        break;
                    case "Katakana":
                        intent.putExtra("SYSTEMTYPE_STR", "katakana");
                        break;
                }
                startActivity(intent);
            }
        });

        // Shows the dialog box
        //
        flashcardDialog.setNeutralButton(getResources().getString(R.string.choice_cancel_text), null);
        flashcardDialog.show();
    }

    // Creates and displays a dialog (Kanji)
    //
    private void initKanjiDialog(Intent intent) {
        String title = "Choose a Japanese-Language Proficiency Test Level";
        jlptLevel = "N5";
        View customTitle = getLayoutInflater().inflate(R.layout.menu_cd_headerwic_layout, null);
        TextView titleView = customTitle.findViewById(R.id.menu_cd_headerwic_tv_title);
        MaterialAlertDialogBuilder kanjiDialog = new MaterialAlertDialogBuilder(requireActivity());
        View view = getLayoutInflater().inflate(R.layout.menu_study_kanji_cd_layout, null);
        kanjiDialog.setView(view);

        // Gets the JLPT level based on the selected radio button
        //
        RadioGroup jlptGroup = view.findViewById(R.id.menu_study_kanji_rl_rg_jlptgroup);
        jlptGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton jlptButton =
                        view.findViewById(jlptGroup.getCheckedRadioButtonId());
                jlptLevel = (String) jlptButton.getText();
            }
        });

        // Dialog box details
        //
        titleView.setText(title);
        kanjiDialog.setCustomTitle(customTitle);
        String positiveBtnTitle;
        positiveBtnTitle = getResources().getString(R.string.choice_confirm_text);

        // Puts the JLPT Level string into the intent after confirming
        //
        kanjiDialog.setPositiveButton(positiveBtnTitle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                intent.putExtra("JLPTLEVEL_STR", jlptLevel);
                startActivity(intent);
            }
        });

        // Shows the dialog box
        //
        kanjiDialog.setNeutralButton(getResources().getString(R.string.choice_cancel_text), null);
        kanjiDialog.show();
        View customErrorTitle = getLayoutInflater().inflate(R.layout.menu_cd_header_layout, null);
        TextView errorTitleView = customErrorTitle.findViewById(R.id.menu_cd_header_tv_title);
        errorTitleView.setText("Help");
//        new MaterialAlertDialogBuilder(getActivity())
//                .setCustomTitle(customErrorTitle)
//                .setMessage("The Japanese-Language Proficiency Test, or JLPT, is a standardized criterion-referenced test to evaluate and certify Japanese language proficiency for non-native speakers, covering language knowledge, reading ability, and listening ability.\n\n" +
//                        "The JLPT has five levels: N1, N2, N3, N4 and N5. The easiest level is N5 and the most difficult level is N1. N4 and N5 measure the level of understanding of basic Japanese mainly learned in class. N1and N2 measure the level of understanding of Japanese used in a broad range of scenes in actual everyday life. N3 is a bridging level between N1/N2 and N4/N5.\n\n" +
//                        "N5 - The ability to understand some basic Japanese.\n" +
//                        "N4 - The ability to understand basic Japanese.\n" +
//                        "N3 - The ability to understand Japanese used in everyday situations to a certain degree.\n" +
//                        "N2 - The ability to understand Japanese used in everyday situations, and in a variety of circumstances to a certain degree.\n" +
//                        "N1 - The ability to understand Japanese used in a variety of circumstances.\n")
//                .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                })
//                .show();
    }

    private void displayErrorDialog() {
        View customErrorTitle = getLayoutInflater().inflate(R.layout.menu_cd_header_layout, null);
        TextView errorTitleView = customErrorTitle.findViewById(R.id.menu_cd_header_tv_title);
        errorTitleView.setText("Create/Choose an Account");
        new MaterialAlertDialogBuilder(getActivity())
                .setCustomTitle(customErrorTitle)
                .setMessage("This feature requires an account to be used. Create/Choose an account before proceeding.")
                .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    private boolean areAnyCheckboxChecked(List<CheckBox> checkBoxList) {
        for (CheckBox cb : checkBoxList) {
            if (cb.isChecked()) { return true; }
        }
        return false;
    }
}