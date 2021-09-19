package com.lya_cacoi.lotylops.cards;

import java.util.ArrayList;


/*
ВАЖНО - используется одновременно для лексики и фразеологии
 */
public class VocabularyCardNetHolder extends VocabularyCard{

    private VocabularyCardNet card;

    public VocabularyCardNetHolder(VocabularyCardNet card){
        this.card= card;
    }


    public String getWord() {
        return card.getWord();
    }

    public void setWord(String word) {
        card.setWord(word);
    }

    public String getMeaning() {
        return card.getMeaning();
    }

    public void setMeaning(String meaning) {
        card.setMeaning(meaning);
    }

    public String getMeaningNative() {
        return card.getMeaningNative();
    }

    public void setMeaningNative(String meaningNative) {
        card.setMeaningNative(meaningNative);
    }

    public String getTranslate() {
        return card.getTranslate();
    }

    public void setTranslate(String translate) {
        card.setTranslate(translate);
    }

    public String getTranscription() {
        return card.getTranscription();
    }

    public void setTranscription(String transcript) {
        card.setTranscription(transcript);
    }

    public String getSynonym() {
        return card.getSynonym();
    }

    public void setSynonym(String synonym) {
        card.synonym = synonym;
    }

    public String getAntonym() {
        return card.getAntonym();
    }

    public void setAntonym(String antonym) {
        card.antonym = antonym;
    }

    public String getHelp() {
        return card.getHelp();
    }

    public void setHelp(String help) {
        card.help = help;
    }

    public String getGroup() {
        return card.getGroup();
    }

    public void setGroup(String group) {
        card.group = group;
    }

    public String getPart() {
        return card.getPart();
    }

    public void setPart(String part) {
        card.part = part;
    }

    public ArrayList<Sentence> getExamples() {
        ArrayList<Sentence> list = new ArrayList<>();
        list.add(new Sentence(card.getExample(), card.getExampleTranslate(), false, null, null));
        return list;
    }

    public void setExamples(ArrayList<Sentence> examples) {
        if (examples.size() > 0) {
            card.setExample(examples.get(0).getSentence());
            card.setExampleTranslate(examples.get(0).getTranslate());
        }
    }

    public ArrayList<Sentence> getTrains() {
        ArrayList<Sentence> list = new ArrayList<>();
        list.add(new Sentence(card.getTrain(), card.getTrainTranslate(), false, null, null));
        return list;
    }

    public void setTrains(ArrayList<Sentence> trains) {
        if (trains.size() > 0) {
            card.setTrain(trains.get(0).getSentence());
            card.setTrainTranslate(trains.get(0).getTranslate());
        }
    }

    @Override
    public String toString() {
        return card.toString();
    }
}