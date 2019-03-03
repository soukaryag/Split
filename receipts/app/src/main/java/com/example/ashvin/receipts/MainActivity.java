package com.example.ashvin.receipts;

import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.widget.Spinner;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import android.widget.Spinner;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import com.example.ashvin.receipts.ResultsActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.firebase.ml.vision.text.RecognizedLanguage;
import com.theartofdev.edmodo.cropper.CropImage;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_CROP = 2;
    private String currentPhotoPath;
    private String currentFileName;
    private Uri photoURI;
    private HashMap<String, Double> priceMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar

        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.groups_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final Button button = findViewById(R.id.button_id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            if(requestCode == REQUEST_TAKE_PHOTO){

                CropImage.activity(photoURI)
                        .start(this);
            }

            else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    File file = new File(resultUri.getPath());

                    Bitmap testImage = BitmapFactory.decodeFile(file.getAbsolutePath());

                    if (file.exists()) {
                        System.out.println("File does exist");
                    }
                    else {
                        System.out.println("File does not exist");
                    }

                    FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(testImage);

                    recognizeText(image);

                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }

            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        currentFileName = imageFileName;
        System.out.println("Image Path: " + image);
        System.out.println("Current Photo Path: " + currentPhotoPath);
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                System.out.println("Error when creating file: " + ex);
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }

//        Intent intent = new Intent(getApplicationContext(), FinalActivity.class);
//        startActivity(intent);

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

//                                System.out.println("Full text: " + firebaseVisionText.getText());
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

        HashMap<String, Integer> amountMap = new HashMap<>();
        HashMap<String, Integer> wordMap = new HashMap<>();

        ArrayList<String> blacklist = new ArrayList<>();
        blacklist.add("tax");
        blacklist.add("%");
        blacklist.add("subtotal");
        blacklist.add("total");
        blacklist.add("cash");
        blacklist.add("change");
        blacklist.add("paid");
        blacklist.add("less");
        blacklist.add("ashless");
        blacklist.add("shless");
        blacklist.add("chang");
        blacklist.add("subtota");
        blacklist.add("visa");
        blacklist.add("#");
        blacklist.add("mastercard");

        int offset = 100;

        for (FirebaseVisionText.TextBlock block: result.getTextBlocks()) {
//            System.out.println("#################");
            String blockText = block.getText();
//            System.out.println("Current Block: " + blockText);
            Float blockConfidence = block.getConfidence();
            List<RecognizedLanguage> blockLanguages = block.getRecognizedLanguages();
            Point[] blockCornerPoints = block.getCornerPoints();
            Rect blockFrame = block.getBoundingBox();
            for (FirebaseVisionText.Line line: block.getLines()) {
//                System.out.println("-------------------");
                String lineText = line.getText();
//                System.out.println("Current line: " + lineText);
                Float lineConfidence = line.getConfidence();
                List<RecognizedLanguage> lineLanguages = line.getRecognizedLanguages();
                Point[] lineCornerPoints = line.getCornerPoints();
                Rect lineFrame = line.getBoundingBox();

                boolean matchDollarAmt = Pattern.matches("^\\d+[.]\\d{2}$",lineText);
                int yCoord = lineCornerPoints[0].y;
//                System.out.println("Current point: " + yCoord);
                if(matchDollarAmt){
                    amountMap.put(lineText, yCoord);
                }
                else{
                    wordMap.put(lineText,yCoord);
                }

                for (FirebaseVisionText.Element element: line.getElements()) {
                    String elementText = element.getText();
//                    System.out.println("Element: " + elementText);
                    Float elementConfidence = element.getConfidence();
                    List<RecognizedLanguage> elementLanguages = element.getRecognizedLanguages();
                    Point[] elementCornerPoints = element.getCornerPoints();
//                    System.out.println("First Point: " + elementCornerPoints[0].toString());
                    Rect elementFrame = element.getBoundingBox();
                }
//                System.out.println("-------------------");
            }
//            System.out.println("#################");
        }

        for(String word: wordMap.keySet()){

            int yCoord = wordMap.get(word);

            for(String amt: amountMap.keySet()){
                double price = Double.parseDouble(amt);

                if(Math.abs(amountMap.get(amt) - yCoord) <= offset){

                    boolean valid = true;

                    for(String blacklisted: blacklist){
                        if(word.toLowerCase().contains(blacklisted)){
//                            System.out.println("blacklist: " + word);
                            valid = false;
                            break;
                        }
                    }

                    if(valid){
                        priceMap.put(word, price);
//                        System.out.println("Added: " + word);
                    }

                }
            }

        }

//        for(String key: priceMap.keySet()){
//            System.out.println("Item name: " + key + " , Price: " + priceMap.get(key));
//        }
        // [END mlkit_process_text_block]

        Intent intent = new Intent(getApplicationContext(), ResultsActivity.class);
        intent.putExtra("PriceMap", priceMap);

//                    ReceiptApplication.applicationState.priceMap = new HashMap<String, Double>(priceMap);
        startActivity(intent);
    }

}