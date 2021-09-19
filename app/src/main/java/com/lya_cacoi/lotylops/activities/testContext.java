package com.lya_cacoi.lotylops.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lya_cacoi.lotylops.ApproachManager.ApproachManager;
import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.activities.utilities.Test;
import com.lya_cacoi.lotylops.activities.utilities.TrasportActivities;
import com.lya_cacoi.lotylops.cards.VocabularyCard;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

import java.util.Random;

import static com.lya_cacoi.lotylops.activities.testSynonyms.amimateError;
import static com.lya_cacoi.lotylops.activities.testSynonyms.animateShouldBePressed;
import static com.lya_cacoi.lotylops.activities.utilities.ActivitiesUtils.getPreparedSoundManager;

public class testContext extends Test {
    private boolean isRight;
    private boolean isTrueAnswer;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_test_context, container, false);

    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        VocabularyCard vCard = (VocabularyCard)card;

        if (vCard.getGroup() != null && vCard.getGroup().length() > 0)
            isTrueAnswer = true;
        else {
            Random random = new Random(46);
            isTrueAnswer = random.nextBoolean();
        }


        if (transportPreferences.getVocabularyApproach(getContext()) ==
                ApproachManager.VOC_AUDIO_APPROACH) {
            view.findViewById(R.id.soundPlay).setVisibility(View.VISIBLE);
            view.findViewById(R.id.soundPlay).setOnClickListener(this::playWord);
        } else{
            view.findViewById(R.id.word).setVisibility(View.VISIBLE);
            ((TextView)view.findViewById(R.id.word)).setText(vCard.getWord());
        }

        view.findViewById(R.id.no).setOnClickListener(v -> answerClick(v, false));
        view.findViewById(R.id.yes).setOnClickListener(v -> answerClick(v, true));

    }


    public void answerClick(View view, boolean isTrue) {
        view.findViewById(R.id.yes).setEnabled(false);
        view.findViewById(R.id.no).setEnabled(false);

        if (isTrue == isTrueAnswer){
            isRight = true;
        }
        else{
        isRight = false;
        if (isTrue) {
            amimateError(view.findViewById(R.id.yes));
            animateShouldBePressed(view.findViewById(R.id.no));
        } else{
            amimateError(view.findViewById(R.id.no));
            animateShouldBePressed(view.findViewById(R.id.yes));
        }

        view.findViewById(R.id.next).setVisibility(View.VISIBLE);
            view.findViewById(R.id.next).setOnClickListener(v -> TrasportActivities.goNextCardActivity(testContext.this, isRight, practiceState,card, index));
    }


        TrasportActivities.putWrongcardsBack(mainPlain.activity, isRight,  practiceState, card, index);

        if (isRight)
            TrasportActivities.goNextCardActivity(this, isRight, practiceState,card, index);

        soundManager = getPreparedSoundManager(mainPlain.activity, card.getId());
        playWord(null);
        if (soundManager != null)
            soundManager.closeSound();

}
}
