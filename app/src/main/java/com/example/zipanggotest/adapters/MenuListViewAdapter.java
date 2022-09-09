package com.example.zipanggotest.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.zipanggotest.MenuButton;
import com.example.zipanggotest.R;
import com.google.android.material.imageview.ShapeableImageView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MenuListViewAdapter extends ArrayAdapter<MenuButton> {
    private final List<MenuButton> menuButtonList;
    private final LayoutInflater layoutInflater;

    public MenuListViewAdapter(@NonNull @NotNull Context context,
                               @NonNull @NotNull List<MenuButton> menuButtonList) {
        super(context, R.layout.menu_lv_button_layout, menuButtonList);
        this.menuButtonList = menuButtonList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return menuButtonList.size();
    }

    @Override
    public MenuButton getItem(int position) {
        return menuButtonList.get(position);
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = layoutInflater.inflate(R.layout.menu_lv_button_layout, null);
        MenuButton menuButton = menuButtonList.get(position);
        ShapeableImageView buttonIcon = view.findViewById(R.id.menu_iv_buttonIcon);
        TextView header = view.findViewById(R.id.menu_tv_buttonHeader);
        TextView description = view.findViewById(R.id.menu_tv_buttonDesc);

        buttonIcon.setImageDrawable(menuButton.getIcon());
        header.setText(menuButton.getHeader());
        description.setText(menuButton.getDescription());

        return view;
    }
}
