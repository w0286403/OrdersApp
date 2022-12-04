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
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

    MainActivity.Size currentSize;
    MainActivity.Topping top1 = MainActivity.Topping.NO_TOPPING;
    MainActivity.Topping top2 = MainActivity.Topping.NO_TOPPING;
    MainActivity.Topping top3 = MainActivity.Topping.NO_TOPPING;

    int numOfToppings = 0;

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

        cb_pepperoni.setOnClickListener(onChecked);
        cb_olives.setOnClickListener(onChecked);
        cb_bellPepper.setOnClickListener(onChecked);
        cb_feta.setOnClickListener(onChecked);
        cb_pineapple.setOnClickListener(onChecked);
        cb_jalapeno.setOnClickListener(onChecked);

        spinner_size.setOnItemSelectedListener(this);

        //Set the text of onscreen widgets
        setText();

        //Code for creating and populating a spinner object
        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spinner_items);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_size.setAdapter(ad);

        try{
            String destPath = "/data/data/" + getPackageName() +"/database/OrderDB.db";
            File f = new File(destPath);
            if(!f.exists()){
                CopyDB(getBaseContext().getAssets().open("OrderDB.db"),
                        new FileOutputStream(destPath));
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        DBAdapter adapter = new DBAdapter(this);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validateText()){
                    adapter.open();
                    adapter.insertOrder(et_name.getText().toString(),et_number.getText().toString(),et_address.getText().toString(),currentSize.ordinal(),top1.ordinal(), top2.ordinal(), top3.ordinal());
                    adapter.close();
                    //Send to view all orders activity
                    Intent i = new Intent(LogActivity.this, OrderActivity.class);
                    startActivity(i);
                    Toast.makeText(view.getContext(), "A New Order Has Been Created!", Toast.LENGTH_SHORT).show();

                }
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

    public View.OnClickListener onChecked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            MainActivity.Topping topping;
                switch (view.getId()) {
                    case (R.id.cb_pepperoni):
                        if (cb_pepperoni.isChecked() && numOfToppings>=3){
                            cb_pepperoni.setChecked(false);
                        }
                        topping = MainActivity.Topping.PEPPERONI;
                        if (cb_pepperoni.isChecked() || numOfToppings != 3) {
                            numOfToppings++;
                        } else {
                            numOfToppings--;
                        }

                        break;
                    case (R.id.cb_olives):

                        if (cb_olives.isChecked() && numOfToppings>=3){
                            cb_olives.setChecked(false);
                        }
                        topping = MainActivity.Topping.OLIVES;
                        if (cb_olives.isChecked()) {
                            numOfToppings++;
                        } else {
                            numOfToppings--;
                        }

                        break;
                    case (R.id.cb_bellPepper):

                        if (cb_bellPepper.isChecked() && (numOfToppings>=3)){
                            cb_bellPepper.setChecked(false);
                        }
                        topping = MainActivity.Topping.BELLPEPPER;
                        if (cb_bellPepper.isChecked()) {
                            numOfToppings++;
                        } else {
                            numOfToppings--;
                        }

                        break;
                    case (R.id.cb_feta):

                        if ( cb_feta.isChecked() && numOfToppings>=3){
                            cb_feta.setChecked(false);
                        }
                        topping = MainActivity.Topping.FETA;
                        if (cb_feta.isChecked()) {
                            numOfToppings++;
                        } else {
                            numOfToppings--;
                        }

                        break;
                    case (R.id.cb_pineapple):
                        if (cb_pineapple.isChecked() && numOfToppings>=3){
                            cb_pineapple.setChecked(false);
                        }
                        topping = MainActivity.Topping.PINEAPPLE;
                        if (cb_pineapple.isChecked()) {
                            numOfToppings++;
                        } else {
                            numOfToppings--;
                        }

                        break;
                    case (R.id.cb_jalapeno):
                        if (cb_jalapeno.isChecked() && numOfToppings>=3){
                            cb_jalapeno.setChecked(false);
                        }
                        topping = MainActivity.Topping.JALAPENO;
                        if (cb_jalapeno.isChecked()) {
                            numOfToppings++;
                        } else {
                            numOfToppings--;
                        }

                        break;
                    default:
                        break;
                }
        }
    };//End on Checked

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
        if (l == 0){
            currentSize = MainActivity.Size.SMALL;
        }else if (l == 1){
            currentSize = MainActivity.Size.MEDIUM;
        }else{
            currentSize = MainActivity.Size.LARGE;
        }
    }//Spinner method

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }//Spinner method

    public void CopyDB(InputStream inputStream, OutputStream outputStream) throws IOException{
        byte[] buffer = new byte[1024];
        int length;
        while((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer,0,length);
        }
        inputStream.close();
        outputStream.close();
    }//end method CopyDB

    public boolean validateText(){
        boolean willPass = true;
        String numPattern = "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}";

        if (et_name.getText().toString().length() <= 0 ||et_name.getText().toString().length() >= 70 ){
            Toast.makeText(getBaseContext(), "Please make sure text is between 1 & 70 characters.", Toast.LENGTH_LONG).show();
            willPass = false;
        }

        if (!et_number.getText().toString().matches(numPattern)){
            Toast.makeText(getBaseContext(), "Please make sure phone number is a valid 10 digit North American number.", Toast.LENGTH_LONG).show();
            willPass = false;
        }

        if (et_address.getText().toString().length() <= 0 ||et_address.getText().toString().length() >= 70 ){
            Toast.makeText(getBaseContext(), "Please make sure text is between 1 & 70 characters.", Toast.LENGTH_LONG).show();
            willPass = false;
        }

        return willPass;
    }

}//End Main Method