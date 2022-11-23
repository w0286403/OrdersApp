package com.example.ordersapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

public class OrderActivity extends AppCompatActivity {

    //Declare variables for widgets
    TextView tv_titleOrders;
    Button btn_viewSingleOrder, btn_backToMain;

    //Declare string array and key for shared preferences
    String[] language;
    static String LANGUAGE_KEY = "language_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        //Connect variables to xml counterpart
        tv_titleOrders = findViewById(R.id.tv_titleOrders);
        btn_backToMain = findViewById(R.id.btn_backToMain);
        btn_viewSingleOrder = findViewById(R.id.btn_viewSingleOrder);

        //Set the text of the widgets on screen
        setText();

        btn_backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Send back to Main
                Intent i = new Intent(OrderActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        btn_viewSingleOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Send to view single order activity
                Intent i = new Intent(OrderActivity.this, EditOrderActivity.class);
                startActivity(i);
            }
        });

    }//End oncreate

    public void setText() {
        //Create prefs and retrieve language data
        SharedPreferences prefs = getSharedPreferences("language", MODE_PRIVATE);

        //If the key is english, set the string array to the english one, otherwise french
        if (Objects.equals(prefs.getString(LANGUAGE_KEY, ""), "english")) {
            language = getResources().getStringArray(R.array.english);
        } else {
            language = getResources().getStringArray(R.array.french);
        }
        //Set all text values of widgets
        tv_titleOrders.setText(language[17]);
        btn_viewSingleOrder.setText(language[18]);
        btn_backToMain.setText(language[19]);

    }//End Set Text Method

}//End main method