package com.example.fa_olaoluwa_lawal_c0857663_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase sqLiteDatabase;
    List<Place> placeList;
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = findViewById(R.id.placeList);
        placeList = new ArrayList<Place>();
        sqLiteDatabase = openOrCreateDatabase("places_db",MODE_PRIVATE,null);

        createTables();
        loadPlaces();
    }
    public void addPlace(View v){
        Intent intent=new Intent(MainActivity.this,MapsActivity2.class);
        this.startActivity(intent);
    }
    public void createTables(){
        String sql = "CREATE TABLE IF NOT EXISTS places(" +
                "id INTEGER NOT NULL CONSTRAINT place_pk PRIMARY KEY AUTOINCREMENT,"+
                "name VARCHAR(20) NOT NULL,"+
                "status INT NOT NULL,"+
                "latitude DOUBLE,"+
                "longitude DOUBLE,"+
                 "dateCreated DATETIME);";

        sqLiteDatabase.execSQL(sql);
    }
    private void loadPlaces() {
        String sql = "SELECT * FROM places";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);



        if (cursor.moveToFirst()) {

            do {
                // create an employee instance

                placeList.add(new Place(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getDouble(3),
                        cursor.getDouble(4),
                        cursor.getString(5)
                ));
                Log.d("sayo check","this is"+ placeList.get(0).getName());
            } while (cursor.moveToNext());
            cursor.close();
        }

        placeAdapter placeAdapt = new placeAdapter(this,R.layout.listdesign,placeList,sqLiteDatabase);
        lv.setAdapter(placeAdapt);
    }

}