package com.lya_cacoi.lotylops.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lya_cacoi.lotylops.ApproachManager.ApproachManager;
import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.activities.utilities.Blockable;
import com.lya_cacoi.lotylops.activities.utilities.CommonFragment;
import com.lya_cacoi.lotylops.activities.utilities.FragmentsServer;
import com.lya_cacoi.lotylops.adapters.BaseSettingsAdapter;
import com.lya_cacoi.lotylops.adapters.SettingsAdapter;
import com.lya_cacoi.lotylops.adapters.SettingsBlockAdapter;
import com.lya_cacoi.lotylops.adapters.units.SettingUnit;
import com.lya_cacoi.lotylops.declaration.consts;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

import java.util.ArrayList;
import java.util.Locale;

import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.CULTURE_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.EXCEPTION_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.FREQUENCY_EXPO;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.GRAMMAR_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.PHONETIC_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.PHRASEOLOGY_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.VOCABULARY_INDEX;

public class SettingsFragment extends CommonFragment implements Blockable {

    public enum SettingsIndex{
        common,                                                                                     // the main page
        VocabularyPace,
        PhraseologyPace,
        VocabularyApproach,                                                                         // audio or read
        VocabularyViewApproach,                                                                     // image-meaning or translate
        VocabularyFrequency,                                                                        // линейная или экспоненциальная скорость
        PhraseologyFrequency,
        GrammarFrequency,
        ExceptionsFrequency,
        CultureFrequency,
        PhoneticsFrequency,
        MeaningLanguage,
        ExampleLanguage,
        NewWordsCount,
        NewWordsCountGrammar,
        NewWordsCountPhraseology,
        NewWordsCountException,
        NewWordsCountPhonetics,
        NewWordsCountCulture,
        GrammarApproach,
        PhoneticsApproach
    }


    private BaseSettingsAdapter adapter;
    private Context context;
    private ArrayList<SettingUnit> units;
    private SettingsIndex settingsIndex = SettingsIndex.common;
    private boolean wasCreated = false;
    private static boolean setEnable = true;

    public SettingsFragment(){
    }
    private SettingsFragment(SettingsIndex index){
        settingsIndex = index;
    }

        @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (settingsIndex == SettingsIndex.common)
            return inflater.inflate(R.layout.fragment_settings, container, false);
        else if (settingsIndex == SettingsIndex.NewWordsCount
                || settingsIndex == SettingsIndex.NewWordsCountGrammar
                || settingsIndex == SettingsIndex.NewWordsCountPhonetics
                || settingsIndex == SettingsIndex.NewWordsCountException
                || settingsIndex == SettingsIndex.NewWordsCountPhraseology
                || settingsIndex == SettingsIndex.NewWordsCountCulture)
            return inflater.inflate(R.layout.new_cards_count, container, false);
        else
            return inflater.inflate(R.layout.select_setting, container, false);

    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();

        boolean isHorisontal = false;                                                               // if select blocks are horisontal and lie on other blocks
        boolean isManySelected = false;                                                             // if the user can select several blocks

        SettingUnit.DoSomehtingInteface doSomehtingIntefaceBoth = null;
        SettingUnit.DoSomehtingInteface doSomehtingIntefaceNone = null;
        String BothDescription = null;
        String NoneDescription = "";

        final Button accept = view.findViewById(R.id.buttonAccept);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        if (recyclerView != null)
            recyclerView.setHasFixedSize(true);
        units = new ArrayList<>();
        if (settingsIndex == SettingsIndex.common) {
            adapter = new SettingsAdapter(view.getContext(), units);
        }
        else{
                adapter = new SettingsBlockAdapter(view.getContext(), units);
        }


