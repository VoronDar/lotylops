package com.lya_cacoi.lotylops.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.activities.utilities.Test;
import com.lya_cacoi.lotylops.activities.utilities.TrasportActivities;
import com.lya_cacoi.lotylops.adapters.ButtonLetterAdapter;
import com.lya_cacoi.lotylops.adapters.units.buttonLetterUnit;
import com.lya_cacoi.lotylops.cards.VocabularyCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static com.lya_cacoi.lotylops.activities.utilities.ActivitiesUtils.getPreparedSoundManager;

public class testSound extends Test {

    private VocabularyCard nowCard;
    private int lettersCount;
    private boolean enabled = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_train_sound, container, false);

    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        nowCard = (VocabularyCard) card;
        final ArrayList<buttonLetterUnit> letters = new ArrayList<>();


        for (int i = 0; i < nowCard.getWord().length(); i++) {
            letters.add(new buttonLetterUnit(nowCard.getWord().toLowerCase().substring(i, i + 1)));
        }
        for (int i = 0; i < (letters.size() + 7) % 7; i++) {
            Random r = new Random();
            String c = String.valueOf((char) (r.nextInt(26) + 'a'));
            letters.add(new buttonLetterUnit(c));
        }
        Collections.shuffle(letters);

        final ButtonLetterAdapter adapter = new ButtonLetterAdapter(getContext(), letters, 0);
        final View v = this.view;
        adapter.setBlockListener(position -> {
            if (!enabled) return;
            TextView word = v.findViewById(R.id.editText);
            if (letters.get(position).isPressed()) {
                String wordString = word.getText().toString();
                wordString = wordString.substring(0, letters.get(position).getWordPosition() - 1) + wordString.substring(letters.get(position).getWordPosition());
                word.setText(wordString);
                letters.get(position).setPressed(false);
                int oldPos = letters.get(position).getWordPosition();
                letters.get(position).setWordPosition(0);
                for (int i = 0; i < letters.size(); i++) {
                    if (letters.get(i).getWordPosition() != 0 && letters.get(i).getWordPosition() > oldPos) {
                        letters.get(i).setWordPosition(letters.get(i).getWordPosition() - 1);
                    }
                }
                lettersCount = word.getText().toString().length();
                adapter.notifyDataSetChanged();
            } else {
                lettersCount = word.getText().toString().length();
                letters.get(position).setPressed(true);
                letters.get(position).setWordPosition(++lettersCount);
                adapter.notifyDataSetChanged();
                word.setText(word.getText() + letters.get(position).getLetter());
            }
        });
        RecyclerView recyclerView = view.findViewById(R.id.blocks);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 7));
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(null);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        adapter.notifyDataSetChanged();

        soundManager = getPreparedSoundManager(mainPlain.activity, card.getId());
        isRealSound = soundManager.isSoundExist();

        ImageView sound = view.findViewById(R.id.soundButton);
        sound.setOnClickListener(this::playWord);

        view.findViewById(R.id.check).setOnClickListener(this::answerClick);

        playWord(null);

    }

    public void answerClick(View view) {
        enabled = false;

        view.setEnabled(false);

        TextView editText = this.view.findViewById(R.id.editText);
        if (editText.getText().toString().equals(nowCard.getWord().toLowerCase())){
            isRight = true;
            editText.setTextColor(getResources().getColor(R.color.colorWhiteRight));
        }
        else{
            playWord(null);
        isRight = false;
            showAlertError(nowCard.getWord(), editText.getText().toString());
    }

        TrasportActivities.putWrongcardsBack(mainPlain.activity, isRight,  practiceState, card, index);

        if (isRight){
            soundManager.closeSound();
            TrasportActivities.goNextCardActivity(this, isRight, practiceState,card, index);
        }

    }
}