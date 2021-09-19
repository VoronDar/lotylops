package com.lya_cacoi.lotylops.Databases.Union;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.lya_cacoi.lotylops.Databases.FirebaseAccess.FirebaseDatabase;
import com.lya_cacoi.lotylops.Databases.FirebaseAccess.pojo.Course;
import com.lya_cacoi.lotylops.Databases.transportSQL.SqlCulture;
import com.lya_cacoi.lotylops.Databases.transportSQL.SqlGrammar;
import com.lya_cacoi.lotylops.Databases.transportSQL.SqlMain;
import com.lya_cacoi.lotylops.Databases.transportSQL.SqlPhonetics;
import com.lya_cacoi.lotylops.Databases.transportSQL.SqlVocaPhrasUnion;
import com.lya_cacoi.lotylops.Databases.transportSQL.TransportSQLCourse;
import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.cards.Card;
import com.lya_cacoi.lotylops.cards.GrammarCard;
import com.lya_cacoi.lotylops.cards.PhoneticsCard;
import com.lya_cacoi.lotylops.cards.VocabularyCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.CULTURE_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.GRAMMAR_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.PHONETIC_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.VOCABULARY_INDEX;

/*

отвечает за передачу между fb sql и view

для чего используются базы данных


выполнять в другом потоке


подгрузка карт:

подгрузка курсов:

1. при инициализации
если пользователь зашел в приложение в первый раз, то загружается информация о всех курсах
(зы - поставить другой preference на это)
, а именно - имя(id), текущая версия,


не чаще, чем раз в день при входе в программу идет проверка обновлений
с фб загружается информация о курсах и их версиях - эта ин

не важно, доступен ли курс, все карты загружаются. Изображения и озвучка загружаются только для активированных курсов

 */
public class DBTransporter implements FirebaseTransporter, SqlTransporter {


    /*

    как работает - getAllCourses -> onNo/AllCourcesGet

     */

    private final TransportRunnable view;
    private TransportSQLCourse transportSQLCourse;
    private SqlMain transportSQL;
    private final FirebaseDatabase db;

    private int a = 0;
    private int loaded_count = 0;



    private final HashMap<String, Boolean> costil = new HashMap<>();
    /*

    как я понял. фб создает свои потоки для загрузки данных. и для того, чтобы отследить, что все выполнилось я использую вот эту хрень
    типо - на начало работы поток вставляет в какое-то поле определенное значение - в конце убирает

    цикл executer просто смотрит, не пуст ли этот костыль
     */

    public Updater updater;

    boolean isCourseDownLoad = false;
    String id;
    int index;
    boolean isNotNewVersion = false;


    public DBTransporter(TransportRunnable view){
        this.view = view;
        this.db = new FirebaseDatabase(this);
        this.updater = new Updater();
    }

    public DBTransporter(TransportRunnable view, String id, int index){
        this(view);
        this.isCourseDownLoad = true;
        this.id = id;
        this.index = index;
    }


    private void getCourse(){
        this.transportSQLCourse = TransportSQLCourse.getInstance(view.getContext(), index);
        this.transportSQL = SqlMain.getInstance(view.getContext(), index);
        a = 0;
        db.getCourse(index, id, getContext());
        while (a == 0 || costil.size() > 0);
        transportSQL.getTodayCardsCount();
    }



    private void getAllCourses(){
        /*
        важно - максимально курсы обновляются раз в день
         */

        Log.i("main", "start");
        updater.update(TransportRunnable.ActionTypes.sendMessage, "проверка обновлений");
        for (int i = VOCABULARY_INDEX; i <= PHONETIC_INDEX; i++) {
            a = 0;
            costil.clear();
            this.transportSQLCourse = TransportSQLCourse.getInstance(view.getContext(), i);
            this.transportSQL = SqlMain.getInstance(view.getContext(), i);
            updater.update(TransportRunnable.ActionTypes.updateIndex, i);
            db.getAllCources(i, getContext());
            while (a == 0 || costil.size() > 0);
            updater.update(TransportRunnable.ActionTypes.updateIndex, i);
            transportSQL.closeDatabases();
            transportSQLCourse.closeDb();
        }
    }

    @Override
    public void onNoCourcesGet() {
        Log.i("main", "got nothing");
        a = 1;
        updater.update(TransportRunnable.ActionTypes.error);
    }

