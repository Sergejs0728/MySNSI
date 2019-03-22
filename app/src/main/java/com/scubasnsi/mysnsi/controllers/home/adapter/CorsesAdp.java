package com.scubasnsi.mysnsi.controllers.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.scubasnsi.R;
import com.scubasnsi.mysnsi.model.data_models.Courses;

import java.util.ArrayList;

/**
 * Created by macrew on 2/22/2018.
 */

public class CorsesAdp extends BaseAdapter{

    Context context;
    int flags[];
    String[] countryNames;
    LayoutInflater inflter;
    ArrayList<Courses> mCourses;
    boolean isFirstTime;

    public CorsesAdp(Context applicationContext, ArrayList<Courses> mCourses) {
        this.context = applicationContext;
        this.mCourses=mCourses;
        this.isFirstTime = true;
        inflter = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return this.mCourses.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = inflter.inflate(R.layout.spinner_item, null);
        TextView countryNames = (TextView) view.findViewById(R.id.name);
        countryNames.setText(this.mCourses.get(position).getmCourseName());
        return view;
    }
}
