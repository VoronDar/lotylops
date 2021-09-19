package com.lya_cacoi.lotylops.cards;

import java.util.ArrayList;

import static com.lya_cacoi.lotylops.declaration.consts.NOUN_LEVEL;


/*
ВАЖНО - используется одновременно для лексики и фразеологии
 */
public class VocabularyCard extends Card{
    private String word;
    private String meaning;
    private String meaningNative;
    private String translate;
    private String transcription;
    private String synonym;
    private String antonym;
    private String help;
    private String group;
    private String part;
    private ArrayList<Sentence> examples;
    private GrammarCard.SelectTrain dialogueTest;

    private Boolean meaningChange = false;
    private Boolean meaningNativeChange = false;
    private Boolean translateChange = false;
    private Boolean transcriptionChange = false;
    private Boolean synonymChange = false;
    private Boolean antonymChange = false;
    private Boolean helpChange = false;
    private Boolean groupChange = false;
    private Boolean partChange = false;

    public VocabularyCard(String course, String id, int repeatlevel, int practiceLevel, int repetitionDat) {
        super(course, id, repeatlevel, practiceLevel, repetitionDat);
    }


    public VocabularyCard(String course, String id, int repeatlevel, int practiceLevel, int repetitionDat, String word, String meaning, String meaningNative, String translate, String transcription, String synonym, String antonym, String help, String group, String part, ArrayList<Sentence> examples, ArrayList<Sentence> trains, String mem) {
        super(course, id, repeatlevel, practiceLevel, repetitionDat);
        put(word, meaning, meaningNative, translate, transcription, synonym, antonym, help, group,
                part, examples, trains, mem);
    }

    public VocabularyCard(String course, String id, int repeatlevel, int practiceLevel, int repetitionDat, String word, String meaning, String meaningNative, String translate, String transcription, String synonym, String antonym, String help, String group, String part, ArrayList<Sentence> examples, ArrayList<Sentence> trains, String mem, GrammarCard.SelectTrain dialogue) {
        super(course, id, repeatlevel, practiceLevel, repetitionDat);
        put(word, meaning, meaningNative, translate, transcription, synonym, antonym, help, group,
                part, examples, trains, mem);
        this.dialogueTest = dialogue;
    }

    public void addStatic(String word, String transcription, String meaning,
                          String meaningNative, String translate, String synonym, String antonym,
                          String mem, String help, String group, String part) {
        this.word = word;
        this.meaning = meaning;
        this.meaningNative = meaningNative;
        this.translate = translate;
        this.transcription = transcription;
        this.synonym = synonym;
        this.antonym = antonym;
        this.help = help;
        this.group = group;
        this.part = part;
        this.mem = mem;
    }


    public void addChanges(Boolean transcriptionC,
                          Boolean meaningC, Boolean meaningNativeC, Boolean translateC, Boolean synonymC,
                          Boolean antonymC, Boolean helpC, Boolean groupC, Boolean partC, boolean isMine) {

        this.meaningChange = meaningC;
        this.meaningNativeChange = meaningNativeC;
        this.translateChange = translateC;
        this.transcriptionChange = transcriptionC;
        this.synonymChange = synonymC;
        this.antonymChange = antonymC;
        this.helpChange = helpC;
        this.groupChange = groupC;
        this.partChange = partC;
        this.isMine = isMine;
    }



    private void put(String word, String meaning, String meaningNative, String translate, String transcription, String synonym, String antonym, String help, String group, String part, ArrayList<Sentence> examples, ArrayList<Sentence> trains, String mem) {
        this.word = word;
        this.meaning = meaning;
        this.meaningNative = meaningNative;
        this.translate = translate;
        this.transcription = transcription;
        this.synonym = synonym;
        this.antonym = antonym;
        this.help = help;
        this.group = group;
        this.part = part;
        this.examples = examples;
        this.trains = trains;
        this.mem = mem;
    }

    private void putNone() {
        this.word = null;
        this.meaning = null;
        this.meaningNative = null;
        this.translate = null;
        this.transcription = null;
        this.synonym = null;
        this.antonym = null;
        this.help = null;
        this.group = null;
        this.part = null;
        this.examples = null;
        this.trains = null;
        this.mem = null;
    }


    public VocabularyCard(String course, String id, String word, String meaning, String meaningNative, String translate, String transcription, String synonym, String antonym, String help, String group, String part, ArrayList<Sentence> examples, ArrayList<Sentence> trains, String mem) {
        super(course, id, NOUN_LEVEL, NOUN_LEVEL, 0);
        setNullProgressCard();
        put(word, meaning, meaningNative, translate, transcription, synonym, antonym, help, group,
                part, examples, trains, mem);
    }

    public VocabularyCard() {
        super(null, null,NOUN_LEVEL,NOUN_LEVEL,0);
        setNullProgressCard();
        putNone();
    }

    public VocabularyCard(String id) {
        super(null, id,NOUN_LEVEL,NOUN_LEVEL,0);
        setNullProgressCard();
        putNone();
    }
    public VocabularyCard(String id, String course) {
        super(course, id,NOUN_LEVEL,NOUN_LEVEL,0);
        setNullProgressCard();
        putNone();
    }

