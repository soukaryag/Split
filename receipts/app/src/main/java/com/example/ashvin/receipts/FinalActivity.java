package com.example.ashvin.receipts;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.text.DecimalFormat;


public class FinalActivity extends Activity {
    private TextView textView;
    PieView pieView;
    Button button;
    Button button2;
    String paid;
    String total;
    String owed;

    String payer1str;
    String payer2str;

    String p1amount;
    String p2amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        DecimalFormat df = new DecimalFormat("0.00");

        this.total = "3.28";   //need this value

        this.p1amount = "2.00";  //need this value
        this.p2amount = "1.00";  //need this value

        this.owed = String.valueOf(df.format(Float.parseFloat(p1amount) + Float.parseFloat(p2amount)));

        this.paid = String.valueOf(df.format(Float.parseFloat(total) - Float.parseFloat(owed)));

        TextView paidField = (TextView) findViewById(R.id.paidAmount);
        paidField.setText("$" + paid);

        TextView totalField = (TextView) findViewById(R.id.totalAmount);
        totalField.setText("$" + total);

        TextView owedField = (TextView) findViewById(R.id.owedAmount);
        owedField.setText("$" + owed);

        TextView p1Field = (TextView) findViewById(R.id.payerOne);
        p1Field.setText("Requested from Ashvin V. $" + p1amount);

        TextView p2Field = (TextView) findViewById(R.id.payerTwo);
        p2Field.setText("Requested from Edwin Y. $" + p2amount);




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

        pieHelperArrayList.add(new PieHelper(34, "Ashvin"));
        pieHelperArrayList.add(new PieHelper(66, "Edwin"));

        pieView.selectedPie(PieView.NO_SELECTED_INDEX);
        pieView.showPercentLabel(true);
        pieView.setDate(pieHelperArrayList);
    }

    private void set(PieView pieView){
        ArrayList<PieHelper> pieHelperArrayList = new ArrayList<PieHelper>();
        pieHelperArrayList.add(new PieHelper(66, Color.rgb(46, 134, 222), "Edwin"));
        pieHelperArrayList.add(new PieHelper(34, Color.rgb(16, 172, 132), "Ashvin"));

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