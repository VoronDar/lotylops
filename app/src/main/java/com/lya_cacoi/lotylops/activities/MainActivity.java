package com.lya_cacoi.lotylops.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lya_cacoi.lotylops.ApproachManager.ApproachManager;
import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.activities.utilities.ActivitiesUtils;
import com.lya_cacoi.lotylops.activities.utilities.CardActivity;
import com.lya_cacoi.lotylops.activities.utilities.LinkedLearning;
import com.lya_cacoi.lotylops.activities.utilities.SoundManager;
import com.lya_cacoi.lotylops.adapters.units.TextBlock;
import com.lya_cacoi.lotylops.cards.VocabularyCard;
import com.lya_cacoi.lotylops.declaration.consts;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

import java.util.ArrayList;

import static android.view.View.GONE;
import static com.lya_cacoi.lotylops.activities.mainPlain.dp;
import static com.lya_cacoi.lotylops.activities.mainPlain.repeatTTS;
import static com.lya_cacoi.lotylops.activities.utilities.ActivitiesUtils.getExampleColor;
import static com.lya_cacoi.lotylops.activities.utilities.ActivitiesUtils.getMeaningColor;
import static com.lya_cacoi.lotylops.activities.utilities.ActivitiesUtils.getPreparedSoundManager;
import static com.lya_cacoi.lotylops.activities.utilities.ActivitiesUtils.getTranslateColor;
import static com.lya_cacoi.lotylops.activities.utilities.ActivitiesUtils.setGroup;
import static com.lya_cacoi.lotylops.activities.utilities.ActivitiesUtils.setPart;


public class MainActivity extends CardActivity {

    private SoundManager soundManager;
    private boolean isRealSound;

