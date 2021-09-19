package com.lya_cacoi.lotylops.transportPreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.lya_cacoi.lotylops.ApproachManager.ApproachManager;
import com.lya_cacoi.lotylops.Databases.transportSQL.SqlStatistic;
import com.lya_cacoi.lotylops.Helper.Helper;
import com.lya_cacoi.lotylops.declaration.consts;
import com.lya_cacoi.lotylops.ruler.Ruler;

import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.FREQUENCY_LINEAR;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.GRAMMAR_WAY_PREFERENCE;

public class transportPreferences {
    public static String AVAILABLE_ID = "card_id_pref";                                             // indicate on ID you can use for creating new cards


    /** глобальный уровень пользователя*/
    public static int getLevel(Context context){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("level", 1);
    }
    public static void setLevel(Context context, int value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt("level", value).apply();
    }
    /** очки опыта, которые помогают добрать уровень*/
    public static int getExp(Context context){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("exp", 0);
    }
    public static void setExp(Context context, int value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt("exp", value).apply();
    }

    public static int getImagePriority(Context context){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(consts.IMAGE_PRIORITY_PREFERENCE, consts.PRIORITY_MAX);
    }
    public static void setImagePriority(Context context, int value){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(consts.IMAGE_PRIORITY_PREFERENCE, value).apply();
    }

    public static int getTranslatePriority(Context context){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(consts.TRANSLATE_PRIORITY_PREFERENCE, consts.PRIORITY_MAX);
    }
    public static void setTranslatePriority(Context context, int value){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(consts.TRANSLATE_PRIORITY_PREFERENCE, value).apply();
    }

    public static int getMeaningPriority(Context context){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(consts.MEANING_PRIORITY_PREFERENCE, consts.PRIORITY_MAX);
    }
    public static void setMeaningPriority(Context context, int value){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(consts.MEANING_PRIORITY_PREFERENCE, value).apply();
    }
    public static int getMeaningLanguage(Context context){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(consts.MEANING_LANGUAGE_PREFERENCE, consts.LANGUAGE_BOTH);
    }
    public static void setMeaningLanguage(Context context, int value){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(consts.MEANING_LANGUAGE_PREFERENCE, value).apply();
    }


    public static int getSynomymAvailability(Context context){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(consts.SYNONYM_AVAILABILITY_PREFERENCE, consts.AVAILABILITY_ON);
    }
    public static void setSynomymAvailability(Context context, int value){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(consts.SYNONYM_AVAILABILITY_PREFERENCE, value).apply();
    }


    public static int getAntonymAvailability(Context context){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(consts.ANTONYM_AVAILABILITY_PREFERENCE, consts.AVAILABILITY_ON);
    }
    public static void setAntonymAvailability(Context context, int value){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(consts.ANTONYM_AVAILABILITY_PREFERENCE, value).apply();
    }



    public static int getExampleAvailability(Context context){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(consts.EXAMPLE_AVAILABILITY_PREFERENCE, consts.AVAILABILITY_ON);
    }
    public static void setExampleAvailability(Context context, int value){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(consts.EXAMPLE_AVAILABILITY_PREFERENCE, value).apply();
    }
    public static int getExampleLanguage(Context context){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(consts.EXAMPLE_LANGUAGE_PREFERENCE, consts.LANGUAGE_BOTH);
    }
    public static void setExampleLanguage(Context context, int value){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(consts.EXAMPLE_LANGUAGE_PREFERENCE, value).apply();
    }


    public static boolean isStarted(Context context){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(consts.IS_GAME_STARTED, false);
    }
    public static void setStarted(Context context, boolean value){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean(consts.IS_GAME_STARTED, value).apply();
    }

    /** если на момент начала дня не было карт вообще */
    public static boolean isNothingStarted(Context context){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("isnothing", false);
    }
    public static void setNothingStarted(Context context, boolean value){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean("isnothing", value).apply();
    }

