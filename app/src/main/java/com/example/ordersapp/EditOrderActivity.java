package com.example.ordersapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

public class EditOrderActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //Declare variables for all widgets
    Button btn_update, btn_delete, btn_backToMain;
    TextView tv_titleSingleOrder;
    EditText et_name, et_number, et_address;
    Spinner spinner_size;
    CheckBox cb_pepperoni, cb_olives, cb_bellPepper, cb_feta, cb_pineapple, cb_jalapeno;

    //Create string array for setting text
    String[] language;
    String[] spinner_items = new String[3];
    static String LANGUAGE_KEY = "language_key";


    int size;
    MainActivity.Size currentSize;

    MainActivity.Topping top1 = MainActivity.Topping.NO_TOPPING;
    MainActivity.Topping top2 = MainActivity.Topping.NO_TOPPING;
    MainActivity.Topping top3 = MainActivity.Topping.NO_TOPPING;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_order);

        int id = Integer.parseInt(getIntent().getStringExtra("id"));

        //Relate all variables to their xml counterparts
        tv_titleSingleOrder = findViewById(R.id.tv_titleSingleOrder);
        btn_update = findViewById(R.id.btn_update);
        btn_delete = findViewById(R.id.btn_delete);

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

        try{
            String destPath = "/data/data/" + getPackageName() +"/database/OrderDB.db";
            File f = new File(destPath);
            if(!f.exists()){
                CopyDB(getBaseContext().getAssets().open("OrderDB.db"),
                        new FileOutputStream(destPath));
            }
        } catch (IOException e){
            e.printStackTrace();
        }

        DBAdapter adapter = new DBAdapter(this);

        //Set the text of onscreen widgets
        setText();

        //Code for creating and populating a spinner object
        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spinner_items);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_size.setAdapter(ad);

        adapter.open();
        Cursor c = adapter.getOrder(id);
        if (c.moveToFirst()){
            et_name.setText(c.getString(c.getColumnIndex("name")));
            et_number.setText(c.getString(c.getColumnIndex("number")));
            et_address.setText(c.getString(c.getColumnIndex("address")));
            String title = tv_titleSingleOrder.getText() + " " + String.valueOf(id);
            tv_titleSingleOrder.setText(title);
            size = c.getInt(c.getColumnIndex("size"));
        }

        adapter.close();

        if (size == MainActivity.Size.SMALL.ordinal()){
            spinner_size.setSelection(MainActivity.Size.SMALL.ordinal());
        }else if (size == MainActivity.Size.MEDIUM.ordinal()){
            spinner_size.setSelection(MainActivity.Size.MEDIUM.ordinal());
        }else{
            spinner_size.setSelection(MainActivity.Size.LARGE.ordinal());
        }

        btn_backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Send back to main activity
                Intent i = new Intent(EditOrderActivity.this, MainActivity.class);
                startActivity(i);
            }
        });//End Back to Main on click listener

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.open();
                if (adapter.deleteOrder(id)){
                    Toast.makeText(view.getContext(), "Delete successful.", Toast.LENGTH_SHORT).show();
                    adapter.close();
                    Intent i = new Intent(EditOrderActivity.this, OrderActivity.class);
                    startActivity(i);
                }else {
                    Toast.makeText(view.getContext(), "Delete has failed.", Toast.LENGTH_SHORT).show();
                }
                adapter.close();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateText()){
                    adapter.open();
                    if (adapter.updateOrder(id,et_name.getText().toString(),et_number.getText().toString(),
                            et_address.getText().toString(),currentSize.ordinal(),top1.ordinal(),top2.ordinal(),top3.ordinal())){
                        Toast.makeText(view.getContext(), "Update successful.", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(view.getContext(), "Update has failed.", Toast.LENGTH_SHORT).show();
                    }
                    adapter.close();
                }
            }
        });
    }//End on create

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
        spinner_items[0] = language[7];
        spinner_items[1] = language[8];
        spinner_items[2] = language[9];
        cb_pepperoni.setText(language[10]);
        cb_olives.setText(language[11]);
        cb_bellPepper.setText(language[12]);
        cb_feta.setText(language[13]);
        cb_pineapple.setText(language[14]);
        cb_jalapeno.setText(language[15]);
        btn_backToMain.setText(language[19]);
        tv_titleSingleOrder.setText(language[20]);
        btn_update.setText(language[21]);
        btn_delete.setText(language[22]);

    }//End Set Text Method
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

    public void CopyDB(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        while((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer,0,length);
        }
        inputStream.close();
        outputStream.close();
    }//end method CopyDB

}//End Main Method