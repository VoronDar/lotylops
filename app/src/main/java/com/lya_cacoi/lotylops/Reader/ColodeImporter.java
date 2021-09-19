package com.lya_cacoi.lotylops.Reader;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.activities.mainPlain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

// класс, который открывает файл с колодой и читает его
public class ColodeImporter {
    private FileReadable activity;
    public static final int REQUEST_CODE = 1;
    public static final int FILE_SELECT_CODE =101;
    private String  actualfilepath ="";

    public ColodeImporter(FileReadable context) {
        this.activity = context;
    }

    public void importFile(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // start runtime permission
            boolean hasPermission =( ContextCompat.checkSelfPermission(activity.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED);
            if (!hasPermission){
                ActivityCompat.requestPermissions( mainPlain.activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
            }else {
                showFileChooser();
            }
        }else {
            showFileChooser();
        }
    }


    public void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            activity.startActivityForResult(  Intent.createChooser(intent, "Select a File to Upload"),  FILE_SELECT_CODE);
        } catch (Exception e) {
            activity.showError(activity.getContext().getString(R.string.select_file_error));
        }
    }

    public void openFile(Uri uri){

        try {
            String tempID, id;

            if (uri == null || uri.getAuthority() == null)
                throw new Exception(activity.getContext().getString(R.string.empty_file));

            if (uri.getAuthority().equals("media")){
                Log.i("main", "222");
                tempID =   uri.toString();
                tempID = tempID.substring(tempID.lastIndexOf("/")+1);
                id = tempID;
                Uri contenturi = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                String selector = MediaStore.Images.Media._ID+"=?";
                actualfilepath = getColunmData( contenturi, selector, new String[]{id}  );
            }else if (uri.getAuthority().equals("com.android.providers.media.documents")){

                Log.i("main", "111");
                tempID = DocumentsContract.getDocumentId(uri);
                String[] split = tempID.split(":");
                id = split[1];
                String selector = "_id=?";
                actualfilepath = getColunmData(null, selector, new String[]{id}  );
            } else if (uri.getAuthority().equals("com.android.providers.downloads.documents")){
                Log.i("main", "aaa");
                tempID =   uri.toString();
                tempID = tempID.substring(tempID.lastIndexOf("/")+1);
                id = tempID;
                Uri contenturi = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                actualfilepath = getColunmData( contenturi, null, null  );

                Log.i("main", "mmmm");

            }

            else if (uri.getAuthority().equals("com.android.externalstorage.documents")){
                tempID = DocumentsContract.getDocumentId(uri);
                String[] split = tempID.split(":");
                String type = split[0];
                id = split[1];
                //if (type.equals("primary")){
                    actualfilepath= activity.getContext().getExternalFilesDir(null)+"/"+id;
                    Log.i("main", actualfilepath);
                //}
            } else{
                Log.i("main", "nothing found");
            }
            File myFile;
            String temppath =  uri.getPath();

            if (temppath == null)
                throw new Exception(activity.getContext().getString(R.string.empty_file));

            if (temppath.contains("//")){
                temppath = temppath.substring(temppath.indexOf("//")+1);
            }
            if ( actualfilepath.equals("") || actualfilepath.equals(" ")) {
                myFile = new File(temppath);
            }else {
                myFile = new File(actualfilepath);
            }
            readfile(myFile);
        } catch (Exception e) {
            activity.showError(e.toString());
        }
    }

    private String getColunmData( Uri uri, String selection, String[] selectarg){
        String filepath ="";
        Cursor cursor;
        String colunm = "_data";
        String[] projection = {colunm};
        cursor =  mainPlain.activity.getContentResolver().query( uri, projection, selection, selectarg, null);
        if (cursor!= null){
            cursor.moveToFirst();
            filepath = cursor.getString(cursor.getColumnIndex(colunm));
        }
        if (cursor!= null)
            cursor.close();
        return  filepath;
    }

    private void readfile(File file){
        StringBuilder builder = new StringBuilder();
        Log.e("main", "read start");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine())!=null){
                builder.append(line);
            }
            br.close();

            if (ColodeInterpier.readImportFile(builder, activity))
                activity.update();

        }catch (Exception e){
            Log.i("main", e.toString());
            activity.showError(e.toString());
        }
    }

}
