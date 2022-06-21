package com.example.fa_olaoluwa_lawal_c0857663_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class addPlace extends AppCompatActivity {
    SQLiteDatabase sqLiteDatabase;
    String CompletionDate;
    Double glatitude;
    Double glongitude;
    ArrayList<String> categoryList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);
        TextView tlName = findViewById(R.id.name3);
        TextView tlongName = findViewById(R.id.name4);
        Bundle bundle = getIntent().getExtras();
        glatitude = bundle.getDouble("latitude");
        glongitude = bundle.getDouble("longitude");
        sqLiteDatabase = openOrCreateDatabase("places_db",MODE_PRIVATE,null);
        tlName.setText(glatitude.toString());
        tlongName.setText(glongitude.toString());

    }
    public void addPlace(View view){
        TextView tName = findViewById(R.id.name);


        String isSub;
        Switch tIsSub  = findViewById(R.id.switch2);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CANADA);
        String date = sdf.format(calendar.getTime());
        if(tIsSub.isChecked()){
            isSub = "1";
        }
        else{
            isSub = "0";
        }
        String name = tName.getText().toString().trim();


        String cDate = CompletionDate;


        if(name.isEmpty()){
            tName.setError("name field is empty");
            tName.requestFocus();
            return;
        }


        String sql = "INSERT INTO places(name,status,latitude,longitude,dateCreated)"+
                "VALUES(?, ?, ?, ?,?)";
        sqLiteDatabase.execSQL(sql,new String[]{name,isSub,glatitude.toString(),glongitude.toString(),date});
        Toast.makeText(addPlace.this, "Place has been added.", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(this,MainActivity.class);
        this.startActivity(intent);

    }
}