package com.cs246.plantapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import com.cs246.plantapp.Utilities.*;
import java.util.ArrayList;
import java.util.List;

import static com.cs246.plantapp.Utilities.BitMapToString;
import static com.cs246.plantapp.Utilities.StringToBitMap;

/**
 * The type Add plant.
 */
public class AddPlant extends AppCompatActivity {
    /**
     * The Request image capture.
     */
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String TAG = "AddPlant";
    private Bitmap bitmapPlant;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent i = this.getIntent();
        SharedPreferences prefs = this.getSharedPreferences("AddPlant", Context.MODE_PRIVATE);
        if (i != null && i.getStringExtra("searchPlant") != null) {
            Gson gson = new Gson();
            String json = i.getStringExtra("searchPlant");
            PlantsObject plantsObject = gson.fromJson(json, PlantsObject.class);

            if (plantsObject.getImage() != null) {
                GetBitmapFromURLAsync getBitmapFromURLAsync = new GetBitmapFromURLAsync();
                getBitmapFromURLAsync.execute(plantsObject.getImage());
            }
            ReplaceAddPlantValues(plantsObject);
        } else if (prefs.contains("tempPlant")) {
            Gson gson = new Gson();
            String json = prefs.getString("tempPlant", "");
            PlantsObject plantsObject = gson.fromJson(json, PlantsObject.class);
            ImageView img = (ImageView) findViewById(R.id.imageButton);
            Bitmap bitTemp = StringToBitMap(plantsObject.getImage());
            img.setImageBitmap(bitTemp);
            ReplaceAddPlantValues(plantsObject);
        }

        ImageButton img = (ImageButton) findViewById(R.id.imageButton);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "ImageButton: clicked");
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

            }
        });

        FloatingActionButton myFab = (FloatingActionButton) this.findViewById(R.id.savePlantButton);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setPlantFireBase();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
    }

    /* GetBitmapFromURLAsync and getBitmapFromURL
    *http://stackoverflow.com/questions/37510411/download-an-image-into-bitmap-file-in-android
    */
    public class GetBitmapFromURLAsync extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            return getBitmapFromURL(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            ImageView img = (ImageView) findViewById(R.id.imageButton);
            bitmapPlant = bitmap;
            img.setImageBitmap(bitmap);
        }
    }

    /**
     * Gets bitmap from url.
     *
     * @param src the src
     * @return the bitmap from url
     */
    public Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Replace add plant values.
     *
     * @param plantsObject the plants object
     */
    public void ReplaceAddPlantValues(PlantsObject plantsObject) {
        EditText name = (EditText) findViewById(R.id.editName);
        EditText potDiam = (EditText) findViewById(R.id.editPotDiameter);
        EditText fert = (EditText) findViewById(R.id.editFertalizer);
        EditText water = (EditText) findViewById(R.id.editWater);
        name.setText(plantsObject.getName());
        potDiam.setText(plantsObject.getSpacing());
        fert.setText(plantsObject.getSoilPH());
        water.setText(plantsObject.getWaterReq());
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        if (plantsObject.getCheckDays() != null) {
            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                if (linearLayout.getChildAt(i) instanceof CheckBox) {
                    ((CheckBox) linearLayout.getChildAt(i)).setChecked(plantsObject.getCheckDays().get(0));
                    plantsObject.getCheckDays().remove(0);
                }
            }
        }
    }

    /**
     * Sets plant object.
     *
     * @return the plant object
     */
    public PlantsObject setPlantObject() {
        PlantsObject tempPlant = new PlantsObject();
        EditText name = (EditText) findViewById(R.id.editName);
        EditText potDiam = (EditText) findViewById(R.id.editPotDiameter);
        EditText fert = (EditText) findViewById(R.id.editFertalizer);
        EditText water = (EditText) findViewById(R.id.editWater);
        if (bitmapPlant != null) {
            String image = BitMapToString(bitmapPlant);
            Log.d("Image length", String.valueOf(image.length()));
            tempPlant.setImage(image);
        }
        tempPlant.setName(name.getText().toString());
        tempPlant.setSoilPH(fert.getText().toString());
        tempPlant.setSpacing(potDiam.getText().toString());
        tempPlant.setWaterReq(water.getText().toString());
        List<Boolean> checkDays = new ArrayList<>();
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            if (linearLayout.getChildAt(i) instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) linearLayout.getChildAt(i);
                checkDays.add(checkBox.isChecked());
            }
        }
        tempPlant.setCheckDays(checkDays);
        Log.d("Set Plant: ", tempPlant.toString());
        return tempPlant;
    }

    /**
     * Sets plant firebase.
     */
    public void setPlantFireBase() {
        SharedPreferences prefs = this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        String user = prefs.getString("User", "");
        PlantsObject tempPlant = setPlantObject();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(tempPlant.getName()).setValue(tempPlant);
    }

    @Override
    public void onPause() {
        super.onPause();
        PlantsObject tempPlant = setPlantObject();
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
        getMenuInflater().inflate(R.menu.menu_addplant, menu);
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
            Log.v(TAG, ": Settings Selected");
            Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(i);

        }

        if (id == R.id.action_logout) {
            Log.v(TAG, ": Beginning Logout");
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            setContentView(R.layout.activity_login);
        }


        return super.onOptionsItemSelected(item);
    }

}