    /** завершена ли обязательная часть плана */
    public static boolean isDayCompleted(Context context){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("isDayCompleted", false);
    }
    public static void setDayCompleted(Context context, boolean value){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean("isDayCompleted", value).apply();
    }


    public static int getStartDay(Context context){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(consts.FIRST_DAY, 0);
    }
    public static void setStartDay(Context context, int value){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(consts.FIRST_DAY, value).apply();
    }


    public static int getExtraDayCount(Context context){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(consts.EXTRA_DAYS, 0);
    }
    public static void setExtraDayCount(Context context, int value){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(consts.EXTRA_DAYS, value).apply();
    }

    public static int getAllTodayStudyCount(Context context, int block){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("allStudyCount" + block, 0);
    }
    public static void setAllTodayStudyCount(Context context, int block, int value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt("allStudyCount" + block, value).apply();
    }
    public static int getAllTodayRepeatCount(Context context, int block){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("allRepeatCount" + block, 0);
    }
    public static void setAllTodayRepeatCount(Context context, int block, int value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt("allRepeatCount" + block, value).apply();
    }

    public static int getTodayStudyLoaded(Context context){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(consts.IS_TODAY_STUDY_CARD_LOADED, 0);
    }
    public static void setTodayStudyLoaded(Context context, int value){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(consts.IS_TODAY_STUDY_CARD_LOADED, value).apply();
    }

    public static int getTodayPhraseologyStudyLoaded(Context context){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(consts.IS_TODAY_PHRASEOLOGY_STUDY_CARD_LOADED, 0);
    }
    public static void setTodayPhraseologyStudyLoaded(Context context, int value){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(consts.IS_TODAY_PHRASEOLOGY_STUDY_CARD_LOADED, value).apply();
    }
    public static int getTodayPhraseologyRepeatLoaded(Context context){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(consts.IS_TODAY_PHRASEOLOGY_REPEAT_CARD_LOADED, 0);
    }
    public static void setTodayPhraseologyRepeatLoaded(Context context, int value){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(consts.IS_TODAY_PHRASEOLOGY_REPEAT_CARD_LOADED, value).apply();
    }

    public static int getTodayRepeatLoaded(Context context){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(consts.IS_TODAY_REPEAT_CARD_LOADED, 0);
    }
    public static void setTodayRepeatLoaded(Context context, int value){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(consts.IS_TODAY_REPEAT_CARD_LOADED, value).apply();
    }

    public static int getTodayStudyReturned(Context context){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(consts.IS_TODAY_CARD_STUDY_RETURNED, 0);
    }
    public static void setTodayStudyReturned(Context context, int value){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(consts.IS_TODAY_CARD_STUDY_RETURNED, value).apply();
    }

    public static int getTodayRepeatReturned(Context context){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(consts.IS_TODAY_CARD_REPEAT_RETURNED, 0);
    }
    public static void setTodayRepeatReturned(Context context, int value){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(consts.IS_TODAY_CARD_REPEAT_RETURNED, value).apply();
    }


    public static int getLastDayComming(Context context){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(consts.LAST_DAT, 0);
    }
    public static void setLastDayComming(Context context, int value){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(consts.LAST_DAT, value).apply();
    }

    public static int getPaceVocabulary(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(consts.PACE_VOCABULARY_PREFERENCE, 5);
    }
    public static void setPaceVocabulary(Context context, int value){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(consts.PACE_VOCABULARY_PREFERENCE, value).apply();
    }

    public static int getPaceGrammar(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(consts.PACE_GRAMMAR_PREFERENCE, 2);
    }
    public static void setPaceGrammar(Context context, int value){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(consts.PACE_GRAMMAR_PREFERENCE, value).apply();
    }



