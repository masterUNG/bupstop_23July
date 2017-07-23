package com.example.faster.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by masterUNG on 7/5/2017 AD.
 */

public class BusAdapter extends BaseAdapter{

    private Context context;
    private String[] numberBusStrings, detailBusStrings;
    private TextView numberBusTextView, detailBusTextView;

    public BusAdapter(Context context, String[] numberBusStrings, String[] detailBusStrings) {
        this.context = context;
        this.numberBusStrings = numberBusStrings;
        this.detailBusStrings = detailBusStrings;
    }

    @Override
    public int getCount() {
        return numberBusStrings.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view1 = layoutInflater.inflate(R.layout.bus_listview, viewGroup, false);

        //For NumberBus
        numberBusTextView = (TextView) view1.findViewById(R.id.txtNumberBus);
        numberBusTextView.setText(numberBusStrings[i]);

        //For Detail
        detailBusTextView = (TextView) view1.findViewById(R.id.txtDetailBus);
        detailBusTextView.setText(detailBusStrings[i]);

        return view1;
    }
}   // Main Class
