package com.lya_cacoi.lotylops.SenteceCheck;

enum Type{
    noun, verb
}

enum VerbForm{
    infinite,
    past,
    ing,
    simple
}

enum NounForm{
    simple,
    multiple,
    attachment
}

interface Verbable{
    void setAddiction(Addiction addiction);
    void setSetting(Setting setting);
    Setting getSetting(); // получить обычный setting (обычный - в плане простого наречия)
    Addiction getAddiction();
}

interface ArticleHolder{
    Article getArticle();
}

interface Nounable{
    Adjective getAdjective();
    void setAdjective(Adjective adjective);
}

public abstract class Part{
    String name;
    Type type;
    int id = -1;
    Part back = null;
    int watched = 0;
    Part linked;
    boolean isException = false;
    Part(String name){
        this.name = name;
    }
    void setLinked(Part link){
        linked = link;
        link.back = this;
    }
    @Override
    public String toString() {
        if (back != null)
        return name  + ", parent =  " + back;
        else
            return name + "\n";
    }

    public String getName() {
        if (name != null && name.length() > 0 && linked == null)
            return name;
        else
            return getFraseName(linked, new StringBuilder()).deleteCharAt(0).toString();
    }

    private static StringBuilder getFraseName(Part part, StringBuilder string){
        if (part != null){
            string.append(" ").append(part.name);
            return getFraseName(part.linked, string);
        } else
            return string;
    }
}

// сюда закидываются не только артикли, но и to, this (и прочие), и притяжательные (my), всякие so, to (им владеют прилагательные и наречия)
class Article extends Part {
    Article(String name) {
        super(name);
    }
}

class ModalVerb extends Part {
    String none;
    ModalVerb(String name, String none){
        super(name);
        this.none = none;
    }

    @Override
    public String toString() {
        if (none == null) return name;
        return name + " " + none;
    }
}

class Preposition extends Part {
    Preposition(String name){
        super(name);
    }
}

class Union extends Part {
    Union(String name){
        super(name);
    }

}

class Subject extends Part implements Nounable, ArticleHolder {
    protected Article article = null;
    private Predicate predicate;
    private Adjective adjective = null;

    Subject(String name, Article article, boolean isException){
        super(name);
        this.type = Type.noun;
        setArticle(article);
        this.isException = isException;
    }

    public void setPredicate(Predicate predicate) {
        this.predicate = predicate;
        if (this.predicate != null)
            this.predicate.back = this;
    }

    public Predicate getPredicate() {
        return predicate;
    }

    @Override
    public Adjective getAdjective() {
        return adjective;
    }

    @Override
    public void setAdjective(Adjective adjective) {
        this.adjective = adjective;
        this.adjective.back = this;
    }
    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
        if (this.article != null)
            this.article.back = this;
    }
}


class Predicate extends Part {
    protected ModalVerb modalVerb = null;
    public Predicate(String name){
        super(name);
    }


    public ModalVerb getModalVerb() {
        return modalVerb;
    }

    public void setModalVerb(ModalVerb modalVerb) {
        this.modalVerb = modalVerb;
        if (this.modalVerb != null)
            this.modalVerb.back = this;
    }

    private boolean isQuestion = false;

    public boolean isQuestion() {
        return isQuestion;
    }

    public void setQuestion(boolean question) {
        isQuestion = question;
    }
}

class PredicateNoun extends Predicate implements Nounable, ArticleHolder {
    protected Article article = null;
    private Adjective adjective = null;
    public PredicateNoun(String name, Article article, ModalVerb modalVerb, boolean isException) {
        super(name);
        setArticle(article);
        setModalVerb(modalVerb);
        this.isException = isException;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
        if (this.article != null)
            this.article.back = this;
    }

    @Override
    public Adjective getAdjective() {
        return adjective;
    }

    @Override
    public void setAdjective(Adjective adjective) {
        this.adjective = adjective;
        this.adjective.back = this;
    }
}


class PredicateVerb extends Predicate implements Verbable {
    private Addiction addiction;
    private Setting setting;

    PredicateVerb(String name){
        super(name);
        this.type = Type.verb;
    }
    PredicateVerb(String name, boolean isException){
        this(name);
        this.isException = isException;
    }

    public void setAddiction(Addiction addiction) {
        this.addiction = addiction;
        if (this.addiction != null)
            this.addiction.back = this;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
        if (this.setting != null)
            this.setting.back = this;
    }

