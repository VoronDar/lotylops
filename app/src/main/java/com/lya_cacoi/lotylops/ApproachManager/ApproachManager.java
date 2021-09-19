package com.lya_cacoi.lotylops.ApproachManager;

import android.content.Context;

import androidx.fragment.app.Fragment;

import com.lya_cacoi.lotylops.Databases.transportSQL.SqlMain;
import com.lya_cacoi.lotylops.cards.Card;
import com.lya_cacoi.lotylops.declaration.consts;

public class ApproachManager {

    /** сначала аудио - потом чтение */
    public static final int VOC_AUDIO_APPROACH                      = 0;
    /** сначала чтение - потом аудио */
    public static final int VOC_READ_APPROACH                       = 1;
    /** только чтение */
    public static final int VOC_ONLY_READ_APPROACH                  = 2;
    /** только аудио */
    public static final int VOC_ONLY_AUDIO_APPROACH                 = 3;
    public static final String VOC_APPROACH_PREFERENCE  = "vocapproachpreference";
    public static final String GRAMMAR_WAY_PREFERENCE   = "grammarway";
    public static final String PHONETICS_WAY_PREFERENCE = "phoneticsway";


    /** работаем только на чтение/слушание */
    public static final int VOC_PACCIVE_APPROACH                    = 0;
    /** работаем над всем постепенно*/
    public static final int VOC_EVERYTHING_APPROACH                 = 1;
    /** прорабатываем все тесты, что есть. Для тех у кого прям адовые проблемы с памятью */
    public static final int VOC_LONG_APPROACH                       = 2;
    /** сразу на практику */
    public static final int VOC_PRACTICE_APPROACH                   = 3;
    /** без тестов на перевод - сразу с аудированием*/
    public static final int VOC_OPTIMAL_APPROACH                    = 4;
    public static final String VOC_TRAIN_PREFERENCE  = "voctrainpreference";


    public static final int GRAMMAR_WAY_DECUCTION   = 0;                                                                // подходы для грамматики - дедукция, индукция, трансдукция
    public static final int GRAMMAR_WAY_INDUCTION   = 1;
    public static final int GRAMMAR_WAY_TRANSDUCTION= 2;

    public static final int PHONETICS_APPROACH_DEDUCTION = 0;
    public static final int PHONETICS_APPROACH_INDUCTION = 1;

    public static final int VOCABULARY_INDEX                    = 0;                                                    // индексы разделов
    public static final int PHRASEOLOGY_INDEX                   = 1;
    public static final int GRAMMAR_INDEX                       = 2;
    public static final int EXCEPTION_INDEX                     = 3;
    public static final int CULTURE_INDEX                       = 4;
    public static final int PHONETIC_INDEX                      = 5;

    public static final String FREQUENCY_PREQUENCE              = "frequency";
    public static final int FREQUENCY_LINEAR = 0;
    public static final int FREQUENCY_EXPO = 1;

    public static String getSectionString(int index){
        switch (index){
            case VOCABULARY_INDEX:
                return "voc";
            case PHRASEOLOGY_INDEX:
                return "phr";
            case EXCEPTION_INDEX:
                return "exc";
            case GRAMMAR_INDEX:
                return "gr";
            case CULTURE_INDEX:
                return "cul";
            case PHONETIC_INDEX:
                return "phn";
            default:
                return null;
        }
    }

    public interface Manager{
        Fragment getTheoryFragment();                                                               // возвращает фрагмент для теории
        Fragment getTrainFragment(int trainLevel, Card card);                                       // возвращает фрагмент для практики
        consts.cardState setCardStudyState(Card card);                                              // возвращает состояние просмотра (stepTrain|practice|study)
        void upPractice(Card card);                                                                 // начисляет значение practiceLevel при закрытии карты
        Fragment closeTest(Card card, consts.cardState getState, boolean isRight, SqlMain sqlMain); // закрывает тест и переходит или к другому тесту или к теории. Возвращает null если дальше не его сфера
        void setErrorProgress(Card card, consts.cardState cardState);                               // начисляет прогресс при ошибке
        boolean isTrainAfterStudy(consts.cardState cardStudyState, Card lastCard);                  // возвращает true, если после показа теории нужно показывать тесты
        int getIndex();
        int getAddableCount();                                                                      // возвращает количество карт, которое можно добавить

    }

    public static Manager getManager(Context context, int index){
        switch (index){
            case VOCABULARY_INDEX:
                return new VocabularyManager(context);
            case GRAMMAR_INDEX:
                return new GrammarManager(context);
            case PHRASEOLOGY_INDEX:
                return new PhraseologyManager(context);
            case EXCEPTION_INDEX:
                return new ExceptionManager(context);
            case CULTURE_INDEX:
                return new CultureManager(context);
            case PHONETIC_INDEX:
                return new PhoneticsManager(context);
            default:
                return null;
        }
    }
}
