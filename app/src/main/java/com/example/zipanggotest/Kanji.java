package com.example.zipanggotest;

import java.util.List;

public class Kanji implements Character {
    private JLPTLevel jlptLevel;
    private String character;
    private int strokeCount;
    private List<String> meaningList;
    private List<List<String>> vocabularyList;
    private String kunyomi;
    private String onyomi;
    private String radical;
    private String part;
    private String variant;
    private String mnemonicImg;
    private String mnemonicTxt;
    private List<String> strokeOrderList;

    public enum JLPTLevel {
        N5, N4, N3, N2, N1;
    }

    public Kanji(int jlptLevel, String character, int strokeCount, List<String> meaningList,
                 List<List<String>> vocabularyList, String kunyomi, String onyomi,
                 String radical, String part, String variant,
                 String mnemonicImg, String mnemonicTxt, List<String> strokeOrderList)
    {
        switch (jlptLevel) {
            case 1:
                this.jlptLevel = JLPTLevel.N1;
            case 2:
                this.jlptLevel = JLPTLevel.N2;
            case 3:
                this.jlptLevel = JLPTLevel.N3;
            case 4:
                this.jlptLevel = JLPTLevel.N4;
            default:
                this.jlptLevel = JLPTLevel.N5;
        }
        this.character = character;
        this.strokeCount = strokeCount;
        this.meaningList = meaningList;
        this.vocabularyList = vocabularyList;
        this.kunyomi = kunyomi;
        this.onyomi = onyomi;
        this.radical = radical;
        this.part = part;
        this.variant = variant;
        this.mnemonicImg = mnemonicImg;
        this.mnemonicTxt = mnemonicTxt;
        this.strokeOrderList = strokeOrderList;
    }

    public Kanji() {

    }

    public JLPTLevel getJlptLevel() {
        return jlptLevel;
    }

    public void setJlptLevel(JLPTLevel jlptLevel) {
        this.jlptLevel = jlptLevel;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public void setStrokeCount(int strokeCount) {
        this.strokeCount = strokeCount;
    }

    public List<String> getMeaningList() {
        return meaningList;
    }

    public void setMeaningList(List<String> meaningList) {
        this.meaningList = meaningList;
    }

    public List<List<String>> getVocabularyList() {
        return vocabularyList;
    }

    public void setVocabularyList(List<List<String>> vocabularyList) {
        this.vocabularyList = vocabularyList;
    }

    public String getKunyomi() {
        return kunyomi;
    }

    public void setKunyomi(String kunyomi) {
        this.kunyomi = kunyomi;
    }

    public String getOnyomi() {
        return onyomi;
    }

    public void setOnyomi(String onyomi) {
        this.onyomi = onyomi;
    }

    public String getRadical() {
        return radical;
    }

    public void setRadical(String radical) {
        this.radical = radical;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
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

    @Override
    public String getCharacter() {
        return character;
    }

    @Override
    public int getStrokeCount() {
        return strokeCount;
    }
}
