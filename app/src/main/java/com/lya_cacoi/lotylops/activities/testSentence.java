package com.lya_cacoi.lotylops.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.lya_cacoi.lotylops.Databases.FirebaseAccess.pojo.Course;
import com.lya_cacoi.lotylops.Databases.transportSQL.SqlGrammar;
import com.lya_cacoi.lotylops.Databases.transportSQL.SqlMain;
import com.lya_cacoi.lotylops.Databases.transportSQL.TransportSQLCourse;
import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.SenteceCheck.Decoder;
import com.lya_cacoi.lotylops.SenteceCheck.Part;
import com.lya_cacoi.lotylops.SenteceCheck.SentenceChecker;
import com.lya_cacoi.lotylops.SenteceCheck.Tester;
import com.lya_cacoi.lotylops.activities.utilities.ActivitiesUtils;
import com.lya_cacoi.lotylops.activities.utilities.Test;
import com.lya_cacoi.lotylops.activities.utilities.TrasportActivities;
import com.lya_cacoi.lotylops.activities.utilities.panel.ErrorPanel;
import com.lya_cacoi.lotylops.activities.utilities.panel.InfoHoldable;
import com.lya_cacoi.lotylops.adapters.AddLinkedAdapter;
import com.lya_cacoi.lotylops.cards.Card;
import com.lya_cacoi.lotylops.cards.GrammarCard;
import com.lya_cacoi.lotylops.cards.VocabularyCard;

import java.util.ArrayList;

import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.GRAMMAR_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.VOCABULARY_INDEX;
import static com.lya_cacoi.lotylops.SenteceCheck.SentenceChecker.isDecodable;

