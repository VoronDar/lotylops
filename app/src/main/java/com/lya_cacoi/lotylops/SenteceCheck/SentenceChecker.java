package com.lya_cacoi.lotylops.SenteceCheck;

import com.lya_cacoi.lotylops.SenteceCheck.Command.Command;
import com.lya_cacoi.lotylops.SenteceCheck.Command.GM;
import com.lya_cacoi.lotylops.SenteceCheck.Command.MistakeCommand;
import com.lya_cacoi.lotylops.SenteceCheck.Command.OrderCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.lya_cacoi.lotylops.SenteceCheck.Command.GM.CAN;
import static com.lya_cacoi.lotylops.SenteceCheck.Command.GM.DO_DOES;
import static com.lya_cacoi.lotylops.SenteceCheck.Command.GM.HAS_HAVE;
import static com.lya_cacoi.lotylops.SenteceCheck.Command.GM.MAY;
import static com.lya_cacoi.lotylops.SenteceCheck.Command.GM.MUST;
import static com.lya_cacoi.lotylops.SenteceCheck.Command.GM.ORDER;
import static com.lya_cacoi.lotylops.SenteceCheck.Command.GM.PREPOSITION;
import static com.lya_cacoi.lotylops.SenteceCheck.Command.GM.SETTING_ORDER;
import static com.lya_cacoi.lotylops.SenteceCheck.Command.GM.TO_BE;
import static com.lya_cacoi.lotylops.SenteceCheck.Command.GM.WAS;
import static com.lya_cacoi.lotylops.SenteceCheck.Decoder.ARTICLE;
import static com.lya_cacoi.lotylops.SenteceCheck.Decoder.allParts;



/*
добавь адекватную проверку форм для nounable

 */


public class SentenceChecker {


    static ArrayList<Command> commands = new ArrayList<>();
    public static Part part;
    static Part target = null;

    static int decStringCount = 0;

    public static void main(String []args) {
        System.out.println("Hello World");

        Scanner in = new Scanner(System.in);
        while (true) {
                String mode = in.nextLine();
                if (mode.equals("all")) {
                    Tester.setIsFullResult(Boolean.parseBoolean(in.nextLine()));
                    Tester.Test(in.nextLine());
                }
                else if (mode.equals("one")) {
                    String sentence = in.nextLine();
                    String check = in.nextLine();
                    Tester.TestForOne(sentence, check);
                }
                else if (mode.equals("gen")) {
                    String sentence = in.nextLine();
                    System.out.println(generateRightStringFromLib(sentence));
                }
                else if (mode.equals("decode")){
                    String sentence = in.nextLine();
                    Decoder.decoder(sentence);
                    System.out.println(allParts);
                }
            System.out.println("\ncompleted\n\n");
        }
    }

    public static boolean isTargetMistake(){
        if (target == null) return false;
        for (Command command: commands){
            if (command.main != null && command.main == target){
                return true;
            }
        }
        if (commands.size() > 2)
            return true;
        return false;
    }

    @Deprecated
    public static boolean check(String str, String decoder){
        target = null;
        Tester.reset();
        part = Decoder.decoder(decoder);

        // если случилась сатана во время проверки, то засчитать ответ, как правильный. Любой ответ

        try {
            ArrayList<String> checking = prepare(str);
            for (Check check : Check.values()) {
                check.check(checking, part);
            }
        } catch (Exception e) {
            System.out.println("PROBLEM + " + Arrays.toString(e.getStackTrace()));
            return true;
        }
        return SentenceChecker.isRight();
    }

    public static boolean check(String str, Part part){
        target = null;
        SentenceChecker.part = part;
        ArrayList<String> checking = prepare(str);
        for (Check check : Check.values()) {
            check.check(checking, part);
        }
        return SentenceChecker.isRight();
    }


    public static boolean isDecodable(String str){
        return (str.charAt(0) == '[');
    }

    public static String generateRightStringFromLib(String str){
        Tester.reset();
        SentenceChecker.part = Decoder.decoder(str);
        return getRightString();
    }

    // запускается в самом конце - поэтому все элементы уже имеют watched = 2
    public static String getRightString(){
        ArrayList<String> sentence = new ArrayList<>();
        Part now = part;
        while (now != null){
            if (now.linked != null);

            switch (now.getClass().getSimpleName()){
                case "Subject":
                    Subject subject = (Subject)now;
                    if (getReady(now)){
                        now.watched = 4;
                        if (subject.getArticle() != null)
                            sentence.add(subject.getArticle().getName());
                        if (subject.getAdjective() != null) {
                            if (subject.getAdjective().getArticle() != null)
                                sentence.add(subject.getAdjective().getArticle().getName());
                            sentence.add(subject.getAdjective().getName());
                        }
                        sentence.add(subject.getName());
                    }
                    if (getAvailable(subject.getPredicate()))
                        now = subject.getPredicate();
                    else
                        now = now.back;
                    break;
                case "PredicateNoun":
                    PredicateNoun predicateNoun = (PredicateNoun)now;
                    if (predicateNoun.getModalVerb() != null)
                        sentence.add(predicateNoun.getModalVerb().toString());
                    if (predicateNoun.getArticle() != null)
                        sentence.add(predicateNoun.getArticle().getName());
                    if (predicateNoun.getAdjective() != null) {
                        if (predicateNoun.getAdjective().getArticle() != null)
                            sentence.add(predicateNoun.getAdjective().getArticle().getName());
                        sentence.add(predicateNoun.getAdjective().getName());
                    }
                    sentence.add(predicateNoun.getName());
                    now = now.back;
                    break;
                case "AddictionNoun":
                    AddictionNoun addictionNoun = (AddictionNoun)now;
                    if (addictionNoun.getArticle() != null)
                        sentence.add(addictionNoun.getArticle().getName());
                    if (addictionNoun.getAdjective() != null){
                        if (addictionNoun.getAdjective().getArticle() != null)
                            sentence.add(addictionNoun.getAdjective().getArticle().getName());
                        sentence.add(addictionNoun.getAdjective().getName());
                    }
                    sentence.add(addictionNoun.getName());
                    now = now.back;
                    break;
                case "PredicateVerb":
                    PredicateVerb predicateVerb = (PredicateVerb)now;
                    if (getReady(now)) {
                        now.watched = 4;
                        if (predicateVerb.getModalVerb() != null)
                            sentence.add(predicateVerb.getModalVerb().toString());
                        if (predicateVerb.getSetting() != null && predicateVerb.getSetting().getSettingType() == SettingType.frequency) {
                            if (predicateVerb.getSetting().getPreposition() != null)
                                sentence.add(predicateVerb.getSetting().getPreposition().getName());
                            if (predicateVerb.getSetting().getArticle() != null)
                                sentence.add(predicateVerb.getSetting().getArticle().getName());
                            sentence.add(predicateVerb.getSetting().getName());

                            predicateVerb.getSetting().watched=4;
                        }
                        sentence.add(predicateVerb.getName());
                    }

                    if (getAvailable(predicateVerb.getAddiction()) &&
                            (!getReady(predicateVerb.getSetting()) ||
                                    ( predicateVerb.getSetting() != null &&
                                            predicateVerb.getSetting().getSettingType() != SettingType.person))) {
                        now = predicateVerb.getAddiction();
                    } else
                    if (getAvailable(predicateVerb.getSetting())) {
                        now = predicateVerb.getSetting();
                    }else
                        now = now.back;
                    break;
                case "AddictionVerb":
                    AddictionVerb addictionVerb = (AddictionVerb)now;
                    if (getReady(now)) {
                        now.watched = 4;
                        if (getAvailable(addictionVerb.getArticle()))
                            sentence.add(addictionVerb.getArticle().getName());
                        if (addictionVerb.getSetting() != null && addictionVerb.getSetting().getSettingType() == SettingType.frequency) {
                            if (addictionVerb.getSetting().getPreposition() != null)
                                sentence.add(addictionVerb.getSetting().getPreposition().getName());
                            if (addictionVerb.getSetting().getArticle() != null)
                                sentence.add(addictionVerb.getSetting().getArticle().getName());
                            sentence.add(addictionVerb.getSetting().getName());
                            addictionVerb.getSetting().watched=4;
                        }
                        sentence.add(addictionVerb.getName());

            }

                    if (getAvailable(addictionVerb.getAddiction()) &&
                            (!getReady(addictionVerb.getSetting()) ||
                                    ( addictionVerb.getSetting() != null &&
                                            addictionVerb.getSetting().getSettingType() != SettingType.person))) {
                        now = addictionVerb.getAddiction();
                    } else if (getAvailable(addictionVerb.getSetting())) {
                        now = addictionVerb.getSetting();
                    } else
                        now = now.back;
                    break;
                case "Setting":
                case "SettingNoun":
                    Setting settingNoun = (Setting)now;

                    if (settingNoun.getSettingType() == SettingType.reason){

                        sentence.add(0, settingNoun.getNameNormal());
                        if (settingNoun.getArticle() != null)
                            sentence.add(0, settingNoun.getArticle().getName());
                        if (settingNoun.getPreposition() != null)
                            sentence.add(0, settingNoun.getPreposition().getName());
                    } else {

                        if (settingNoun.getPreposition() != null)
                            sentence.add(settingNoun.getPreposition().getName());
                        if (settingNoun.getArticle() != null)
                            sentence.add(settingNoun.getArticle().getName());
                        sentence.add(settingNoun.getNameNormal());
                    }
                    now = now.back;


                    break;
                case "SettingVerb":
                    SettingVerb settingVerb = (SettingVerb)now;
                    if (getReady(now)) {
                        now.watched = 4;
                        if (settingVerb.getPreposition() != null)
                            sentence.add(settingVerb.getPreposition().getName());
                        if (settingVerb.getArticle() != null)
                            sentence.add(settingVerb.getArticle().getName());
                        sentence.add(settingVerb.getNameNormal());
                        if (settingVerb.getUnion() != null)
                            sentence.add(settingVerb.getUnion().getName());
                    }
                    if (getAvailable(settingVerb.getAddiction()))
                        now = settingVerb.getAddiction();
                    else if (getAvailable(settingVerb.getSubject()))
                        now = settingVerb.getSubject();
                    else if (getAvailable(settingVerb.getSetting()))
                        now = settingVerb.getSetting();
                    else
                        now = now.back;
                    break;

            }
            if (now != null && now.watched < 3)
                now.watched = 3;
        }
        StringBuilder builder = new StringBuilder();
        for (String s: sentence) {
            //if (s.contains(" "))
            //    builder.append(s);
            //else
                builder.append(" ").append(s);
        }
        return builder.toString();
    }

