package com.example.jodeci.passwordmanager;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.R.attr.entries;
import static android.R.attr.resource;

/**
 * Created by jodec on 10/23/2017.
 */

public class CustomAdapter extends ArrayAdapter<Entry> {
    Context context;
    ArrayList<Entry> entries;


    public CustomAdapter(Context context, ArrayList<Entry> entries) {
        super(context, R.layout.custom_view_layout,entries);

        this.context = context;
        this.entries = entries;

    }


    public void remove(int id) {
        for (Entry p : entries) {
            if(p.get_id() == id ){
                entries.remove(p);
            }
        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.custom_view_layout, parent, false);

            holder = new ViewHolder();
            holder.myEntry = entries.get(position);
            holder.appname = (TextView) convertView.findViewById(R.id.lblDisplay1);
            holder.user = (TextView) convertView.findViewById(R.id.lblDisplay2);
            holder.pass = (TextView) convertView.findViewById(R.id.lblDisplay3);
            holder.deleteEntry = (ImageButton) convertView.findViewById(R.id.btnRubbish);
            holder.editEntry = (ImageButton) convertView.findViewById(R.id.btnEdit);
            holder.deleteEntry.setTag(holder.myEntry);
            holder.editEntry.setTag(holder.myEntry);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.myEntry = entries.get(position);
        holder.appname.setText( entries.get(position).get_applicationName() );
        holder.user.setText( entries.get(position).get_appUsername() );
        holder.pass.setText( entries.get(position).get_appPassword() );
        holder.id = entries.get(position).get_id();
        holder.deleteEntry.setTag(holder.myEntry);
        holder.editEntry.setTag(holder.myEntry);


        return convertView;
    }

    public static class ViewHolder {
        Entry myEntry;
        TextView appname, user, pass;
        ImageButton deleteEntry, editEntry;
        public int id;
    }
}
