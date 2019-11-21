package com.example.bluetooth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

class MyAdapter extends ArrayAdapter<D_ScannedDevicesInfo> {
    List<D_ScannedDevicesInfo>arrayList;
    public MyAdapter(@NonNull Context context, int resource, @NonNull List<D_ScannedDevicesInfo> objects) {
        super(context, resource, objects);
        arrayList=objects;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view= LayoutInflater.from(getContext()).inflate(R.layout.singleviewforscanneddevices,parent,false);
        TextView name=view.findViewById(R.id.Name);
        TextView address=view.findViewById(R.id.Address);
        name.setText(arrayList.get(position).Name);
        address.setText(arrayList.get(position).Address);

        return view;
    }

}
