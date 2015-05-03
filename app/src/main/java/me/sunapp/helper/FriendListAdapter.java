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
import me.sunapp.model.Student;

public class FriendListAdapter extends ArrayAdapter<Student>{
    private ArrayList<Student> students;

    public FriendListAdapter(ArrayList<Student> students){
        super(ContextManager.getInstance().getAppContext(), R.layout.student_row, students);
        this.students = students;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) ContextManager.getInstance().getAppContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.student_row, parent, false);
        }
        TextView name = (TextView)convertView.findViewById(R.id.student_name);

        Student obj = students.get(position);
        name.setText(obj.getName());

        ImageView avatar = (ImageView)convertView.findViewById(R.id.student_row_avatar);
        ImageLoader.getInstance().displayImage(obj.getAvatar(), avatar);

        return convertView;
    }
}