    public static int getTodayPhraseologyStudyReturned(Context context){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(consts.IS_TODAY_PHRASEOLOGY_CARD_STUDY_RETURNED, 0);
    }
    public static void setTodayPhraseologyStudyReturned(Context context, int value){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(consts.IS_TODAY_PHRASEOLOGY_CARD_STUDY_RETURNED, value).apply();
    }

    public static int getTodayPhraseologyRepeatReturned(Context context){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(consts.IS_TODAY_PHRASEOLOGY_CARD_REPEAT_RETURNED, 0);
    }
    public static void setTodayPhraseologyRepeatReturned(Context context, int value){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(consts.IS_TODAY_PHRASEOLOGY_CARD_REPEAT_RETURNED, value).apply();
    }

    public static int getTodayLeftStudyCardQuantity(Context context, int block){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("leftstudycards" + block, 0);
    }
    public static void setTodayLeftStudyCardQuantity(Context context, int block, int value){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt("leftstudycards" + block, value).apply();
    }
    public static int getTodayLeftRepeatCardQuantity(Context context, int block){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("leftrepeatcards" + block, 0);
    }
    public static void settTodayLeftRepeatCardQuantity(Context context, int block, int value){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt("leftrepeatcards" + block, value).apply();
    }


    public static int getPacePhraseology(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(consts.PACE_PHRASEOLOGY_PREFERENCE, 5);
    }
    public static void setPacePhraseology(Context context, int value){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(consts.PACE_PHRASEOLOGY_PREFERENCE, value).apply();
    }

    public static int getPaceException(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(consts.PACE_EXCEPTION_PREFERENCE, 5);
    }
    public static void setPaceException(Context context, int value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(consts.PACE_EXCEPTION_PREFERENCE, value).apply();
    }

    public static int getPaceCulture(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(consts.PACE_CULTURE_PREFERENCE, 5);
    }
    public static void setPaceCulture(Context context, int value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(consts.PACE_CULTURE_PREFERENCE, value).apply();
    }

    public static int getPacePhonetics(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(consts.PACE_PHONETICS_PREFERENCE, 5);
    }
    public static void setPacePhonetics(Context context, int value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(consts.PACE_PHONETICS_PREFERENCE, value).apply();
    }


    public static int getVocabularyApproach(Context context){                                       // return audio or read approach
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(ApproachManager.VOC_APPROACH_PREFERENCE, ApproachManager.VOC_READ_APPROACH);
    }
    public static void setVocabularyApproach(Context context, int value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(ApproachManager.VOC_APPROACH_PREFERENCE, value).apply();
    }

    public static int getVocabularyTrainApproach(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(ApproachManager.VOC_TRAIN_PREFERENCE, ApproachManager.VOC_EVERYTHING_APPROACH);
    }
    public static void setVocabularyTrainApproach(Context context, int value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(ApproachManager.VOC_TRAIN_PREFERENCE, value).apply();
    }

    public static int getPhoneticsApproach(Context context){                                       // return audio or read approach
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(ApproachManager.PHONETICS_WAY_PREFERENCE, ApproachManager.PHONETICS_APPROACH_DEDUCTION);
    }
    public static void setPhoneticspproach(Context context, int value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(ApproachManager.PHONETICS_WAY_PREFERENCE, value).apply();
    }

    public static int getCriticalValue(Context context, int section){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(ApproachManager.getSectionString(section)+ consts.CRITICAL_PREFERENCE, 15);
    }
    public static void setCriticalValue(Context context, int section, int value){                      // set value, when new cards don't add
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(ApproachManager.getSectionString(section)+ consts.CRITICAL_PREFERENCE, value).apply();
    }

    public static int getAvailableId(Context context, int section){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(ApproachManager.getSectionString(section)+AVAILABLE_ID, 0);
    }
    public static void setAvailableId(Context context, int section, int value){                     // set value, when new cards don't add
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(ApproachManager.getSectionString(section)+AVAILABLE_ID, value).apply();
    }


