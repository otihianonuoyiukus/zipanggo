package com.example.zipanggotest;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

public class MenuButton {
  private Context context;
  private Intent intent;
  private String header, description;
  private Drawable icon;

  public MenuButton(Context context, Intent intent, String header, String description, Drawable icon) {
    this.context = context;
    this.intent = intent;
    this.header = header;
    this.description = description;
    this.icon = icon;
  }

  public Intent getIntent() {
    return intent;
  }

  public String getHeader() {
    return header;
  }

  public String getDescription() {
    return description;
  }

  public Drawable getIcon() {
    return icon;
  }
}
