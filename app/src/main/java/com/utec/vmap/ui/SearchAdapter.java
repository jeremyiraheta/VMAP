package com.utec.vmap.ui;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.utec.vmap.R;
import com.utec.vmap.Util;
import com.utec.vmap.api.ApiCallback;
import com.utec.vmap.api.RestfulApi;

import java.util.List;

public class SearchAdapter extends CursorAdapter {


    private List<String> list;
    private TextView text;
    public SearchAdapter(Context context, Cursor cursor, List<String> items)
    {
        super(context, cursor, false);
        list = items;
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item, parent, false);

        text = (TextView) view.findViewById(R.id.textS);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = ((TextView)v).getText().toString();
                int value = Util.getPinteres(txt);
                if(!Util.containsPinteres(value))
                {
                    Util.addPinteres(txt);
                    Toast.makeText(view.getContext(),"Punto de interes agregado " + txt,Toast.LENGTH_LONG).show();
                }else{
                    Util.removePintere(value);
                    Toast.makeText(view.getContext(),"Se elimino el punto de interes " + txt,Toast.LENGTH_LONG).show();
                }
                Util.setReload(true);
            }
        });
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        text.setText(list.get(cursor.getPosition()));

    }

}
