package com.example.ashvin.receipts;

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
import android.support.v7.app.AppCompatActivity;
import android.graphics.Paint.Style;

import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.util.List;

public class ResultsActivity extends AppCompatActivity {
    private int item1Clicks = 0;
    private int item2Clicks = 0;
    private int[] colors = {Color.WHITE, Color.GREEN, Color.BLUE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        //HardCoded Stuff
        TextView name = (TextView) findViewById(R.id.person_id);
        name.setText("Soukarya");

        final Button item1 = (Button) findViewById(R.id.item1);
        item1.setText("McDonalds                $1.00");
        item1.setBackground(updateButtonBorder1());
        item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item1Clicks += 1;
                item1.setBackground(updateButtonBorder1());
            }
        });

        final Button item2 = (Button) findViewById(R.id.item2);
        item2.setText("S Caramel Mocha          $2.00");
        item2.setBackground(updateButtonBorder2());
        item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item2Clicks += 1;
                item2.setBackground(updateButtonBorder2());
            }
        });

        TextView groupName = (TextView) findViewById(R.id.group_name);
        groupName.setText("Group 1:");

        TextView color1 = (TextView) findViewById(R.id.color1);
        color1.setText("Green:");

        TextView color2 = (TextView) findViewById(R.id.color2);
        color2.setText("Blue");

        TextView member1 = (TextView) findViewById(R.id.member1);
        member1.setText("Ashvin V.");

        TextView member2 = (TextView) findViewById(R.id.member2);
        member2.setText("Edwin Y.");


        String filepath = getIntent().getStringExtra("Path");
        File file = new File(filepath);
        Bitmap testImage = BitmapFactory.decodeFile(file.getAbsolutePath());

        if (file.exists()) {
            System.out.println("File does exist");
//            ImageView imageView = findViewById(R.id.image_view);
//            imageView.setImageURI(Uri.fromFile(file));

            try {
                ExifInterface exif = new ExifInterface(filepath);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

                System.out.println("Orientation: " + orientation);

                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap rotatedBitmap = Bitmap.createBitmap(testImage, 0, 0, testImage.getWidth(), testImage.getHeight(), matrix, true);
                testImage = rotatedBitmap;
            } catch(IOException err) {
                System.out.println(err);
            }
        }
        else {
            System.out.println("File does not exist");
        }

//        Bitmap test_image = getBitmapFromAsset(this,"target.jpg");
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(testImage);

        recognizeText(image);

//        TextRecognizer txtRecog = new TextRecognizer.Builder(getApplicationContext()).build();
//
//        if(txtRecog.isOperational()){
//            System.out.println("Text Recog is operational");
//            Frame frame = new Frame.Builder().setBitmap(testImage).build();
//            SparseArray<TextBlock> items = txtRecog.detect(frame);
//
//            for(int i = 0; i < items.size(); i++){
//                TextBlock item = items.valueAt(i);
//                System.out.println("Text: " + item.getValue());
//            }
//        }

    }

    private void recognizeText(FirebaseVisionImage image) {

        // [START get_detector_default]
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
                .getOnDeviceTextRecognizer();
        // [END get_detector_default]

        // [START run_detector]
        Task<FirebaseVisionText> result =
                detector.processImage(image)
                        .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                            @Override
                            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                // Task completed successfully
                                // [START_EXCLUDE]
                                // [START get_text]
                                processTextBlock(firebaseVisionText);
                                // [END get_text]
                                // [END_EXCLUDE]
                            }
                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
                                        // Task failed with an exception
                                        // ...
                                        e.printStackTrace();
                                    }
                                });
        // [END run_detector]
    }

    private void processTextBlock(FirebaseVisionText result) {
        // [START mlkit_process_text_block]
        String resultText = result.getText();

        for (FirebaseVisionText.TextBlock block: result.getTextBlocks()) {
            System.out.println("#################");
            String blockText = block.getText();
            Float blockConfidence = block.getConfidence();
            List<RecognizedLanguage> blockLanguages = block.getRecognizedLanguages();
            Point[] blockCornerPoints = block.getCornerPoints();
            Rect blockFrame = block.getBoundingBox();
            for (FirebaseVisionText.Line line: block.getLines()) {
                System.out.println("-------------------");
                String lineText = line.getText();
                Float lineConfidence = line.getConfidence();
                List<RecognizedLanguage> lineLanguages = line.getRecognizedLanguages();
                Point[] lineCornerPoints = line.getCornerPoints();
                Rect lineFrame = line.getBoundingBox();
                for (FirebaseVisionText.Element element: line.getElements()) {
                    String elementText = element.getText();
                    System.out.println("Element: " + elementText);
                    Float elementConfidence = element.getConfidence();
                    List<RecognizedLanguage> elementLanguages = element.getRecognizedLanguages();
                    Point[] elementCornerPoints = element.getCornerPoints();
                    System.out.println("First Point: " + elementCornerPoints[0].toString());
                    Rect elementFrame = element.getBoundingBox();
                }
                System.out.println("-------------------");
            }
            System.out.println("#################");
        }
        // [END mlkit_process_text_block]
    }

    private void processTextRecognitionResult(FirebaseVisionText texts) {
        List<FirebaseVisionText.TextBlock> blocks = texts.getTextBlocks();
        if (blocks.size() == 0) {
            System.out.println("No Text Found");
            return;
        }

        for (int i = 0; i < blocks.size(); i++) {
            System.out.println("#################");
            List<FirebaseVisionText.Line> lines = blocks.get(i).getLines();
            for (int j = 0; j < lines.size(); j++) {

                List<FirebaseVisionText.Element> elements = lines.get(j).getElements();
                for (int k = 0; k < elements.size(); k++) {

                    System.out.print(elements.get(k).getText() + " , ");
                }

                System.out.println("");
            }
            System.out.println("#################");
        }
    }

    public static Bitmap getBitmapFromAsset(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();

        InputStream is;
        Bitmap bitmap = null;
        try {
            is = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }
    public ShapeDrawable updateButtonBorder1() {
        ShapeDrawable sd = new ShapeDrawable();
        sd.setShape(new RectShape());
        sd.getPaint().setColor(colors[item1Clicks % 3]);
        sd.getPaint().setStrokeWidth(10f);
        sd.getPaint().setStyle(Style.STROKE);
        return sd;
    }
    public ShapeDrawable updateButtonBorder2() {
        ShapeDrawable sd = new ShapeDrawable();
        sd.setShape(new RectShape());
        sd.getPaint().setColor(colors[item2Clicks % 3]);
        sd.getPaint().setStrokeWidth(10f);
        sd.getPaint().setStyle(Style.STROKE);
        return sd;
    }

}