package com.example.zipanggotest;

import java.util.List;

public class Kana implements Character {
    private String character;
    private List<String> transliterationList;
    private int strokeCount;
    private List<List<String>> wordList;
    private String mnemonicImg;
    private String mnemonicTxt;
    private List<String> strokeOrderList;
    private String audio;
    private SoundType soundType;

    enum SoundType {
        GOJUUON, DAKUON, HANDAKUON, YOUON
    }

    public Kana() {

    }

    public Kana(String character, List<String> transliterationList, int strokeCount,
                List<List<String>> wordList, String mnemonicImg, String mnemonicTxt,
                List<String> strokeOrderList, String audio, SoundType soundType)
    {
        this.character = character;
        this.transliterationList = transliterationList;
        this.strokeCount = strokeCount;
        this.wordList = wordList;
        this.mnemonicImg = mnemonicImg;
        this.mnemonicTxt = mnemonicTxt;
        this.strokeOrderList = strokeOrderList;
        this.audio = audio;
    }

    @Override
    public String getCharacter() {
        return character;
    }

    public List<String> getTransliterationList() {
        return transliterationList;
    }

    @Override
    public int getStrokeCount() {
        return strokeCount;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public void setTransliterationList(List<String> transliterationList) {
        this.transliterationList = transliterationList;
    }

    public void setStrokeCount(int strokeCount) {
        this.strokeCount = strokeCount;
    }

    public List<List<String>> getWordList() {
        return wordList;
    }

    public void setWordList(List<List<String>> wordList) {
        this.wordList = wordList;
    }

    public String getMnemonicImg() {
        return mnemonicImg;
    }

    public void setMnemonicImg(String mnemonicImg) {
        this.mnemonicImg = mnemonicImg;
    }

    public String getMnemonicTxt() {
        return mnemonicTxt;
    }

    public void setMnemonicTxt(String mnemonicTxt) {
        this.mnemonicTxt = mnemonicTxt;
    }

    public List<String> getStrokeOrderList() {
        return strokeOrderList;
    }

    public void setStrokeOrderList(List<String> strokeOrderList) {
        this.strokeOrderList = strokeOrderList;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public SoundType getSoundType() {
        return soundType;
    }

    public void setSoundType(SoundType soundType) {
        this.soundType = soundType;
    }

    public SoundType getDiacriticType() {
        return soundType;
    }

    public void setDiacriticType(SoundType diacriticType) {
        this.soundType = soundType;
    }
}