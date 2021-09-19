package com.lya_cacoi.lotylops.SenteceCheck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Decoder {

    public static final String PARENT = "=";
    public static final String DIVIDER = "]";
    public static final String ARTICLE = "*";
    public static final String FRASE = "F";
    public static final String MODAL_VERB = "-";
    public static final String IS_QUESTION = "?";
    public static final String IS_EXCEPTION = "!";
    public static final String TYPE_END = ":";
    public static final String UNION = "/";


    public static ArrayList<Part> allParts = new ArrayList<>();


    public static Part decoder(String code) {
        /*
           "s:she]!pv:wants=0]av:live*to=1]"
           "s:she]pn:human*a-is=0]"
           "s:she]pv:running-is=0]st:fast*so=1]"
           s:we]pv:going-are=0]av:destroy*to=1]an:world*the=2]ad:entire=3]
           s:i]pv:want=0]av:connect*to=1]an:pictures*these=2]
            s:device*this]pv:should=0]av:be=1]an:here=2]st:you*with=2]
            s:i]pv:filled-am=0]st:strengths*with=1]
            s:i]pv:have=0]an:relationships*a=1]ad:good=2]st:parents*with=1]

            s:i]pv:will=0]av:be=1]an:father*a=2]ad:good=3]sn:children*your-for=2]ad:pretty=5]
            i will be a good father for your pretty children

            s:i]pv:going-am=0]av:magnify*to=1]an:image*this=2]
            s:i]pv:cut=0]an:you=1]sn:knife*a-with=1]

            s:i]!pv:bitten-was=0]
            s:i]pv:tell=0]sth:you=1]av:not*do=1]av:touch=3]an:children*the=4]

            s:i]pv:did=0]an:movement*this=1]ad:hard*really=2]svt:prove*to=1]an:possibilities*my=4]
            s:i]pv:did=0]an:movement*this=1]ad:hard*really=2]sv:prove*to/that=1]s:i=4]pv:deal-can=5]sn:difficulties*with=6]ad:any=7]

            s:i]pv:did=0]an:movement=1]sv:prove*to=1]s:i=3]pv:can=4]svt:tomorrow=5]

            s:i]pv:want=0]av:make*to=1]sn:gift*a=2]an:you=2]
            s:i]pv:see=0]an:you=1]stt:time*next=1]

            s:i]pv:see=0]an:you=1]stt:time*next=1]

            s:she]pv:fly-must not=0]


one
s:i]pn:god*a-am=0]
i am a good

one
s:i]pn:good-am not=0]
i am not good


one
s:i]pn:good-am=0]
i am good

one
s:i]?pn:good-am=0]
i am not good

s:i]?pv:going-am=0]av:magnify*to=1]an:image*this=2]

s:i]?pv:make-did=0]an:movement*this=1]ad:hard*really=2]sv:prove*to/that=1]s:i=4]pv:deal-can=5]sn:difficulties*with=6]ad:any=7]
s:i]?pv:make-did=0]an:movement*this=1]ad:hard*really=2]sv:=1]s:i=4]pv:deal-can=5]sn:difficulties*with=6]ad:any=7]

s:you]#pv:pv:got an:it=0]stt:champion=1]

s:]#pv:pv:take an:it st:easy=0]sv:/=1]s:everything=2]pv:be-will=3]st:ok=4]


        ** важно - если есть модальные have to и прочие - артикль не ставим


!!! делаем двойную проверку, определяется по кол-ву [


            *smt - артикль
            -is - модальный/вспомогательный глагол  или preposition (предлог)
            /that - союз
            =х  - родитель

            F в начале - неразрывная фраза
            t в самом начале - таргет
            ! - в начале - исключение
            ? в САМОМ САМОМ БТЬ начале - вопросительная форма (только у подлежащих)

            Кстати - в конструкции i am ADJECTIVE можно записать adjective как predicateNoun


            [s:this]pv:was=0]#st:a:a pn:piece p:of pn:cake=1]

            в классах обозначается так - допустим фраза является дополнением, тогда создается объект класса дополнения с пустым словом,
            на него вешаются linked объекты идущие друг за другом. Индекс дополнению вешается -1
            по наличию у объекта ненулевого linked проверка будет определять то, что само дополнение не является существующим элементом
            и будет проверять непосредственно linked элементы

s:this]pv:was=0]#an:a:a pn:piece p:of s:cake=1]

[s:this]#pv:a:a pn:piece p:of pn:cake -was=0]st:me-for=1]

s:]#pv:pv:is an:there=0]an:something=1]

s:]#pv:pv:is an:there=0]an:something=1]

s:]pv:leave=0]an:me=1]#st:st:alone st:alone=1]

#s:an:lord an:genry]#pv:pv:went p:off=0]#st:p:out p:of p:here=1]

s:i]#pn:s:james an:bond*a-am=0]

s:i]#pn:s:james an:bond*a-am=0]

s:i]pn:dog*a-am=0]#ad:an:very an:very an:nice=1]

s:she]pv:left=0]an:house*the=1]#stt:p:because p:of an:rain=1]


// st - не робит, s не робит, все остальное робит - ТЕПЕРЬ ВСЕ РОБИТ

// осталось только зделать обращения и бессоюзную связь предложений и збс



// ВАЖНО ПРО ТИПЫ st
stt - нормально воспримет наречие в конце или в начале предложения
str - нормально воспримет наречие только в начале предложения
stf - I am SETTING verb

sth - I told SETTING something


ПОСЛЕДНИЙ ЗНАК ПРЕПИНАНИЯ УДАЛЯЕТСЯ - ВСЕ ОСТАЛЬНЫЕ ЗНАКИ СТАНОВЯТСЯ ЗАПЯТЫМИ


         */




        //String coming = "s:you]pn:pretty*so`are=0]";
        allParts.clear();
        SentenceChecker.target = null;

        String coming = code.toLowerCase().substring(1);

        try {
            Pattern mask = Pattern.compile(DIVIDER);
            String[] parts = mask.split(coming);
            ArrayList<Part> preparedParts = new ArrayList<>(parts.length);
            //preparedParts.add(new Subject(getName(parts[0]), getArticle(parts[0]), isException(parts[0])));
            //allParts.add(preparedParts.get(0));

            //if (getArticle(parts[0]) != null)
            //    allParts.add(((Subject) preparedParts.get(0)).getArticle());

            for (int i = 0; i < parts.length; i++) {
                String nowStr = parts[i];
                //System.out.println(nowStr + ", size = " + preparedParts.size() + "type - " + getType(nowStr));
                Part part = ((i != 0)?preparedParts.get(getParentId(nowStr)):null);
                //System.out.println("decoding word " + nowStr);
                switch (getType(nowStr)) {
                    case SettingVerb:
                        SettingVerb settingV = new SettingVerb(getName(nowStr), getArticle(nowStr), getPreposition(nowStr), isException(nowStr), getSettingType(nowStr), setUnion(nowStr));
                        ((Verbable) part).setSetting(settingV);
                        preparedParts.add(settingV);
                        break;
                    case SettingNoun:
                        SettingNoun settingN = new SettingNoun(getName(nowStr), getArticle(nowStr), getPreposition(nowStr), getSettingType(nowStr), isException(nowStr));
                        ((Verbable) part).setSetting(settingN);
                        preparedParts.add(settingN);
                        break;
                    case Setting:
                        Setting setting = new Setting(getName(nowStr), getArticle(nowStr), getPreposition(nowStr), getSettingType(nowStr));
                        ((Verbable) part).setSetting(setting);
                        preparedParts.add(setting);
                        break;
                    case Subject:
                        Subject subject = new Subject(getName(nowStr), getArticle(nowStr), isException(nowStr));
                        if (i != 0)
                            ((SettingVerb) part).setSubject(subject);
                        preparedParts.add(subject);
                        break;
                    case PredicateNoun:
                        PredicateNoun predicateNoun = new PredicateNoun(getName(nowStr), getArticle(nowStr), getModalVerb(nowStr), isException(nowStr));
                        ((Subject) part).setPredicate(predicateNoun);
                        predicateNoun.setQuestion(isQuestion(nowStr));
                        preparedParts.add(predicateNoun);
                        break;
                    case PredicateVerb:
                        PredicateVerb predicate = new PredicateVerb(getName(nowStr), isException(nowStr));
                        ((Subject) part).setPredicate(predicate);
                        predicate.setModalVerb(getModalVerb(nowStr));
                        predicate.setQuestion(isQuestion(nowStr));
                        preparedParts.add(predicate);
                        break;
                    case AddictionNoun:
                        AddictionNoun addictionNoun = new AddictionNoun(getName(nowStr), getArticle(nowStr), isException(nowStr));
                        ((Verbable) part).setAddiction(addictionNoun);
                        preparedParts.add(addictionNoun);
                        break;
                    case AddictionVerb:
                        AddictionVerb addictionVerb = new AddictionVerb(getName(nowStr), getArticle(nowStr), isException(nowStr));
                        ((Verbable) part).setAddiction(addictionVerb);
                        preparedParts.add(addictionVerb);
                        break;
                    case Adjective:
                        Adjective adjective = new Adjective(getName(nowStr), getArticle(nowStr));
                        ((Nounable) part).setAdjective(adjective);
                        preparedParts.add(adjective);
                        break;
                }


                if (isFrase(nowStr)) {
                    Part linked = preparedParts.get(preparedParts.size() - 1);
                    String frase = linked.name;
                    linked.name = "";
                    linked.id = -2;
                    while (frase.contains(TYPE_END)) {
                        String nowString = frase.substring(0, (frase.indexOf(' ') != -1) ? frase.indexOf(' ') : frase.length());
                        if (frase.contains(" "))
                            frase = frase.substring(nowString.length() + 1);
                        else
                            frase = "";
                        Part newLinked;
                        System.out.println(nowString);
                        switch (getType(nowString)) {
                            case Subject:
                                newLinked = new Subject(getName(nowString), null, isException(nowString));
                                break;
                            case Setting:
                                newLinked = new Setting(getName(nowString), null, null, getSettingType(nowString));
                                break;
                            case Adjective:
                                newLinked = new Adjective(getName(nowString), null);
                                break;
                            case SettingNoun:
                                newLinked = new SettingNoun(getName(nowString), null, null, getSettingType(nowString), isException(nowString));
                                break;
                            case SettingVerb:
                                newLinked = new SettingVerb(getName(nowString), null, null, isException(nowString), getSettingType(nowString), null);
                                break;
                            case AddictionNoun:
                                newLinked = new AddictionNoun(getName(nowString), null, isException(nowString));
                                break;
                            case AddictionVerb:
                                newLinked = new AddictionVerb(getName(nowString), null, isException(nowString));
                                break;
                            case PredicateNoun:
                                newLinked = new PredicateNoun(getName(nowString), null, null, isException(nowString));
                                break;
                            case PredicateVerb:
                                newLinked = new PredicateVerb(getName(nowString), isException(nowString));
                                break;
                            case Article:
                                newLinked = new Article(getName(nowString));
                                break;
                            case ModalVerb:
                                String substr = nowString.substring(0, nowString.contains(":") ? nowString.indexOf(':') : nowString.indexOf('='));
                                if (substr.substring(0, nowString.indexOf(' ')).contains(" "))
                                    newLinked = new ModalVerb(getName(nowString), nowString.substring(nowString.indexOf(' ', nowStr.lastIndexOf(' '))));
                                else
                                    newLinked = new ModalVerb(getName(nowString), null);
                                break;
                            case Preposition:
                                newLinked = new Preposition(getName(nowString));
                                break;
                            default:
                                newLinked = null;
                        }
                        linked.setLinked(newLinked);
                        linked = newLinked;
                        allParts.add(linked);
                    }
                }

                if (preparedParts.get(preparedParts.size()-1).getName().length() == 0)
                    preparedParts.get(preparedParts.size()-1).id = 0;


                allParts.add(preparedParts.get(preparedParts.size() - 1));
                if (isTarget(nowStr))
                    SentenceChecker.target = preparedParts.get(preparedParts.size() - 1);
                if (getArticle(nowStr) != null)
                    try {
                        allParts.add(((ArticleHolder) preparedParts.get(preparedParts.size() - 1)).getArticle());
                    } catch (Exception ignored) {
                    }
                if (getModalVerb(nowStr) != null)
                    try {
                        allParts.add(((Predicate) preparedParts.get(preparedParts.size() - 1)).getModalVerb());
                    } catch (Exception ignored) {
                    }
                if (getPreposition(nowStr) != null)
                    try {
                        allParts.add(((Setting) preparedParts.get(preparedParts.size() - 1)).getPreposition());
                    } catch (Exception ignored) {
                    }
                if (setUnion(nowStr) != null)
                    try {
                        allParts.add(((SettingVerb) preparedParts.get(preparedParts.size() - 1)).getUnion());
                    } catch (Exception ignored) {
                    }
            }
        return preparedParts.get(0);
        } catch (Exception e){
            //Log.i("main", "decoder error: (" + coming + ") - message " + e.getMessage());
            System.out.println("decoder error: (" + coming + ") - cause " + e.getCause() + "\n - message " + e.getMessage() + "\n - stackTrace " + Arrays.toString(e.getStackTrace()));
            return null;
        }
    }

    private static String getName(String str){
        if (str.contains(ARTICLE))
            return str.substring(str.indexOf(TYPE_END) +1, str.indexOf(ARTICLE));
        else if (str.contains(MODAL_VERB))
            return str.substring(str.indexOf(TYPE_END) +1, str.indexOf(MODAL_VERB));
        else if (str.contains(UNION))
            return str.substring(str.indexOf(TYPE_END) +1, str.indexOf(UNION));
        else if (str.contains(PARENT))
            return str.substring(str.indexOf(TYPE_END) +1, str.indexOf(PARENT));
        return str.substring(str.indexOf(TYPE_END)+1);
    }
    private static int getParentId(String str){
        return Integer.parseInt(str.substring(str.indexOf(PARENT)+1));
    }

    enum Type{
        Subject,
        PredicateVerb,
        PredicateNoun,
        AddictionVerb,
        AddictionNoun,
        Adjective,
        Setting,
        SettingNoun,
        SettingVerb,
        ModalVerb,
        Article,
        Preposition
    }

    private static Type getType(String str) throws Exception {
        int start = 0;
        if (isTarget(str))
            start++;
        if (isException(str))
            start++;
        if (isQuestion(str))
            start++;
        if (isFrase(str))
            start++;
        switch (str.substring(start, start + 2)) {
            case "st":
                return Type.Setting;
            case "sn":
                return Type.SettingNoun;
            case "sv":
                return Type.SettingVerb;
            case "pv":
                return Type.PredicateVerb;
            case "pn":
                return Type.PredicateNoun;
            case "av":
                return Type.AddictionVerb;
            case "an":
                return Type.AddictionNoun;
            case "ad":
                return Type.Adjective;
        }
        if (str.charAt(start) == 's') return Type.Subject;
        else if (str.charAt(start) == 'a') return Type.Article;
        else if (str.charAt(start) == 'm') return Type.ModalVerb;
        else if (str.charAt(start) == 'p') return Type.Preposition;
        throw new Exception(str + " hasn't type, start " + start);
    }

    private static Article getArticle(String str){
        if (!str.contains(ARTICLE)) return null;
        if (str.contains(MODAL_VERB))
            return new Article(str.substring(str.indexOf(ARTICLE) +1, str.indexOf(MODAL_VERB)));
        if (str.contains(UNION))
            return new Article(str.substring(str.indexOf(ARTICLE) +1, str.indexOf(UNION)));
        if (str.contains(PARENT))
            return new Article(str.substring(str.indexOf(ARTICLE) +1, str.indexOf(PARENT)));
        return new Article(str.substring(str.indexOf(ARTICLE)+1));
    }

    private static ModalVerb getModalVerb(String str){
        // - have no -> haven't - записывается также have no - определяется прямо по нему

        if (!str.contains(MODAL_VERB)) return null;

        // ищет no или not
        if (str.substring(str.indexOf(MODAL_VERB)).contains(" ")) {
            if (str.contains(PARENT))
                return new ModalVerb(str.substring(str.indexOf(MODAL_VERB) + 1, str.indexOf(" ")),
                        str.substring(str.indexOf(" ") +1, str.indexOf(PARENT)));
            return new ModalVerb(str.substring(str.indexOf(MODAL_VERB) + 1, str.indexOf(" ")),
                    str.substring(str.indexOf(" ") +1));
        }
        if (str.contains(PARENT))
            return new ModalVerb(str.substring(str.indexOf(MODAL_VERB) + 1, str.indexOf(PARENT)), null);
        return new ModalVerb(str.substring(str.indexOf(MODAL_VERB)+1), null);
    }

    private static Preposition getPreposition(String str){
        if (!str.contains(MODAL_VERB)) return null;
        if (str.contains(UNION))
            return new Preposition(str.substring(str.indexOf(MODAL_VERB) +1, str.indexOf(UNION)));
        else if (str.contains(PARENT))
            return new Preposition(str.substring(str.indexOf(MODAL_VERB) +1, str.indexOf(PARENT)));
        return new Preposition(str.substring(str.indexOf(MODAL_VERB)+1));
    }

    private static Union setUnion(String str){
        if (!str.contains(UNION)) return null;
        if (str.contains(PARENT))
            return new Union(str.substring(str.indexOf(UNION) +1, str.indexOf(PARENT)));
        return new Union(str.substring(str.indexOf(UNION)+1));
    }

    private static SettingType getSettingType(String str){
        int charAdd = 0;
        if (isFrase(str)) charAdd++;
        if (isException(str)) charAdd++;
        if (isTarget(str)) charAdd++;
        if (isQuestion(str)) charAdd++;

        switch (str.charAt(2 + charAdd)){
            case 'f':
                return SettingType.frequency;
            case 'a':
                return SettingType.action;
            case 't':
                return SettingType.time;
            case 'h':
                return SettingType.person;
            case 'r':
                return SettingType.reason;
            default:
                return SettingType.place;
        }
    }


    // порядок - вопрос - таргет - исключение - фраза

    private static boolean isFrase(String str){
        return (str.charAt(0) == '#' || str.charAt(1) == '#' || (str.length() > 2 && str.charAt(2) == '#') || (str.length() > 3 && str.charAt(3) == '#'));
    }

    private static boolean isException(String str){
        return (str.charAt(0) == '!' || str.charAt(1) == '!' || (str.length() > 2 && str.charAt(2) == '!'));
    }
    private static boolean isTarget(String str){
        return (str.charAt(0) == 't' || (str.charAt(1) == 't' && str.charAt(0) != 's'));
    }
    private static boolean isQuestion(String str){
        return (str.charAt(0) == '?');
    }
}

