package com.lya_cacoi.lotylops.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lya_cacoi.lotylops.Databases.FirebaseAccess.pojo.Course;
import com.lya_cacoi.lotylops.Databases.Union.DBTransporter;
import com.lya_cacoi.lotylops.Databases.Union.TransportRunnable;
import com.lya_cacoi.lotylops.Databases.transportSQL.SqlMain;
import com.lya_cacoi.lotylops.Databases.transportSQL.SqlVocabulary;
import com.lya_cacoi.lotylops.Databases.transportSQL.TransportSQLCourse;
import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.Reader.ColodeImporter;
import com.lya_cacoi.lotylops.Reader.FileReadable;
import com.lya_cacoi.lotylops.activities.utilities.Backable;
import com.lya_cacoi.lotylops.activities.utilities.Blockable;
import com.lya_cacoi.lotylops.activities.utilities.CommonFragment;
import com.lya_cacoi.lotylops.activities.utilities.panel.InfoHoldable;
import com.lya_cacoi.lotylops.activities.utilities.panel.InfoPannel;
import com.lya_cacoi.lotylops.activities.utilities.panel.loadPanel;
import com.lya_cacoi.lotylops.adapters.CourseLibAdapter;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

import java.util.ArrayList;

import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.PHRASEOLOGY_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.VOCABULARY_INDEX;
import static com.lya_cacoi.lotylops.Helper.Helper.SHOW_DAY_PLAN;
import static com.lya_cacoi.lotylops.activities.StartActivity.hasConnection;
import static com.lya_cacoi.lotylops.activities.mainPlain.activity;
import static com.lya_cacoi.lotylops.activities.mainPlain.sizeRatio;
import static com.lya_cacoi.lotylops.activities.mainPlain.triggerRatio;

public class LibraryCourseFragment extends CommonFragment implements FileReadable, Backable, Blockable, TransportRunnable {

    private ArrayList<Course> units;
    private ColodeImporter importer;
    private final int index;
    private CourseLibAdapter adapter;
    private boolean setEnabled = true;
    private Course nowCourse;
    private boolean isGoToLib = false;

    private boolean isUnionBaseOpened = false;

