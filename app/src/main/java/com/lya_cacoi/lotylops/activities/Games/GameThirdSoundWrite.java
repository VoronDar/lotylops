package com.lya_cacoi.lotylops.activities.Games;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lya_cacoi.lotylops.Databases.Auth.User;
import com.lya_cacoi.lotylops.Databases.transportSQL.SqlVocabulary;
import com.lya_cacoi.lotylops.Game.GameData;
import com.lya_cacoi.lotylops.Game.GameManager;
import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.activities.mainPlain;
import com.lya_cacoi.lotylops.adapters.ButtonLetterAdapter;
import com.lya_cacoi.lotylops.adapters.units.buttonLetterUnit;
import com.lya_cacoi.lotylops.cards.VocabularyCard;

import java.util.ArrayList;

public class GameThirdSoundWrite extends Game {

    private TextView editText;

    private boolean isRight;
    private SqlVocabulary sqlVocabulary;
    private Button accept;
    private Button cancel;

    private VocabularyCard card;

    public GameThirdSoundWrite(GameData gameData, User user) {
        super(gameData, user);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.game_third_sound_, container, false);

    }

    @Override
    public void onViewCreated(final @NonNull View view, @Nullable Bundle savedInstanceState) {
        editText = view.findViewById(R.id.editText);
        setOnMoveListener(editText);


        sqlVocabulary = SqlVocabulary.getInstance(requireContext());
        sqlVocabulary.openDbForLearn();


        do {
            card = (VocabularyCard) sqlVocabulary.getRandomCardFromStatic();
        } while (card.getWord() == null);


        cancel = view.findViewById(R.id.cancel);
        accept = view.findViewById(R.id.accept);

        cancel.setOnClickListener(v -> {
            isRight = false;
            v.setClickable(false);
            accept.setClickable(false);
            checkAnswer();
        });
        accept.setOnClickListener(v -> {
            isRight = (editText.getText().toString().toLowerCase().equals(card.getWord().toLowerCase()));
            v.setClickable(false);
            cancel.setClickable(false);
            checkAnswer();
        });


        setSound(card.getWord());
        playSound(card.getWord());

        super.onViewCreated(view, savedInstanceState);

    }

    protected boolean isRight(){
        return isRight;
    }
    protected void loadData(){


        do {
            card = (VocabularyCard) sqlVocabulary.getRandomCardFromStatic();
        } while (card.getWord() == null);

        setSound("");

        if (isRight) {
            editText.setTextColor(requireContext().getResources().getColor(R.color.colorWhiteRight));
        } else{
            editText.setTextColor(requireContext().getResources().getColor(R.color.colorAccent));
        }

        Animation a = AnimationUtils.loadAnimation(requireContext(), R.anim.become_smaller);


        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                editText.setTextColor(requireContext().getResources().getColor(R.color.colorWhiteExample));
                editText.setText("");
                setSound(card.getWord());
                playSound(card.getWord());
                editText.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.become_bigger));
                cancel.setClickable(true);
                accept.setClickable(true);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        editText.startAnimation(a);

    }
    protected int getTimeLeftMax(){
        return 30;
    }


    @Override
    public GameManager.GameLog getLog() {
        return new GameManager.GameLog(null, card.getWord(), editText.getText().toString(), isRight());
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
    public Fragment getSisterGame() {
        return new GameFifthCheckSynonym(gameData, user);
    }

    protected void setOnMoveListener(View view){
        view.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction()!= KeyEvent.ACTION_DOWN)
                return false;
            if(keyCode == KeyEvent.KEYCODE_ENTER ){
                Log.i("main", "click");
                accept.performClick();
                return false;

            }
            return false;
        });
    }
}
