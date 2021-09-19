package com.lya_cacoi.lotylops.cards;

import java.util.ArrayList;

import static com.lya_cacoi.lotylops.declaration.consts.NOUN_LEVEL;

public class VocabularyCardNetUploader extends VocabularyCardNetBase {
    private String example;
    private String exampleTranslate;
    private String train;
    private String trainTranslate;


    private String dialogueSentence;
    private String dialogueRight;
    private String dialogueSecond;
    private String dialogueThird;
    private String dialogueFourth;

    public VocabularyCardNetUploader(VocabularyCard card) {
        super(card);
        if (card.getExamples() != null && card.getExamples().size() > 0) {
            this.example = card.getExamples().get(0).getSentence();
            this.exampleTranslate = card.getExamples().get(0).getTranslate();
        }

        if (card.getTrains() != null && card.getTrains().size() > 0) {
            this.train = card.getTrains().get(0).getSentence();
            this.trainTranslate = card.getTrains().get(0).getTranslate();
        }

        if (card.getDialogueTest() != null) {
            this.dialogueSentence = card.getDialogueTest().getAnswer();
            this.dialogueRight = card.getDialogueTest().getRight();
            this.dialogueSecond = card.getDialogueTest().getSecond();
            this.dialogueThird = card.getDialogueTest().getThird();
            this.dialogueFourth = card.getDialogueTest().getFourth();
        }
    }

    public VocabularyCardNetUploader() {
        super();
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getExampleTranslate() {
        return exampleTranslate;
    }

    public void setExampleTranslate(String exampleTranslate) {
        this.exampleTranslate = exampleTranslate;
    }

    public String getTrain() {
        return train;
    }

    public void setTrain(String train) {
        this.train = train;
    }

    public String getTrainTranslate() {
        return trainTranslate;
    }

    public void setTrainTranslate(String trainTranslate) {
        this.trainTranslate = trainTranslate;
    }

    public String getDialogueSentence() {
        return dialogueSentence;
    }

    public void setDialogueSentence(String dialogueSentence) {
        this.dialogueSentence = dialogueSentence;
    }

    public String getDialogueRight() {
        return dialogueRight;
    }

    public void setDialogueRight(String dialogueRight) {
        this.dialogueRight = dialogueRight;
    }

    public String getDialogueSecond() {
        return dialogueSecond;
    }

    public void setDialogueSecond(String dialogueSecond) {
        this.dialogueSecond = dialogueSecond;
    }

    public String getDialogueThird() {
        return dialogueThird;
    }

    public void setDialogueThird(String dialogueThird) {
        this.dialogueThird = dialogueThird;
    }

    public String getDialogueFourth() {
        return dialogueFourth;
    }

    public void setDialogueFourth(String dialogueFourth) {
        this.dialogueFourth = dialogueFourth;
    }


    public VocabularyCard transformToClassicCard(){

        ArrayList<Card.Sentence> examples = new ArrayList<>();
        if (getExample() != null)
            examples.add(new Card.Sentence(getExample(), getExampleTranslate(), false, null, null));
        ArrayList<Card.Sentence> trains = new ArrayList<>();
        if (getTrain() != null)
            trains.add(new Card.Sentence(getTrain(), getTrainTranslate(), false, null, null));

        GrammarCard.SelectTrain dialogueTest = null;
        if (getDialogueSentence() != null){
            dialogueTest = new GrammarCard.SelectTrain(null, getDialogueSentence(),
                    getDialogueRight(), getDialogueSecond(), getDialogueThird(), getDialogueFourth());
        }

        return new VocabularyCard(getCourse(), null, NOUN_LEVEL, NOUN_LEVEL, 0, getWord(),
                getMeaning(), getMeaningNative(), getTranslate(), getTranscription(), getSynonym(),
                getAntonym(), getHelp(), getGroup(), getPart(), examples, trains, null, dialogueTest);
    }

    @Override
    public String toString() {
        return "VocabularyCardNetUploader{" +
                "example='" + example + '\'' +
                ", exampleTranslate='" + exampleTranslate + '\'' +
                ", train='" + train + '\'' +
                ", trainTranslate='" + trainTranslate + '\'' +
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