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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cs246.plantapp.Utilities.BitMapToString;
import static com.cs246.plantapp.Utilities.StringToBitMap;

/**
 * The type Add plant.
 */
public class AddPlant extends AppCompatActivity {
    /**
     * The Request image capture.
     */
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String TAG = "AddPlant";
    private Bitmap bitmapPlant;
    private String oldName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        checkForIntentPrefs(this.getIntent());


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
                if (checkFields()) {
                    setPlantFireBase();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }
            }
        });

    }


    /**
     * Check for intent prefs.
     *
     * @param i the
     */
    private void checkForIntentPrefs(Intent i) {
        SharedPreferences prefs = this.getSharedPreferences("AddPlant", Context.MODE_PRIVATE);
        if (i.getStringExtra("searchPlant") != null) {
            Gson gson = new Gson();
            String json = i.getStringExtra("searchPlant");
            PlantsObject plantsObject = gson.fromJson(json, PlantsObject.class);
            if (plantsObject.getWaterReq() != null) {
                switch (plantsObject.getWaterReq()) {
                    case "0.2":
                        plantsObject.setWaterReq("0");
                        break;
                    case "0.5":
                        plantsObject.setWaterReq("1");
                        break;
                    case "0.8":
                        plantsObject.setWaterReq("2");
                        break;
                    default:
                        break;
                }
            } else {
                plantsObject.setWaterReq("0");
            }
            if (plantsObject.getImage() != null) {
                GetBitmapFromURLAsync getBitmapFromURLAsync = new GetBitmapFromURLAsync();
                getBitmapFromURLAsync.execute(plantsObject.getImage());
            }
            ReplaceAddPlantValues(plantsObject);
        } else if (prefs.contains("tempPlant")) {
            Gson gson = new Gson();
            String json = prefs.getString("tempPlant", "");
            PlantsObject plantsObject = gson.fromJson(json, PlantsObject.class);
            oldName = plantsObject.getName();
            if (plantsObject.getImage() != null) {
                ImageView img = (ImageView) findViewById(R.id.imageButton);
                Bitmap bitTemp = StringToBitMap(plantsObject.getImage());
                img.setImageBitmap(bitTemp);
            }
            ReplaceAddPlantValues(plantsObject);
        }
        SharedPreferences prefsSettings = this.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.linearLayoutAddLabel);
        if (prefsSettings.contains("Days")) {
            Gson gson = new Gson();
            Type list = new TypeToken<List<Boolean>>() {
            }.getType();
            List<Boolean> days = gson.fromJson(prefsSettings.getString("Days", ""), list);
            for (int k = 0; k < linearLayout.getChildCount(); k++) {
                if (linearLayout.getChildAt(k) instanceof CheckBox) {
                    if (!days.get(k)) {
                        ((CheckBox) linearLayout.getChildAt(k)).setVisibility(View.GONE);
                        ((TextView) linearLayout1.getChildAt(k)).setVisibility(View.GONE);
                        // days.remove(0);
                    }
                }
            }
        }
    }

    /**
     * The type Get bitmap from url async.
     */
/* GetBitmapFromURLAsync and getBitmapFromURL
    *http://stackoverflow.com/questions/37510411/download-an-image-into-bitmap-file-in-android
    */
    private class GetBitmapFromURLAsync extends AsyncTask<String, Void, Bitmap> {
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
    private Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Replace add plant values from Preferences.
     *
     * @param plantsObject the plants object
     */
    private void ReplaceAddPlantValues(PlantsObject plantsObject) {
        EditText name = (EditText) findViewById(R.id.editName);
        EditText potDiam = (EditText) findViewById(R.id.editPotDiameter);
        EditText fert = (EditText) findViewById(R.id.editFertalizer);
        Spinner water = (Spinner) findViewById(R.id.selectWaterNeed);
        name.setText(plantsObject.getName());
        potDiam.setText(plantsObject.getSpacing());
        fert.setText(plantsObject.getSoilPH());
        water.setSelection(Integer.parseInt(plantsObject.getWaterReq()));
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        if (plantsObject.getCheckDays() != null) {
            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                if (linearLayout.getChildAt(i) instanceof CheckBox) {
                    String key = "Day " + String.valueOf(((CheckBox) linearLayout.getChildAt(i)).getContentDescription());
                    if (plantsObject.getCheckDays().containsKey(key)) {
                        ((CheckBox) linearLayout.getChildAt(i)).setChecked(plantsObject.getCheckDays().get(key));
//                        plantsObject.getCheckDays().remove(0);
                    }
                }
            }
        }
    }

    private boolean checkFields() {
        boolean valid = true;
        EditText name = (EditText) findViewById(R.id.editName);
        if (name.getText().toString().isEmpty()) {
            valid = false;
            name.setError("Must Enter a Name");
        }
        EditText potDiam = (EditText) findViewById(R.id.editPotDiameter);
        if (potDiam.getText().toString().isEmpty()) {
            valid = false;
            potDiam.setError("Must Enter a Diameter");
        }
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        boolean hasCheck = false;
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            if (linearLayout.getChildAt(i) instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) linearLayout.getChildAt(i);
                if (checkBox.isChecked()) {
                    hasCheck = true;
                }
            }
        }
        if (hasCheck == false) {
            valid = false;
            Toast.makeText(getApplicationContext(), "Must Select at Least One Day to Water",
                    Toast.LENGTH_SHORT).show();
        }
        return valid;
    }

    /**
     * Sets a plantobject.
     *
     * @return the plant object
     */
    private PlantsObject setPlantObject() {
        PlantsObject tempPlant = new PlantsObject();
        EditText name = (EditText) findViewById(R.id.editName);
        EditText potDiam = (EditText) findViewById(R.id.editPotDiameter);
        EditText fert = (EditText) findViewById(R.id.editFertalizer);
        Spinner water = (Spinner) findViewById(R.id.selectWaterNeed);
        if (bitmapPlant != null) {
            String image = BitMapToString(bitmapPlant);
            Log.d("Image length", String.valueOf(image.length()));
            tempPlant.setImage(image);
        }
        tempPlant.setName(name.getText().toString());
        tempPlant.setSoilPH(fert.getText().toString());
        tempPlant.setSpacing(potDiam.getText().toString());
        Log.d("Spinner Position", String.valueOf(water.getSelectedItemPosition()));
        tempPlant.setWaterReq(String.valueOf(water.getSelectedItemPosition()));
        Map<String, Boolean> checkDays = new HashMap<>();
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            if (linearLayout.getChildAt(i) instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) linearLayout.getChildAt(i);
                checkDays.put("Day " + String.valueOf(checkBox.getContentDescription()), checkBox.isChecked());
            }
        }
        tempPlant.setCheckDays(checkDays);
        Log.d("Set Plant: ", tempPlant.toString());
        return tempPlant;
    }

    /**
     * Sets plant firebase.
     */
    private void setPlantFireBase() {
        PlantsObject tempPlant = setPlantObject();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        if (!oldName.equals(tempPlant.getName()) && !oldName.isEmpty()) {
            mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(oldName).removeValue();
        }
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
