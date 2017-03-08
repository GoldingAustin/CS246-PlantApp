package com.cs246.plantapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;

public class AddPlant extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap bitmapPlant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences prefs = this.getSharedPreferences("AddPlant", Context.MODE_PRIVATE);
        if (prefs.contains("tempPlant")) {
            Gson gson = new Gson();
            String json = prefs.getString("tempPlant", "");
            PlantsObject plantsObject = gson.fromJson(json, PlantsObject.class);
            EditText name = (EditText) findViewById(R.id.editName);
            EditText potDiam = (EditText) findViewById(R.id.editPotDiameter);
            EditText fert = (EditText) findViewById(R.id.editFertalizer);
            EditText water = (EditText) findViewById(R.id.editWater);
            ImageView img = (ImageView) findViewById(R.id.imageButton);
            Bitmap bitTemp = StringToBitMap(plantsObject.getImage());
            img.setImageBitmap(bitTemp);
            name.setText(plantsObject.getName());
            potDiam.setText(plantsObject.getSpacing());
            fert.setText(plantsObject.getSoilPH());
            water.setText(plantsObject.getWaterReq());
        }

        ImageButton img = (ImageButton) findViewById(R.id.imageButton);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

            }
        });
    }

    /* BitMapToString and StringToBitMap
     * http://stackoverflow.com/questions/13562429/how-many-ways-to-convert-bitmap-to-string-and-vice-versa
     */
    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        PlantsObject tempPlant = new PlantsObject();
        EditText name = (EditText) findViewById(R.id.editName);
        EditText potDiam = (EditText) findViewById(R.id.editPotDiameter);
        EditText fert = (EditText) findViewById(R.id.editFertalizer);
        EditText water = (EditText) findViewById(R.id.editWater);
        if (bitmapPlant != null) {
            String image = BitMapToString(bitmapPlant);
            tempPlant.setImage(image);
        }
        tempPlant.setName(name.getText().toString());
        tempPlant.setSoilPH(fert.getText().toString());
        tempPlant.setSpacing(potDiam.getText().toString());
        tempPlant.setWaterReq(water.getText().toString());


        SharedPreferences prefs = this.getSharedPreferences("AddPlant", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(tempPlant);
        editor.putString("tempPlant", json);
        editor.commit();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            bitmapPlant = imageBitmap;
            ImageView img = (ImageView) findViewById(R.id.imageButton);
            img.setImageBitmap(imageBitmap);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(i);

        }

        if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            setContentView(R.layout.activity_login);
        }

        return super.onOptionsItemSelected(item);
    }

}
