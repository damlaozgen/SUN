package me.sunapp.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import me.sunapp.ContextManager;
import me.sunapp.R;
import me.sunapp.model.Joinable;
import me.sunapp.model.Student;

public class InterestListViewAdapter extends ArrayAdapter<Joinable>{
    ArrayList<Joinable> joinables;
    public InterestListViewAdapter(ArrayList<Joinable> items){
        super(ContextManager.getInstance().getAppContext(), R.layout.interest_row, items);
        this.joinables = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) ContextManager.getInstance().getAppContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.interest_row, parent, false);
        }
        TextView name = (TextView)convertView.findViewById(R.id.interest_name);

        Joinable obj = joinables.get(position);
        name.setText(obj.getName());


        return convertView;
    }
}
