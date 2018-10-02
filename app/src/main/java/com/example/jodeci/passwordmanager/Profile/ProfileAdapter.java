package com.example.jodeci.passwordmanager.Profile;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jodeci.passwordmanager.Home.HomeAdapter;
import com.example.jodeci.passwordmanager.R;
import com.example.jodeci.passwordmanager.Util.Preferences;
import com.example.jodeci.passwordmanager.Util.Util;
import com.example.jodeci.passwordmanager.database.DataViewModel;
import com.example.jodeci.passwordmanager.database.Profiles;

import java.util.List;

/**
 * Created by jodeci on 9/21/2018.
 */

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

    private Context context;
    private DataViewModel mViewModel;
    private List<Profiles> mProfileList;
    private ProfileView rootParent;
    private Profiles activeProfile;

    public static String INTENT_KEY = "Profile_id";
    public static int EDIT_PROFILE_REQ = 100;

    public ProfileAdapter(Context context, DataViewModel viewModel){
        this.context = context;
        this.mViewModel = viewModel;
        this.mProfileList = mViewModel.getProfiles();
        this.rootParent = (ProfileView) context;
        activeProfile = mViewModel.getProfileByID(Preferences.getCurrentProfileID(context));
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new ProfileAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.profile_view_item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(mProfileList != null){
            holder.btnIcon.setText(mProfileList.get(position).firstLetter());
            holder.btnIcon.setBackground(Util.createCircle(mProfileList.get(position).color));
            holder.lblName.setText(mProfileList.get(position).name);
            holder.lblName.setTextColor(mProfileList.get(position).color);

            setItemOnclickListerner(holder.itemView, mProfileList.get(position));
            setItemButtonOnclickListener(holder, mProfileList.get(position));

            //set the active profile
            if(mProfileList.get(position).id == activeProfile.id){
                holder.txtActive.setVisibility(View.VISIBLE);
            } else {
                holder.txtActive.setVisibility(View.INVISIBLE);
            }
        }


    }

    private void setItemOnclickListerner(View itemView, final Profiles profile) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditProfile.class);
                intent.putExtra("Profile_id", profile.id);
                rootParent.startActivityForResult(intent, EDIT_PROFILE_REQ);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mProfileList != null){
            return mProfileList.size();
        }
        return 0;
    }

    public void removeAt(int adapterPosition) {
        if (activeProfile.id == mProfileList.get(adapterPosition).id){
            Toast.makeText(context,"Cannot delete Active Profile", Toast.LENGTH_LONG).show();
            return;
        }
        mViewModel.deleteProfile(mProfileList.get(adapterPosition));
        mProfileList.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private Button btnIcon;
        private TextView lblName, txtActive;
        private  View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            btnIcon = itemView.findViewById(R.id.btnProfileIcon);
            lblName = itemView.findViewById(R.id.lblNameIcon);
            txtActive = itemView.findViewById(R.id.txtActive);
        }
    }

    public void notifyItemChanged(){
        mProfileList = mViewModel.getProfiles();
        notifyDataSetChanged();
    }

    public int getPostionOfItem(Profiles p){
        int position = 0;
        for(position = 0; position < mProfileList.size(); position++){
            if(p.id == mProfileList.get(position).id){
                return position;
            }
        }
        return -1;
    }

    private void setItemButtonOnclickListener(final ViewHolder holder, final Profiles p){
        holder.btnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //change the old active profile
                Preferences.saveCurrentProfile(p, context);
                activeProfile = p;
                notifyDataSetChanged();
            }
        });
    }
}
