package com.lya_cacoi.lotylops.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.activities.utilities.Test;
import com.lya_cacoi.lotylops.activities.utilities.TrasportActivities;
import com.lya_cacoi.lotylops.cards.VocabularyCard;

import static com.lya_cacoi.lotylops.activities.utilities.ActivitiesUtils.getPreparedSoundManager;

public class testSoundClear extends Test {

    private VocabularyCard nowCard;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_train_sound_clear, container, false);

    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        nowCard = (VocabularyCard) card;



        soundManager = getPreparedSoundManager(mainPlain.activity, card.getId());
        isRealSound = soundManager.isSoundExist();

        ImageView sound = view.findViewById(R.id.soundButton);
        sound.setOnClickListener(this::playWord);

        view.findViewById(R.id.check).setOnClickListener(this::answerClick);

        playWord(null);

    }

    public void answerClick(View view) {
        view.setEnabled(false);

        TextView editText = this.view.findViewById(R.id.editText);
        editText.setEnabled(false);
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