package com.example.ashvin.receipts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.media.ExifInterface;
import android.net.Uri;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.widget.Button;
import android.support.v7.app.AppCompatActivity;
import android.graphics.Paint.Style;

import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.firebase.ml.vision.text.RecognizedLanguage;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;
import java.text.DecimalFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ResultsActivity extends AppCompatActivity {
    private int item1Clicks = 0;
    private int item2Clicks = 0;
    private int[] colors = {Color.WHITE, Color.GREEN, Color.BLUE};

    private ArrayList<Integer> buttonClicks = new ArrayList<>();
    private HashMap<String, Double> priceMap;
    Button button2;
    Button button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide(); // hide the title bart a

        setContentView(R.layout.activity_results);

        button2 = (Button)findViewById(R.id.button_request);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), FinalActivity.class);
                startActivity(intent);
            }
        });

        button3 = (Button)findViewById(R.id.button_retake);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        HashMap<String,Double> priceMap = (HashMap<String, Double>)getIntent().getSerializableExtra("PriceMap");
        for(String key: priceMap.keySet()){
            System.out.println("Item name: " + key + " , Price: " + priceMap.get(key));
        }
        //HardCoded Stuff
        TextView name = (TextView) findViewById(R.id.person_id);
        name.setText("Soukarya");

        int currId = 0;
        for(String item: priceMap.keySet()){

            DecimalFormat formatter = new DecimalFormat("#0.00");
            String currPrice = formatter.format(priceMap.get(item));

            String text = item + "                $" + currPrice;

            LinearLayout layout = (LinearLayout) findViewById(R.id.linLayout);
            final Button btn = new Button(this);
            btn.setText(text);
            layout.addView(btn);

            final int index = currId;

            buttonClicks.add(0);

            btn.setWidth(900);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(55, 160, 55, -70);
            params.gravity = Gravity.CENTER_HORIZONTAL;
            btn.setLayoutParams(params);

//            btn.setPadding(0,300,0,0);
            btn.setBackground(updateButtonBorder(index));
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int num = buttonClicks.get(index);
                    buttonClicks.set(index,num+1);
                    btn.setBackground(updateButtonBorder(index));
                }
            });

            currId += 1;
        }

        TextView groupName = (TextView) findViewById(R.id.group_name);
        groupName.setText("HooHacks Squad:");

        TextView member1 = (TextView) findViewById(R.id.member1);
        member1.setText("Ashvin V.");

        TextView member2 = (TextView) findViewById(R.id.member2);
        member2.setText("Edwin Y.");

    }

    public ShapeDrawable updateButtonBorder(int btnClicks) {
        ShapeDrawable sd = new ShapeDrawable();
        sd.setShape(new RectShape());
        sd.getPaint().setColor(colors[buttonClicks.get(btnClicks) % 3]);
        sd.getPaint().setStrokeWidth(10f);
        sd.getPaint().setStyle(Style.STROKE);
        return sd;
    }

}