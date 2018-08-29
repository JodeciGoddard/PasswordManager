package com.example.jodeci.passwordmanager;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by jodeci on 8/29/2018.
 */

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {
    private ArrayList<Entry> entries;
    private Context context;
    private ProfileView profile;

    public void remove(int id) {
        int index = 0;
        for (Entry p : entries) {
            if(p.get_id() == id ){
                entries.remove(p);
                break;
            }
            index++;
        }
        notifyItemRemoved(index);
    }

    public void updateEntry(Entry e){
        int index = 0;
        for (Entry p : entries) {
            if(p.get_id() == e.get_id() ){
                p.set_applicationName(e.get_applicationName());
                p.set_appUsername(e.get_appUsername());
                p.set_appPassword(e.get_appPassword());
                break;
            }
            index++;
        }
        notifyItemChanged(index);
    }

    public void add(Entry e){
        entries.add(e);
        notifyItemInserted(entries.size() - 1);
    }

    public ProfileAdapter(Context context, ArrayList<Entry> entries, ProfileView profile){
        this.entries = entries;
        this.context = context;
        this.profile = profile;
    }

    @NonNull
    @Override
    public ProfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new ViewHolder(LayoutInflater.from(context).inflate(R.layout.custom_view_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileAdapter.ViewHolder holder, int position) {
        holder.entry = entries.get(position);
        holder.appname.setText( entries.get(position).get_applicationName() );
        holder.user.setText( entries.get(position).get_appUsername() );
        holder.pass.setText( entries.get(position).get_appPassword() );
        holder.id = entries.get(position).get_id();

        setDeleteButtonListener(holder.deleteEntry, holder.entry);
        setEditButtonListener(holder.editEntry,holder.entry);

    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public void setDeleteButtonListener(ImageButton b, final Entry e){

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                Log.i("DELETE: ", "view id: " + e.get_id());
                                remove(e.get_id()); //--make sure the correct id is given
                                profile.setDefaultText();


                                profile.dbHandler.deleteEntry(e.get_id(), profile.username);
                                Log.i("DELETE: " , "" + 0);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure, you wish to delete?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });


    }

    public void setEditButtonListener(ImageButton b, final Entry e){

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuiler = new AlertDialog.Builder(context);
                View mView = profile.getLayoutInflater().inflate(R.layout.dialog_edit_item, null);
                final EditText editApp = (EditText) mView.findViewById(R.id.txtEditAppName);
                final EditText editPass = (EditText) mView.findViewById(R.id.txtEditPassword);
                final EditText editUser = (EditText) mView.findViewById(R.id.txtEditUsername);
                final Button save = (Button) mView.findViewById(R.id.btnEditSave);

                editApp.setText(e.get_applicationName());
                editPass.setText(e.get_appPassword());
                editUser.setText(e.get_appUsername());

                mBuiler.setView(mView);
                final AlertDialog diag = mBuiler.create();

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String appname, user, pass;
                        appname = editApp.getText().toString();
                        user = editUser.getText().toString();
                        pass = editPass.getText().toString();
                        if ( !appname.trim().equals("") && !user.trim().equals("")&& !pass.trim().equals("")){
                            final Entry entry = e;
                            entry.set_applicationName(appname);
                            entry.set_appPassword(pass);
                            entry.set_appUsername(user);

                            //update entry in the database;
                            profile.dbHandler.updateEntry(entry, profile.username);

                            //update the list of entries
                            updateEntry(entry);
                            diag.cancel();
                            Toast.makeText(context,"Change saved",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context,"Please fill all fields",Toast.LENGTH_SHORT).show();
                        }


                    }
                });

                diag.show();
            }
        });



    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView appname, user, pass;
        private ImageButton deleteEntry, editEntry;
        public Entry entry;
        private int id;

        public ViewHolder(View itemView) {
            super(itemView);
            this.appname = (TextView) itemView.findViewById(R.id.lblDisplay1);
            this.user = (TextView) itemView.findViewById(R.id.lblDisplay2);
            this.pass = (TextView) itemView.findViewById(R.id.lblDisplay3);
            this.deleteEntry = (ImageButton) itemView.findViewById(R.id.btnRubbish);
            this.editEntry = (ImageButton) itemView.findViewById(R.id.btnEdit);
        }
    }
}
