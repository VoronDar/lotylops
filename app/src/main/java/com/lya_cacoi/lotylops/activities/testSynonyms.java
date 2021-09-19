package com.lya_cacoi.lotylops.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.lya_cacoi.lotylops.Databases.transportSQL.SqlVocabulary;
import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.activities.utilities.Test;
import com.lya_cacoi.lotylops.activities.utilities.TrasportActivities;
import com.lya_cacoi.lotylops.cards.VocabularyCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class testSynonyms extends Test {
    private boolean isRight;
    private ArrayList<String> answers;
    private Set<Integer> rights;
    private Set<Integer> selected;

    private TextView firstAnswer;
    private TextView secondAnswer;
    private TextView thirdAnswer;
    private TextView fourthAnswer;
    private TextView fifthAnswer;
    private TextView sixAnswer;

    private CardView firstCard;
    private CardView secondCard;
    private CardView thirdCard;
    private CardView fourthCard;
    private CardView fifthCard;
    private CardView sixCard;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_test_synonyms, null);
        return inflater.inflate(R.layout.activity_test_synonyms, container, false);

    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        SqlVocabulary sqlVocabulary = SqlVocabulary.getInstance(getContext());
        sqlVocabulary.openDbForLearn();

        answers = new ArrayList<>();
        rights = new HashSet<>();
        selected = new HashSet<>();

        VocabularyCard vCard = (VocabularyCard)card;

        if (vCard.getSynonym() != null && vCard.getSynonym().length() > 0){
            Collections.addAll(answers, vCard.getSynonym().split(", "));
        }

        // counter - переменная для учета того, что в принципе подходящих слов нет
        int counter = 20;

        for (int i = answers.size(); i < 6; i++){
            VocabularyCard crd;
            do {
                crd =  (VocabularyCard) sqlVocabulary.getRandomCardFromStatic();
            } while (crd.getSynonym() == null && --counter>0);

            if (counter <= 0){
                Log.e("main", "ШО ДЕЛАТЬ НЕТ ПОДХОДЯЩИХ ТАСКОВ");
                throw new RuntimeException();
            }
        }

        Collections.shuffle(answers);

        for (int i = 0; i < answers.size(); i++){
            if (vCard.getSynonym() == null || vCard.getSynonym().length() == 0)
                break;
            String[] sy = vCard.getSynonym().split(", ");
            for (String s: sy){
                if (answers.get(i).equals(s)){
                    rights.add(i);
                }
            }
        }


        ////// SET VIEWS ///////////////////////////////////////////////////////////////////////////
        firstAnswer = view.findViewById(R.id.first_syn_text);
        secondAnswer = view.findViewById(R.id.second_syn_text);
        thirdAnswer = view.findViewById(R.id.third_syn_text);
        fourthAnswer = view.findViewById(R.id.fourth_syn_text);
        fifthAnswer = view.findViewById(R.id.fifth_syn_text);
        sixAnswer = view.findViewById(R.id.six_syn_text);

        firstCard = view.findViewById(R.id.first_syn_card);
        secondCard = view.findViewById(R.id.second_syn_card);
        thirdCard = view.findViewById(R.id.third_syn_card);
        fourthCard = view.findViewById(R.id.fourth_syn_card);
        fifthCard = view.findViewById(R.id.fifth_syn_card);
        sixCard = view.findViewById(R.id.six_syn_card);

        firstAnswer.setText(answers.get(0));
        secondAnswer.setText(answers.get(1));
        thirdAnswer.setText(answers.get(2));
        fourthAnswer.setText(answers.get(3));
        fifthAnswer.setText(answers.get(4));
        sixAnswer.setText(answers.get(5));


        firstCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelect(v, 0);
            }
        });
        secondCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelect(v, 1);
            }
        });
        thirdCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelect(v, 2);
            }
        });
        fourthCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelect(v, 3);
            }
        });
        fifthCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelect(v, 4);
            }
        });
        sixCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelect(v, 5);
            }
        });

        view.findViewById(R.id.check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answerClick(v);
            }
        });


        ////////////////////////////////////////////////////////////////////////////////////////////

    }


    private void onSelect(View view, int index){
        if (selected.contains(index)) {
            selected.remove(index);
            animate(view, 0, 100);
        }
        else {
            selected.add(index);
            animate(view, 100, 0);
        }
    }

    static void animate(View view, float from, float to){
    }

    static void amimateError(View view){

    }
    static void animateShouldBePressed(View view){

    }


    public void answerClick(View view) {

        if (selected.equals(rights)){
            isRight = true;
        }
        else {
            isRight = false;

            ///// SET VIEWS ////////////////////////////////////////////////////////////////////////
            if (rights.contains(0) && !selected.contains(0))
                animateShouldBePressed(firstAnswer);
            if (rights.contains(1) && !selected.contains(1))
                animateShouldBePressed(secondAnswer);
            if (rights.contains(2) && !selected.contains(2))
                animateShouldBePressed(thirdAnswer);
            if (rights.contains(3) && !selected.contains(3))
                animateShouldBePressed(fourthAnswer);
            if (rights.contains(4) && !selected.contains(4))
                animateShouldBePressed(fifthAnswer);
            if (rights.contains(5) && !selected.contains(5))
                animateShouldBePressed(sixAnswer);


            if (!rights.contains(0) && selected.contains(0))
                amimateError(firstAnswer);
            if (!rights.contains(1) && selected.contains(1))
                amimateError(secondAnswer);
            if (!rights.contains(2) && selected.contains(2))
                amimateError(thirdAnswer);
            if (!rights.contains(3) && selected.contains(3))
                amimateError(fourthAnswer);
            if (!rights.contains(4) && selected.contains(4))
                amimateError(fifthAnswer);
            if (!rights.contains(5) && selected.contains(5))
                amimateError(sixAnswer);
            ////////////////////////////////////////////////////////////////////////////////////////

            ((Button)view.findViewById(R.id.check)).setText(getString(R.string.next));
            view.findViewById(R.id.check).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TrasportActivities.goNextCardActivity(testSynonyms.this, isRight, practiceState,card, index);
                }
            });
        }

        TrasportActivities.putWrongcardsBack(mainPlain.activity, isRight,  practiceState, card, index);

        if (isRight)
            TrasportActivities.goNextCardActivity(this, isRight, practiceState,card, index);
    }
}