    public static int getGrammarWay(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(GRAMMAR_WAY_PREFERENCE, 0);
    }
    public static void setGrammarWay(Context context, int value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(GRAMMAR_WAY_PREFERENCE, value).apply();
    }

    public static void setOnStartDayStudyCount(Context context, int section, int value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(ApproachManager.getSectionString(section)+ consts.START_STUDY_COUNT_PREFERENCE, value).apply();
    }

    public static int getOnStartDayStudyCount(Context context, int section){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(ApproachManager.getSectionString(section)+ consts.START_STUDY_COUNT_PREFERENCE, 0);
    }

    public static void setStudiedCount(Context context, int section, int value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(ApproachManager.getSectionString(section)+"studiedCount", value).apply();
    }

    public static int getStudiedCount(Context context, int section){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(ApproachManager.getSectionString(section)+"studiedCount", 0);
    }

    public static void setRememberedCount(Context context, int section, int value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(ApproachManager.getSectionString(section)+"rememberCount", value).apply();
    }

    public static int getRememberedCount(Context context, int section){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(ApproachManager.getSectionString(section)+"rememberCount", 0);
    }
    public static void setForgettedCount(Context context, int section, int value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(ApproachManager.getSectionString(section)+"forgetCount", value).apply();
    }

    public static int getForgettedCount(Context context, int section){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(ApproachManager.getSectionString(section)+"forgetCount", 0);
    }

    public static void setLastTeacherDay(Context context, int section, int value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(ApproachManager.getSectionString(section)+"teacherDay", value).apply();
    }

    public static int getLastTeacherDay(Context context, int section){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(ApproachManager.getSectionString(section)+"teacherDay", Ruler.getDay(context) + SqlStatistic.pickCount);
    }


    public static void setFrequency(Context context, int section, int value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(ApproachManager.getSectionString(section)+ ApproachManager.FREQUENCY_PREQUENCE, value).apply();
    }

    public static int getFrequency(Context context, int section){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(ApproachManager.getSectionString(section)+ ApproachManager.FREQUENCY_PREQUENCE, FREQUENCY_LINEAR);
    }

    public static int getHelpStudy(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(consts.HELP_STATE_PREFERENCE, 0);
    }
    public static void setHelpStudy(Context context, int value){                      // set value, when new cards don't add
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(consts.HELP_STATE_PREFERENCE, value).apply();
    }


    public static void setLevel(Context context, int section, int value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(ApproachManager.getSectionString(section)+ Helper.LEVEL_PREFERENCE, value).apply();
    }

    public static int getLevel(Context context, int section){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(ApproachManager.getSectionString(section)+ Helper.LEVEL_PREFERENCE, -1);
    }

    public static void setMoney(Context context, int Value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt("money", Value).apply();
    }

    public static int getMoney(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("money", 4);
    }


    public static void setPreference(Context context, String preference, int Value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(preference, Value).apply();
    }

    public static int getPreference(Context context, String preference){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(preference, 0);
    }




    public static void setName(Context context, String value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString("acc_name", value).apply();
    }

    public static String getName(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("acc_name", "");
    }

    public static void setEmail(Context context, String value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString("acc_email", value).apply();
    }

    public static String getEmail(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("acc_email", "");
    }

    public static void setHelp(Context context, int block, boolean value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean("help_" + block, value).apply();
    }

    public static boolean getHelp(Context context, int block){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("help_" + block, false);
    }

    public static void setAchieve(Context context, int block, boolean value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean("achieve_" + block, value).apply();
    }

    public static boolean getAchieve(Context context, int block){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("achieve_" + block, false);
    }

    public static void setWeek(Context context, int block, int value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt("week"+ block , value).apply();
    }

    public static int getWeek(Context context, int block){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("week" + block, 0);
    }


    public static void setWeekRepeated(Context context, int block, int value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt("weekRepeated" + block , value).apply();
    }

