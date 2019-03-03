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
import java.util.HashMap;


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

    float percentEd; //payer2
    float percentAs; //payer1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        DecimalFormat df = new DecimalFormat("0.00");
        ArrayList<Integer> buttonClicks = (ArrayList<Integer>) getIntent().getSerializableExtra("ButtonClicks"); //0= souakrya, 1 = edwin, 2 =ashvin
        HashMap<String, Double> priceMap = (HashMap<String, Double>) getIntent().getSerializableExtra("PriceMap"); //
        ArrayList<String> itemNames = (ArrayList<String>) getIntent().getSerializableExtra("ItemNames");

        float temp1 = 0;
        float temp2 = 0;
        for(int i = 0; i < buttonClicks.size(); i++){
            if(buttonClicks.get(i) == 1){  //edwin   (2)
                temp2 = temp2 + (priceMap.get(itemNames.get(i))).floatValue();
            }
            else if(buttonClicks.get(i) == 2){ //ashvin  (1)
                temp1 = temp1 + (priceMap.get(itemNames.get(i))).floatValue();
            }
        }

        this.p1amount = String.valueOf(df.format(temp1));  //need this value
        this.p2amount = String.valueOf(df.format(temp2));  //need this value


        this.total = String.valueOf(df.format(temp1 + temp2));   //need this value

        this.owed = String.valueOf(df.format(Float.parseFloat(p1amount) + Float.parseFloat(p2amount)));

        this.paid = String.valueOf(df.format(Float.parseFloat(total) - Float.parseFloat(owed)));

        TextView paidField = (TextView) findViewById(R.id.paidAmount);
        paidField.setText("$" + paid);

        TextView totalField = (TextView) findViewById(R.id.totalAmount);
        totalField.setText("$" + total);

        TextView owedField = (TextView) findViewById(R.id.owedAmount);
        owedField.setText("$" + owed);

        TextView p1Field = (TextView) findViewById(R.id.payerOne);
        p1Field.setText("Requested Ashvin V. for $" + p1amount);

        TextView p2Field = (TextView) findViewById(R.id.payerTwo);
        p2Field.setText("Requested Edwin Y. for $" + p2amount);



        percentEd = (Float.parseFloat(p1amount) / Float.parseFloat(owed)) *100;
        percentAs = (Float.parseFloat(p2amount) / Float.parseFloat(owed)) *100;


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
        set(pieView, this.percentAs, this.percentEd, Float.parseFloat(total));
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

        pieHelperArrayList.add(new PieHelper(10, "blah"));   //not important
        pieHelperArrayList.add(new PieHelper(90, "blah"));

        pieView.selectedPie(PieView.NO_SELECTED_INDEX);
        pieView.showPercentLabel(true);
        pieView.setDate(pieHelperArrayList);
    }

    private void set(PieView pieView, float percentAs, float percentEd, float total){
        ArrayList<PieHelper> pieHelperArrayList = new ArrayList<PieHelper>();
        pieHelperArrayList.add(new PieHelper(percentEd, Color.rgb(46, 134, 222), "Ashvin", total));
        pieHelperArrayList.add(new PieHelper(percentAs, Color.rgb(16, 172, 132), "Edwin", total));

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