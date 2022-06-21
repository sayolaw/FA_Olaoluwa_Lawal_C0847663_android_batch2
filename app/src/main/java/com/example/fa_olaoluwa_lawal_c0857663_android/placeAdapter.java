package com.example.fa_olaoluwa_lawal_c0857663_android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class placeAdapter extends ArrayAdapter {
    Context context;
    int layoutRes;
    String CompletionDate;
    List<Place> placeModelList;
    ListView lv;
    SQLiteDatabase sqLiteDatabase;

    //    public void deleteTask(Task taskModel){
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("Are you sure you want to delete this product");
//        builder.setPositiveButton("Yes",(dialogInterface, i) -> {
//            String sql = "DELETE FROM products WHERE id = ?";
//            sqLiteDatabase.execSQL(sql,new Integer[]{productModel.getId()});
//            loadEmployees();
//        });
//        builder.setNegativeButton("No",(d, i) ->{
//            Toast.makeText(context,"Product Not Deleted",Toast.LENGTH_LONG).show();
//        } );
//        builder.create().show();
//    }
    public placeAdapter(@NonNull Context context, int resource, @NonNull List<Place> placeModelList,
                           SQLiteDatabase sqLiteDatabase) {
        super(context, resource);
        this.placeModelList = placeModelList;
        this.context = context;
        layoutRes = resource;
        this.sqLiteDatabase = sqLiteDatabase;
    }
    @Override
    public int getCount(){
        return placeModelList.size();
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = convertView;
        if(v == null) v = inflater.inflate(layoutRes,null);
        TextView nameTv = v.findViewById(R.id.name2);
        Switch switchTv = v.findViewById(R.id.switch1);






        Place placeModel = placeModelList.get(position);
        nameTv.setText(placeModel.getName());
        if(placeModel.getStatus() == 1){
            switchTv.setChecked(true);
        }


        v.findViewById(R.id.editTask).setOnClickListener(view -> {
            deletePlaces(placeModel);
        });
        v.findViewById(R.id.view).setOnClickListener(view -> {
            showMap(placeModel);
        });

        v.findViewById(R.id.editBtn).setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                editCategories(placeModel);


            }

            //            @RequiresApi(api = Build.VERSION_CODES.O)
            public void editCategories(Place placeModel) {
                Intent intent=new Intent(context,MapsActivity3.class);
                Bundle bundle = new Bundle();

//Add your data to bundle
                bundle.putDouble("latitude", placeModel.getLatitude());
                bundle.putString("name", placeModel.getName());
                bundle.putInt("id", placeModel.getId());
                bundle.putInt("status", placeModel.getStatus());
                bundle.putDouble("longitude", placeModel.getLongitude());

//Add the bundle to the intent
                intent.putExtras(bundle);

                context.startActivity(intent);

            }
        });

        return v;

    }
    public void showMap(Place placemodel){
        Intent intent=new Intent(context,MapsActivity.class);
        Bundle bundle = new Bundle();

//Add your data to bundle
        bundle.putDouble("latitude", placemodel.getLatitude());
        bundle.putString("date", placemodel.getDate());
        bundle.putDouble("longitude", placemodel.getLongitude());

//Add the bundle to the intent
        intent.putExtras(bundle);

        context.startActivity(intent);
    }
    public void deletePlaces(Place placeModel){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Are you sure you want to delete this place");
        builder.setPositiveButton("Yes",(dialogInterface, i) -> {
            String sql = "DELETE FROM places WHERE id = ?";
            sqLiteDatabase.execSQL(sql,new Integer[]{placeModel.getId()});
            loadCategories();
        });
        builder.setNegativeButton("No",(d, i) ->{
            Toast.makeText(context,"Place Not Deleted",Toast.LENGTH_LONG).show();
        } );
        builder.create().show();
    }
    private void loadCategories() {

        String sql = "SELECT * FROM places";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);



        if (cursor.moveToFirst()) {
            Log.d("here","we are here");
            placeModelList.clear();
            do {
                // create an employee instance

                placeModelList.add(new Place(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getDouble(3),
                        cursor.getDouble(4),
                        cursor.getString(5)

                ));

            } while (cursor.moveToNext());
            cursor.close();
        }
        notifyDataSetChanged();

    }
}
