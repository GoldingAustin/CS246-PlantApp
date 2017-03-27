package com.cs246.plantapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.cs246.plantapp.Utilities.StringToBitMap;

/**
 * Created by austingolding on 3/16/17.
 */

public class PlantsListAdapter extends ArrayAdapter<PlantsObject> {
    private Context c;
    private ArrayList<PlantsObject> plants;
    private String measurement;

    /**
     * Instantiates a new Plants adapter.
     *
     * @param c      the c
     * @param plants the plants
     */
    public PlantsListAdapter(Context c, ArrayList<PlantsObject> plants, String measurement) {
        super(c, 0, plants);
        this.c = c;
        this.plants = plants;
        this.measurement = measurement;
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

        String day = "";
        int numDays = 0;
        List<Boolean> checks = plantsObject.getCheckDays();
        for (int i = 0; i < checks.size(); i++) {
            if (checks.get(i)) {
                day += days[i] + " ";
                numDays++;
            }
        }
        TextView daysToWater = (TextView) convertView.findViewById(R.id.plants_list_detail);
        daysToWater.setText(day);
        Pair<String, String> tempWater = convertRequiredWater(plantsObject);
        Double waterPerDay = 0.0;
        if (numDays > 0) {waterPerDay = (Double.valueOf(tempWater.first) / numDays);}
        else { waterPerDay = Double.valueOf(tempWater.first); }
        water.setText(String.valueOf(waterPerDay.intValue()) + " " + tempWater.second + " per day");
        TextView diameter = (TextView) convertView.findViewById(R.id.plants_list_diameter);
        diameter.setText(plantsObject.getSpacing());
        TextView fertalizer = (TextView) convertView.findViewById(R.id.plants_list_fertalizer);
        fertalizer.setText(plantsObject.getSoilPH());

        ImageView image = (ImageView) convertView.findViewById(R.id.plants_list_thumbnail);
        Bitmap bitmap = StringToBitMap(plantsObject.getImage());
        image.setImageBitmap(bitmap);

        return convertView;
    }

    public Pair<String, String> convertRequiredWater(PlantsObject plantsObject) {
        double water = 0;
        switch (plantsObject.getWaterReq()) {
            case "0":
                water = 0.2;
                break;
            case "1":
                water = 0.5;
                break;
            case "2":
                water = 0.8;
                break;
            default:
                break;
        }
        if (measurement.equals("Metric")) {
            double diameter = new Scanner(plantsObject.getSpacing()).useDelimiter("\\D+").nextDouble();
            Log.d("Diameter", String.valueOf(diameter));
            if (plantsObject.getSpacing().contains("in")) diameter = (diameter / 12);
            else if (plantsObject.getSpacing().contains("ft")) diameter = diameter;
            else diameter = (diameter / 12);
            diameter = (diameter / 2) * (diameter / 2);
            double area = diameter * 3.14;
            double gallons = 0.623 * water * area * 1.199166666666667;
            double liter = gallons * 4546.09;
            return new Pair<String, String>(String.format("%.2f", liter), "ml");
        }   else {
            double diameter = new Scanner(plantsObject.getSpacing()).useDelimiter("\\D+").nextDouble();
            Log.d("Diameter", String.valueOf(diameter));
            if (plantsObject.getSpacing().contains("in")) diameter /= 12;
            else diameter /= 12;
            diameter = (diameter / 2) * (diameter / 2);
            double area = diameter * 3.14;
            double gallons = .623 * water * area * 1.199166666666667;
            double cups = gallons * 18.942;
            return new Pair<String, String>(String.format("%.2f", cups), "cups");
        }
    }
}
