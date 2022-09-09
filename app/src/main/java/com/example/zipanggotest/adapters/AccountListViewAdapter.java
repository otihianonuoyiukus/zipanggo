package com.example.zipanggotest.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.zipanggotest.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AccountListViewAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final LayoutInflater layoutInflater;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private final List<String> accountList;

    public AccountListViewAdapter(@NonNull @NotNull Context context,
                                  @NonNull @NotNull List<String> accountList)
    {
        super(context, R.layout.activity_game_score, accountList);
        this.context = context;
        this.accountList = accountList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return accountList.size();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return super.getItem(position);
    }



    @SuppressLint({"ViewHolder", "InflateParams"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        view = layoutInflater.inflate(R.layout.menu_account_lv_layout, null);
        String accNameStr = accountList.get(position);
        String currentAccount;
        TextView accName = view.findViewById(R.id.menu_account_tv_profilename);
        TextView accDesc = view.findViewById(R.id.menu_account_tv_profiledesc);
        sharedpreferences = context.getSharedPreferences("Account", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        currentAccount = sharedpreferences.getString("currentAccount", null);
        accName.setText(accNameStr);
        if(accNameStr.equalsIgnoreCase(currentAccount)) {
            accDesc.setText(((Activity)context).getResources().getString(R.string.menu_account_desc_1));
        } else {
            accDesc.setText(((Activity)context).getResources().getString(R.string.menu_account_desc_2));
        }

        return view;
    }
}
