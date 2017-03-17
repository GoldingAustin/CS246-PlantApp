package com.cs246.plantapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.cs246.plantapp.Utilities.StringToBitMap;

/**
 * Created by austingolding on 3/16/17.
 */

public class PlantsListAdapter extends ArrayAdapter<PlantsObject> {
    private Context c;
    private ArrayList<PlantsObject> plants;

    /**
     * Instantiates a new Plants adapter.
     *
     * @param c      the c
     * @param plants the plants
     */
    public PlantsListAdapter(Context c, ArrayList<PlantsObject> plants) {
        super(c, 0, plants);
        this.c = c;
        this.plants = plants;
    }

    @Override
    public int getCount() {
        return plants.size();
    }

    @Override
    public PlantsObject getItem(int position) {
        return plants.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        PlantsObject plantsObject = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.plants_list, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.plants_list_name);
        name.setText(plantsObject.getName());
        TextView water = (TextView) convertView.findViewById(R.id.plants_list_water);
        water.setText(plantsObject.getWaterReq());
        TextView diameter = (TextView) convertView.findViewById(R.id.plants_list_diameter);
        diameter.setText(plantsObject.getSpacing());
        TextView fertalizer = (TextView) convertView.findViewById(R.id.plants_list_fertalizer);
        fertalizer.setText(plantsObject.getSoilPH());
        String day = "";
        List<Boolean> checks = plantsObject.getCheckDays();
        for (int i = 0; i < checks.size(); i++) {
            if (checks.get(i)) {
                day += days[i] + " ";
            }
        }
        TextView daysToWater = (TextView) convertView.findViewById(R.id.plants_list_detail);
        daysToWater.setText(day);
        ImageView image = (ImageView) convertView.findViewById(R.id.plants_list_thumbnail);
        Bitmap bitmap = StringToBitMap(plantsObject.getImage());
        image.setImageBitmap(bitmap);

        return convertView;
    }
}
