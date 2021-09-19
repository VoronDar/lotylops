package com.lya_cacoi.lotylops.adapters.units;

public class toDoUnit {
    private boolean Removable;
    private String chapterName;
    private String toDo;
    private int index;
    private int cardCount;

    public toDoUnit(boolean removable, String chapterName, String toDo, int index, int cardCount) {
        Removable = removable;
        this.chapterName = chapterName;
        this.toDo = toDo;
        this.index = index;
        this.cardCount = cardCount;
    }

    public boolean isRemovable() {
        return Removable;
    }

    public void setRemovable(boolean removable) {
        Removable = removable;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getToDo() {
        return toDo;
    }

    public void setToDo(String toDo) {
        this.toDo = toDo;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getCardCount() {
        return cardCount;
    }

    public void setCardCount(int cardCount) {
        this.cardCount = cardCount;
    }
}
