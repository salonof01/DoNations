package com.onofre.salvador.donations;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.net.URI;
import java.net.URL;

public class showImage extends AppCompatActivity {
String strImage;
    private static final String TAG = showImage.class.getSimpleName();
    ImageView imagenGrande;
Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        imagenGrande = (ImageView) findViewById(R.id.imageView2);

        Intent intent= getIntent();

        strImage= intent.getStringExtra("image");

        Log.d(TAG ,"this is a test" +" "+ strImage);
        Glide.with(this).load(strImage).into(imagenGrande);


        }
    }