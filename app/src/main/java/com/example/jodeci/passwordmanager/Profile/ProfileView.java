package com.example.jodeci.passwordmanager.Profile;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.example.jodeci.passwordmanager.Home.HomeAdapter;
import com.example.jodeci.passwordmanager.Home.HomeView;
import com.example.jodeci.passwordmanager.R;
import com.example.jodeci.passwordmanager.database.DataViewModel;

public class ProfileView extends AppCompatActivity {

    private DataViewModel mViewModel;
    private RecyclerView view;
    private ProfileAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        mViewModel = ViewModelProviders.of(this).get(DataViewModel.class);

        //ToolBar setup
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        toolbar.setTitleTextColor(getResources().getColor(R.color.TextOnLight));
        toolbar.setTitle("Manage Profiles");
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);

        initialiseListView();

    }

    @Override
    public boolean onSupportNavigateUp(){
        //onBackPressed();
        finish();
        return true;
    }

    public void initialiseListView(){
        view = findViewById(R.id.profileRecyclerView);
        view.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration itemDeco = new DividerItemDecoration(view.getContext(), DividerItemDecoration.HORIZONTAL);
        view.addItemDecoration(itemDeco);
        adapter = new ProfileAdapter(ProfileView.this, mViewModel);
        view.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == ProfileAdapter.EDIT_PROFILE_REQ ) {
            adapter.notifyItemChanged();
        }
    }
}
