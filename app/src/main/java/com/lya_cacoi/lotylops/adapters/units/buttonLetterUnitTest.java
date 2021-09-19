package com.lya_cacoi.lotylops.adapters.units;

public class buttonLetterUnitTest extends buttonLetterUnit implements TestUnit{
    private int difficult;

    public buttonLetterUnitTest(String letter, int difficult) {
        super(letter);
        this.difficult = difficult;
    }

    public int getDifficult() {
        return difficult;
    }

    public void setDifficult(int difficult) {
        this.difficult = difficult;
    }

    @Override
    public boolean isRightPressed() {
        return isPressed();
    }
}
