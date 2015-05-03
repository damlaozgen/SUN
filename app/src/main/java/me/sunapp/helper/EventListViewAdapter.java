package me.sunapp.helper;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import me.sunapp.ContextManager;
import me.sunapp.R;
import me.sunapp.model.Event;

public class EventListViewAdapter extends ArrayAdapter<Event> {
    private final ArrayList<Event> items;

    public EventListViewAdapter(ArrayList<Event> values) {
        super(ContextManager.getInstance().getAppContext(), R.layout.event_row, values);
        this.items = values;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) ContextManager.getInstance().getAppContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.event_row, parent, false);
        }
        TextView name = (TextView)convertView.findViewById(R.id.event_name);

        Event obj = items.get(position);
        name.setText(obj.getName());

        return convertView;
    }
}
