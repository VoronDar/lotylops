package com.lya_cacoi.lotylops.cards;

import static com.lya_cacoi.lotylops.declaration.consts.NOUN_LEVEL;

public class CultureCard extends Card implements TheoryHolder{
    public CultureCard() {
        super(null, null,NOUN_LEVEL,NOUN_LEVEL,0);
        setNullProgressCard();
    }

    private String theory;
    private GrammarCard.SelectTrain selectTrain;

    public CultureCard(String course, String id, int repeatlevel, int practiceLevel, int repetitionDat, String theory, GrammarCard.SelectTrain selectTrain) {
        super(course, id, repeatlevel, practiceLevel, repetitionDat);
        this.theory = theory;
        this.selectTrain = selectTrain;
    }


    public CultureCard(String course, String id, int repeatlevel, int practiceLevel, int repetitionDat) {
        super(course, id, repeatlevel, practiceLevel, repetitionDat);
    }
    public String getTheory() {
        return theory;
    }

    public void setTheory(String theory) {
        this.theory = theory;
    }

    public GrammarCard.SelectTrain getSelectTrain() {
        return selectTrain;
    }

    public void setSelectTrain(GrammarCard.SelectTrain selectTrain) {
        this.selectTrain = selectTrain;
    }
}