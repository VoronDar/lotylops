package com.lya_cacoi.lotylops.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.SenteceCheck.SentenceChecker;
import com.lya_cacoi.lotylops.activities.utilities.ActivitiesUtils;
import com.lya_cacoi.lotylops.activities.utilities.Test;
import com.lya_cacoi.lotylops.activities.utilities.TrasportActivities;
import com.lya_cacoi.lotylops.adapters.ButtonLetterAdapter;
import com.lya_cacoi.lotylops.adapters.units.TestUnit;
import com.lya_cacoi.lotylops.adapters.units.buttonLetterUnit;
import com.lya_cacoi.lotylops.cards.Card;
import com.lya_cacoi.lotylops.cards.GrammarCard;
import com.lya_cacoi.lotylops.cards.VocabularyCard;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.GRAMMAR_INDEX;
import static com.lya_cacoi.lotylops.SenteceCheck.SentenceChecker.isDecodable;
import static com.lya_cacoi.lotylops.activities.mainPlain.dp;

public class testBlock extends Test {
    private boolean isRight;
    private Card.Sentence sentence;
    private TextView sentenceText;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_train_block, container, false);
    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {

            if (index == GRAMMAR_INDEX) {

                GrammarCard gCard = (GrammarCard) card;
                if (gCard.getSelectTrainsId() != null)
                    sentence = gCard.getBlockTrains().get(gCard.getPracticeLevel() - gCard.getSelectTrainsId().size() - 1);
                else
                    sentence = gCard.getBlockTrains().get(gCard.getPracticeLevel() - 1);
            } else{
                VocabularyCard vCard = (VocabularyCard) card;
                if (vCard.getTrains() != null && vCard.getTrains().size() > 0)
                    sentence = vCard.getTrains().get(0);

            }

            if (sentence == null){
                throw new Exception();
            }


            TextView word = this.view.findViewById(R.id.word);
            String writeSentence = sentence.getTranslate();
            word.setText(writeSentence);
            String right;
            if (SentenceChecker.isDecodable(sentence.getSentence())) {
                right = SentenceChecker.generateRightStringFromLib(sentence.getSentence());
            }
            else
                right = sentence.getSentence();
            String[] words;
            if (right.charAt(0) != ' ')
                words = right.split(" ");
            else
                words = right.substring(1).split(" ");

            for (int i = 0; i < words.length; i++){
                if (words[i].equals("i")) words[i] = "I";
            }

            if (words.length == 1) throw new Exception();

            final ArrayList<TestUnit> units = new ArrayList<>(word.length());
            final ArrayList<TestUnit> pressed = new ArrayList<>(word.length());
            for (String s: words){
                units.add(new buttonLetterUnit(s));
            }
            Collections.shuffle(units);

            final ButtonLetterAdapter blockAdapter = new ButtonLetterAdapter(getContext(), units);

            sentenceText = view.findViewById(R.id.sentence);
            blockAdapter.setBlockListener(position -> {
                buttonLetterUnit u = (buttonLetterUnit)units.get(position);
                u.setPressed(!u.isPressed());

                if (u.isPressed()){
                    pressed.add(units.get(position));
                } else{
                    pressed.remove(units.get(position));
                }
                blockAdapter.notifyItemChanged(position);

                StringBuilder builder = new StringBuilder();
                for (TestUnit unit : pressed){
                    builder.append(((buttonLetterUnit)unit).getLetter()).append(" ");
                }
                if (builder.length() > 0)
                builder.deleteCharAt(builder.length()-1);
                sentenceText.setText(builder.toString());
            });

            RecyclerView blockRecycler = view.findViewById(R.id.blocks);


            /*
            if (mainPlain.sizeRatio < mainPlain.triggerRatio) {
                blockRecycler.setPadding(mainPlain.sizeWidth / 8, 0, mainPlain.sizeWidth / 8, 0);
                sentenceText.setTextSize(mainPlain.sizeHeight/(30*multiple));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mainPlain.sizeHeight/8);
                params.setMargins(mainPlain.sizeWidth/7, 0, mainPlain.sizeWidth/7, mainPlain.sizeHeight/13);
                sentenceText.setLayoutParams(params);
                sentenceText.setPadding(dp(20),dp(20),dp(20),dp(20));
                word.setTextSize(mainPlain.sizeHeight/(27*multiple));
            }

             */

            blockRecycler.setAdapter(blockAdapter);




            blockRecycler.setLayoutManager(new StaggeredGridLayoutManager((right.length()/25)+1, StaggeredGridLayoutManager.HORIZONTAL));
            blockRecycler.setOverScrollMode(View.OVER_SCROLL_NEVER);


            view.findViewById(R.id.check).setOnClickListener(this::answerClick);
        }
        catch (Error e){
            ActivitiesUtils.createErrorWindow(mainPlain.activity, () -> goNext(null), card.getId(),
                    getResources().getString(R.string.unknown_error));
        }
        catch (Exception e){
            ActivitiesUtils.createErrorWindow(mainPlain.activity, () -> goNext(null), card.getId(),
                    getResources().getString(R.string.unknown_error));
        }


    }


    private String deleteOtherChar(String string){
        return string.replaceAll("[!,\\n?.\"]", "").replaceAll("`", "'");
    }

    private void answerClick(View view) {

        String rightString;
        String describe;

        boolean isDecoded = false;

        if (isDecodable(sentence.getSentence())) {
            isRight = SentenceChecker.check(deleteOtherChar(sentenceText.getText().toString()), sentence.getSentence());
            rightString = SentenceChecker.getRightString();
            describe = SentenceChecker.showCommand();
            isDecoded = true;
        }
        else{

            String getText = deleteOtherChar(sentenceText.getText().toString().trim().toLowerCase());
            String answer;
            answer = card.getTrains().get(0).getSentence();
            while (answer.contains("|")){
                String thisString = answer.substring(0, answer.indexOf("|"));
                answer = answer.substring(answer.indexOf("|") + 1);
                //answers.add(thisString);
                if (deleteOtherChar(thisString.trim().toLowerCase()).equals(getText)){
                    isRight = true;
                }
            }
           // answers.add(answer.trim().toLowerCase());
            Log.i("main", answer);
            if (deleteOtherChar(answer.trim().toLowerCase()).equals(getText)){
                isRight = true;
            }
            rightString = answer;
            describe = "";
        }

        if (isRight){
            sentenceText.setTextColor(getResources().getColor(R.color.colorWhiteRight));
            mainPlain.repeatTTS.speak(sentenceText.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
        }
        else{
            mainPlain.repeatTTS.speak(rightString, TextToSpeech.QUEUE_FLUSH, null);
            AlertDialog.Builder adb = new AlertDialog.Builder(mainPlain.activity);
            View my_custom_view = getLayoutInflater().inflate(R.layout.error_layout, null);
            adb.setView(my_custom_view);

            TextView rightAnswer = my_custom_view.findViewById(R.id.rightAnswer);


            rightAnswer.setText(rightString);
            TextView yourAnswer = my_custom_view.findViewById(R.id.yourAnswer);
            yourAnswer.setText(sentenceText.getText().toString() + "\n"  +  describe);
            adb.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    goNext(null);
                }
            });
            final AlertDialog ad = adb.create();
            my_custom_view.findViewById(R.id.next).setOnClickListener(v -> ad.cancel());

            if (mainPlain.sizeRatio < mainPlain.triggerRatio){
                rightAnswer.setTextSize(mainPlain.sizeHeight/(30f*mainPlain.multiple));
                yourAnswer.setTextSize(mainPlain.sizeHeight/(30f*mainPlain.multiple));
                ((TextView)my_custom_view.findViewById(R.id.errorName)).
                        setTextSize(mainPlain.sizeHeight/(40f*mainPlain.multiple));
                ((Button)my_custom_view.findViewById(R.id.next)).
                        setTextSize(mainPlain.sizeHeight/(35f*mainPlain.multiple));
                ((Button)my_custom_view.findViewById(R.id.next)).
                        setHeight((int)(mainPlain.sizeHeight/(12f)));
                rightAnswer.setTextSize(mainPlain.sizeHeight/(30f*mainPlain.multiple));
            }

            ad.show();
    }

        this.view.findViewById(R.id.check).setEnabled(false);

        TrasportActivities.putWrongcardsBack(mainPlain.activity, isRight,  practiceState, card, index);

        if (isRight)
            TrasportActivities.goNextCardActivity(this, true, practiceState,card, index);

    }
    public void skipTest(View view){
        TrasportActivities.skipPracticeCard(this, card, index);
    }

    public void goNext(View view) {
        TrasportActivities.goNextCardActivity(this, isRight, practiceState, card, index);

    }

}