    public LibraryCourseFragment(int index) {
        this.index = index;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cources, container, false);

    }



    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final TransportSQLCourse sqlCourse = TransportSQLCourse.getInstance(view.getContext(), index);
        sqlCourse.openDb();
        //importer = new ColodeImporter(this);


        //// SET WIDGET SIZE////////////////////////////////////////////////////////////////////////
        //if (sizeRatio < triggerRatio)
            //((TextView) view.findViewById(R.id.cards)).setTextSize(sizeHeight / 45f);
            //view.findViewById(R.id.cards).setVisibility(View.GONE);
        ////////////////////////////////////////////////////////////////////////////////////////////




        //// FILL COURSES //////////////////////////////////////////////////////////////////////////
        units = sqlCourse.getAllCources();
        ////////////////////////////////////////////////////////////////////////////////////////////



        //// PREPARE VIEW //////////////////////////////////////////////////////////////////////////
        adapter = new CourseLibAdapter(view.getContext(), units);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        //if ((sizeRatio >= triggerRatio) || mainPlain.sizeWidth < smallWidth)
            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        //else
        //    recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        adapter.notifyDataSetChanged();
        ////////////////////////////////////////////////////////////////////////////////////////////



        //// SET ACTIONS ///////////////////////////////////////////////////////////////////////////
        adapter.setBlockListener(position -> {
            if ((!(mainPlain.sizeRatio < mainPlain.triggerRatio) && setEnabled )
                    || (mainPlain.sizeRatio < mainPlain.triggerRatio && SelectSectionFragment.setEnable)) {
                if (index == VOCABULARY_INDEX || index == PHRASEOLOGY_INDEX) {

                    if (units.get(position).isEnabled()){
                    //if (mainPlain.sizeRatio < mainPlain.triggerRatio) {
                    //    SelectSectionFragment.fr().slide(new LibraryFragment(
                    //            units.get(position), index));
                    //} else {
                        ((mainPlain) requireActivity()).slideFragment(new LibraryFragment(
                                units.get(position), index));
                        isGoToLib = true;
                        isUnionBaseOpened = true;
                    //}

                    }
                    else{
                        nowCourse = units.get(position);
                        getVocCourse(units.get(position));
                        }
                } else{ getGrammarCourse(units.get(position));
                    nowCourse = units.get(position);
                }
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ColodeImporter.REQUEST_CODE){
                if (grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    importer.showFileChooser();
                }else {
                    Toast.makeText(requireContext(),getString(R.string.permission_blocked), Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ColodeImporter.FILE_SELECT_CODE){
            if (resultCode == mainPlain.RESULT_OK) {
                importer.openFile(data.getData());
            }
        }
    }

    @Override
    public void showError(String string) {
        Toast.makeText(requireContext(), string, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void update() {
        ((mainPlain) requireActivity()).slideFragment(new LibraryCourseFragment(index));
        isGoToLib = true;
    }

    private void getGrammarCourse(final Course course) {
        String title;
        if (course.isEnabled())
            title = "выключить курс " + course.getName();
        else if (course.isHasAccess())
            title = "включить курс " + course.getName();
        else
            title = "получить курс " + course.getName();

        loadPanel.openPanel((InfoHoldable) requireActivity(), title, "Ок", listener -> {
            if (course.isEnabled()) {
                isUnionBaseOpened = true;
                course.setEnabled(false);
                SqlMain sql = SqlMain.getInstance(requireContext(), index);
                sql.openDbForUpdate();
                sql.deleteCardsFromProgressByCourse(course.getId());
                sql.getTodayCardsCount();
                //pulseAnimation(loading);
                TransportSQLCourse transportSQLCourse = TransportSQLCourse.getInstance(requireContext(), index);
                transportSQLCourse.updateCourse(course);
                //transportSQLCourse.closeDb();
                adapter.notifyDataSetChanged();
                listener.onLoaded();
            } else if (course.isHasAccess()) {
                course.setEnabled(true);
                isUnionBaseOpened = true;
                /*
                SqlMain sql = SqlMain.getInstance(requireContext(), index);
                sql.openDbForUpdate();
                DBTransporter.pushInOrder(sql.getAllCardsForLib(course.getId()), course, sql);
                if (transportPreferences.getHelpStudy(requireContext()) == SHOW_DAY_PLAN) {
                    ((mainPlain)requireActivity()).showHelp();
                }
                sql.getTodayCardsCount();

                 */
                TransportSQLCourse transportSQLCourse = TransportSQLCourse.getInstance(requireContext(), index);
                transportSQLCourse.updateCourse(course);
                if (hasConnection(requireContext())) {
                    DBTransporter dbTransporter = new DBTransporter(LibraryCourseFragment.this, course.getId(), index);
                    dbTransporter.updater.execute();
                } else {
                    if (course.getVersion() == 0) {
                        //messageError.setVisibility(View.VISIBLE);
                        //accept.setClickable(true);
                        //cancel.setClickable(true);
                    } else {
                        SqlMain sql = SqlMain.getInstance(requireContext(), index);
                        sql.openDbForUpdate();
                        DBTransporter.pushInOrder(sql.getAllCardsForLib(course.getId()), course, sql);
                        if (transportPreferences.getHelpStudy(requireContext()) == SHOW_DAY_PLAN) {
                            ((mainPlain) requireActivity()).showHelp();
                        }
                        sql.getTodayCardsCount();
                        adapter.notifyDataSetChanged();
                        listener.onLoaded();
                    }
                }

            } else if (!course.isAccessibility()) {
                course.setHasAccess(true);
                TransportSQLCourse transportSQLCourse = TransportSQLCourse.getInstance(requireContext(), index);
                transportSQLCourse.updateCourse(course);
                //transportSQLCourse.closeDb();
                adapter.notifyDataSetChanged();
                listener.onLoaded();
            }
        });
    }




    private void getVocCourse(final Course course){

        String title;
        if (course.isHasAccess())
            title = "включить курс " + course.getName();
        else
            title = "получить курс " + course.getName();

        loadPanel.openPanel((InfoHoldable) activity, title, "Ок", listener -> {
            if (course.isHasAccess()) {
                course.setEnabled(true);
                isUnionBaseOpened = true;



                TransportSQLCourse transportSQLCourse = TransportSQLCourse.getInstance(requireContext(), index);
                transportSQLCourse.updateCourse(course);


                if (hasConnection(requireContext())) {
                    DBTransporter dbTransporter = new DBTransporter(LibraryCourseFragment.this, course.getId(), index);
                    dbTransporter.updater.execute();
                } else{
                    if (course.getVersion() == 0){
                        //messageError.setVisibility(View.VISIBLE);
                        //accept.setClickable(true);
                        //cancel.setClickable(true);
                    } else{
                        SqlMain sql = SqlMain.getInstance(requireContext(), index);
                        sql.openDbForUpdate();
                        DBTransporter.pushInOrder(sql.getAllCardsForLib(course.getId()), course, sql);
                        if (transportPreferences.getHelpStudy(requireContext()) == SHOW_DAY_PLAN) {
                            ((mainPlain)requireActivity()).showHelp();
                        }
                        sql.getTodayCardsCount();
                        adapter.notifyDataSetChanged();
                        loadPanel.forseClose(activity);
                    }
                }

            }
            else if (!course.isAccessibility()) {
                course.setHasAccess(true);

                TransportSQLCourse transportSQLCourse = TransportSQLCourse.getInstance(requireContext(), index);
                transportSQLCourse.updateCourse(course);
                //transportSQLCourse.closeDb();
                adapter.notifyDataSetChanged();
                loadPanel.forseClose(activity);

            }
        });
    }

    @Override
    public void onPushBackListener() {
        if (sizeRatio >= triggerRatio)
        ((mainPlain) requireActivity()).slideFragment(new SelectSectionFragment());
    }

    @Override
    public void setEnable(Boolean enable) {
        setEnabled = enable;
    }


    @Override
    public void onSomethingGoesWrong(int messageId) {
        //ad.cancel();
        Toast.makeText(requireContext(), "Невозможно загрузить данные", Toast.LENGTH_LONG).show();
        loadPanel.forseClose(activity);

    }

    @Override
    public void onUserChangesWillOverWrite() {

    }

    @Override
    public void onSuccess() {

        if (index == VOCABULARY_INDEX && (!transportPreferences.isGameAllowed(requireContext()) || !transportPreferences.isTrainAllowed(requireContext()))) {
            SqlVocabulary sql = SqlVocabulary.getInstance(requireContext());
            sql.getGameAvailable();
        }

        adapter.notifyDataSetChanged();
        if (transportPreferences.getHelpStudy(requireContext()) == SHOW_DAY_PLAN) {
            ((mainPlain)requireActivity()).showHelp();
        }
        loadPanel.forseClose(activity);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (!isGoToLib) {
            if (isUnionBaseOpened)
                SqlMain.getInstance(requireContext(), index).closeDatabases();
            TransportSQLCourse.getInstance(requireContext(), index).closeDb();
        }
    }

    @Override
    public void onCourseStatChange(int index) {

        if (nowCourse.getVersion() == 0){
            Toast.makeText(requireContext(), "курс не может быть загружен", Toast.LENGTH_LONG).show();
        } else{
            SqlMain sql = SqlMain.getInstance(requireContext(), index);
            sql.openDbForUpdate();
            DBTransporter.pushInOrder(sql.getAllCardsForLib(nowCourse.getId()), nowCourse, sql);
            if (transportPreferences.getHelpStudy(requireContext()) == SHOW_DAY_PLAN) {
                ((mainPlain)requireActivity()).showHelp();
            }
            sql.getTodayCardsCount();
            adapter.notifyDataSetChanged();
        }
        loadPanel.forseClose(activity);
    }

    @Override
    public void getMessage(String message) {

    }


    private void pulseAnimation(final View view){
        final Animation a1 = new AlphaAnimation(0.5f,1);
        a1.setDuration(300);
        final Animation a2 = new AlphaAnimation(1,0.5f);
        a2.setDuration(300);
        a1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setAlpha(1);
                view.startAnimation(a2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        a2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                view.setAlpha(0.5f);
                view.startAnimation(a1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.startAnimation(a1);
    }

    @Override
    public String getTitle() {
        return "курсы";
    }
}
