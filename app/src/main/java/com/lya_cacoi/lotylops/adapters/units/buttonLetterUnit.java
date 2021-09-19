package com.lya_cacoi.lotylops.adapters.units;

public class buttonLetterUnit implements TestUnit {
    private String letter;
    private boolean pressed;
    private int wordPosition;

    public buttonLetterUnit(String letter) {
        this.letter = letter;
        pressed = false;
        wordPosition = 0;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public boolean isPressed() {
        return pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    public int getWordPosition() {
        return wordPosition;
    }

    public void setWordPosition(int wordPosition) {
        this.wordPosition = wordPosition;
    }

    @Override
    public boolean isRightPressed() {
        return false;
    }

    @Override
    public int getDifficult() {
        return 0;
    }
}
