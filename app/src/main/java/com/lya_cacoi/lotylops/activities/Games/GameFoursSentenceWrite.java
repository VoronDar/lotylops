package com.lya_cacoi.lotylops.activities.Games;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lya_cacoi.lotylops.Databases.Auth.User;
import com.lya_cacoi.lotylops.Databases.transportSQL.SqlVocabulary;
import com.lya_cacoi.lotylops.Game.GameData;
import com.lya_cacoi.lotylops.Game.GameManager;
import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.SenteceCheck.Decoder;
import com.lya_cacoi.lotylops.SenteceCheck.SentenceChecker;
import com.lya_cacoi.lotylops.activities.mainPlain;

import static com.lya_cacoi.lotylops.SenteceCheck.SentenceChecker.isDecodable;
import static com.lya_cacoi.lotylops.activities.testSentence.deleteOtherChar;

public class GameFoursSentenceWrite extends Game {

    private TextView editText;

    private boolean isRight;
    private SqlVocabulary sqlVocabulary;
    private Button accept;
    private Button cancel;

    private String sentence;

    public GameFoursSentenceWrite(GameData gameData, User user) {
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

        mainPlain.repeatTTS.setSpeechRate(1.5f);


        editText = view.findViewById(R.id.editText);


        sqlVocabulary = SqlVocabulary.getInstance(requireContext());
        sqlVocabulary.openDbForLearn();


        ((TextView)view.findViewById(R.id.command)).setText("запиши предложение правильно");


        do {
            sentence = sqlVocabulary.getRandomTrain();
            if (isDecodable(sentence))
                sentence = SentenceChecker.generateRightStringFromLib(sentence);
        } while (sentence == null || sentence.length() == 0);

        cancel = view.findViewById(R.id.cancel);
        accept = view.findViewById(R.id.accept);

        cancel.setOnClickListener(v -> {
            isRight = false;
            v.setClickable(false);
            accept.setClickable(false);
            checkAnswer();
        });
        accept.setOnClickListener(v -> {
            isRight = (deleteOtherChar(editText.getText().toString().toLowerCase()).equals(deleteOtherChar(sentence.toLowerCase())));

            Log.i("main", editText.getText() + " -> " + sentence);
            Log.i("main", deleteOtherChar(editText.getText().toString().toLowerCase()+ " -> " + deleteOtherChar(sentence.toLowerCase())));

            v.setClickable(false);
            cancel.setClickable(false);
            checkAnswer();
        });


        setSound(sentence);
        playSound(sentence);

        super.onViewCreated(view, savedInstanceState);

    }

    protected boolean isRight(){
        return isRight;
    }
    protected void loadData(){
        do {
            sentence = sqlVocabulary.getRandomTrain();
            if (isDecodable(sentence))
                sentence = SentenceChecker.generateRightStringFromLib(sentence);
        } while (sentence == null || sentence.length() == 0);

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
                setSound(sentence);
                playSound(sentence);
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
        return new GameManager.GameLog(null ,sentence , editText.getText().toString(), isRight);
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

}
