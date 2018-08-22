package com.example.jodeci.passwordmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

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

    private Button btnGenerate;

    private EditText txtPhrase;
    private TextView lblTest;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generator_view);
        initWidgets();

        btnGenerate = (Button) findViewById(R.id.btnGenerate);

        txtPhrase = (EditText) findViewById(R.id.txtPhrase);
        lblTest = (TextView) findViewById(R.id.lblTestArea);

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get inputs
                int chars,caps,lower,symbols,numbers;
                String phrase;

                if(swtchChars.isChecked()){
                    chars = barChars.getProgress();
                } else {
                    chars = -1;
                }

                if(swtchCaps.isChecked()){
                    caps = barCaps.getProgress();
                } else {
                    caps = -1;
                }

                if(swtchLower.isChecked()){
                    lower = barLower.getProgress();
                } else {
                    lower = -1;
                }

                if(swtchSymbols.isChecked()){
                    symbols = barSymbols.getProgress();
                } else {
                    symbols = -1;
                }

                if(swtchNumbers.isChecked()){
                    numbers = barNumbers.getProgress();
                } else {
                    numbers = -1;
                }

                phrase = txtPhrase.getText().toString();

                PasswordGenerator gen = new PasswordGenerator(chars,caps,lower,symbols,numbers, phrase);
                String result = gen.getRandomPassword();

                lblTest.setText(result);

            }
        });

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