    @Override
    public void onVocPhrAllCourcesGet(final ArrayList<Course> courses){
        Log.i("main", "got courses");
        transportSQL.openDbForUpdate();
        //transportSQL.deleteDb();
        final ArrayList<Course> lastupdated = transportSQLCourse.getAllCources();
        int nowIndex = transportSQLCourse.getDbIndex();

        updater.update(TransportRunnable.ActionTypes.updateIndex, nowIndex);

        costil.put(nowIndex + "c", true);


        low:
        for (final Course n : courses) {
            costil.put(n.getId(), true);
            a = 1;
            for (final Course j : lastupdated) {
                if (j.getId().equals(n.getId())) {
                    if (j.getVersion() < n.getVersion() && j.isEnabled()) {


                        updater.update(TransportRunnable.ActionTypes.sendMessage, "обновление курса '" + j.getId() + "'");

                        costil.put(j.getId() + "card", true);

                        db.getAllCardsFromCourse(nowIndex, n.getId(), new CardsGetter() {
                            @Override
                            public void onAllCardsGet(ArrayList<Card> cards) {
                                for (final Card card: cards) {
                                    transportSQL.updateCard(card);
                                    if (card.getTrains() != null && card.getTrains().size() > 0)
                                        transportSQL.updateTrains(card.getTrains());
                                    if (((VocabularyCard)card).getExamples() != null && ((VocabularyCard)card).getExamples().size() > 0)
                                    ((SqlVocaPhrasUnion)transportSQL).updateExamples(((VocabularyCard)card).getExamples());
                                    if (((VocabularyCard)card).getDialogueTest() != null)
                                        ((SqlVocaPhrasUnion)transportSQL).updateDialogue(((VocabularyCard)card).getDialogueTest());

                                    /*
                                    costil.put(card.getId() + "fl", true);
                                    db.loadImg(card.getId(), new EndGetter(){
                                        @Override
                                        public void onEnd() {
                                            costil.remove(card.getId() + "fl");
                                            updater.update();
                                        }
                                    });

                                     */
                                }

                                pushInOrder(cards, j, transportSQL);

                                n.setEnabled(true);
                                n.setAccessibility(true);
                                transportSQLCourse.updateCourse(n);
                                costil.remove(j.getId() + "card");

                            }

                            @Override
                            public void onNoCardsGet() {
                                costil.remove(j.getId() + "card");

                            }
                        });

                    } else if (isCourseDownLoad && j.getVersion() >= n.getVersion()){
                        updater.update(TransportRunnable.ActionTypes.nowCourseWontBeUpdated);
                    }
                    costil.remove(n.getId());
                    continue low;
                }
            }
            n.setVersion(0);
            transportSQLCourse.AddCourse(n);
            costil.remove(n.getId());

        }

        costil.remove(nowIndex + "c");

    }

