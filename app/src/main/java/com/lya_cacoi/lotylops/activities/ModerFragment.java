package com.lya_cacoi.lotylops.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lya_cacoi.lotylops.Databases.FirebaseAccess.FirebaseDatabase;
import com.lya_cacoi.lotylops.Databases.FirebaseAccess.pojo.Course;
import com.lya_cacoi.lotylops.Databases.transportSQL.TransportSQLCourse;
import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.activities.utilities.CardChangable;
import com.lya_cacoi.lotylops.activities.utilities.CommonFragment;
import com.lya_cacoi.lotylops.adapters.UserCardsAdapter;
import com.lya_cacoi.lotylops.cards.VocabularyCard;
import com.lya_cacoi.lotylops.cards.VocabularyCardNet;
import com.lya_cacoi.lotylops.cards.VocabularyCardNetUploader;

import java.util.ArrayList;
import java.util.List;

import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.VOCABULARY_INDEX;


public class ModerFragment extends CommonFragment implements FirebaseDatabase.UserCardAddable, CardChangable {

    private List<VocabularyCardNet> cards;
    private List<String> ids;
    private UserCardsAdapter adapter;
    private int last_index = -1;
    private VocabularyCard card;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_moder_user, container, false);

    }


    private RecyclerView recyclerView;

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final FirebaseDatabase firebaseDatabase = new FirebaseDatabase();
        firebaseDatabase.getUserCards(this);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false));

        view.findViewById(R.id.more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseDatabase.getUserCards(ModerFragment.this);
            }
        });

    }

    @Override
    public void failureAdd(String card_id) {
        Toast.makeText(getContext(), "failure", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void successAdd(String card_id) {
        Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();
        if (last_index < cards.size()){
            FirebaseDatabase db = new FirebaseDatabase();
            db.informUserAboutCardAccepting(cards.get(last_index).getAuthor(), accepted);
            cards.remove(last_index);
            ids.remove(last_index);
            adapter.notifyDataSetChanged();
        }
    }
    private boolean accepted = false;

    @Override
    public void accept(final List<VocabularyCardNet> vocabularyCardNet, final List<String> ids) {
        last_index = -1;
        Toast.makeText(getContext(), "got " + vocabularyCardNet.size(), Toast.LENGTH_SHORT).show();
        cards = vocabularyCardNet;
        adapter = new UserCardsAdapter(getContext(), cards);
        recyclerView.setAdapter(adapter);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        adapter.notifyDataSetChanged();
        this.ids = ids;

        adapter.setAcceptListener(new UserCardsAdapter.BlockListener() {
            @Override
            public void onClick(final int position) {

                AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                final View my_custom_view = getLayoutInflater().inflate(R.layout.alert_course_select, null);
                adb.setView(my_custom_view);
                final AlertDialog ad = adb.create();

                final Spinner spinner = my_custom_view.findViewById(R.id.spinner);
                TransportSQLCourse sqlCourse = TransportSQLCourse.getInstance(getContext(), VOCABULARY_INDEX);
                sqlCourse.openDb();

                final ArrayList<Course> courses = sqlCourse.getAllCources();
                final ArrayList<String> coursesNames = new ArrayList<>();
                sqlCourse.closeDb();

                for (Course course : courses){
                    coursesNames.add(course.getName());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, coursesNames);
                spinner.setAdapter(arrayAdapter);
                my_custom_view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ad.cancel();
                    }
                });

                my_custom_view.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ad.cancel();
                        accepted = true;
                        FirebaseDatabase db = new FirebaseDatabase();
                        cards.get(position).setCourse(courses.get(spinner.getSelectedItemPosition()).getId());

                        Log.i("main", cards.get(position).toString());
                        Log.i("main", ((VocabularyCardNetUploader)cards.get(position)).toString());

                        db.responceOnUserCard(ModerFragment.this, ids.get(position), (VocabularyCardNetUploader)cards.get(position), true);
                        last_index = position;
                    }
                });
                ad.show();

            }
        });
        adapter.setDeclineListener(new UserCardsAdapter.BlockListener() {
            @Override
            public void onClick(int position) {
                last_index = position;
                FirebaseDatabase db = new FirebaseDatabase();
                db.responceOnUserCard(ModerFragment.this, ids.get(position), (VocabularyCardNetUploader)cards.get(position), false);
            }
        });
    }

    @Override
    public void setToCard(VocabularyCard card, int position) {
    }

    @Override
    public VocabularyCard getCard() {
        return null;
    }

    @Override
    public String getTitle() {
        return "модератор";
    }
}
