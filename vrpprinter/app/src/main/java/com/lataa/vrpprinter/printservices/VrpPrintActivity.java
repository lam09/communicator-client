package com.lataa.vrpprinter.printservices;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.lataa.vrpprinter.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class VrpPrintActivity extends AppCompatActivity  {

    private static final String LOG_TAG = "Error";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ticket_layout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // start runtime permission
            Boolean hasPermission =( ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED);
            if (!hasPermission){
                ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // start runtime permission
            Boolean hasPermission =( ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED);
            if (!hasPermission){
                ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            }
        }
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

    /**
     * Prints the contents on the screen to a PDF file,
     * which i then saved in Documents/PDF
     * @param view The clicked view
     */
    public void printPDF(View view){
        if(isExternalStorageWritable()) {
            String filename = getFileName();
            File file = new File(getAlbumStorageDir("PDF"), filename);
            try {
                FileOutputStream outputStream = new FileOutputStream(file);
                createPDF(outputStream);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Checks if external storage is available for read and write
     * @return boolean
     */
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * Returns a name for the file that will be created
     * @return String
     */
    private String getFileName() {
        //TODO: 06/10/2015
        return "file2" + ".pdf";
    }

    /**
     * Creates a PDF document and writes it to external storage using the
     * received FileOutputStream object
     * @param outputStream a FileOutputStream object
     * @throws IOException
     */
    private void createPDF(FileOutputStream outputStream) throws IOException {
        PrintedPdfDocument document = new PrintedPdfDocument(this,
                getPrintAttributes());

        // start a page
        PdfDocument.Page page = document.startPage(1);

        // draw something on the page
        View content = getContentView();
        content.draw(page.getCanvas());

        // finish the page
        document.finishPage(page);
        //. . .
        // add more pages
        //. . .
        // write the document content
        document.writeTo(outputStream);

        //close the document
        document.close();
    }


    private View getContentView() {
        return findViewById(R.id.ticket_layout);
    }

    private PrintAttributes getPrintAttributes() {
        PrintAttributes.Builder builder = new PrintAttributes.Builder().setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                .setResolution(new PrintAttributes.Resolution("res1","Resolution",50,50)).setMinMargins(new PrintAttributes.Margins(5, 5, 5, 5));
        PrintAttributes printAttributes = builder.build();
        return printAttributes;
    }


    private File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // start runtime permission
            Boolean hasPermission =( ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED);
            if (!hasPermission){
                ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            }
        }
        //File file = new File(Environment.getExternalStoragePublicDirectory(
        //        Environment.DIRECTORY_DOCUMENTS));
        File file = Environment.getExternalStorageDirectory();
        System.out.println(file.getAbsolutePath());
        //File file = new File(this.getFilesDir(),albumName);
        if (!file.mkdirs()) {
            System.out.println("Directory not created");
        }
        return file;
    }
}
