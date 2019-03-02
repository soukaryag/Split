package com.example.ashvin.receipts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

public class ResultsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        String filepath = getIntent().getStringExtra("Path");
        System.out.println("Filepath: " + filepath);

        //Do all the OCR Here


        File file = new File(filepath);
        if (file.exists()) {
            System.out.println("File does exist");
            ImageView imageView = findViewById(R.id.image_view);
            imageView.setImageURI(Uri.fromFile(file));
        }
        else {
            System.out.println("File does not exist");
        }

//        ExifInterface exif = new ExifInterface(filepath);
//        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

//        Matrix matrix = new Matrix();
//        matrix.postRotate(90);
//        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//        ImageView imageView = findViewById(R.id.image_view);

//        imageView.setImageBitmap(rotatedBitmap);
    }
}
