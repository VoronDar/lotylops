package com.lya_cacoi.lotylops.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lya_cacoi.lotylops.ApproachManager.ApproachManager;
import com.lya_cacoi.lotylops.Databases.FirebaseAccess.pojo.Course;
import com.lya_cacoi.lotylops.Databases.transportSQL.SqlMain;
import com.lya_cacoi.lotylops.Databases.transportSQL.SqlVocaPhrasUnion;
import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.SenteceCheck.SentenceChecker;
import com.lya_cacoi.lotylops.activities.utilities.Backable;
import com.lya_cacoi.lotylops.activities.utilities.CardChangable;
import com.lya_cacoi.lotylops.activities.utilities.CommonFragment;
import com.lya_cacoi.lotylops.activities.utilities.FragmentsServer;
import com.lya_cacoi.lotylops.activities.utilities.panel.InfoHoldable;
import com.lya_cacoi.lotylops.activities.utilities.panel.SimpleInfoPannel;
import com.lya_cacoi.lotylops.adapters.NewCardElementAdapter;
import com.lya_cacoi.lotylops.adapters.units.newCardElementUnit;
import com.lya_cacoi.lotylops.cards.VocabularyCard;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

import java.util.ArrayList;
import java.util.Random;

import static com.lya_cacoi.lotylops.activities.LibraryFragment.index;

public class NewCardFragment extends CommonFragment implements Backable, CardChangable {


    private final Course course;
    private NewCardElementAdapter adapter;
    private Context context;
    private final boolean isChange;
    private String cardId;
    private ArrayList<newCardElementUnit> units;
    private VocabularyCard initCard;
    private SqlMain sqlCard;
    public VocabularyCard card;
    public enum ElIds{
        word,
        transl,
        transcript,
        meaning,
        meaningNative,
        example,
        exampleNative,
        train,
        trainNative,
        mem,
        group,
        part,
        help,
        synonym,
        antonym
    }
    private boolean isRight = true;

    public NewCardFragment(boolean isChange, String cardId, Course course){
        this.isChange = isChange;
        this.cardId = cardId;
        this.course = course;
    }
    public NewCardFragment(Course course){
        this.isChange = false;
        this.course = course;
    }


