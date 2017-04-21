package com.example.tesla.yandextranslator.Data;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tesla.yandextranslator.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class HistoryAdapter extends ArrayAdapter<HistoryTranslate> {
    private Activity activity;
    private ArrayList<HistoryTranslate> translateArrayList;
    private static LayoutInflater inflater = null;
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    public HistoryAdapter (Activity activity, int textViewResourceId, ArrayList<HistoryTranslate> _translateArrayList) {
        super(activity, textViewResourceId, _translateArrayList);
        try {
            this.activity = activity;
            this.translateArrayList = _translateArrayList;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {

        }
    }

    public int getCount() {
        return translateArrayList.size();
    }

    public HistoryTranslate getItem(HistoryTranslate position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder {
        TextView nativeView;
        TextView foreignView;
        TextView languageView;
        TextView dateView;
        ImageView imageViewOff;
        ImageView imageViewOn;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.record_translate_layout, null);
                holder = new ViewHolder();

                holder.nativeView = (TextView) vi.findViewById(R.id.textViewNativeId);
                holder.foreignView = (TextView) vi.findViewById(R.id.textViewForeignId);
                holder.languageView = (TextView) vi.findViewById(R.id.textViewLanguagerId);
                holder.dateView = (TextView) vi.findViewById(R.id.textViewDateId);
                holder.imageViewOff = (ImageView) vi.findViewById(R.id.imageViewOff);
                holder.imageViewOn = (ImageView) vi.findViewById(R.id.imageViewOn);

                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }
            HistoryTranslate historyTranslate = translateArrayList.get(position);
            holder.nativeView.setText(historyTranslate.getNativeValue());
            holder.foreignView.setText(historyTranslate.getForeignValue());
            holder.languageView.setText(historyTranslate.getNativeLanguage() + "-" + historyTranslate.getForeignLanguage());
            holder.dateView.setText(dateFormat.format(historyTranslate.getDate()));
            if(historyTranslate.IsFavorite) {
                holder.imageViewOn.setVisibility(View.VISIBLE);
                holder.imageViewOff.setVisibility(View.INVISIBLE);
            } else {
                holder.imageViewOff.setVisibility(View.VISIBLE);
                holder.imageViewOn.setVisibility(View.INVISIBLE);
            }

        } catch (Exception e) {
            System.out.print("Error - " + e.getMessage());

        }
        return vi;
    }
}
