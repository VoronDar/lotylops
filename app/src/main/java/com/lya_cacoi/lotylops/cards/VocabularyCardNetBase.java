package com.lya_cacoi.lotylops.cards;

public class VocabularyCardNetBase {
    protected String word;
    protected String meaning;
    protected String meaningNative;
    protected String translate;
    protected String transcription;
    protected String synonym;
    protected String antonym;
    protected String help;
    protected String group;
    protected String part;
    private String course;
    String id;

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    VocabularyCardNetBase(VocabularyCard card){
        this.word = card.getWord();
        this.meaning = card.getMeaning();
        this.meaningNative = card.getMeaningNative();
        this.translate = card.getTranslate();
        this.transcription = card.getTranscription();
        this.synonym = card.getSynonym();
        this.antonym = card.getAntonym();
        this.help = card.getHelp();
        this.group = card.getGroup();
        this.part = card.getPart();
    }

    public VocabularyCardNetBase(VocabularyCardNet card){
        this.word = card.getWord();
        this.meaning = card.getMeaning();
        this.meaningNative = card.getMeaningNative();
        this.translate = card.getTranslate();
        this.transcription = card.getTranscription();
        this.synonym = card.getSynonym();
        this.antonym = card.getAntonym();
        this.help = card.getHelp();
        this.group = card.getGroup();
        this.part = card.getPart();
        this.course = course;
    }

    VocabularyCardNetBase() {

    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getMeaningNative() {
        return meaningNative;
    }

    public void setMeaningNative(String meaningNative) {
        this.meaningNative = meaningNative;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    public String getTranscription() {
        return transcription;
    }

    public void setTranscription(String transcription) {
        this.transcription = transcription;
    }

    public String getSynonym() {
        return synonym;
    }

    public void setSynonym(String synonym) {
        this.synonym = synonym;
    }

    public String getAntonym() {
        return antonym;
    }

    public void setAntonym(String antonym) {
        this.antonym = antonym;
    }

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }
}