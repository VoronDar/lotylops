package com.lya_cacoi.lotylops.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.lya_cacoi.lotylops.Databases.Auth.FirebaseAuthentification;
import com.lya_cacoi.lotylops.Databases.LanguagesManager;
import com.lya_cacoi.lotylops.Databases.Union.DBTransporter;
import com.lya_cacoi.lotylops.Databases.Union.TransportRunnable;
import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.ruler.Ruler;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

import java.util.ArrayList;

import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.CULTURE_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.EXCEPTION_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.GRAMMAR_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.PHONETIC_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.PHRASEOLOGY_INDEX;
import static com.lya_cacoi.lotylops.activities.utilities.ActivitiesUtils.removeTitleBar;
import static com.lya_cacoi.lotylops.activities.utilities.ActivitiesUtils.setOrientation;

public class StartActivity extends AppCompatActivity implements TransportRunnable, FirebaseAuthentification.AuthRegistrable {

    private static final String REFRESH_DAY_PREFERENCE = "update_day";
    private static final String REFRESH_SUCCESS_PREFERENCE = "refresh_success";
    private static final String PREFERENCE_NAME = "transporter";
    private ArrayList<Integer> index_infos = new ArrayList<>(6);
    private DBTransporter dbTransporter;

    private boolean isStopped = false;

    private final int DAYS_WITHOUT_UPDATES = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        transportPreferences.setNowLanguage(getContext(), LanguagesManager.ENGLISH);


        if (getContext().getSharedPreferences(PREFERENCE_NAME, 0).getInt
                (REFRESH_DAY_PREFERENCE, 0) + DAYS_WITHOUT_UPDATES < Ruler.getAbsoluteDay()
             ||
                !getContext().getSharedPreferences(PREFERENCE_NAME, 0).getBoolean
                (REFRESH_SUCCESS_PREFERENCE, true)) {

            Log.i("main", "need to be updated");

            getContext().getSharedPreferences(PREFERENCE_NAME, 0).edit().
                    putBoolean(REFRESH_SUCCESS_PREFERENCE, false).apply();

            getContext().getSharedPreferences(PREFERENCE_NAME, 0).edit().
                    putInt(REFRESH_DAY_PREFERENCE, Ruler.getAbsoluteDay()).apply();
        } else {onSuccess(); return;}


        if (!transportPreferences.isStarted(getApplicationContext())) {
            FirebaseApp.initializeApp(this);
            FirebaseAuthentification.registerAsAnonym(this, this);
        }
        onCompleteListener();


        //SqlStatistic sqlStatistic = SqlStatistic.getInstance(getContext(), VOCABULARY_INDEX);
        //sqlStatistic.openDbForWatch();
        //sqlStatistic.deleteDB();
        //sqlStatistic.closeDatabases();

        /*


        TransportSQLCourse sqlCourse = TransportSQLCourse.getInstance(getContext(), EXCEPTION_INDEX);
        sqlCourse.openDb();
        sqlCourse.AddCourse(new Course("mlt", 2, true, false, 2, true, "????????"));
        sqlCourse.closeDb();
        SqlMain sqlMain = SqlException.getInstance(getContext());
        sqlMain.openDbForUpdate();
        sqlMain.addNewCard(new ExceptionCard("mlt", "mlt1", NOUN_LEVEL, NOUN_LEVEL, 0, "is ,mm", "men"));
        sqlMain.addNewCard(new ExceptionCard("mlt", "mlt2", NOUN_LEVEL, NOUN_LEVEL, 0, "is ww", "women"));
        sqlMain.closeDatabases();

         */
        /*
        TransportSQLCourse sqlCourse = TransportSQLCourse.getInstance(getContext(), CULTURE_INDEX);
        sqlCourse.openDb();
        sqlCourse.AddCourse(new Course("smt", 2, true, false, 2, true, "????????"));
        sqlCourse.closeDb();
        SqlMain sqlMain = SqlMain.getInstance(getContext(), CULTURE_INDEX);
        sqlMain.openDbForUpdate();
        sqlMain.addNewCard(new CultureCard("smt", "smt1", NOUN_LEVEL, NOUN_LEVEL, 0, "asdas", new GrammarCard.SelectTrain("smt1", "is aaa", "aa", "bb", null, null)));
        sqlMain.closeDatabases();


*/


