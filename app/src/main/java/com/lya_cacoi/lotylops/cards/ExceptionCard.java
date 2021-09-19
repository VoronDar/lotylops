package com.lya_cacoi.lotylops.cards;

public class ExceptionCard extends Card {
    private String question;
    private String answer;

    // для тестирований
    public boolean isLastTest = true;

    public ExceptionCard(String course, String id, int repeatlevel, int practiceLevel, int repetitionDat, String question, String answer) {
        super(course, id, repeatlevel, practiceLevel, repetitionDat);
        this.question = question;
        this.answer = answer;
    }

    public ExceptionCard(String id, String course) {
        super(id, course);
        setNullProgressCard();
    }

    public ExceptionCard(){
        super(null, null);
        setNullProgressCard();
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