    public Setting getSetting() {
        return setting;
    }

    public Addiction getAddiction() {
        return addiction;
    }

}

class Adjective extends Part implements ArticleHolder {
    protected Article article = null;
    Adjective(String name, Article article){
        super(name);
        setArticle(article);
    }
    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
        if (this.article != null)
            this.article.back = this;
    }
}

abstract class Addiction extends Part implements ArticleHolder {
    protected Article article = null;
    Addiction(String name, boolean isException){
        super(name);
        this.isException = isException;
    }

    public Article getArticle() {
        return article;
    }
}

class AddictionVerb extends Addiction implements Verbable {
    private Addiction addiction;
    private Setting setting;

    AddictionVerb(String name, Article article, boolean isException) {
        super(name, isException);
        this.type = Type.verb;
        this.article = article;
        if (this.article != null)
            this.article.back = this;
    }
    public void setAddiction(Addiction addiction) {
        this.addiction = addiction;
        if (this.addiction != null)
            this.addiction.back = this;
    }

    public Addiction getAddiction() {
        return this.addiction;
    }

    public Setting getSetting() {
        return setting;
    }

    @Override
    public void setSetting(Setting setting) {
        this.setting = setting;
        if (this.setting != null)
            this.setting.back = this;
    }
}

class AddictionNoun extends Addiction implements Nounable {
    private Adjective adjective;

    AddictionNoun(String name, Article article, boolean isException) {
        super(name, isException);
        this.article = article;
        if (this.article != null)
            this.article.back = this;
    }


    @Override
    public Adjective getAdjective() {
        return adjective;
    }

    @Override
    public void setAdjective(Adjective adjective) {
        this.adjective = adjective;
        this.adjective.back = this;
    }
}


enum SettingType{
    place,
    time,
    action,
    reason,
    frequency,
    person,
}

class Setting extends Part implements ArticleHolder {
    private SettingType settingType;
    protected Article article = null;
    private Preposition preposition = null;
    Setting(String name, Article article, Preposition preposition, SettingType settingType){
        super(name);
        setArticle(article);
        setPreposition(preposition);
        this.settingType = settingType;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
        if (this.article != null)
            this.article.back = this;
    }

    public Preposition getPreposition() {
        return preposition;
    }

    public void setPreposition(Preposition preposition) {
        this.preposition = preposition;
        if (this.preposition != null)
            this.preposition.back = this;
    }

    public SettingType getSettingType() {
        return settingType;
    }

    @Override
    public String getName() {
        return (((preposition != null)?preposition.getName()+ " ":""))
                + (((article != null)?article.getName()+ " ":""))
                + super.getName();
    }

    public String getNameNormal() {
        return super.getName();
    }

}


class SettingNoun extends Setting implements Nounable {
    private Adjective adjective = null;

    SettingNoun(String name, Article article, Preposition preposition, SettingType settingType, boolean isException) {
        super(name, article, preposition, settingType);
        this.isException = isException;
    }

    @Override
    public Adjective getAdjective() {
        return adjective;
    }

    @Override
    public void setAdjective(Adjective adjective) {
        this.adjective = adjective;
        if (this.adjective != null)
            this.adjective.back = this;
    }
}

class SettingVerb extends Setting implements Verbable {

    private Addiction addiction;
    private Setting setting;
    private Subject subject;
    private Union union;

    SettingVerb(String name, Article article, Preposition preposition, boolean isException, SettingType settingType, Union union) {
        super(name, article, preposition, settingType);
        this.type = Type.verb;
        this.isException = isException;
        setArticle(article);
        setUnion(union);
    }
    public void setAddiction(Addiction addiction) {
        this.addiction = addiction;
        if (this.addiction != null)
            this.addiction.back = this;
    }

    public Addiction getAddiction() {
        return this.addiction;
    }

    public Setting getSetting() {
        return setting;
    }

    @Override
    public void setSetting(Setting setting) {
        this.setting = setting;
        if (this.setting != null)
            this.setting.back = this;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
        if (this.subject != null)
            this.subject.back = this;
    }

    public Union getUnion() {
        return union;
    }

    public void setUnion(Union union) {
        this.union = union;
        if (this.union != null)
            this.union.back = this;
    }
}

