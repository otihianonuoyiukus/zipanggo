package com.example.zipanggotest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zipanggotest.adapters.AccountListViewAdapter;
import com.example.zipanggotest.adapters.MenuFragmentAdapter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class MenuActivity extends AppCompatActivity {
    private final static int MAX_ACCOUNT = 5;

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private Set<String> set;
    private String currentAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.activity_menu);

        sharedpreferences = getSharedPreferences("Account", MODE_PRIVATE);
        editor = sharedpreferences.edit();
        set = sharedpreferences.getStringSet("account", null);

        tabLayout = findViewById(R.id.menu_tl_tab);
        viewPager2 = findViewById(R.id.menu_vp2_viewpager);
        viewPager2.setOffscreenPageLimit(3);
        //viewPager2.setSaveEnabled(false);

        FragmentManager fragmentManager = getSupportFragmentManager();
        MenuFragmentAdapter menuFragmentAdapter =
                new MenuFragmentAdapter(MenuActivity.this, fragmentManager, getLifecycle());
        viewPager2.setAdapter(menuFragmentAdapter);

        initTab();

        if(sharedpreferences.getString("currentAccount", null) == null) {
            View customErrorTitle = getLayoutInflater().inflate(R.layout.menu_cd_header_layout, null);
            TextView errorTitleView = customErrorTitle.findViewById(R.id.menu_cd_header_tv_title);
            errorTitleView.setText("Create/Choose an Account");
            new MaterialAlertDialogBuilder(MenuActivity.this)
                    .setCustomTitle(customErrorTitle)
                    .setMessage("Please create/choose an account before proceeding. Some features are not available without an account")
                    .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) { }
                    })
                    .show();
        } else {
            currentAccount = sharedpreferences.getString("currentAccount", null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_account) {
            initAccountDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void initTab() {
        tabLayout.addTab(tabLayout.newTab().setText("Study"));
        tabLayout.addTab(tabLayout.newTab().setText("Games"));
        tabLayout.addTab(tabLayout.newTab().setText("Statistics"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }

    protected void initAccountDialog() {
        // Populates the ListView with the names of the existing accounts
        //
        View customTitle = getLayoutInflater().inflate(R.layout.menu_cd_header_layout, null);
        TextView titleView = customTitle.findViewById(R.id.menu_cd_header_tv_title);
        MaterialAlertDialogBuilder accountDialog = new MaterialAlertDialogBuilder(MenuActivity.this);
        View view = getLayoutInflater().inflate(R.layout.menu_account_layout, null);
        ListView accountListView = view.findViewById(R.id.menu_account_lv_accountlist);
        List<String> accountList;
        if(set != null) {
            accountList = new ArrayList<>(set);
        } else {
            accountList = new ArrayList<>();
        }
        AccountListViewAdapter accountListViewAdapter = new AccountListViewAdapter(MenuActivity.this, accountList);
        accountListView.setAdapter(accountListViewAdapter);
        // Takes the name of the account from the ListView and saves it into the currentAccount key in the SharedPreferences
        //
        accountListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String accName = (String)((TextView)(view.findViewById(R.id.menu_account_tv_profilename))).getText();
                if(accName.equalsIgnoreCase(currentAccount)) { return; }    // Prevents the pop-up menu from appearing if the currentAccount is the same as the accName
                PopupMenu popupMenu = new PopupMenu(MenuActivity.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.account_popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // Displays a dialog confirming of the account's deletion
                        //
                        if (item.getItemId() == R.id.account_delete) {
                            View customDelTitle = getLayoutInflater().inflate(R.layout.menu_cd_header_layout, null);
                            TextView delTitleView = customDelTitle.findViewById(R.id.menu_cd_header_tv_title);
                            delTitleView.setText("Delete Account");
                            new MaterialAlertDialogBuilder(MenuActivity.this)
                                    .setCustomTitle(customDelTitle)
                                    .setMessage("Are you sure you want to delete this account? All data from this account will be lost.")
                                    .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            accountList.remove(accName);
                                            set = new HashSet<>(accountList);
                                            //TODO: Sort the List before adding it in SharedPreferences
                                            editor.putStringSet("account", set).commit();
                                            //TODO: Delete the statistics of the deleted account too
                                            editor.remove(accName + "_bookmark").commit();
                                            editor.remove(accName + "_speed_hiragana").commit();
                                            editor.remove(accName + "_speed_katakana").commit();
                                            editor.remove(accName + "_match_hiragana").commit();
                                            editor.remove(accName + "_match_katakana").commit();
                                            editor.remove(accName + "_missing_easy").commit();
                                            editor.remove(accName + "_missing_medium").commit();
                                            editor.remove(accName + "_missing_hard").commit();
                                            editor.remove(accName + "_quizreport").commit();
                                            editor.remove(accName + "_roma2kanaspeedbeginner").commit();
                                            editor.remove(accName + "_roma2kanaspeedintermediate").commit();
                                            editor.remove(accName + "_roma2kanaspeedbeginner").commit();
                                            accountListViewAdapter.notifyDataSetChanged();
                                            Toast.makeText(MenuActivity.this, "Account deleted", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) { }
                                    })
                                    .show();

                        // Sets the currentAccount in the SharedPreferences
                        //
                        } else {
                            for(int i = 0; i < accountList.size(); i++) {
                                TextView profileDesc = parent.getChildAt(i).findViewById(R.id.menu_account_tv_profiledesc);
                                if (position == i)
                                    profileDesc.setText(getResources().getString(R.string.menu_account_desc_1));
                                else
                                    profileDesc.setText(getResources().getString(R.string.menu_account_desc_2));
                            }
                            currentAccount = accName;
                            editor.putString("currentAccount", accName).commit();
                            Toast.makeText(MenuActivity.this, "Account chosen", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
        TextView createButton = view.findViewById(R.id.menu_account_tv_create);
        // Displays a dialog asking the user for an account name
        //
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder createAccDialog = new MaterialAlertDialogBuilder(MenuActivity.this);
                View view = getLayoutInflater().inflate(R.layout.menu_account_create_layout, null);
                // Adds the account into the SharedPreferences by StringSet
                //
                createAccDialog.setPositiveButton("CREATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(accountList.size() >= MAX_ACCOUNT) {     // Checks if the account capacity has been reached.
                            Toast.makeText(MenuActivity.this, "Max account capacity reached", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        TextInputEditText accInput = view.findViewById(R.id.menu_account_create_tiet_textinput);
                        String accNameInput = (accInput.getText()).toString();
                        if(accNameInput.isEmpty()) {      // Checks if the account name input field is empty.
                            Toast.makeText(MenuActivity.this, "Please input a name", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(accountList.contains(accNameInput)) {      // Checks if the account name already exists.
                            Toast.makeText(MenuActivity.this, "Account already exists", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        accountList.add(accNameInput);
                        set = new HashSet<>(accountList);
                        //TODO: Sort the List before adding it in SharedPreferences
                        editor.putStringSet("account", set);
                        editor.commit();
                        accountListViewAdapter.notifyDataSetChanged();
                        Toast.makeText(MenuActivity.this, "Account created", Toast.LENGTH_SHORT).show();
                    }
                });
                createAccDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                });
                createAccDialog.setView(view);
                createAccDialog.show();
            }
        });
        titleView.setText(R.string.menu_account_header);
        accountDialog.setView(view);
        accountDialog.setCustomTitle(customTitle);
        accountDialog.show();
    }
}