package com.lya_cacoi.lotylops.ruler;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.lya_cacoi.lotylops.ApproachManager.ApproachManager;
import com.lya_cacoi.lotylops.Databases.FirebaseAccess.FirebaseDatabase;
import com.lya_cacoi.lotylops.Databases.transportSQL.SqlCulture;
import com.lya_cacoi.lotylops.Databases.transportSQL.SqlException;
import com.lya_cacoi.lotylops.Databases.transportSQL.SqlGrammar;
import com.lya_cacoi.lotylops.Databases.transportSQL.SqlMain;
import com.lya_cacoi.lotylops.Databases.transportSQL.SqlPhonetics;
import com.lya_cacoi.lotylops.Databases.transportSQL.SqlPhraseology;
import com.lya_cacoi.lotylops.Databases.transportSQL.SqlVocabulary;
import com.lya_cacoi.lotylops.Helper.Helper;
import com.lya_cacoi.lotylops.Shop.Shop;
import com.lya_cacoi.lotylops.Statistics.StatisticManager;
import com.lya_cacoi.lotylops.activities.DayPlanFragment;
import com.lya_cacoi.lotylops.cards.Card;
import com.lya_cacoi.lotylops.declaration.consts;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.FREQUENCY_LINEAR;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.PHONETIC_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.VOCABULARY_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.getManager;

public class Ruler {
    private static int PractiseChapter;                                                             // selected practice chapter
    private static int PractiseTestTypes = 0;                                                       // selected practice chapter
    private static boolean isPracticeNow = false;                                                   // true - if practice
    public static consts.dayState dayState;                                                         // dayState - study, repeat, practice


    private consts.cardState cardStudyState;                                                        // card's state (card\problem\stepTrain)
    private ApproachManager.Manager approachManager;

    private final Context context;
    private static int index = VOCABULARY_INDEX;
    private static Card lastCard;
    private static boolean isProblemTrain;

    private SqlMain sqlMain;

    private static Ruler ruler;


    private ArrayList<Card> practiceCards;                                                          // карты для практики


    //----------------------------------------------------------------------------------------------





    /////// LIFECYCLE //////////////////////////////////////////////////////////////////////////////
    public static Ruler getInstance(Context context){
        if (ruler == null)
            ruler = new Ruler(context, index);
        return ruler;
    }public static Ruler getInstance(Context context, int index){
        if (ruler == null)
            ruler = new Ruler(context, index);
        else if (getSQLIndex() != index){
            ruler.closeDatabase();
            ruler = new Ruler(context, index);
        }
        return ruler;
    }
    public static Ruler getNewRuler(Context context){
        ruler = new Ruler(context, VOCABULARY_INDEX);
        return ruler;
    }

    private Ruler(Context context, int index){                                                       // generate Ruler with tranportSQL index
        Ruler.index = index;
        this.context = context;

    }
    public void closeDatabase() {                                                                   // необходимо закрывать базы данных каждый раз при завершении работы
        Log.i("main", "closeDB");

        if (sqlMain != null)
            sqlMain.closeDatabases();
    }
    public static int getSQLIndex(){                                                                       // получить индекс раздела он же индекс таблицы
        return index;
    }
    public static Card getLastCard(){
        return lastCard;
    }
    public static boolean IsProblemTrain(){
        return isProblemTrain;
    }

