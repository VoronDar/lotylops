package com.lya_cacoi.lotylops.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class testWriting extends Test {
    private int lettersCount = 0;
    private String wordString;
    private boolean enabled = true;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_train_writing, container, false);

    }

    @SuppressLint({"WrongViewCast", "SetTextI18n"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setMeaningAndTranslate();

        final ArrayList<buttonLetterUnit> letters = new ArrayList<>();


        wordString = ((VocabularyCard)card).getWord();

        for (int i = 0; i < wordString.length(); i++){
            letters.add(new buttonLetterUnit(wordString.toLowerCase().substring(i, i+1)));
        }
        for (int i = 0; i < (letters.size()+7)%7; i++){
            Random r = new Random();
            String c = String.valueOf((char)(r.nextInt(26) + 'a'));
            letters.add(new buttonLetterUnit(c));
        }
        Collections.shuffle(letters);

        final ButtonLetterAdapter adapter = new ButtonLetterAdapter(getContext(), letters, 0);
        final View v = this.view;
        adapter.setBlockListener(position -> {
            if (!enabled) return;
            TextView word = v.findViewById(R.id.editText);
            if (letters.get(position).isPressed()){
                String wordString = word.getText().toString();
                wordString = wordString.substring(0, letters.get(position).getWordPosition()-1) + wordString.substring(letters.get(position).getWordPosition());
                word.setText(wordString);
                letters.get(position).setPressed(false);
                int oldPos =  letters.get(position).getWordPosition();
                letters.get(position).setWordPosition(0);
                for (int i = 0; i < letters.size(); i++){
                    if (letters.get(i).getWordPosition() != 0 && letters.get(i).getWordPosition() > oldPos){
                        letters.get(i).setWordPosition(letters.get(i).getWordPosition()-1);
                    }
                }
                lettersCount = word.getText().toString().length();
                adapter.notifyDataSetChanged();
            }
            else{
                lettersCount = word.getText().toString().length();
                letters.get(position).setPressed(true);
                letters.get(position).setWordPosition(++lettersCount);
                adapter.notifyDataSetChanged();
                word.setText(word.getText() + letters.get(position).getLetter());
            }
        });
        RecyclerView recyclerView = view.findViewById(R.id.letterBlocks);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 7));
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(null);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        adapter.notifyDataSetChanged();

        view.findViewById(R.id.check).setOnClickListener(this::answerClick);

        /*
        TextView editText = this.view.findViewById(R.id.editText);
        if (mainPlain.sizeRatio < mainPlain.triggerRatio){
            editText.setTextSize(mainPlain.sizeHeight/(20*multiple));
        }


        if (mainPlain.sizeRatio < mainPlain.triggerRatio)
            view.findViewById(R.id.meaning).setPadding(mainPlain.sizeWidth/7, 0,
                    mainPlain.sizeWidth/7, 0);

         */
        
    }


    public void answerClick(View view) {
        view.setEnabled(false);

        enabled = false;
        TextView editText = this.view.findViewById(R.id.editText);
        boolean isRight;
        if (editText.getText().toString().equals(wordString.toLowerCase())){
            isRight = true;
            editText.setTextColor(getResources().getColor(R.color.colorWhiteRight));
        }
        else{
        isRight = false;
            showAlertError(((VocabularyCard)card).getWord(), editText.getText().toString());
    }


        TrasportActivities.putWrongcardsBack(mainPlain.activity, isRight,  practiceState, card, index);

        if (isRight)
            TrasportActivities.goNextCardActivity(this, true, practiceState,card, index);

        soundManager = getPreparedSoundManager(mainPlain.activity, card.getId());
        playWord(null);
        if (soundManager != null)
            soundManager.closeSound();

}
}
