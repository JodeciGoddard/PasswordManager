package com.example.jodeci.passwordmanager.Home;

import android.content.Context;
import android.content.DialogInterface;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jodeci.passwordmanager.R;
import com.example.jodeci.passwordmanager.database.DataViewModel;
import com.example.jodeci.passwordmanager.database.Items;
import com.example.jodeci.passwordmanager.database.Profiles;

import java.util.List;

/**
 * Created by jodeci on 8/29/2018.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    //private ArrayList<Entry> entries;
    private Context context;
    private HomeView profileView;
    private DataViewModel mViewModel;

    private List<Items> mItems;
    private List<String> mProfileList;

    public HomeAdapter(Context context, HomeView profile, DataViewModel model){
        this.context = context;
        this.profileView = profile;
        this.mViewModel = model;
        mItems = model.getAllitems();
        mProfileList = mViewModel.getProfilesAsString();
    }

    @NonNull
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new ViewHolder(LayoutInflater.from(context).inflate(R.layout.home_view_item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.ViewHolder holder, int position) {

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
        profileView.setDefaultText();
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
                                profileView.setDefaultText();

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
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
                View mView = profileView.getLayoutInflater().inflate(R.layout.dialog_edit_item, null);
                final EditText editApp = mView.findViewById(R.id.txtEditAppName);
                final EditText editPass = mView.findViewById(R.id.txtEditPassword);
                final EditText editUser =  mView.findViewById(R.id.txtEditUsername);
                final Spinner spnProfile = mView.findViewById(R.id.spnEditProfile);
                final Button save = mView.findViewById(R.id.btnEditSave);

                editApp.setText(i.appname);
                editPass.setText(i.password);
                editUser.setText(i.username);

                mBuilder.setView(mView);
                final AlertDialog diag = mBuilder.create();

                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, mProfileList);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnProfile.setAdapter(spinnerAdapter);
                spnProfile.setSelection(spinnerAdapter.getPosition(i.profile));


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
                            i.profile = spnProfile.getSelectedItem().toString();

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

    public void filterList(Profiles profile){
        if(profile.name.equals("Default")){
            mItems = mViewModel.getAllitems();
        } else{
            mItems = mViewModel.getItemWithProfile(profile.name);
        }
        mProfileList = mViewModel.getProfilesAsString();
        notifyDataSetChanged();
    }
}
