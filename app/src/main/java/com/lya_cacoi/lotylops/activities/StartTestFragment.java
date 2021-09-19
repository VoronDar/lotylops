package com.lya_cacoi.lotylops.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.lya_cacoi.lotylops.Databases.FirebaseAccess.pojo.Course;
import com.lya_cacoi.lotylops.Databases.Union.DBTransporter;
import com.lya_cacoi.lotylops.Databases.Union.TransportRunnable;
import com.lya_cacoi.lotylops.Databases.transportSQL.SqlMain;
import com.lya_cacoi.lotylops.Databases.transportSQL.TransportSQLCourse;
import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.adapters.ButtonLetterAdapter;
import com.lya_cacoi.lotylops.adapters.CourseSelectAdapter;
import com.lya_cacoi.lotylops.adapters.GrammarEnterTestAdapter;
import com.lya_cacoi.lotylops.adapters.units.GrammarEnterTest;
import com.lya_cacoi.lotylops.adapters.units.TestUnit;
import com.lya_cacoi.lotylops.adapters.units.buttonLetterUnitTest;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

import java.util.ArrayList;

import static android.view.View.GONE;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.GRAMMAR_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.PHONETIC_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.PHRASEOLOGY_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.VOCABULARY_INDEX;

public class StartTestFragment extends Fragment implements TransportRunnable {

    private ArrayList<TestUnit> buttonLetterUnits;
    private RecyclerView.Adapter<?> blockAdapter;
    private int index = -1;
    private Boolean isClickable = false;        // можно ли кликать кнопки теста
    private int courseEnableCount = 0;



