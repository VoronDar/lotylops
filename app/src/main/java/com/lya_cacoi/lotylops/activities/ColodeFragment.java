package com.lya_cacoi.lotylops.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.activities.utilities.CommonFragment;
import com.lya_cacoi.lotylops.adapters.PracticeTypeAdapter;
import com.lya_cacoi.lotylops.adapters.units.PracticeTestsUnit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ColodeFragment extends CommonFragment {


    private PracticeTypeAdapter adapter;
    private Context context;
    private ArrayList<PracticeTestsUnit> units;
    private int request_code =1, FILE_SELECT_CODE =101;
    private TextView textView;
    private String TAG ="mainactivty";
    public String  actualfilepath="";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_course, container, false);

    }



    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        units = new ArrayList<>();
        //units.add(new PracticeTestsUnit());

        adapter = new PracticeTypeAdapter(units, false);
        adapter.setBlockListener(new PracticeTypeAdapter.BlockListener() {
            @Override
            public void onClick(int position) {
                ////
            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        recyclerView.setAdapter(adapter);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        adapter.notifyDataSetChanged();
    }

    private void downloadFile() {

        StringBuilder allText = null;
        BufferedReader reader = null;
        try {
            Log.i("main", "prepare");
            reader = new BufferedReader(
                    new InputStreamReader(mainPlain.activity.getAssets().open("import.txt")));

            Log.i("main", "start_read");
            // do reading, usually loop until end of file reading
            String mLine;
            allText = new StringBuilder();
            while ((mLine = reader.readLine()) != null) {
                allText.append(mLine);
            }


        } catch (IOException e) {
            //log the exception
            return;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                    if (allText != null)
                        readImportFile(allText);
                    else
                        Log.i("main", "errorFileRead");
                        // do something
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
    }


        private void readImportFile(StringBuilder allText){
        /*
        ArrayList<VocabularyCard> cards = new ArrayList<>();
        String Id = null;
        int availableId;
        final String importChange = "c";
        final String importAsk = "a";
        final String importUpdate = "u";
        final String importLeave = "l";
        String importType = importAsk;
        Log.i("main", "startAnalyzeFile");
            try {
                Stack<Character> stack = new Stack<>();
                StringBuilder block = new StringBuilder("");
                StringBuilder indicate = new StringBuilder("");
                VocabularyCard card = new VocabularyCard();

                final String id = "id";
                final String word = "w";
                final String translate = "t";
                final String meaning = "m";
                final String meanNative = "mn";
                final String transcript = "tr";
                final String exampleNative = "en";
                final String example = "e";
                final String synonym = "s";
                final String antonym = "a";
                final String group = "g";
                final String part = "pa";
                final String mem = "as";
                final String train = "p";
                final String trainNative = "pn";
                final String help = "h";
                final String levelRemember = "l";
                final String levelPractice = "lp";
                final String dayTrain = "d";



                int count = 1;
                for (Character n : allText.toString().toCharArray()) {
                    System.out.println(n);
                    if (stack.size() > 0 && stack.peek().equals('~')) {
                        if (n.equals('~'))
                            stack.pop();
                        continue;
                    }
                    switch (n) {
                        case '[':
                            //Log.i("main[", "OPEN");
                            if (Id == null && cards.size() == 0)
                                Id = "U";
                            else if (cards.size() == 0)
                                availableId = 1;
                            card = new VocabularyCard();
                        case '{':
                        case '<':
                            stack.add(n);
                            break;
                        case '#':
                            if (stack.size() == 0 || !stack.peek().equals('#'))
                                stack.add(n);
                            else{
                                String bl = block.toString();
                                try {
                                    switch (indicate.toString()) {
                                        case "":
                                            Id = block.toString();
                                            break;
                                        case word:
                                            card.setWord(bl);
                                            break;
                                        case translate:
                                            card.setTranslate(bl);
                                            break;
                                        case transcript:
                                            card.setTranscription(bl);
                                            break;
                                        case meaning:
                                            card.setMeaning(bl);
                                            break;
                                        case meanNative:
                                            card.setMeaningNative(bl);
                                            break;
                                        //case example:
                                        //    card.setExample(bl);
                                        //    break;
                                        //case exampleNative:
                                        //    card.setExampleNative(bl);
                                        //    break;
                                        //case train:
                                        //    card.setTrain(bl);
                                        //    break;
                                        //case trainNative:
                                        //    card.setTrainNative(bl);
                                        //    break;
                                        case group:
                                            card.setGroup(bl);
                                            break;
                                        case part:
                                            card.setPart(bl);
                                            break;
                                        case help:
                                            card.setHelp(bl);
                                            break;
                                        case mem:
                                            card.setMem(bl);
                                            break;
                                        case antonym:
                                            card.setAntonym(bl);
                                            break;
                                        case synonym:
                                            card.setSynonym(bl);
                                            break;
                                        case levelRemember:
                                            card.setRepeatlevel(Integer.parseInt(bl));
                                            break;
                                        case levelPractice:
                                            card.setPracticeLevel(Integer.parseInt(bl));
                                            break;
                                        case dayTrain:
                                            card.setRepetitionDat(Integer.parseInt(bl));
                                            break;
                                        case id:
                                            card.setId(Id + bl);
                                            break;
                                        default:
                                            throw new Error("Indefined index - " + indicate.toString());
                                    }
                                }
                                catch (NumberFormatException e){
                                    throw new Error("You have to use int-> " + bl);
                                }
                                block = new StringBuilder();
                                indicate = new StringBuilder();
                                stack.pop();
                            }
                            break;
                        case ']':
                            if (stack.peek().equals('[')) {
                                //Log.i("main", "endWord");
                                stack.pop();
                                if (card.getId() == null) {
                                    card.setId(Id + availableId++);
                                }
                                cards.add(card);
                                Log.i("mainRead", Integer.toString(count++));
                                Log.i("mainRead", card.getId());
                                card = new VocabularyCard();
                                //Log.i("main-card", "newCard");
                            } else {
                                throw new Error("[");
                            }
                            break;
                        case '>':
                            if (stack.peek().equals('<')) {
                                //Log.i("main", "endCourse");
                                stack.pop();
                            } else
                                throw new Error("couldn't find simbol '<'");
                            break;
                        case '~':
                            if (stack.size() == 0)
                                stack.add('~');
                            else
                                throw new Error("couldn't find simbol '~");
                            break;
                        case '}':
                            if (stack.peek().equals('{')) {
                                    String bl = block.toString();
                                    if (bl.equals(importAsk) || bl.equals(importChange) ||
                                            bl.equals(importLeave))
                                    importType = block.toString();
                                block = new StringBuilder("");
                                stack.pop();
                            } else
                                throw new Error("{");
                            break;
                        case ' ':
                            if (!stack.peek().equals('#'))
                                break;
                        default:
                            if (stack.peek().equals('#') || stack.peek().equals('{'))
                                block.append(n);
                            else if (!stack.peek().equals('~'))
                                indicate.append(n);
                    }
                }
            } catch (Error e) {
                Log.w("main", e.getMessage());
                // do something
                //return;
            } catch (Exception e){
                Log.w("main", e.getCause());
            }
            finally {

                Log.i("main", "end");

                ArrayList<String> allId = transportSql.getAllCardsId();
                int count = 1;
                for (int i = 0; i < cards.size(); i++){
                    Log.i("mainProgress", Integer.toString(count++));
                    for (String n : allId) {
                        //Log.i("main-this_CARD", cards.get(i).getId());
                        if (n.equals(cards.get(i).getId())) {
                            //Log.i("main", "there is simular Id - " + n);
                            if (importType.equals(importLeave)){
                                cards.remove(i--);
                                break;
                            }
                            else if (importType.equals(importChange)){
                                VocabularyCard card = (VocabularyCard) transportSql.getString(n);
                                if (card.equals(cards.get(i))) {
                                    cards.remove(i--);
                                    break;
                                }
                                if (!card.getWord().equals(cards.get(i).getWord())){
                                    ArrayList<Card> allSim = transportSql.getAllCardsByWord(cards.get(i).getWord());
                                    if (allSim.size() > 0){
                                        //Log.i("main", "there is several words - what's to do");
                                        // do dmt;
                                    }
                                }
                                transportSql.deleteStringCommon(n);
                                transportSql.addString(cards.get(i));
                                //Log.i("main", "change card - " + n);
                                cards.remove(i--);
                                break;
                            }
                            else if (importType.equals(importUpdate)){
                                VocabularyCard card = (VocabularyCard) transportSql.getString(n);
                                if (card.equals(cards.get(i))) {
                                    cards.remove(i--);
                                    break;
                                }
                                if (!card.getWord().equals(cards.get(i).getWord())) {
                                    ArrayList<Card> allSim = transportSql.getAllCardsByWord(cards.get(i).getWord());
                                    if (allSim.size() > 0) {
                                        //Log.i("main", "there is several words - what's to do");
                                        // do dmt;
                                    }
                                }
                                transportSql.deleteStringCommon(n);
                                transportSql.addString(cards.get(i));
                                cards.remove(i--);
                                //Log.i("main", "change card - " + n);
                                break;
                            }
                            else{
                                //Log.i("main", "act - ");
                            }
                        }
                    }
                }

                for (int i = 0; i < cards.size(); i++){
                    ArrayList<Card> allSim = transportSql.getAllCardsByWord(cards.get(i).getWord());
                    if (allSim.size() > 0)
                    {
                        //Log.i("main", "new");
                    }
                    else
                        transportSql.addString(cards.get(i));
                }



                transportSql.closeDatabases();
                Log.i("mainEnd", "END");
                // LATER


                //for (int i = 0; i < cards.size(); i++){
                //    for (String n : allWords) {
                //        if (n.equals(cards.get(i).getWord())) {
                //            Log.i("main", cards.get(i).getWord());
                //        }
                //    }
                //}


            }
            */
        }


    private void openUserFile(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // start runtime permission
            Boolean hasPermission =( ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED);
            if (!hasPermission){
                Log.e(TAG, "get permision   ");
                ActivityCompat.requestPermissions( mainPlain.activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, request_code);
            }else {
                Log.e(TAG, "get permision-- already granted ");
                showFileChooser();
            }
        }else {
            //readfile();
            showFileChooser();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:{
                if (grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    //readfile();
                    showFileChooser();
                }else {
                    // show a msg to user
                }
            }
        }
    }
    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(  Intent.createChooser(intent, "Select a File to Upload"),  FILE_SELECT_CODE);
        } catch (Exception e) {
            Log.e(TAG, " choose file error "+e.toString());
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Log.e(TAG, " result is "+ data + "  uri  "+ data.getData()+ " auth "+ data.getData().getAuthority()+ " path "+ data.getData().getPath());
        String fullerror ="";
        if (requestCode == FILE_SELECT_CODE){
            if (resultCode == mainPlain.RESULT_OK){
                try {
                    Uri imageuri = data.getData();
                    InputStream stream = null;
                    String tempID= "", id ="";
                    Uri uri = data.getData();
                    Log.e(TAG, "file auth is "+uri.getAuthority());
                    fullerror = fullerror +"file auth is "+uri.getAuthority();
                    if (imageuri.getAuthority().equals("media")){
                        tempID =   imageuri.toString();
                        tempID = tempID.substring(tempID.lastIndexOf("/")+1);
                        id = tempID;
                        Uri contenturi = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        String selector = MediaStore.Images.Media._ID+"=?";
                        actualfilepath = getColunmData( contenturi, selector, new String[]{id}  );
                    }else if (imageuri.getAuthority().equals("com.android.providers.media.documents")){
                        tempID = DocumentsContract.getDocumentId(imageuri);
                        String[] split = tempID.split(":");
                        String type = split[0];
                        id = split[1];
                        Uri contenturi = null;
                        if (type.equals("image")){
                            contenturi = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        }else if (type.equals("video")){
                            contenturi = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        }else if (type.equals("audio")){
                            contenturi = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        }
                        String selector = "_id=?";
                        actualfilepath = getColunmData( contenturi, selector, new String[]{id}  );
                    } else if (imageuri.getAuthority().equals("com.android.providers.downloads.documents")){
                        tempID =   imageuri.toString();
                        tempID = tempID.substring(tempID.lastIndexOf("/")+1);
                        id = tempID;
                        Uri contenturi = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                        // String selector = MediaStore.Images.Media._ID+"=?";
                        actualfilepath = getColunmData( contenturi, null, null  );


                    }else if (imageuri.getAuthority().equals("com.android.externalstorage.documents")){
                        tempID = DocumentsContract.getDocumentId(imageuri);
                        String[] split = tempID.split(":");
                        String type = split[0];
                        id = split[1];
                        Uri contenturi = null;
                        if (type.equals("primary")){
                            actualfilepath=  Environment.getExternalStorageDirectory()+"/"+id;
                        }
                    }
                    File myFile = new File(actualfilepath);
                    // MessageDialog dialog = new MessageDialog(Home.this, " file details --"+actualfilepath+"\n---"+ uri.getPath() );
                    // dialog.displayMessageShow();
                    String temppath =  uri.getPath();
                    if (temppath.contains("//")){
                        temppath = temppath.substring(temppath.indexOf("//")+1);
                    }
                    Log.e(TAG, " temppath is "+ temppath);
                    fullerror = fullerror +"\n"+" file details -  "+actualfilepath+"\n --"+ uri.getPath()+"\n--"+temppath;
                    if ( actualfilepath.equals("") || actualfilepath.equals(" ")) {
                        myFile = new File(temppath);
                    }else {
                        myFile = new File(actualfilepath);
                    }
                    Log.e(TAG, " myfile is "+ myFile.getAbsolutePath());
                    readfile(myFile);
                } catch (Exception e) {
                    Log.e(TAG, " read errro "+ e.toString());
                }
            }
        }
    }
    public String getColunmData( Uri uri, String selection, String[] selectarg){
        String filepath ="";
        Cursor cursor = null;
        String colunm = "_data";
        String[] projection = {colunm};
        cursor =  mainPlain.activity.getContentResolver().query( uri, projection, selection, selectarg, null);
        if (cursor!= null){
            cursor.moveToFirst();
            Log.e(TAG, " file path is "+  cursor.getString(cursor.getColumnIndex(colunm)));
            filepath = cursor.getString(cursor.getColumnIndex(colunm));
        }
        if (cursor!= null)
            cursor.close();
        return  filepath;
    }
    public void readfile(File file){
        // File file = new File(Environment.getExternalStorageDirectory(), "mytextfile.txt");
        StringBuilder builder = new StringBuilder();
        Log.e("main", "read start");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine())!=null){
                builder.append(line);
            }
            br.close();

            readImportFile(builder);

        }catch (Exception e){
            Log.e("main", " error is "+e.toString());
        }
        //Log.e("main", " read text is "+ builder.toString());
    }

    @Override
    public String getTitle() {
        return "курсы";
    }
}