    @Deprecated
    public void onAllCourcesGet(final ArrayList<Course> courses) {
        Log.i("main", "got courses");
        transportSQL.openDbForUpdate();
        //transportSQL.deleteDb();
        final ArrayList<Course> lastupdated = transportSQLCourse.getAllCources();
        int nowIndex = transportSQLCourse.getDbIndex();

        updater.update(TransportRunnable.ActionTypes.updateIndex, nowIndex);

         costil.put(nowIndex + "c", true);

        low:
        for (final Course n : courses) {
            costil.put(n.getId(), true);
            a = 1;
            for (final Course j : lastupdated) {

                if (j.getId().equals(n.getId())) {
                    // если курс был обновлен, обновляем все карты - картинку заново никогда не подгружаем,
                    // если только таковая не отсутствует вообще
                    // важно - даже если курс еще не активирован - карты все равно необходимо добавить
                    // абсолютно новые карты добавляются заного
                    // важно - у карт в sql должна лежать информация о том, были ли обновлены какие-либо части карты
                    if (j.getVersion() < n.getVersion()) {

                        updater.update(TransportRunnable.ActionTypes.sendMessage, "обновление курса '" + j.getId() + "'");

                        costil.put(j.getId() + "card", true);
                        db.getAllCardsFromCourse(nowIndex, j.getId(), new CardsGetter() {
                            @Override
                            public void onAllCardsGet(ArrayList<Card> crd) {
                                final ArrayList<VocabularyCard> cards = new ArrayList<>();
                                for (Card c: crd){
                                    cards.add((VocabularyCard) c);
                                }
                                ArrayList<Card> cardsFromSql = transportSQL.getAllCardsFromCourse(j.getId());

                                add:
                                for (int i = 0; i < cards.size(); i++){
                                    final int Fi = i;
                                    for (int k = 0; k < cardsFromSql.size(); k++) {if (cards.get(i).getId().equals(cardsFromSql.get(k).getId())){
                                        final int Fk = k;
                                            if (cards.get(i).isOverWrite()){
                                                updater.update(TransportRunnable.ActionTypes.checkOverWrite);
                                            } else{
                                                // если есть примеры, которые надо обновить
                                                //if ()
                                                //else
                                                {


                                                    //updater.update(TransportRunnable.ActionTypes.sendMessage, "обновление карты '" + cards.get(k).getId() + "'");
                                                    updater.update();
                                                    costil.put(cards.get(k).getId(), true);
                                                    db.getExamples(cards.get(k).getId(), new SentenceGetter(){
                                                        @Override
                                                        public void onAllSentenceGet(ArrayList<VocabularyCard.Sentence> crd) {
                                                            cards.get(Fk).setExamples(crd);


                                                            ArrayList<VocabularyCard.Sentence> sentences = new ArrayList<>();
                                                            if (cards.get(Fk).getExamples() != null){
                                                                for (VocabularyCard.Sentence s: cards.get(Fk).getExamples()){
                                                                    if (!cards.get(Fk).getExamples().contains(s)) {
                                                                        sentences.add(s);
                                                                    }
                                                                }
                                                                //transportSQL.updateExamples(sentences);
                                                                ((SqlVocaPhrasUnion)transportSQL).updateExamples(crd);
                                                            }
                                                            costil.remove(cards.get(Fk).getId());
                                                        }

                                                        @Override
                                                        public void onNoSentenceGet() {
                                                            costil.remove(cards.get(Fk).getId());
                                                            updater.update();
                                                        }
                                                    });


                                                    costil.put(cards.get(Fk).getId() + "t", true);
                                                    db.getTrains(cards.get(k).getId(), new SentenceGetter(){
                                                        @Override
                                                        public void onAllSentenceGet(ArrayList<VocabularyCard.Sentence> crd) {
                                                            ArrayList<VocabularyCard.Sentence> sentences = new ArrayList<>();
                                                            cards.get(Fk).setTrains(crd);
                                                            if (cards.get(Fk).getTrains() != null) {
                                                                sentences.clear();
                                                                for (VocabularyCard.Sentence s : cards.get(Fk).getTrains()) {
                                                                    if (!cards.get(Fk).getTrains().contains(s)) {
                                                                        sentences.add(s);
                                                                    }
                                                                }
                                                                //transportSQL.updateTrains(sentences);
                                                                transportSQL.updateTrains(crd);
                                                            }

                                                            costil.remove(cards.get(Fk).getId() + "t");
                                                            updater.update();
                                                        }

                                                        @Override
                                                        public void onNoSentenceGet() {
                                                            costil.remove(cards.get(Fk).getId() + "t");

                                                        }
                                                    });

                                                    transportSQL.updateCard(cards.get(i));


                                                }
                                            }
                                            continue add;
                                        }

                                    }



                                    costil.put(cards.get(Fi).getId() + "_2e", true);

                                    //updater.update(TransportRunnable.ActionTypes.sendMessage, "добавление карты '" + cards.get(i).getId() + "'");
                                    db.getExamples(cards.get(i).getId(), new SentenceGetter() {
                                        @Override
                                        public void onAllSentenceGet(ArrayList<VocabularyCard.Sentence> sentences) {
                                            cards.get(Fi).setExamples(sentences);
                                            updater.update();
                                            costil.remove(cards.get(Fi).getId() + "_2e");
                                        }

                                        @Override
                                        public void onNoSentenceGet() {
                                            costil.remove(cards.get(Fi).getId() + "_2e");

                                        }
                                    });
                                    costil.put(cards.get(Fi).getId() + "_2t", true);
                                    db.getTrains(cards.get(i).getId(), new SentenceGetter() {
                                        @Override
                                        public void onAllSentenceGet(ArrayList<VocabularyCard.Sentence> sentences) {
                                            cards.get(Fi).setTrains(sentences);
                                            updater.update();
                                            costil.remove(cards.get(Fi).getId() + "_2t");
                                        }

                                        @Override
                                        public void onNoSentenceGet() {
                                            costil.remove(cards.get(Fi).getId() + "_2t");

                                        }
                                    });


                                    //updater.update(TransportRunnable.ActionTypes.sendMessage, "загрузка файлов для карты '" + cards.get(i).getId() + "'");
                                    costil.put(cards.get(i).getId() + "fl", true);
                                    db.loadImg(cards.get(i).getId(), new EndGetter(){
                                        @Override
                                        public void onEnd() {
                                            costil.remove(cards.get(Fi).getId() + "fl");
                                            updater.update();
                                        }
                                    });
                                    costil.put(cards.get(i).getId() + "fm3", true);
                                    db.loadSound(cards.get(i).getId(), new EndGetter(){
                                        @Override
                                        public void onEnd() {
                                            costil.remove(cards.get(Fi).getId() + "fm3");
                                            updater.update();
                                        }
                                    });

                                    transportSQL.addNewCard(cards.get(i));
                                    if (j.isEnabled()){
                                        transportSQL.pushInProgress(cards.get(i));
                                    }
                                    updater.update();


                                }
                                costil.remove(j.getId() + "card");
                                transportSQLCourse.updateCourse(n, j);
                                costil.remove(n.getId());
                            }

                            @Override
                            public void onNoCardsGet() {
                                Log.i("main", "nothing come");
                                costil.remove(j.getId() + "card");
                                transportSQLCourse.updateCourse(n, j);
                                costil.remove(n.getId());
                            }
                        });




                    } else{
                        costil.remove(n.getId());
                    }

                    continue low;
                }
            }

            costil.remove(n.getId());

            costil.put(n.getId() + "-l", true);
            db.getAllCardsFromCourse(nowIndex, n.getId(), new CardsGetter() {
                @Override
                public void onAllCardsGet(final ArrayList<Card> cards){
                    for (int i = 0; i < cards.size(); i++) {

                        final VocabularyCard card = (VocabularyCard) cards.get(i);

                        costil.put(card.getId() + "e", true);
                        db.getExamples(card.getId(), new SentenceGetter(){
                            @Override
                            public void onAllSentenceGet(ArrayList<VocabularyCard.Sentence> crd) {
                                card.setExamples(crd);

                                ArrayList<VocabularyCard.Sentence> sentences = new ArrayList<>();
                                if (card.getExamples() != null){
                                    for (VocabularyCard.Sentence s: card.getExamples()){
                                        if (!card.getExamples().contains(s)) {
                                            sentences.add(s);
                                        }
                                    }
                                    ((SqlVocaPhrasUnion)transportSQL).updateExamples(crd);
                                }
                                costil.remove(card.getId() + "e");
                            }

                            @Override
                            public void onNoSentenceGet() {
                                costil.remove(card.getId() + "e");
                            }
                       });


                    //updater.update(TransportRunnable.ActionTypes.sendMessage, "добавление карты '" + cards.get(i).getId() + "'");
                        costil.put(card.getId() + "t", true);
                        db.getTrains(card.getId(), new SentenceGetter(){
                            @Override
                            public void onAllSentenceGet(ArrayList<VocabularyCard.Sentence> crd) {
                                ArrayList<VocabularyCard.Sentence> sentences = new ArrayList<>();
                                card.setTrains(crd);
                                if (card.getTrains() != null) {
                                    sentences.clear();
                                    for (VocabularyCard.Sentence s : card.getTrains()) {
                                        if (!card.getTrains().contains(s)) {
                                            sentences.add(s);
                                        }
                                    }
                                    transportSQL.updateTrains(crd);
                                }


                                costil.remove(card.getId() + "t");
                                updater.update();
                            }

                            @Override
                            public void onNoSentenceGet() {
                                costil.remove(card.getId() + "t");

                            }
                        });

                        costil.put(cards.get(i).getId() + "fl", true);
                        final int Fi = i;
                        db.loadImg(cards.get(i).getId(), new EndGetter(){
                            @Override
                            public void onEnd() {
                                costil.remove(cards.get(Fi).getId() + "fl");
                                updater.update();
                            }
                        });
                        costil.put(cards.get(i).getId() + "fm3", true);
                        db.loadSound(cards.get(i).getId(), new EndGetter(){
                            @Override
                            public void onEnd() {
                                costil.remove(cards.get(Fi).getId() + "fm3");
                                updater.update();
                            }
                        });
                        //updater.update(TransportRunnable.ActionTypes.sendMessage, "загрузка файлов для карты '" + cards.get(i).getId() + "'");
                        transportSQL.addNewCardToStatic((VocabularyCard) cards.get(i));
                        if (n.isEnabled()){
                            transportSQL.pushInProgress((VocabularyCard)cards.get(i));
                        }
                        updater.update();



                    }
                    transportSQLCourse.AddCourse(n);
                    costil.remove(n.getId() + "-l");
                }

                @Override
                public void onNoCardsGet() {
                    transportSQLCourse.AddCourse(n);
                    Log.i("main", "nothing come");
                    costil.remove(n.getId() + "-l");

                }
            });
        }
        costil.remove(nowIndex +"c");
    }

