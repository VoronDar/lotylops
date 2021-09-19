package com.lya_cacoi.lotylops.Databases.FirebaseAccess;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lya_cacoi.lotylops.Databases.Auth.Command;
import com.lya_cacoi.lotylops.Databases.Auth.FirebaseAuthentification;
import com.lya_cacoi.lotylops.Databases.Auth.User;
import com.lya_cacoi.lotylops.Databases.FirebaseAccess.pojo.Course;
import com.lya_cacoi.lotylops.Databases.Union.DBTransporter;
import com.lya_cacoi.lotylops.Databases.Union.FirebaseTransporter;
import com.lya_cacoi.lotylops.Game.GameData;
import com.lya_cacoi.lotylops.Shop.Shop;
import com.lya_cacoi.lotylops.cards.Card;
import com.lya_cacoi.lotylops.cards.CultureCard;
import com.lya_cacoi.lotylops.cards.ExceptionCard;
import com.lya_cacoi.lotylops.cards.GrammarCard;
import com.lya_cacoi.lotylops.cards.PhoneticsCard;
import com.lya_cacoi.lotylops.cards.VocabularyCard;
import com.lya_cacoi.lotylops.cards.VocabularyCardNet;
import com.lya_cacoi.lotylops.cards.VocabularyCardNetBase;
import com.lya_cacoi.lotylops.cards.VocabularyCardNetUploader;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.CULTURE_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.EXCEPTION_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.GRAMMAR_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.PHONETIC_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.PHRASEOLOGY_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.VOCABULARY_INDEX;
import static com.lya_cacoi.lotylops.activities.NewCardFragment.generateId;


public class FirebaseDatabase {
    private final String COURCES_COLLECTION_NAME = "colodes";
    private final String COURCES_GRAMMAR_COLLECTION_NAME = "grammar_colodes";
    private final static String USERS_COMMANDS = "users_command";
    private final String COURCES_PHRASEOLOGY_COLLECTION_NAME = "phraseology_colodes";
    private final String COURCES_EXCEPTION_COLLECTION_NAME = "exception_colodes";
    private final String COURCES_CULTURE_COLLECTION_NAME = "culture_colodes";
    private final String COURCES_PHONETIC_COLLECTION_NAME = "phonetic_colodes";
    private final String EXAMPLES_COLLECTION_NAME = "examples";
    private final String TRAINS_COLLECTION_NAME = "trains";
    private final String SELECT_TRAINS_COLLECTION_NAME = "selectTrain";
    private final String DB_TAG = "FDatabase";
    private final FirebaseTransporter transporter;
    private final String USERS_CARDS = "user_cards";
    private final String USERS_COLODES = "users";
    private final String GAMES = "games";

    public FirebaseDatabase(FirebaseTransporter transporter) {
        this.transporter = transporter;
    }

    public FirebaseDatabase() {
        transporter = null;
    }


