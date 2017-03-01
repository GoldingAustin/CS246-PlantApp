package com.cs246.plantapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class PlantsAdapter extends BaseAdapter {
    private Context c;
    private ArrayList<PlantsObject> plants;
    private LayoutInflater inflater;

    public PlantsAdapter(Context c, ArrayList<PlantsObject> plants) {
        this.c = c;
        inflater = (LayoutInflater)c.getSystemService(c.LAYOUT_INFLATER_SERVICE);
        this.plants = plants;
    }

    @Override
    public int getCount() {
        return plants.size();
    }

    @Override
    public Object getItem(int position) {
        return plants.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder listViewHolder = new ViewHolder();
        if(convertView==null)
        {
            convertView=inflater.inflate(R.layout.content_search,parent,false);
            listViewHolder.plantNames = (TextView)convertView.findViewById(R.id.list_items_search);
            convertView.setTag(listViewHolder);
        }
        else {
            listViewHolder = (ViewHolder)convertView.getTag();
        }
        listViewHolder.plantNames.setText(this.plants.get(position).getName());

        return convertView;
    }

    static class ViewHolder {
        TextView plantNames;
    }
}
