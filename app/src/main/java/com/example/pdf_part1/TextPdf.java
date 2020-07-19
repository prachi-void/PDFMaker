package com.example.pdf_part1;


import android.Manifest;
import android.content.Context;
import android.os.Bundle;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.itextpdf.text.Document;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class TextPdf extends AppCompatActivity {

    Button btnCreate;
    EditText editText;
    MainActivity foo=new MainActivity();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_text_pdf);

        btnCreate = (Button)findViewById(R.id.create);
        editText =(EditText) findViewById(R.id.edittext);


                btnCreate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        createPdf();
                    }
                });

    }

    public void createPdf() {
        Document pdfDoc= new Document() ;
        Calendar cal= Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        File root =new File(Environment.getExternalStorageDirectory(),"pdfFolder");
        if (!root.exists()) {
            root.mkdir();
        }
        File file = new File(root, df.format(cal.getTime())+".pdf");
        try {
            PdfWriter.getInstance(pdfDoc,new FileOutputStream(file));
            pdfDoc.open();
            String text=editText.getText().toString();
            pdfDoc.add(new Paragraph(text));
            pdfDoc.close();
           // showToast(TextPdf.this,"pdf is saved "+file.getAbsolutePath());
            Toast.makeText(TextPdf.this,"pdf is saved.."+file.getAbsolutePath(),Toast.LENGTH_SHORT).show();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    void  requestMultiplePermissions () {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted by user!"
                                    , Toast.LENGTH_SHORT).show();
                        }
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            Toast.makeText(getApplicationContext(), "Permission Denied...", Toast.LENGTH_SHORT).show();
                        }
                    }

                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }
}


