package com.example.ashvin.receipts;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class FinalActivity extends Activity {
    private TextView textView;
    PieView pieView;
    Button button;
    Button button2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);
        textView = (TextView)findViewById(R.id.textView);
        pieView = (PieView)findViewById(R.id.pie_view);
        button = (Button)findViewById(R.id.pie_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                randomSet(pieView);
            }
        });
        button2 = (Button)findViewById(R.id.button_id_home);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d();
            }
        });
        set(pieView);
    }
    private void randomSet(PieView pieView){
        ArrayList<PieHelper> pieHelperArrayList = new ArrayList<PieHelper>();
//        ArrayList<Integer> intList = new ArrayList<Integer>();
//        int totalNum = (int) (5*Math.random()) + 5;
//
//        int totalInt = 0;
//        for(int i=0; i<totalNum; i++){
//            int ranInt = (int)(Math.random()*10)+1;
//            intList.add(ranInt);
//            totalInt += ranInt;
//        }
//        for(int i=0; i<totalNum; i++){
//            pieHelperArrayList.add(new PieHelper(100f*intList.get(i)/totalInt));
//        }

        pieHelperArrayList.add(new PieHelper(1/3));
        pieHelperArrayList.add(new PieHelper(2/3));

        pieView.selectedPie(PieView.NO_SELECTED_INDEX);
        pieView.showPercentLabel(true);
        pieView.setDate(pieHelperArrayList);
    }

    private void set(PieView pieView){
        ArrayList<PieHelper> pieHelperArrayList = new ArrayList<PieHelper>();
        pieHelperArrayList.add(new PieHelper(66, Color.rgb(46, 134, 222)));
        pieHelperArrayList.add(new PieHelper(34, Color.rgb(16, 172, 132)));

        pieView.setDate(pieHelperArrayList);
        pieView.setOnPieClickListener(new PieView.OnPieClickListener() {
            @Override
            public void onPieClick(int index) {
                if(index != PieView.NO_SELECTED_INDEX) {
                    textView.setText(index + " selected");
                }else{
                    textView.setText("No selected pie");
                }
            }
        });
        pieView.selectedPie(0);
    }

    private void d(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

}