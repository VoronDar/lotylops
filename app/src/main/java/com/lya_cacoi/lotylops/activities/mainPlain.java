
/*

    Перая запускаемая активность
    Главное меню - содержит фрагменты для плана дня, библиотеки, свободной практики, настроек.

    Иерархия:
    Ruler - определяет что делать дальше и направляет к новой активности
    CardRepeatManager - управляет показом кард (конкретно - repeatDay repeatLevel и practiceLevel)


    базы данных :

    SqlMain - базовая абстракция для карт всех секций
    TransportSqlCourse - для курсов всех секций

    !! SqlVocalPhrasUnion - общая для вокабуляра и фразеологии - но у них еще есть отдельные,
        они от нее наследуются

        сами базы данных

        КУРС - курсы
        СТАТИКА - все что выкладывается на карточках теории
        ПРИМЕРЫ - собственно примеры
        TRAINS - для составления приложений любыми способами
        SELECT_TRAIN - для тестов selectTrain образца для фонетики/грамматики/культуры
        DIALOGUE_TRAIN - для тестов selectTrain образца для вокабуляра/фразеологии - диалоговые задания
            объявление в контракте для грамматики - так сложилось


    активности:

    MainActivity    - карточка
    CardPhraseology - карточка для фразеологии
    testSentence    - тесты
    testSound
    testTranslate
    testTranslateNative
    testWriting
    mainPlain       - главное меню

    фрагменты этой активности содержат постфикс    Fragment
    DayPlanFragment         - фрагмент плана дня
    FreePoolFragment        - фрагмент свободной практики
    LibraryFragment         - фрагмент библиотеки карточек
    SettingsFragment        - фрагмент настройки

    TrasportActivities - содержит статические функции перемещения между активностями
    ActivitiesUtils - общие функции для активностей, которые никак не касаются перемещения между
        активностями

    вспомогательные классы:
    VocabularyCard - карточка карты слова

    классы для передачи данных в адаптеры
    toDoUnit            - в фрагменте DayPlanFragment
    TextBlock           - в активности MainActivity
    ButtonLetterUnit    - для активностей написания слов - для кнопочек
    Им же соотвествуют адаптеры

    transportPreferences - статические функции транспортировки преференсов
    consts - список обозначений


 */




package com.lya_cacoi.lotylops.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.lya_cacoi.lotylops.Databases.LanguagesManager;
import com.lya_cacoi.lotylops.Databases.Union.TransportRunnable;
import com.lya_cacoi.lotylops.Databases.transportSQL.SqlMain;
import com.lya_cacoi.lotylops.Databases.transportSQL.TransportSQLCourse;
import com.lya_cacoi.lotylops.LevelManager.LevelManager;
import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.Shop.Shop;
import com.lya_cacoi.lotylops.activities.statistics.statistics;
import com.lya_cacoi.lotylops.activities.utilities.Backable;
import com.lya_cacoi.lotylops.activities.utilities.CommonFragment;
import com.lya_cacoi.lotylops.activities.utilities.FragmentsServer;
import com.lya_cacoi.lotylops.activities.utilities.panel.ErrorPanel;
import com.lya_cacoi.lotylops.activities.utilities.panel.InfoHoldable;
import com.lya_cacoi.lotylops.activities.utilities.panel.PanelManager;
import com.lya_cacoi.lotylops.activities.utilities.panel.ShopPanel;
import com.lya_cacoi.lotylops.activities.utilities.panel.SimpleInfoPannel;
import com.lya_cacoi.lotylops.activities.utilities.panel.loadPanel;
import com.lya_cacoi.lotylops.declaration.consts;
import com.lya_cacoi.lotylops.ruler.Ruler;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

import java.util.Locale;

import static android.view.View.GONE;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.PHONETIC_INDEX;
import static com.lya_cacoi.lotylops.activities.utilities.ActivitiesUtils.removeTitleBar;
import static com.lya_cacoi.lotylops.activities.utilities.ActivitiesUtils.setOrientation;


public class mainPlain extends AppCompatActivity implements TextToSpeech.OnInitListener, TransportRunnable, InfoHoldable {

