package me.sunapp.helper;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import me.sunapp.ContextManager;
import me.sunapp.R;
import me.sunapp.client.SUNClient;
import me.sunapp.model.Course;
import me.sunapp.model.Hobby;
import me.sunapp.model.Joinable;
import me.sunapp.model.Student;

public class InterestListViewAdapter extends ArrayAdapter<Joinable>{
    ArrayList<Joinable> joinables;
    InterestListObserver observer;
    public InterestListViewAdapter(ArrayList<Joinable> items, InterestListObserver observer){
        super(ContextManager.getInstance().getAppContext(), R.layout.interest_row, items);
        this.joinables = items;
        this.observer = observer;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) ContextManager.getInstance().getAppContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.interest_row, parent, false);
        }
        TextView name = (TextView)convertView.findViewById(R.id.interest_name);

        final Joinable obj = joinables.get(position);
        Log.d("Adapter", "obj:"+obj);
        name.setText(obj.getName());
        if(obj instanceof Course){
            name.setTextColor(Color.CYAN);
        }else if(obj instanceof Hobby){
            name.setTextColor(Color.RED);
        }
        Button toggleButton = (Button)convertView.findViewById(R.id.interestToggleButton);
        toggleButton.setText("Add");
        for(Joinable j : SUNClient.getInstance().getCurrentUser().getInterests()){
            if(j.getId() == obj.getId()){
                toggleButton.setText("Remove");
            }
        }
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                observer.toggleInterest(obj);
            }
        });

        return convertView;
    }

    public static interface InterestListObserver{
        public void toggleInterest(Joinable j);
    }
}
