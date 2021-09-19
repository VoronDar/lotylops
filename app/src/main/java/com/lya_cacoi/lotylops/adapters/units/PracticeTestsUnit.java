package com.lya_cacoi.lotylops.adapters.units;

public class PracticeTestsUnit {
    private int cardCount;
    private String Chapter;
    private int ImageResourse;
    private Boolean Gone;
    private int ID;
    private int sqlIndex;

    public int getSqlIndex() {
        return sqlIndex;
    }

    public void setSqlIndex(int sqlIndex) {
        this.sqlIndex = sqlIndex;
    }

    public PracticeTestsUnit(int id, int cardCount, String chapter, Boolean gone, int image, int sqlIndex) {
        this.ID = id;
        this.cardCount = cardCount;
        Chapter = chapter;
        Gone = gone;
        ImageResourse = image;
        this.sqlIndex = sqlIndex;
    }


    public int getCardCount() {
        return cardCount;
    }

    public void setCardCount(int cardCount) {
        this.cardCount = cardCount;
    }

    public String getChapter() {
        return Chapter;
    }

    public void setChapter(String chapter) {
        Chapter = chapter;
    }

    public Boolean getGone() {
        return Gone;
    }

    public void setGone(Boolean gone) {
        Gone = gone;
    }

    public int getImageResourse() {
        return ImageResourse;
    }

    public void setImageResourse(int imageResourse) {
        ImageResourse = imageResourse;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