    @Override
    public void onAllGrammarCourcesGet(ArrayList<Course> courses) {
/*
        Log.i("main", "got courses");
        transportSQL.openDbForUpdate();
        //transportSQL.deleteDb();
        final ArrayList<Course> lastupdated = transportSQLCourse.getAllCources();

        updater.update(TransportRunnable.ActionTypes.updateIndex, GRAMMAR_INDEX);
        costil.put("c", true);

        low:
        for (final Course n : courses) {
            costil.put(n.getId(), true);
            a = 1;
            for (final Course j : lastupdated) {


                if (j.getId().equals(n.getId())) {
                    if (j.getVersion() < n.getVersion()) {

                        updater.update(TransportRunnable.ActionTypes.sendMessage, "обновление курса '" + j.getId() + "'");

                        costil.put(j.getId() + "card", true);
                        db.getAllCardsFromCourse(GRAMMAR_INDEX, j.getId(), new CardsGetter() {
                            @Override
                            public void onAllCardsGet(ArrayList<Card> crd) {
                                final ArrayList<GrammarCard> cards = new ArrayList<>();
                                for (Card c: crd){
                                    cards.add((GrammarCard) c);
                                }
                                ArrayList<Card> cardsFromSql = transportSQL.getAllCardsFromCourse(j.getId());

                                add:
                                for (int i = 0; i < cards.size(); i++){
                                    final int Fi = i;
                                    for (int k = 0; k < cardsFromSql.size(); k++) {
                                        if (cards.get(i).getId().equals(cardsFromSql.get(k).getId())) {
                                            transportSQL.updateCard(cards.get(Fi));
                                        } else {
                                            transportSQL.addNewCardToStatic(cards.get(Fi));
                                            if (j.isEnabled())
                                                transportSQL.pushInProgress(cards.get(Fi));

                                        }
                                        final int Fk = k;
                                        //if (cards.get(i).isOverWrite()){
                                        //    updater.update(TransportRunnable.ActionTypes.checkOverWrite);
                                        //} else
                                        {
                                            // если есть примеры, которые надо обновить
                                            //if ()
                                            //else
                                            {


                                                if (cards.get(Fi).getTrainsId() != null) {
                                                    for (final String id : cards.get(Fi).getTrainsId()) {
                                                        costil.put(cards.get(Fi).getId() + id + "t", true);
                                                        db.getTrainById(id, new SentenceGetter() {
                                                            @Override
                                                            public void onAllSentenceGet(ArrayList<VocabularyCard.Sentence> crd) {
                                                                if (cards.get(Fi).getTrains() == null)
                                                                    cards.get(Fi).setTrains(crd);
                                                                else
                                                                    cards.get(Fi).getTrains().addAll(crd);
                                                                transportSQL.updateTrains(crd);
                                                                costil.remove(cards.get(Fi).getId() + id + "t");
                                                                updater.update();
                                                            }

                                                            @Override
                                                            public void onNoSentenceGet() {
                                                                costil.remove(cards.get(Fi).getId() + id + "t");

                                                            }
                                                        });
                                                    }
                                                }

                                                if (cards.get(Fi).getBlockTrainsId() != null) {
                                                    for (final String id : cards.get(Fi).getBlockTrainsId()) {
                                                        costil.put(cards.get(Fi).getId() + id + "bl", true);
                                                        db.getTrainById(id, new SentenceGetter() {
                                                            @Override
                                                            public void onAllSentenceGet(ArrayList<VocabularyCard.Sentence> crd) {
                                                                if (cards.get(Fi).getBlockTrains() == null)
                                                                    cards.get(Fi).setBlockTrains(crd);
                                                                else
                                                                    cards.get(Fi).getBlockTrains().addAll(crd);
                                                                transportSQL.updateTrains(crd);
                                                                costil.remove(cards.get(Fi).getId() + id + "bl");
                                                                updater.update();
                                                            }

                                                            @Override
                                                            public void onNoSentenceGet() {
                                                                costil.remove(cards.get(Fi).getId() + id + "bl");

                                                            }
                                                        });
                                                    }
                                                }

                                                if (cards.get(Fi).getSelectTrainsId() != null) {
                                                    for (final String id : cards.get(Fi).getSelectTrainsId()) {
                                                        costil.put(cards.get(Fi).getId() + id + "sl", true);
                                                        db.getSelectTrain(id, new SelectGetter() {
                                                            @Override
                                                            public void onAllSentenceGet(ArrayList<GrammarCard.SelectTrain> crd) {
                                                                if (cards.get(Fi).getSelectTrains() == null)
                                                                    cards.get(Fi).setSelectTrains(crd);
                                                                else
                                                                    cards.get(Fi).getSelectTrains().addAll(crd);
                                                                ((SqlGrammar) transportSQL).updateSelect(crd);
                                                                costil.remove(cards.get(Fi).getId() + id + "sl");
                                                                updater.update();
                                                            }

                                                            @Override
                                                            public void onNoSentenceGet() {
                                                                costil.remove(cards.get(Fi).getId() + id + "sl");

                                                            }
                                                        });
                                                    }
                                                }
                                                transportSQL.updateCard(cards.get(i));
                                            }
                                        }
                                        continue add;

                                    }
                                }
                                costil.remove(j.getId() + "card");
                                //pushInOrder(crd, n, transportSQL);
                                transportSQLCourse.updateCourse(n, j);

                                costil.remove(n.getId());
                            }

                            @Override
                            public void onNoCardsGet() {
                                Log.i("main", "nothing come");
                                costil.remove(j.getId() + "card");
                                transportSQLCourse.updateCourse(n, j);

                                costil.remove(n.getId());
                            }
                        });




                    } else{
                            costil.remove(n.getId());
                    }
                    continue low;
                }
            }

            costil.remove(n.getId());
            transportSQLCourse.AddCourse(n);
            costil.put(n.getId() + "-l", true);
            db.getAllCardsFromCourse(GRAMMAR_INDEX, n.getId(), new CardsGetter() {
                @Override
                public void onAllCardsGet(final ArrayList<Card> cards){
                    for (int i = 0; i < cards.size(); i++) {

                        final GrammarCard card = (GrammarCard) cards.get(i);
                        if (card.getTrainsId() != null) {
                            for (final String id : card.getTrainsId()) {
                                costil.put(card.getId() + id + "t", true);
                                db.getTrainById(id, new SentenceGetter() {
                                    @Override
                                    public void onAllSentenceGet(ArrayList<VocabularyCard.Sentence> crd) {
                                        if (card.getTrains() == null)
                                            card.setTrains(crd);
                                        else
                                            card.getTrains().addAll(crd);
                                        transportSQL.updateTrains(crd);
                                        costil.remove(card.getId() + id + "t");
                                        updater.update();
                                    }

                                    @Override
                                    public void onNoSentenceGet() {
                                        costil.remove(card.getId() + id + "t");

                                    }
                                });
                            }
                        }

                        if (card.getBlockTrainsId() != null) {
                            for (final String id : card.getBlockTrainsId()) {
                                costil.put(card.getId() + id + "bl", true);
                                db.getTrainById(id, new SentenceGetter() {
                                    @Override
                                    public void onAllSentenceGet(ArrayList<VocabularyCard.Sentence> crd) {
                                        if (card.getBlockTrains() == null)
                                            card.setBlockTrains(crd);
                                        else
                                            card.getBlockTrains().addAll(crd);
                                        transportSQL.updateTrains(crd);
                                        costil.remove(card.getId() + id + "bl");
                                        updater.update();
                                    }

                                    @Override
                                    public void onNoSentenceGet() {
                                        costil.remove(card.getId() + id + "bl");

                                    }
                                });
                            }
                        }

                        if (card.getSelectTrainsId() != null) {
                            for (final String id : card.getSelectTrainsId()) {
                                costil.put(card.getId() + id + "sl", true);
                                db.getSelectTrain(id, new SelectGetter() {
                                    @Override
                                    public void onAllSentenceGet(ArrayList<GrammarCard.SelectTrain> crd) {
                                        if (card.getSelectTrains() == null)
                                            card.setSelectTrains(crd);
                                        else
                                            card.getSelectTrains().addAll(crd);

                                        ((SqlGrammar) transportSQL).updateSelect(crd);
                                        costil.remove(card.getId() + id + "sl");
                                        updater.update();
                                    }

                                    @Override
                                    public void onNoSentenceGet() {
                                        costil.remove(card.getId() + id + "sl");

                                    }
                                });
                            }
                        }
                        //updater.update(TransportRunnable.ActionTypes.sendMessage, "загрузка файлов для карты '" + cards.get(i).getId() + "'");
                        transportSQL.addNewCardToStatic(card);
                        updater.update();

                    }

                    //pushInOrder(cards, n, transportSQL);
                    costil.remove(n.getId() + "-l");
                }

                @Override
                public void onNoCardsGet() {
                    Log.i("main", "nothing come");
                    costil.remove(n.getId() + "-l");

                }
            });
            costil.remove(n.getId());
        }


        costil.remove("c");

 */

        Log.i("main", "got courses");
        transportSQL.openDbForUpdate();
        //transportSQL.deleteDb();
        final ArrayList<Course> lastupdated = transportSQLCourse.getAllCources();

        updater.update(TransportRunnable.ActionTypes.updateIndex, GRAMMAR_INDEX);
        costil.put("c", true);

        low:
        for (final Course n : courses) {
            costil.put(n.getId(), true);
            a = 1;
            for (final Course j : lastupdated) {


                if (j.getId().equals(n.getId())) {
                    if (j.getVersion() < n.getVersion() && j.isEnabled()) {

                        updater.update(TransportRunnable.ActionTypes.sendMessage, "обновление курса '" + j.getId() + "'");

                        costil.put(j.getId() + "card", true);
                        db.getAllCardsFromCourse(GRAMMAR_INDEX, j.getId(), new CardsGetter() {
                            @Override
                            public void onAllCardsGet(ArrayList<Card> crd) {
                                final ArrayList<GrammarCard> cards = new ArrayList<>();
                                for (Card c: crd){
                                    final GrammarCard gc = (GrammarCard)c;

                                    if (gc.getTrainsId() != null) {
                                        for (final String id : gc.getTrainsId()) {
                                            costil.put(gc.getId() + id + "t", true);
                                            db.getTrainById(id, new SentenceGetter() {
                                                @Override
                                                public void onAllSentenceGet(ArrayList<VocabularyCard.Sentence> crd) {
                                                    if (gc.getTrains() == null)
                                                        gc.setTrains(crd);
                                                    else
                                                        gc.getTrains().addAll(crd);
                                                    transportSQL.updateTrains(crd);
                                                    costil.remove(gc.getId() + id + "t");
                                                    updater.update();
                                                }

                                                @Override
                                                public void onNoSentenceGet() {
                                                    costil.remove(gc.getId() + id + "t");

                                                }
                                            });
                                        }
                                    }

                                    if (gc.getBlockTrainsId() != null) {
                                        for (final String id : gc.getBlockTrainsId()) {
                                            costil.put(gc.getId() + id + "bl", true);
                                            db.getTrainById(id, new SentenceGetter() {
                                                @Override
                                                public void onAllSentenceGet(ArrayList<VocabularyCard.Sentence> crd) {
                                                    if (gc.getBlockTrains() == null)
                                                        gc.setBlockTrains(crd);
                                                    else
                                                        gc.getBlockTrains().addAll(crd);
                                                    transportSQL.updateTrains(crd);
                                                    costil.remove(gc.getId() + id + "bl");
                                                    updater.update();
                                                }

                                                @Override
                                                public void onNoSentenceGet() {
                                                    costil.remove(gc.getId() + id + "bl");

                                                }
                                            });
                                        }
                                    }

                                    if (gc.getSelectTrainsId() != null) {
                                        for (final String id : gc.getSelectTrainsId()) {
                                            costil.put(gc.getId() + id + "sl", true);
                                            db.getSelectTrain(id, new SelectGetter() {
                                                @Override
                                                public void onAllSentenceGet(ArrayList<GrammarCard.SelectTrain> crd) {
                                                    if (gc.getSelectTrains() == null)
                                                        gc.setSelectTrains(crd);
                                                    else
                                                        gc.getSelectTrains().addAll(crd);
                                                    ((SqlGrammar) transportSQL).updateSelect(crd);
                                                    costil.remove(gc.getId() + id + "sl");
                                                    updater.update();
                                                }

                                                @Override
                                                public void onNoSentenceGet() {
                                                    costil.remove(gc.getId() + id + "sl");

                                                }
                                            });
                                        }
                                    }
                                    transportSQL.updateCard(c);
                                }

                                pushInOrder(crd, j, transportSQL);

                                costil.remove(j.getId() + "card");
                                //pushInOrder(crd, n, transportSQL);
                                transportSQLCourse.updateCourse(n, j);

                                costil.remove(n.getId());
                            }

                            @Override
                            public void onNoCardsGet() {
                                Log.i("main", "nothing come");
                                costil.remove(j.getId() + "card");
                                transportSQLCourse.updateCourse(n, j);

                                costil.remove(n.getId());
                            }
                        });




                    } else{

                        if (isCourseDownLoad && j.getVersion() >= n.getVersion()){
                            updater.update(TransportRunnable.ActionTypes.nowCourseWontBeUpdated);
                        }

                        costil.remove(n.getId());
                    }
                    continue low;
                }
            }

            costil.remove(n.getId());
            n.setVersion(0);
            transportSQLCourse.AddCourse(n);
        }


        costil.remove("c");
    }