    private static boolean getAvailable(Part part){
        return part != null && part.watched < 3;
    }

    private static boolean getReady(Part part){
        return part != null && part.watched < 4;
    }

    private static void putCommand(Command command){
        commands.add(command);
    }

    // важно - после показа ошибок их закрывают
    public static String showCommand(){
        StringBuilder builder = new StringBuilder();
        for (Command a: commands) {
            if (a.getClass().getSimpleName().equals("MistakeCommand")) {
                if (((MistakeCommand) a).correctWriting.length() == 0) continue;
            } else if (a.getClass().getSimpleName().equals("OrderCommand")
                    && (((OrderCommand) a).mistakeLast.name.length() == 0
                    || a.main.name.length() == 0)) continue;
            a.show();
            builder.append(a.name).append("\n");
        }
        return builder.toString();
    }
    public static boolean isGrammarMistakes(){
        for (Command a: commands) {
            if (a.isGrammarMistake()) return true;
        }
        return false;
    }
    public static ArrayList<String> getAllGrammarMistakes(){
        ArrayList<String> mistakes = new ArrayList<>();
        for (Command a: commands) {
            if (a.isGrammarMistake())
                mistakes.add(a.mistakeId);
        }
        return mistakes;
    }

    public static boolean isRight(){

        try {
            if (commands.size() > 0) {
                for (int i = 0; i < commands.size(); i++) {
                    if (commands.get(i).main == null) continue;
                    if (commands.get(i).main.name.length() != 0) return false;
                }
                return true;
            }
        } catch (Exception e){
            System.out.println("something went wrong " + e.getMessage() + "\n\n" + Arrays.toString(e.getStackTrace()));
            System.out.println("--- > " + commands);
            return true;
        }

        return true;
    }



    static ArrayList<String> prepare(String checking){

        String check = checking.toLowerCase();

        ArrayList<String> strings = new ArrayList<>();
        int last = 0;
        for (int i = 0; i < check.length() - 1; i++){
            if (check.charAt(i) == ' '){
                strings.add(check.substring(last, i));
                last = i+1;
            }
        }



        strings.add(check.substring(last));
        return strings;
    }

    static HashMap<Integer, Part> id;
    static ArrayList<Part> unfounded_id;
    static ArrayList<Integer> empty_id;
    public enum Check{
        find{
            @Override
            void check(ArrayList<String> string, Part sentence) {
                //Part now = sentence;
                id = new HashMap(string.size());
                unfounded_id = new ArrayList<>();
                empty_id = new ArrayList<>();


                int times = 0;
                for (Part now: allParts){
                    if (now.name.length() == 0 && now.linked != null){
                        //System.out.println("there is no subjects");
                        now.watched = 1;
                        if (!id.containsKey(string.indexOf(now.linked.name)))
                            now.id = string.indexOf(now.linked.name);
                        else
                            now.id = string.lastIndexOf(now.linked.name);

                        if (now.id != -1) {
                            string.add(now.id, "%" + ++times);
                            now.name = "%" + times;
                        }
                    }
                }

                for (Part now: allParts){

                    if (now.getClass().getSimpleName().equals("Subject") && now.getName().length() == 0 && now.linked == null){
                        //System.out.println("there is no subjects");
                        now.id = 0;
                        id.put(now.id, now);
                        now.watched = 1;
                        string.add(0, "$");
                        now.name = "$";
                        continue;
                    }

                    if (now.getClass().getSimpleName().equals("ModalVerb")) {
                        // родитель модального глагола должен провериться после него, чтобы избежать ложных обвинений в несовпадении форм
                        if (unfounded_id.contains(getClosestSubject(now)))
                            unfounded_id.remove(getClosestSubject(now));
                        unfounded_id.add(now);
                        unfounded_id.add(getClosestSubject(now));
                        continue;
                    }
                    if (now.id != -2) {
                        if (!id.containsKey(string.indexOf(now.name)))
                            now.id = string.indexOf(now.name);
                        else
                            now.id = string.lastIndexOf(now.name);
                    }



                    if (now.id != -1) {
                        id.put(now.id, now);
                        now.watched = 1;
                    }
                    else {
                        unfounded_id.add(now);
                    }
                }

            }
            },

