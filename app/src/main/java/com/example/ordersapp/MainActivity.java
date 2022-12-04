package com.example.ordersapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.*;
import android.widget.*;
import android.view.*;
import android.content.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    //Declare widget objects
    Button btn_pizzaBuilder, btn_viewOrders, btn_lang;
    TextView tv_restaurantName;

    //Declare variables for shared preferences
    String[] language; //Holds the string array
    static String LANGUAGE_KEY = "language_key";
    SharedPreferences prefs;

    //Enums to hold in database
    enum Topping {
        NO_TOPPING,
        PEPPERONI,
        OLIVES,
        BELLPEPPER,
        FETA,
        PINEAPPLE,
        JALAPENO
    }

    enum Size {
        SMALL,
        MEDIUM,
        LARGE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Link variables to their xml counterpart
        btn_pizzaBuilder = findViewById(R.id.btn_pizzaBuilder);
        btn_viewOrders = findViewById(R.id.btn_viewOrders);
        btn_lang = findViewById(R.id.btn_lang);
        tv_restaurantName = findViewById(R.id.tv_restaurantName);

        //Get any shared preferences and create the preferences editor
        prefs = getSharedPreferences("language", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();

        //Set text on screen depending on preference
        setText();

        try{
            String destPath = "/data/data/" + getPackageName() +"/database/OrderDB.db";
            File f = new File(destPath);
            if(!f.exists()){
                CopyDB(getBaseContext().getAssets().open("OrderDB.db"), new FileOutputStream(destPath));
            }
        } catch (IOException e){
            e.printStackTrace();
        }

        btn_pizzaBuilder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Send to the log activity
                Intent i = new Intent(MainActivity.this, LogActivity.class);
                startActivity(i);
            }
        });//End of on click pizza builder button

        btn_viewOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Send to the order activity
                Intent i = new Intent(MainActivity.this, OrderActivity.class);
                startActivity(i);
            }
        });//End of on click view orders button

        btn_lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //If the shared prefs language key is english, then assign it to french,
                //Other wise assign it to english
                if (Objects.equals(prefs.getString(LANGUAGE_KEY, ""), "english")) {
                    prefsEditor.putString(LANGUAGE_KEY, "french");
                } else {
                    prefsEditor.putString(LANGUAGE_KEY, "english");
                }
                prefsEditor.apply();//save the settings
                setText();//Set the text on screen
                //Show to the user that it has been saved
                Toast.makeText(getBaseContext(), "Saved", Toast.LENGTH_LONG).show();
            }
        });//End of on click language button
    }

    public void setText() {
        //If the key is english, set the language array to be english, otherwise french
        if (Objects.equals(prefs.getString(LANGUAGE_KEY, ""), "english")) {
            language = getResources().getStringArray(R.array.english);
        } else {
            language = getResources().getStringArray(R.array.french);
        }
        //set all the text values based on the string array
        tv_restaurantName.setText(language[0]);
        btn_pizzaBuilder.setText(language[1]);
        btn_viewOrders.setText(language[2]);
    }//End Of Set Text Method

    public void CopyDB(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        while((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer,0,length);
        }
        inputStream.close();
        outputStream.close();
    }//end method CopyDB


}//End Of Main