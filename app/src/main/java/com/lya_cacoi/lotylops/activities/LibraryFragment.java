package com.lya_cacoi.lotylops.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.lya_cacoi.lotylops.Databases.FirebaseAccess.FirebaseDatabase;
import com.lya_cacoi.lotylops.Databases.FirebaseAccess.pojo.Course;
import com.lya_cacoi.lotylops.Databases.Union.DBTransporter;
import com.lya_cacoi.lotylops.Databases.transportSQL.SqlMain;
import com.lya_cacoi.lotylops.Databases.transportSQL.SqlVocaPhrasUnion;
import com.lya_cacoi.lotylops.Databases.transportSQL.TransportSQLCourse;
import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.Reader.ColodeImporter;
import com.lya_cacoi.lotylops.Reader.ColodeInterpier;
import com.lya_cacoi.lotylops.Reader.FileReadable;
import com.lya_cacoi.lotylops.activities.utilities.Backable;
import com.lya_cacoi.lotylops.activities.utilities.Blockable;
import com.lya_cacoi.lotylops.activities.utilities.CommonFragment;
import com.lya_cacoi.lotylops.activities.utilities.FragmentsServer;
import com.lya_cacoi.lotylops.adapters.VocabularyCardLibAdapter;
import com.lya_cacoi.lotylops.adapters.units.VocabularyCardLibUnit;
import com.lya_cacoi.lotylops.cards.Card;
import com.lya_cacoi.lotylops.cards.VocabularyCard;
import com.lya_cacoi.lotylops.cards.VocabularyCardNetUploader;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

import java.util.ArrayList;
import java.util.Stack;

import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.PHRASEOLOGY_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.VOCABULARY_INDEX;
import static com.lya_cacoi.lotylops.Helper.Helper.SHOW_DAY_PLAN;
import static com.lya_cacoi.lotylops.activities.mainPlain.smallWidth;

public class LibraryFragment extends CommonFragment implements FileReadable, FirebaseDatabase.CardsLoadable, Backable, Blockable {

    private ArrayList<VocabularyCardLibUnit> units;
    private ColodeImporter importer;
    private final Course course;
    private AlertDialog alertSendCard;
    public static int index = VOCABULARY_INDEX;
    private SqlMain sqlCard;
    private boolean setEnabled = true;

    public LibraryFragment(Course course) {
        this.course = course;
    }

