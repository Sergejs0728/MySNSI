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

public class CountriesAdp extends BaseAdapter {

    Context context;
    int flags[];
    String[] countryNames;
    LayoutInflater inflter;
    String[] country;



    public CountriesAdp(Context applicationContext, String[] country) {
        this.context = applicationContext;
        this.country=country;
        inflter = (LayoutInflater.from(applicationContext));


    }

    @Override
    public int getCount() {
        return this.country.length;
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
        countryNames.setText(this.country[position]);
        return view;
    }
}
