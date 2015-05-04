package me.sunapp.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import me.sunapp.ContextManager;
import me.sunapp.R;
import me.sunapp.model.Event;
import me.sunapp.model.NewsItem;

public class NewsItemListViewAdapter extends ArrayAdapter<NewsItem>{
    ArrayList<NewsItem> items;
    public NewsItemListViewAdapter(ArrayList<NewsItem> list){
        super(ContextManager.getInstance().getAppContext(), R.layout.news_row, list);
        items = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) ContextManager.getInstance().getAppContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.event_row, parent, false);
        }
        TextView name = (TextView)convertView.findViewById(R.id.event_name);

        NewsItem obj = items.get(position);
        name.setText(obj.getText());

        return convertView;
    }
}
