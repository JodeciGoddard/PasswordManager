package com.example.jodeci.passwordmanager.Profile;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.jodeci.passwordmanager.R;
import com.example.jodeci.passwordmanager.Util.Preferences;
import com.example.jodeci.passwordmanager.database.DataViewModel;
import com.example.jodeci.passwordmanager.database.Profiles;

public class AddProfileView extends AppCompatActivity {

    private EditText txtName;
    private ImageView preview;
    private SeekBar red;
    private SeekBar green;
    private SeekBar blue;
    private int color;
    private Button save;

    private DataViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_profile_view);

        //ToolBar setup
        Toolbar toolbar = findViewById(R.id.newProfile_toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("New Profile");
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);

        mViewModel = ViewModelProviders.of(this).get(DataViewModel.class);

        txtName = findViewById(R.id.txtProfileName);
        preview = findViewById(R.id.imgPreview);
        red = findViewById(R.id.barRed);
        green = findViewById(R.id.barGreen);
        blue = findViewById(R.id.barBlue);
        save = findViewById(R.id.btnNewProfileSave);

        //setup the bars
        red.setMax(255);
        green.setMax(255);
        blue.setMax(255);

        //set default preview color
        setPreviewColor(red.getProgress(), green.getProgress(), blue.getProgress());

        seekBarListener(red);
        seekBarListener(green);
        seekBarListener(blue);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateProfileName()){
                    Profiles profile = new Profiles(txtName.getText().toString(), color);
                    long id = mViewModel.insert(profile);
                    profile.id = (int) id;
                    Preferences.saveCurrentProfile(profile, AddProfileView.this);
                    Toast.makeText(AddProfileView.this, "Profile Saved", Toast.LENGTH_LONG).show();
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
        preview.setImageDrawable(createCircle(color));
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }


    private GradientDrawable createCircle(int col){
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setColor(col);
        shape.setStroke(2, col);
        return shape;
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
}