    private void setClickable(boolean isClickable){
        this.isClickable = isClickable;
        if (index == GRAMMAR_INDEX){
            ((GrammarEnterTestAdapter)blockAdapter).setClickable(isClickable);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.setting_test, container, false);
    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*
        здесь также предлагаются на выбор курсы - чууутка попозже сделать таки разделения на базовые и углубленные, но пока что включаются все соответствующие уровню
         */


        final Button accept = view.findViewById(R.id.buttonAccept);
        final Button cancel = view.findViewById(R.id.buttonCancel);
        final TextView settingsName = view.findViewById(R.id.settingsName);
        final TextView askingText = view.findViewById(R.id.askingText);
        final RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        buttonLetterUnits = new ArrayList<>();

        view.findViewById(R.id.test).setEnabled(false);


        view.findViewById(R.id.buttonNo).setOnClickListener(v -> move());
        view.findViewById(R.id.buttonYes).setOnClickListener(v -> {
            if (StartSettingsFragment.settingsIndex == StartSettingsFragment.SettingsIndex.LevelOfPh) {
                view.findViewById(R.id.asking).setEnabled(false);
                StartTimeFragment.isPhonetics = true;

                final TransportSQLCourse courseSql = TransportSQLCourse.getInstance(getContext(), index);
                courseSql.openDb();
                final ArrayList<Course> courses = courseSql.getAllCources();
                final SqlMain sql = SqlMain.getInstance(getContext(), index);
                sql.openDbForUpdate();
                        for (int i = 0; i < courses.size(); i++) {
                            courses.get(i).setEnabled(true);
                            courseSql.updateCourse(courses.get(i));
                            DBTransporter.pushInOrder(sql.getAllCardsForLib(courses.get(i).getId()), courses.get(i), sql);
                        }sql.closeDatabases();
                        courseSql.closeDb();
                move();
            } else{
                accept.setEnabled(true);
                cancel.setEnabled(true);
                recyclerView.setEnabled(true);
                view.findViewById(R.id.test).setEnabled(true);
                Animation a  = new AlphaAnimation(1, 0);
                a.setDuration(400);
                a.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        view.findViewById(R.id.asking).setVisibility(GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                view.findViewById(R.id.asking).startAnimation(a);

            }
            setClickable(true);
        });



        if (StartSettingsFragment.settingsIndex == StartSettingsFragment.SettingsIndex.LevelOfVoc)
            index = VOCABULARY_INDEX;
        else if (StartSettingsFragment.settingsIndex == StartSettingsFragment.SettingsIndex.LevelOfPhr)
            index = PHRASEOLOGY_INDEX;
        else if (StartSettingsFragment.settingsIndex == StartSettingsFragment.SettingsIndex.LevelOfGr)
            index = GRAMMAR_INDEX;
        else
            index = PHONETIC_INDEX;


        switch (index){
            case VOCABULARY_INDEX:


                settingsName.setText(R.string.fragment_title_start_vocabulary);
                askingText.setText(R.string.fragment_desrc_start_vocabulary);

                // A1
                buttonLetterUnits.add(new buttonLetterUnitTest("start", 0));
                buttonLetterUnits.add(new buttonLetterUnitTest("want", 0));
                buttonLetterUnits.add(new buttonLetterUnitTest("late", 0));
                buttonLetterUnits.add(new buttonLetterUnitTest("people", 0));

                // A2
                buttonLetterUnits.add(new buttonLetterUnitTest("break", 1));
                buttonLetterUnits.add(new buttonLetterUnitTest("another", 1));
                buttonLetterUnits.add(new buttonLetterUnitTest("activity", 1));
                buttonLetterUnits.add(new buttonLetterUnitTest("late", 1));

                // B1
                buttonLetterUnits.add(new buttonLetterUnitTest("forward", 2));
                buttonLetterUnits.add(new buttonLetterUnitTest("develop", 2));
                buttonLetterUnits.add(new buttonLetterUnitTest("condition", 2));
                buttonLetterUnits.add(new buttonLetterUnitTest("particular", 2));


                // B2 до (2500)
                buttonLetterUnits.add(new buttonLetterUnitTest("admire", 3));
                buttonLetterUnits.add(new buttonLetterUnitTest("preference", 3));
                buttonLetterUnits.add(new buttonLetterUnitTest("rapid", 3));
                buttonLetterUnits.add(new buttonLetterUnitTest("wealth", 3));

                // B2 (до 5000)
                buttonLetterUnits.add(new buttonLetterUnitTest("beneficial", 4));
                buttonLetterUnits.add(new buttonLetterUnitTest("mislead", 4));
                buttonLetterUnits.add(new buttonLetterUnitTest("remedy", 4));
                buttonLetterUnits.add(new buttonLetterUnitTest("rubbish", 4));


                // C1
                buttonLetterUnits.add(new buttonLetterUnitTest("taunt", 5));
                buttonLetterUnits.add(new buttonLetterUnitTest("warp", 5));
                buttonLetterUnits.add(new buttonLetterUnitTest("muddle", 5));
                buttonLetterUnits.add(new buttonLetterUnitTest("outburst", 5));


                // C2
                buttonLetterUnits.add(new buttonLetterUnitTest("furl", 6));
                buttonLetterUnits.add(new buttonLetterUnitTest("streaky", 6));
                buttonLetterUnits.add(new buttonLetterUnitTest("happenstance", 6));
                buttonLetterUnits.add(new buttonLetterUnitTest("senility", 6));

                break;
            case PHRASEOLOGY_INDEX:


                settingsName.setText(R.string.fragment_title_start_phraseology);
                askingText.setText(R.string.fragment_desrc_start_phraseology);

                // грубо говоря, делится на 4 уровня - 1 фразы и базовые вещи. 2 - фразовые глаголы, 3 - разговорные конструкции, 4 - идиомы

                buttonLetterUnits.add(new buttonLetterUnitTest("how are you?", 0));
                buttonLetterUnits.add(new buttonLetterUnitTest("excuse me", 0));
                buttonLetterUnits.add(new buttonLetterUnitTest("i am sorry", 0));
                buttonLetterUnits.add(new buttonLetterUnitTest("good morning", 0));

                // A2
                buttonLetterUnits.add(new buttonLetterUnitTest("come on", 1));
                buttonLetterUnits.add(new buttonLetterUnitTest("go away", 1));
                buttonLetterUnits.add(new buttonLetterUnitTest("see you later", 1));
                buttonLetterUnits.add(new buttonLetterUnitTest("you are welcome", 1));

                // разговорные
                buttonLetterUnits.add(new buttonLetterUnitTest("know better", 2));
                buttonLetterUnits.add(new buttonLetterUnitTest("on the other hand", 2));
                buttonLetterUnits.add(new buttonLetterUnitTest("by the way", 2));
                buttonLetterUnits.add(new buttonLetterUnitTest("it seems to me", 2));

                // идиомы
                buttonLetterUnits.add(new buttonLetterUnitTest("face like thunder", 3));
                buttonLetterUnits.add(new buttonLetterUnitTest("at all cost", 3));
                buttonLetterUnits.add(new buttonLetterUnitTest("in broad daylight", 3));
                buttonLetterUnits.add(new buttonLetterUnitTest("catch unawares", 3));

                break;
            case GRAMMAR_INDEX:

                settingsName.setText(R.string.fragment_title_start_grammar);
                askingText.setText(R.string.fragment_desrc_start_grammar);

                buttonLetterUnits.add(new GrammarEnterTest("Я буду идти", 1, "I will go", "I will be go", 0));
                buttonLetterUnits.add(new GrammarEnterTest("Она хочет спать", 1, "She wants to sleep", "She is wanting to sleep", 0));


                buttonLetterUnits.add(new GrammarEnterTest("Я читаю сейчас", 2, "I read now", "I'm reading now", 1));
                buttonLetterUnits.add(new GrammarEnterTest("Вчера днем на спала", 1, "She was sleeping last afternoon", "She slept last afternoon", 1));

                buttonLetterUnits.add(new GrammarEnterTest("Я учил английский в прошлом году", 2, "I have learned english last year", "I learned english last year", 2));
                buttonLetterUnits.add(new GrammarEnterTest("Я никогда не был здесь раньше", 1, "I have never been here before", "I never been here before", 2));

                buttonLetterUnits.add(new GrammarEnterTest("Я уже три часа готовлю ужин", 2, "I'm cooking diner for 3 hours", "I have been cooking dinner for 3 hours", 3));
                buttonLetterUnits.add(new GrammarEnterTest("Я все еще играю", 2, "I still have been playing", "I'm still playing", 3));
                break;
            case PHONETIC_INDEX:
                askingText.setText(R.string.fragment_desrc_start_phonetics);
                return;
        }



        if (index != GRAMMAR_INDEX) {
            blockAdapter = new ButtonLetterAdapter(getContext(), buttonLetterUnits);
            ((ButtonLetterAdapter)blockAdapter).setBlockListener(position -> {
                if (!isClickable) return;
                ((buttonLetterUnitTest)buttonLetterUnits.get(position)).setPressed(!((buttonLetterUnitTest)buttonLetterUnits.get(position)).isPressed());
                blockAdapter.notifyItemChanged(position);
            });

            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        } else {
            blockAdapter = new GrammarEnterTestAdapter(getContext(), buttonLetterUnits);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        }


        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(blockAdapter);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);


        accept.setEnabled(false);
        cancel.setEnabled(false);
        recyclerView.setEnabled(false);

        cancel.setOnClickListener(v -> move());

        accept.setOnClickListener(v -> {


    /*

    надо для курсов поставить тематику

    работаем так - первая, где 2-3 пропуска - ставится для обучения (базовые)
    если везде угадано - ставим последнюю (с погрешностью в 1)
    если угадано везде штуки по две, ставь 4 (саааамые базовые)


    В любом случае, показать колоды, которые будут подключены

    исключения не делаем (НЕ ЗАБУДЬ ПРИСОБАЧИТЬ ИХ К ГРАММАТИКЕ)
    не делаем культуру

    грамматика, как и фонетика - выбрать один из двух правильных вариантов

     */
            accept.setEnabled(false);
            cancel.setEnabled(false);


            switch (index) {
                case VOCABULARY_INDEX:
                    StartTimeFragment.isVocabulary = true;
                    break;
                case PHRASEOLOGY_INDEX:
                    StartTimeFragment.isPhraseology = true;
                    break;
                case GRAMMAR_INDEX:
                    StartTimeFragment.isGrammar = true;
                    break;
            }



                    int nowDifficult = 0;           // нынешняя сложность
        int allKnow = 0;                // всего знает
        int nowKnow = 0;                // знает в этой сложности

        for (TestUnit u : buttonLetterUnits) {
            if (u.getDifficult() > nowDifficult) {
                if (nowKnow <= 2) {
                    transportPreferences.setLevel(getContext(), index, nowDifficult);
                    break;
                }

                nowKnow = 0;
            }
            if (u.isRightPressed()) {
                allKnow++;
                nowKnow++;
            }
            nowDifficult = u.getDifficult();
        }

        if (allKnow >= buttonLetterUnits.size() - 1)
            transportPreferences.setLevel(getContext(), index, (buttonLetterUnits.get(buttonLetterUnits.size()-1).getDifficult()));


            view.findViewById(R.id.colodes).setVisibility(View.VISIBLE);
            view.findViewById(R.id.test).setEnabled(false);


            final TransportSQLCourse courseSql = TransportSQLCourse.getInstance(getContext(), index);
            ArrayList<Course> courses = courseSql.getAllByDifficulty(transportPreferences.getLevel(getContext(), index));

            Log.i("main", "courses size - " + courses.size());


            int i = 0;

            Log.i("main", "level - " + transportPreferences.getLevel(getContext(), index));

            while (courses.size() == 0 && i < 5){
                courses = courseSql.getAllByDifficulty(transportPreferences.getLevel(getContext(), index)+i++);
            }
            Log.i("main", "courses size - " + courses.size());

            i = 0;
            while (courses.size() == 0 && i < 5){
                courses = courseSql.getAllByDifficulty(transportPreferences.getLevel(getContext(), index)-i++);
            }
            Log.i("main", "courses size - " + courses.size());

            final ArrayList<CourseSelectAdapter.Item> array = new ArrayList<>(courses.size());

            for (Course c: courses){
                array.add(new CourseSelectAdapter.Item(c.getName()));
            }

            CourseSelectAdapter courseSelectAdapter = new CourseSelectAdapter(array);
            courseSelectAdapter.notifyDataSetChanged();
            final RecyclerView coursesView = view.findViewById(R.id.courses);
            coursesView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
            coursesView.setNestedScrollingEnabled(false);
            coursesView.setAdapter(courseSelectAdapter);
            coursesView.setOverScrollMode(View.OVER_SCROLL_NEVER);

            view.findViewById(R.id.colodes).setAlpha(0);

            Animation a  = new AlphaAnimation(0, 1);
            a.setDuration(400);
            a.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.findViewById(R.id.colodes).setAlpha(1);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            view.findViewById(R.id.colodes).startAnimation(a);


            final ArrayList<Course> finalCourses = courses;
            view.findViewById(R.id.accept).setOnClickListener(v1 -> {

                v1.setEnabled(false);

                coursesView.setEnabled(false);
                final SqlMain sql = SqlMain.getInstance(getContext(), index);
                sql.openDbForUpdate();

                for (int i1 = 0; i1 < array.size(); i1++) {
                    if (array.get(i1).isSelected()) {
                        courseEnableCount++;
                    }
                }
                if (courseEnableCount == 0) {
                    sql.closeDatabases();
                    TransportSQLCourse.getInstance(getContext(), index).closeDb();
                    move();
                    return;
                }

                for (int i1 = 0; i1 < array.size(); i1++) {
                    if (array.get(i1).isSelected()) {
                        finalCourses.get(i1).setEnabled(true);
                        courseSql.updateCourse(finalCourses.get(i1));
                        //DBTransporter.pushInOrder(sql.getAllCardsForLib(finalCourses.get(i).getId()), finalCourses.get(i), sql);

                        DBTransporter dbTransporter = new DBTransporter(StartTestFragment.this, finalCourses.get(i1).getId(), index);
                        dbTransporter.updater.execute();

                    }
                }
            });



            //move();

        });

    }

    private void move(){
        StartSettingsFragment.settingsIndex = StartSettingsFragment.settingsIndex.getNextIndex();
        if (StartSettingsFragment.settingsIndex == StartSettingsFragment.SettingsIndex.VelocityOfLearning){
            ((startAppActivity) requireActivity()).slideFragment(new StartTimeFragment());
        }
        else if (StartSettingsFragment.settingsIndex != null)
            ((startAppActivity) requireActivity()).slideFragment(new StartTestFragment());
        else {
            ((startAppActivity) requireActivity()).moveToMain();
        }

    }

    @Override
    public void onSomethingGoesWrong(int messageId) {

    }

    @Override
    public void onUserChangesWillOverWrite() {

    }

    @Override
    public void onSuccess() {
        courseEnableCount--;
        if (courseEnableCount <= 0){
            SqlMain.getInstance(getContext(), index).closeDatabases();
            TransportSQLCourse.getInstance(getContext(), index).closeDb();
            move();
        }

    }

    @Override
    public void onCourseStatChange(int index) {

    }

    @Override
    public void getMessage(String message) {

    }
}