    private void getSelectCard(final List<String> ids) {
        //Log.i("main", "add selects ");
        if (ids == null) return;
        for (final String id: ids) {
            costil.put(id + "select", true);
            //Log.i("main", "there are selects ");
            db.getSelectTrain(id, new SelectGetter() {
                @Override
                public void onAllSentenceGet(ArrayList<GrammarCard.SelectTrain> crd) {
                    //Log.i("main", "select " + id);
                    if (transportSQLCourse.getDbIndex() == CULTURE_INDEX)
                        ((SqlCulture) transportSQL).updateSelect(crd.get(0));
                    else
                        ((SqlPhonetics)transportSQL).updateSelect(crd);
                    costil.remove(id + "select");
                    updater.update();
                }

                @Override
                public void onNoSentenceGet() {
                    //Log.i("main", "there is no select " + id);
                    costil.remove(id + "select");
                    updater.update();
                }
            });
        }
    }

    private void getSelectCard(final String id) {
        Log.i("main", "add selects ---");
        if (id == null) return;
        List<String> list = new ArrayList<>(1);
        list.add(id);
        getSelectCard(list);
    }

    @Override
    public void onAllSimpleCourseGet(final ArrayList<Course> courses) {
/*
        transportSQL.openDbForUpdate();
        final ArrayList<Course> lastupdated = transportSQLCourse.getAllCources();
        final int nowIndex = transportSQLCourse.getDbIndex();
        updater.update(TransportRunnable.ActionTypes.updateIndex, nowIndex);
        costil.put("s" + nowIndex, true);

        low:
        for (final Course n : courses) {
            costil.put(n.getId(), true);
            a = 1;
            for (final Course j : lastupdated) {


                if (j.getId().equals(n.getId())) {
                    if (j.getVersion() < n.getVersion()) {


                        updater.update(TransportRunnable.ActionTypes.sendMessage, "обновление курса '" + j.getId() + "'");


                        costil.put(j.getId() + "card", true);
                        db.getAllCardsFromCourse(nowIndex, j.getId(), new CardsGetter() {
                            @Override
                            public void onAllCardsGet(ArrayList<Card> cards) {
                                ArrayList<Card> cardsFromSql = transportSQL.getAllCardsFromCourse(j.getId());

                                add:
                                for (int i = 0; i < cards.size(); i++){
                                    for (int k = 0; k < cardsFromSql.size(); k++) {
                                        if (cards.get(i).getId().equals(cardsFromSql.get(k).getId())) {
                                            transportSQL.updateCard(cards.get(i));
                                            if (nowIndex == CULTURE_INDEX) getSelectCard(cards.get(i).getId());
                                            else if (nowIndex == PHONETIC_INDEX) getSelectCard(((PhoneticsCard)cards.get(i)).getSelectTrainsId());
                                            if (j.isEnabled())
                                                transportSQL.updateProgress(cards.get(i));
                                            continue add;
                                        }}
                                        transportSQL.addNewCard(cards.get(i));
                                        if (j.isEnabled())
                                            transportSQL.pushInProgress(cards.get(i));
                                }

                                costil.remove(j.getId() + "card");
                                transportSQLCourse.updateCourse(n);
                                costil.remove(n.getId());
                            }

                            @Override
                            public void onNoCardsGet() {
                                costil.remove(j.getId() + "card");
                                transportSQLCourse.updateCourse(n);
                                costil.remove(n.getId());
                            }
                        });} else{
                            costil.remove(n.getId());
                    }


                    continue low;
                }
            }

            costil.remove(n.getId());
            transportSQLCourse.AddCourse(n);
            costil.put(n.getId() + "card", true);
            db.getAllCardsFromCourse(nowIndex, n.getId(), new CardsGetter() {
                @Override
                public void onAllCardsGet(ArrayList<Card> crd) {

                    for (int i = 0; i < crd.size(); i++) {
                        transportSQL.addNewCardToStatic(crd.get(i));
                        if (nowIndex == CULTURE_INDEX) getSelectCard(crd.get(i).getId());
                        else if (nowIndex == PHONETIC_INDEX)
                            getSelectCard(((PhoneticsCard) crd.get(i)).getSelectTrainsId());
                    }
                    costil.remove(n.getId() + "card");
                }

                @Override
                public void onNoCardsGet() {
                    costil.remove(n.getId() + "card");
                }
            });
            costil.remove(n.getId());
        }

        costil.remove("s" + nowIndex);

 */

        transportSQL.openDbForUpdate();
        final ArrayList<Course> lastupdated = transportSQLCourse.getAllCources();
        final int nowIndex = transportSQLCourse.getDbIndex();
        updater.update(TransportRunnable.ActionTypes.updateIndex, nowIndex);
        costil.put("s" + nowIndex, true);

        low:
        for (final Course n : courses) {
            costil.put(n.getId(), true);
            a = 1;
            for (final Course j : lastupdated) {


                if (j.getId().equals(n.getId())) {
                    if (j.getVersion() < n.getVersion() && j.isEnabled()) {




                        updater.update(TransportRunnable.ActionTypes.sendMessage, "обновление курса '" + j.getId() + "'");


                        costil.put(j.getId() + "card", true);
                        db.getAllCardsFromCourse(nowIndex, j.getId(), new CardsGetter() {
                            @Override
                            public void onAllCardsGet(ArrayList<Card> cards) {
                                for (int i = 0; i < cards.size(); i++){
                                    transportSQL.updateCard(cards.get(i));
                                    if (nowIndex == CULTURE_INDEX) getSelectCard(cards.get(i).getId());
                                    else if (nowIndex == PHONETIC_INDEX) getSelectCard(((PhoneticsCard)cards.get(i)).getSelectTrainsId());

                                }

                                DBTransporter.pushInOrder(cards, j, transportSQL);

                                costil.remove(j.getId() + "card");
                                transportSQLCourse.updateCourse(n);
                                costil.remove(n.getId());
                            }

                            @Override
                            public void onNoCardsGet() {
                                costil.remove(j.getId() + "card");
                                transportSQLCourse.updateCourse(n);
                                costil.remove(n.getId());
                            }
                        });} else{
                        if (isCourseDownLoad && j.getVersion() >= n.getVersion()){
                            updater.update(TransportRunnable.ActionTypes.nowCourseWontBeUpdated);
                        }
                        costil.remove(n.getId());
                    }


                    continue low;
                }
            }

            costil.remove(n.getId());
            n.setVersion(0);
            transportSQLCourse.AddCourse(n);
        }
        costil.remove("s" + nowIndex);
    }

