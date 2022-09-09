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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zipanggotest.FlashcardActivity;
import com.example.zipanggotest.MenuActivity;
import com.example.zipanggotest.MenuButton;
import com.example.zipanggotest.MissingKanjiActivity;
import com.example.zipanggotest.RomaKanaMatchActivity;
import com.example.zipanggotest.adapters.MenuListViewAdapter;
import com.example.zipanggotest.QuizActivity;
import com.example.zipanggotest.R;
import com.example.zipanggotest.RomaKanaSpeedActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class MenuGameFragment extends Fragment {
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState)
    {
        sharedpreferences = getActivity().getSharedPreferences("Account", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu_game, container, false);
        ListView buttonList = view.findViewById(R.id.menu_game_lv_buttonList);
        buttonList.setAdapter(initMenuGame(buttonList));

        return view;
    }

    private MenuListViewAdapter initMenuGame(ListView buttonList) {
        List<MenuButton> menuButtonList = new ArrayList<>();

        // Roma2Kana Speed Game
        addButton(menuButtonList, RomaKanaSpeedActivity.class, R.drawable.ic_menu_button_speed,
                getResources().getString(R.string.menu_game_button_romakanaspeed_header),
                getResources().getString(R.string.menu_game_button_romakanaspeed_desc)
        );

        // Roma2Kana Match Game
        addButton(menuButtonList, RomaKanaMatchActivity.class, R.drawable.ic_menu_button_match,
                getResources().getString(R.string.menu_game_button_romakanamatch_header),
                getResources().getString(R.string.menu_game_button_romakanamatch_desc)
        );

        // Missing Kanji Game
        addButton(menuButtonList, MissingKanjiActivity.class, R.drawable.ic_menu_button_missingkanji,
                getResources().getString(R.string.menu_game_button_missingkanji_header),
                getResources().getString(R.string.menu_game_button_missingkanji_desc)
        );

        buttonList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(sharedpreferences.getString("currentAccount", null) == null) {
                    displayErrorDialog();
                    return;
                }
                MenuButton menuButton = (MenuButton) parent.getItemAtPosition(position);
                Intent intentMenu = menuButton.getIntent();
//                String header = menuButton.getHeader();
//                intentMenu.putExtra("SYSTEMTYPE_STR", "hiragana");
                startActivity(intentMenu);
            }
        });

        return new MenuListViewAdapter(requireActivity(), menuButtonList);
    }

    private void addButton(List<MenuButton> buttonList, Class<?> cls, int resId, String header, String description) {
        Intent intent = new Intent(getActivity(), cls);
        Drawable icon = AppCompatResources.getDrawable(requireActivity(), resId);
        MenuButton button = new MenuButton(requireActivity(), intent, header, description, icon);
        buttonList.add(button);
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
}