    public LibraryFragment(Course course, int index) {
        this.course = course;
        LibraryFragment.index = index;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_library, container, false);

    }



    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sqlCard = SqlMain.getInstance(requireContext(), index);
        assert sqlCard != null;
        sqlCard.openDbForUpdate();

        FragmentsServer.setTitle(getTitle());

        mainPlain.activity.selectedFr = this;

        Button action;
        Button add;
        Button load;


        //// SET WIDGET SIZE////////////////////////////////////////////////////////////////////////


        /// УДАЛИ
        action = view.findViewById(R.id.removeCourse);
        add = view.findViewById(R.id.addNewBack);
        load = view.findViewById(R.id.loadNewBack);


        add.setVisibility(View.VISIBLE);
        action.setVisibility(View.VISIBLE);
        //load.setVisibility(View.VISIBLE);
        ////////////////////////////////////////////////////////////////////////////////////////////




        //// FILL VOCABULARY CARDS /////////////////////////////////////////////////////////////////
        final ArrayList<Card> getCards = sqlCard.getAllCardsForLib(course.getId());
        units = new ArrayList<>();
        for (Card n : getCards) {
            units.add(new VocabularyCardLibUnit(((VocabularyCard)n).getWord(),
                    ((VocabularyCard)n).getTranslate(), n.getId(), n.getRepeatlevel(), n.isMine));
        }
        ////////////////////////////////////////////////////////////////////////////////////////////


        //// PREPARE VIEW //////////////////////////////////////////////////////////////////////////
        VocabularyCardLibAdapter adapter = new VocabularyCardLibAdapter(view.getContext(), units, index);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        if ((mainPlain.sizeRatio >= mainPlain.triggerRatio) || mainPlain.sizeWidth < smallWidth)
            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        else
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
                    StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        adapter.notifyDataSetChanged();
        ////////////////////////////////////////////////////////////////////////////////////////////



        //// SET COURSE ACTION /////////////////////////////////////////////////////////////////////
        if (course.isEnabled())
            action.setText("отключить");
        else if (course.isHasAccess())
            action.setText("изучить");
        ////////////////////////////////////////////////////////////////////////////////////////////



        //// SET ACTIONS ///////////////////////////////////////////////////////////////////////////
        adapter.setBlockListener(new VocabularyCardLibAdapter.BlockListener() {
            @Override
            public void onClick(int position) {
                if (setEnabled) {
                    isGoToNew = true;
                    ((mainPlain) requireActivity()).slideFragment(new NewCardFragment(true,
                            units.get(position).getID(), course));
                }
            }

            // отправление своей карты
            @Override
            public void onSendClick(final int position) {

                AlertDialog.Builder adb = new AlertDialog.Builder(LibraryFragment.this.requireContext());
                final View my_custom_view = getLayoutInflater().inflate(R.layout.send_card_to_server_alert, null);

                adb.setView(my_custom_view);
                alertSendCard = adb.create();
                alertSendCard.show();

                my_custom_view.findViewById(R.id.cancel).setOnClickListener(v -> alertSendCard.cancel());
                my_custom_view.findViewById(R.id.add).setOnClickListener(v -> {
                    ArrayList<String> list =  checkCardForSend(getCards.get(position));
                    Log.i("main", "list size " + list.size());
                    if (list.size() == 0) {

                        if (!((SqlVocaPhrasUnion)sqlCard).checkForNear(units.get(position), course.getId()))
                            new FirebaseDatabase().loadCardToServer(LibraryFragment.this, (VocabularyCard) sqlCard.getCard(units.get(position).getID()), requireContext());
                        else
                            failureLoad(true);
                    }else{
                        alertSendCard.cancel();
                        AlertDialog.Builder adb1 = new AlertDialog.Builder(LibraryFragment.this.requireContext());
                        adb1.setCancelable(true);
                        adb1.setPositiveButton("исправить", (dialog, which) -> {
                            isGoToNew = true;
                            ((mainPlain) requireActivity()).slideFragment(new NewCardFragment(true, getCards.get(position).getId(), course));
                        });
                        adb1.setTitle("карту нужно доработать");

                        StringBuilder stringBuilder = new StringBuilder();
                        for (String s: list)
                            stringBuilder.append(s).append(", ");
                        stringBuilder.deleteCharAt(stringBuilder.length()-1);
                        stringBuilder.deleteCharAt(stringBuilder.length()-1);
                        adb1.setMessage("Не заполнены поля " + stringBuilder.toString());
                        alertSendCard = adb1.create();
                        alertSendCard.show();
                    }

                });
}
        });

        add.setOnClickListener(v -> {
            if (setEnabled) {
                isGoToNew = true;
                ((mainPlain) requireActivity()).slideFragment(new NewCardFragment(course));
            }
        });


        action.setOnClickListener(v -> {
            if (setEnabled) {
                Button action1 = (Button) v;
                if (course.isEnabled()) {
                    course.setEnabled(false);
                    v.setEnabled(false);
                    sqlCard.deleteCardsFromProgressByCourse(course.getId());
                    action1.setText("изучить курс");
                    //sqlCard.recountCardCount();
                    sqlCard.getTodayCardsCount();
                } else if (course.isHasAccess()) {
                    v.setEnabled(false);
                    course.setEnabled(true);
                    DBTransporter.pushInOrder(getCards, course, sqlCard);
                    //sqlCard.addCardToProgress(getCards);
                    if (transportPreferences.getHelpStudy(requireContext()) == SHOW_DAY_PLAN) {
                        ((mainPlain) requireActivity()).showHelp();
                    }
                    action1.setText("отключить курс");
                    //sqlCard.recountCardCount();

                    sqlCard.getTodayCardsCount();
                } else if (!course.isAccessibility()) {
                    v.setEnabled(false);
                    course.setHasAccess(true);
                    action1.setText("изучить курс");
                }

                TransportSQLCourse transportSQLCourse = TransportSQLCourse.getInstance(requireContext(), index);
                transportSQLCourse.openDb();
                transportSQLCourse.updateCourse(course);
                transportSQLCourse.closeDb();
                //sqlCard.closeDatabases();
                Toast.makeText(requireContext(), "успешно", Toast.LENGTH_SHORT).show();
                v.setEnabled(true);

            }

        });
        ////////////////////////////////////////////////////////////////////////////////////////////





        //// LOAD FILE /////////////////////////////////////////////////////////////////////////////

        load.setOnClickListener(v -> {
            //importer = new ColodeImporter(LibraryFragment.this);
            //importer.importFile();
            //pickFile();

            final FirebaseFirestore fr = FirebaseFirestore.getInstance();

            ArrayList<VocabularyCard> cards = new ArrayList<>();
            ColodeInterpier.interpier(getFile(), new Stack<>(), new StringBuilder(),
                    new StringBuilder(), new VocabularyCard(), "a", cards, null);


            FirebaseDatabase db = new FirebaseDatabase();
            for (VocabularyCard card: cards) {
                Log.i("main", card.toString());
                db.pushCardsToServer(LibraryFragment.this, card, requireContext());
            }
        });


        ////////////////////////////////////////////////////////////////////////////////////////////
    }



    public void pickFile(){
        AlertDialog.Builder adb = new AlertDialog.Builder(requireContext());
        final View my_custom_view = getLayoutInflater().inflate(R.layout.mem_add_layout, null);
        adb.setOnCancelListener(dialog -> {
        });



        adb.setView(my_custom_view);
        final AlertDialog ad = adb.create();
        my_custom_view.findViewById(R.id.cancel).setOnClickListener(v -> ad.cancel());

        my_custom_view.findViewById(R.id.add).setOnClickListener(v -> {
            //ColodeInterpier.importFromPick(((EditText)my_custom_view.findViewById(R.id.yourAnswer)).getText().toString());
            StringBuilder builder = new StringBuilder(((EditText)my_custom_view.findViewById(R.id.yourAnswer)).getText().toString());
            ColodeInterpier.readImportFile(builder, LibraryFragment.this);
        });

        ad.show();
    }

    private boolean isGoToNew = false;
    @Override
    public void onDestroyView() {
        if (!isGoToNew)
            sqlCard.closeDatabases();
        super.onDestroyView();
    }

    // возвращает null если причин отказать нет
    private ArrayList<String> checkCardForSend(Card getCard){
        ((SqlVocaPhrasUnion)sqlCard).setAllSentences((VocabularyCard) getCard);
        VocabularyCard card = (VocabularyCard) getCard;
        ArrayList<String> problems = new ArrayList<>();
        if (card.getWord() == null || card.getWord().length() < 3) problems.add("слово");
        if (card.getTranslate() == null || card.getTranslate().length() == 0) problems.add("перевод");
        //if (card.getMeaningNative() == null || card.getMeaningNative().length() == 0) problems.add("определение на родном языке");
        //if (card.getMeaning() == null || card.getMeaning().length() == 0) problems.add("определение на английском языке");
        if (card.getPart() == null || card.getPart().length() == 0) problems.add("часть речи");
        if (card.getExamples() == null || card.getExamples().size() == 0) problems.add("пример использования слова");
        if (card.getTrains() == null || card.getTrains().size() == 0) problems.add("предложение для тренировки использования слова");
        return problems;
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
        ((mainPlain) requireActivity()).slideFragment(new LibraryFragment(course));
    }

    @Override
    public void succesLoad() {
        alertSendCard.cancel();
        Toast.makeText(requireContext(), "success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void failureLoad(boolean isThereSameCard) {
        alertSendCard.cancel();
        Toast.makeText(requireContext(), "failure" + ((isThereSameCard)?" because there is the same card already" : ""), Toast.LENGTH_SHORT).show();
        AlertDialog.Builder adb = new AlertDialog.Builder(LibraryFragment.this.requireContext());
        adb.setCancelable(true);
        adb.setPositiveButton("ок", (dialog, which) -> alertSendCard.cancel());
        adb.setMessage("Такое слово уже доступно для изучения всеми");
        alertSendCard = adb.create();
        alertSendCard.show();

    }

    @Override
    public void onPushBackListener() {
        //if (mainPlain.sizeRatio < mainPlain.triggerRatio){
        //    SelectSectionFragment.fr().slide(new LibraryCourseFragment(index));
        //} else
            ((mainPlain)requireActivity()).slideFragment(new LibraryCourseFragment(index));
    }

    @Override
    public void setEnable(Boolean enable) {
            setEnabled = enable;
    }

    @Override
    public String getTitle() {
        return "карты";
    }
    
    private StringBuilder getFile(){
        return new StringBuilder();
    }
}
