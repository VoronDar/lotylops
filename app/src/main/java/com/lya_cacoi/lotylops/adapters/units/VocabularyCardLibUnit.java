package com.lya_cacoi.lotylops.adapters.units;

public class VocabularyCardLibUnit{
    private String word;
    private String translate;
    private String ID;
    private int level;
    private boolean isMine;

    public VocabularyCardLibUnit(String word, String translate, String ID, int level, boolean isMine) {
        this.word = word;
        this.translate = translate;
        this.ID = ID;
        this.level = level;
        this.isMine = isMine;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