        if (settingsIndex == SettingsIndex.common){

            FragmentsServer.setTitle(getString(R.string.fragment_name_settings));
            int imageId;
            if (transportPreferences.getVocabularyApproach(context) == ApproachManager.VOC_AUDIO_APPROACH)
                imageId = R.drawable.ic_icon_vocabular_audio;
            else
                imageId = R.drawable.ic_icon_vocabular_read;

            units.add(new SettingUnit(imageId, getResources().getString(R.string.VocabularyApproach), () -> new SettingsFragment(SettingsIndex.VocabularyApproach)));

            if (transportPreferences.getMeaningPriority(context) == consts.PRIORITY_NEVER)
                imageId = R.drawable.ic_icon_vocabular_view_translate;
            else if (transportPreferences.getTranslatePriority(context) == consts.PRIORITY_NEVER)
                imageId = R.drawable.ic_icon_vocabular_view_meaning;
            else
                imageId = R.drawable.ic_icon_vocabular_view;


            units.add(new SettingUnit(imageId, getResources().getString(R.string.VocabularyViewApproach), () -> new SettingsFragment(SettingsIndex.VocabularyViewApproach)));

            if (transportPreferences.getMeaningLanguage(context) == consts.LANGUAGE_NEW)
                imageId = R.drawable.ic_icon_description_new;
            else if (transportPreferences.getMeaningLanguage(context) == consts.LANGUAGE_NATIVE)
                imageId = R.drawable.ic_icon_description_native;
            else
                imageId = R.drawable.ic_icon_description;

            units.add(new SettingUnit(imageId, getResources().getString(R.string.DefenitionLanguage), () -> new SettingsFragment(SettingsIndex.MeaningLanguage)));


            if (transportPreferences.getExampleAvailability(context) == consts.AVAILABILITY_OFF)
                imageId = R.drawable.ic_icon_description;
            else if (transportPreferences.getExampleLanguage(context) == consts.LANGUAGE_NEW)
                imageId = R.drawable.ic_icon_description_new;
            else if (transportPreferences.getExampleLanguage(context) == consts.LANGUAGE_NATIVE)
                imageId = R.drawable.ic_icon_description_native;
            else
                imageId = R.drawable.ic_icon_description;


            units.add(new SettingUnit(imageId, getResources().getString(R.string.ExampleLanguage), () -> new SettingsFragment(SettingsIndex.ExampleLanguage)));


            if (transportPreferences.getFrequency(context, ApproachManager.VOCABULARY_INDEX) == FREQUENCY_EXPO)
                imageId = R.drawable.ic_icon_velocity;
            else
                imageId = R.drawable.ic_icon_frequency;
            units.add(new SettingUnit(imageId,
                    getResources().getString(R.string.frequency) + " " + getResources().getString(R.string.vocabulary_padeg), () -> new SettingsFragment(SettingsIndex.VocabularyFrequency)));


            units.add(new SettingUnit(R.drawable.ic_icon_velocity, getResources().getString(R.string.VocabularyCardCountPerDay), () -> new SettingsFragment(SettingsIndex.NewWordsCount)));

            if (transportPreferences.getFrequency(context, PHRASEOLOGY_INDEX) == FREQUENCY_EXPO)
                imageId = R.drawable.ic_icon_velocity;
            else
                imageId = R.drawable.ic_icon_frequency;
            units.add(new SettingUnit(imageId,
                    getResources().getString(R.string.frequency) + " " + getResources().getString(R.string.phraseology_padeg), () -> new SettingsFragment(SettingsIndex.PhraseologyFrequency)));

            units.add(new SettingUnit(R.drawable.ic_icon_velocity, getResources().getString(R.string.PhraseologyCardCountPerDay),
                    () -> new SettingsFragment(SettingsIndex.NewWordsCountPhraseology)));


            if (transportPreferences.getGrammarWay(context) == ApproachManager.GRAMMAR_WAY_DECUCTION)
                imageId = R.drawable.ic_grammar_deduction;
            else if (transportPreferences.getGrammarWay(context) == ApproachManager.GRAMMAR_WAY_INDUCTION)
                imageId = R.drawable.ic_grammar_induction;
            else
                imageId = R.drawable.ic_grammar_transduction;

            units.add(new SettingUnit(imageId, getResources().getString(R.string.GrammarApproach), () -> new SettingsFragment(SettingsIndex.GrammarApproach)));

            if (transportPreferences.getFrequency(context, GRAMMAR_INDEX) == FREQUENCY_EXPO)
                imageId = R.drawable.ic_icon_velocity;
            else
                imageId = R.drawable.ic_icon_frequency;
            units.add(new SettingUnit(imageId,
                    getResources().getString(R.string.frequency) + " " + getResources().getString(R.string.grammar_padeg), () -> new SettingsFragment(SettingsIndex.GrammarFrequency)));

            units.add(new SettingUnit(R.drawable.ic_icon_velocity, getResources().getString(R.string.GrammarVelocity), () -> new SettingsFragment(SettingsIndex.NewWordsCountGrammar)));


            if (transportPreferences.getFrequency(context, EXCEPTION_INDEX) == FREQUENCY_EXPO)
                imageId = R.drawable.ic_icon_velocity;
            else
                imageId = R.drawable.ic_icon_frequency;
            units.add(new SettingUnit(imageId,
                    getResources().getString(R.string.frequency) + " " + getResources().getString(R.string.exception_padeg), () -> new SettingsFragment(SettingsIndex.ExceptionsFrequency)));


            units.add(new SettingUnit(R.drawable.ic_icon_velocity, getResources().getString(R.string.ExceptionsCardCountPerDay), () -> new SettingsFragment(SettingsIndex.NewWordsCountException)));

            if (transportPreferences.getPhoneticsApproach(context) == ApproachManager.PHONETICS_APPROACH_DEDUCTION)
                imageId = R.drawable.ic_grammar_deduction;
            else if (transportPreferences.getPhoneticsApproach(context) == ApproachManager.PHONETICS_APPROACH_INDUCTION)
                imageId = R.drawable.ic_grammar_transduction;

            units.add(new SettingUnit(imageId, getResources().getString(R.string.PhoneticsApproach), () -> new SettingsFragment(SettingsIndex.PhoneticsApproach)));



            if (transportPreferences.getFrequency(context, PHONETIC_INDEX) == FREQUENCY_EXPO)
                imageId = R.drawable.ic_icon_velocity;
            else
                imageId = R.drawable.ic_icon_frequency;
            units.add(new SettingUnit(imageId,
                    getResources().getString(R.string.frequency) + " " + getResources().getString(R.string.phonetics_padeg), () -> new SettingsFragment(SettingsIndex.PhoneticsFrequency)));


            units.add(new SettingUnit(R.drawable.ic_icon_velocity, getResources().getString(R.string.PhoneticsCardCountPerDay), () -> new SettingsFragment(SettingsIndex.NewWordsCountPhonetics)));


            if (transportPreferences.getFrequency(context, CULTURE_INDEX) == FREQUENCY_EXPO)
                imageId = R.drawable.ic_icon_velocity;
            else
                imageId = R.drawable.ic_icon_frequency;
            units.add(new SettingUnit(imageId,
                    getResources().getString(R.string.frequency) + " " + getResources().getString(R.string.culture_padeg), () -> new SettingsFragment(SettingsIndex.CultureFrequency)));


            units.add(new SettingUnit(R.drawable.ic_icon_velocity, getResources().getString(R.string.CultureCardCountPerDay), () -> new SettingsFragment(SettingsIndex.NewWordsCountCulture)));
            adapter.setBlockListener(position -> {
                if (adapter.isClickable())
                ((mainPlain) requireActivity()).goToTheSpecialSettings(units.get(position).openAlert());
            });
        }
        else if (settingsIndex == SettingsIndex.NewWordsCount){
            FragmentsServer.setTitle("скорость изучения лексики");
            isHorisontal = true;
            final EditText count = view.findViewById(R.id.editText);
            count.setText(Integer.toString(transportPreferences.getPaceVocabulary(getContext())));
            onSetPaceAction(Integer.parseInt(count.getText().toString()), VOCABULARY_INDEX);
            count.setOnKeyListener((v, keyCode, event) -> {
                Log.i("main", "KLICK!!");
                if (!count.getText().toString().equals(""))
                    onSetPaceAction(Integer.parseInt(count.getText().toString()), VOCABULARY_INDEX);
                else
                    onSetPaceAction(0, VOCABULARY_INDEX);
                return false;
            });
            view.findViewById(R.id.buttonCancel).setOnClickListener(v -> ((mainPlain) requireActivity()).goToTheSpecialSettings(new SettingsFragment(SettingsIndex.common)));
            view.findViewById(R.id.buttonAccept).setOnClickListener(v -> {
              transportPreferences.setPaceVocabulary(getContext(), Integer.parseInt(count.getText().toString()));
                transportPreferences.setCriticalValue(getContext(), ApproachManager.VOCABULARY_INDEX, Integer.parseInt(count.getText().toString())*3);
                if (transportPreferences.getTodayLeftStudyCardQuantity(context, VOCABULARY_INDEX) > Integer.parseInt(count.getText().toString())) {
                    transportPreferences.setTodayLeftStudyCardQuantity(context, VOCABULARY_INDEX, Integer.parseInt(count.getText().toString()));
                    ((mainPlain) requireActivity()).goToTheSpecialSettings(new SettingsFragment(SettingsIndex.common));
                } else
                    setAlertWindow();

            });
        }else if (settingsIndex == SettingsIndex.NewWordsCountPhraseology){
            FragmentsServer.setTitle("скорость изучения фразеологии");
            ((TextView)view.findViewById(R.id.newWords)).setText("новых фраз в день");
            isHorisontal = true;
            final EditText count = view.findViewById(R.id.editText);
            count.setText(Integer.toString(transportPreferences.getPacePhraseology(getContext())));
            onSetPaceAction(Integer.parseInt(count.getText().toString()), PHRASEOLOGY_INDEX);
            count.setOnKeyListener((v, keyCode, event) -> {
                Log.i("main", "KLICK!!");
                if (!count.getText().toString().equals(""))
                    onSetPaceAction(Integer.parseInt(count.getText().toString()), PHRASEOLOGY_INDEX);
                else
                    onSetPaceAction(0, PHRASEOLOGY_INDEX);
                return false;
            });
            view.findViewById(R.id.buttonCancel).setOnClickListener(v -> ((mainPlain) requireActivity()).goToTheSpecialSettings(new SettingsFragment(SettingsIndex.common)));
            view.findViewById(R.id.buttonAccept).setOnClickListener(v -> {
                transportPreferences.setPacePhraseology(getContext(), Integer.parseInt(count.getText().toString()));
                transportPreferences.setCriticalValue(getContext(), PHRASEOLOGY_INDEX, Integer.parseInt(count.getText().toString())*3);
                if (transportPreferences.getTodayLeftStudyCardQuantity(context, PHRASEOLOGY_INDEX) > Integer.parseInt(count.getText().toString())) {
                    transportPreferences.setTodayLeftStudyCardQuantity(context, PHRASEOLOGY_INDEX, Integer.parseInt(count.getText().toString()));
                    ((mainPlain) requireActivity()).goToTheSpecialSettings(new SettingsFragment(SettingsIndex.common));
                } else
                    setAlertWindow();

            });
        }else if (settingsIndex == SettingsIndex.NewWordsCountException){
            FragmentsServer.setTitle("скорость изучения исключений");
            ((TextView)view.findViewById(R.id.newWords)).setText("новых правил в день");
            isHorisontal = true;
            final EditText count = view.findViewById(R.id.editText);
            count.setText(Integer.toString(transportPreferences.getPaceException(getContext())));
            onSetPaceAction(Integer.parseInt(count.getText().toString()), EXCEPTION_INDEX);
            count.setOnKeyListener((v, keyCode, event) -> {
                Log.i("main", "KLICK!!");
                if (!count.getText().toString().equals(""))
                    onSetPaceAction(Integer.parseInt(count.getText().toString()), EXCEPTION_INDEX);
                else
                    onSetPaceAction(0, EXCEPTION_INDEX);
                return false;
            });

            view.findViewById(R.id.buttonCancel).setOnClickListener(v -> ((mainPlain) requireActivity()).goToTheSpecialSettings(new SettingsFragment(SettingsIndex.common)));
            view.findViewById(R.id.buttonAccept).setOnClickListener(v -> {
                transportPreferences.setPaceException(getContext(), Integer.parseInt(count.getText().toString()));
                transportPreferences.setCriticalValue(getContext(), EXCEPTION_INDEX, Integer.parseInt(count.getText().toString())*3);
                if (transportPreferences.getTodayLeftStudyCardQuantity(context, EXCEPTION_INDEX) > Integer.parseInt(count.getText().toString())) {
                    transportPreferences.setTodayLeftStudyCardQuantity(context, EXCEPTION_INDEX, Integer.parseInt(count.getText().toString()));
                    ((mainPlain) requireActivity()).goToTheSpecialSettings(new SettingsFragment(SettingsIndex.common));
                } else
                    setAlertWindow();
            });

        }else if (settingsIndex == SettingsIndex.NewWordsCountCulture){
            FragmentsServer.setTitle("скорость изучения культуры");
            ((TextView)view.findViewById(R.id.newWords)).setText("новых статей в день");
            isHorisontal = true;
            final EditText count = view.findViewById(R.id.editText);
            count.setText(Integer.toString(transportPreferences.getPaceCulture(getContext())));
            onSetPaceAction(Integer.parseInt(count.getText().toString()), CULTURE_INDEX);
            count.setOnKeyListener((v, keyCode, event) -> {
                Log.i("main", "KLICK!!");
                if (!count.getText().toString().equals(""))
                    onSetPaceAction(Integer.parseInt(count.getText().toString()), CULTURE_INDEX);
                else
                    onSetPaceAction(0, CULTURE_INDEX);
                return false;
            });
            view.findViewById(R.id.buttonCancel).setOnClickListener(v -> ((mainPlain) requireActivity()).goToTheSpecialSettings(new SettingsFragment(SettingsIndex.common)));
            view.findViewById(R.id.buttonAccept).setOnClickListener(v -> {
                transportPreferences.setPaceCulture(getContext(), Integer.parseInt(count.getText().toString()));
                transportPreferences.setCriticalValue(getContext(), ApproachManager.CULTURE_INDEX, Integer.parseInt(count.getText().toString())*3);
                if (transportPreferences.getTodayLeftStudyCardQuantity(context, CULTURE_INDEX) > Integer.parseInt(count.getText().toString())) {
                    transportPreferences.setTodayLeftStudyCardQuantity(context, CULTURE_INDEX, Integer.parseInt(count.getText().toString()));
                    ((mainPlain) requireActivity()).goToTheSpecialSettings(new SettingsFragment(SettingsIndex.common));
                } else
                    setAlertWindow();
            });
        }
    else if (settingsIndex == SettingsIndex.NewWordsCountPhonetics){
            FragmentsServer.setTitle("скорость изучения фонетики");
            ((TextView)view.findViewById(R.id.newWords)).setText("новых правил в день");
        isHorisontal = true;
        final EditText count = view.findViewById(R.id.editText);
        count.setText(Integer.toString(transportPreferences.getPacePhonetics(getContext())));
        onSetPaceAction(Integer.parseInt(count.getText().toString()), CULTURE_INDEX);
        count.setOnKeyListener((v, keyCode, event) -> {
            Log.i("main", "KLICK!!");
            if (!count.getText().toString().equals(""))
                onSetPaceAction(Integer.parseInt(count.getText().toString()), CULTURE_INDEX);
            else
                onSetPaceAction(0, CULTURE_INDEX);
            return false;
        });
        view.findViewById(R.id.buttonCancel).setOnClickListener(v -> ((mainPlain) requireActivity()).goToTheSpecialSettings(new SettingsFragment(SettingsIndex.common)));
        view.findViewById(R.id.buttonAccept).setOnClickListener(v -> {
            transportPreferences.setPacePhonetics(getContext(), Integer.parseInt(count.getText().toString()));
            transportPreferences.setCriticalValue(getContext(), ApproachManager.PHONETIC_INDEX, Integer.parseInt(count.getText().toString())*3);
            if (transportPreferences.getTodayLeftStudyCardQuantity(context, PHONETIC_INDEX) > Integer.parseInt(count.getText().toString())) {
                transportPreferences.setTodayLeftStudyCardQuantity(context, PHONETIC_INDEX, Integer.parseInt(count.getText().toString()));
                ((mainPlain) requireActivity()).goToTheSpecialSettings(new SettingsFragment(SettingsIndex.common));
            } else
                setAlertWindow();
        });
    }
        else if (settingsIndex == SettingsIndex.NewWordsCountGrammar){
            FragmentsServer.setTitle("скорость изучения правил");
            ((TextView)view.findViewById(R.id.newWords)).setText("новых правил в день");
            isHorisontal = true;
            final EditText count = view.findViewById(R.id.editText);
            count.setText(Integer.toString(transportPreferences.getPaceGrammar(getContext())));
            onSetPaceAction(Integer.parseInt(count.getText().toString()), GRAMMAR_INDEX);
            count.setOnKeyListener((v, keyCode, event) -> {
                if (!count.getText().toString().equals(""))
                    onSetPaceAction(Integer.parseInt(count.getText().toString()), GRAMMAR_INDEX);
                else
                    onSetPaceAction(0, GRAMMAR_INDEX);
                return false;
            });
            view.findViewById(R.id.buttonCancel).setOnClickListener(v -> ((mainPlain) requireActivity()).goToTheSpecialSettings(new SettingsFragment(SettingsIndex.common)));
            view.findViewById(R.id.buttonAccept).setOnClickListener(v -> {
                transportPreferences.setPaceGrammar(getContext(), Integer.parseInt(count.getText().toString()));
                transportPreferences.setCriticalValue(getContext(), GRAMMAR_INDEX, Integer.parseInt(count.getText().toString())*3);
                if (transportPreferences.getTodayLeftStudyCardQuantity(context, GRAMMAR_INDEX) > Integer.parseInt(count.getText().toString())) {
                    transportPreferences.setTodayLeftStudyCardQuantity(context, GRAMMAR_INDEX, Integer.parseInt(count.getText().toString()));
                    ((mainPlain) requireActivity()).goToTheSpecialSettings(new SettingsFragment(SettingsIndex.common));
                } else
                    setAlertWindow();
            });
        }
        else{
            if (settingsIndex == SettingsIndex.VocabularyApproach) {
                FragmentsServer.setTitle(getString(R.string.VocabularyApproach));
                boolean isAudio = (transportPreferences.getVocabularyApproach(context) == ApproachManager.VOC_AUDIO_APPROACH);
                units.add(new SettingUnit(R.drawable.ic_icon_vocabular_audio, getResources().getString(R.string.VocabularyAudioAp), isAudio, getString(R.string.VocabularyAudioApDescript), () -> transportPreferences.setVocabularyApproach(context, ApproachManager.VOC_AUDIO_APPROACH)));
                units.add(new SettingUnit(R.drawable.ic_icon_vocabular_read, getResources().getString(R.string.VocabularyTraditionalAp), !(isAudio), getString(R.string.VocabularyTraditionalApDescript), () -> transportPreferences.setVocabularyApproach(context, ApproachManager.VOC_READ_APPROACH)));
            }
            else if (settingsIndex == SettingsIndex.GrammarApproach) {
                FragmentsServer.setTitle(getString(R.string.GrammarApproach));
                int index = transportPreferences.getGrammarWay(context);
                units.add(new SettingUnit(R.drawable.ic_grammar_deduction, getResources().getString(R.string.GrammarDeduction), (index == ApproachManager.GRAMMAR_WAY_DECUCTION), getString(R.string.GrammarDeductionDesc), () -> transportPreferences.setGrammarWay(context, ApproachManager.GRAMMAR_WAY_DECUCTION)));
                units.add(new SettingUnit(R.drawable.ic_grammar_transduction, getResources().getString(R.string.GrammarTransduction), (index == ApproachManager.GRAMMAR_WAY_TRANSDUCTION), getString(R.string.GrammarTransductionDesc), () -> transportPreferences.setGrammarWay(context, ApproachManager.GRAMMAR_WAY_TRANSDUCTION)));
                units.add(new SettingUnit(R.drawable.ic_grammar_induction, getResources().getString(R.string.GrammarInduction), (index == ApproachManager.GRAMMAR_WAY_INDUCTION), getString(R.string.GrammarInductionDesc), () -> transportPreferences.setGrammarWay(context, ApproachManager.GRAMMAR_WAY_INDUCTION)));

            }
            else if (settingsIndex == SettingsIndex.PhraseologyFrequency) {
                FragmentsServer.setTitle(getResources().getString(R.string.frequency) + " " + getResources().getString(R.string.phraseology_padeg));
                int index = transportPreferences.getFrequency(context, PHRASEOLOGY_INDEX);
                units.add(new SettingUnit(R.drawable.ic_icon_velocity, getResources().getString(R.string.frequency_expo), (index == FREQUENCY_EXPO), getString(R.string.frequency_excpo_descript), () -> transportPreferences.setFrequency(context, PHRASEOLOGY_INDEX ,FREQUENCY_EXPO)));
                units.add(new SettingUnit(R.drawable.ic_icon_frequency, getResources().getString(R.string.frequency_linear), (index == ApproachManager.FREQUENCY_LINEAR), getString(R.string.frequency_linear_descript), () -> transportPreferences.setFrequency(context, PHRASEOLOGY_INDEX, ApproachManager.FREQUENCY_LINEAR)));
            }
            else if (settingsIndex == SettingsIndex.GrammarFrequency) {
                FragmentsServer.setTitle(getResources().getString(R.string.frequency) + " " + getResources().getString(R.string.grammar_padeg));
                int index = transportPreferences.getFrequency(context, GRAMMAR_INDEX);
                units.add(new SettingUnit(R.drawable.ic_icon_velocity, getResources().getString(R.string.frequency_expo), (index == FREQUENCY_EXPO), getString(R.string.frequency_excpo_descript), () -> transportPreferences.setFrequency(context, GRAMMAR_INDEX ,FREQUENCY_EXPO)));
                units.add(new SettingUnit(R.drawable.ic_icon_frequency, getResources().getString(R.string.frequency_linear), (index == ApproachManager.FREQUENCY_LINEAR), getString(R.string.frequency_linear_descript), () -> transportPreferences.setFrequency(context, GRAMMAR_INDEX, ApproachManager.FREQUENCY_LINEAR)));
            }
            else if (settingsIndex == SettingsIndex.PhoneticsFrequency) {
                FragmentsServer.setTitle(getResources().getString(R.string.frequency) + " " + getResources().getString(R.string.grammar_padeg));
                int index = transportPreferences.getFrequency(context, PHONETIC_INDEX);
                units.add(new SettingUnit(R.drawable.ic_icon_velocity, getResources().getString(R.string.frequency_expo), (index == FREQUENCY_EXPO), getString(R.string.frequency_excpo_descript), () -> transportPreferences.setFrequency(context, PHONETIC_INDEX ,FREQUENCY_EXPO)));
                units.add(new SettingUnit(R.drawable.ic_icon_frequency, getResources().getString(R.string.frequency_linear), (index == ApproachManager.FREQUENCY_LINEAR), getString(R.string.frequency_linear_descript), () -> transportPreferences.setFrequency(context, PHONETIC_INDEX, ApproachManager.FREQUENCY_LINEAR)));
            }
            else if (settingsIndex == SettingsIndex.CultureFrequency) {
                FragmentsServer.setTitle(getResources().getString(R.string.frequency) + " " + getResources().getString(R.string.culture_padeg));
                int index = transportPreferences.getFrequency(context, CULTURE_INDEX);
                units.add(new SettingUnit(R.drawable.ic_icon_velocity, getResources().getString(R.string.frequency_expo), (index == FREQUENCY_EXPO), getString(R.string.frequency_excpo_descript), () -> transportPreferences.setFrequency(context, CULTURE_INDEX ,FREQUENCY_EXPO)));
                units.add(new SettingUnit(R.drawable.ic_icon_frequency, getResources().getString(R.string.frequency_linear), (index == ApproachManager.FREQUENCY_LINEAR), getString(R.string.frequency_linear_descript), () -> transportPreferences.setFrequency(context, CULTURE_INDEX, ApproachManager.FREQUENCY_LINEAR)));
            }
            else if (settingsIndex == SettingsIndex.ExceptionsFrequency) {
                FragmentsServer.setTitle(getResources().getString(R.string.frequency) + " " + getResources().getString(R.string.grammar_padeg));
                int index = transportPreferences.getFrequency(context, EXCEPTION_INDEX);
                units.add(new SettingUnit(R.drawable.ic_icon_velocity, getResources().getString(R.string.frequency_expo), (index == FREQUENCY_EXPO), getString(R.string.frequency_excpo_descript), () -> transportPreferences.setFrequency(context, EXCEPTION_INDEX ,FREQUENCY_EXPO)));
                units.add(new SettingUnit(R.drawable.ic_icon_frequency, getResources().getString(R.string.frequency_linear), (index == ApproachManager.FREQUENCY_LINEAR), getString(R.string.frequency_linear_descript), () -> transportPreferences.setFrequency(context, EXCEPTION_INDEX, ApproachManager.FREQUENCY_LINEAR)));
            }
            else if (settingsIndex == SettingsIndex.VocabularyFrequency) {
                FragmentsServer.setTitle(getResources().getString(R.string.frequency) + " " + getResources().getString(R.string.vocabulary_padeg));
                int index = transportPreferences.getFrequency(context, ApproachManager.VOCABULARY_INDEX);
                units.add(new SettingUnit(R.drawable.ic_icon_velocity, getResources().getString(R.string.frequency_expo), (index == FREQUENCY_EXPO), getString(R.string.frequency_excpo_descript), () -> transportPreferences.setFrequency(context, VOCABULARY_INDEX ,FREQUENCY_EXPO)));
                units.add(new SettingUnit(R.drawable.ic_icon_frequency, getResources().getString(R.string.frequency_linear), (index == ApproachManager.FREQUENCY_LINEAR), getString(R.string.frequency_linear_descript), () -> transportPreferences.setFrequency(context, VOCABULARY_INDEX, ApproachManager.FREQUENCY_LINEAR)));
            }
            else if (settingsIndex == SettingsIndex.PhoneticsApproach) {
                FragmentsServer.setTitle(getResources().getString(R.string.PhoneticsApproach));
                int index = transportPreferences.getPhoneticsApproach(context);
                units.add(new SettingUnit(R.drawable.ic_grammar_deduction, getResources().getString(R.string.PhoneticsTheoryName), (index == ApproachManager.PHONETICS_APPROACH_DEDUCTION), getString(R.string.PhoneticsTheoryDescript), () -> transportPreferences.setPhoneticspproach(context, ApproachManager.PHONETICS_APPROACH_DEDUCTION)));
                units.add(new SettingUnit(R.drawable.ic_grammar_transduction, getResources().getString(R.string.PhoneticsSimpleName), (index == ApproachManager.PHONETICS_APPROACH_INDUCTION), getString(R.string.PhoneticsSimpleDescript), () -> transportPreferences.setPhoneticspproach(context, ApproachManager.PHONETICS_APPROACH_INDUCTION)));
            }
            else if (settingsIndex == SettingsIndex.VocabularyViewApproach) {
                FragmentsServer.setTitle(getString(R.string.VocabularyViewApproach));

                isManySelected = true;

                units.add(new SettingUnit(R.drawable.ic_icon_vocabular_view_translate, getResources().getString(R.string.VocabularyTranslateViewAp), (transportPreferences.getTranslatePriority(context) != consts.PRIORITY_NEVER), getString(R.string.VocabularyTranslateViewApDescript), () -> {
                    transportPreferences.setImagePriority(context, consts.PRIORITY_NEVER);
                    transportPreferences.setMeaningPriority(context, consts.PRIORITY_NEVER);
                    transportPreferences.setTranslatePriority(context, consts.PRIORITY_MAX);
                }));

                units.add(new SettingUnit(R.drawable.ic_icon_vocabular_view_meaning, getResources().getString(R.string.VocabularyMeaningViewAp), (transportPreferences.getMeaningPriority(context) != consts.PRIORITY_NEVER), getString(R.string.VocabularyMeaningViewApDescript), () -> {
                    transportPreferences.setImagePriority(context, consts.PRIORITY_MAX);
                    transportPreferences.setMeaningPriority(context, consts.PRIORITY_MAX);
                    transportPreferences.setTranslatePriority(context, consts.PRIORITY_NEVER);
                }));

                doSomehtingIntefaceBoth = () -> {
                    transportPreferences.setImagePriority(context, consts.PRIORITY_MAX);
                    transportPreferences.setMeaningPriority(context, consts.PRIORITY_MAX);
                    transportPreferences.setTranslatePriority(context, consts.PRIORITY_MAX);
                };
                BothDescription = getString(R.string.VocabularyBothViewApDescript);
            }
            else if (settingsIndex == SettingsIndex.MeaningLanguage) {
                FragmentsServer.setTitle(getString(R.string.DefenitionLanguage));

                isManySelected = true;

                units.add(new SettingUnit(R.drawable.ic_icon_description_new, getResources().getString(R.string.LanguageNew), transportPreferences.getMeaningLanguage(context) != consts.LANGUAGE_NATIVE, getString(R.string.DefenitionLanguageNewDescript), () -> transportPreferences.setMeaningLanguage(context, consts.LANGUAGE_NEW)));
                units.add(new SettingUnit(R.drawable.ic_icon_description_native, getResources().getString(R.string.LanguageNative), transportPreferences.getMeaningLanguage(context) != consts.LANGUAGE_NEW, getString(R.string.DefenitionLanguageNativeDescript), () -> transportPreferences.setMeaningLanguage(context, consts.LANGUAGE_NATIVE)));

                doSomehtingIntefaceBoth = () -> transportPreferences.setMeaningLanguage(context, consts.LANGUAGE_BOTH);
                BothDescription = getString(R.string.DefenitionLanguageBothDescript);
            }

            else if (settingsIndex == SettingsIndex.ExampleLanguage) {
                FragmentsServer.setTitle(getString(R.string.ExampleLanguage));

                isManySelected = true;

                units.add(new SettingUnit(R.drawable.ic_icon_description_new, getResources().getString(R.string.LanguageNew), ((transportPreferences.getExampleLanguage(context) == consts.LANGUAGE_NEW ||
                        transportPreferences.getExampleLanguage(context) == consts.LANGUAGE_BOTH) && transportPreferences.getExampleAvailability(context) == consts.AVAILABILITY_ON), getString(R.string.ExampleLanguageNewDescript), () -> {
                            transportPreferences.setExampleLanguage(context, consts.LANGUAGE_NEW);
                            transportPreferences.setExampleAvailability(context, consts.AVAILABILITY_ON);
                        }));
                units.add(new SettingUnit(R.drawable.ic_icon_description_native, getResources().getString(R.string.LanguageNative), ((transportPreferences.getExampleLanguage(context) == consts.LANGUAGE_NATIVE ||
                        transportPreferences.getExampleLanguage(context) == consts.LANGUAGE_BOTH) && transportPreferences.getExampleAvailability(context) == consts.AVAILABILITY_ON), getString(R.string.ExampleLanguageNativeDescript), () -> {
                            transportPreferences.setExampleLanguage(context, consts.LANGUAGE_NATIVE);
                            transportPreferences.setExampleAvailability(context, consts.AVAILABILITY_ON);
                        }));

                doSomehtingIntefaceBoth = () -> {
                    transportPreferences.setExampleLanguage(context, consts.LANGUAGE_BOTH);
                    transportPreferences.setExampleAvailability(context, consts.AVAILABILITY_ON);
                };
                doSomehtingIntefaceNone = () -> transportPreferences.setExampleAvailability(context, consts.AVAILABILITY_OFF);

                BothDescription = getString(R.string.ExampleLanguageBothDescript);
                NoneDescription = getString(R.string.ExampleLanguageNoneDescript);
            }



            if (units.size()==2) {
                recyclerView.setVisibility(View.GONE);
                view.findViewById(R.id.linearLayout).setVisibility(View.VISIBLE);
                isHorisontal = true;

                final TextView description = view.findViewById(R.id.description);
                final TextView command1 = view.findViewById(R.id.command);
                final TextView command2 = view.findViewById(R.id.command2);
                final View indicator = view.findViewById(R.id.indicator);
                final View indicator2 = view.findViewById(R.id.indicator2);
                final ImageView image1 = view.findViewById(R.id.image);
                final ImageView image2 = view.findViewById(R.id.image2);

                command1.setText(units.get(0).getCommand());
                command2.setText(units.get(1).getCommand());
                image1.setImageResource(units.get(0).getImageID());
                image2.setImageResource(units.get(1).getImageID());

                final boolean finalIsManySelected = isManySelected;
                final String finalBothDescription = BothDescription;
                final String finalNoneDescription = NoneDescription;
                view.findViewById(R.id.setting_block_one).setOnClickListener(v -> {
                    Log.i("main", "click 1");
                    if (!finalIsManySelected) {
                        description.setText(units.get(0).getDescription());
                        units.get(0).setChecked(true);
                        units.get(1).setChecked(false);
                        indicator.setVisibility(View.VISIBLE);
                        indicator2.setVisibility(View.GONE);
                    } else {
                        if (units.get(0).isChecked()) {
                            description.setText(units.get(1).getDescription());
                            units.get(0).setChecked(false);
                            indicator.setVisibility(View.GONE);
                        } else {
                            description.setText(units.get(0).getDescription());
                            units.get(0).setChecked(true);
                            indicator.setVisibility(View.VISIBLE);
                        }
                    }
                    if (units.get(0).isChecked() && units.get(1).isChecked()) {
                        description.setText(finalBothDescription);
                    } else if (!units.get(0).isChecked() && !units.get(1).isChecked()) {
                        if (finalNoneDescription.equals(""))
                            accept.setVisibility(View.GONE);
                        description.setText(finalNoneDescription);
                    } else
                        accept.setVisibility(View.VISIBLE);
                });

                view.findViewById(R.id.setting_block_two).setOnClickListener(v -> {
                    Log.i("main", "click 2");
                    if (!finalIsManySelected) {
                        description.setText(units.get(1).getDescription());
                        units.get(1).setChecked(true);
                        units.get(0).setChecked(false);
                        indicator2.setVisibility(View.VISIBLE);
                        indicator.setVisibility(View.GONE);
                    } else {
                        if (units.get(1).isChecked()) {
                            description.setText(units.get(0).getDescription());
                            units.get(1).setChecked(false);
                            indicator2.setVisibility(View.GONE);
                        } else {
                            description.setText(units.get(1).getDescription());
                            units.get(1).setChecked(true);
                            indicator2.setVisibility(View.VISIBLE);
                        }
                    }
                    if (units.get(0).isChecked() && units.get(1).isChecked()) {
                        description.setText(finalBothDescription);
                    } else if (!units.get(0).isChecked() && !units.get(1).isChecked()) {
                        if (finalNoneDescription.equals(""))
                            accept.setVisibility(View.GONE);
                        description.setText(finalNoneDescription);
                    } else
                        accept.setVisibility(View.VISIBLE);


                });

                if (!isManySelected) {
                    if (units.get(0).isChecked())
                        view.findViewById(R.id.setting_block_one).callOnClick();
                    else
                        view.findViewById(R.id.setting_block_two).callOnClick();
                }
                else
                {
                    Log.i("main", "isChecked 0 " + units.get(0).isChecked());
                    Log.i("main", "isChecked 1 " + units.get(1).isChecked());

                    if (units.get(0).isChecked() && units.get(1).isChecked()) {
                        description.setText(BothDescription);
                        indicator2.setVisibility(View.VISIBLE);
                        indicator.setVisibility(View.VISIBLE);
                    }
                    else if (units.get(0).isChecked()) {
                        indicator.setVisibility(View.VISIBLE);
                        indicator2.setVisibility(View.GONE);
                        description.setText(units.get(0).getDescription());
                    }
                    else if (units.get(1).isChecked()) {
                        indicator2.setVisibility(View.VISIBLE);
                        indicator.setVisibility(View.GONE);
                        description.setText(units.get(1).getDescription());
                    }
                    else {
                        description.setText(NoneDescription);
                        indicator.setVisibility(View.GONE);
                        indicator2.setVisibility(View.GONE);
                    }
                }

            }
            else{
                view.findViewById(R.id.linearLayout).setVisibility(View.GONE);


                final TextView description = view.findViewById(R.id.description);

                for (SettingUnit unit : units){
                    if (unit.isChecked()){
                        description.setText(unit.getDescription());
                        break;
                    }
                }



                adapter.setBlockListener(position -> {
                    ((TextView)view.findViewById(R.id.description)).setText(units.get(position).getDescription());
                    for (int i = 0; i < units.size(); i++){
                        if (units.get(i).isChecked())
                            units.get(i).setChecked(false);
                    }
                    units.get(position).setChecked(true);
                    adapter.notifyDataSetChanged();
                });
            }

            if (transportPreferences.isStarted(context)){
                Button cancel = view.findViewById(R.id.buttonCancel);
                cancel.setVisibility(View.VISIBLE);
                cancel.setOnClickListener(v -> ((mainPlain) requireActivity()).goToTheSpecialSettings(new SettingsFragment(SettingsIndex.common)));
            }

            final SettingUnit.DoSomehtingInteface finalDoSomehtingInteface = doSomehtingIntefaceBoth;

            final boolean finalIsManySelected1 = isManySelected;
            final SettingUnit.DoSomehtingInteface finalDoSomehtingIntefaceNone = doSomehtingIntefaceNone;
            accept.setOnClickListener(v -> {
                if (finalIsManySelected1 && units.get(0).isChecked() && units.get(1).isChecked())
                    finalDoSomehtingInteface.doSomething();
                else if(finalDoSomehtingIntefaceNone!= null && !units.get(0).isChecked() && !units.get(1).isChecked())
                    finalDoSomehtingIntefaceNone.doSomething();
                else{
                    for (int i = 0; i < units.size(); i++) {
                        if (units.get(i).isChecked()) {
                            units.get(i).doSomething();
                            break;
                        }
                    }
                }

                if(transportPreferences.isStarted(context)){
                    ((mainPlain) requireActivity()).goToTheSpecialSettings(new SettingsFragment(SettingsIndex.common));
                }
                else{
                    // change it!
                }
            });


        }



