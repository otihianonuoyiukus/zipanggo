package com.example.zipanggotest;

public class StaggeredButton {
    private String text;
    private int background;

    public StaggeredButton(String text, int background){
        this.text = text;
        this.background = background;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }
}
