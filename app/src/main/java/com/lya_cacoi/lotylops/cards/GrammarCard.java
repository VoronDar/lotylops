package com.lya_cacoi.lotylops.cards;

import java.util.ArrayList;
import java.util.List;

import static com.lya_cacoi.lotylops.declaration.consts.NOUN_LEVEL;

public class GrammarCard extends Card implements TheoryHolder {
    public GrammarCard() {
        super(null, null,NOUN_LEVEL,NOUN_LEVEL,0);
        setNullProgressCard();
    }

    public static class SelectTrain{
        private String id;
        private String answer;
        private String right;
        private String second;
        private String third;
        private String fourth;

        public SelectTrain() {
            this.third = null;
            this.fourth = null;
        }

        public SelectTrain(String id, String answer, String right, String second, String third, String fourth) {
            this.id = id;
            this.answer = answer;
            this.right = right;
            this.second = second;
            this.third = third;
            this.fourth = fourth;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public String getRight() {
            return right;
        }

        public void setRight(String right) {
            this.right = right;
        }

        public String getSecond() {
            return second;
        }

        public void setSecond(String second) {
            this.second = second;
        }

        public String getThird() {
            return third;
        }

        public void setThird(String third) {
            this.third = third;
        }

        public String getFourth() {
            return fourth;
        }

        public void setFourth(String fourth) {
            this.fourth = fourth;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }


    private String theory;
    private ArrayList<SelectTrain> selectTrains;
    private ArrayList<Sentence> blockTrains;
    private List<String> selectTrain;
    private List<String>  placeTrain;
    private List<String>  trainsId;
    private String mistakeId;               // айдишник, по которому можно определять ошибки в sentenceTrain


    public GrammarCard(String course, String id, int repeatlevel, int practiceLevel, int repetitionDat) {
        super(course, id, repeatlevel, practiceLevel, repetitionDat);
    }

    public GrammarCard(String id, String course, String theory, List<String>  selectTrainsId,List<String>  blockTrainsId, List<String>  trainsId) {
        super(id, course);
        this.theory = theory;
        this.selectTrain = selectTrainsId;
        this.placeTrain = blockTrainsId;
        this.trainsId = trainsId;
    }

    public GrammarCard(String course, String id, int repeatlevel, int practiceLevel, int repetitionDat, String theory, ArrayList<SelectTrain> selectTrains, ArrayList<Sentence> blockTrains, ArrayList<Sentence> trains, List<String>  selectTrainsId, List<String>  blockTrainsId, List<String>  trainsId) {
        super(course, id, repeatlevel, practiceLevel, repetitionDat);
        this.theory = theory;
        this.selectTrains = selectTrains;
        this.blockTrains = blockTrains;
        this.trains = trains;
        this.selectTrain = selectTrainsId;
        this.placeTrain = blockTrainsId;
        this.trainsId = trainsId;
    }

    public void addStatic(String theory,List<String>  selectTrainsId, List<String>  blockTrainsId, List<String>  trainsId) {
        this.theory = theory;
        this.selectTrain = selectTrainsId;
        this.placeTrain = blockTrainsId;
        this.trainsId = trainsId;
    }

    public String getMistakeId() {
        return mistakeId;
    }

    public void setMistakeId(String mistakeId) {
        this.mistakeId = mistakeId;
    }

    public String getTheory() {
        return theory;
    }

    public void setTheory(String theory) {
        this.theory = theory;
    }

    public ArrayList<SelectTrain> getSelectTrains() {
        return selectTrains;
    }

    public void setSelectTrains(ArrayList<SelectTrain> selectTrains) {
        this.selectTrains = selectTrains;
    }

    public ArrayList<Sentence> getBlockTrains() {
        return blockTrains;
    }

    public void setBlockTrains(ArrayList<Sentence> blockTrains) {
        this.blockTrains = blockTrains;
    }

    public List<String>  getSelectTrainsId() {
        return selectTrain;
    }

    public void setSelectTrainsId(List<String>  selectTrainsId) {
        this.selectTrain = selectTrainsId;
    }

    public List<String>  getBlockTrainsId() {
        return placeTrain;
    }

    public void setBlockTrainsId(List<String>  blockTrainsId) {
        this.placeTrain = blockTrainsId;
    }

    public List<String>  getTrainsId() {
        return trainsId;
    }

    public void setTrainsId(List<String> trainsId) {
        this.trainsId = trainsId;
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
        if (getPracticeLevel() < getSelectTrainsId().size())
            return getSelectTrainsId().get(getPracticeLevel()-1);
        if (getPracticeLevel() < getTrainsId().size())
            return getBlockTrainsId().get(getPracticeLevel()-1);
        return getTrainsId().get(getPracticeLevel()-1);
    }

    @Override
    public String toString() {
        return "GrammarCard{" +
                "theory='" + theory + '\'' +
                ", selectTrains=" + selectTrains +
                ", blockTrains=" + blockTrains +
                ", selectTrain=" +selectTrain +
                ", placeTrain=" + placeTrain +
                ", trainsId=" + trainsId +
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

    public  List<String> getPlaceTrain() {
        return placeTrain;
    }


    public int getTestCount(){
        int count = 0;
        if (getSelectTrainsId()!= null)
            count += getSelectTrainsId().size();
        if (getBlockTrainsId()!= null)
            count += getBlockTrainsId().size();
        if (getTrainsId()!= null)
            count += getTrainsId().size();
        return count;
    }



}