package com.lya_cacoi.lotylops.activities.utilities;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.lya_cacoi.lotylops.Databases.FirebaseAccess.pojo.Course;
import com.lya_cacoi.lotylops.Databases.transportSQL.SqlMain;
import com.lya_cacoi.lotylops.Databases.transportSQL.TransportSQLCourse;
import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.cards.Card;
import com.lya_cacoi.lotylops.cards.ExceptionCard;
import com.lya_cacoi.lotylops.cards.PhoneticsCard;
import com.lya_cacoi.lotylops.cards.VocabularyCard;

import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.CULTURE_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.EXCEPTION_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.GRAMMAR_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.PHONETIC_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.PHRASEOLOGY_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.VOCABULARY_INDEX;


public class LinkedLearning{

    /*

    у карт всех секций, кроме культуры и исключений могут быть linked
    это карты и курсы, которые связаны с текущей картой и приложение советует их изучить в случае чего
    при наличии linked приложение проверяет, есть ли объект на изучении, и если нет - советует изучить

    запись - 1 - первая буква секции строчной буквой, - c - курс i - карта, пробел, айдишник
    ei mlt1
    gc pi


    ВАЖНО - ССЫЛКИ НА ОТДЕЛЬНЫЕ КАРТЫ ГРАММАТИКИ И КУЛЬТУРЫ НЕ ДЕЛАТЬ!!

f - phonetics да простят меня боги олимпа!





     */

    private View view; //
    private String linked;
    private Context context;
    private int dbIndex;

    private int linkedSection;
    private boolean isCourseLinked;
    private String idLinked;
    private SqlMain sqlMain;
    private TransportSQLCourse sqlCourse;

    private Card card;
    private Course course;

    public LinkedLearning(View view, String linked, Context context, int index) {
        this.linked = linked;
        this.dbIndex = index;
        this.context = context;


        if (!isLinkedEnable()) return;

        this.view = view;


        if (!isLinkedInActive()) {
            //if (course == null && card == null){
            //    Log.e("main", "the're is no " + idLinked + " in ");
            //    closeDb();
            //    return;
            //}
            Log.i("main", "linked card is not active");
            pushLinkedOnView();
        }
        else{
            Log.i("main", "linked card is already active");
            closeDb();
        }

    }

    private boolean isLinkedEnable(){

        if (linked == null || linked.length() < 4) return false;
        else {

            Log.i("main", "linked is " + linked);

            switch (linked.charAt(0)){
                case 'v': linkedSection = VOCABULARY_INDEX; break;
                case 'p': linkedSection = PHRASEOLOGY_INDEX; break;
                case 'g': linkedSection = GRAMMAR_INDEX; break;
                case 'e': linkedSection = EXCEPTION_INDEX; break;
                case 'c': linkedSection = CULTURE_INDEX; break;
                case 'f': linkedSection = PHONETIC_INDEX; break;
                default: return false;
            }
            switch (linked.charAt(1)){
                case 'c': isCourseLinked = true; break;
                case 'i': isCourseLinked = false; break;
                default: return false;
            }

            if (!linked.contains(" ")) return false;
            idLinked = linked.substring(linked.indexOf(" ")+1);

            Log.i("main", "linked card is found: "
                    + ((isCourseLinked)?"course":"card")
                    + ", index - " + linkedSection
                    + ", id - " + idLinked);

            if (isCourseLinked) {
                sqlCourse = TransportSQLCourse.getInstance(context, linkedSection);
                if (linkedSection != dbIndex)
                    sqlCourse.openDb();
            }
            sqlMain = SqlMain.getInstance(context, linkedSection);
            if (linkedSection != dbIndex)
                sqlMain.openDbForUpdate();
        }

        return true;
    }


    private boolean isLinkedInActive(){
        if (isCourseLinked){
            course = sqlCourse.isCourseInProgress(idLinked);
            return (course != null);
        } else{
            card = sqlMain.isCardInProgress(idLinked);
            return (card != null);
        }
    }

    private void pushLinkedOnView() {


        //// ПОЛУЧЕНИЕ КАРТЫ ///////////////////////////////////////////////////////////////////////
        if (isCourseLinked)
            course = sqlCourse.getCourse(idLinked);
        else
            card = sqlMain.getCard(idLinked);

        if (course == null && isCourseLinked){
                Log.e("main", "the're is no " + idLinked + " in " + linkedSection + " section");
                closeDb();
                return;
        }

        if (card == null && isCourseLinked){
            if (course == null && isCourseLinked){
                Log.e("main", "the're is no " + idLinked + " in " + linkedSection + " section");
                closeDb();
                return;
            }

            //Log.e("main", "the're is no " + idLinked + " in " + linkedSection + " section");
            //closeDb();
            return;
        }


        String getName = null;
        if (!isCourseLinked) {
            getName = getName(card);
            if (getName == null) return;
        }
        ////////////////////////////////////////////////////////////////////////////////////////////


        //// ПОКАЗ ИНФЫ /////////////////////////////////////////////////////////////////////////////
        final Button addLinked = view.findViewById(R.id.add_linked);

        if (addLinked == null) {
            Log.e("main", "LINKED CARD, ADD LINKED ISN'T EXIST");
            return;
        }


        addLinked.setVisibility(View.VISIBLE);

        String text = "Изучение " + ((isCourseLinked) ? "курса" : "карты")
                + " '" + ((isCourseLinked) ? course.getName():getName)  + "'"
                + " может быть тебе интересным. " +
                "Изучить?";


        addLinked.setText(text);
        ////////////////////////////////////////////////////////////////////////////////////////////


        //// ДЕЙСТВИЕ //////////////////////////////////////////////////////////////////////////////
        addLinked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLinked.setVisibility(View.GONE);
                if (isCourseLinked) {
                    course.setEnabled(true);
                    sqlMain.addCardToProgress(sqlMain.getAllCardsFromCourse(course.getId()));
                    sqlCourse.updateCourse(course);
                    if (dbIndex != linkedSection) sqlMain.getTodayCardsCount();
                } else {
                    sqlMain.pushInProgress(card);
                    if (dbIndex != linkedSection) sqlMain.getTodayCardsCount();
                }
                closeDb();

            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
    }

    private String getName(Card card){
        switch (linkedSection){
            case VOCABULARY_INDEX:
            case PHRASEOLOGY_INDEX:
                VocabularyCard vCard = (VocabularyCard) card;
                return (vCard.getWord() + " - " +
                        ((vCard.getTranslate() != null)
                                ?vCard.getTranslate():
                                ((vCard.getMeaningNative() != null)
                                        ?vCard.getMeaningNative():vCard.getMeaning())));
            case EXCEPTION_INDEX:
                return ((ExceptionCard)card).getQuestion();
            case PHONETIC_INDEX:
                return "фонетика - " + ((PhoneticsCard)card).getTranscription();
                default:
                    Log.e("main", "YOU'RE NOT ALLOW TO LINK GRAMMAR AND CULTURE CARDS, IMPOSTOR!!");
                    closeDb();
                    return null;


        }
    }

    private void closeDb(){
        if (dbIndex != linkedSection) {
            sqlMain.closeDatabases();
            if (sqlCourse != null) sqlCourse.closeDb();

            sqlMain = sqlMain.getInstance(context, dbIndex);
            sqlMain.openDbForLearn();
        }



    }
}
