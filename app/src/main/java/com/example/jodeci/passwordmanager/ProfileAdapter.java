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

import com.example.jodeci.passwordmanager.database.DataViewModel;
import com.example.jodeci.passwordmanager.database.Items;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jodeci on 8/29/2018.
 */

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {
    //private ArrayList<Entry> entries;
    private Context context;
    private ProfileView profile;
    private DataViewModel mViewModel;

    private List<Items> mItems;

    public ProfileAdapter(Context context, ProfileView profile, DataViewModel model){
        this.context = context;
        this.profile = profile;
        this.mViewModel = model;
        mItems = model.getAllitems();
    }

    @NonNull
    @Override
    public ProfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new ViewHolder(LayoutInflater.from(context).inflate(R.layout.custom_view_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileAdapter.ViewHolder holder, int position) {

        if(mItems != null){
            holder.appname.setText( mItems.get(position).appname );
            holder.user.setText( mItems.get(position).username);
            holder.pass.setText( mItems.get(position).password);
            holder.id = mItems.get(position).id;

            setDeleteButtonListener(holder.deleteEntry, mItems.get(position));
            setEditButtonListener(holder.editEntry, mItems.get(position));
        }

    }

    @Override
    public int getItemCount() {
        if (mItems != null)
            return mItems.size();
        else return 0;
    }

    public void setItems(List<Items> items){
        mItems = items;
        profile.setDefaultText();
        notifyDataSetChanged();
    }

    public void setDeleteButtonListener(ImageButton b, final Items i){

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:

                                removeItemFromList(i);
                                profile.setDefaultText();

                                Log.i("DELETE: " , "" + i.id);
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

    public void setEditButtonListener(ImageButton b, final Items i){

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuiler = new AlertDialog.Builder(context);
                View mView = profile.getLayoutInflater().inflate(R.layout.dialog_edit_item, null);
                final EditText editApp = (EditText) mView.findViewById(R.id.txtEditAppName);
                final EditText editPass = (EditText) mView.findViewById(R.id.txtEditPassword);
                final EditText editUser = (EditText) mView.findViewById(R.id.txtEditUsername);
                final Button save = (Button) mView.findViewById(R.id.btnEditSave);

                editApp.setText(i.appname);
                editPass.setText(i.password);
                editUser.setText(i.username);

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
                            i.appname = appname;
                            i.password = pass;
                            i.username = user;

                            //update item in datatbase
                            mViewModel.updateItem(i);
                            notifyItemChanged(getItemPosition(i));

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

    public List<Items> getList() {
        return mItems;
    }

    public void insert(Items item) {
        mItems.add(item);
        notifyItemInserted(mItems.size() - 1);
    }

    private void removeItemFromList(Items i){
        mViewModel.deleteByItemID(i.id);

        int position = getItemPosition(i);
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    private int getItemPosition(Items myitem){
        for (int i = 0; i < mItems.size(); i++){
            int compare = mItems.get(i).id;
            if(myitem.id == compare){
                return i;
            }
        }
        return  -1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView appname, user, pass;
        private ImageButton deleteEntry, editEntry;
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