public class testSentence extends Test {
    private boolean isRight;
    private Card.Sentence sentence;
    private Part decodedSentence;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_train_sentence, container, false);

    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {

            TextView word = this.view.findViewById(R.id.word);
                if (card.getTrains() == null || (card.getTrains().size() == 0)) {
                    Toast.makeText(getContext(), "у слова '" + ((VocabularyCard) card).getWord() + "' нет предложений для тренировки", Toast.LENGTH_SHORT).show();
                    Log.i("main", "у слова '" + ((VocabularyCard) card).getWord() + "' нет предложений для тренировки");
                    skipTest(null);
                    return;
                }
                if (index == VOCABULARY_INDEX)
                    sentence = card.getTrains().get(0);
                else {
                    GrammarCard gCard = (GrammarCard) card;
                    sentence = card.getTrains().get(card.getPracticeLevel() - 1
                            - (gCard.getSelectTrainsId() == null ? 0 : gCard.getSelectTrainsId().size())
                            - (gCard.getBlockTrainsId() == null ? 0 : gCard.getBlockTrainsId().size()));
                }

            if (isDecodable(sentence.getSentence())) {
                isDecoded = true;
                decodedSentence = Decoder.decoder(sentence.getSentence());
                if (decodedSentence == null) {
                    Log.i("main", sentence.getSentence() + " cannot be decoded");
                    Toast.makeText(getContext(), "card " + card.getId() + ", sentence " + sentence.getId() + " something went wrong - sentence cannot be used", Toast.LENGTH_SHORT).show();
                    skipTest(null);
                    return;
                } else{
                    Tester.reset();
                }
            } else
                isDecoded = false;
            String writeSentence = sentence.getTranslate();

            word.setText(writeSentence);

            view.findViewById(R.id.check).setOnClickListener(this::answerClick);
            TextView editText = this.view.findViewById(R.id.editText);
            if (mainPlain.sizeRatio < mainPlain.triggerRatio){
                editText.setTextSize(mainPlain.sizeHeight/(25f*multiple));
                word.setTextSize(mainPlain.sizeHeight/(30f*multiple));
            }
        }
        catch (Error e){
            ActivitiesUtils.createErrorWindow(mainPlain.activity, () -> goNext(null), card.getId(),
                    getResources().getString(R.string.unknown_error));
        }


    }


    public static String deleteOtherChar(String string){
        return string.replaceAll("[!\n?.\"]", ",").replaceAll("`", "'").replace(" ", "");
    }
    public static String deleteOtherCharThis(String string){
        return string.replaceAll("[!\n?.\"]", ",").replaceAll("`", "'");
    }

    public static String deleteAllNoLetters(String string){
        return deleteOtherCharThis(string).replaceAll(" ", "");
    }

    boolean isDecoded = false;
    private void answerClick(View view) {


        EditText editText = this.view.findViewById(R.id.editText);
        String rightString;
        String describe;

        editText.setEnabled(false);


        if (isDecoded) {
            isRight = SentenceChecker.check(deleteOtherCharThis(editText.getText().toString()), decodedSentence);
            rightString = SentenceChecker.getRightString();
            describe = SentenceChecker.showCommand();
        }
        else{

            String getText = deleteOtherCharThis(editText.getText().toString().trim().toLowerCase());
            String answer;
            answer = sentence.getSentence();
            //ArrayList<String> answers = new ArrayList<>();


            while (answer.contains("|")){
                String thisString = answer.substring(0, answer.indexOf("|"));
                answer = answer.substring(answer.indexOf("|") + 1);
                //answers.add(thisString);
                if (deleteOtherCharThis(thisString.trim().toLowerCase()).equals(getText)){
                    isRight = true;
                }
            }
           // answers.add(answer.trim().toLowerCase());
            Log.i("main", answer);
            if (deleteOtherCharThis(answer.trim().toLowerCase()).equals(getText)){
                isRight = true;
            }
            rightString = answer;
            describe = "";
        }

        if (isRight){
            editText.setTextColor(getResources().getColor(R.color.colorWhiteRight));
            mainPlain.repeatTTS.speak(editText.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
        }
        else{
            mainPlain.repeatTTS.speak(rightString, TextToSpeech.QUEUE_FLUSH, null);
            if (isDecoded) {
                // todo скорее всего это что-то крутое, что надо будет оставить
                //if (SentenceChecker.isGrammarMistakes()) informAboutGrammarMistake(my_custom_view);
            }
            ErrorPanel.openPanel((InfoHoldable) requireActivity(), editText.getText().toString(), rightString,
                    describe, (ErrorPanel.DoAfter) () -> goNext(null));


    }

        this.view.findViewById(R.id.check).setEnabled(false);




        TrasportActivities.putWrongcardsBack(mainPlain.activity, (isDecoded && !SentenceChecker.isTargetMistake()) || isRight,  practiceState, card, index);
        if (isRight)
            TrasportActivities.goNextCardActivity(this, true, practiceState,card, index);

        // если строка была декодирована и ошибка не в таргете - то засчитать за правильный вариант ответа (но показать ошибки)
        if (isDecoded && !SentenceChecker.isTargetMistake())
            isRight = true;

    }
    public void skipTest(View view){
        TrasportActivities.goNextCardActivity(this, true, practiceState,card, index);
    }


    private ArrayList<Course> moveToCourses;
    private TransportSQLCourse sql;

    private void informAboutGrammarMistake(View view){
        // показывает все ссылочки на грамматические ошибки
        ArrayList<String> problems = SentenceChecker.getAllGrammarMistakes();
        if (problems.size() == 0) return;

        sql = TransportSQLCourse.getInstance(getContext(), GRAMMAR_INDEX);
        sql.openDb();
        ArrayList<Course> availableCourses = sql.getAvailableGrammarCourse();

        moveToCourses = new ArrayList<>();

        for (int i = 0; i < problems.size(); i++){
            for (int j = 0; j < availableCourses.size(); j++){
                if (problems.get(i).equals(availableCourses.get(j).getMistakeId())){
                    if (!availableCourses.get(i).isEnabled()){
                        moveToCourses.add(availableCourses.get(i));
                    }
                    break;
                }
            }
        }

        if (moveToCourses.size() == 0) return;

        RecyclerView recyclerView = view.findViewById(R.id.add_block);
        final AddLinkedAdapter adapter = new AddLinkedAdapter(getContext(), moveToCourses);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        adapter.setBlockListener(position -> {
            moveToCourses.get(position).setEnabled(true);
            SqlMain sqlMain = SqlGrammar.getInstance(getContext());
            sqlMain.addCardToProgress(sqlMain.getAllCardsFromCourse(moveToCourses.get(position).getId()));
            sql.updateCourse(moveToCourses.get(position));
            if (index != GRAMMAR_INDEX)
                sqlMain.closeDatabases();
            moveToCourses.remove(position);
            adapter.notifyDataSetChanged();
        });

    }




    @Override
    public void onDestroy() {
        if (sql != null)
            sql.closeDb();
        super.onDestroy();
    }
}

