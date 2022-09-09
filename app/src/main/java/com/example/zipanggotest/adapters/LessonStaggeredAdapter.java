package com.example.zipanggotest.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zipanggotest.R;
import com.example.zipanggotest.StaggeredButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LessonStaggeredAdapter extends RecyclerView.Adapter<LessonStaggeredAdapter.StaggeredViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(StaggeredButton item);
    }

    private List<StaggeredButton> buttonList = new ArrayList<>();
    private final OnItemClickListener listener;

    public LessonStaggeredAdapter(List<StaggeredButton> buttonList, OnItemClickListener listener) {
        this.buttonList = buttonList;
        this.listener = listener;
    }

    @NonNull
    @NotNull
    @Override
    public StaggeredViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lesson_staggered_recyclerview_button, parent, false);

        return new StaggeredViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull LessonStaggeredAdapter.StaggeredViewHolder holder, int position) {
        holder.bind(buttonList.get(position), listener);
        holder.buttonImage.setImageResource(buttonList.get(position).getBackground());
        holder.buttonText.setText(buttonList.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return buttonList.size();
    }

    public class StaggeredViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout buttonLayout;
        RoundedImageView buttonImage;
        TextView buttonText;

        public StaggeredViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            buttonLayout = itemView.findViewById(R.id.lesson_rl_buttonlayout);
            buttonImage = itemView.findViewById(R.id.lesson_rl_riv_buttonbg);
            buttonText = itemView.findViewById(R.id.lesson_rl_tv_buttontext);
//            ViewGroup.LayoutParams params = buttonLayout.getLayoutParams();
//            params.height = 500;
//            buttonLayout.setLayoutParams(params);
//            buttonLayout.requestLayout();
        }

        public void bind(final StaggeredButton item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
