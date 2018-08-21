package com.example.jodeci.passwordmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;

public class GeneratorView extends AppCompatActivity {

    private Switch swtchChars;
    private Switch swtchCaps;
    private Switch swtchLower;
    private Switch swtchSymbols;
    private Switch swtchNumbers;

    private SeekBar barChars;
    private SeekBar barCaps;
    private SeekBar barLower;
    private SeekBar barSymbols;
    private SeekBar barNumbers;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generator_view);
        initWidgets();
    }

    private void initWidgets(){
        swtchChars = (Switch) findViewById(R.id.swtch_chars);
        swtchCaps = (Switch) findViewById(R.id.swtch_caps);
        swtchLower = (Switch) findViewById(R.id.swtch_lower);
        swtchSymbols = (Switch) findViewById(R.id.swtch_symbols);
        swtchNumbers = (Switch) findViewById(R.id.swtch_numbers);
        barChars = (SeekBar) findViewById(R.id.bar_chars);
        barCaps = (SeekBar) findViewById(R.id.bar_caps);
        barLower = (SeekBar) findViewById(R.id.bar_lower);
        barSymbols = (SeekBar) findViewById(R.id.bar_symbols);
        barNumbers = (SeekBar) findViewById(R.id.bar_numbers);

        setSwitchListener(swtchChars, barChars);
        setSwitchListener(swtchCaps, barCaps);
        setSwitchListener(swtchLower, barLower);
        setSwitchListener(swtchSymbols, barSymbols);
        setSwitchListener(swtchNumbers , barNumbers);
    }

    private void setSwitchListener(Switch swtch, final SeekBar sbar ){
        swtch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sbar.setEnabled(true);
                }
                if (!isChecked){
                    sbar.setEnabled(false);
                }
            }
        });
    }
}
