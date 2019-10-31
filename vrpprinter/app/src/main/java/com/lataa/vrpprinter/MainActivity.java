package com.lataa.vrpprinter;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private static final int READ_REQUEST_CODE = 0001;
    private static final String TAG = "Main activity";
    EditText fileNameInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fileNameInput =(EditText) findViewById(R.id.fileName);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                String filePath = "";
                    filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+fileNameInput.getText().toString()+".pdf";
                   // filePath = Environment.getDataDirectory()+"/Download/"+fileNameInput.getText().toString()+".pdf";
                showFileChooser();
//                getTextFromPdf(filePath);
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // start runtime permission
            Boolean hasPermission =( ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED);
            if (!hasPermission){
                ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
            }
        }
    }
    boolean isKitKat = false;
    String realPath_1,file_1;
    private void showFileChooser() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            isKitKat = true;
            startActivityForResult(Intent.createChooser(intent, "Select file"), 1);
        } else {
            isKitKat = false;
            Intent intent = new Intent();
            intent.setType("*/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select file"), 1);
        }
    }
    @TargetApi(19)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {

            if (data != null && data.getData() != null && resultCode == RESULT_OK) {

                boolean isImageFromGoogleDrive = false;

                Uri uri = data.getData();

                if (isKitKat && DocumentsContract.isDocumentUri(this, uri)) {
                    if ("com.android.externalstorage.documents".equals(uri.getAuthority())) {
                        String docId = DocumentsContract.getDocumentId(uri);
                        String[] split = docId.split(":");
                        String type = split[0];

                        if ("primary".equalsIgnoreCase(type)) {
                            realPath_1 = Environment.getExternalStorageDirectory() + "/" + split[1];
                        } else {
                            Pattern DIR_SEPORATOR = Pattern.compile("/");
                            Set<String> rv = new HashSet<>();
                            String rawExternalStorage = System.getenv("EXTERNAL_STORAGE");
                            String rawSecondaryStoragesStr = System.getenv("SECONDARY_STORAGE");
                            String rawEmulatedStorageTarget = System.getenv("EMULATED_STORAGE_TARGET");
                            if (TextUtils.isEmpty(rawEmulatedStorageTarget)) {
                                if (TextUtils.isEmpty(rawExternalStorage)) {
                                    rv.add("/storage/sdcard0");
                                } else {
                                    rv.add(rawExternalStorage);
                                }
                            } else {
                                String rawUserId;
                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                    rawUserId = "";
                                } else {
                                    String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                                    String[] folders = DIR_SEPORATOR.split(path);
                                    String lastFolder = folders[folders.length - 1];
                                    boolean isDigit = false;
                                    try {
                                        Integer.valueOf(lastFolder);
                                        isDigit = true;
                                    } catch (NumberFormatException ignored) {
                                    }
                                    rawUserId = isDigit ? lastFolder : "";
                                }
                                if (TextUtils.isEmpty(rawUserId)) {
                                    rv.add(rawEmulatedStorageTarget);
                                } else {
                                    rv.add(rawEmulatedStorageTarget + File.separator + rawUserId);
                                }
                            }
                            if (!TextUtils.isEmpty(rawSecondaryStoragesStr)) {
                                String[] rawSecondaryStorages = rawSecondaryStoragesStr.split(File.pathSeparator);
                                Collections.addAll(rv, rawSecondaryStorages);
                            }
                            String[] temp = rv.toArray(new String[rv.size()]);
                            for (int i = 0; i < temp.length; i++) {
                                File tempf = new File(temp[i] + "/" + split[1]);
                                if (tempf.exists()) {
                                    realPath_1 = temp[i] + "/" + split[1];
                                }
                            }
                        }
                    } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                        String id = DocumentsContract.getDocumentId(uri);
                        Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                        Cursor cursor = null;
                        String column = "_data";
                        String[] projection = {column};
                        try {
                            cursor = this.getContentResolver().query(contentUri, projection, null, null,
                                    null);
                            if (cursor != null && cursor.moveToFirst()) {
                                int column_index = cursor.getColumnIndexOrThrow(column);
                                realPath_1 = cursor.getString(column_index);
                            }
                        } finally {
                            if (cursor != null)
                                cursor.close();
                        }
                    } else if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                        String docId = DocumentsContract.getDocumentId(uri);
                        String[] split = docId.split(":");
                        String type = split[0];

                        Uri contentUri = null;
                        if ("image".equals(type)) {
                            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        } else if ("video".equals(type)) {
                            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        } else if ("audio".equals(type)) {
                            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        }

                        String selection = "_id=?";
                        String[] selectionArgs = new String[]{split[1]};

                        Cursor cursor = null;
                        String column = "_data";
                        String[] projection = {column};

                        try {
                            cursor = this.getContentResolver().query(contentUri, projection, selection, selectionArgs, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                int column_index = cursor.getColumnIndexOrThrow(column);
                                realPath_1 = cursor.getString(column_index);
                            }
                        } finally {
                            if (cursor != null)
                                cursor.close();
                        }
                    } else if ("com.google.android.apps.docs.storage".equals(uri.getAuthority())) {
                        isImageFromGoogleDrive = true;
                    }
                } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                    Cursor cursor = null;
                    String column = "_data";
                    String[] projection = {column};

                    try {
                        cursor =this.getContentResolver().query(uri, projection, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            int column_index = cursor.getColumnIndexOrThrow(column);
                            realPath_1 = cursor.getString(column_index);
                        }
                    } finally {
                        if (cursor != null)
                            cursor.close();
                    }
                } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                    realPath_1 = uri.getPath();
                }

                try {
                    Log.d(TAG, "Real Path 1 : " + realPath_1);
                    file_1 = realPath_1.substring(realPath_1.lastIndexOf('/') + 1, realPath_1.length());
                    Log.i("File Name 1 ", file_1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }}
    public void getTextFromPdf(byte[] data){
        try {

            String parsedText="";
            PdfReader reader = new PdfReader(data);
            int n = reader.getNumberOfPages();
            for (int i = 0; i <n ; i++) {
                parsedText   = parsedText+ PdfTextExtractor.getTextFromPage(reader, i+1).trim()+"\n"; //Extracting the content from the different pages
            }
            System.out.println(parsedText);
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
