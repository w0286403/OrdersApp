package com.example.ordersapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

public class OrderActivity extends AppCompatActivity {

    //Declare variables for widgets
    TextView tv_titleOrders;
    Button btn_viewSingleOrder, btn_backToMain;

    //Declare string array and key for shared preferences
    String[] language;
    static String LANGUAGE_KEY = "language_key";
    int buttonSelected;
    boolean hasButton;

    @SuppressLint("Range")
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

        adapter.open();
        Cursor c = adapter.getAllOrder();
        if (c.moveToFirst()){
            do{
                LinearLayout ll = (LinearLayout)findViewById(R.id.layout2);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

                Button btn = new Button(this);
                btn.setId(c.getInt(c.getColumnIndex("_id")));
                final int id = btn.getId();

                String text = "# " + String.valueOf(id) + " - " + c.getString( c.getColumnIndex("created_at"));

                btn.setText(text);
                ll.addView(btn, params);
                btn = ((Button) findViewById(id));
                btn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        Toast.makeText(view.getContext(), "Current order selected = " + id, Toast.LENGTH_SHORT).show();
                        buttonSelected = id;
                        hasButton = true;
                    }
                });

            }while (c.moveToNext());
        }
        adapter.close();

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
                if (hasButton){
                    Intent i = new Intent(OrderActivity.this, EditOrderActivity.class);
                    i.putExtra("id",Integer.toString(buttonSelected));
                    startActivity(i);
                }
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

    public void CopyDB(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        while((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer,0,length);
        }
        inputStream.close();
        outputStream.close();
    }//end method CopyDB



}//End main method