    private FragmentManager fragmentManager;
    private CommonFragment fragment;
    public FragmentTransaction ft;
    public static int sizeWidth;
    public static int sizeHeight;
    public static float sizeRatio;
    public static final float triggerRatio = 1.65f;
    public static final float triggerMidRatio = 1.45f;
    public static final int smallWidth = 500;
    public static TextToSpeech repeatTTS;
    public static mainPlain activity;
    public static int multiple;


    @Override
    public void onSomethingGoesWrong(int messageId) {
        Toast.makeText(this, "error " + messageId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUserChangesWillOverWrite() {

    }

    @Override
    public void onSuccess() {
        Toast.makeText(this, "SUCCESS", Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void getMessage(String message) {

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ////// PREPARING ///////////////////////////////////////////////////////////////////////////
        if (activity == null)
            activity = this;

        setKeyboardResize();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_plain);
        setOrientation(this);
        removeTitleBar(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ////////////////////////////////////////////////////////////////////////////////////////////


        repeatTTS = new TextToSpeech(this, this);


        ////// GET SCREEN SIZE /////////////////////////////////////////////////////////////////////
        Point size =  new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(size);
        sizeWidth = size.x;
        sizeHeight = size.y;
        sizeRatio = (float)sizeHeight/(float)sizeWidth;
        if (mainPlain.sizeRatio < mainPlain.triggerRatio) {
            multiple = 2;
            if (mainPlain.sizeHeight < 1300)
                multiple = 1;
        }
        ////////////////////////////////////////////////////////////////////////////////////////////

        //// SET START FRAGMENT ////////////////////////////////////////////////////////////////////
        fragmentManager = getSupportFragmentManager();
        final FragmentTransaction ft = fragmentManager.beginTransaction();

        if (!Ruler.isDailyPlanCompleted(getContext()))
            fragment = new DayPlanFragment();
        else {
            fragment = new ExtraActionsFragment();
            Shop.Thing.addPlan.reload(getContext());
        }
        ft.add(R.id.fragment_container, fragment);
        ft.commit();
        ////////////////////////////////////////////////////////////////////////////////////////////



        //// ICON BUTTONS ///////////////////////////////////////////////////////////////////////////
        BottomNavigationView appBar = findViewById(R.id.bottom_navigation);
        appBar.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        appBar.setItemTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorMenuLabel)));
        appBar.setOnNavigationItemSelectedListener(item -> {
            Log.i("main", "clicked " + item.getItemId());
            // todo задумойся, правильно ли это. Кроме того, нужно сделать что-то подобное для infoPannel
            if (ShopPanel.isOpened) {
                ShopPanel.closeShop(this, (ShopPanel.ShopHoldable) () -> {
                    switch (item.getItemId()) {
                        case R.id.learn:
                            slideFragment(new DayPlanFragment());
                            break;
                        case R.id.library:
                            slideFragment(new SelectSectionFragment());
                            break;
                        case R.id.profile:
                            slideFragment(new HomeFragment());
                            break;
                        case R.id.settings:
                            slideFragment(new SettingsFragment());
                            break;
                        default:
                            break;
                    }
                });
                return true;
            }
            else if (PanelManager.isPanel) {
                loadPanel.closePanel(this);
                SimpleInfoPannel.closePanel(this);
                ErrorPanel.closePanel(this, null);
            }

                switch (item.getItemId()) {
                    case R.id.learn:
                        slideFragment(new DayPlanFragment());
                        break;
                    case R.id.library:
                        slideFragment(new SelectSectionFragment());
                        break;
                    case R.id.profile:
                        slideFragment(new HomeFragment());
                        break;
                    case R.id.settings:
                        slideFragment(new SettingsFragment());
                        break;
                    default:
                        break;
                }
                return true;

        });
        ////////////////////////////////////////////////////////////////////////////////////////////


        findViewById(R.id.fragment_container).setOnLongClickListener(v -> {
            boolean canceled = Ruler.isDailyPlanCompleted(getContext());
            if (!canceled) {
                for (int i = 0; i <= PHONETIC_INDEX; i++) {
                    transportPreferences.setTodayLeftStudyCardQuantity(getContext(), i, 0);
                    transportPreferences.settTodayLeftRepeatCardQuantity(getContext(), i, 0);
                }
            } else {
                setNewDay();
                putOffAction();
            }
            //reload();
            return true;
        });



        //// MODER LONG PRESS //////////////////////////////////////////////////////////////////////
        //findViewById(R.id.auth).setVisibility(View.GONE);
        /*

        dayPlan.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //DBTransporter dbTransporter = new DBTransporter(mainPlain.this);
                //dbTransporter.updater.execute();
                //Ruler.setTodayTables(mainPlain.this);

                StartActivity.setWrongUpdate(getContext());
                Toast.makeText(getContext(), "перезапусти, чтобы обновить", Toast.LENGTH_SHORT).show();

                return false;
            }
        });

        //для тестирования
        library.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                reload();
                Toast.makeText(getContext(), "перезапусти, чтобы обновить", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        game.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                GameManager.endGame(getContext());
                return false;
            }
        });


        //для тестирования

        stat.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                //SqlStatistic stat =  SqlStatistic.getInstance(getContext(), VOCABULARY_INDEX);
                //stat.openDbForWatch();
                // stat.deleteDB();

                if (transportPreferences.getNowLanguage(getContext()).equals(LanguagesManager.ENGLISH))
                    transportPreferences.setNowLanguage(getContext(), LanguagesManager.GERMAN);
                else
                    transportPreferences.setNowLanguage(getContext(), LanguagesManager.ENGLISH);

                return true;
            }
        });


        pool.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                transportPreferences.setHelpStudy(getContext(), 0);
                transportPreferences.setStarted(getContext(), false);
                return true;
            }
        });

         */

        ////////////////////////////////////////////////////////////////////////////////////////////



        //// DAILY ROUTINE /////////////////////////////////////////////////////////////////////////
        Ruler.setTodayTables(this);
        //Ruler.writeStat();
        putOffAction();

        showHelp();
        ////////////////////////////////////////////////////////////////////////////////////////////


        //// INDICATE LANGUAGE /////////////////////////////////////////////////////////////////////
        Animation a = new RotateAnimation(0, 300);
        ImageView indicator = findViewById(R.id.languageIndicator);

        indicator.setVisibility(GONE);

        String nowLanguage = transportPreferences.getNowLanguage(getContext());

        if (nowLanguage.equals(LanguagesManager.ENGLISH))
            indicator.setImageDrawable(getResources().getDrawable(R.drawable.gamecoin));
        else if (nowLanguage.equals(LanguagesManager.GERMAN))
            indicator.setImageDrawable(getResources().getDrawable(R.drawable.gamecoinnone));
        ////////////////////////////////////////////////////////////////////////////////////////////

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onCourseStatChange(int index) {

    }


    interface OnSlideListener{
        void slide();
    }


    @Override
    public View getView(int id) {
        return findViewById(id);
    }

    // поработать тут надо бы
    private void setMyTitle(final String string, final OnSlideListener listener){
        listener.slide();
        if (true) return;
        final int DURATION = 150;
        final TextView title = findViewById(R.id.title);

        Animation animation = new AlphaAnimation(1,0);
        animation.setDuration(DURATION);

        if (string == null) {

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    title.setVisibility(GONE);
                    listener.slide();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            title.startAnimation(animation);
            return;
        }
        findViewById(R.id.title).setVisibility(View.VISIBLE);


        final Animation animation2 = new AlphaAnimation(0,1);
        animation2.setDuration(DURATION);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ((TextView)findViewById(R.id.title)).setText(string);
                title.startAnimation(animation2);
                listener.slide();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        title.startAnimation(animation);

    }


    public void slideFragment(@NonNull Fragment fragment){
        pressTheButton(getFragmentId(fragment));
        if (PanelManager.isPanel) {
            loadPanel.closePanel(this);
            SimpleInfoPannel.closePanel(this);
            ErrorPanel.closePanel(this, null);
        }

        if (fragment.getClass().getSimpleName().equals("DayPlanFragment") && Ruler.isDailyPlanCompleted(getContext())) {
            fragment = new ExtraActionsFragment();
            Shop.Thing.addPlan.reload(getContext());
        }

        this.fragment = (CommonFragment) fragment;
        try {
            selectedFr = (Backable) fragment;
        } catch (ClassCastException e){
            selectedFr = null;
        }

        /*
        setMyTitle(this.fragment.getTitle(), () -> {
            ft = fragmentManager.beginTransaction();
            ft.setCustomAnimations(R.anim.slide_right, R.anim.slide_left);
            ft.replace(R.id.fragment_container, mainPlain.this.fragment);
            ft.commit();
        });

         */


        ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_right, R.anim.slide_left);
        ft.replace(R.id.fragment_container, mainPlain.this.fragment);
        ft.commit();
        FragmentsServer.setFr(this.fragment);
        FragmentsServer.setTitle(this.fragment.getTitle());
    }



    public void slideFragment(@NonNull Fragment fragment, final boolean isLeft){
        pressTheButton(getFragmentId(fragment));
        if (PanelManager.isPanel) {
            loadPanel.closePanel(this);
            SimpleInfoPannel.closePanel(this);
            ErrorPanel.closePanel(this, null);
        }
        this.fragment = (CommonFragment) fragment;
        try {
            selectedFr = (Backable) fragment;
        } catch (ClassCastException e){
            selectedFr = null;
        }

        /*
        setMyTitle(this.fragment.getTitle(), new OnSlideListener() {
            @Override
            public void slide() {
                ft = fragmentManager.beginTransaction();
                if (isLeft)
                    ft.setCustomAnimations(R.anim.slide_right_reverse, R.anim.slide_left_reverse);
                else
                    ft.setCustomAnimations(R.anim.slide_right, R.anim.slide_left);
                ft.replace(R.id.fragment_container, mainPlain.this.fragment);
                ft.commit();
            }
        });

         */

        ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_right, R.anim.slide_left);
        ft.replace(R.id.fragment_container, mainPlain.this.fragment);
        ft.commit();
        FragmentsServer.setFr(this.fragment);
        FragmentsServer.setTitle(this.fragment.getTitle());
    }

