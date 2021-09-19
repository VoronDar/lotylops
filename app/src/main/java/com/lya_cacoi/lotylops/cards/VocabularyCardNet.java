package com.lya_cacoi.lotylops.cards;

public class VocabularyCardNet extends VocabularyCardNetUploader {
    private String author;
    private int author_rate;

    public VocabularyCardNet(VocabularyCard card) {
        super(card);
        this.author = card.author;
    }

    public VocabularyCardNet() {
        super();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getAuthor_rate() {
        return author_rate;
    }

    public void setAuthor_rate(int author_rate) {
        this.author_rate = author_rate;
    }


    @Override
    public String toString() {
        return "VocabularyCardNet{" +
                "author='" + author + '\'' +
                ", author_rate=" + author_rate +
                ", word='" + word + '\'' +
                ", meaning='" + meaning + '\'' +
                ", meaningNative='" + meaningNative + '\'' +
                ", translate='" + translate + '\'' +
                ", transcription='" + transcription + '\'' +
                ", synonym='" + synonym + '\'' +
                ", antonym='" + antonym + '\'' +
                ", help='" + help + '\'' +
                ", group='" + group + '\'' +
                ", part='" + part + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}