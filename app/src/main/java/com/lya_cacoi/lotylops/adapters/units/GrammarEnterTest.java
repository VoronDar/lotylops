package com.lya_cacoi.lotylops.adapters.units;

public class GrammarEnterTest implements TestUnit {
    private String question;
    private int rightIndex;
    private String firstAnswer;
    private String secondAnswer;
    private int pressed;
    private int difficult;

    public GrammarEnterTest(String question, int rightIndex, String firstAnswer, String secondAnswer, int difficult) {
        this.question = question;
        this.rightIndex = rightIndex;
        this.firstAnswer = firstAnswer;
        this.secondAnswer = secondAnswer;
        this.difficult = difficult;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getRightIndex() {
        return rightIndex;
    }

    public void setRightIndex(int rightIndex) {
        this.rightIndex = rightIndex;
    }

    public String getFirstAnswer() {
        return firstAnswer;
    }

    public void setFirstAnswer(String firstAnswer) {
        this.firstAnswer = firstAnswer;
    }

    public String getSecondAnswer() {
        return secondAnswer;
    }

    public void setSecondAnswer(String secondAnswer) {
        this.secondAnswer = secondAnswer;
    }

    public int getPressed() {
        return pressed;
    }

    public void setPressed(int pressed) {
        this.pressed = pressed;
    }

    public int getDifficult() {
        return difficult;
    }

    public void setDifficult(int difficult) {
        this.difficult = difficult;
    }

    @Override
    public boolean isRightPressed() {
        return getPressed()==getRightIndex();
    }
}