        /*
        TransportSQLCourse sqlCourse = TransportSQLCourse.getInstance(getContext(), PHONETIC_INDEX);
        sqlCourse.openDb();
        sqlCourse.deleteDb();
        sqlCourse.AddCourse(new Course("smt", 2, true, false, 2, true, "?????????????? ??????????"));
        sqlCourse.closeDb();
        SqlMain sqlMain = SqlMain.getInstance(getContext(), PHONETIC_INDEX);
        sqlMain.openDbForUpdate();
        sqlMain.deleteDb();
        PhoneticsCard card = new PhoneticsCard("smt", "smt1", NOUN_LEVEL, NOUN_LEVEL, 0);
        card.setTheory("???????? [??] - ??????????????, ??????????????. ?????????????? ?????????? ???? ?????????????? ???????? [??], ?????????????? ???????????? ?????? ???????????????????????? ???????????????? ???????????? ?????? ??????????\n\n?????? ???????????????????????? ?????????? [??] ?????????? ?????????????? ???????????????????? ???????? ???? ???????????? ?????????? (????????????, ?????? ???? ???????????????????? ???????? ?????? ???????????????????????? ???????????????? ?????????? [??]), ???????????? ???????????????????? ??????, ?????????????? ???????????????????? ???????? ?? ???????????????????? ???????????????? ????????.\n\n?????? ???? ?????????? ?????????? ?????????????? ?????????????? ?? ?????????? [??] ?? ?????????????? ?????????? ???????? - ?????? ?? ?????????????? ???????????????????? ???????????????? ?? ???????????? ?????? ?? ????????");
        card.setTranscription("[??]");
        List<String> list = new ArrayList<>();
        list.add("love");
        list.add("blood");
        list.add("cup");
        list.add("puck");
        card.setWordExamples(list);
        List<String> lis = new ArrayList<>();
        //lis.add("does");
        //lis.add("gun");
        //card.setWordTrains(lis);
        sqlMain.addNewCard(card);


        card = new PhoneticsCard("smt", "smt2", NOUN_LEVEL, NOUN_LEVEL, 0);
        card.setTranscription("[??]");
        card.setTheory("???? ???????????? ???????? [??] ???????? ?????????? ???????????????????? ?????????? ??????????????????:\n\n???????????? o, ?????????? ?????????????? ?????????? m, n, v ?????? th\n\n???????????? u, ?????????? ?????????????? ?????????? ??????????????????\n\n???????????????????? ou");
        list = new ArrayList<>();
        list.add("much");
        list.add("must");
        list.add("duck");
        list.add("country");
        list.add("young");
        list.add("trouble");
        list.add("come");
        card.setWordExamples(list);
        lis = new ArrayList<>();
        lis.add("smt2_0");
        lis.add("smt2_1");
        lis.add("smt2_2");
        card.setSelectTrainsId(lis);
        ArrayList<GrammarCard.SelectTrain> selectTrains = new ArrayList<>();
        selectTrains.add(new GrammarCard.SelectTrain("smt2_0", "???????????????? ??????????, ?? ?????????????? ???????????????????????? ???????? [??]", "study", "aunt", "pull", null));
        selectTrains.add(new GrammarCard.SelectTrain("smt2_1", "???????????????? ??????????, ?? ?????????????? ???????????????????????? ???????? [??]", "luck", "lark", null, null));
        selectTrains.add(new GrammarCard.SelectTrain("smt2_2", "???????????????? ??????????, ?? ?????????????? ???????????????????????? ???????? [??]", "duck", "dark", null, null));
        card.setSelectTrains(selectTrains);
        sqlMain.addNewCard(card);


        card = new PhoneticsCard("smt", "smt3", NOUN_LEVEL, NOUN_LEVEL, 0);
        card.setTranscription("[a:]");
        card.setTheory("???????? [a:] - ??????????????, ????????????.\n\n?????? ???????????????????????? ?????????? [a:] ???????? ???????????????????????? ?????????? ?? ?????????????? ????????????. ???????? ???????????????????????? ??????????");
        list = new ArrayList<>();
        list.add("hard");
        list.add("aunt");
        list.add("garden");
        list.add("after");
        list.add("glass");
        card.setWordExamples(list);
        lis = new ArrayList<>();
        //lis.add("star");
        //lis.add("task");
        //lis.add("father");
        //lis.add("last");
        //lis.add("past");
        //lis.add("pass");
        //card.setWordTrains(lis);
        sqlMain.addNewCard(card);


        card = new PhoneticsCard("smt", "smt4", NOUN_LEVEL, NOUN_LEVEL, 0);
        card.setTranscription("[??]");
        card.setTheory("???? ???????????? ???????? [a:] ???????? ?????????? ????????????????????:\n\n???????????????????? ???????? a + r\n\n???????????? a, ?????????? ?????????????? ?????????? f, nt ?????? th\n\n???????????????????? a + s + ??????????????????");
        list = new ArrayList<>();
        list.add("art");
        list.add("grass");
        list.add("raft");
        list.add("charm");
        list.add("heart");
        card.setWordExamples(list);

        lis = new ArrayList<>();
        lis.add("smt4_0");
        lis.add("smt4_1");
        lis.add("smt4_2");
        lis.add("smt4_3");
        card.setSelectTrainsId(lis);
        selectTrains = new ArrayList<>();
        selectTrains.add(new GrammarCard.SelectTrain("smt4_0", "???????????????? ??????????, ?? ?????????????? ???????????????????????? ???????? [a:]", "bath", "bat", "bad", null));
        selectTrains.add(new GrammarCard.SelectTrain("smt4_1", "???????????????? ??????????, ?? ?????????????? ???????????????????????? ???????? [a:]", "half", "study", "steady", null));
        selectTrains.add(new GrammarCard.SelectTrain("smt4_2", "???????????????? ??????????, ?? ?????????????? ???????????????????????? ???????? [a:]", "dark", "duck", null, null));
        selectTrains.add(new GrammarCard.SelectTrain("smt4_3", "???????????????? ??????????, ?? ?????????????? ???????????????????????? ???????? [a:]", "park", "puck", null, null));
        card.setSelectTrains(selectTrains);
        sqlMain.addNewCard(card);

        card = new PhoneticsCard("smt", "smt5", NOUN_LEVEL, NOUN_LEVEL, 0);
        card.setTranscription("[??]");
        card.setTheory("????????????????????????:");
        list = new ArrayList<>();
        list.add("Mark's car's faster than Bart's car.\n" +
        "Bart's car's smarter than Mark's car.");
        card.setWordExamples(list);
        sqlMain.addNewCard(card);

        sqlMain.closeDatabases();


        sqlCourse = TransportSQLCourse.getInstance(getContext(), CULTURE_INDEX);
        sqlCourse.openDb();
        sqlCourse.deleteDb();
        sqlCourse.AddCourse(new Course("cul", 2, true, false, 2, true, "????????????????"));
        sqlCourse.closeDb();
        sqlMain = SqlMain.getInstance(getContext(), CULTURE_INDEX);
        sqlMain.openDbForUpdate();
        sqlMain.deleteDb();
        CultureCard cCard = new CultureCard("cul", "cul1", NOUN_LEVEL, NOUN_LEVEL, 0,
        "???????? ???????????????????????? - ?????????????????????????????? ???????????????? ????????????????. ???? ???????????????????? ?? ?????????????????? ?????????????? ????????????. ?? 2019 ???????? ?????? ???????? 28 ????????????, ?? 2020 - 26 ????????????. \n\n\n?????????????? ?????????????????? ???????????????????? ?? 17 ???????? - ?? ?????????? ???????????? ???????????????????????? ?? ??????????????. ?????????????????????? ???????????? ???? ?????????????????????????? ???????????????????????? ???????????? ???? ?????????? ??????????????????, ?????????? ?????????????? ???????? ???? ???????????? ???????????????? ?????????????????????? ??????????????????, ????-???? ???????? ???????????? ????????, ?????????????????????? ?? ??????????????, ???????????? ?????????? ?????????? ???????????????? ??????????. ?????????? ???????????? ???????????????? ?????????????? ???????????? ???????????????????????? ???????????????? ??????????????????. ?????????????????? ???????????? ?????? ???????????????? ???????????? ???????? ????????????????????????, ?????? ?????????????????????? ?????????????????????? ???????????????? ???? ???????????? ?? ???????? ???? ????, ?????? ???????????????? ???? ????????????. \n\n???????????????? ???????? ?????????????????????? ???????? ?? 1863 ????????.",
                new GrammarCard.SelectTrain("cul1", "?????????? ?????????????????????? ???????? ????????????????????????", "?????????????????? ?????????????? ????????????", "???????????? ?????????????????????? ??????????????", "26 ????????????", "28 ??????????????"));
        sqlMain.addNewCard(cCard);


        cCard = new CultureCard("cul", "cul2", NOUN_LEVEL, NOUN_LEVEL, 0,
                "???? ?????????? ?????? ???????????????????????? ?????????????? ???????????????????? ???????????????????? ?????????????????????? ?? ?????????????????? ???????? ?????????? ?????????? ?????????????????? ???????????? ?????????????? ?? ???????????? ???????????????????????? ???????????????????? ????-???? ???????????????????? ???????????????????? ????????????????????",
                new GrammarCard.SelectTrain("cul2", "???????????? ???? ?????????????? ?? ???????????? ???????????????????? ???? ?????????? ?????? ????????????????????????", "?????????? ????????????", "?????????????? ???? ????????????", null, null));
        sqlMain.addNewCard(cCard);


        cCard = new CultureCard("cul", "cul3", NOUN_LEVEL, NOUN_LEVEL, 0,
                "???????? ???????????????????????? ?????? ???????????????? ????????????????, ?????? ???????????????? ???????????? ?????????????? ?????????????? ???????????????????? ???????????????????????? ????????. ???????????????? ?????????? ??????: ?????????????? ??????????????, ????????, ???????????????????? ????????, ?????????????????? ??????????, ???????????????????? ????????, ???? ???????????? ?????????????????? ?????????? ?????????? ????????????????????, ?? ?????????? ?????????? ?????????????????????? ?? ????????????",
                new GrammarCard.SelectTrain("cul3", "?????????? ???? ???????? ???? ???????????????? ???????????????????????? ?? ???????? ????????????????????????", "?????????????? ????????????", "???????????????????? ????????", "?????????????????? ??????????", null));
        sqlMain.addNewCard(cCard);

        sqlMain.closeDatabases();


        sqlCourse = TransportSQLCourse.getInstance(getContext(), EXCEPTION_INDEX);
        sqlCourse.openDb();
        sqlCourse.deleteDb();
        sqlCourse.AddCourse(new Course("exc", 2, true, false, 2, true, "?????????????????????????? ??????????"));
        sqlCourse.AddCourse(new Course("2frm", 2, true, false, 2, true, "2 ?????????? ???????????????????????? ????????????????"));
        sqlCourse.AddCourse(new Course("3frm", 2, true, false, 2, true, "3 ?????????? ???????????????????????? ????????????????"));
        sqlCourse.closeDb();
        sqlMain = SqlMain.getInstance(getContext(), EXCEPTION_INDEX);
        sqlMain.openDbForUpdate();
        sqlMain.deleteDb();
        sqlMain.addNewCard(new ExceptionCard("exc", "exc1", NOUN_LEVEL, NOUN_LEVEL, 0, "?????????????????????????? ?????????? ?????????? man", "men"));
        sqlMain.addNewCard(new ExceptionCard("exc", "exc2", NOUN_LEVEL, NOUN_LEVEL, 0, "?????????????????????????? ?????????? ?????????? woman", "women"));
        sqlMain.addNewCard(new ExceptionCard("exc", "exc3", NOUN_LEVEL, NOUN_LEVEL, 0, "?????????????????????????? ?????????? ?????????? child", "children"));

        sqlMain.addNewCard(new ExceptionCard("2frm", "2frm1", NOUN_LEVEL, NOUN_LEVEL, 0, "2 ?????????? ?????????????? break", "broke"));
        sqlMain.addNewCard(new ExceptionCard("2frm", "2frm2", NOUN_LEVEL, NOUN_LEVEL, 0, "2 ?????????? ?????????????? choose", "chose"));
        sqlMain.addNewCard(new ExceptionCard("2frm", "2frm3", NOUN_LEVEL, NOUN_LEVEL, 0, "2 ?????????? ?????????????? come", "came"));

        sqlMain.addNewCard(new ExceptionCard("3frm", "3frm1", NOUN_LEVEL, NOUN_LEVEL, 0, "3 ?????????? ?????????????? break", "broken"));
        sqlMain.addNewCard(new ExceptionCard("3frm", "3frm2", NOUN_LEVEL, NOUN_LEVEL, 0, "3 ?????????? ?????????????? choose", "chosen"));
        sqlMain.addNewCard(new ExceptionCard("3frm", "3frm3", NOUN_LEVEL, NOUN_LEVEL, 0, "3 ?????????? ?????????????? come", "come"));
        sqlMain.closeDatabases();


        sqlCourse = TransportSQLCourse.getInstance(getContext(), VOCABULARY_INDEX);
        sqlCourse.openDb();
        sqlCourse.deleteDb();
        sqlCourse.AddCourse(new Course("voc", 2, true, false, 2, true, "voc"));
        sqlCourse.closeDb();
        sqlMain = SqlMain.getInstance(getContext(), VOCABULARY_INDEX);
        sqlMain.openDbForUpdate();
        sqlMain.deleteDb();
        sqlMain.addNewCard(new VocabularyCard("voc", "voc1", NOUN_LEVEL, NOUN_LEVEL, 0, "milk", null, null, "????????????", null, null, null, null, null, null, null, null, null));
        sqlMain.addNewCard(new VocabularyCard("voc", "voc2", NOUN_LEVEL, NOUN_LEVEL, 0, "cow", null, null, "????????????", null, null, null, null, null, null, null, null, null));
        sqlMain.addNewCard(new VocabularyCard("voc", "voc3", NOUN_LEVEL, NOUN_LEVEL, 0, "tea", null, null, "??????", null, null, null, null, null, null, null, null, null));
        sqlMain.addNewCard(new VocabularyCard("voc", "voc4", NOUN_LEVEL, NOUN_LEVEL, 0, "sea", null, null, "????????", null, null, null, null, null, null, null, null, null));
        sqlMain.addNewCard(new VocabularyCard("voc", "voc5", NOUN_LEVEL, NOUN_LEVEL, 0, "ocean", null, null, "??????????", null, null, null, null, null, null, null, null, null));
        sqlMain.closeDatabases();

        onSuccess();
        if (true) return;

        */