    public VocabularyCard(String id, String course, boolean isMine) {
        super(course, id,NOUN_LEVEL,NOUN_LEVEL,0);
        setNullProgressCard();
        putNone();
        this.isMine = isMine;
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

    public ArrayList<Sentence> getExamples() {
        return examples;
    }

    public void setExamples(ArrayList<Sentence> examples) {
        this.examples = examples;
    }

    public Boolean getMeaningChange() {
        return meaningChange;
    }

    public void setMeaningChange(Boolean meaningChange) {
        this.meaningChange = meaningChange;
    }

    public Boolean getMeaningNativeChange() {
        return meaningNativeChange;
    }

    public void setMeaningNativeChange(Boolean meaningNativeChange) {
        this.meaningNativeChange = meaningNativeChange;
    }

    public Boolean getTranslateChange() {
        return translateChange;
    }

    public void setTranslateChange(Boolean translateChange) {
        this.translateChange = translateChange;
    }

    public Boolean getTranscriptionChange() {
        return transcriptionChange;
    }

    public void setTranscriptionChange(Boolean transcriptionChange) {
        this.transcriptionChange = transcriptionChange;
    }

    public Boolean getSynonymChange() {
        return synonymChange;
    }

    public void setSynonymChange(Boolean synonymChange) {
        this.synonymChange = synonymChange;
    }

    public Boolean getAntonymChange() {
        return antonymChange;
    }

    public void setAntonymChange(Boolean antonymChange) {
        this.antonymChange = antonymChange;
    }

    public Boolean getHelpChange() {
        return helpChange;
    }

    public void setHelpChange(Boolean helpChange) {
        this.helpChange = helpChange;
    }

    public Boolean getGroupChange() {
        return groupChange;
    }

    public void setGroupChange(Boolean groupChange) {
        this.groupChange = groupChange;
    }

    public Boolean getPartChange() {
        return partChange;
    }

    public void setPartChange(Boolean partChange) {
        this.partChange = partChange;
    }

    public GrammarCard.SelectTrain getDialogueTest() {
        return dialogueTest;
    }

    public void setDialogueTest(GrammarCard.SelectTrain dialogueTest) {
        this.dialogueTest = dialogueTest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VocabularyCard that = (VocabularyCard) o;

        if (!word.equals(that.word)) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (meaning != null ? !meaning.equals(that.meaning) : that.meaning != null) return false;
        if (meaningNative != null ? !meaningNative.equals(that.meaningNative) : that.meaningNative != null)
            return false;
        if (translate != null ? !translate.equals(that.translate) : that.translate != null)
            return false;
        if (transcription != null ? !transcription.equals(that.transcription) : that.transcription != null)
            return false;
        if (synonym != null ? !synonym.equals(that.synonym) : that.synonym != null) return false;
        if (antonym != null ? !antonym.equals(that.antonym) : that.antonym != null) return false;
        if (help != null ? !help.equals(that.help) : that.help != null) return false;
        if (group != null ? !group.equals(that.group) : that.group != null) return false;
        if (part != null ? !part.equals(that.part) : that.part != null) return false;
        if (examples != null ? !examples.equals(that.examples) : that.examples != null)
            return false;
        if (trains != null ? !trains.equals(that.trains) : that.trains != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = word.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (meaning != null ? meaning.hashCode() : 0);
        result = 31 * result + (meaningNative != null ? meaningNative.hashCode() : 0);
        result = 31 * result + (translate != null ? translate.hashCode() : 0);
        result = 31 * result + (transcription != null ? transcription.hashCode() : 0);
        result = 31 * result + (synonym != null ? synonym.hashCode() : 0);
        result = 31 * result + (antonym != null ? antonym.hashCode() : 0);
        result = 31 * result + (help != null ? help.hashCode() : 0);
        result = 31 * result + (group != null ? group.hashCode() : 0);
        result = 31 * result + (part != null ? part.hashCode() : 0);
        result = 31 * result + (examples != null ? examples.hashCode() : 0);
        result = 31 * result + (trains != null ? trains.hashCode() : 0);
        return result;
    }

    public boolean isOverWrite(){
        return meaningChange || meaningNativeChange || transcriptionChange || translateChange ||
                antonymChange || synonymChange || helpChange || groupChange || partChange;
    }

    @Override
    public String toString() {
        return "VocabularyCard{" +
                "word='" + word + '\'' +
                ", meaning='" + meaning + '\'' +
                ", meaningNative='" + meaningNative + '\'' +
                ", translate='" + translate + '\'' +
                ", transcription='" + transcription + '\'' +
                ", synonym='" + synonym + '\'' +
                ", antonym='" + antonym + '\'' +
                ", help='" + help + '\'' +
                ", group='" + group + '\'' +
                ", part='" + part + '\'' +
                ", examples=" + examples +
                ", meaningChange=" + meaningChange +
                ", meaningNativeChange=" + meaningNativeChange +
                ", translateChange=" + translateChange +
                ", transcriptionChange=" + transcriptionChange +
                ", synonymChange=" + synonymChange +
                ", antonymChange=" + antonymChange +
                ", helpChange=" + helpChange +
                ", groupChange=" + groupChange +
                ", partChange=" + partChange +
                ", course='" + course + '\'' +
                ", id='" + id + '\'' +
                ", repeatlevel=" + repeatlevel +
                ", practiceLevel=" + practiceLevel +
                ", repetitionDat=" + repetitionDat +
                ", mem='" + mem + '\'' +
                ", trains=" + trains +
                '}';
    }


    private String linked;

    public void setLinked(String linked) {
        this.linked = linked;
    }

    public String getLinked() {
        return linked;
    }
}