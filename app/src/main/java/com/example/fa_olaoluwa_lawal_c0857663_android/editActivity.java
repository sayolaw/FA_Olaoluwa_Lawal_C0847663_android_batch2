package com.example.fa_olaoluwa_lawal_c0857663_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class editActivity extends AppCompatActivity {
    SQLiteDatabase sqLiteDatabase;
    Double glatitude;
    Double glongitude;
    String name;
    int id;
    int status;
    ArrayList<String> categoryList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        TextView tName = findViewById(R.id.name);
        TextView tlName = findViewById(R.id.name3);
        TextView tlongName = findViewById(R.id.name4);
        Switch tIsSub  = findViewById(R.id.switch2);
        Bundle bundle = getIntent().getExtras();
        glatitude = bundle.getDouble("latitude");
        glongitude = bundle.getDouble("longitude");
        name = bundle.getString("name");
        id = bundle.getInt("id");
        status = bundle.getInt("status");
        glongitude = bundle.getDouble("longitude");
        sqLiteDatabase = openOrCreateDatabase("places_db",MODE_PRIVATE,null);
        tlName.setText(glatitude.toString());
        tName.setText(name.toString());
        tlongName.setText(glongitude.toString());
        if(status==1){
            tIsSub.setChecked(true);
        }
    }
    public void editPlace(View view){
        String isSub;
        TextView tName = findViewById(R.id.name);
        Switch tIsSub  = findViewById(R.id.switch2);
        if(tIsSub.isChecked()){
            isSub = "1";
        }
        else{
            isSub = "0";
        }
        String name = tName.getText().toString().trim();


        if(name.isEmpty()){
            tName.setError("name field is empty");
            tName.requestFocus();
            return;
        }

        String sql = "UPDATE places SET name = ?,status = ?, latitude = ?, longitude = ? WHERE id = ?";
                    sqLiteDatabase.execSQL(sql, new String[]{
                            tName.getText().toString().trim(),isSub,glatitude.toString(),glongitude.toString(),String.valueOf(id)});

        Toast.makeText(editActivity.this, "Place has been edited.", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(this,MainActivity.class);
        this.startActivity(intent);

    }
}