    public void startToStudy(){
        Ruler.setIsPracticeNow(false);
        if (practiceCards != null)
            practiceCards.clear();
        sqlMain = SqlMain.getInstance(context, index);
        sqlMain.openDbForLearn();
        approachManager = getManager(context, index);
    }
    public void startToTrain(){
        sqlMain = SqlVocabulary.getInstance(context);
        ((SqlVocabulary) sqlMain).openDbForTrain();
        approachManager = getManager(context, index);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////




    //----------------------------------------------------------------------------------------------




    /////// STATIC GETTERS|SETTERS /////////////////////////////////////////////////////////////////
    public static boolean isIsPracticeNow() {
        return isPracticeNow;
    }
    public static void setIsPracticeNow(boolean isPracticeNow) {
        Ruler.isPracticeNow = isPracticeNow;
    }
    public static int getPractiseChapter() {
        return PractiseChapter;
    }
    public static void setPractiseChapter(int practiseChapter) {
        PractiseChapter = practiseChapter;
    }
    public static int getPractiseTestTypes() {
        return PractiseTestTypes;
    }
    public static void setPractiseTestTypes(int practiseTestTypes) {
        PractiseTestTypes = practiseTestTypes;
    }
    public static void setProblemTrain(Boolean isProblemTrain){
        Ruler.isProblemTrain = isProblemTrain;
        if (Ruler.isProblemTrain)
            Ruler.getInstance(null).setCardStudyState(consts.cardState.problemTrain);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////




    /////// UTILS //////////////////////////////////////////////////////////////////////////////////
    public static boolean isDailyPlanCompleted(Context context){                                            // true - если учебный план завершен
        for (int i = 0; i <= PHONETIC_INDEX; i++){
            if (transportPreferences.getTodayLeftStudyCardQuantity(context, i) > 0)
                return false;
            if (transportPreferences.getTodayLeftRepeatCardQuantity(context, i) > 0)
                return false;
        }
        return true;
    }
    public static String getCardEnd(int number) {                                                    // говнокод для определения окончания слов после числительных
        if (number % 10 == 0 || number % 10 >= 5 || ((number + 100) % 100 >= 10
                && (number + 100) % 100 <= 20))
            return "карточек";
        else if (number % 10 == 1)
            return "карточка";
        else
            return "карточки";
    }
    public static int getDay(Context context) {                                                     // get learning day for selecting today cards
        return getAbsoluteDay() + transportPreferences.getExtraDayCount(context);
    }
    public static int getAbsoluteDay() {                                                            // get today calendar day
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        return calendar.get(Calendar.DAY_OF_YEAR) + (calendar.get(Calendar.YEAR) * 365);
    }
    public static consts.cardState getCardState(boolean data) {                                     // активности теста принимают булевую переменную, которая означает тип теста - эта функция

        if (data)                                                                                   // расшифровывает ее в понятный вид
            return consts.cardState.problemTrain;
        else
            return consts.cardState.stepTrain;
    }

    public consts.cardState getCardStudyState() {
        return cardStudyState;
    }
    private void setCardStudyState(consts.cardState cardStudyState){
        this.cardStudyState = cardStudyState;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////




    //----------------------------------------------------------------------------------------------



    /////// GET CARD ///////////////////////////////////////////////////////////////////////////////

    private Card getTodayCard(Context context) {                                                     // получить первую в очереди карту, которую надо повторить сегодня - берет из текущей базы данных
        Card card;
        if (Ruler.dayState == consts.dayState.study)
            card = sqlMain.getNextStudyCard();
        else
            card = sqlMain.getNextRepeatCard();
        return card;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////


    /////// PUT CARDS BACK /////////////////////////////////////////////////////////////////////////
    public void reloadErrorStringWhileStudy(Card card) {                                            // кладет карту с ошибкой назаж
        card.setRepeatlevel(consts.NOUN_LEVEL);
        card.setPracticeLevel(consts.NOUN_LEVEL);
        CardRepeatManage.upDay(card, context, approachManager);
        sqlMain.updateProgress(card);

        StatisticManager.uppMistakes(context, getSQLIndex());

        //if (Ruler.dayState == consts.dayState.repeat)
        //    transportPreferences.setForgettedCount(context, getSQLIndex(), transportPreferences.getForgettedCount(context, getSQLIndex())+1);

    }
    public void reloadRightStringWhileStudy(Card card) {                                            // закрывает карту и кладет ее назаж
        CardRepeatManage.riseFromFall(card);
        CardRepeatManage.upDay(card, context, approachManager);
        sqlMain.updateProgress(card);

        if (Ruler.dayState == consts.dayState.study) {
            sqlMain.reduceLeftStudyCount();
            StatisticManager.uppStudied(context, getSQLIndex());
        } else {
            sqlMain.reduceLeftRepeatCount();
            StatisticManager.uppRepeated(context, getSQLIndex());
        }
    }
    public void putErrorPracticeCardBack(Context context, consts.cardState state, Card card) {      // закидывает в таблицу карточку, практику которой пользователь запорол
        approachManager.setErrorProgress(card, state);
        card.setRepeatlevel(consts.NOUN_LEVEL);
        CardRepeatManage.upDay(card, context, approachManager);
        sqlMain.updateProgress(card);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////



    /////// GET NEW INTENT /////////////////////////////////////////////////////////////////////////
    /*
        все функции, что называются с changeActivity берут из базы данных первую карту в очереди и
        если таковая вообще существует возвращает соотсветствующую ее ситуации активности, если же
        карт нет, то возвращает к главному меню

        closeTest работает уже с существующей картой после закрытия теста и определяет, отсылать еще
        на один тест или показывать карту
    */
    public Fragment changeActivityWhileStudy(Context lastContext) {                                  // для пользования во время изучения и повторения
        Fragment fr;

        if (approachManager.isTrainAfterStudy(cardStudyState, lastCard))
            return approachManager.getTrainFragment(lastCard.getRepeatlevel(), lastCard);

        Card card;
        card = getTodayCard(lastContext);
        if (card == null){
                sqlMain.closeDatabases();
                Ruler.dayState = null;
                return new DayPlanFragment();
        }

            cardStudyState = approachManager.setCardStudyState(card);

            if (cardStudyState == consts.cardState.study)
                fr = approachManager.getTheoryFragment();
            else if (cardStudyState == consts.cardState.problemTrain)
                fr = approachManager.getTrainFragment(card.getPracticeLevel(), card);
            else
                fr = approachManager.getTrainFragment(card.getRepeatlevel(), card);

            if (fr == null){
                return changeActivityWhileStudy(lastContext);
            }


            lastCard = card;

            if (cardStudyState != consts.cardState.study) {
                isProblemTrain = (cardStudyState == consts.cardState.problemTrain);
            }

            return fr;

    }
    public Fragment closeTest(Context context, boolean isRight, consts.cardState getState, Card card)
        {                                                                                           // переходит к новой активности после теста
            if (Ruler.dayState == consts.dayState.repeat && !isRight)
                transportPreferences.setForgettedCount(context, getSQLIndex(), transportPreferences.getForgettedCount(context, getSQLIndex())+1);

            lastCard = card;

            Fragment fr =  approachManager.closeTest(card, getState, isRight, sqlMain);
            if (fr == null) {
                if (getState != consts.cardState.study)
                    reloadRightStringWhileStudy(card);
                return changeActivityWhileStudy(context);
            }
            else return fr;

    }
    public Fragment changeActivityWhilePracticeForTheNextTests(Fragment lastFragment) {             // переходит к новой активности во время практики в режиме "подготовка"
        //return getFragment(sqlMain.getStringFromPractiseByLevel(getPractiseTestTypes()));
        for (Card card : practiceCards){
            if (card.getPracticeLevel() == getPractiseTestTypes())
                return getFragment(card);
        }
        return getFragment(null);



    }
    public Fragment changeActivityWhilePracticeForTheExam(Fragment lastFragment) {                      // переход к новой активности во время практики режима "экзамен"
        return null;
    }
    public Fragment changeActivityWhilePracticeForTheAllCards(Fragment lastFragment) {              // переход к новой активности во время практики режима "все карты"
        if (practiceCards.size() == 0)
            return getFragment(null);
        else return getFragment(practiceCards.get(0));
    }

    private Fragment getFragment(Card card) {                                                       // get only intent for practice
        if (card == null) {
            Ruler.setIsPracticeNow(false);
            Ruler.dayState = null;
            return new DayPlanFragment();
        } else {
            lastCard = card;
            Fragment fragment = approachManager.getTrainFragment(getPractiseTestTypes() + consts.START_TEST-1, card);
            if (fragment == null){
                //reloadRightStringWhileStudy(card);
                return changeActivityWhileStudy(context);
            } else return fragment;
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////


    /////// CLOSE PRACTICE CARDS ///////////////////////////////////////////////////////////////////
    /*
    тем или иным образом закрывают карты практики
     */

    private void deletePractiseCardById(String id){
        for (int i = 0; i < practiceCards.size(); i++){
            if (practiceCards.get(i).getId().equals(id)){
                practiceCards.remove(practiceCards.get(i));
                break;
            }
        }
    }

    public Fragment skipPracticeNextTest(Fragment fragment, String id) {                                // выкидывает карту из таблицы практики
        deletePractiseCardById(id);
        return changeActivityWhilePracticeForTheNextTests(fragment);
    }
    public Fragment skipPracticeAllCards(Fragment fragment, String id) {                                // выкидывает карту из таблицы практики
        deletePractiseCardById(id);
        return changeActivityWhilePracticeForTheAllCards(fragment);
    }
    public void putCardWhilePracticeForTheNextTest(Card card, boolean isRight) {                    // кладет ошибочную карту обратно в таблицу
        practiceCards.remove(card);
        if (!isRight) {
            practiceCards.add( card);
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////


    //----------------------------------------------------------------------------------------------




    /////// RELOAD TABLES //////////////////////////////////////////////////////////////////////////
    public static void regenerateDatabase(Context context) {
        /*
    TransportSQLInterface transportSQL = MainTransportSQL.getTransport(VOCABULARY_INDEX, context);
    //transportSQL.deleteCommon();
    //    transportSQL.deleteRepeat();
        //    transportSQL.deleteStudy();

         */


        /*
        * На данный момент здесь обновление карт из кода - надо будет потом перенести на файлы или перевести на облачную базу
        *  данных. Оно работает, но если работать дальше, то стоит все же поменять этот момент
        *
        * */

        /*
        transportSQL.addString(new VocabularyCard("anatomy", "1", NOUN_LEVEL, NOUN_LEVEL, 0, "creature", "anything that lives but is not a plant",
                "живая особь", "существо, создание","|ˈkriːtʃər|", "being", null, null, null, null, "Dolphins are intelligent creatures",
                "Дельфины - умные существа",
                "[s:i]!pv:saw=0]an:creature*the=1]stt:night*last=1]", "Я увидел это существо прошлой ночью", null));

        transportSQL.addString(new VocabularyCard("2", NOUN_LEVEL, NOUN_LEVEL, 0, "magnify",
                "to make something look larger than it is, especially by looking at it through a lens", "делать что-либо больше, чем оно есть на самом деле, смотря на него сквозь линзу", "увеличивать",
                "|mæɡnɪfaɪ|", null, null, null, null,
                "verb", "I was able to magnify the footage",
                "Мне удалось увеличить изображение",
                "I'm going to magnify the image|I am going to magnify the image|I am going to magnify this image|I'm going to magnify this image",
                "Я собираюсь увеличить это изображение", null));
         */

        /*
        {
            transportSQL.addString(new VocabularyCard("C1", null, "creature",
                    "anything that lives but is not a plant", "живая особь", "существо, создание",
                    "|ˈkriːtʃər|", null, null, null, null,
                    null, null, null, "noun", "Dolphins are intelligent creatures",
                    "Дельфины - умные существа", null,
                    null,
                    "I saw the creature last night|I saw this creature last night", "Я увидел это существо прошлой ночью", null));

            transportSQL.addString(new VocabularyCard("C2", null, "magnify",
                    "to make something look larger than it is, especially by looking at it through a lens", "делать что-либо больше, чем оно есть на самом деле, смотря на него сквозь линзу", "увеличивать",
                    "|mæɡnɪfaɪ|", null, null, null, null,
                    null, null, null, "verb", "I was able to magnify the footage",
                    "Мне удалось увеличить изображение", null,
                    null,
                    "I'm going to magnify the image|I am going to magnify the image|I am going to magnify this image|I'm going to magnify this image", "Я собираюсь увеличить это изображение", null));

            transportSQL.addString(new VocabularyCard("C3", null, "feature",
                    "a typical quality or an important part of something", "типичное качество или важная часть чего-либо", "особенность",
                    "|fiː.tʃər|", null, null, null, null,
                    null, null, null, null, "Our latest model of phone has several new features",
                    "Наша последняя модель телефона имеет несколько новых функций", null,
                    null,
                    "People say my hair is my best feature|People say that my hair is my best feature", "Люди говорят, что мои волосы - моя лучшая особенность", null));

            transportSQL.addString(new VocabularyCard("C4", null, "doubt",
                    "a feeling of not being certain about something, especially about how good or true it is", "чувство неуверенности в чем-либо, особенно насколько что-либо правдиво", "сомнение, сомневаться",
                    "|daʊt|", "doubt.png", null, null, null,
                    "сомневаться в (чем-либо) - to have doubt about (smt)", null, null, "noun, verb", "I never doubt his ability to do the job",
                    "Я никогда не сомневаюсь в его способности выполнять работу", null,
                    null,
                    "I have no doubts about his abilities|I have no doubts about his possibilities|I haven't doubts about his abilities| I haven't doubts about his possibilities|I have no doubt about his abilities|I have no doubt about his possibilities|I haven't doubt about his abilities| I haven't doubt about his possibilities|I have no doubts in his abilities|I have no doubts in his possibilities|I haven't doubts in his abilities| I haven't doubts in his possibilities|I have no doubt in his abilities|I have no doubt in his possibilities|I haven't doubt in his abilities| I haven't doubt in his possibilities", "Я не сомневаюсь в его способностях", null));
            transportSQL.addString(new VocabularyCard("C5", null, "kettle",
                    "a container for boiling water, that has a lid, handle, and spout and is made from plastic or metal", "сосуд с ручкой и носиком для кипячения воды или заварки чая", "чайник",
                    "|ˈket.əl|", null, null, null, null,
                    null, null, null, null, "to start to boil water in a kettle",
                    "Начать кипятить воду в чайнике", null,
                    null,
                    "We have to boil the kettle|We have to boil this kettle|We should to boil the kettle|We should to boil this kettle", "Мы должны вскипятить этот чайник", null));

            transportSQL.addString(new VocabularyCard("C6", null, "chin",
                    "the part of a person's face below their mouth", "округло выступающая на лице передняя часть нижней челюсти", "подбородок",
                    "|tʃɪn|", null, null, null, null,
                    null, null, null, null, "Her chin resting in her hands",
                    "Ее подбородок покоится в ее руках", null,
                    null,
                    "A leader should have a strong chin|A leader must have a strong shin", "Лидер должен иметь сильный подбородок", null));
            transportSQL.addString(new VocabularyCard("C7", null, "anymore",
                    "used when something does not happen in future or now", "используется в случае, когда действие не будет выполняться в будущем или прямо сейчас", "(никогда) больше",
                    "|/ˌen.iˈmɔːr|", null, null, null, null,
                    "всегда ставится в конец грамматической основы", null, null, "adverb", "I don't do yoga anymore",
                    "Я больше не занимаюсь йогой", null,
                    null,
                    "I can't do that anymore|I can not do this anymore|I can't do this anymore|I can not do that anymore", "Я больше не могу (продолжать делать что-то)", null));
            transportSQL.addString(new VocabularyCard("C8", null, "wrongly",
                    "not correctly", "неправильный способ делать что-либо", "ошибочно",
                    "|ˈrɒŋ.li|", null, null, null, null,
                    "является наречием - поэтому используется только в связи с глаголами, для существительных используйте wrong", null, null, "adverb", "Several people were wrongly convicted",
                    "Несколько человек были ошибочно осуждены", null,
                    null,
                    "He wrongly did this exercise|He wrongly did the exercise", "Он неправильно сделал это упражнение", null));
            transportSQL.addString(new VocabularyCard("C9", null, "assume",
                    "to accept something to be true without question or proof", "принимать что либо без доказательств", "предполагать, принимать на веру",
                    "|əˈsjuːm|", null, null, "believe", null,
                    "понимайте разницу между assume и presume, несмотря на то, что оба эти слова переводятся, как \"предполагать\", " +
                            "их нельзя назвать синонимами. Assume следует использует в случае отсутствия веских доказательств, " +
                            "в то время как presume подразумевает их наличие. Assume также является синонимом слова believe, но в отличие от последнего, assume использутеся, " +
                            "когда человек не до конца уверен в своей правоте", null, null, "verb", "I assumed that you had gone out",
                    " Я предполагал, что ты вышел", null,
                    null,
                    "We assume that such changes are possible|We assume that these changes are possible|We assume such changes are possible|We assume these changes are possible", "Мы предполагаем, что такие изменения возможны", null));
            transportSQL.addString(new VocabularyCard("C10", null, "wonder",
                    "to want to know something or to try to understand the reason for something", "хотеть узнать или понять что-либо", "интересоваться, желать знать",
                    "|ˈwʌndər|", null, null, null, null,
                    "слово буквально означает 'спрашивать себя', поэтому нельзя писать \"I wonder myself\"", null, null, "verb", "I wonder what he's making for dinner",
                    "Я задаюсь вопросом, что он делает на ужин", null,
                    null,
                    "I wonder why she left so suddenly|I wonder why she left so quickly|I wonder why she left so fast", "Мне интересно, почему она ушла так быстро", null));
            transportSQL.addString(new VocabularyCard("C11", null, "entire",
                    "whole or complete", "весь, целый, полностью", "весь, целый",
                    "|ɪnˈtaɪər|", null, null, "all, whole", null,
                    "всегда ставится впереди существительного, а не наоборот: ВСЮ ее жизнь - her ENTIRE life\n" +
                            "Важно понимать разницу между all, whole и entire. Entire более похож на whole, " +
                            "эти слова употребляются с исчисляемыми существительными, в отличие от all, который имеет также значение \"все\". " +
                            "Также entire используется для того, чтобы показать масштаб", null, null, "adjective", "She spent her entire life caring for other people",
                    "Она потратила всю свою жизнь, заботясь о других людях", null,
                    null,
                    "We spent an entire year playing computer games", "Мы потратили целый год, играя в компьютерные игры", null));
            transportSQL.addString(new VocabularyCard("C12", null, "connect",
                    "to join two things or places together", "связывать 2 вещи вместе", "(под)соединять, связывать",
                    "|kəˈnekt|", "connect.png", null, "join", "disconnect",
                    "Connect WITH something", null, null, "verb", "Cannot connect to database, please check your Internet connection",
                    "Не удается подключиться к базе данных, пожалуйста, проверьте подключение к Интернету", null,
                    null,
                    "I see pictures and connect them", "Я вижу картинки и соединяю их", null));
            transportSQL.addString(new VocabularyCard("C13", null, "device",
                    "a piece of equipment that is used for a particular purpose", "оборудование, которое используется для определенной цели", "устройство",
                    "|dɪˈvaɪs|", null, null, null, null,
                    null, null, null, null, "A pager is a small, electronic device for sending messages",
                    "Пейджер - это небольшое электронное устройство для отправки сообщений", null,
                    null,
                    "The device should be on every planet|The device must be on every planet|This device should be on every planet|This device must be on every planet", "Такое устройство должно быть на каждой планете", null));
            transportSQL.addString(new VocabularyCard("C14", null, "energy",
                    "the power and ability to be very active without becoming tired", "способность быть активным, не уставая", "энергия",
                    "|ˈenədʒi|", "energy.png", null, null, null,
                    null, null, null, null, "Looking after children takes up a lot of time and energy",
                    "Присмотр за детьми отнимает много времени и сил", null,
                    null,
                    "I'm filled with the strengths and energy", "Я полон сил и енергии", null));
            transportSQL.addString(new VocabularyCard("C15", null, "relationship",
                    "the way two people or groups feel and behave towards each other", "то, как несколько человек ведут себя по отношению друг к другу", "отношения",
                    "|rɪˈleɪʃənʃɪp|", null, null, "relation", null,
                    "практически всегда используется в одинственном числе", null, null, null, "Our relationship is time-tested",
                    "Наши отношения прошли испытание временем", null,
                    null,
                    "I have a good relationship with my parents", "У меня хорошие отношения с моими родителями", null));
            transportSQL.addString(new VocabularyCard("C16", null, "leftover",
                    "food that remains after a meal", "еда оставшаяся после потребления", "объедки",
                    "|ˈleftˌəʊvər|", null, null, null, null,
                    "если это существительное, то используется только во множественном числе, если это прилагательное, то ставится перед существительным без каких либо предлогов", null, null, "noun, adjective", "I have some leftover chicken or we could order pizza",
                    "У меня есть остатки курицы или же мы можем заказать пиццу", null,
                    null,
                    "I have some leftover chicken|I have got some leftover chicken", "У меня есть остатки курицы", null));
            transportSQL.addString(new VocabularyCard("C17", null, "cockroach",
                    "a large, brown or black insect that can live in houses and places where food is prepared", "Прямокрылое всеядное насекомое, вредитель в хозяйстве", "таракан",
                    "|ˈkɒkrəʊtʃ|", "cockroach.png", null, null, null, null, null, null, null,
                    "Cockroaches are insects",
                    "Тараканы - это насекомые", null,
                    null,
                    "I think that it's a cockroach|I think that it is a cockroach|I think it's a cockroach|I think it is a cockroach|I think this is a cockroach|I think that this is a cockroach", "Я думаю, что это таракан", null));
            transportSQL.addString(new VocabularyCard("C18", null, "able",
                    "to have the ability to do something or the possibility of doing something", "иметь возможность сделать что либо", "мочь, иметь возможность",
                    "|ˈeɪbl|", null, null, null, null,
                    "используется в конструкции be able to (do something)", null, null, "adjective", "Currently, Lithuania is not able to undertake such extensive commitments",
                    "В настоящее время Литва не в состоянии взять на себя такие широкие обязательства", null,
                    null,
                    "He will be able to help you|He'll be able to help you", "Он будет способен помочь тебе", null));
            transportSQL.addString(new VocabularyCard("C19", null, "instant",
                    "happening immediately", "происходящий немедленно", "мгновенный, немедленный",
                    "|ˈɪnstənt|", null, null, null, null,
                    "instant food - быстрозаваримая|растворимая еда", null, null, "adjective", "And that's why we drink Campfire Gold instant coffee",
                    "Поэтому мы и пьём растворимый кофе Кэмпфайр Голд", null,
                    null,
                    "I demand everyone stop this instant|I demand that everyone stop this instant|I demand everyone stop it instant|I demand that everyone stop it instant", "Я требую, чтобы все остановили это немедленно", null));
            transportSQL.addString(new VocabularyCard("C20", null, "oatmeal",
                    "a type of flour made from oats", "крупа, сделанная из овса", "овсянка",
                    "|ˈəʊt.miːl|", null, null, null, null,
                    "это более американский вариант, в британии распространено porridge", null, null, null,
                    "Amy, this oatmeal's a little underdone this morning",
                    "Эми, овсяная каша сегодня слегка недоварена", null,
                    null,
                    "Oatmeal is my favourite cereal", "Овсянка - моя любимая каша", null));
            transportSQL.addString(new VocabularyCard("C21", null, "heater",
                    "a device that produces heat", "устройство, которое производит тепло", "нагреватель, обогреватель",
                    "|ˈhiː.tə|", null, null, null, null,
                    null, null, null, null,
                    "And when it gets to that temperature, the heater turns off",
                    "И когда достигается нужная температура, нагреватель отключается", null,
                    null,
                    "We built this big heater", "Мы построили этот большой обогреватель", null));
            transportSQL.addString(new VocabularyCard("C22", null, "hidden",
                    "not easy to find", "что то, что нелегко найти", "спрятанный, скрытый",
                    "|ˈhɪd.ən|", null, null, null, null,
                    null, null, null, "adjective",
                    "There were hidden microphones in the room to record their conversation",
                    "В комнате были спрятаны микрофоны, чтобы записать их разговор", null,
                    null,
                    "There were hidden cameras in the room|There were hidden cameras in this room", "В этой комнате были скрытые камеры", null));
            transportSQL.addString(new VocabularyCard("C23", null, "underneath",
                    "under or below", "ниже чего либо", "ниже, под",
                    "|ˌʌn.dəˈniːθ|", null, null, "under, below", null,
                    null, null, null, "preposition, adverb",
                    "The tunnel goes right underneath the city",
                    "Туннель идет прямо под городом", null,
                    null,
                    "They found a bomb underneath the car|They found a bomb underneath this car", "Они нашли бомбу под этой машиной (здесь НЕ используются слова under или below)", null));
            transportSQL.addString(new VocabularyCard("C24", null, "boil",
                    "to reach, or cause something to reach, the temperature at which a liquid starts to turn into a gas", "приводить воду в газообразное состояние", "кипятить",
                    "|bɔɪl|", null, null, null, null,
                    null, null, null, "verb",
                    "She scalded herself on some boiling water",
                    "Она ошпарилась на кипящей воде", null,
                    null,
                    "We need to boil some water|We have to boil some water", "Нам нужно вскипятить немного воды", null));
            transportSQL.addString(new VocabularyCard("C25", null, "steam",
                    "the hot gas that is produced when water boils", "горячий газ испускаемый при кипении", "пар",
                    "|stiːm|", null, null, null, null,
                    null, null, null, null,
                    "The pump is driven by steam",
                    "Насос приводится в движение паром", null,
                    null,
                    "The shower room was full of steam|This shower room was full of steam", "Эта душевая комната была полна пара", null));
            transportSQL.addString(new VocabularyCard("C26", null, "cause",
                    "the reason why something happens", "причина, по которой что-то происходит", "причина, вызывать",
                    "|kɔːz|", null, null, "reason", null, null,
                    null, null, "null, verb",
                    "He's never given me any cause for concern",
                    "Она никогда не давала мне причин для беспокойства", null,
                    null,
                    "I don't think he had any cause to complain|I don't think that he had any cause to complain", "Я не думаю, что у него были причины жаловаться (здесь Не используется слово reason)", null));
            transportSQL.addString(new VocabularyCard("C27", null, "bend",
                    "to become curved, or to make something become curved", "делать что то кривым или искривляться самому", "искривлять, искривляться, сгибать, сгибаться, изгиб, сгиб",
                    "|bend|", null, null, null, null,
                    null, null, null, "null, verb",
                    "The trees were bending in the wind",
                    "Деревья искривлялись на ветру", null,
                    null,
                    "She bent the bar|She bent this bar", "Она согнула эту балку", null));
            transportSQL.addString(new VocabularyCard("C28", null, "pour",
                    "to make a liquid flow from or into a container", "лить жидкость в контейнер", "наливать",
                    "|pɔːr|", null, null, null, null,
                    null, null, null, "verb",
                    "I poured the milk into a jug",
                    "Я налил воду в кувшин", null,
                    null,
                    "I can pour you some tea", "Я могу налить тебе чаю", null));
            transportSQL.addString(new VocabularyCard("C29", null, "distract",
                    "to make someone stop giving their attention to something", "мешать кому то концентрироваться на чем-то", "отвлекать",
                    "|dɪˈstrækt|", null, null, null, null,
                    null, null, null, "verb",
                    "Stop distracting me - I'm trying to finish my essay",
                    "Не отвлекай меня - я пытаюсь закончить свое эссе", null,
                    null,
                    "Don't distract them", "Не отвлекай их", null));
            transportSQL.addString(new VocabularyCard("C30", null, "infinitesimal",
                    "extremely small, moving to zero", "крайне маленький, стремящийся к нулю", "мальчайший",
                    "|ˌɪn.fɪ.nɪˈtes.ɪ.məl|", null, null, null, null,
                    "Используется в основном в формальной речи", null, "formal", "adjective",
                    "The amounts of radioactivity present were infinitesimal",
                    "Количество радиоактивности было мельчайшим", null,
                    null,
                    "These creatures are infinitesimal", "Эти создания бесконечно малые", null));
            transportSQL.addString(new VocabularyCard("C31", null, "itsy-bitsy",
                    "extremely small", "крайне маленький", "малюсенький",
                    "|ˌɪt.siˈbɪt.si|", null, null, null, null,
                    "Не используется в формальной речи", null, "humorous", "adjective",
                    "The itsy-bitsy spider",
                    "Этот малюсенький паучок", null,
                    null,
                    "There are many itsy-bitsy spiders", "Здесь много малюсеньких паучков", null));
            transportSQL.addString(new VocabularyCard("C32", null, "weakling",
                    "someone who is weak, either physically or in character", "кто то слабый физически или морально", "слабак, слабый человек",
                    "|ˈwiːklɪŋ|", null, null, null, null,
                    "Не используется в формальной речи - считается оскорблением", null, "disapproving", null,
                    "Exercise can turn a weakling into a big, tough guy",
                    "Упражнения могут обратить слабака в большого, крутого парня", null,
                    null,
                    "You are weakling", "Ты слабак", null));
            transportSQL.addString(new VocabularyCard("C33", null, "refrigerator",
                    "a piece of kitchen equipment that uses electricity to preserve food at a cold temperature", "Кухонное оборудование, которое поддерживает холодную температуру для сохранения еды",
                    "холодильник",
                    "|rɪˈfrɪdʒ.ər.eɪ.tər|", null, null, null, null,
                    "чаще в разговорной речи используется слово fridge, refrigerator используется в формальной речи", null, "old-fashioned", null,
                    "Store fruit juice in the refrigerator",
                    "Храни фруктовый сок в холодильнике", null,
                    null,
                    "I saw your fridge in his kitchen|I saw your refrigerator in his kitchen", "Я видел твой холодильник на его кухне", null));

            transportSQL.addString(new VocabularyCard("C34", null, "stove",
                    "a piece of equipment that burns fuel or uses electricity in order to heat a place", "Кухонное оборудование, которое используют для приготовления еды",
                    "плита",
                    "|stəʊv|", null, null, null, null,
                    "чаще в разговорной речи используется слово fridge, refrigerator используется в формальной речи", null, null, null,
                    "She got some eggs out and heated a pan on the stove",
                    "Она достала несколько яиц и разогревала сковороду на плите", null,
                    null,
                    "You can cook something on this stove", "Вы можете приготовить что-нибудь на этой плите", null));

            transportSQL.addString(new VocabularyCard("C35", null, "bustle",
                    "do things in a quick, busy way. Busy activity", "двигаться или делать что-либо быстро и небрежно. Сумбурная деятельность",
                    "сновать, суетиться",
                    "|ˈbʌs.əl|", null, null, null, null,
                    null, null, null, "verb, noun",
                    "There were lots of shoppers bustling about",
                    "Вокруг суетилось множество покупателей", null,
                    null,
                    "The bustle of city is tiring me|City bustle bores me", "Городская суета утомляет меня", null));

            transportSQL.addString(new VocabularyCard("C36", null, "fall off",
                    "to get lower in amount or level:", "когда количество чего-либо уменьшается",
                    "спадать",
                    null, null, null, null, null,
                    null, null, null, "phrasal verb",
                    "Sales have been falling off recently",
                    "Продажи в последнее время падают", null,
                    null,
                    "The poverty is falling off this year", "бедность падает в этом году", null));

            transportSQL.addString(new VocabularyCard("C37", null, "fall-off",
                    "a reduction in the amount or quality of something", "уменьшение количества, качества чего-либо",
                    "спад",
                    null, null, null, null, null,
                    null, null, null, "noun",
                    "We have seen seen a dramatic falloff of demand in the final quarter",
                    "Мы видели резкое падение спроса в последнем квартале", null,
                    null,
                    "The fall-off of poverty", "Спад бедности", null));

            transportSQL.addString(new VocabularyCard("C38", null, "irrelevant",
                    "not related to what is being discussed", "то, что не относится к теме разговора и не рассматривается",
                    "неважно, неуместно, не имеет значения",
                    "|adjective|", null, null, null, null,
                    null, null, null, "adjective",
                    "These documents are irrelevant to the investigation",
                    "Эти документы неуместны к расследованию", null,
                    null,
                    "What she confined is irrelevant", "То, что она сказала, не имеет значение", null));

            transportSQL.addString(new VocabularyCard("C39", null, "den",
                    "the home of particular types of wild animal", "дом для некоторых диких животных",
                    "нора, берлога, логово",
                    "|den|", null, null, null, null,
                    null, null, null, null,
                    "You're walking into the lion's den",
                    "Ты идешь прямо в логово льва", null,
                    null,
                    "She walked into the lion's den", "Она пришла в логово льва", null));

            transportSQL.addString(new VocabularyCard("C40", null, "salad",
                    "a mixture of vegetables eaten as a separate dish", "смесь из овощей, являющаяся отдельным блюдом",
                    "салат",
                    "|ˈsæl.əd|", null, null, null, null,
                    null, null, null, null,
                    "And she cannot eat chicken salad",
                    "И ещё она терпеть не может куриный салат", null,
                    null,
                    "This is the best salad i have ever eaten|This is the best salad i have ever eaten", "Это лучший салат, который я когда-либо ел", null));

            }
            */
         /*
            {

            //transportSQL.closeDatabases();

            //transportSQL = MainTransportSQL.getTransport(PHRASEOLOGY_INDEX, context);
            //transportSQL.deleteCommon();
            //transportSQL.deleteRepeat();
            //transportSQL.deleteStudy();
            transportSQL.addString(new VocabularyCard("C41", null, "come on",
                    "used to encourage someone to do something, to hurry, to try harder, etc", "используется для мотивирования кого-либо",
                    "давай, ну-же", null, null, null,
                    null, "phrasal verb", "Come on! We're going to be late", "Давай же! Мы опоздываем!",
                    "Come on! Do that!|Come on! Do it!|Come on! Do this!", "Давай же! Сделай это!"));

            transportSQL.addString(new VocabularyCard("C42", null, "burn out",
                    "If a fire burns out, it stops producing flames because nothing remains that can burn", "когда пламя начинает производить меньше огня",
                    "угасать, сгорать, перегорать", null, "Может относиться не только непосредственно к пламени, но еще и к людям, приборам", null,
                    null, "phrasal verb", "The engine has burned out", "Двигатель перегорел",
                    "Our house has completely burned out|Our house has completely burnt out|Our house completely burned out|Our house completely burnt out|Our house is completely burned out|Our house is completely burnt out", "Наш дом полностью сгорел"));

            transportSQL.addString(new VocabularyCard("C43", null, "warm up",
                    "reach a sufficiently high temperature\n", "достичь достаточно высокой температуры",
                    "разминаться, разогреваться", null, null, null,
                    null, "phrasal verb", "They were warming-up before the match", "Они разминались перед матчем",
                    "Her kettle is warming-up|Her kettle is warming up", "Ее чайник разогревается"));

            transportSQL.addString(new VocabularyCard("C44", null, "give off", "to produce heat, light, a smell, or a gas", "производить тепло, свет, запах или газ", "выделять, испускать", null, null, null,
                    null, "phrasal verb", "The fire was giving off a lot of smoke", "Огонь выделял много дыма",
                    "The boiling water gives off steam|Boiling water gives off steam", "Кипящая вода выделяет пар"));

            transportSQL.addString(new VocabularyCard("C45", null, "heat up", "to make something become hot or warm, or to become hot or warm", "Нагревать что то или нагреваться самому",
                    "нагревать, подогревать", null, null, null,
                    null, "phrasal verb", "I'll just heat up some soup", "Я подогрею немного супа",
                    "I can heat up some pasta", "Я могу подогреть немного пасты"));

            transportSQL.addString(new VocabularyCard("C46", null, "start out", "to begin to do something", "Начать делать что-либо",
                    "начинать", null, "для того что бы сказать \"начать (свою карьеру) будучи кем-то\" нужно использовать конструкцию start out as somebody", null,
                    null, "phrasal verb", "He started out as a teacher", "Он начинал как учитель",
                    "She started out as baker", "Она начинала как пекарь"));

            transportSQL.addString(new VocabularyCard("C47", null, "hang on", "to wait for a short time", "Подождать немного",
                    "подождать", null, null, null,
                    null, "phrasal verb", "Hang on a minute - I'll be with you in a moment!", "Подожди минуту - я сейчас подойду",
                    "Hang on a minute", "Подожди минуту"));

            transportSQL.addString(new VocabularyCard("C48", null, "get out", "to go in a different place", "Уйти в другое место",
                    "выбираться, уходить", null, null, null,
                    null, "phrasal verb", "I told her to get out, but she wouldn't listen", "Я попросила её уйти - она не слушала",
                    "Let's get out of here|Let's get out of there", "Давай выбираться отсюда"));

            transportSQL.addString(new VocabularyCard("C49", null, "worse yet", "used when one thing is worse than another", "используется, чтобы показать, что одна вещь хуже другой",
                    "что еще хуже", null, null, null,
                    null, "steady expression", "Worse yet, we haven't even seen him yet", "Что еще хуже, мы его еще даже не видели",
                    "Worse yet, she left our team", "Что еще хуже, она покинула нашу команду"));

            transportSQL.addString(new VocabularyCard("C50", null, "in a while", "used if something happened long ago", "указывает на то, что что-либо произошло давно",
                    "давно, давненько", null, null, null,
                    null, "adverb", "I haven't heard about him in a while", "Я давно о нем не слышал",
                    "I haven't seen him in a while| I have not seen him in a while", "Давненько я его не видел"));

            transportSQL.addString(new VocabularyCard("C51", null, "at all", "in any way", "используется для указания на то, что что-либо нисколько не произошло",
                    "вообще, совсем", null, null, null,
                    null, "adverb", "He's not doing it at all any more", "Он больше этого вообще не делает",
                    "I don't know your parents at all", "Я совсем не знаю ваших родителей"));

            transportSQL.addString(new VocabularyCard("C52", null, "take care", "to protect someone or something and provide the things that that person or thing needs", "защищать кого-либо или что-либо и предоставлять ему вещи, в которых он нуждается",
                    "заботиться", null, "чтобы сказать \"позаботиться о ком-то\" нужно использовать конструкцию \"take care of somebody\"", null,
                    null, "verb", "Maybe you should take care of your own house first", "Может быть, тебе следует в первую очередь, позаботиться о собственном доме",
                    "I can take care of myself", "Я могу о себе позаботиться"));

            transportSQL.addString(new VocabularyCard("C53", null, "in return", "in exchange", "в обмен на что-то",
                    "взамен, в обмен", null, null, null,
                    null, "adverb", "Tom found nothing to say in return", "Том не нашел ничего, чтобы сказать в ответ",
                    "You have to do something for me in return|You must do something for me in return", "Вы должны сделать кое-что для меня взамен"));

            transportSQL.addString(new VocabularyCard("C54", null, "over here", "in the place where you are", "указывает на место, где ты находишься",
                    "сюда", null, null, null,
                    null, "adverb", "Over here against the wall, please", "Сюда, к стене, пожалуйста",
                    "Over here, Tom! Look!", "Сюда, Том! Посмотри!"));

            transportSQL.addString(new VocabularyCard("C55", null, "careful attention", "when someone cares about something very much", "проявление сильной заботы к чему-либо",
                    "пристальное внимание", null, null, null,
                    null, "noun", "Careful attention must be paid to every detail", "Пристальное внимание должно быть уделено каждой детали",
                    "We always pay careful attention to small details", "Мы всегда уделяем пристальное внимание маленьким деталям"));

            transportSQL.addString(new VocabularyCard("C56", null, "pay attention", "to watch, listen to, or think about something carefully", "уделять чему-либо внимания, быть внимательным",
                    "уделять внимание, обратить внимание", null, "обратить внимание на что-либо - pay attention to something", null,
                    null, "steady expression", "You weren't paying attention to what I was saying", "Ты не был внимательным к тому, что я говорил",
                    "You need to pay attention to this problem|You have to pay attention to this problem", "Вам нужно обратить внимание на эту проблему"));

            transportSQL.addString(new VocabularyCard("C57", null, "it is got enough", "when you have enough amount of something", "когда у тебя есть достаточное количество чего-либо",
                    "этого достаточно", null, null, null,
                    null, "steady expression", "It's got enough food to feed a family of 4 for a week", "Здесь достаточно еды, чтобы неделю кормить семью из 4 человек",
                    "It is got enough water for 3 weeks|It's got enough for 3 weeks", "Этого хватит на 3 недели"));

        }
        */
        //LibraryFragment.downloadFile();

        //transportSQL.closeDatabases();

        //transportSQL = MainTransportSQL.getTransport(PHRASEOLOGY_INDEX, context);
        //transportSQL.deleteCommon();
        //transportSQL.deleteRepeat();
        //transportSQL.deleteStudy();
        //transportSQL.closeDatabases();


    transportPreferences.setTodayRepeatReturned(context, consts.DATABASE_LOAD_COMPLETED);
    transportPreferences.setTodayStudyReturned(context, consts.DATABASE_LOAD_COMPLETED);

    transportPreferences.setTodayPhraseologyRepeatLoaded(context, consts.DATABASE_LOAD_NONE);
        transportPreferences.setTodayPhraseologyStudyLoaded(context, consts.DATABASE_LOAD_NONE);
        transportPreferences.setTodayStudyLoaded(context, consts.DATABASE_LOAD_NONE);
        transportPreferences.setTodayStudyReturned(context, consts.DATABASE_LOAD_NONE);


    }
    public static void setTodayTables(Context context) {                                            // set Today tables in start of day - reload this - id tables isn't empty and fill them again

        StatisticManager.setThisWeekInfo(context);

        if (Ruler.getDay(context) > transportPreferences.getLastDayComming(context)) {              // user didn't open the app this day
            StatisticManager.addNew(context);

            if (!transportPreferences.isStarted(context)) {                                         // user didn't open the app before
                transportPreferences.setStarted(context, true);
                regenerateDatabase(context);
                transportPreferences.setStartDay(context, Ruler.getAbsoluteDay());                  // init start day
                Helper.rebootAll(context);

                transportPreferences.setGameScores(context, 0);
                transportPreferences.setPlayerHealth(context, 25, 1);
                transportPreferences.setPlayerHealth(context, 25, 2);
                transportPreferences.setPlayerName(context, null);
                transportPreferences.setGamePlayed(context, false);
                transportPreferences.setWinAlwaysCount(context, 0);
                transportPreferences.setWinHolder(context, 1);

                transportPreferences.setExp(context, 0);
                transportPreferences.setLevel(context, 1);
                for (int i = 0; i <= PHONETIC_INDEX; i++)
                transportPreferences.setFrequency(context, i, FREQUENCY_LINEAR);

            }


            FirebaseDatabase.checkForMessagesForMe(context);

            transportPreferences.setDayPlanState(context, 0);
            transportPreferences.setDayCompleted(context, false);
            Shop.Thing.addPlan.reload(context);
            Shop.Thing.practice.reload(context);
            for (int i = 0; i <= PHONETIC_INDEX; i++){
                transportPreferences.setTodayLeftStudyCardQuantity(context, i,0);
                transportPreferences.settTodayLeftRepeatCardQuantity(context, i, 0);
                transportPreferences.setAllTodayRepeatCount(context, i,0);
                transportPreferences.setAllTodayStudyCount(context, i, 0);
            }


            //new Ruler(context, VOCABULARY_INDEX).reloadTodayTables();
            //new Ruler(context, PHRASEOLOGY_INDEX).reloadTodayTables();

            SqlMain sql = SqlVocabulary.getInstance(context);
            sql.getAllTodayCardsCount();
            sql.getTodayCardsCount();
            sql = SqlGrammar.getInstance(context);
            sql.getAllTodayCardsCount();
            sql.getTodayCardsCount();
            sql = SqlPhraseology.getInstance(context);
            sql.getAllTodayCardsCount();
            sql.getTodayCardsCount();
            sql = SqlException.getInstance(context);
            sql.getAllTodayCardsCount();
            sql.getTodayCardsCount();
            sql = SqlCulture.getInstance(context);
            sql.getAllTodayCardsCount();
            sql.getTodayCardsCount();
            sql = SqlPhonetics.getInstance(context);
            sql.getAllTodayCardsCount();
            sql.getTodayCardsCount();

            transportPreferences.setNothingStarted(context, false);

            for (int i = VOCABULARY_INDEX; i <= PHONETIC_INDEX; i++){
                if (transportPreferences.getAllTodayRepeatCount(context, i) != 0) break;
                if (transportPreferences.getAllTodayStudyCount(context, i) != 0) break;
                if (i == PHONETIC_INDEX)
                    transportPreferences.setNothingStarted(context, true);
            }

            Log.i("main", "nothing:" + transportPreferences.isNothingStarted(context));

            transportPreferences.setLastDayComming(context, Ruler.getDay(context));

            //TeacherManager.checkPaceEffectiveness(context, VOCABULARY_INDEX);
            //TeacherManager.checkForCritical(context);
        }

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /////// FILL PRACTICE DB ///////////////////////////////////////////////////////////////////////
    /*
    вопрос - а собственно нахрена нужен метод, который имеет только 1 строку?
    Так как все остальные операции с таблицами выполняются здесь, то я искала эти функции долгое
    время, короче - ради сохранения логики программы можно пойти и на такие жертвы
     */
    public ArrayList<Integer> fillPracticeDBForNextTest(){
        startToTrain();
        if (practiceCards == null)
            practiceCards = new ArrayList<>();
        else
            practiceCards.clear();
        return sqlMain.getCardsRepeatedToday(practiceCards);
    }
    public int fillPracticeDBForExam(String course){
        startToTrain();
        if (practiceCards == null)
            practiceCards = new ArrayList<>();
        else
            practiceCards.clear();
        return sqlMain.getCardsFromCourseForPractice(practiceCards, course);
    }

    public Card getRandomCard() {
        return sqlMain.getRandomCardFromStatic();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////


    public static void writeStatistic(){
        // утром перед зачислением нового дня записывается статистика по статам
        // ради этой цели в preference запихивается значение последнего отработанного дня
    }

    public static void checkPaceEffectiveness(int index){
    }


    public static void finishStart(Context context){
        transportPreferences.setStartDay(context, Ruler.getAbsoluteDay());
        transportPreferences.setLastDayComming(context, 0);
        transportPreferences.setExtraDayCount(context, 0);
        transportPreferences.setHelpStudy(context, 0);
    }

}
