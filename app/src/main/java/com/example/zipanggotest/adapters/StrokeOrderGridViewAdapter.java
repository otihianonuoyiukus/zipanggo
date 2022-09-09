package com.example.zipanggotest.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.zipanggotest.R;

import java.util.List;

public class StrokeOrderGridViewAdapter extends BaseAdapter {
    Context context;
    LayoutInflater li;
    List<String> imgName;

    public StrokeOrderGridViewAdapter(Context applicationContext, List<String> imgName) {
        this.context = applicationContext;
        this.imgName = imgName;
        li = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return imgName.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = li.inflate(R.layout.stroke_order_grid_layout, null);

        try {
            String img = imgName.get(position);
            String uri = "@drawable/" + img;
            int imageResource =
                    context.getResources().getIdentifier(uri, null, context.getPackageName());
            ImageView strokeImg = view.findViewById(R.id.stroke_order_grid_iv_stroke);
            Drawable res = context.getResources().getDrawable(imageResource);
            strokeImg.setImageDrawable(res);
        } catch (Resources.NotFoundException ex) { ex.printStackTrace(); }

        return view;
    }
}