    //////// CHANGE ACTIVITY ///////////////////////////////////////////////////////////////////////
    public void goToTheRepeating(int index){
            Ruler ruler = Ruler.getInstance(getApplicationContext(), index);
            ruler.startToStudy();
            Fragment fragment = ruler.changeActivityWhileStudy(this);
            //ruler.closeDatabase();
            slideFragment((CommonFragment) fragment);
    }
    public void goTothePractice(){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment;
        if (Ruler.getPractiseChapter() == consts.SELECT_REPEAT_ALL_COURSE)
            fragment = new PoolCourseFragment();
        else
            fragment = new PracticeSelectPoolFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_right, R.anim.slide_left);
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void goToTheSpecialSettings(Fragment fragment){
        slideFragment(fragment);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////




    //////// TESTING METHODS ///////////////////////////////////////////////////////////////////////
    private void reload() {

        transportPreferences.setStarted(this, false);
        transportPreferences.setUserId(this, null);
        transportPreferences.setExtraDayCount(this, 0);
        transportPreferences.setLastDayComming(this, 0);
        Ruler.regenerateDatabase(this);
        transportPreferences.setDaysWorkedCount(this, 0);


        for (int i = 0; i <= PHONETIC_INDEX; i++) {
            SqlMain sql = SqlMain.getInstance(getApplicationContext(), i);
            sql.openDbForUpdate();
            sql.deleteDb();

            TransportSQLCourse transportSQLCourse = TransportSQLCourse.getInstance(getContext(), i);
            transportSQLCourse.openDb();
            transportSQLCourse.deleteDb();
        }

        StartActivity.setWrongUpdate(getContext());



        //DBTransporter dbTransporter = new DBTransporter(this);
        //dbTransporter.updater.execute();
        //Ruler.setTodayTables(this);
    }
    private void setNewDay()
    {
        transportPreferences.setExtraDayCount(this, transportPreferences.getExtraDayCount(this) + 1);
        Ruler.setTodayTables(this);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////



    //////// STATICS IMPACT ////////////////////////////////////////////////////////////////////////
    private void putOffAction(){
        final statistics stat = statistics.getInstance(this);
        if (!stat.isEmpty()){
            Log.i("main", "bla!");
        final AlertDialog.Builder adb = new AlertDialog.Builder(this);
        View my_custom_view = getLayoutInflater().inflate(R.layout.do_stat, null);
        adb.setView(my_custom_view);

        TextView actionMessage = my_custom_view.findViewById(R.id.message);
        actionMessage.setText(stat.getActionName());

            adb.setOnCancelListener(dialog -> {
                Log.i("main", "here");
                if (!stat.isEmpty())
                    putOffAction();
                else{
                    slideFragment(new DayPlanFragment());
                }
            });

        final AlertDialog ad = adb.create();

        Button cancel = my_custom_view.findViewById(R.id.cancel);
        cancel.setOnClickListener(v -> {
            stat.cancelComand();
            ad.cancel();
        });
        Button accept = my_custom_view.findViewById(R.id.accept);
            accept.setOnClickListener(v -> {
                stat.executeCommand();
                ad.cancel();
            });

            if (sizeRatio < triggerRatio){
                cancel.setTextSize(sizeHeight/(40*multiple));
                accept.setTextSize(sizeHeight/(40*multiple));
                actionMessage.setTextSize(sizeHeight/(30*multiple));
            }

            ad.show();
        }

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    protected void onDestroy() {

        if (repeatTTS != null) {
            repeatTTS.stop();
            repeatTTS.shutdown();
        }
        super.onDestroy();
    }


    ////////// TEXT TO SPEECH //////////////////////////////////////////////////////////////////////
    @Override
    public void onInit(int status) {
        Log.i("text-to-main", "init tts!");
        repeatTTS.setSpeechRate(1);
        repeatTTS.setPitch(0.5f);
        if (status == TextToSpeech.SUCCESS) {
            Log.i("main", "tts success");
            //Locale locale = new Locale("uc");
            int result = repeatTTS.setLanguage(Locale.UK);
            //int result = repeatTTS.setLanguage(locale);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("main", "Извините, этот язык не поддерживается");
            }
            Log.i("main", "tts loaded");
            if (Build.VERSION.SDK_INT <= 21) {
                Log.i("main", "21 api");
                //repeatTTS.speak("activated", TextToSpeech.QUEUE_FLUSH,
                //        null);
            } else{
                Log.i("main", "another api - " + Build.VERSION.SDK_INT);
                /*
                repeatTTS.speak("activated333", TextToSpeech.QUEUE_FLUSH, null, "a");
                repeatTTS.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onStart(String utteranceId) {

                        Log.i("main", "started");
                    }

                    @Override
                    public void onDone(String utteranceId) {

                        Log.i("main", "done");
                    }

                    @Override
                    public void onError(String utteranceId) {

                        Log.i("main", "error");
                    }
                });

                 */
            }

        } else {
            Log.i("main", "tts warning " + status);
            Toast.makeText(this, "TTS - warning " + status, Toast.LENGTH_SHORT).show();
        }
        /*
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> intActivities = packageManager.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (intActivities.size()!= 0){
            Toast.makeText(getContext(), "TTS RECOGNIZE - completed", Toast.LENGTH_SHORT).show();
            listenToSpeech();
        }


         */
    }

        /*
    private void listenToSpeech(){
        Intent listenInetnt =  new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        listenInetnt.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());
        listenInetnt.putExtra(RecognizerIntent.EXTRA_PROMPT, "say a word");
        listenInetnt.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        listenInetnt.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 2);

        startActivityForResult(listenInetnt, VR_REQUEST);
    }
         */
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public static int dp(int dp){
        return dp * ( activity.getContext().getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
    public static int px(int px){
        return px / (activity.getContext().getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }


    public Backable selectedFr;
    public void onBackPressed() {
        if (selectedFr != null)
            selectedFr.onPushBackListener();
    }


    public void showHelp(){
        // состояния помощи -  0 - показать где библиотека

        /*

        int helpState = transportPreferences.getHelpStudy(getApplicationContext());
        if (helpState > 100) return;

        Log.i("main", "SHOW help " + transportPreferences.getHelpStudy(getApplicationContext()));

        View fragentHolder = findViewById(R.id.fragment_container);

        View navigationPanel = findViewById(R.id.panel);


        final View dayPlan =findViewById(R.id.planIcon);
        final View library =findViewById(R.id.libIcon);
        final View pool =findViewById(R.id.poolIcon);
        final View settings =findViewById(R.id.settingsIcon);
        final View game =findViewById(R.id.game);
        final View moder =findViewById(R.id.auth);



        final View tonerFragment = findViewById(R.id.WindowToner);


        findViewById(R.id.help_panel).setVisibility(View.VISIBLE);
        TextView helpText = findViewById(R.id.help_text);
        TextView helpDesc = findViewById(R.id.help_desc);

        View cardHelp = findViewById(R.id.cardHelp);
        switch (helpState){
            case SHOW_LIBRARY:
            case SHOW_LIBRARY_AFTER:
            case SHOW_DAY_PLAN:
            case SHOW_POOL:
            case SHOW_SETTING:
                ConstraintLayout.LayoutParams p = (ConstraintLayout.LayoutParams)cardHelp.getLayoutParams();
                p.bottomMargin = (sizeHeight/10) + dp(40);
                cardHelp.setLayoutParams(p);
                AnimationSet set = new AnimationSet(true);
                Animation alpha = new AlphaAnimation(0, 1);
                Animation translate = new TranslateAnimation(0, 0, dp(50), 0);
                translate.setDuration(200);
                alpha.setDuration(300);
                set.addAnimation(alpha);
                set.addAnimation(translate);
                findViewById(R.id.cardHelp).startAnimation(set);
                ((Blockable)fragment).setEnable(false);

        }

        if ((helpState == SHOW_LIBRARY && !Ruler.isDailyPlanCompleted(getApplicationContext())) ||
            helpState == SHOW_DAY_PLAN){
            helpText.setText("Начните свое обучение");
            helpDesc.setText("Каждый день задания будут пополняться\nВыполняйте свою дневную норму и получайте награду!");


            emphasazeView(dayPlan);
            setFullAlpha(dayPlan);

            //setLowAlpha(pool);
            //setLowAlpha(library);
            //setLowAlpha(settings);
            //setLowAlpha(game);
            //setLowAlpha(findViewById(R.id.auth));
        } else if ((helpState == SHOW_LIBRARY && Ruler.isDailyPlanCompleted(getApplicationContext()))) {
            helpText.setText("Что вы хотите изучать?");
            helpDesc.setText("Выберите нужные разделы и интересные темы");


            emphasazeView(library);
            //setFullAlpha(library);
            //setLowAlpha(pool);
            //setLowAlpha(dayPlan);
            //setLowAlpha(settings);
            //setLowAlpha(game);
            //setLowAlpha(findViewById(R.id.auth));
        }


        switch (helpState){
            case SHOW_LIBRARY_AFTER:
                helpText.setText("Хотите изучить что-то другое?");
                helpDesc.setText("Выберите нужные разделы и интересные темы");


                //setLowAlpha(pool);
                //setLowAlpha(dayPlan);
                //setLowAlpha(settings);
                //setLowAlpha(game);
                //setLowAlpha(findViewById(R.id.auth));
                emphasazeView(library);
                //setFullAlpha(library);
                break;
            case SHOW_POOL:
                helpText.setText("Занимайтесь дополнительно");
                helpDesc.setText("Заходите, если хотите позаниматься еще");

                emphasazeView(pool);
                //setFullAlpha(pool);

                //setLowAlpha(library);
                //setLowAlpha(dayPlan);
                //setLowAlpha(settings);
                //setLowAlpha(findViewById(R.id.auth));
                //setLowAlpha(game);
                break;
            case SHOW_SETTING:
                helpText.setText("Настройте свое обучение");
                helpDesc.setText("Это поможет обучаться комфортнее");
                //setLowAlpha(pool);
                //setLowAlpha(dayPlan);
                //setLowAlpha(library);
                //setLowAlpha(game);
                //setLowAlpha(findViewById(R.id.auth));

                emphasazeView(settings);
                //setFullAlpha(settings);

                break;
                default:
                    break;
        }
        Animation alpha = new AlphaAnimation(0, 0.9f);
        alpha.setDuration(400);
        alpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tonerFragment.setAlpha(0.5f);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        tonerFragment.setVisibility(View.VISIBLE);
        tonerFragment.startAnimation(alpha);



         */
    }

    private void setLowAlpha(final View view){

        Animation alphaButton = new AlphaAnimation(1, 0.1f);
        alphaButton.setDuration(500);
        alphaButton.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setAlpha(0.1f);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        view.startAnimation(alphaButton);
        view.setEnabled(false);
    }
    private void setFullAlpha(final View view){

        view.setAlpha(1);
    }

    private void returnAlpha(final View view){


        if (view.getAlpha() != 1){
            Animation a = new AlphaAnimation(view.getAlpha(), 1);
            a.setDuration(300);
            a.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) { }

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.setAlpha(1);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            view.startAnimation(a);
            view.setEnabled(true);
        }
    }


    private boolean isEmphasaze = true;

    private void emphasazeView(final View view){

        isEmphasaze = true;
        final Animation a = new ScaleAnimation(1, 0.9f, 1f, 0.9f);
        a.setDuration(500);
        final Animation aT = new TranslateAnimation(0, -view.getScaleX()*0.05f, 0, -view.getScaleY()*0.05f);
        aT.setDuration(500);

        final AnimationSet aS = new AnimationSet(true);
        aS.addAnimation(a);
        aS.addAnimation(aT);


        final Animation a2 = new ScaleAnimation(0.9f, 1f, 0.9f, 1f);
        a2.setDuration(500);
        final Animation aT2 = new TranslateAnimation(-view.getScaleX()*0.05f, 0, -view.getScaleY()*0.05f, 0);
        aT2.setDuration(500);

        final AnimationSet aS2 = new AnimationSet(true);
        aS2.addAnimation(a2);
        aS2.addAnimation(aT2);

        aS.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (isEmphasaze == true)
                view.startAnimation(aS2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        aS2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (isEmphasaze == true)
                view.startAnimation(aS);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(aS);
    }

    public void closeHelp(){
        /*
        try {
            ((Blockable) fragment).setEnable(true);
        } catch (ClassCastException e){

        }
        findViewById(R.id.fragment_container).setEnabled(true);
        findViewById(R.id.panel).setEnabled(true);


        final View dayPlan =findViewById(R.id.planIcon);
        final View library =findViewById(R.id.libIcon);
        final View pool =findViewById(R.id.poolIcon);
        final View settings =findViewById(R.id.settingsIcon);
        final View game =findViewById(R.id.game);
        final View tonerFragment =findViewById(R.id.WindowToner);

        //returnAlpha(dayPlan);
        //returnAlpha(library);
        //returnAlpha(pool);
        //returnAlpha(settings);
        //returnAlpha(game);
        //returnAlpha(findViewById(R.id.auth));

        isEmphasaze = false;

        Animation alphaToner = new AlphaAnimation(tonerFragment.getAlpha(), 0);
        alphaToner.setDuration(400);
        alphaToner.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tonerFragment.setVisibility(GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        tonerFragment.startAnimation(alphaToner);



        AnimationSet set = new AnimationSet(true);
        Animation alpha = new AlphaAnimation(1, 0);
        Animation translate = new TranslateAnimation(0, 0, 0, dp(50));
        translate.setDuration(300);
        alpha.setDuration(200);
        set.addAnimation(alpha);
        set.addAnimation(translate);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                findViewById(R.id.help_panel).setVisibility(GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        findViewById(R.id.cardHelp).startAnimation(set);

         */
    }




    public void addMoney(int count) {
        SimpleInfoPannel.openPanel(this, "Поздравляем! Твою карту приняли!", "Ты получаешь" + count + " монеты",
                R.drawable.coin_size);
        LevelManager.addExp(getContext(), LevelManager.Action.acceptCard);

    }

    public void declineCardLoad() {
        SimpleInfoPannel.openPanel(this, "К сожалению, твою карту не приняли.", "Возможно, похожая карта уже доступна к изучению",
                R.drawable.coin_size);
    }

    /**  вызывает просто pressTheButton, но определяет кнопку по id фрагмента*/
    private void pressTheButton(int id){
        /*
        switch (id){
            case 1:
                pressTheButton(findViewById(R.id.planIcon));
                break;
            case 2:
                pressTheButton(findViewById(R.id.poolIcon));
                break;
            case 3:
                pressTheButton(findViewById(R.id.libIcon));
                break;
            case 4:
                pressTheButton(findViewById(R.id.statIcon));
                break;
            case 5:
                pressTheButton(findViewById(R.id.game));
                break;
            case 6:
                pressTheButton(findViewById(R.id.settingsIcon));
                break;
                default:
        }

         */
    }


    private void pressTheButton(final View button){

        newPressTheButton(button);
        if (true) return;

        final float lastAlpha = 0.2f;
        final int duration = 300;


        final Animation a = new AlphaAnimation(1, lastAlpha);
        a.setDuration(duration);

        final Animation a2 = new AlphaAnimation(lastAlpha, 1);
        a2.setDuration(duration);



        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                button.startAnimation(a2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        button.startAnimation(a);
    }

    private void newPressTheButton(final View button){
        final float lastAlpha = 0.6f;

        /*


        if (findViewById(R.id.planIcon).getAlpha() > lastAlpha)
            alphaAnim(findViewById(R.id.planIcon), 1, lastAlpha);
        else if (findViewById(R.id.poolIcon).getAlpha() > lastAlpha)
            alphaAnim(findViewById(R.id.poolIcon), 1, lastAlpha);
        else if (findViewById(R.id.auth).getAlpha() > lastAlpha)
            alphaAnim(findViewById(R.id.auth), 1, lastAlpha);
        else if (findViewById(R.id.game).getAlpha() > lastAlpha)
            alphaAnim(findViewById(R.id.game), 1, lastAlpha);
        else if (findViewById(R.id.libIcon).getAlpha() > lastAlpha)
            alphaAnim(findViewById(R.id.libIcon), 1, lastAlpha);
        else if (findViewById(R.id.settingsIcon).getAlpha() > lastAlpha)
            alphaAnim(findViewById(R.id.settingsIcon), 1, lastAlpha);
        else if (findViewById(R.id.statIcon).getAlpha() > lastAlpha)
            alphaAnim(findViewById(R.id.statIcon), 1, lastAlpha);

        alphaAnim(button, lastAlpha, 1);

         */
    }

    private void alphaAnim(final View view, final float start, final float end){


        final Animation a = new AlphaAnimation(start, end);
        a.setDuration(300);


        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setAlpha(end);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.startAnimation(a);
    }


    private int getFragmentId(Fragment fr){
        if (fr.getClass().getSimpleName().equals("DayPlanFragment")) return 1;
        if (fr.getClass().getSimpleName().equals("PoolFragment")) return 2;
        if (fr.getClass().getSimpleName().equals("PoolCourseFragment")) return 2;
        if (fr.getClass().getSimpleName().equals("PracticeSelectPoolFragment")) return 2;
        if (fr.getClass().getSimpleName().equals("LibraryFragment")) return 3;
        if (fr.getClass().getSimpleName().equals("NewCardFragment")) return 3;
        if (fr.getClass().getSimpleName().equals("LibraryCourseFragment")) return 3;
        if (fr.getClass().getSimpleName().equals("SelectSectionFragment")) return 3;
        if (fr.getClass().getSimpleName().equals("Statistcs")) return 4;
        if (fr.getClass().getSimpleName().equals("GameStartFragment")) return 5;
        if (fr.getClass().getSimpleName().equals("SettingsFragment")) return 6;

        return 0;
    }

    private Fragment getFragmentById(int id){
        switch(id){
            case 7:
            case 1: return new DayPlanFragment();
            case 2: return new PoolFragment();
            case 3: return new SelectSectionFragment();
            case 4: return new StatistcsFragment();
            case 5: return new GameStartFragment();
            case 0:
            case 6: return new SettingsFragment();
        }
        return new DayPlanFragment();
    }

    private void swipeLeft(){
        int id = getFragmentId(fragment);
        if (id != 0)
            slideFragment(getFragmentById(id+1), true);
    }


    private void swipeRight(){
        int id = getFragmentId(fragment);
        if (id != 0)
            slideFragment(getFragmentById(id-1), false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final GestureDetector.SimpleOnGestureListener
            simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {

            float sensitivity = 120;
            if ((e1.getX() - e2.getX()) > sensitivity) {
                swipeLeft();
            } else if ((e2.getX() - e1.getX()) > sensitivity) {
                swipeRight();
            }
            return true;
        }
    };

    GestureDetector gestureDetector = new GestureDetector(getBaseContext(),
            simpleOnGestureListener);

    public void setKeyboardResize() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }
}