        @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_card, container, false);
    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();

        final Button accept = view.findViewById(R.id.buttonAccept);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        units = new ArrayList<>();
        sqlCard = SqlMain.getInstance(context, index);

        if (isChange) {
            FragmentsServer.setTitle(getString(R.string.change_card));
            initCard = (VocabularyCard) sqlCard.getCard(cardId);


            units.add(new newCardElementUnit(getString(R.string.word), initCard.getWord(), true, ElIds.word));
            units.add(new newCardElementUnit(getString(R.string.translate), initCard.getTranslate(), false, ElIds.transl));
            units.add(new newCardElementUnit(getString(R.string.meaningLearn), initCard.getMeaning(), false, ElIds.meaning));
            units.add(new newCardElementUnit(getString(R.string.meaningNative), initCard.getMeaningNative(), false, ElIds.meaningNative));


            Log.i("main", (initCard.getExamples() != null) + " - is Examples");
            Log.i("main", (initCard.getTrains() != null) + " - is Trains");
            Log.i("main", "id " + initCard.getId());

            if (initCard.getExamples() != null){
                units.add(new newCardElementUnit(getString(R.string.exampleLearn), initCard.getExamples().get(0).getSentence(), false, ElIds.example));
            units.add(new newCardElementUnit(getString(R.string.exampleNative), initCard.getExamples().get(0).getTranslate(), false, ElIds.exampleNative));
        } else{
                units.add(new newCardElementUnit(getString(R.string.exampleLearn), null, false, ElIds.example));
                units.add(new newCardElementUnit(getString(R.string.exampleNative), null, false, ElIds.exampleNative));

            }


        if (initCard.getTrains() != null) {
            VocabularyCard.Sentence train = initCard.getTrains().get(0);
            int variantNum = 0;
            if (SentenceChecker.isDecodable(train.getSentence())) {
                units.add(new newCardElementUnit(getString(R.string.exampleTrainLearn),
                        SentenceChecker.generateRightStringFromLib(train.getSentence()), true, ElIds.train, false));
            } else {
                while (train.getSentence().contains("|")) {
                    if (variantNum == 0)
                        units.add(new newCardElementUnit(getString(R.string.exampleTrainLearn),
                                train.getSentence().substring(0, train.getSentence().indexOf("|")), true, ElIds.train));
                    //else
                    //units.add(new newCardElementUnit(getString(R.string.exampleTrainLearn) +
                    //     " (" + getString(R.string.variant) + ")",
                    //    train.substring(0, train.indexOf("|")), false, ElIds.train));
                    //train = train.substring(train.indexOf("|") + 1);
                    variantNum++;
                    break;

                }
                if (variantNum == 0)
                    units.add(new newCardElementUnit(getString(R.string.exampleTrainLearn), train.getSentence(), true, ElIds.train));
            }
            //else
            //    units.add(new newCardElementUnit(getString(R.string.exampleTrainLearn) +
            //            " (" + getString(R.string.variant) + ")", train, false, ElIds.train));
            //adapter.setLastSentenceIndex(variantNum + index);


            units.add(new newCardElementUnit(getString(R.string.exampleTrainNative), train.getTranslate(), true, ElIds.trainNative,
                    units.get(units.size()-1).isEditable()));
        } else{

            units.add(new newCardElementUnit(getString(R.string.exampleTrainLearn), null, true, ElIds.train));
            units.add(new newCardElementUnit(getString(R.string.exampleTrainNative), null, true, ElIds.trainNative));


        }


        if (initCard.getPart() == null || initCard.getPart().length() < 2)
            units.add(new newCardElementUnit(getString(R.string.part), "существительное", false, ElIds.part));
        else
            units.add(new newCardElementUnit(getString(R.string.part), initCard.getPart(), false, ElIds.part));
        units.add(new newCardElementUnit(getString(R.string.group), initCard.getGroup(), false, ElIds.group));
        units.add(new newCardElementUnit(getString(R.string.synonym), initCard.getSynonym(), false, ElIds.synonym));
        units.add(new newCardElementUnit(getString(R.string.antonym), initCard.getAntonym(), false, ElIds.antonym));
        units.add(new newCardElementUnit(getString(R.string.help), initCard.getHelp(), false, ElIds.help));
        units.add(new newCardElementUnit(getString(R.string.transcription), initCard.getTranscription(), false, ElIds.transcript));
        units.add(new newCardElementUnit(getString(R.string.mem), initCard.getMem(), false, ElIds.mem));
        for (newCardElementUnit n : units) {
            n.setValue(n.getPrevValue());
        }
    }
        else{
            FragmentsServer.setTitle(getString(R.string.add_new_card));
            units.add(new newCardElementUnit(getString(R.string.word), getString(R.string.wordPrev), true, ElIds.word));
            units.add(new newCardElementUnit(getString(R.string.translate), getString(R.string.translatePrev), false, ElIds.transl));
            units.add(new newCardElementUnit(getString(R.string.meaningLearn), getString(R.string.meaningLearnPrev), false, ElIds.meaning));
            units.add(new newCardElementUnit(getString(R.string.meaningNative), getString(R.string.meaningNativePrev), false, ElIds.meaningNative));
            units.add(new newCardElementUnit(getString(R.string.exampleLearn), getString(R.string.exampleLearnPrev), false, ElIds.example));
            units.add(new newCardElementUnit(getString(R.string.exampleNative), getString(R.string.exampleNativePrev), false, ElIds.exampleNative));
            units.add(new newCardElementUnit(getString(R.string.exampleTrainLearn), getString(R.string.exampleTrainLearnPrev), true, ElIds.train));
            units.add(new newCardElementUnit(getString(R.string.exampleTrainNative), getString(R.string.exampleTrainNativePrev), true, ElIds.trainNative));
            units.add(new newCardElementUnit(getString(R.string.part), getString(R.string.partPrev), false, ElIds.part));
            units.add(new newCardElementUnit(getString(R.string.group), getString(R.string.groupPrev), false, ElIds.group));
            units.add(new newCardElementUnit(getString(R.string.synonym), getString(R.string.synonymPrev), false, ElIds.synonym));
            units.add(new newCardElementUnit(getString(R.string.antonym), getString(R.string.antonymPrev), false, ElIds.antonym));
            units.add(new newCardElementUnit(getString(R.string.help), getString(R.string.helpPrev), false, ElIds.help));
            units.add(new newCardElementUnit(getString(R.string.transcription), getString(R.string.transcriptionPrev), false, ElIds.transcript));
            units.add(new newCardElementUnit(getString(R.string.mem), getString(R.string.memPrev), false, ElIds.mem));
        }

        adapter = new NewCardElementAdapter(isChange, view.getContext(), units, this);
        adapter.setHasStableIds(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(adapter.getItemViewType(0), 0);
        adapter.notifyDataSetChanged();
        final int AvailableId = transportPreferences.getAvailableId(context, ApproachManager.VOCABULARY_INDEX);
        if (!isChange)
            card = new VocabularyCard("M" + AvailableId, course.getId(), true);
        else {
            card = new VocabularyCard(initCard.getId(), initCard.getCourse());
            card.isMine = ((SqlVocaPhrasUnion)sqlCard).isMine(card.getId());
            card.setExamples(initCard.getExamples());
            card.setTrains(initCard.getTrains());
        }
        accept.setOnClickListener(v -> {
            setToCard(card);
            if (isRight){
                if (isChange) {
                    sqlCard.updateCard(card);
                    ((SqlVocaPhrasUnion)sqlCard).updateChanges(card);
                    sqlCard.updateTrains(card.getTrains());
                    ((SqlVocaPhrasUnion)sqlCard).updateExamples(card.getExamples());
                    //if (course.isEnabled()) sqlCard.pushInProgress(card);
                } else{
                    sqlCard.addNewCard(card);
                    if (course.isEnabled())
                        sqlCard.pushInProgress(card);
                    ((SqlVocaPhrasUnion)sqlCard).updateChanges(card);
                    sqlCard.updateTrains(card.getTrains());
                    ((SqlVocaPhrasUnion)sqlCard).updateExamples(card.getExamples());
                }
                transportPreferences.setAvailableId(context, index, AvailableId+1);
                onPushBackListener();

            } else if (isSomething){
                Log.i("main", exc.getName());
                createWarningWindow(exc.getName());
            }
    });
        view.findViewById(R.id.buttonCancel).setOnClickListener(v -> onPushBackListener());
    }

    private void createWarningWindow(String message){
        SimpleInfoPannel.openPanel((InfoHoldable) getActivity(), "Ошибка при редактировании", getString(R.string.you_ought_to_fill) + "'" + message + "'", -1);
    }

    // возвращает false, если поле, которое должно было быть запонено - не заполнено
    private boolean isRightField(newCardElementUnit n){
        return (!(n.isRequired()) || (n.getValue() != null && n.getValue().length() > 1));
    }

    private newCardElementUnit exc;
    private boolean isSomething = false;
    public void setToCard(VocabularyCard card) {
        isRight = true;
        isSomething = false;
        for (int i = 0; i < units.size(); i++) {
            setToCard(card, i);
        }

        if (!isSomething) {
            createWarningWindow(getString(R.string.translate) + "'" + getString(R.string.or)
                    + "'" + getString(R.string.meaning) + "'" + getString(R.string.or) + "'" +
                    getString(R.string.meaningNative));
            isRight = false;
        }
    }

    public void setToCard(VocabularyCard card, int pos) {
        newCardElementUnit n = units.get(pos);
            if (n.getId() == ElIds.word) {
                card.setWord(n.getValue());
            } else if (n.getId() == ElIds.train && n.getValue() != null) {
                //if (card.getTrain() == null) {
                //    card.setTrain(n.getValue());
                //}
                //else
                if (card.getTrains() == null || card.getTrains().size() == 0) {
                    card.setTrains(new ArrayList<>(1));
                    card.getTrains().add(new VocabularyCard.Sentence(n.getValue(), null, true, generateId(), card.getId()));
                } else {
                    card.getTrains().get(0).setSentence(n.getValue());
                }
            }
            //else if (n.getId() == ElIds.train && isWriteLearn)
            //    continue;
            else if (n.getId() == ElIds.trainNative && n.getValue() != null) {
                if (card.getTrains() == null || card.getTrains().size() == 0) {
                    card.setTrains(new ArrayList<>(1));
                    card.getTrains().add(new VocabularyCard.Sentence(null, n.getValue(), true, generateId(), card.getId()));
                } else {
                    card.getTrains().get(0).setTranslate(n.getValue());
                }
            } else if (n.getId() == ElIds.transl) {
                    isSomething = true;
                    card.setTranslate(n.getValue());
            } else if (n.getId() == ElIds.meaning) {
                    isSomething = true;
                    card.setMeaning(n.getValue());
            } else if (n.getId() == ElIds.meaningNative) {
                    isSomething = true;
                    card.setMeaningNative(n.getValue());
            } else if (n.getId() == ElIds.example) {
                if (card.getExamples() == null || card.getExamples().size() == 0) {
                    card.setExamples(new ArrayList<>(1));
                    card.getExamples().add(new VocabularyCard.Sentence(n.getValue(), null, true, generateId(), card.getId()));
                } else
                    card.getExamples().get(0).setSentence(n.getValue());
            } else if (n.getId() == ElIds.exampleNative) {
                if (card.getExamples() == null || card.getExamples().size() == 0) {
                    card.setExamples(new ArrayList<>(1));
                    card.getExamples().add(new VocabularyCard.Sentence(null, n.getValue(), true, generateId(), card.getId()));
                } else
                    card.getExamples().get(0).setTranslate(n.getValue());
            } else if (n.getId() == ElIds.mem) {
            } else if (n.getId() == ElIds.part) {
                card.setPart(n.getValue());
            } else if (n.getId() == ElIds.group) {
                card.setGroup(n.getValue());
            } else if (n.getId() == ElIds.transcript) {
                card.setTranscription(n.getValue());
            } else if (n.getId() == ElIds.help) {
                card.setHelp(n.getValue());
            } else if (n.getId() == ElIds.synonym) {
                card.setSynonym(n.getValue());
            } else if (n.getId() == ElIds.antonym) {
                card.setAntonym(n.getValue());
            }
            units.get(pos).setValue(n.getValue());
            try {
                adapter.notifyDataSetChanged();
            } catch (IllegalStateException ignored){
            }

            if (!isRightField(n)) {
                isRight = false;
                exc = n;
            }
        }

    public static String generateId() {
            StringBuilder builder = new StringBuilder();

            String sequence = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm1234567890";

            int rand;
            Random r = new Random();
            int count = r.nextInt(2) + 7;
            for (int i = 0; i < count; i++) {
                rand = r.nextInt(sequence.length());
                builder.append(sequence.charAt(rand));
            }
            return builder.toString();
    }

    @Override
    public void onPushBackListener() {
            if (mainPlain.sizeRatio < mainPlain.triggerRatio){
                ((mainPlain) requireActivity()).slideFragment(new SelectSectionFragment(index, course));
            } else
                ((mainPlain) requireActivity()).slideFragment(new LibraryFragment(course));
        }

    @Override
    public VocabularyCard getCard() {
        return card;
    }

    @Override
    public String getTitle() {
        return "новая карта";
    }
}