            checkMistakes {
                @Override
                void check(ArrayList<String> string, Part sentence) {
                    ArrayList<Part> other_unfounded_id = unfounded_id;
                    unfounded_id = new ArrayList<>();
                    for (Part now : other_unfounded_id) {
                        switch (now.getClass().getSimpleName()) {
                            case "Subject":
                            case "PredicateNoun":
                            case "AddictionNoun":
                                checkNounable(now, string);
                                break;
                            case "SettingVerb":
                            case "PredicateVerb":
                            case "AddictionVerb":
                                checkVerbable(now, string);
                                break;
                            case "SettingNoun":
                            case "Setting":
                            case "Adjective":
                                for (int i = 0; i < string.size(); i++) {
                                    if (id.containsKey(i)) continue;
                                    if (checkUnion(string.get(i), now, i)) {
                                        now.id = i;
                                        id.put(now.id, now);
                                        break;
                                    }
                                }
                                if (now.id == -1) {
                                    putCommand(new MistakeCommand("'" + now.name + "' не найдено", now, now.name, null));
                                    unfounded_id.add(now);
                                }
                                break;
                            case "Preposition":
                                checkPreposition((Preposition) now, string);
                                break;
                            case "ModalVerb":
                                checkModal((ModalVerb) now, string);
                                break;
                            case "Article":
                                checkServicePart(now, string);
                                break;
                        }
                        now.watched = 1;
                    }

                    // если слов больше, чем нашлось (всегда проверяется ибо фразы)
                    ArrayList<String> template = (ArrayList<String>) string.clone();
                    empty_id.clear();
                        for (int i = 0; i < template.size(); i++) {
                            if (!id.containsKey(i)) {
                                putCommand(new Command("найдена неизвестная последовательность " + template.get(i), null, null));
                                empty_id.add(i);
                            }
                        }

                    if (unfounded_id.size() == 1 && empty_id.size() == 1) {
                        System.out.println("let '" + template.get(empty_id.get(0)) + "' be '" + unfounded_id.get(0).name);
                        unfounded_id.get(0).id = empty_id.get(0);
                        id.put(empty_id.get(0), unfounded_id.get(0));
                        empty_id.clear();
                        unfounded_id.clear();
                    }
                }

                private boolean isSynonym(String checking, Part part) {
                    /*
                    if (part.isException)
                        ArrayList<String> synonyms = transportSql.getCardSynonymsFromException(part.name);
                    else if (getVerbForm(part.name) == VerbForm.infinite) {
                        ArrayList<String> synonyms = transportSql.getCardSynonyms(part.name);
                    } else{
                        ArrayList<String> synonyms = tryCastToInfAndFindSynonyms(part.name));
                    }

                     */

                    if (part.type == Type.verb)
                        System.out.println(tryCastToInfAndFindSynonyms(part.name));


                    ArrayList<String> synonyms = new ArrayList<>();
                    //if (part.type == Type.verb)
                    //    synonyms.add("hug");
                    if (synonyms.size() == 0) return false;
                    for (String s : synonyms) {
                        if (part.type == Type.verb) {
                            VerbForm vf = getVerbForm(part.name);
                            if (s.equals(checking) && vf== VerbForm.infinite)
                                return true;
                            String prepared = verbFormBuilding(s, getVerbForm(part.name));
                            if (prepared.equals(checking)) return true;
                            if (verbFormsInaccordance(prepared, checking, false)) {
                                putCommand(new MistakeCommand(checking + " - " + prepared + ": " +
                                        "несовпадение форм или ошибка при образовании формы", part, checking,
                                        ((vf == VerbForm.infinite)? GM.VERB_INF:
                                                ((vf == VerbForm.ing)? GM.VERB_ING: GM.VERB_PAST))));
                                return true;
                            }
                        }
                        if (part.type == Type.noun) {
                        /*
                        if (s.equals(checking) && ((Nounable)part).getNounForm() == NounForm.simple) return true;
                        if (nounIncompatible(s,checking, ((Nounable)part).getNounForm())) {
                            putCommand(new MistakeCommand(checking + " (" + part.name + ") noun forms incompatible", part.id, s));
                            return true;
                        }

                         */
                        }
                        if (mistakeCheck(s, checking)) {
                            putCommand(new MistakeCommand(checking + " - " + s + ": ошибка", part, s, null));
                            return true;
                        }
                    }
                    return false;
                }


                //////// ПРОВЕРКА ГЛАГОЛЬНЫХ ФОРМ //////////////////////////////////////////////////////////////////////////

                // количество глассных в слове
                int getVowelCount(String word) {
                    Pattern pattern = Pattern.compile("[aoyeiu]");
                    Matcher matcher = pattern.matcher(word);
                    int count = 0;
                    while (matcher.find())
                        count++;
                    return count;
                }

                // возвращает true, если y последняя в слове и перед ней есть согласная
                boolean getLastYCanErased(String word) {
                    if (word.charAt(word.length() - 1) != 'y') return false;
                    Pattern pattern = Pattern.compile("[aoyeiu]");
                    Matcher matcher = pattern.matcher(word.substring(word.length() - 2, word.length() - 1));
                    return !matcher.matches();
                }

                // возвращает true, если последняя буква в глаголе может быть вставлена 2 раза (исключения не здесь)
                boolean getLastWordCanMultiply(String word) {
                    Pattern pattern = Pattern.compile("[^aoyiue]");
                    Matcher matcher = pattern.matcher(word.substring(word.length() - 1));

                    if (!matcher.find()) return false;
                    matcher.reset(word.substring(word.length() - 2, word.length() - 1));
                    if (!matcher.find()) {
                        if (getVowelCount(word) == 1) return true;

                        char firstVowel = '!';
                        for (int i = word.length() - 2; i >= 0; i--) {
                            matcher.reset(word.substring(i, i + 1));
                            if (!matcher.find()) {
                                if (firstVowel == '!') firstVowel = word.charAt(i);
                                else return (firstVowel != word.charAt(i));
                            }
                        }
                        //String transcript = transportSql.getTranscription();
                        String transcript = "[fəˈɡet]";
                        if (transcript == null) {
                            System.out.println("there is no transcription - 'ing' form writing might be incorrect");
                            return false;
                        }
                        // работаем так - ждем 2 согласного - если там апостроф был ровно до него - удвоить

                        boolean isVowel = false;
                        Pattern consonant = Pattern.compile("[bdf3ɡhklmnpstvz∫rθðŋw\\[\\]]");
                        Matcher matcherConsonant = consonant.matcher(transcript.substring(transcript.length() - 2));
                        for (int i = transcript.length() - 2; i >= 0; i--) {
                            matcherConsonant.reset(transcript.substring(i, i + 1));
                            if (!matcherConsonant.matches()) {
                                isVowel = true;
                            }
                            if (matcherConsonant.matches() && isVowel) {
                                return (transcript.charAt(i - 1) == '\'' || transcript.charAt(i - 1) == 'ˈ');
                            }
                        }

                    }
                    return false;
                }


                // когда эта функция запускается, строка должна быть не исключением
                // пытается сформировать инфинитив (находит совпадение в базе данных, ибо без этого точно хрен что определишь)
                // и найти синонимы
                // возвращает null, если не удалось, или нет синонимов
                private ArrayList<String> tryCastToInfAndFindSynonyms(String name) {
                    VerbForm verbForm = getVerbForm(name);
                    String cast = "";
                    ArrayList<String> synonyms = null;
                    switch (verbForm) {
                        case past:
                            cast = name.substring(0, name.indexOf("ed"));
                            break;
                        case simple:
                            cast = name.substring(0, name.length() - 1);
                            break;
                        case ing:
                            cast = name.substring(0, name.indexOf("ing"));
                            break;
                    }
                    //System.out.println(cast);
                    //synonyms = transportSql.getCardSynonyms(cast);
                    if (synonyms == null) {
                        switch (verbForm) {
                            case past:
                                cast = name.substring(0, name.length() - 1);
                                break;
                            case simple:
                                if (name.contains("es"))
                                    cast = name.substring(0, name.indexOf("es"));
                                break;
                            case ing:
                                cast = name.substring(0, name.indexOf("ing")) + "e";
                                break;
                        }
                        //System.out.println(cast);
                        //synonyms = transportSql.getCardSynonyms(cast);
                        if (synonyms == null) {
                            switch (verbForm) {
                                case past:
                                    if (name.contains("ied"))
                                        cast = name.substring(0, name.indexOf("ied")) + "y";
                                    else if (name.contains("icked"))
                                        cast = name.substring(0, name.indexOf("ked"));
                                    else if (name.charAt(name.indexOf("ed") - 1) == name.charAt(name.indexOf("ed") - 2))
                                        cast = name.substring(0, name.indexOf("ed") - 1);
                                    else
                                        return null;
                                    break;
                                case simple:
                                    if (name.contains("ies"))
                                        cast = name.substring(0, name.indexOf("ies")) + "y";
                                    else
                                        return null;
                                    break;
                                case ing:
                                    if (name.contains("ying"))
                                        cast = name.substring(0, name.indexOf("ying")) + "ie";
                                    else if (name.contains("icking"))
                                        cast = name.substring(0, name.indexOf("king"));
                                    else if (name.charAt(name.indexOf("ing") - 1) == name.charAt(name.indexOf("ing") - 2))
                                        cast = name.substring(0, name.indexOf("ing") - 1);
                                    else return null;
                                    break;
                            }
                            //System.out.println(cast);
                            //synonyms = transportSql.getCardSynonyms(cast);
                        }
                    }
                    return synonyms;
                }


                private String nounMultipleFormBuilding(String noun){
                    if (noun.length() < 3) return noun;

                    char endingS = noun.charAt(noun.length()-1);
                    String endingSS = noun.substring(noun.length()-2);
                    String endingSSS = noun.substring(noun.length()-3);

                    switch (endingS) {
                        case 's':
                        case 'x':
                        case 'o':
                            return noun + "es";
                    }
                    switch (endingSS) {
                        case "zz":
                        case "sh":
                        case "ch":
                            return noun + "es";
                    }
                    if ("tch".equals(endingSSS)) {
                        return noun + "es";
                    }
                    if (getLastYCanErased(noun))
                        return noun.substring(0, noun.length() - 1) + "ies";
                    return noun + "s";
                }

                // строит форму глагола (оно не разбирается в исключениях)
                private String verbFormBuilding(String verb, VerbForm verbForm) {

                    final String endingS = verb.substring(verb.length() - 1);
                    final String endingSS = verb.substring(verb.length() - 2);
                    final String fundament = verb.substring(0, verb.length() - 2);
                    final String fundament1 = verb.substring(0, verb.length() - 1);
                    final String endingSSS;
                    if (verb.length() > 2)
                        endingSSS = verb.substring(verb.length() - 3);
                    else
                        endingSSS = null;
                    switch (verbForm) {
                        case infinite:
                            return verb;
                        case ing:
                            if (!endingSS.equals("e") && endingS.equals("e"))
                                return fundament1 + "ing";
                            if (endingSS.equals("ie"))
                                return fundament + "ying";
                            else if (endingSS.equals("ic"))
                                return fundament + "icking";
                                //else if (Ruler.getDialect() == Dialect.Br && verb.substring(verb.length()-1).equals("l"))
                            else if (endingS.equals("l")) {
                                if (!endingSS.equals("ll"))
                                    return verb + "ling";
                                else
                                    return verb + "ing";
                            } else if (endingS.equals("w") || endingS.equals("x"))
                                return verb + "ing";
                            else if (getLastWordCanMultiply(verb))
                                return verb + endingS + "ing";
                            else
                                return verb + "ing";
                        case simple:
                            switch (endingS) {
                                case "s":
                                case "x":
                                case "o":
                                    return verb + "es";
                            }
                            switch (endingSS) {
                                case "zz":
                                case "sh":
                                case "ch":
                                    return verb + "es";
                            }
                            if ("tch".equals(endingSSS)) {
                                return verb + "es";
                            }
                            if (getLastYCanErased(verb))
                                return verb.substring(0, verb.length() - 1) + "ies";
                            return verb + "s";
                        case past:
                            if ("e".equals(endingS))
                                return verb + "d";
                            if (getLastYCanErased(verb))
                                return verb.substring(0, verb.length() - 1) + "ied";
                            if (endingSS.equals("ic"))
                                return fundament + "icked";
                            else if (endingS.equals("l")) {
                                if (!endingSS.equals("ll"))
                                    return verb + "led";
                                else
                                    return verb + "ed";
                            } else if (endingS.equals("w") || endingS.equals("x"))
                                return verb + "ed";
                            else if (getLastWordCanMultiply(verb))
                                return verb + endingS + "ed";
                            else
                                return verb + "ed";
                    }
                    return null;
                }


                // находит или ошибку в использовании формы или ошибку (далеко не каждую) в образовании формы (только для глаголов)
            /*
            для неправильных глаголов - создать базу данных с исключениями и смотреть по ней
            важно - если это исключение, то нужно его как-то пометить, ибо бывают случаи, когда исключения не отделимы от адекватных слов
            к примеру - found (может быть формой find а может быть просто found инфинитивом)

             */

                private VerbForm getVerbForm(String name) {
                    VerbForm verbForm;
                    if (name.length() > 2 && name.substring(name.length() - 2).equals("ed"))
                        verbForm = VerbForm.past;
                    else if (name.length() > 3 && name.substring(name.length() - 3).equals("ing"))
                        verbForm = VerbForm.ing;
                    else if (name.length() > 2 && (name.substring(name.length() - 1).equals("s")
                            && name.charAt(name.length() - 2) != 's' && name.charAt(name.length() - 2) != 'o'
                            && name.charAt(name.length() - 2) != 'x'
                            && !name.substring(name.length() - 3, name.length() - 1).equals("ch")
                            && !name.substring(name.length() - 3, name.length() - 1).equals("zz")
                            && !name.substring(name.length() - 3, name.length() - 1).equals("sh")))
                        verbForm = VerbForm.simple;
                    else
                        verbForm = VerbForm.infinite;
                    return verbForm;
                }

                private NounForm getNounForm(String name) {
                    NounForm nounForm;
                    if (name.length() > 2 && (name.substring(name.length() - 2).equals("'s") || name.substring(name.length() - 2).equals("`s")))
                        nounForm = NounForm.attachment;
                    else
                        nounForm = NounForm.simple;
                    return nounForm;
                }

                private boolean verbFormsInaccordance(String name, String s, boolean isException) {
                    if (isException) {
                    /*
                    важно помнить, что оно должно найти остальные варианты по одному из исключений - соответственно оно не будет ключевым
                    и нужно будет просмотреть вообще все исключения, чтобы найти необходимый вариант
                     */
                        //ArrayList<String> varies = transportSql.getExceptionsVaries(name);
                        ArrayList<String> varies = new ArrayList<>();
                        varies.add("begin");
                        varies.add("begun");
                        varies.add("began");

                        for (String v : varies) {
                            if (s.equals(v)) return true;
                        }
                    } else {
                        VerbForm verbForm = getVerbForm(name);
                        switch (verbForm) {
                            case infinite:
                                if (s.startsWith(name)) return true;
                                if ((name.charAt(name.length() - 1) == 'y' || name.charAt(name.length() - 1) == 'e')
                                        && s.startsWith(name.substring(0, name.length() - 1)))
                                    return true;
                                break;
                            case ing:
                                if (s.startsWith(name.substring(0, name.length() - 4))) return true;
                                break;
                            case simple:
                                if (s.startsWith(name.substring(0, name.length() - 2))) return true;
                            case past:
                                if (s.startsWith(name.substring(0, name.length() - 3))) return true;

                        }

                    }
                    return false;
                }
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////

                private boolean nounIncompatible(String name, String s, boolean isException) {
                    if (isException) {
                    /*
                    важно помнить, что оно должно найти остальные варианты по одному из исключений - соответственно оно не будет ключевым
                    и нужно будет просмотреть вообще все исключения, чтобы найти необходимый вариант
                     */
                        //ArrayList<String> varies = transportSql.getExceptionsVaries(name);
                        ArrayList<String> varies = new ArrayList<>();
                        varies.add("child");
                        varies.add("children");
                        varies.add("child's");

                        for (String v : varies) {
                            if (s.equals(v)) return true;
                        }
                    }
                    NounForm nounForm = getNounForm(name);
                    switch (nounForm) {
                        case simple:
                            if (s.startsWith(name)) return true;
                            break;
                        case multiple:
                            if (name.charAt(name.length() - 2) == 'e')
                                return s.startsWith(name.substring(0, name.length() - 2));
                            else
                                return s.startsWith(name.substring(0, name.length() - 1));
                        case attachment:
                            return s.startsWith(name.substring(0, name.length() - 2));
                    }
                    return false;
                }

                boolean checkUnion(String checking, Part part, int now_id) {

                    if (isSynonym(checking, part)) {
                        part.id = now_id;
                        id.put(part.id, part);
                        return true;
                    }

                    if (mistakeCheck(part.name, checking)) {
                        putCommand(new MistakeCommand(part.name + " - " + checking + ": ошибка", part, part.name, null));
                        part.id = now_id;
                        id.put(part.id, part);
                        return true;
                    }
                    return false;
                }

                void checkNounable(Part now, ArrayList<String> string) {
                    if (now.id == -1) {
                        for (int i = 0; i < string.size(); i++) {
                            if (id.containsKey(i)) continue;
                            if (nounIncompatible(now.name, string.get(i), now.isException)) {

                                putCommand(new MistakeCommand(string.get(i) + " - " + now.name + ": несовпадение форм или ошибка при образовании формы", now, now.name, GM.NOUN_MANY));
                                now.id = i;
                                id.put(now.id, now);
                                break;
                            }
                            if (checkUnion(string.get(i), now, i))
                                break;
                        }
                        if (now.id == -1) {
                            putCommand(new MistakeCommand("'" + now.name + "' не найдено", now, now.name, null));
                            unfounded_id.add(now);
                        }
                    }
                }

                void checkVerbable(Part now, ArrayList<String> string) {

                    if (now.id == -1) {
                        for (int i = 0; i < string.size(); i++) {
                            if (id.containsKey(i)) continue;
                            if (verbFormsInaccordance(now.name, string.get(i), now.isException)) {
                                VerbForm vf = getVerbForm(now.name);
                                putCommand(new MistakeCommand(string.get(i) + " - " + now.name + ": несовпадение форм или ошибка при образовании формы", now, now.name,
                                        ((vf == VerbForm.infinite)? GM.VERB_INF:
                                                ((vf == VerbForm.ing)? GM.VERB_ING: GM.VERB_PAST))));
                                now.id = i;
                                id.put(now.id, now);
                                break;
                            }
                            if (checkUnion(string.get(i), now, i))
                                break;

                        }
                        if (now.id == -1) {
                            putCommand(new MistakeCommand("'" + now.name + "' не найдено", now, now.name, null));
                            unfounded_id.add(now);
                        }
                    }
                }

                // проверяет артикли
                void checkServicePart(Part article, ArrayList<String> string) {
                    if (article != null) {
                        if (article.id == -1 && article.watched == 0) {
                            switch (article.name) {
                                case "this":
                                    serviceJumbled(article, string, "the", false, null);
                                    break;
                                case "the":
                                    serviceJumbled(article, string, "this", false, null);
                                    serviceJumbled(article, string, "a", true, ARTICLE);
                                    serviceJumbled(article, string, "an", true, ARTICLE);
                                    break;
                                case "a":
                                    serviceJumbled(article, string, "an", true, ARTICLE);
                                    serviceJumbled(article, string, "the", true, ARTICLE);
                                    break;
                                case "an":
                                    serviceJumbled(article, string, "a", true, ARTICLE);
                                    serviceJumbled(article, string, "the", true, ARTICLE);
                                    break;
                                case "no":
                                    try {
                                        Verbable father = (Verbable) article.back.back;
                                        if ((((Part)father).name.equals("have") || ((Part)father).name.equals("had") || ((Part)father).name.equals("has"))) {
                                            boolean isDoThere = false;
                                            boolean isNotThere = false;

                                            String doString = ((((Part)father).name.equals("have")) ? "do" : ((((Part)father).name.equals("has")) ? "does" : "did"));

                                            for (int j = 0; j < string.size(); j++) {
                                                if (id.containsKey(j)) continue;
                                                if (string.get(j).equals(doString)) {
                                                    isDoThere = true;
                                                    ((Part)father).id = j;
                                                    id.put(((Part)father).id, ((Part)father));
                                                    if (string.contains(((Part)father).name))
                                                        id.put(string.indexOf(((Part)father).name), ((Part)father));
                                                    ((Part)father).name = doString + " not " + ((Part)father).name;
                                                    continue;
                                                }
                                                if (string.get(j).equals("not")) {
                                                    isNotThere = true;
                                                    article.id = j;
                                                    id.put(article.id, ((Part)father));
                                                    article.name = "";
                                                    continue;
                                                }
                                                if (string.get(j).equals(doString + "n't")) {
                                                    ((Part)father).id = j;
                                                    id.put(((Part)father).id, ((Part)father));
                                                    if (string.contains(((Part)father).name))
                                                        id.put(string.indexOf(((Part)father).name), ((Part)father));
                                                    article.id = j;
                                                    id.put(((Part)father).id, ((Part)father));
                                                    article.name = "";
                                                    ((Part)father).name = doString + "n't " + ((Part)father).name;
                                                    isNotThere = true;
                                                    isDoThere = true;
                                                }
                                            }


                                            if (article.back.linked == null) {

                                                String rightName = nounMultipleFormBuilding(article.back.name);
                                                if (id.containsValue(article.back)){

                                                    int errorId = -1;

                                                    for (int k = 0; k < commands.size(); k++){
                                                        if (commands.get(k).main == article.back ){
                                                            if (commands.get(k).main != null)
                                                                errorId = commands.get(k).main.id;
                                                            commands.remove(k);
                                                            break;
                                                        }
                                                    }

                                                    if (!string.contains(rightName)) {
                                                        if (errorId != -1 && mistakeCheck(rightName, string.get(errorId)))
                                                            putCommand(
                                                                    new MistakeCommand("'" + string.get(errorId) + "' - " + rightName + ": ошибка", article.back, rightName, null));
                                                        else
                                                            putCommand(
                                                                new MistakeCommand("'" + article.back.name + "' должно быть в множественном числе", article.back, rightName, null));
                                                    }
                                                } else {
                                                    if (!string.contains(rightName))
                                                        article.back.name = rightName;
                                                        checkNounable(article.back, string);
                                                    article.back.id = string.indexOf(rightName);
                                                    id.put(article.back.id, article.back);
                                                    article.back.watched = 1;
                                                }
                                                article.back.name = rightName;

                                            }


                                            if (isDoThere && isNotThere) break;


                                        }
                                    } catch (ClassCastException e){

                                    }
                            }
                            cycle:
                            for (int i = 0; i < string.size(); i++) {
                                if (id.containsKey(i)) continue;
                                if (article.name.length() > 2) {
                                    if (mistakeCheck(string.get(i), article.name)) {

                                        putCommand(new MistakeCommand(string.get(i) + " - " + article.name + ": ошибка", article, article.name, GM.ARTICLES));
                                        article.id = i;
                                        id.put(article.id, article);
                                        break;
                                    }
                                }

                            }
                        }
                        if (article.id == -1 && article.watched == 0) {
                            putCommand(new MistakeCommand("'" + article.name + "' не найдено", article, article.name, GM.ARTICLES));
                            unfounded_id.add(article);
                        }
                        article.watched = 1;
                    }
                }

                // проверяет модальные глаголы и
                void checkModal(ModalVerb article, ArrayList<String> string) {
                    if (string.indexOf(article.name) != -1) {
                        article.id = string.indexOf(article.name);
                        id.put(article.id, article);
                    }
                    else{
                        switch (article.name) {
                            case "am":
                            case "is":
                            case "are":
                                // рассматриваем ситуацию с I'm и прочим
                                if (getClosestSubject(article).id == -1) {
                                    int index = string.indexOf(getClosestSubject(article).name + "'" + article.name.substring(1));
                                    putModalAndSubjectTogether(article, index);
                                }
                                // проверяем, тот же subj'm но если он уже не верный
                                if (getClosestSubject(article).id == -1) {
                                    if (!checkAnotherModalAndSubjectTogether(article, "re", string, TO_BE))
                                        if (!checkAnotherModalAndSubjectTogether(article, "m", string, TO_BE))
                                            checkAnotherModalAndSubjectTogether(article, "s", string, TO_BE);
                                }

                                serviceJumbled(article, string, "is", true, TO_BE);
                                serviceJumbled(article, string, "are", true, TO_BE);
                                serviceJumbled(article, string, "am", true, TO_BE);


                                break;
                            case "will": // придумай тут по поводу won't что нибудь - КОРОЧЕ ОНО ОТДЕЛЬНО
                                if (getClosestSubject(article).id == -1) {
                                    int index = string.indexOf(getClosestSubject(article).name + "'" + "ll");
                                    putModalAndSubjectTogether(article, index);
                                }
                                if (string.indexOf("won't") != -1){
                                    article.id = string.indexOf("won't");
                                    id.put(article.id, article);
                                }
                                if (string.indexOf("won`t") != -1){
                                    article.id = string.indexOf("won`t");
                                    id.put(article.id, article);
                                }

                                break;
                            case "has":
                                serviceJumbled(article, string, "have", true, HAS_HAVE);
                                serviceJumbled(article, string, "had", true, HAS_HAVE);
                                break;
                            case "does":
                                serviceJumbled(article, string, "do", true, DO_DOES);
                                break;
                            case "do":
                                serviceJumbled(article, string, "does", true, DO_DOES);
                                break;
                            case "can":
                                serviceJumbled(article, string, "could", true, CAN);
                                if (article.id == -1){
                                    if (article.none.equals("not")){
                                        if (string.indexOf("can't") != -1){
                                            article.id = string.indexOf("can't");
                                            id.put(article.id, article);
                                            return;
                                        } else if (string.indexOf("cannot") != -1){
                                            article.id = string.indexOf("cannot");
                                            id.put(article.id, article);
                                            return;
                                        }else if (string.indexOf("cann't") != -1){
                                            article.id = string.indexOf("cann't");
                                            id.put(article.id, article);
                                            putCommand(new MistakeCommand("cann't - can't: ошибка", article, article.name, CAN));
                                            return;
                                        }
                                    }
                                }

                                break;
                            case "could":
                                serviceJumbled(article, string, "can", true, CAN);
                                break;
                            case "may":
                                serviceJumbled(article, string, "might", true, MAY);
                                break;
                            case "might":
                                serviceJumbled(article, string, "may", true, MAY);
                                break;
                            case "was":
                                serviceJumbled(article, string, "were", true, WAS);
                                break;
                            case "were":
                                serviceJumbled(article, string, "was", true, WAS);
                                break;
                            case "would":    // тут тоже реши вопрос с subj'd
                                if (getClosestSubject(article).id == -1) {
                                    putModalAndSubjectTogether(article, string.indexOf(getClosestSubject(article).name + "'" + "d"));
                                }
                                break;
                            case "have":
                                serviceJumbled(article, string, "has", true, HAS_HAVE);
                                serviceJumbled(article, string, "had", true, HAS_HAVE);
                                if (!article.none.equals("to"))
                                    break;
                            case "must":
                            case "should":
                            case "ought":
                                if (string.indexOf("must") != -1){
                                    putCommand(new MistakeCommand( "must должен быть заменен на " + article.name, article, null, MUST));
                                    article.id = string.indexOf("must");
                                    if (article.none.equals("to")) article.none = null;
                                    id.put(article.id, article);
                                }
                                else if (string.indexOf("should") != -1){
                                    putCommand(new MistakeCommand( "should должен быть заменен на " + article.name, article, null, MUST));
                                    article.id = string.indexOf("should");
                                    if (article.none.equals("to")) article.none = null;
                                    id.put(article.id, article);
                                }
                                else if (string.indexOf("have") != -1 && string.indexOf("to") != -1 && string.indexOf("have") + 1 == string.indexOf("to")){
                                    putCommand(new MistakeCommand( "have to должен быть заменен на " + article.name, article, null, MUST));
                                    article.id = string.indexOf("have");
                                    article.none = "to";
                                    id.put(article.id, article);
                                    id.put(string.indexOf("to"), article);
                                    return;
                                }
                                else if (string.indexOf("ought") != -1){
                                    putCommand(new MistakeCommand( "ought to должен быть заменен на " + article.name, article, null, MUST));
                                    article.id = string.indexOf("ought");
                                    article.none = "to";
                                    id.put(article.id, article);
                                }
                                else if (article.none != null && article.none.equals("not") && string.indexOf("mustn't") != -1){
                                    putCommand(new MistakeCommand( "mustn't должен быть заменен на " + article.name + " " + article.none, article, null, MUST));
                                    article.id = string.indexOf("mustn't");
                                    id.put(article.id, article);
                                }
                                else if (article.none != null && article.none.equals("not") && string.indexOf("shouldn't") != -1){
                                    putCommand(new MistakeCommand( "shouldn't должен быть заменен на " + article.name + " " + article.none, article, null, MUST));
                                    article.id = string.indexOf("shouldn't");
                                    id.put(article.id, article);
                                }

                                break;
                        }
                    }

                            if (article.none != null) {
                                // рассматриваем amn't

                                if (!article.none.equals("to")) {
                                    int index = string.indexOf(article.name + "n't");
                                    if (index != -1) {
                                        article.id = index;
                                        id.put(article.id, article);
                                        return;
                                    }
                                }

                                // рассматриваем am not
                                if (article.id != -1) {
                                    if (string.indexOf(article.none) != -1 && !id.containsKey(string.indexOf(article.none))) {
                                        if (article.id + 1 != string.indexOf(article.none))
                                            putCommand(new MistakeCommand(article.none + " должен стоять после " + article.name, article, null, null));
                                        id.put(string.indexOf(article.none), article);
                                        return;
                                    }
                                    putCommand(new MistakeCommand(article.none + " не найден", article, article.none, null));
                                }
                            }
                        if (article.id == -1 && article.watched == 0) {
                            putCommand(new MistakeCommand("'" + article.name + "' не найдено", article, article.name, null));
                            unfounded_id.add(article);
                        }
                        article.watched = 1;

                }

                private void serviceJumbled(Part part, ArrayList<String> str, String first, boolean isAProblem, String gm){
                    if ((str.indexOf(first) != -1 && !id.containsKey(str.indexOf(first)))) {
                        part.id = str.indexOf(first);
                        id.put(part.id, part);
                        if (isAProblem)
                            putCommand(new MistakeCommand(first + " должен быть заменен на " + part.name, part, part.name, gm));
                    }
                    if ((str.indexOf(first + "n't") != -1 && !id.containsKey(str.indexOf(first + "n't")))) {
                        part.id = str.indexOf(first + "n't");
                        id.put(part.id, part);
                        if (isAProblem)
                            putCommand(new MistakeCommand(first + " должен быть заменен на " + part.name, part, part.name, gm));
                    }
                }

                private boolean putModalAndSubjectTogether(ModalVerb article, int index){
                    if (index != -1) {
                        getClosestSubject(article).id = index;
                        article.id = index;
                        //id.put(article.id, article);
                        id.put(getClosestSubject(article).id, getClosestSubject(article));
                        return true;
                    }
                    return false;
                }

                private boolean checkAnotherModalAndSubjectTogether(ModalVerb article, String appendix, ArrayList<String> string, String gm){
                    if (putModalAndSubjectTogether(article, string.indexOf(getClosestSubject(article).name + "'" + appendix))) {
                        putCommand(new MistakeCommand(
                                getClosestSubject(article).name + "'" + appendix +
                                        " должен быть заменен на " + getClosestSubject(article).name +
                                        "'" + article.name.substring(1), article, article.name, gm));
                        return true;
                    }
                    return false;
                }



            private void checkPreposition(Preposition preposition, ArrayList<String> string){
                if (preposition != null) {
                    if (preposition.id == -1 && preposition.watched == 0) {
                        for (int i = 0; i < string.size(); i++) {
                            if (id.containsKey(i)) continue;
                            if (preposition.name.length() > 4) {
                                if (mistakeCheck(string.get(i), preposition.name)) {
                                    putCommand(new MistakeCommand(string.get(i) + " - " + preposition.name + ": ошибка", preposition, preposition.name, null));
                                    preposition.id = i;
                                    id.put(preposition.id, preposition);
                                    break;
                                }
                            }
                        }
                    }
                    if (preposition.id == -1 && preposition.watched == 0) {
                        preposition.watched = 1;
                        unfounded_id.add(preposition);

                        for (int i = 0; i < string.size(); i++) {
                            if (id.containsKey(i)) continue;
                            if (prepositionJumbled(preposition, string.get(i), "in", "into")){
                                // ok
                            } else if (prepositionJumbled(preposition, string.get(i), "with", "by") ||
                                    prepositionJumbled(preposition, string.get(i), "beside", "besides")||
                                    prepositionJumbled(preposition, string.get(i), "of", "off"))
                                putCommand(new MistakeCommand(string.get(i) + " должен быть заменен на '" + preposition.name + "'", preposition, preposition.name, PREPOSITION));
                            else{
                                putCommand(new MistakeCommand("'" +preposition.name + "' не найдено", preposition, preposition.name, null));
                                unfounded_id.add(preposition);
                                return;
                            }
                            preposition.id = i;
                            id.put(preposition.id, preposition);
                            break;
                        }


                    }
                }
            }

            boolean prepositionJumbled(Part preposition, String check, String firstPreposition, String lastPreposition){
                return (preposition.name.equals(firstPreposition) && check.equals(lastPreposition)) ||
                        (preposition.name.equals(lastPreposition) && check.equals(firstPreposition));
            }

            boolean mistakeCheck(String name, String cheking){

                // я хз как это правильно проверять, но буду проверять на замену менее 20% букв, пропуск 1 буквы или вставку одной лишней

                if (cheking.length() <= 2)
                    return false;
                if (name.length() < 2)
                    return false;

                if (name.length() == cheking.length()) {
                    ArrayList<Integer> compared = new ArrayList<>(1);
                    for (int i = 0; i < name.length(); i++) {
                        if (cheking.charAt(i) != name.charAt(i))
                            compared.add(i);
                    }
                    return compared.size() <= 2;
                } else if (name.length() < cheking.length()) {
                    ArrayList<Integer> compared = new ArrayList<>(1);
                    int minus = 0;
                    for (int i = 0; i < cheking.length(); i++) {
                        if (i < name.length()) {
                            if (cheking.charAt(i) != name.charAt(i - minus)) {
                                compared.add(i);
                                minus++;
                            }
                        }
                    }
                    return (compared.size() <= 2 && compared.size() < name.length()/3.3);
                } else{
                    ArrayList<Integer> compared = new ArrayList<>(1);
                    int minus = 0;
                    for (int i = 0; i < name.length(); i++) {
                        if (i < cheking.length()) {
                            if (cheking.charAt(i - minus) != name.charAt(i)) {
                                compared.add(i);
                                minus++;
                            }
                        }
                    }
                    return (compared.size() <= 2 && compared.size() < cheking.length()/3.3);
                }
            }

        },
        order {
            Part now;
            @Override
            void check(ArrayList<String> string, Part sentence) {
                now = sentence;


                while (now != null) {
                    switch (now.getClass().getSimpleName()) {
                        case "Subject":
                            Subject subject = (Subject) now;
                            if (checkForAdjective(subject) && checkForActive(subject.getPredicate())) {
                                if (checkForNotNear(subject, subject.getPredicate(), false, true))
                                    putCommand(new OrderCommand(subject.getPredicate().getName() + " должен стоять после " + subject.getName(), subject.getPredicate(), subject, ORDER));
                                now = subject.getPredicate();
                            } else {
                                if (checkForActive(subject.getArticle())) {
                                    if (checkForNotNear(subject.getArticle(), subject, false, true))
                                        putCommand(new OrderCommand(subject.getName() + " должен стоять после " + subject.getArticle().getName(), subject.getArticle(), subject, ARTICLE));
                                }
                                now = now.back;
                            }
                            break;
                        case "PredicateNoun":
                            PredicateNoun predicateNoun = (PredicateNoun) now;

                            if (checkForAdjective(predicateNoun)) {

                                if (checkForActive(predicateNoun.getArticle())) {
                                    if (checkForNotNear(predicateNoun.getArticle(), predicateNoun, false, true)) {
                                        putCommand(new OrderCommand(predicateNoun.getName() + " должен стоять после " + predicateNoun.getArticle().getName(), predicateNoun.getArticle(), predicateNoun, ARTICLE));
                                    }
                                    if (checkForActive(predicateNoun.getModalVerb())) {
                                        if (!predicateNoun.isQuestion() && checkForNotNear(predicateNoun.getModalVerb(), predicateNoun.getArticle(), false, false)) {
                                            putCommand(new OrderCommand(predicateNoun.getModalVerb().getName() + " должен стоять после " + predicateNoun.getArticle().getName(), predicateNoun.getModalVerb(), predicateNoun, ORDER));
                                        } else if (predicateNoun.isQuestion() && checkForNotNear(predicateNoun.getModalVerb(), getClosestSubject(predicateNoun), false, true))
                                            putCommand(new OrderCommand(getClosestSubject(predicateNoun).getName() + " должен стоять после " + predicateNoun.getModalVerb().getName(), predicateNoun.getModalVerb(), predicateNoun, TO_BE));
                                    }
                                } else if (checkForActive(predicateNoun.getModalVerb())) {
                                    if (!predicateNoun.isQuestion() && checkForNotNear(predicateNoun.getModalVerb(), predicateNoun, false, true)) {
                                        putCommand(new OrderCommand(predicateNoun.getModalVerb().getName() + " должен стоять перед " + predicateNoun.getName(), predicateNoun.getModalVerb(), predicateNoun, TO_BE));
                                    } else if (predicateNoun.isQuestion() && checkForNotNear(predicateNoun.getModalVerb(), getClosestSubject(predicateNoun), false, true))
                                        putCommand(new OrderCommand(predicateNoun.getModalVerb().getName()  + " должен стоять перед " + getClosestSubject(predicateNoun).getName(), predicateNoun.getModalVerb(), predicateNoun, TO_BE));
                                }
                                now = now.back;
                            }
                            break;
                        case "PredicateVerb":
                            PredicateVerb predicate = (PredicateVerb) now;

                            if (checkForPresence(predicate.getSetting()) && predicate.getSetting().getSettingType() == SettingType.time) {
                                if ((checkForNotNear(predicate, predicate.getSetting(), true, true)) &&
                                        (checkForNotNear(predicate.getSetting(), getClosestSubject(predicate), true, true))) {
                                    putCommand(new OrderCommand(predicate.getSetting().getName() + " должен стоять после " + getClosestSubject(predicate).getName(), predicate.getSetting(), predicate, SETTING_ORDER));
                                }
                                now = predicate.getSetting();
                            }
                                else if (checkForPresence(predicate.getSetting()) && predicate.getSetting().getSettingType() == SettingType.reason) {
                                    if (checkForNotNear(predicate.getSetting(), getClosestSubject(predicate), true, true)) {
                                        putCommand(new OrderCommand(predicate.getSetting().getName() + " должен стоять перед " + getClosestSubject(predicate).getName(), predicate.getSetting(), predicate, SETTING_ORDER));
                                    }
                                    now = predicate.getSetting();
                            } else if (checkForPresence(predicate.getSetting()) && predicate.getSetting().getSettingType() == SettingType.frequency) {
                                if (checkForActive(predicate.getModalVerb())) {
                                    if (!predicate.isQuestion() && checkForNotNear(predicate.getModalVerb(), predicate.getSetting(), false, true))
                                        putCommand(new OrderCommand(predicate.getSetting().getName() + " должен стоять после " + predicate.getModalVerb().getName(), predicate.getSetting(), predicate, SETTING_ORDER));
                                    else if (predicate.isQuestion() && checkForNotNear(predicate.getModalVerb(), getClosestSubject(predicate), false, true))
                                        putCommand(new OrderCommand(getClosestSubject(predicate).getName() + " должен стоять после " + predicate.getModalVerb().getName(), predicate.getModalVerb(), predicate, TO_BE));

                                    if (predicate.isQuestion() && checkForNotNear( getClosestSubject(predicate), predicate.getSetting(), true, true))
                                        putCommand(new OrderCommand(predicate.getSetting().getName() + " должен стоять после " + getClosestSubject(predicate).getName(), predicate.getModalVerb(), predicate, TO_BE));

                                } else if (checkForNotNear(predicate.getSetting(), predicate, true, true))
                                    putCommand(new OrderCommand(predicate.getSetting().getName() + " должен стоять после " + predicate.getName(), predicate.getSetting(), predicate, SETTING_ORDER));
                                now = predicate.getSetting();
                            } else if (checkForPresence(predicate.getSetting())) {
                                if (checkForPresence(predicate.getAddiction())) {
                                    if (checkForNotNear(predicate.getAddiction(), predicate.getSetting(), true, true))
                                        putCommand(new OrderCommand(predicate.getSetting().getName() + " должен стоять после " + predicate.getAddiction().getName(), predicate.getSetting(), predicate.getAddiction(), SETTING_ORDER));
                                } else {
                                    if (checkForNotNear(predicate, predicate.getSetting(), false, true))
                                        putCommand(new OrderCommand(predicate.getSetting().getName() + " должен стоять после " + predicate.getName(), predicate.getSetting(), predicate, SETTING_ORDER));
                                }
                                now = predicate.getSetting();
                            } else if (checkForPresence(predicate.getAddiction())) {
                                if (checkForNotNear(predicate, predicate.getAddiction(), false, true)) {
                                    putCommand(new OrderCommand(predicate.getAddiction().getName() + " должен стоять после " + predicate.getName(), predicate.getAddiction(), predicate, ORDER));
                                }
                                now = predicate.getAddiction();
                            } else {
                                if (checkForActive(predicate.getModalVerb())) {
                                    if (!predicate.isQuestion() && checkForNotNear(predicate.getModalVerb(), predicate, false, true)) {
                                        putCommand(new OrderCommand(predicate.getModalVerb().getName() + " должен стоять перед " + predicate.getName(), predicate.getModalVerb(), predicate, TO_BE));
                                    } else if (predicate.isQuestion() && checkForNotNear(predicate.getModalVerb(), getClosestSubject(predicate), false, true))
                                        putCommand(new OrderCommand(getClosestSubject(predicate).getName() + " должен стоять после " + predicate.getModalVerb().getName(), predicate.getModalVerb(), predicate, TO_BE));

                                }
                                now = now.back;
                            }
                            break;
                        case "SettingNoun":
                            if (!checkForAdjective((Nounable) now)) break;
                        case "Setting":
                            if (checkForActive(((Setting) now).getPreposition())) {
                                if (checkForActive(((Setting) now).getArticle())) {
                                    if (checkForNotNear(((Setting) now).getPreposition(), ((Setting) now).getArticle(), false, true)) {
                                        putCommand(new OrderCommand(((Setting) now).getArticle().getName() + " должен стоять после " + ((Setting) now).getPreposition().getName(), ((Setting) now).getArticle(), now, ARTICLE));
                                    }
                                } else {
                                    if (checkForNotNear(((Setting) now).getPreposition(), now, false, true)) {
                                        putCommand(new OrderCommand(now.name + " должен стоять после " + ((Setting) now).getPreposition().getName(), ((Setting) now).getPreposition(), now, ORDER));
                                    }
                                }
                            }
                            now = now.back;
                            break;
                        case "AddictionNoun":

                            if (checkForAdjective((Nounable) now)) {
                                if (checkForActive(((ArticleHolder) now).getArticle())) {
                                    if (checkForNotNear(((ArticleHolder) now).getArticle(), now, false, true)) {
                                        putCommand(new OrderCommand(now + " должен стоять после " + ((ArticleHolder) now).getArticle().getName(), ((ArticleHolder) now).getArticle(), now, ARTICLE));
                                    }
                                }
                                now = now.back;
                            }
                            break;
                        case "SettingVerb":
                            SettingVerb settingVerb = (SettingVerb) now;
                            if (checkForPresence(settingVerb.getSubject())) {
                                if (checkForActive(settingVerb.getUnion())) {
                                    if (checkForNotNear(settingVerb.getUnion(), settingVerb.getSubject(), true, true))
                                        putCommand(new OrderCommand(settingVerb.getSubject().getName() + "должен стоять после " + settingVerb.getUnion().getName(), settingVerb.getUnion(), settingVerb.getSubject(), ORDER));

                                    if (checkForNotNear(settingVerb, settingVerb.getUnion(), true, true))
                                        putCommand(new OrderCommand(settingVerb.getUnion().getName() + " должен стоять после " + settingVerb.getName(), settingVerb.getUnion(), settingVerb, SETTING_ORDER));
                                } else {
                                    if (checkForNotNear(settingVerb, settingVerb.getSubject(), true, true))
                                        putCommand(new OrderCommand(settingVerb.getSubject().getName() + " должен стоять после " + settingVerb.getName(), settingVerb, settingVerb.getSubject(), SETTING_ORDER));
                                }
                                now = settingVerb.getSubject();
                                break;
                            }
                        case "AddictionVerb":
                            Verbable verbable = (Verbable) now;
                            ArticleHolder Articleholder = (ArticleHolder) now;


                            if (checkForPresence(verbable.getSetting())) {
                                if (checkForPresence(verbable.getAddiction())) {
                                    if (checkForNotNear(verbable.getAddiction(), verbable.getSetting(), true, true))
                                        putCommand(new OrderCommand(verbable.getSetting().getName() + " должен стоять после " + verbable.getAddiction().getName(), verbable.getSetting(), verbable.getAddiction(), SETTING_ORDER));
                                } else {
                                    if (checkForNotNear(now, verbable.getSetting(), false, true))
                                        putCommand(new OrderCommand(verbable.getSetting().getName() + " должен стоять после " + now.getName(), verbable.getSetting(), now, SETTING_ORDER));
                                }
                                now = verbable.getSetting();
                            } else if (checkForPresence(verbable.getAddiction())) {
                                if (checkForNotNear(now, verbable.getAddiction(), false, true)) {
                                    putCommand(new OrderCommand(verbable.getAddiction().getName() + " должен стоять после " + now.getName(), verbable.getAddiction(), now, ORDER));
                                }
                                now = verbable.getAddiction();
                            } else {
                                if (checkForActive(Articleholder.getArticle())) {
                                    if (checkForNotNear(Articleholder.getArticle(), now, false, true)) {
                                        putCommand(new OrderCommand(now.getName() + " должен стоять после " + Articleholder.getArticle().getName(), Articleholder.getArticle(), now, ARTICLE));
                                    }
                                }
                                now = now.back;
                            }
                            break;
                    }
                    if (now != null) {

                        // проверка порядка фраз
                        if (now.linked != null){
                            int pos = now.linked.id;
                            Part nextLinked = now.linked.linked;
                            while (nextLinked != null){
                                if (nextLinked.id != ++pos && nextLinked.id != -1){
                                    putCommand(new OrderCommand(nextLinked.getName() + " должен стоять после " + nextLinked.back.getName(), nextLinked.back, nextLinked, null));
                                }
                                nextLinked = nextLinked.linked;
                            }
                        }

                        now.watched = 2;
                    }
                }
            }

            boolean checkForPresence(Part part){
                return part != null && part.watched < 2;
            }
            boolean checkForActive(Part part){
                return checkForPresence(part) && part.id != -1;
            }

            // проверяет - если между двумя айдишниками пустые айдишники (айдишники, которые не учитываются из-за того,
            // что вообще хрен знает откуда взялись)
            // возвращает true - если они идут один за другим и false - если между ними что-то есть или они идут не один за другим
            // важно - первое число должно быть больше второго, иначе false
            boolean checkForNotNear(Part first, Part last, boolean is_first_depend, boolean is_last_depend) {

                if (first.id == -2) first.id = first.linked.id;
                //if (first.id == -2) first.id = getLastLinkedId(first.linked);
                else if (last.id == -2) last.id = last.linked.id;

                //System.out.println("difference " + first.id + " " + last.id);

                if (first.id >= last.id) {
                    return true;
                }
                for (int i = first.id+1; i < last.id; i++) {
                    if (!empty_id.contains(i)) {
                            if (is_first_depend && checkForAttachment(id.get(i), first)) continue;
                            if (is_last_depend && checkForAttachment(id.get(i), last)) continue;
                            if (first.linked != null && checkForAttachment(id.get(i), first)) continue;
                            if (first.linked == null && first.name.contains(" ")&& checkForAttachment(id.get(i), first)) continue;


                        return true;
                    }
                }


                return false;
            }

            int getLastLinkedId(Part part){
                return ((part.linked == null)?part.id:getLastLinkedId(part.linked));
            }

            // возвращает true если adjective уже проверили
            boolean checkForAdjective(Nounable nounable){
                if (checkForActive(nounable.getAdjective())) {
                    if (checkForNotNear(nounable.getAdjective(), (Part)nounable,  false, false))
                        putCommand(new OrderCommand(nounable.getAdjective().getName() + " должен стоять перед " +  ((Part)nounable).getClass(), (Part)nounable, nounable.getAdjective(), ORDER));
                    now = nounable.getAdjective();
                    ArticleHolder holder = (ArticleHolder) now;
                    if (checkForActive(holder.getArticle())) {
                        if (checkForNotNear(holder.getArticle(), now, false, true)) {
                            putCommand(new OrderCommand(now.getName() + " должен стоять после " + holder.getArticle().getName(), holder.getArticle(), now, ARTICLE));
                        }
                    }
                    now.watched = 2;
                    now = now.back;
                    return false;
                }
                return true;
            }

        };

        // проверяет, является ли элемент ребенком другого
        // тут суть в чем - допустим у нас есть дополнение и определение, причем определение должно стоять после дополнения
        // но и у дополнения и у определения могут быть еще зависимости. Причем они могут стоять между ними.
        // и нужно определить, что стоящие между определением и дополнением слова - должны там стоять, потому что зависят от них
        // переменные depend показывают, есть ли у таргетов какие-то зависимости, которые нужно учитывать
        boolean checkForAttachment(Part now, Part target){
            if (now == null)
                return false;
            /*
            if (now.getClass().getSimpleName().equals("Subject")){
                return now.equals(getClosestSubject(target));
            }

             */
            return now.equals(target) || checkForAttachment(now.back, target);
        }

        Part getClosestSubject(Part now){
            if (now.getClass().getSimpleName().equals("Subject")) return now;
            return getClosestSubject(now.back);
        }

        abstract void check(ArrayList<String> string, Part sentence);

    }

}