    public static void pushInOrder(ArrayList<Card> cards, Course n, SqlMain transportSQL){
        Log.i("main", cards.size() + " - size");

        //if (!n.isEnabled()) return;

        ArrayList<Card> orderred = new ArrayList<>(cards.size());
        try {
            for (Card card : cards) {
                int mine = Integer.parseInt(card.getId().substring(n.getId().length()));
                int max = 999;
                int max_id = -1;
                for (int i = 0; i < orderred.size(); i++) {
                    int this_id = Integer.parseInt(orderred.get(i).getId().substring(n.getId().length()));
                    if (this_id < max && this_id > mine) {
                        max = this_id;
                        max_id = i;
                    }
                }
                if (max_id == -1)
                    orderred.add(card);
                else
                    orderred.add(max_id, card);

            }

            for (Card card : orderred) {
                    transportSQL.pushInProgress(card);
            }
        } catch (Exception e){

            try {
                // возьмем ситуацию, где айдишники карт отличаются от айдишника курса, но они все равно одинаковы по структуре

                Pattern p = Pattern.compile("\\d+");
                Matcher matcher = p.matcher(cards.get(0).getId());
                int startIndex = -1;
                if (matcher.find(0)) {
                    startIndex = matcher.start();
                }

                // не нашло чисел или есть только 1 элемент, или айдишники элементов не совпадают
                if (startIndex == -1 || !cards.get(0).getId().substring(0, startIndex).equals(cards.get(1).getId().substring(0, startIndex)))
                    throw new Exception();

                for (Card card : cards) {
                    int mine = Integer.parseInt(card.getId().substring(startIndex));
                    int max = 999;
                    int max_id = -1;
                    for (int i = 0; i < orderred.size(); i++) {
                        int this_id = Integer.parseInt(orderred.get(i).getId().substring(startIndex));
                        if (this_id < max && this_id > mine) {
                            max = this_id;
                            max_id = i;
                        }
                    }
                    if (max_id == -1)
                        orderred.add(card);
                    else
                        orderred.add(max_id, card);

                }

                for (Card card : orderred) {
                        transportSQL.pushInProgress(card);
                }
            } catch (Exception e2){

                for (Card card : cards) {
                    transportSQL.pushInProgress(card);
                }
            }
        }
    }