    public void getAllCources(final int index, Context context){
        String collection = transportPreferences.getNowLanguage(context);
        switch (index) {
            case VOCABULARY_INDEX:
                collection = COURCES_COLLECTION_NAME;
                break;
            case PHRASEOLOGY_INDEX:
                collection += COURCES_PHRASEOLOGY_COLLECTION_NAME;
                break;
            case GRAMMAR_INDEX:
                collection = COURCES_GRAMMAR_COLLECTION_NAME;
                break;
            case EXCEPTION_INDEX:
                collection = COURCES_EXCEPTION_COLLECTION_NAME;
                break;
            case CULTURE_INDEX:
                collection = COURCES_CULTURE_COLLECTION_NAME;
                break;
            case PHONETIC_INDEX:
                collection = COURCES_PHONETIC_COLLECTION_NAME;
        }


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(collection)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot snapshot = task.getResult();
                        if (snapshot == null) {
                            Log.d(DB_TAG, "nothing");
                            transporter.onNoCourcesGet();
                            return;
                        }
                        switch (index) {
                            case VOCABULARY_INDEX:
                            case PHRASEOLOGY_INDEX:
                                transporter.onVocPhrAllCourcesGet((ArrayList<Course>) snapshot.toObjects(Course.class));
                                break;
                            case GRAMMAR_INDEX:
                                transporter.onAllGrammarCourcesGet((ArrayList<Course>) snapshot.toObjects(Course.class));
                                break;
                            case EXCEPTION_INDEX:
                            case CULTURE_INDEX:
                            case PHONETIC_INDEX:
                                transporter.onAllSimpleCourseGet((ArrayList<Course>) snapshot.toObjects(Course.class));
                                break;
                        }
                    }
                });

    }


    public void getCourse(final int index, final String id, Context context){
        String collection = transportPreferences.getNowLanguage(context);
        switch (index) {
            case VOCABULARY_INDEX:
                collection = COURCES_COLLECTION_NAME;
                break;
            case PHRASEOLOGY_INDEX:
                collection += COURCES_PHRASEOLOGY_COLLECTION_NAME;
                break;
            case GRAMMAR_INDEX:
                collection = COURCES_GRAMMAR_COLLECTION_NAME;
                break;
            case EXCEPTION_INDEX:
                collection = COURCES_EXCEPTION_COLLECTION_NAME;
                break;
            case CULTURE_INDEX:
                collection = COURCES_CULTURE_COLLECTION_NAME;
                break;
            case PHONETIC_INDEX:
                collection = COURCES_PHONETIC_COLLECTION_NAME;
        }


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(collection).document(id).get().addOnSuccessListener(documentSnapshot -> {
            ArrayList<Course> courses = new ArrayList<>();
            courses.add(documentSnapshot.toObject(Course.class));
            switch (index) {
                case VOCABULARY_INDEX:
                case PHRASEOLOGY_INDEX:
                    transporter.onVocPhrAllCourcesGet(courses);
                    break;
                case GRAMMAR_INDEX:
                    transporter.onAllGrammarCourcesGet(courses);
                    break;
                case EXCEPTION_INDEX:
                case CULTURE_INDEX:
                case PHONETIC_INDEX:
                    transporter.onAllSimpleCourseGet(courses);
                    break;
            }
        });
    }

    public void getAllCardsFromCourse(final int index, final String course, final DBTransporter.CardsGetter getter) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(course)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot snapshot = task.getResult();

                        if (snapshot == null) {
                            Log.d(DB_TAG, "nothing");
                            getter.onNoCardsGet();
                            return;
                        }

                        ArrayList<Card> list = new ArrayList<>();
                        switch (index){
                            case VOCABULARY_INDEX:
                            case PHRASEOLOGY_INDEX: {
                                ArrayList<VocabularyCardNetUploader> fbList = new ArrayList<>(snapshot.toObjects(VocabularyCardNetUploader.class));
                                for (int i = 0; i < fbList.size(); i++) {
                                    list.add(fbList.get(i).transformToClassicCard());
                                }
                            }
                                break;
                            case GRAMMAR_INDEX:
                                list.addAll(snapshot.toObjects(GrammarCard.class));
                                break;
                            case EXCEPTION_INDEX:
                                list.addAll(snapshot.toObjects(ExceptionCard.class));
                                break;
                            case CULTURE_INDEX:
                                list.addAll(snapshot.toObjects(CultureCard.class));
                                break;
                            case PHONETIC_INDEX:
                                list.addAll(snapshot.toObjects(PhoneticsCard.class));
                                break;
                        }
                        for (int i = 0; i < snapshot.getDocuments().size(); i++) {
                            list.get(i).setId(snapshot.getDocuments().get(i).getId());
                            list.get(i).setCourse(course);
                            if (index == VOCABULARY_INDEX || index == PHRASEOLOGY_INDEX){

                                VocabularyCard now = (VocabularyCard) list.get(i);

                                if (now.getTrains() != null && now.getTrains().size() > 0) {
                                    now.getTrains().get(0).setId(now.getId());
                                    now.getTrains().get(0).setLinked_id(now.getId());
                                }
                                if (now.getExamples() != null && now.getExamples().size() > 0) {
                                    now.getExamples().get(0).setId(now.getId());
                                    now.getExamples().get(0).setLinked_id(now.getId());
                                }
                            }
                        }
                        ArrayList<Card> send = new ArrayList<>(list.size());
                        send.addAll(list);
                        getter.onAllCardsGet(send);
                        return;

                    }
                    getter.onNoCardsGet();
                });
    }
    public void getExamples(String id, final DBTransporter.SentenceGetter sentenceGetter) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(EXAMPLES_COLLECTION_NAME).whereEqualTo("linked_id", id)
                .get()
                .addOnCompleteListener(task -> pushSentence(task, sentenceGetter));
    }
    public void getTrains(String id, final DBTransporter.SentenceGetter sentenceGetter) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(TRAINS_COLLECTION_NAME).whereEqualTo("linked_id", id)
                .get()
                .addOnCompleteListener(task -> pushSentence(task, sentenceGetter));
    }
    public void getSelectTrain(final String id, final DBTransporter.SelectGetter sentenceGetter) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection(SELECT_TRAINS_COLLECTION_NAME).document(id);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            ArrayList<GrammarCard.SelectTrain> list = new ArrayList<>();
            list.add( documentSnapshot.toObject(GrammarCard.SelectTrain.class));
            if (documentSnapshot == null || list.size() == 0 || list.get(0) == null) {
                Log.d(DB_TAG, "nothing " + id);
                sentenceGetter.onNoSentenceGet();
                return;
            }
            list.get(0).setId(documentSnapshot.getId());
            sentenceGetter.onAllSentenceGet(list);
        });
    }

    public void getTrainById(final String id, final DBTransporter.SentenceGetter sentenceGetter) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection(TRAINS_COLLECTION_NAME).document(id);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            ArrayList<Card.Sentence> list = new ArrayList<>();
            list.add( documentSnapshot.toObject(Card.Sentence.class));
            if (documentSnapshot == null || list.size() == 0 || list.get(0) == null) {
                Log.d(DB_TAG, "nothing " + id);
                sentenceGetter.onNoSentenceGet();
                return;
            }
            list.get(0).setId(documentSnapshot.getId());
            sentenceGetter.onAllSentenceGet(list);
        });
    }

    private void pushSentence(@NonNull Task<QuerySnapshot> task, final DBTransporter.SentenceGetter sentenceGetter){
        if (task.isSuccessful()) {
            QuerySnapshot snapshot = task.getResult();

            if (snapshot == null) {
                Log.d(DB_TAG, "nothing");
                sentenceGetter.onNoSentenceGet();
                return;
            }

            ArrayList<VocabularyCard.Sentence> list = (ArrayList<VocabularyCard.Sentence>)snapshot.toObjects(VocabularyCard.Sentence.class);

            for (int i = 0; i < snapshot.getDocuments().size(); i++) {
                list.get(i).setId(snapshot.getDocuments().get(i).getId());
            }
            if (list.size() > 0) {
                sentenceGetter.onAllSentenceGet(list);
                return;
            }

        }
        sentenceGetter.onNoSentenceGet();
    }

    public interface CardsLoadable{
        void succesLoad();
        void failureLoad(boolean isThereSameCard);
    }

    public void loadCardToServer(final CardsLoadable loadable, final VocabularyCard card, Context context){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        card.author = FirebaseAuthentification.getId(context);

        db.collection("words").whereEqualTo("word", card.getWord()).whereEqualTo("translate", card.getTranslate())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot snapshot = task.getResult();
                        if (snapshot == null || snapshot.isEmpty()) {
                            db.collection(USERS_CARDS).document(card.getId()).set(new VocabularyCardNet(card)).addOnCompleteListener(task1 -> {
                                Log.d("main", "DocumentSnapshot added");
                                loadable.succesLoad();
                            }) // не обязательный слушатель проблемы передачи
                                    .addOnFailureListener(e -> {
                                        Log.w("main", "Error adding document", e);
                                        loadable.failureLoad(false);
                                    });
                        } else loadable.failureLoad(true);
                    }
                });
    }

    public void pushCardsToServer(final CardsLoadable loadable, final VocabularyCard card, Context context){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(card.getCourse()).document(card.getId()).set(new VocabularyCardNetUploader(card))
                .addOnCompleteListener(task -> Log.i("main", "DocumentSnapshot added"))
                .addOnFailureListener(e -> Log.i("main", "ooh")).addOnCompleteListener(task -> Log.i("main", "canceled"));
    }


    public interface UserCardAddable{
        void failureAdd(String card_id);
        void successAdd(String card_id);
        void accept(List<VocabularyCardNet> vocabularyCardNet, List<String> ids);
    }

    @Deprecated
    public void responceOnUserCard(final UserCardAddable addable, final String card_id, final VocabularyCardNet card, boolean isAdd){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(USERS_CARDS).document(card_id).delete();
        if (isAdd) {

            /*

            отклонено пока. Пока что слова скидываются в папки с курсами, а не в общую коллекцию

            db.collection("words")
                    .add(new VocabularyCardNetBase(card))
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.getResult() != null) {
                                String id = task.getResult().getId();
                                db.collection(EXAMPLES_COLLECTION_NAME).add(new Card.SentenceNet(id, card.getExample(), card.getExampleTranslate()));
                                db.collection(TRAINS_COLLECTION_NAME).add(new Card.SentenceNet(id, card.getTrain(), card.getTrainTranslate()));
                                db.collection(COURCES_COLLECTION_NAME).document(card.getCourse()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.getResult() != null) {
                                            Course thisCourse = task.getResult().toObject(Course.class);
                                            if (thisCourse != null) {
                                                thisCourse.setVersion(thisCourse.getVersion()+1);
                                                db.collection(COURCES_COLLECTION_NAME).document(card.getCourse()).update("version", thisCourse.getVersion());
                                            }
                                        }
                                        addable.successAdd(card_id);
                                    }

                                });
                            } else{
                                Log.e("main", "NOT ADDED");
                                addable.failureAdd(card_id);
                            }
                        }
                    });

             */


            String id = generateId();
            Log.i("main", "id " + generateId());
            db.collection(card.getCourse()).document(id).set(new VocabularyCardNetBase(card));
            if (card.getExample() != null && card.getExample().length() > 2)
                db.collection(EXAMPLES_COLLECTION_NAME).document(generateId()).set(new Card.SentenceNet(id, card.getExample(), card.getExampleTranslate()));
            if (card.getTrain() != null && card.getTrain().length() > 2)
            db.collection(TRAINS_COLLECTION_NAME).document(generateId()).set(new Card.SentenceNet(id, card.getTrain(), card.getTrainTranslate()));
            db.collection(COURCES_COLLECTION_NAME).document(card.getCourse()).get().addOnCompleteListener(task -> {
                if (task.getResult() != null) {
                    Course thisCourse = task.getResult().toObject(Course.class);
                    if (thisCourse != null) {
                        thisCourse.setVersion(thisCourse.getVersion()+1);
                        db.collection(COURCES_COLLECTION_NAME).document(card.getCourse()).update("version", thisCourse.getVersion());
                    }
                }
                addable.successAdd(card_id);
            });
        }
        else{
            addable.successAdd(card_id);
        }
    }


    public void responceOnUserCard(final UserCardAddable addable, final String card_id, final VocabularyCardNetUploader card, boolean isAdd){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();


        Log.i("main", "HERE");
        Log.i("main", card.toString());

        db.collection(USERS_CARDS).document(card_id).delete();
        if (isAdd) {
            //String id = generateId();
            //Log.i("main", "id " + generateId());
            //db.collection(card.getCourse()).document(id).set(card);

            Map<String, Object> map = new HashMap<>();
            map.put("word", card.getWord());
            if (card.getTranslate() != null)
                map.put("translate", card.getTranslate());
            if (card.getMeaning() != null)
                map.put("meaning", card.getMeaning());
            if (card.getMeaningNative() != null)
                map.put("meaningNative", card.getMeaningNative());
            if (card.getPart() != null)
                map.put("part", card.getPart());
            if (card.getGroup() != null)
                map.put("group", card.getGroup());
            if (card.getHelp() != null)
                map.put("help", card.getHelp());
            if (card.getSynonym() != null)
                map.put("synonym", card.getSynonym());
            if (card.getTranscription() != null)
                map.put("transcription", card.getTranscription());
            if (card.getAntonym() != null)
                map.put("antonym", card.getAntonym());
            if (card.getExample() != null)
                map.put("example", card.getExample());
            if (card.getExampleTranslate() != null)
                map.put("exampleTranslate", card.getExampleTranslate());
            if (card.getTrain() != null)
                map.put("train", card.getTrain());
            if (card.getTrainTranslate() != null)
                map.put("trainTranslate", card.getTrainTranslate());


            db.collection(card.getCourse()).document(card_id).set(map);
            db.collection(COURCES_COLLECTION_NAME).document(card.getCourse()).get().addOnCompleteListener(task -> {
                if (task.getResult() != null) {
                    Course thisCourse = task.getResult().toObject(Course.class);
                    if (thisCourse != null) {
                        thisCourse.setVersion(thisCourse.getVersion()+1);
                        db.collection(COURCES_COLLECTION_NAME).document(card.getCourse()).update("version", thisCourse.getVersion());
                    }
                }
                addable.successAdd(card_id);
            });
        }
        else{
            addable.successAdd(card_id);
        }
    }


    public void getUserCards(final UserCardAddable acceptable){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(USERS_CARDS).orderBy("author_rate").limit(15).get().
                addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot snapshot = task.getResult();
                        if (snapshot == null) {
                            Log.d(DB_TAG, "nothing");
                            acceptable.accept(new ArrayList<VocabularyCardNet>(), null);
                            return;
                        }
                        List<VocabularyCardNet> cards = new ArrayList<>(snapshot.toObjects(VocabularyCardNet.class));
                        List<String> ids = new ArrayList<>();
                            for (int i = 0; i < snapshot.getDocuments().size(); i++) {
                                ids.add(snapshot.getDocuments().get(i).getId());
                        }
                        acceptable.accept(cards, ids);
                } else
                        acceptable.accept(new ArrayList<>(), null);
                });
    }


    public void loadImg(final String id, final DBTransporter.EndGetter endGetter) {
        final long ONE_MEGABYTE = 1024 * 1024;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference child = storageRef.child(id + ".jpg");

        ////// ЧТЕНИЕ ИЗОБРАЖЕНИЙ С ОБЛАКА  ////////////////////////////////////////////////
        child.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

            ////// ЗАПИСЬ В ЛОКАЛЬНЫЙ ФАЙЛ  /////////////////////////////////////////////
            File f = new File(transporter.getContext().getCacheDir(), id + ".jpeg");
            try {
                f.createNewFile();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                byte[] bitmapdata = bos.toByteArray();
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ////////////////////////////////////////////////////////////////////////////
            endGetter.onEnd();
        }).addOnFailureListener(exception -> endGetter.onEnd());
        ////////////////////////////////////////////////////////////////////////////////////////
    }



    public void loadSound(final String id, final DBTransporter.EndGetter endGetter) {
        final long ONE_MEGABYTE = 1024 * 1024;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference child = storageRef.child(id + ".mp3");

        ////// ЧТЕНИЕ ИЗОБРАЖЕНИЙ С ОБЛАКА  ////////////////////////////////////////////////
        child.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            ////// ЗАПИСЬ В ЛОКАЛЬНЫЙ ФАЙЛ  /////////////////////////////////////////////
            File f = new File(transporter.getContext().getCacheDir(), id + ".mp3");
            try {
                f.createNewFile();
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(bytes);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ////////////////////////////////////////////////////////////////////////////
            endGetter.onEnd();
        }).addOnFailureListener(exception -> endGetter.onEnd());
        ////////////////////////////////////////////////////////////////////////////////////////
    }


    public void findPersonForGame(final User me, final GameWannable wannable){

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(USERS_COLODES).
                whereGreaterThan("rate", (long)(me.getRate()*0.85))
                .whereLessThan("rate", (long)(me.getRate()*1.15))
                .whereEqualTo("game_active",true)
                .limit(5)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot snapshot = task.getResult();
                        if (snapshot == null || snapshot.isEmpty()) {
                            Log.i("main", "nothing come");
                            return;
                        }

                        List<User> users = snapshot.toObjects(User.class);

                        //for (User user : users){
                        //    db.collection("games").
                        //}

                        //wannable.offer(snapshot.toObjects(User.class));
                    }
                });
    }

    public interface GameWannable{
        void offer(List<User> users);
    }


    public void gameClose(String id) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(GAMES).document(id).delete();
    }

    public void gamerWinOrLose(final String player, final int change, final boolean isWin) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(USERS_COLODES).document(player).get().addOnCompleteListener(task -> {
            User user = task.getResult().toObject(User.class);
            db.collection(USERS_COLODES).document(player).update("rate", user.getRate() + ((isWin)?change:-change));
        });
    }

    public void informPlayer(String player, boolean isWin) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(player).add(new User.Info((isWin)?3:4));
    }

    public void updateGame(GameData gameData) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(GAMES).document(gameData.getId()).set(gameData);
    }


    public GameData getGameData() {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<GameData> data=  db.collection(GAMES).limit(1).get().getResult().toObjects(GameData.class);
        data.get(0).setId("g1");
        return data.get(0);
    }

    public static void checkForMessagesForMe(final Context context){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(USERS_COMMANDS).whereEqualTo("user", FirebaseAuthentification.getId(context)).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                if (snapshot == null || snapshot.isEmpty()) {
                    Log.i("main", "nothing come");
                    return;
                }

                List<Command> commands = snapshot.toObjects(Command.class);

                if (commands.size() != 0){
                    db.collection(USERS_COMMANDS).document(snapshot.getDocuments().get(0).getId()).delete();
                    Command.interpierCommand(commands.get(0).comand, context);
                }

            }
        });
    }

    public void informUserAboutCardAccepting(String user, boolean isAcepted){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(USERS_COMMANDS).document().set(new Command(((isAcepted)?"give " + Shop.MONEY_FOR_LOADING_CARD:"decline"), user));
    }


}