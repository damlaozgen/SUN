package me.sunapp.helper;

import android.content.Context;
import android.graphics.Color;
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
import me.sunapp.client.SUNClient;
import me.sunapp.model.Student;

public class LeaderBoardListAdapter extends ArrayAdapter<Student>{
    private ArrayList<Student> students;

    public LeaderBoardListAdapter(ArrayList<Student> students){
        super(ContextManager.getInstance().getAppContext(), R.layout.leaderboard_row, students);
        this.students = students;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) ContextManager.getInstance().getAppContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.leaderboard_row, parent, false);
        }
        TextView name = (TextView)convertView.findViewById(R.id.student_name);

        Student obj = students.get(position);
        name.setText(obj.getName());

        TextView points = (TextView)convertView.findViewById(R.id.points);
        points.setText(""+obj.getPoints());

        ImageView avatar = (ImageView)convertView.findViewById(R.id.student_row_avatar);
        ImageLoader.getInstance().displayImage(obj.getAvatar(), avatar);

        if(obj.getId() == SUNClient.getInstance().getCurrentUser().getId()){
            convertView.setBackgroundColor(Color.GRAY);
        }else{
            convertView.setBackgroundColor(Color.TRANSPARENT);
        }

        return convertView;
    }

}
