package com.lya_cacoi.lotylops.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lya_cacoi.lotylops.LevelManager.LevelManager;
import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.activities.utilities.Backable;
import com.lya_cacoi.lotylops.activities.utilities.Blockable;
import com.lya_cacoi.lotylops.activities.utilities.CommonFragment;
import com.lya_cacoi.lotylops.adapters.PracticeTypeAdapter;
import com.lya_cacoi.lotylops.adapters.units.PracticeTestsUnit;
import com.lya_cacoi.lotylops.declaration.consts;
import com.lya_cacoi.lotylops.ruler.Ruler;

import java.util.ArrayList;
import java.util.Objects;

import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.VOCABULARY_INDEX;
import static com.lya_cacoi.lotylops.declaration.consts.P_SENTENCE_INDEX;
import static com.lya_cacoi.lotylops.declaration.consts.P_TRANSLATE_INDEX;
import static com.lya_cacoi.lotylops.declaration.consts.P_TRANSLATE_NATIVE_INDEX;
import static com.lya_cacoi.lotylops.declaration.consts.P_WRITING_INDEX;
import static com.lya_cacoi.lotylops.declaration.consts.SELECT_REPEAT_ALL_COURSE;
import static com.lya_cacoi.lotylops.declaration.consts.SENTENCE_INDEX;
import static com.lya_cacoi.lotylops.declaration.consts.SOUND_CLEAR__INDEX;
import static com.lya_cacoi.lotylops.declaration.consts.SOUND_INDEX;
import static com.lya_cacoi.lotylops.declaration.consts.TRANSLATE_INDEX;
import static com.lya_cacoi.lotylops.declaration.consts.TRANSLATE_NATIVE_INDEX;
import static com.lya_cacoi.lotylops.declaration.consts.WRITING_CLEAR_INDEX_;
import static com.lya_cacoi.lotylops.declaration.consts.WRITING_INDEX;

public class PracticeSelectPoolFragment extends CommonFragment implements Blockable, Backable {
    private Context context;
    private ArrayList<PracticeTestsUnit> units;
    private static boolean setEnable = true;

    // название курса - если таковое вообще имеет значение
    private String courseName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public PracticeSelectPoolFragment() {
    }

    PracticeSelectPoolFragment(String courseName) {
        this.courseName = courseName;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pool_new, container, false);

    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();

        LevelManager.show(requireContext(), requireView().findViewById(R.id.expSlider));
        //if (mainPlain.sizeRatio < mainPlain.triggerRatio)
        //((TextView)view.findViewById(R.id.exerciseType)).setTextSize(mainPlain.sizeHeight/45f);

        units = new ArrayList<>(6);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
            recyclerView.setVisibility(View.VISIBLE);
            boolean isSomethingPractice = false;

            switch (Ruler.getPractiseChapter()) {
                case consts.SELECT_REPEAT_NEXT_DAY:

                    ArrayList<Integer> CardCount = getCardCountNextTest();
                    Log.i("it", "size: " + CardCount.size());
                    for (int i = 0; i <= consts.END_TEST - consts.START_TEST; i++) {
                        if (CardCount.get(i) > 0) {
                            isSomethingPractice = true;
                            units.add(new PracticeTestsUnit(i, CardCount.get(i), getStringVocabularyByIndex(i), false, getImageVocabularyByIndex(i), VOCABULARY_INDEX));
                        }
                    }
                    //CardCount = getCardCountNextTest(PHRASEOLOGY_INDEX);

                    //for (int i = 0; i <= consts.P_END_TEST - consts.START_TEST; i++) {
                    //    if (CardCount.get(i) > 0) {
                    //        isSomethingPractice = true;
                    //        units.add(new PracticeTestsUnit(i, CardCount.get(i), getStringPhraseologyByIndex(i), false, getImagePhraseologyByIndex(i), PHRASEOLOGY_INDEX));
                    //    }
                    //}
                    break;

                case consts.SELECT_REPEAT_ALL_COURSE:{
                    isSomethingPractice = true;
                    int cardCount = getAllCardCount();
                    for (int i = 0; i <= consts.END_TEST - consts.START_TEST; i++) {
                        units.add(new PracticeTestsUnit(i, cardCount, getStringVocabularyByIndex(i), false, getImageVocabularyByIndex(i), VOCABULARY_INDEX));
                    }

                    //cardCount = getAllCardCount(PHRASEOLOGY_INDEX);
                    //for (int i = 0; i <= consts.P_END_TEST - consts.START_TEST; i++) {
                    //    units.add(new PracticeTestsUnit(i, cardCount, getStringPhraseologyByIndex(i), false, getImagePhraseologyByIndex(i), PHRASEOLOGY_INDEX));
                    //}
                    break;
                }
                case consts.SELECT_ALL_TODAY_CARDS:
                    isSomethingPractice = true;
                    int cardCount = getCardCountAllTodayCards();
                    for (int i = 0; i <= consts.END_TEST - consts.START_TEST; i++) {
                        units.add(new PracticeTestsUnit(i, cardCount, getStringVocabularyByIndex(i), false, getImageVocabularyByIndex(i), VOCABULARY_INDEX));
                    }

                    //cardCount = getCardCountAllTodayCards(PHRASEOLOGY_INDEX);
                    //for (int i = 0; i <= consts.P_END_TEST - consts.START_TEST; i++) {
                    //    units.add(new PracticeTestsUnit(i, cardCount, getStringPhraseologyByIndex(i), false, getImagePhraseologyByIndex(i), PHRASEOLOGY_INDEX));
                    //}
                    break;

        }


            recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));

        PracticeTypeAdapter adapter = new PracticeTypeAdapter(units, true);
        adapter.setBlockListener(position -> {
            if (setEnable)
                selectTest(position, units.get(position).getSqlIndex());
        });


        recyclerView.setAdapter(adapter);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        adapter.notifyDataSetChanged();

        boolean wasCreated = true;
        adapter.setClickable(setEnable);

    }

    private void selectTest(int position, int index){
        Ruler ruler = Ruler.getInstance(context, index);
        Ruler.setPractiseTestTypes(units.get(position).getID()+ consts.START_TEST);
        Ruler.setIsPracticeNow(true);
        Fragment fragment;
        switch (Ruler.getPractiseChapter()) {
            case consts.SELECT_ALL_TODAY_CARDS:
            case consts.SELECT_REPEAT_ALL_COURSE:
                fragment = ruler.changeActivityWhilePracticeForTheAllCards(this);
                break;
            case consts.SELECT_REPEAT_NEXT_DAY:
                fragment = ruler.changeActivityWhilePracticeForTheNextTests(this);
                break;
            default:
                fragment = null;
        }
        ruler.closeDatabase();
        mainPlain.activity.slideFragment(Objects.requireNonNull(fragment));
    }


    private ArrayList<Integer> getCardCountNextTest(){

        Ruler ruler = Ruler.getInstance(context, com.lya_cacoi.lotylops.ApproachManager.ApproachManager.VOCABULARY_INDEX);
        return ruler.fillPracticeDBForNextTest();
    }
    private int getCardCountAllTodayCards(){
        ArrayList<Integer> cardCounts = getCardCountNextTest();
        int cardCount = 0;
        for (int i = 0; i < cardCounts.size(); i++)
            cardCount += cardCounts.get(i);
        return cardCount;
    }
    private int getAllCardCount(){
        Ruler ruler = Ruler.getInstance(context, com.lya_cacoi.lotylops.ApproachManager.ApproachManager.VOCABULARY_INDEX);
        //ruler.closeDatabase();
        return ruler.fillPracticeDBForExam(courseName);
    }

    private String getStringVocabularyByIndex(int index){
        switch (index){
            case TRANSLATE_INDEX:
                return context.getResources().getString(R.string.PracticecTranslate);
            case TRANSLATE_NATIVE_INDEX:
                return context.getResources().getString(R.string.PracticecTranslateNative);
            case SOUND_INDEX:
                return context.getResources().getString(R.string.PracticecAudio);
            case SOUND_CLEAR__INDEX:
                return "аудио правописание";
            case WRITING_INDEX:
                return "письмо правописание";
            case WRITING_CLEAR_INDEX_:
                return context.getResources().getString(R.string.PracticecWriting);
            case SENTENCE_INDEX:
                return context.getResources().getString(R.string.PracticecSentence);
            default:
                return null;
        }
    }
    private String getStringPhraseologyByIndex(int index){
        switch (index){
            case P_TRANSLATE_INDEX:
                return context.getResources().getString(R.string.PracticecTranslate);
            case P_TRANSLATE_NATIVE_INDEX:
                return context.getResources().getString(R.string.PracticecTranslateNative);
            case P_WRITING_INDEX:
                return context.getResources().getString(R.string.PracticecWriting);
            case P_SENTENCE_INDEX:
                return context.getResources().getString(R.string.PracticecSentence);
            default:
                return null;
        }
    }
    private int getImageVocabularyByIndex(int index){
        switch (index){
            case TRANSLATE_INDEX:
                return R.drawable.ic_translate_new;
            case TRANSLATE_NATIVE_INDEX:
                return R.drawable.ic_translate_native;
            case SOUND_INDEX:
            case SOUND_CLEAR__INDEX:
                return R.drawable.ic_sound;
            case WRITING_INDEX:
            case WRITING_CLEAR_INDEX_:
                return R.drawable.ic_write;
            case SENTENCE_INDEX:
                return R.drawable.ic_sentence;
            default:
                return 0;
        }
    }
    private int getImagePhraseologyByIndex(int index){
        switch (index){
            case P_TRANSLATE_INDEX:
                return R.drawable.ic_translate_new;
            case P_TRANSLATE_NATIVE_INDEX:
                return R.drawable.ic_translate_native;
            case P_WRITING_INDEX:
                return R.drawable.ic_write;
            case P_SENTENCE_INDEX:
                return R.drawable.ic_sentence;
            default:
                return 0;
        }
    }

    @Override
    public void setEnable(Boolean enable) {
        setEnable = enable;
    }

    @Override
    public String getTitle() {
        return "дополнительная практика";
    }

    @Override
    public void onPushBackListener() {
        if (Ruler.getPractiseChapter() == SELECT_REPEAT_ALL_COURSE)
            ((mainPlain)requireActivity()).slideFragment(new PoolCourseFragment());
        else
            ((mainPlain)requireActivity()).slideFragment(new PoolFragment());
    }
}
