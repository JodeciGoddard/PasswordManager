package com.example.jodeci.passwordmanager.Profile;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.jodeci.passwordmanager.R;
import com.example.jodeci.passwordmanager.Util.Preferences;
import com.example.jodeci.passwordmanager.Util.Util;
import com.example.jodeci.passwordmanager.database.DataViewModel;
import com.example.jodeci.passwordmanager.database.Items;
import com.example.jodeci.passwordmanager.database.Profiles;

import java.util.List;

public class EditProfile extends AppCompatActivity {

    private EditText txtName;
    private ImageView preview;
    private SeekBar red;
    private SeekBar green;
    private SeekBar blue;
    private int color;
    private Button save;

    private DataViewModel mViewModel;
    private Profiles profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //ToolBar setup
        Toolbar toolbar = findViewById(R.id.newProfile_Edit_toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.TextOnLight));
        toolbar.setTitle("Edit Profile");
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);

        mViewModel = ViewModelProviders.of(this).get(DataViewModel.class);
        Intent intent = getIntent();
        int id = intent.getIntExtra(ProfileAdapter.INTENT_KEY, -1);
        if(id != -1) { profile = mViewModel.getProfileByID(id);}

        txtName = findViewById(R.id.txtEditProfileName);
        preview = findViewById(R.id.imgEditPreview);
        red = findViewById(R.id.barEditRed);
        green = findViewById(R.id.barEditGreen);
        blue = findViewById(R.id.barEditBlue);
        save = findViewById(R.id.btnEditNewProfileSave);

        //setup the bars
        red.setMax(255);
        green.setMax(255);
        blue.setMax(255);

        //set barProgress
        red.setProgress(Color.red(profile.color));
        green.setProgress(Color.green(profile.color));
        blue.setProgress(Color.blue(profile.color));

        //set Text
        txtName.setText(profile.name);

        //set default preview color
        setPreviewColor(red.getProgress(), green.getProgress(), blue.getProgress());

        seekBarListener(red);
        seekBarListener(green);
        seekBarListener(blue);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateProfileName()){
                    changeItemProfileNames(profile.name, txtName.getText().toString());
                    profile.name = txtName.getText().toString();
                    profile.color = color;
                    mViewModel.updateProfile(profile);
                    Toast.makeText(EditProfile.this, "Profile Saved", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });

    }


    private void seekBarListener(SeekBar colorBar){
        colorBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setPreviewColor(red.getProgress(), green.getProgress(), blue.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void setPreviewColor(int r, int g, int b){
        color = Color.argb(255, r,g,b);
        preview.setImageDrawable(Util.createCircle(color));
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }


    private boolean validateProfileName(){
        String test = txtName.getText().toString().trim();
        if(test.equals("")){
            Toast.makeText(this, "Please enter profile name", Toast.LENGTH_LONG).show();
            return false;
        }

        if (txtName.getText().toString().matches("^.*[^a-zA-Z0-9].*$")){
            Toast.makeText(this, "Invalid Name Characters", Toast.LENGTH_LONG).show();
            return false;
        }

        return  true;
    }

    private void changeItemProfileNames(String oldname, String newname){
        List<Items> list = mViewModel.getItemWithProfile(oldname);
        if(list != null && list.size() > 0){
            for (Items item : list){
                item.profile = newname;
                mViewModel.updateItem(item);
            }
        }
    }



}
