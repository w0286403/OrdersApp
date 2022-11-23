package com.example.ordersapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Objects;

public class LogActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //Declare variables for all widgets
    Button btn_submit, btn_backToMain;
    TextView tv_titleBuilder;
    EditText et_name, et_number, et_address;
    Spinner spinner_size;
    CheckBox cb_pepperoni, cb_olives, cb_bellPepper, cb_feta, cb_pineapple, cb_jalapeno;

    //Create string array for setting text
    String[] language;
    String[] spinner_items = new String[3];
    static String LANGUAGE_KEY = "language_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        //Relate all variables to their xml counterparts
        tv_titleBuilder = findViewById(R.id.tv_titleBuilder);
        btn_submit = findViewById(R.id.btn_submit);
        btn_backToMain = findViewById(R.id.btn_backToMain);
        et_name = findViewById(R.id.et_name);
        et_number = findViewById(R.id.et_number);
        et_address = findViewById(R.id.et_address);
        spinner_size = findViewById(R.id.spinner_size);
        cb_pepperoni = findViewById(R.id.cb_pepperoni);
        cb_olives = findViewById(R.id.cb_olives);
        cb_bellPepper = findViewById(R.id.cb_bellPepper);
        cb_feta = findViewById(R.id.cb_feta);
        cb_pineapple = findViewById(R.id.cb_pineapple);
        cb_jalapeno = findViewById(R.id.cb_jalapeno);

        spinner_size.setOnItemSelectedListener(this);

        //Set the text of onscreen widgets
        setText();

        //Code for creating and populating a spinner object
        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spinner_items);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_size.setAdapter(ad);


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Send to view all orders activity
                Intent i = new Intent(LogActivity.this, OrderActivity.class);
                startActivity(i);
            }
        });//End submit on click listener

        btn_backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Send back to main activity
                Intent i = new Intent(LogActivity.this, MainActivity.class);
                startActivity(i);
            }
        });//End Back to Main on click listener

    }

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
        tv_titleBuilder.setText(language[3]);
        et_name.setHint(language[4]);
        et_number.setHint(language[5]);
        et_address.setHint(language[6]);
        spinner_items[0] = language[7];
        spinner_items[1] = language[8];
        spinner_items[2] = language[9];
        cb_pepperoni.setText(language[10]);
        cb_olives.setText(language[11]);
        cb_bellPepper.setText(language[12]);
        cb_feta.setText(language[13]);
        cb_pineapple.setText(language[14]);
        cb_jalapeno.setText(language[15]);
        btn_submit.setText(language[16]);
        btn_backToMain.setText(language[19]);


    }//End Set Text Method

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    }//Spinner method

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }//Spinner method

}//End Main Method