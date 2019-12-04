package com.example.drana_000.bluetoothyoklamasistemi;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import static com.example.drana_000.bluetoothyoklamasistemi.YoklamaActivity.manuelYoklamaKontrol;


/**
 * Created by Drana_000 on 17.5.2017.
 */

public class ListeAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Ogrenciler> mOgrenciListesi;


    public ListeAdapter(Activity activity, List<Ogrenciler> ogrencilers)
    {
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mOgrenciListesi = ogrencilers;
    }

    @Override
    public int getCount()
    {
        return mOgrenciListesi.size();
    }

    @Override
    public Ogrenciler getItem(int position)
    {
        return mOgrenciListesi.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    private class ViewHolder {
        TextView tv;
        CheckBox cb;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {

        final ViewHolder holder ;
        if (convertView == null) {


            //satirView = mInflater.inflate(R.layout.eleman_tasarim, null);
            convertView =mInflater.inflate(R.layout.eleman_tasarim,parent,false);
            holder=new ViewHolder();
            holder.tv = (TextView)convertView.findViewById(R.id.textView);
            holder.cb = (CheckBox)convertView.findViewById(R.id.checkBox1);
            convertView.setTag(holder);
        }
        else
        {
            holder=(ViewHolder)convertView.getTag();

        }
        holder.tv.setText(mOgrenciListesi.get(position).getOgrenciNo());

        if(mOgrenciListesi.get(position).getDerseKatilim()==true)
            holder.cb.setChecked(true);
        else
            holder.cb.setChecked(false);

        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(manuelYoklamaKontrol==true) {
                    mOgrenciListesi.get(position).derseKatilim = !mOgrenciListesi.get(position).derseKatilim;
                    if (mOgrenciListesi.get(position).getDerseKatilim())
                        holder.cb.setChecked(true);
                    else
                        holder.cb.setChecked(false);
                }
            }
        });


        return convertView;
    }


}