        if (!isHorisontal) {
            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
            recyclerView.setAdapter((RecyclerView.Adapter) adapter);
            recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
            adapter.notifyDataSetChanged();
            }

        wasCreated = true;
        adapter.setClickable(setEnable);

        }

        private void onSetPaceAction(int pace, int index){
            final int lowerStopTrigger;
            final int lowerNormalTrigger;
            final int upperNormalTrigger;
            final int upperWarningTrigger;
            final int upperStopTrigger;
            if (index == GRAMMAR_INDEX || index == PHONETIC_INDEX || index == CULTURE_INDEX){
                lowerStopTrigger = 0;
                lowerNormalTrigger = 2;
                upperNormalTrigger = 5;
                upperWarningTrigger = 15;
                upperStopTrigger = 30;
            } else {
                lowerStopTrigger = 0;
                lowerNormalTrigger = 7;
                upperNormalTrigger = 50;
                upperWarningTrigger = 200;
                upperStopTrigger = 500;
            }
            TextView description = requireView().findViewById(R.id.description);
            TextView warning = requireView().findViewById(R.id.warning);
            Button accept = requireView().findViewById(R.id.buttonAccept);
            accept.setVisibility(View.VISIBLE);
            description.setText(getString(R.string.paceYouCanLearn) + " " + (pace*7) + " " +
                    getWordEnd(pace*7, index) + " " + getString(R.string.pacePerWeek) + (pace*30) + " " +
                    getString(R.string.pacePerMonth) + " " + pace*365  + " " + getString(R.string.pacePerYear));

            if (pace <= lowerStopTrigger || pace >= upperStopTrigger){
                warning.setText(getString(R.string.paceNever));
                description.setText(getString(R.string.paceNever));
                warning.setTextColor(getResources().getColor(R.color.colorAccent));
                accept.setVisibility(View.GONE);
            }
            else if (pace <= lowerNormalTrigger){
                warning.setText(getString(R.string.paceToSmall));
            warning.setTextColor(getResources().getColor(R.color.colorOrange));}
            else if (pace <= upperNormalTrigger){
                warning.setText(getString(R.string.paceNormal));
            warning.setTextColor(getResources().getColor(R.color.colorPrimary));}
            else if (pace <= upperWarningTrigger){
                warning.setText(getString(R.string.paceHigh));
                warning.setTextColor(getResources().getColor(R.color.colorOrange));
                }
            else{
                warning.setText(getString(R.string.paceReallyHigh));
            warning.setTextColor(getResources().getColor(R.color.colorOrange));}
        }



    public String getWordEnd(int number, int index) {
        String currentLanguage = Locale.getDefault().getDisplayLanguage();
        if (index == VOCABULARY_INDEX) {
            if (currentLanguage.toLowerCase().contains("en")) {
                if (number == 1)
                    return getString(R.string.paceWord);
                else
                    return getString(R.string.paceWords);
            }
            if (number % 10 == 0 || number % 10 >= 5 || ((number + 100) % 100 >= 10                     // говнокод для определения окончания слов после числительных
                    && (number + 100) % 100 <= 20))
                return getString(R.string.paceWordsRuMuch);
            else if (number % 10 == 1)
                return getString(R.string.paceWordsRuOne);
            else
                return getString(R.string.paceWordsRuSeveral);
        } else if (index == PHRASEOLOGY_INDEX) {
            if (currentLanguage.toLowerCase().contains("en")) {
                if (number == 1)
                    return getString(R.string.pacePhrase);
                else
                    return getString(R.string.pacePhrases);
            }
            if (number % 10 == 0 || number % 10 >= 5 || ((number + 100) % 100 >= 10                     // говнокод для определения окончания слов после числительных
                    && (number + 100) % 100 <= 20))
                return getString(R.string.pacePhrasesRuMuch);
            else if (number % 10 == 1)
                return getString(R.string.pacePhrasesRuOne);
            else
                return getString(R.string.pacePhrasesRuSeveral);
        } else if (index == GRAMMAR_INDEX || index == PHONETIC_INDEX || index == EXCEPTION_INDEX) {
            if (currentLanguage.toLowerCase().contains("en")) {
                if (number == 1)
                    return "rule";
                else
                    return "rules";
            }
            if (number % 10 == 0 || number % 10 >= 5 || ((number + 100) % 100 >= 10                     // говнокод для определения окончания слов после числительных
                    && (number + 100) % 100 <= 20))
                return "правил";
            else if (number % 10 == 1)
                return "правило";
            else
                return "правила";
        } else  {
            if (currentLanguage.toLowerCase().contains("en")) {
                if (number == 1)
                    return "article";
                else
                    return "articles";
            }
            if (number % 10 == 0 || number % 10 >= 5 || ((number + 100) % 100 >= 10                     // говнокод для определения окончания слов после числительных
                    && (number + 100) % 100 <= 20))
                return "статей";
            else if (number % 10 == 1)
                return "статья";
            else
                return "статьи";
        }
    }


    private void setAlertWindow(){
        final AlertDialog.Builder adb = new AlertDialog.Builder(context);
        View my_custom_view = getLayoutInflater().inflate(R.layout.add_card_wrong, null);
        adb.setView(my_custom_view);

        TextView errorName = my_custom_view.findViewById(R.id.errorName);
        errorName.setText("принято");
        TextView actionMessage = my_custom_view.findViewById(R.id.what_is_wrong);
        actionMessage.setText("изменения будут задействованы с завтрашнего дня");

        final AlertDialog ad = adb.create();

        Button cancel = my_custom_view.findViewById(R.id.next);
        cancel.setOnClickListener(v -> ad.cancel());

        ad.show();
        ad.setOnCancelListener(dialog -> ((mainPlain) requireActivity()).goToTheSpecialSettings(new SettingsFragment(SettingsIndex.common)));
    }


    @Override
    public void setEnable(Boolean enable) {
        setEnable = enable;
        if (wasCreated) {
            adapter.setClickable(setEnable);
        }
    }

    @Override
    public String getTitle() {
        return "настройки";
    }
}

