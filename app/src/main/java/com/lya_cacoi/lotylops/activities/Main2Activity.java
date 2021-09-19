package com.lya_cacoi.lotylops.activities;

import android.Manifest;
import android.content.ContentUris;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.lya_cacoi.lotylops.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;


public class Main2Activity extends AppCompatActivity {
    private int request_code =1, FILE_SELECT_CODE =101;
    private TextView textView;
    private String TAG ="mainactivty";
    public String  actualfilepath="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // sdcard/myfolder/mytextfile.txt
        //URI  sdcard/media/123242
        textView =(TextView) findViewById(R.id.text2);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // start runtime permission
            Boolean hasPermission =( ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED);
            if (!hasPermission){
                Log.e(TAG, "get permision   ");
                ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, request_code);
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
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(  Intent.createChooser(intent, "Select a File to Upload"),  FILE_SELECT_CODE);
        } catch (Exception e) {
            Log.e(TAG, " choose file error "+e.toString());
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Log.e(TAG, " result is "+ data + "  uri  "+ data.getData()+ " auth "+ data.getData().getAuthority()+ " path "+ data.getData().getPath());
        String fullerror ="";
        if (requestCode == FILE_SELECT_CODE){
            if (resultCode == RESULT_OK){
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
                    //File file = new File(actualfilepath);
                    //Log.e(TAG, " actual file path is "+ actualfilepath + "  name ---"+ file.getName());
//                    File myFile = new File(actualfilepath);
                    Log.e(TAG, " myfile is "+ myFile.getAbsolutePath());
                    readfile(myFile);
                    // lyf path  - /storage/emulated/0/kolektap/04-06-2018_Admin_1528088466207_file.xls
                } catch (Exception e) {
                    Log.e(TAG, " read errro "+ e.toString());
                }
                //------------  /document/primary:kolektap/30-05-2018_Admin_1527671367030_file.xls
            }
        }
    }
    public String getColunmData( Uri uri, String selection, String[] selectarg){
        String filepath ="";
        Cursor cursor = null;
        String colunm = "_data";
        String[] projection = {colunm};
        cursor =  getContentResolver().query( uri, projection, selection, selectarg, null);
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
                builder.append("\n");
            }
            br.close();
        }catch (Exception e){
            Log.e("main", " error is "+e.toString());
        }
        Log.e("main", " read text is "+ builder.toString());
        //textView.setText(builder.toString());
    }
}