    public static int getWeekRepeated(Context context, int block){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("weekRepeated" + block, 0);
    }
    public static void setWeekStudied(Context context, int block, int value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt("weekStudied" + block , value).apply();
    }

    public static int getWeekStudied(Context context, int block){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("weekStudied" + block, 0);
    }

    public static void setWeekMistakes(Context context, int block, int value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt("weekMistakes" + block , value).apply();
    }

    public static int getWeekMistakes(Context context, int block){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("weekMistakes" + block, 0);
    }

    public static void setWorkedByDay(Context context, int block, boolean value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean("workedByDay" + block , value).apply();
    }

    public static boolean getWorkedByDay(Context context, int block){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("workedByDay" + block, false);
    }


    public static boolean getSales(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("sales", false);
    }


    public static void setSales(Context context, boolean sales){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean("sales", sales).apply();
    }

    public static boolean isSales(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("sales", true);
    }

    // состояния - 0 - нет и не начиналось, 1 - есть и начинается, 2 - было и закончилось, 3 - было, закончилось, задокументировано, что завершено
    public static void setDayPlanState(Context context, int Value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt("dpstate", Value).apply();
    }

    public static int getDayPlanState(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("dpstate", 0);
    }


    public static void setPlayerHealth(Context context, int Value, int player){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt("plhealth" + player, Value).apply();
    }

    public static int getPlayerHealth(Context context, int player){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("plhealth" + player, 25);
    }

    public static void setPlayerScore(Context context, int Value, int player){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt("plcoins" + player, Value).apply();
    }

    public static int getPlayerScore(Context context, int player){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("plcoins" + player, -1);
    }

    public static void setWinHolder(Context context, int Value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt("winHolder", Value).apply();
    }

    public static int getWinHolder(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("winHolder", 1);
    }

    public static void setWinAlwaysCount(Context context, int Value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt("winCount", Value).apply();
    }

    public static int getWinAlwaysCount(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("winCount", 0);
    }

    public static void setTurnplayer(Context context, int Value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt("turnHolder", Value).apply();
    }

    public static int getTurnplayer(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("turnHolder", 1);
    }

    public static void setTurnType(Context context, int Value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt("turnType", Value).apply();
    }

    public static int getTurnType(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("turnType", 1);
    }

    public static void setGameScores(Context context, int Value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt("gameScores", Value).apply();
    }

    public static int getGameScores(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("gameScores", 0);
    }

    public static void setMaxGameScores(Context context, int Value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt("maxGameScores", Value).apply();
    }

    public static int getMaxGameScores(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("maxGameScores", 0);
    }

    public static boolean isGamePlayed(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("gamePlayed", false);
    }

    public static void setGamePlayed(Context context, Boolean value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean("gamePlayed", value).apply();
    }

    public static boolean isTodayPractisePaid(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("practisePaid", false);
    }

    public static void setTodayPractisePaid(Context context, Boolean value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean("practisePaid", value).apply();
    }

    public static String getPlayerName(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("gamerName", null);
    }

    public static void setPlayerName(Context context, String Value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString("gamerName", Value).apply();
    }

    public static void setUserId(Context context, String uid) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString("userId", uid).apply();
    }
    public static String getUserId(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("userId", null);
    }


    public static void setNowLanguage(Context context, String uid) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString("language", uid).apply();
    }
    public static String getNowLanguage(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("language", "");
    }


    public static void setGameAllowed(Context context, Boolean allowed) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean("gameAllowed", allowed).apply();
    }
    public static Boolean isGameAllowed(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("gameAllowed", false);
    }
    public static void setTrainAllowed(Context context, Boolean allowed) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean("gametrainAllowed", allowed).apply();
    }
    public static Boolean isTrainAllowed(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("gametrainAllowed", false);
    }


    public static void setDaysWorkedCount(Context context, int count) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt("daysworkedcount", count).apply();
    }
    public static int getDaysWorkedCount(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("daysworkedcount", 0);
    }

}
