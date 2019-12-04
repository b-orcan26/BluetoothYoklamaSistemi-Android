package com.example.drana_000.bluetoothyoklamasistemi;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class DersAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private JSONArray mDersListesi;
    RadioButton selected=null;


    public DersAdapter(Activity activity, JSONArray dersler)
    {
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDersListesi = dersler;
    }


    @Override
    public int getCount() {
        return mDersListesi.length();
    }

    @Override
    public Object getItem(int i) {

        try {
            return mDersListesi.getJSONObject(i);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public long getItemId(int i) {
        int dersid=0;
        try {
            JSONObject jsonObje=mDersListesi.getJSONObject(i);
            dersid=Integer.parseInt(jsonObje.getString("dersid"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dersid;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {

        final ViewHolder holder ;
        if (convertView == null) {


            //satirView = mInflater.inflate(R.layout.eleman_tasarim, null);
            convertView =mInflater.inflate(R.layout.ders_tasarim,parent,false);
            holder=new ViewHolder();
            holder.rb = (RadioButton) convertView.findViewById(R.id.radioButton5);
            convertView.setTag(holder);
        }
        else
        {
            holder=(ViewHolder)convertView.getTag();

        }
        int dersID=0;
        String dersAdi="";
        try {
            JSONObject jsonObje1=mDersListesi.getJSONObject(position);
            dersID=Integer.parseInt(jsonObje1.getString("dersid"));
            dersAdi=jsonObje1.getString("dersadi");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.rb.setText(dersAdi);
        holder.rb.setId(dersID);
        holder.rb.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(selected != null)
                {
                    selected.setChecked(false);
                }

                holder.rb.setChecked(true);
                selected = holder.rb;
            }
        });



        return convertView;
    }

    private class ViewHolder {
        RadioButton rb;
    }






}

