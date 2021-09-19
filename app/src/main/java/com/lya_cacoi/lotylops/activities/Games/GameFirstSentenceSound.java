package com.lya_cacoi.lotylops.activities.Games;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.lya_cacoi.lotylops.Databases.Auth.User;
import com.lya_cacoi.lotylops.Databases.transportSQL.SqlVocabulary;
import com.lya_cacoi.lotylops.Game.GameData;
import com.lya_cacoi.lotylops.Game.GameManager;
import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.SenteceCheck.SentenceChecker;
import com.lya_cacoi.lotylops.activities.mainPlain;
import com.lya_cacoi.lotylops.adapters.ButtonLetterAdapter;
import com.lya_cacoi.lotylops.adapters.units.TestUnit;
import com.lya_cacoi.lotylops.adapters.units.buttonLetterUnit;

import java.util.ArrayList;
import java.util.Collections;

import static com.lya_cacoi.lotylops.activities.testSentence.deleteAllNoLetters;

public class GameFirstSentenceSound extends Game {

    private TextView sentenceText;

    private boolean isRight;
    private SqlVocabulary sqlVocabulary;
    private Button accept;
    private Button cancel;

    private String sentence;

    private String right;

    private RecyclerView blockRecycler;

    public GameFirstSentenceSound(GameData gameData, User user) {
        super(gameData, user);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.game_first_sound_sentence, container, false);

    }

    @Override
    public void onViewCreated(final @NonNull View view, @Nullable Bundle savedInstanceState) {

        mainPlain.repeatTTS.setSpeechRate(1.5f);


        sentenceText = view.findViewById(R.id.editText);


        blockRecycler = requireView().findViewById(R.id.blocks);

        sqlVocabulary = SqlVocabulary.getInstance(requireContext());
        sqlVocabulary.openDbForLearn();


        do {
            sentence = sqlVocabulary.getRandomTrain();
        } while (sentence == null);


        setData();

        cancel = view.findViewById(R.id.cancel);
        accept = view.findViewById(R.id.accept);

        cancel.setOnClickListener(v -> {
            isRight = false;
            v.setClickable(false);
            accept.setClickable(false);
            checkAnswer();
        });
        accept.setOnClickListener(v -> {
            isRight = (deleteAllNoLetters(sentenceText.getText().toString().toLowerCase()).equals(deleteAllNoLetters(right.toLowerCase())));
            v.setClickable(false);
            cancel.setClickable(false);
            checkAnswer();
        });


        setSound(right);
        playSound(right);

        super.onViewCreated(view, savedInstanceState);

    }

    protected boolean isRight(){
        return isRight;
    }
    protected void loadData(){


        do {
            sentence = sqlVocabulary.getRandomTrain();
        } while (sentence == null);

        setSound("");

        if (isRight) {
            sentenceText.setTextColor(requireContext().getResources().getColor(R.color.colorWhiteRight));
        } else{
            sentenceText.setTextColor(requireContext().getResources().getColor(R.color.colorAccent));
        }

        Animation a = AnimationUtils.loadAnimation(requireContext(), R.anim.become_smaller);



        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                sentenceText.setTextColor(requireContext().getResources().getColor(R.color.colorWhiteExample));
                sentenceText.setText("");
                setData();
                playSound(right);
                setSound(right);
                sentenceText.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.become_bigger));
                cancel.setClickable(true);
                accept.setClickable(true);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        sentenceText.startAnimation(a);
        blockRecycler.startAnimation(a);

    }
    protected int getTimeLeftMax(){
        return 20;
    }


    @Override
    public GameManager.GameLog getLog() {
        return new GameManager.GameLog(null, right, sentenceText.getText().toString(), isRight);
    }

    private void setSound(final String sound){
        requireView().findViewById(R.id.soundButton).setOnClickListener(v -> mainPlain.repeatTTS.speak(sound, TextToSpeech.QUEUE_FLUSH,
                null));
    }

    private void playSound(String sound){
        mainPlain.repeatTTS.speak(sound, TextToSpeech.QUEUE_FLUSH,
                null);
    }

    @Override
    public void onDestroy() {
        mainPlain.repeatTTS.setSpeechRate(1f);
        super.onDestroy();
    }

    private void setData(){
        if (SentenceChecker.isDecodable(sentence))
            right = SentenceChecker.generateRightStringFromLib(sentence);
        else
            right = sentence;

        String[] words = right.split(" ");
        final ArrayList<TestUnit> units = new ArrayList<>();
        final ArrayList<TestUnit> pressed = new ArrayList<>();
        for (String s: words){
            units.add(new buttonLetterUnit(s));
        }
        Collections.shuffle(units);

        final ButtonLetterAdapter blockAdapter = new ButtonLetterAdapter(requireContext(), units);
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

        /*

        if (mainPlain.sizeRatio < mainPlain.triggerRatio) {
            blockRecycler.setPadding(mainPlain.sizeWidth / 8, 0, mainPlain.sizeWidth / 8, 0);
            sentenceText.setTextSize(mainPlain.sizeHeight/(30*multiple));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mainPlain.sizeHeight/8);
            params.setMargins(mainPlain.sizeWidth/7, 0, mainPlain.sizeWidth/7, mainPlain.sizeHeight/13);
            sentenceText.setLayoutParams(params);
            sentenceText.setPadding(dp(20),dp(20),dp(20),dp(20));
        }
        
         */

        blockRecycler.setAdapter(blockAdapter);
        blockRecycler.setLayoutManager(new StaggeredGridLayoutManager((right.length()/20)+1, StaggeredGridLayoutManager.HORIZONTAL));
        blockRecycler.setOverScrollMode(View.OVER_SCROLL_NEVER);

    }
}
