package com.lya_cacoi.lotylops.cards;

import java.util.ArrayList;
import java.util.List;

import static com.lya_cacoi.lotylops.declaration.consts.NOUN_LEVEL;

public class PhoneticsCard extends Card implements TheoryHolder{
    public PhoneticsCard() {
        super(null, null,NOUN_LEVEL,NOUN_LEVEL,0);
        setNullProgressCard();
    }
    private String theory;
    private String transcription;
    private ArrayList<GrammarCard.SelectTrain> selectTrains;
    private List<String> selectTrain;
    private List<String> wordExamples;
    private List<String> wordTrains;

    public String getTranscription() {
        return transcription;
    }

    public void setTranscription(String transcription) {
        this.transcription = transcription;
    }

    public void setSelectTrain(List<String> selectTrain) {
        this.selectTrain = selectTrain;
    }

    public void setWordExamples(List<String> wordExamples) {
        this.wordExamples = wordExamples;
    }

    public PhoneticsCard(String course, String id, int repeatlevel, int practiceLevel, int repetitionDat) {
        super(course, id, repeatlevel, practiceLevel, repetitionDat);
    }

    public PhoneticsCard(String id, String course, String theory, List<String>  selectTrainsId, List<String>  blockTrainsId, List<String> wordTrains) {
        super(id, course);
        this.theory = theory;
        this.selectTrain = selectTrainsId;
        this.wordExamples = blockTrainsId;
        this.wordTrains = wordTrains;
    }

    public PhoneticsCard(String course, String id, int repeatlevel, int practiceLevel, int repetitionDat, String theory, ArrayList<GrammarCard.SelectTrain> selectTrains, ArrayList<Sentence> trains, List<String>  selectTrainsId, List<String>  blockTrainsId, List<String> wordTrains) {
        super(course, id, repeatlevel, practiceLevel, repetitionDat);
        this.theory = theory;
        this.selectTrains = selectTrains;
        this.trains = trains;
        this.selectTrain = selectTrainsId;
        this.wordExamples = blockTrainsId;
        this.wordTrains = wordTrains;
    }

    public void addStatic(String theory,List<String>  selectTrainsId, List<String>  blockTrainsId, List<String>  trainsId) {
        this.theory = theory;
        this.selectTrain = selectTrainsId;
        this.wordExamples = blockTrainsId;
        this.wordTrains = trainsId;
    }

    public String getTheory() {
        return theory;
    }

    public void setTheory(String theory) {
        this.theory = theory;
    }

    public ArrayList<GrammarCard.SelectTrain> getSelectTrains() {
        return selectTrains;
    }

    public void setSelectTrains(ArrayList<GrammarCard.SelectTrain> selectTrains) {
        this.selectTrains = selectTrains;
    }

    public List<String>  getSelectTrainsId() {
        return selectTrain;
    }

    public void setSelectTrainsId(List<String>  selectTrainsId) {
        this.selectTrain = selectTrainsId;
    }

    public List<String>  getBlockTrainsId() {
        return wordExamples;
    }

    public void setBlockTrainsId(List<String>  blockTrainsId) {
        this.wordExamples = blockTrainsId;
    }

    public List<String> getWordTrains() {
        return wordTrains;
    }

    public void setWordTrains(List<String> wordTrains) {
        this.wordTrains = wordTrains;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return theory != null ? theory.hashCode() : 0;
    }


    public String getNowTrainId(){
        if (getSelectTrainsId() != null || getSelectTrainsId().size() > 0)
            return getSelectTrainsId().get(getPracticeLevel()-1);
        return getWordTrains().get(getPracticeLevel()-1);
    }

    @Override
    public String toString() {
        return "PhoneticsCard{" +
                "theory='" + theory + '\'' +
                ", selectTrains=" + selectTrains +
                ", selectTrain=" +selectTrain +
                ", wordExamples=" + wordExamples +
                ", wordTrains=" + wordTrains +
                ", course='" + course + '\'' +
                ", id='" + id + '\'' +
                ", repeatlevel=" + repeatlevel +
                ", practiceLevel=" + practiceLevel +
                ", repetitionDat=" + repetitionDat +
                ", mem='" + mem + '\'' +
                ", trains=" + trains +
                '}';
    }

    public List<String> getSelectTrain() {
        return selectTrain;
    }

    public  List<String> getWordExamples() {
        return wordExamples;
    }


    public int getTestCount(){
        if (getSelectTrainsId()!= null)
            return getSelectTrainsId().size();
        if (getWordTrains()!= null)
            return getWordTrains().size();
        return 0;
    }
}