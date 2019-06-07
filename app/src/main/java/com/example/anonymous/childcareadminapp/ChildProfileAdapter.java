package com.example.anonymous.childcareadminapp;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Anonymous on 6/7/2019.
 */

public class ChildProfileAdapter extends ArrayAdapter {
    List list = new ArrayList();
    public ChildProfileAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }

    public void add(@Nullable Child object) {

        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row;
        row = convertView;
        ChildProfileHolder childHolder = new ChildProfileHolder();
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.display_child_list_view, parent,false);
            childHolder.tx_name = (TextView) row.findViewById(R.id.tv_child_name);
            childHolder.tx_age = (TextView) row.findViewById(R.id.tv_child_age);
            childHolder.tx_religion = (TextView) row.findViewById(R.id.tv_child_religion);
            row.setTag(childHolder);
        }
        else{
            childHolder = (ChildProfileHolder) row.getTag();
        }

        Child child = (Child) this.getItem(position);
        childHolder.tx_name.setText(child.getNickname());
        childHolder.tx_age.setText(child.getAge());
        childHolder.tx_religion.setText(child.getReligion());

        return row;
    }

    private static class ChildProfileHolder{
        TextView tx_name, tx_age, tx_religion;
    }
}
