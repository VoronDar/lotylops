package com.lya_cacoi.lotylops.Reader;

import android.util.Log;

import com.lya_cacoi.lotylops.Databases.transportSQL.SqlMain;
import com.lya_cacoi.lotylops.Databases.transportSQL.SqlVocaPhrasUnion;
import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.cards.Card;
import com.lya_cacoi.lotylops.cards.GrammarCard;
import com.lya_cacoi.lotylops.cards.VocabularyCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Stack;

import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.VOCABULARY_INDEX;
import static com.lya_cacoi.lotylops.activities.NewCardFragment.generateId;

// получает строку, которую переводит в карты и вставляет в базы данных
public class ColodeInterpier {

    private static final String importChange = "c";
    private static final String importAsk = "a";
    private static final String importUpdate = "u";
    private static final String importLeave = "l";


    /*
        правила импорта: если импортируется


     */


    public static void main(String[] args) {
        System.out.println("hello world");

        String importType = importAsk;



        Scanner in = new Scanner(System.in);
        while (true) {
            StringBuilder builder = new StringBuilder();
            ArrayList<VocabularyCard> cards = new ArrayList<>();
            String Id = null;
            Stack<Character> stack = new Stack<>();
            StringBuilder block = new StringBuilder();
            StringBuilder indicate = new StringBuilder();
            VocabularyCard card = new VocabularyCard();
            {
                String newStr = "";
                while (!newStr.endsWith(">")) {
                    newStr = in.nextLine();
                    builder.append(newStr);
                }
            }

            try {
                interpier(builder, stack, block, indicate, card, importType, cards, Id);
                System.out.println(cards);
                System.out.println("success");

            } catch (Exception e) {
                System.out.println("O ouh --> \n" + builder + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
            }
            System.out.println("----------------------");
        }





    }




    public static void interpier(StringBuilder allText, Stack<Character> stack,
                          StringBuilder block, StringBuilder indicate, VocabularyCard card, String importType,
                          ArrayList<VocabularyCard> cards, String Id){

        final char sectionDivider = '+';


        final char vocabular = 'W';

        final String id = "id";
        final String word = "w";
        final String translate = "t";
        final String meaning = "m";
        final String meanNative = "mn";
        final String transcript = "tr";
        final String exampleNative = "en";
        final String example = "e";
        final String synonym = "s";
        final String antonym = "a";
        final String group = "g";
        final String part = "pa";
        final String mem = "as";
        final String train = "p";
        final String trainNative = "pn";
        final String help = "h";
        final String levelRemember = "l";
        final String levelPractice = "lp";
        final String dayTrain = "d";
        final String dialogue = "c";
        final String dialogueRight = "c1";
        final String dialogueSecond = "c2";
        final String dialogueThird = "c3";
        final String dialogueFourth = "c4";



        final char divider = '|';
        


        final char grammar = 'G';

        final char exception = 'E';

        final String question = "q";
        final String answer = "a";

        int count = 1;
        for (Character n : allText.toString().toCharArray()) {
            if (stack.size() > 0 && stack.peek().equals('~')) {
                if (n.equals('~'))
                    stack.pop();
                continue;
            }
            switch(n){
                case sectionDivider:
                    if (stack.size() == 0)
                        stack.add(n);
                    else{
                        stack.pop();
                    }
                    break;
                case vocabular:
                    break;
                case '{':
                    if (Id == null && cards.size() == 0)
                        Id = "U";
                    card = new VocabularyCard();
                //case '{':
                case '<':
                    stack.add(n);
                    break;
                case divider:
                    if (stack.size() == 0 || !stack.peek().equals(divider))
                        stack.add(n);
                    else{
                        String bl = block.toString();
                        //Log.i("main", "done");
                        switch (indicate.toString()) {
                            case "":
                                Id = block.toString();
                                break;
                            case word:
                                card.setWord(bl);
                                break;
                            case translate:
                                card.setTranslate(bl);
                                break;
                            case transcript:
                                card.setTranscription(bl);
                                break;
                            case meaning:
                                card.setMeaning(bl);
                                break;
                            case meanNative:
                                card.setMeaningNative(bl);
                                break;
                            case example:
                                if (card.getExamples() == null || card.getExamples().size() == 0){
                                    card.setExamples(new ArrayList<Card.Sentence>());
                                    card.getExamples().add(new Card.Sentence());
                                    card.getExamples().get(0).setId(card.getId());
                                    card.getExamples().get(0).setLinked_id(card.getId());
                                }
                                card.getExamples().get(0).setSentence(bl);
                                break;
                            case exampleNative:
                                if (card.getExamples() == null || card.getExamples().size() == 0){
                                    card.setExamples(new ArrayList<Card.Sentence>());
                                    card.getExamples().add(new Card.Sentence());
                                    card.getExamples().get(0).setId(card.getId());
                                    card.getExamples().get(0).setLinked_id(card.getId());
                                }
                                card.getExamples().get(0).setTranslate(bl);
                                break;
                            case train:
                                if (card.getTrains() == null || card.getTrains().size() == 0){
                                    card.setTrains(new ArrayList<Card.Sentence>());
                                    card.getTrains().add(new Card.Sentence());
                                    card.getTrains().get(0).setId(card.getId());
                                    card.getTrains().get(0).setLinked_id(card.getId());
                                }
                                card.getTrains().get(0).setSentence(bl);
                                break;
                            case trainNative:
                                if (card.getTrains() == null || card.getTrains().size() == 0){
                                    card.setTrains(new ArrayList<Card.Sentence>());
                                    card.getTrains().add(new Card.Sentence());
                                    card.getTrains().get(0).setId(card.getId());
                                    card.getTrains().get(0).setLinked_id(card.getId());
                                }
                                card.getTrains().get(0).setTranslate(bl);
                                break;
                            case group:
                                card.setGroup(bl);
                                break;
                            case dialogue:
                                if (card.getDialogueTest() == null)
                                    card.setDialogueTest(new GrammarCard.SelectTrain(card.getId(),
                                            bl, null, null, null, null));
                                else
                                    card.getDialogueTest().setAnswer(bl);
                                break;
                            case dialogueRight:
                                if (card.getDialogueTest() == null)
                                    card.setDialogueTest(new GrammarCard.SelectTrain(card.getId(),
                                            null, bl, null, null, null));
                                else
                                    card.getDialogueTest().setRight(bl);
                                break;
                            case dialogueSecond:
                                if (card.getDialogueTest() == null)
                                    card.setDialogueTest(new GrammarCard.SelectTrain(card.getId(),
                                            null, null, bl, null, null));
                                else
                                    card.getDialogueTest().setSecond(bl);
                                break;
                            case dialogueThird:
                                if (card.getDialogueTest() == null)
                                    card.setDialogueTest(new GrammarCard.SelectTrain(card.getId(),
                                            null, null, null, bl, null));
                                else
                                    card.getDialogueTest().setThird(bl);
                                break;
                            case dialogueFourth:
                                if (card.getDialogueTest() == null)
                                    card.setDialogueTest(new GrammarCard.SelectTrain(card.getId(),
                                            null, null, null, null, bl));
                                else
                                    card.getDialogueTest().setFourth(bl);
                                break;
                            case part:
                                card.setPart(bl);
                                break;
                            case help:
                                card.setHelp(bl);
                                break;
                            case mem:
                                card.setMem(bl);
                                break;
                            case antonym:
                                card.setAntonym(bl);
                                break;
                            case synonym:
                                card.setSynonym(bl);
                                break;
                            case levelRemember:
                                card.setRepeatlevel(Integer.parseInt(bl));
                                break;
                            case levelPractice:
                                card.setPracticeLevel(Integer.parseInt(bl));
                                break;
                            case dayTrain:
                                card.setRepetitionDat(Integer.parseInt(bl));
                                break;
                            case id:
                                card.setId(Id + bl);
                                break;
                            default:
                                throw new NullPointerException("Indefined index - " + indicate.toString());
                        }
                        block = new StringBuilder();
                        indicate = new StringBuilder();
                        stack.pop();
                    }
                    break;
                case '}':
                    if (stack.peek().equals('{')) {
                        //Log.i("main", "endWord");
                        stack.pop();
                        card.setCourse(Id);
                        if (card.getId() == null) {
                            //card.setId(Id + availableId++);
                            card.setId(Id + generateId());
                            if (card.getExamples() != null && card.getExamples().size() > 0){
                                card.getExamples().get(0).setLinked_id(card.getId());
                                card.getExamples().get(0).setId(card.getId());
                            }
                            if (card.getTrains() != null && card.getTrains().size() > 0){
                                card.getTrains().get(0).setLinked_id(card.getId());
                                card.getTrains().get(0).setId(card.getId());
                            }
                            if (card.getDialogueTest() != null){
                                card.getDialogueTest().setId(card.getId());
                            }
                        }
                        cards.add(card);
                        //Log.i("mainRead", Integer.toString(count++));
                        //Log.i("mainRead", card.getId());
                        card = new VocabularyCard();
                        card.isMine = true;
                        //Log.i("main-card", "newCard");
                    } else {
                        throw new NullPointerException("[");
                    }
                    break;
                case '>':
                    if (stack.peek().equals('<')) {
                        //Log.i("main", "endCourse");
                        stack.pop();
                    } else
                        throw new NullPointerException("couldn't find simbol '<'");
                    break;
                case '~':
                    if (stack.size() == 0)
                        stack.add('~');
                    else
                        throw new NullPointerException("couldn't find simbol '~");
                    break;
                    /*
                case '}':
                    if (stack.peek().equals('{')) {
                        String bl = block.toString();
                        if (bl.equals(importAsk) || bl.equals(importChange) ||
                                bl.equals(importLeave))
                            importType = block.toString();
                        block = new StringBuilder("");
                        stack.pop();
                    } else
                        throw new NullPointerException("{");
                    break;

                     */
                case ' ':
                case '\n':
                case '\t':
                    if (!stack.peek().equals(divider))
                        break;
                default:
                    if (stack.peek().equals(divider) //|| stack.peek().equals('{'))
                    )
                        block.append(n);
                    else if (!stack.peek().equals('~'))
                        indicate.append(n);
            }
        }

        //Log.i("main", "end");
    }



    public static boolean readImportFile(StringBuilder allText, FileReadable activity){
        ArrayList<VocabularyCard> cards = new ArrayList<>();
        String Id = null;

        SqlMain sql = SqlMain.getInstance(activity.getContext(), VOCABULARY_INDEX);


        String importType = importAsk;
        Log.i("main", "startAnalyzeFile");

        Stack<Character> stack = new Stack<>();
        StringBuilder block = new StringBuilder();
        StringBuilder indicate = new StringBuilder();
        VocabularyCard card = new VocabularyCard();


        try {
            interpier(allText, stack, block, indicate, card, importType, cards, Id);

        } catch (Exception e){
            Log.i("main", "" + Arrays.toString(e.getStackTrace()) + "\n\n\n" + e.getMessage() + "\n\n\n" + card.toString() + "\n\n" + cards);
            activity.showError(activity.getContext().getString(R.string.read_file_error));
            return false;
        }

        push(cards, importType, sql);
        return true;
    }


    private static void push(ArrayList<VocabularyCard> cards, String importType, SqlMain transportSql){

        // я тут поменял на новое, вооооть, теперь оно просто обновляет вне зависимости от чего-либо

        Log.i("main", "push");


        for (VocabularyCard card: cards) {
            card.isMine = true;
            transportSql.updateCard(card);
            ((SqlVocaPhrasUnion)transportSql).updateChanges(card);
            if (card.getTrains() != null && card.getTrains().size() > 0)
                transportSql.updateTrains(card.getTrains());
            if (card.getExamples() != null && card.getExamples().size() > 0)
                ((SqlVocaPhrasUnion) transportSql).updateExamples(card.getExamples());
            if (card.getDialogueTest() != null)
                ((SqlVocaPhrasUnion) transportSql).updateDialogue(card.getDialogueTest());
        }

        Log.i("main", "completed");
        /*
        ArrayList<String> allId = transportSql.getAllCardsId();


        // А ВОТ ТУТ РАЗБЕРИСЬ И ПРОКОМЕНТИРУЙ НОРМАЛЬНО - я ничего не понимаю

        for (int i = 0; i < cards.size(); i++){

            for (String n : allId) {
                if (n.equals(cards.get(i).getId())) {

                    if (importType.equals(importLeave)){
                        cards.remove(i--);
                        break;
                    }
                    else if (importType.equals(importChange)){
                        VocabularyCard card = (VocabularyCard) transportSql.getString(n);
                        if (card.equals(cards.get(i))) {
                            cards.remove(i--);
                            Log.i("main", "leave");
                            break;
                        }
                        else
                            Log.i("main", "change");
                        if (!card.getWord().equals(cards.get(i).getWord())){
                            ArrayList<Card> allSim = transportSql.getAllCardsByWord(cards.get(i).getWord());
                            if (allSim.size() > 0){
                                //Log.i("main", "there is several words - what's to do");
                                // do dmt;
                            }
                        }
                        transportSql.deleteStringCommon(n);
                        transportSql.addString(cards.get(i));
                        //Log.i("main", "change card - " + n);
                        cards.remove(i--);
                        break;
                    }
                    else if (importType.equals(importUpdate)){
                        VocabularyCard card = (VocabularyCard) transportSql.getString(n);
                        if (card.equals(cards.get(i))) {
                            cards.remove(i--);
                            break;
                        }
                        if (!card.getWord().equals(cards.get(i).getWord())) {
                            ArrayList<Card> allSim = transportSql.getAllCardsByWord(cards.get(i).getWord());
                            if (allSim.size() > 0) {
                                //Log.i("main", "there is several words - what's to do");
                                // do dmt;
                            }
                        }
                        transportSql.deleteStringCommon(n);
                        transportSql.addString(cards.get(i));
                        cards.remove(i--);
                        //Log.i("main", "change card - " + n);
                        break;
                    }
                    else{
                        //Log.i("main", "act - ");
                    }
                }
            }
        }

        // сделать адекватный спрос

        for (int i = 0; i < cards.size(); i++){
        //    ArrayList<Card> allSim = transportSql.getAllCardsByWord(cards.get(i).getWord());
        //    if (allSim.size() > 0)
        //    {
                //Log.i("main", "new");
        //    }
        //    else
                transportSql.addString(cards.get(i));
        }



        transportSql.closeDatabases();
        Log.i("mainEnd", "END");
        // LATER


         */
                /*
                for (int i = 0; i < cards.size(); i++){
                    for (String n : allWords) {
                        if (n.equals(cards.get(i).getWord())) {
                            Log.i("main", cards.get(i).getWord());
                        }
                    }
                }

                 */

    }

}