    public class Updater extends AsyncTask<Void, TransportRunnable.ActionTypes, Void>{
        private String message;
        private int nowIndex = 0;

        @Override
        protected Void doInBackground(Void... unused) {
            if (isCourseDownLoad)
                getCourse();
            else
            getAllCourses();
            return(null);
        }

        @Override
        protected void onProgressUpdate(TransportRunnable.ActionTypes... items) {

            if (isCourseDownLoad && items[0] != TransportRunnable.ActionTypes.nowCourseWontBeUpdated)
                return;

            switch (items[0]){
                case error:
                    view.onSomethingGoesWrong(R.string.cources_get_failed);
                    break;
                case checkOverWrite:
                    view.onUserChangesWillOverWrite();
                case sendMessage:
                    view.getMessage(message);
                case updateIndex:
                case nowCourseWontBeUpdated:
                    view.onCourseStatChange(nowIndex);
            }

        }
        @Override
        protected void onPostExecute(Void unused) {
            Log.i("main", "execute");
            //transportSQLCourse.closeDb();
            //transportSQL.closeDatabases();
            view.getMessage("завершено");
            view.onSuccess();
        }

        void update(TransportRunnable.ActionTypes type){
            publishProgress(type);
        }
        void update(TransportRunnable.ActionTypes type, String message){
            this.message = message;
            publishProgress(type);
        }
        void update(TransportRunnable.ActionTypes type, int index){
            this.nowIndex = index;
            publishProgress(type);
        }
        void update(){
            this.message = "загружено " + loaded_count++ + " файлов";
            publishProgress(TransportRunnable.ActionTypes.sendMessage);
        }
    }

    @Override
    public Context getContext() {
        return view.getContext();
    }

    public interface CardsGetter{
        void onAllCardsGet(ArrayList<Card> cards);
        void onNoCardsGet();
    }
    public interface SentenceGetter{
        void onAllSentenceGet(ArrayList<VocabularyCard.Sentence> sentences);
        void onNoSentenceGet();
    }
    public interface SelectGetter{
        void onAllSentenceGet(ArrayList<GrammarCard.SelectTrain> sentences);
        void onNoSentenceGet();
    }
    public interface EndGetter{
        void onEnd();
    }

}
