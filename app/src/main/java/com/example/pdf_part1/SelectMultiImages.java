package com.example.pdf_part1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;

public class SelectMultiImages extends AppCompatActivity {


    Button btnSelectImages;
    RecyclerView rcvPhoto;
    private PhotoAdapter photoAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_images);

        btnSelectImages=findViewById(R.id.btn_select_images);
        rcvPhoto=findViewById(R.id.rcv_photo);

        photoAdapter=new PhotoAdapter(this);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2,GridLayoutManager.VERTICAL
                ,false);
        rcvPhoto.setLayoutManager(gridLayoutManager);
        rcvPhoto.setFocusable(false);
        rcvPhoto.setAdapter(photoAdapter);

        btnSelectImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImages();
            }
        });

    }

    private void selectImages()
    {
        PermissionListener permissionListener=new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                openBottomPicker();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(SelectMultiImages.this,"Permission Denied ):",Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    public void openBottomPicker()
    {
        TedBottomPicker.OnMultiImageSelectedListener listener = new TedBottomPicker.OnMultiImageSelectedListener() {

            @Override
            public void onImagesSelected(ArrayList<Uri> uriList) {
                photoAdapter.setDataPhoto(uriList);

            }

        };
        TedBottomPicker tedBottomPicker= new TedBottomPicker.Builder(SelectMultiImages.this)
                .setOnMultiImageSelectedListener(listener)
                .setCompleteButtonText("Done")
                .setEmptySelectionText("No image")
                .create();
        tedBottomPicker.show(getSupportFragmentManager());

    }





}