    @SuppressLint({"WrongViewCast", "SetTextI18n"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textBlocksList = new ArrayList<>();

        view.findViewById(R.id.memButton).setOnClickListener(v1 -> addMem());
        View closeThing = view.findViewById(R.id.closetThing);
        closeThing.setVisibility(View.VISIBLE);
        closeThing.setOnClickListener(v -> {
            deleteCard(v);
            deleteCard(requireView().findViewById(R.id.cardSupport));
        });
        requireView().findViewById(R.id.cardSupport).setOnClickListener(v -> closeThing.performClick());
        try {
            TextView word = this.view.findViewById(R.id.word);
            ImageView sound = this.view.findViewById(R.id.sound);
            TextView transcription = this.view.findViewById(R.id.transcription);

            VocabularyCard card = (VocabularyCard) abstractCard;


            if (transportPreferences.getVocabularyApproach(context) == ApproachManager.VOC_AUDIO_APPROACH) {
                word.setVisibility(GONE);
                sound.setVisibility(View.VISIBLE);
                soundButton.setVisibility(GONE);
                transcription.setVisibility(GONE);
            } else {
                word.setText(card.getWord().substring(0, 1).toUpperCase() + card.getWord().substring(1).toLowerCase());
                if (card.getTranscription() != null) {
                    transcription.setText(card.getTranscription());
                }
            }


            boolean isMeaning = (transportPreferences.getMeaningPriority(context) > consts.PRIORITY_NEVER);
            boolean isTranslate = ((transportPreferences.getTranslatePriority(context) >= consts.PRIORITY_NEVER_TEST) && card.getTranslate() != null);

            // add priorityMeaning
            boolean isInCardMeaning = false;
            if (isMeaning) {
                    textBlocksList.add(new TextBlock(0, null, 0, card.getId()));
                    if (card.getMeaning() != null && transportPreferences.getMeaningLanguage(context) >= consts.LANGUAGE_NEW) {

                        textBlocksList.add(new TextBlock(dp(18), card.getMeaningNative(), getMeaningColor(this.view)));
                        isInCardMeaning = true;
                    }
                    if (card.getMeaningNative() != null && (transportPreferences.getMeaningLanguage(context) == consts.LANGUAGE_BOTH ||
                            transportPreferences.getMeaningLanguage(context) == consts.LANGUAGE_NATIVE)) {
                        setItem(R.id.meaningMative, card.getMeaningNative(), R.id.mean_divider);
                        isInCardMeaning = true;
                    }
                    if (!isInCardMeaning && card.getMeaningNative() != null){
                     textBlocksList.add(new TextBlock(dp(18), card.getMeaningNative(), getMeaningColor(this.view)));
                        isInCardMeaning = true;
                    }
                    else if (!isInCardMeaning && card.getMeaning() != null){
                        setItem(R.id.meaningMative, card.getMeaningNative(), R.id.mean_divider);
                       isInCardMeaning = true;
                    }
            }


            if (isTranslate || !isInCardMeaning) {
                if (card.getTranslate() != null) {
                    setItem(R.id.translate, card.getTranslate(), R.id.mean_divider);
                    if (card.getMeaning() != null && transportPreferences.getMeaningLanguage(context) != consts.LANGUAGE_NEW)
                        setItem(R.id.meaning, card.getMeaning(), R.id.mean_divider);
                }
                else if (card.getMeaning() != null || card.getMeaningNative() != null) {
                    if (card.getMeaning() != null && transportPreferences.getMeaningLanguage(context) >= consts.LANGUAGE_NEW) {
                        setItem(R.id.meaning, card.getMeaning(), R.id.mean_divider);
                    }
                    else if (card.getMeaningNative() != null && (transportPreferences.getMeaningLanguage(context) == consts.LANGUAGE_BOTH ||
                            transportPreferences.getMeaningLanguage(context) == consts.LANGUAGE_NATIVE))
                        setItem(R.id.meaningMative, card.getMeaningNative(), R.id.mean_divider);
                    else {
                        setItem(R.id.meaning, card.getMeaning(), R.id.mean_divider);
                        setItem(R.id.meaningMative, card.getMeaningNative(), R.id.mean_divider);
                    }
                }
                else {
                    ActivitiesUtils.createErrorWindow(mainPlain.activity, () -> onRemember(null), abstractCard.getId(), getResources().getString(R.string.no_translate_meaning));
                }
            }


            addMemInCard();


            if (transportPreferences.getVocabularyApproach(context) == ApproachManager.VOC_AUDIO_APPROACH) {
                textBlocksList.add(new TextBlock(dp(23), card.getTranscription(), getExampleColor(this.view), true));
                textBlocksList.add(new TextBlock(dp(23), card.getWord(), getTranslateColor(this.view)));
            }

            if (card.getHelp() != null && card.getHelp().length() > 0) {
                String help = card.getHelp().replace("\\n", System.getProperty("line.separator") + " ");
                setItem(R.id.help, help, R.id.help_divider);
            }

            if (transportPreferences.getExampleAvailability(context) == consts.AVAILABILITY_ON) {
                if (card.getExamples() != null && card.getExamples().size() > 0) {
                    if (card.getExamples().get(0) != null && transportPreferences.getExampleLanguage(context) >= consts.LANGUAGE_NEW) {
                        setItem(R.id.example, card.getExamples().get(0).getSentence(), 0);
                    }
                    if (card.getExamples().get(0) != null && ((transportPreferences.getExampleLanguage(context) == consts.LANGUAGE_NATIVE ||
                            transportPreferences.getExampleLanguage(context) == consts.LANGUAGE_BOTH)))
                        setItem(R.id.exampleTranslate, card.getExamples().get(0).getTranslate(), 0);
                }
            }


            if (card.getSynonym() != null && transportPreferences.getSynomymAvailability(context) == consts.AVAILABILITY_ON){
                setItem(R.id.synonyms, "Синонимы: " + card.getSynonym(), R.id.syn_divider);
            }
            if (card.getAntonym() != null && transportPreferences.getAntonymAvailability(context) == consts.AVAILABILITY_ON){
                setItem(R.id.antonyms, "Антонимы: " + card.getSynonym(), R.id.syn_divider);
            }

            setGroup(this.view, card.getGroup());
            setPart(this.view, card.getPart());



            view.findViewById(R.id.playSound).setOnClickListener(v -> playSound());
            view.findViewById(R.id.sound).setOnClickListener(v -> playSound());

            /*
            if (mainPlain.sizeRatio < mainPlain.triggerRatio){
                word.setTextSize(mainPlain.sizeHeight/(15f*mainPlain.multiple));
                transcription.setTextSize(mainPlain.sizeHeight/(25f*mainPlain.multiple));
                ((TextView)view.findViewById(R.id.part)).setTextSize(mainPlain.sizeHeight/(35f*mainPlain.multiple));
                ((TextView)view.findViewById(R.id.group)).setTextSize(mainPlain.sizeHeight/(40f*mainPlain.multiple));
            }

             */

        }
        catch(Error e){
            ActivitiesUtils.createErrorWindow(mainPlain.activity, () -> onRemember(null), abstractCard.getId(),
                    getResources().getString(R.string.unknown_error));
        }

        soundManager = getPreparedSoundManager(mainPlain.activity, abstractCard.getId());
        isRealSound = soundManager.isSoundExist();
        playSound();

        new LinkedLearning(view, ((VocabularyCard) abstractCard).getLinked(), context, sqlIndex);



    }

    private void playSound(){
        if (isRealSound)
            soundManager.playSound();
        else
            repeatTTS.speak(((VocabularyCard)abstractCard).getWord(), TextToSpeech.QUEUE_FLUSH,null);
    }

    @Override
    public void onDestroy() {
        if (isRealSound)
            soundManager.closeSound();
        super.onDestroy();
    }


}