        //transportPreferences.setHelpStudy(getApplicationContext(), 0);
        //transportPreferences.setStarted(getApplicationContext(), false);

        setContentView(R.layout.start_activity);
        setOrientation(this);
        removeTitleBar(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Log.i("main", "start - update");
    }

    @Override
    public void onCompleteListener() {
        if (hasConnection(getContext())) {
            dbTransporter = new DBTransporter(this);
            dbTransporter.updater.execute();

            for (int i = 0; i < 6; i++){
                index_infos.add(0);
            }
        }
        else
            onSuccess();
    }

    @Override
    public void onSomethingGoesWrong(int messageId) {

    }

    @Override
    public void onUserChangesWillOverWrite() {

    }

    @Override
    public void onSuccess() {


        getContext().getSharedPreferences(PREFERENCE_NAME, 0).edit().
                putBoolean(REFRESH_SUCCESS_PREFERENCE, true).apply();

        if (transportPreferences.isStarted(getContext()))
            transportPreferences.setHelpStudy(getContext(), 1000);

        if (isStopped) return;

        Intent intent;
        if (transportPreferences.isStarted(getApplicationContext()))
            intent = new Intent(getApplicationContext(), mainPlain.class);
        else
            intent = new Intent(getApplicationContext(), startAppActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void getMessage(String message) {
        TextView textView = findViewById(R.id.loading_progress);
        textView.setText(message);
    }


    // ?????????????????? view ????????????????
    // ???????????????????? ?????????? 0
    // 0 - ???? ?????????????????????? (???????????????????????? 0.2)
    // 1 - ?????????????????????? ?????????? (???????????????????????? 0.5)
    // 2 - ?????????????????????? ?????????? (???????????????????????? 0.5 ?? ??????????????????)
    // 3 - ?????????????????? (???????????????????????? 1)
    @Override
    public void onCourseStatChange(int index) {
        index_infos.set(index, index_infos.get(index)+1);

        final View view;
        switch (index){
            case PHONETIC_INDEX:
                view = findViewById(R.id.ph_load);
                break;
            case PHRASEOLOGY_INDEX:
                view = findViewById(R.id.phr_load);
                break;
            case CULTURE_INDEX:
                view = findViewById(R.id.cul_load);
                break;
            case GRAMMAR_INDEX:
                view = findViewById(R.id.gr_load);
                break;
            case EXCEPTION_INDEX:
                view = findViewById(R.id.ex_load);
                break;

                default:
                    view = findViewById(R.id.voc_load);
        }


        if (index_infos.get(index) == 1){
            Animation animation = new AlphaAnimation(0.2f, 0.5f);
            animation.setDuration(200);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation){view.setAlpha(0.5f);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            view.startAnimation(animation);
        } else if (index_infos.get(index) == 2){
            Animation animation = new AlphaAnimation(0.5f, 0.9f);
            animation.setDuration(300);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation){
                    Animation animation2 = new AlphaAnimation(0.5f, 0.9f);
                    animation2.setDuration(300);
                    animation2.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            view.startAnimation(animation);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    view.startAnimation(animation2);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            view.startAnimation(animation);
        }else if (index_infos.get(index) == 3){
            Animation animation = new AlphaAnimation(0.5f, 1f);
            animation.setDuration(100);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation){view.setAlpha(1f);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            view.startAnimation(animation);
        }

    }

    public static boolean hasConnection(final Context context)
    {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isStopped = true;
    }

    public static void setWrongUpdate(Context context){
        context.getSharedPreferences(PREFERENCE_NAME, 0).edit().
                putBoolean(REFRESH_SUCCESS_PREFERENCE, false).apply();
    }
}
