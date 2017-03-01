package com.cs246.plantapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.cs246.plantapp.PlantsObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by austingolding on 2/22/17.
 */

public class PlantsAdapter extends ArrayAdapter<PlantsObject> {
    private Context c;
    private ArrayList<PlantsObject> plants;

    public PlantsAdapter(Context c, ArrayList<PlantsObject> plants) {
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
        PlantsObject plantsObject = getItem(position);
        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_items, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.list_items_search);
        name.setText(plantsObject.getName());

        return convertView;
    }
}
