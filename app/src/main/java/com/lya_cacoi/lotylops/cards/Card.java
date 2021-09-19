package com.lya_cacoi.lotylops.cards;

import com.lya_cacoi.lotylops.declaration.consts;

import java.util.ArrayList;

public class Card {

    public static class SentenceNet{
        private String linked_id;
        private String sentence;
        private String translate;

        public SentenceNet(String linked_id, String sentence, String translate) {
            this.linked_id = linked_id;
            this.sentence = sentence;
            this.translate = translate;
        }

        public SentenceNet() {
        }

        public String getLinked_id() {
            return linked_id;
        }

        public void setLinked_id(String linked_id) {
            this.linked_id = linked_id;
        }

        public String getSentence() {
            return sentence;
        }

        public void setSentence(String sentence) {
            this.sentence = sentence;
        }

        public String getTranslate() {
            return translate;
        }

        public void setTranslate(String translate) {
            this.translate = translate;
        }
    }

    public static class Sentence {
        private String sentence;
        private String translate;
        private Boolean change;
        private String id;
        private String linked_id = "";

        public Sentence(String sentence, String translate, Boolean change, String id, String linked_id) {
            this.sentence = sentence;
            this.translate = translate;
            this.change = change;
            this.id = id;
            this.linked_id = linked_id;
        }

        public Sentence() {
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Sentence sentence = (Sentence) o;

            if (!this.sentence.equals(sentence.sentence)) return false;
            if (!translate.equals(sentence.translate)) return false;
            if (!id.equals(sentence.id)) return false;
            return linked_id.equals(sentence.linked_id);
        }

        @Override
        public int hashCode() {
            int result = sentence.hashCode();
            result = 31 * result + translate.hashCode();
            result = 31 * result + id.hashCode();
            result = 31 * result + linked_id.hashCode();
            return result;
        }
        public String getSentence() {
            return sentence;
        }

        public void setSentence(String sentence) {
            this.sentence = sentence;
        }

        public String getTranslate() {
            return translate;
        }

        public void setTranslate(String translate) {
            this.translate = translate;
        }

        public Boolean getChange() {
            return change;
        }

        public void setChange(Boolean change) {
            this.change = change;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLinked_id() {
            return linked_id;
        }

        public void setLinked_id(String linked_id) {
            this.linked_id = linked_id;
        }

        @Override
        public String toString() {
            return "Sentence{" +
                    "sentence='" + sentence + '\'' +
                    ", translate='" + translate + '\'' +
                    ", change=" + change +
                    ", id='" + id + '\'' +
                    ", linked_id='" + linked_id + '\'' +
                    '}';
        }
    }


    protected String course;
    protected String id;
    protected int repeatlevel;
    protected int practiceLevel;
    protected int repetitionDat;
    protected String mem;
    protected ArrayList<Sentence> trains;



    public Card(String id, String course) {
        this.id = id;
        this.course = course;
    }


    public Card(String course, String id, int repeatlevel, int practiceLevel, int repetitionDat) {
        this.id = id;
        this.repeatlevel = repeatlevel;
        this.practiceLevel = practiceLevel;
        this.repetitionDat = repetitionDat;
        this.course = course;
    }

    protected void setNullProgressCard(){
        this.repeatlevel = consts.NOUN_LEVEL;
        this.practiceLevel = consts.NOUN_LEVEL;
        this.repetitionDat = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRepeatlevel() {
        return repeatlevel;
    }

    public void setRepeatlevel(int repeatlevel) {
        this.repeatlevel = repeatlevel;
    }

    public int getPracticeLevel() {
        return practiceLevel;
    }

    public void setPracticeLevel(int practiceLevel) {
        this.practiceLevel = practiceLevel;
    }

    public int getRepetitionDat() {
        return repetitionDat;
    }

    public void setRepetitionDat(int repetitionDat) {
        this.repetitionDat = repetitionDat;
    }

    public String getMem() {
        return mem;
    }

    public void setMem(String mem) {
        this.mem = mem;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id='" + id + '\'' +
                ", couse=" + course +
                ", repeatlevel=" + repeatlevel +
                ", practiceLevel=" + practiceLevel +
                ", repetitionDat=" + repetitionDat +
                ", mem='" + mem + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Card card = (Card) o;

        if (!course.equals(card.course)) return false;
        return id.equals(card.id);
    }

    @Override
    public int hashCode() {
        int result = course.hashCode();
        result = 31 * result + id.hashCode();
        return result;
    }

    public ArrayList<Sentence> getTrains() {
        return trains;
    }

    public void setTrains(ArrayList<Sentence> trains) {
        this.trains = trains;
    }


    // ник автора
    public String author = null;
    // является ли карта созданной пользователем с нуля
    public boolean isMine = false;

}
