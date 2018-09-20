package com.example.jodeci.passwordmanager.Generator;

import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;

import com.example.jodeci.passwordmanager.Generator.PasswordGenerator;
import com.example.jodeci.passwordmanager.R;

public class GeneratorView extends AppCompatActivity {

    private Switch swtchChars;
    private Switch swtchCaps;
    private Switch swtchLower;
    private Switch swtchSymbols;
    private Switch swtchNumbers;

    private NumberPicker npChars;
    private NumberPicker npCaps;
    private NumberPicker npLower;
    private NumberPicker npSymbols;
    private NumberPicker npNumbers;

    private TextView lblnumChars;
    private TextView lblnumCaps;
    private TextView lblnumLower;
    private TextView lblnumSymbols;
    private TextView lblNumbers;

    private Button btnGenerate;

    private EditText txtPhrase;
    private TextView lblTest;

    private final String CHAR_STATE = "characterProgressBar";
    private final String CAP_STATE = "capitalProgressBar";
    private final String LOWER_STATE = "lowercaseProgressBar";
    private final String SYMBOL_STATE = "symbolProgreessBar";
    private final String NUMBER_STATE = "numberProgressBar";
    private final String TEXT_PHRASE_STATE = "inputtextbox";
    private final String TEXT_RESULT_STATE = "textResult";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generator_view);
        initWidgets();

        //ToolBar setup
        Toolbar toolbar = (Toolbar) findViewById(R.id.generator_toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Password Generator");
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);

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
                    chars = npChars.getValue();
                } else {
                    chars = -1;
                }

                if(swtchCaps.isChecked()){
                    caps = npCaps.getValue();
                } else {
                    caps = -1;
                }

                if(swtchLower.isChecked()){
                    lower = npLower.getValue();
                } else {
                    lower = -1;
                }

                if(swtchSymbols.isChecked()){
                    symbols = npSymbols.getValue();
                } else {
                    symbols = -1;
                }

                if(swtchNumbers.isChecked()){
                    numbers = npNumbers.getValue();
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

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //save the progress
        outState.putInt(CHAR_STATE, npChars.getValue());
        outState.putInt(CAP_STATE, npCaps.getValue());
        outState.putInt(LOWER_STATE, npLower.getValue());
        outState.putInt(SYMBOL_STATE, npSymbols.getValue());
        outState.putInt(NUMBER_STATE, npNumbers.getValue());
        outState.putString(TEXT_PHRASE_STATE, txtPhrase.getText().toString());
        outState.putString(TEXT_RESULT_STATE, lblTest.getText().toString());

        //saves the view heirarchy
        super.onSaveInstanceState(outState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        restoreState(savedInstanceState);
    }

    private void initWidgets(){
        swtchChars = (Switch) findViewById(R.id.swtch_chars);
        swtchCaps = (Switch) findViewById(R.id.swtch_caps);
        swtchLower = (Switch) findViewById(R.id.swtch_lower);
        swtchSymbols = (Switch) findViewById(R.id.swtch_symbols);
        swtchNumbers = (Switch) findViewById(R.id.swtch_numbers);

        npChars = (NumberPicker) findViewById(R.id.np_char);
        npCaps = (NumberPicker) findViewById(R.id.np_cap);
        npLower = (NumberPicker) findViewById(R.id.np_lower);
        npSymbols = (NumberPicker) findViewById(R.id.np_symbol);
        npNumbers = (NumberPicker) findViewById(R.id.np_num);

        lblnumChars = (TextView) findViewById(R.id.lblNumChar);
        lblnumCaps = (TextView) findViewById(R.id.lblNumCaps);
        lblnumLower = (TextView) findViewById(R.id.lblNumLower);
        lblnumSymbols = (TextView) findViewById(R.id.lblNumSymbols);
        lblNumbers = (TextView) findViewById(R.id.lblNumNumbers);

        setSwitchListener(swtchChars, npChars, lblnumChars);
        setSwitchListener(swtchCaps, npCaps, lblnumCaps);
        setSwitchListener(swtchLower, npLower, lblnumLower);
        setSwitchListener(swtchSymbols, npSymbols, lblnumSymbols);
        setSwitchListener(swtchNumbers , npNumbers, lblNumbers);
    }

    private void setSwitchListener(Switch swtch, final NumberPicker np, final TextView txt ){
        //setup number pickker values
        np.setMinValue(0);
        np.setMaxValue(15);

        swtch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    np.setEnabled(true);
                    txt.setVisibility(View.INVISIBLE);
                }
                if (!isChecked){
                    np.setEnabled(false);
                    txt.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void restoreState(Bundle savedInstanceState){
        npChars.setValue(savedInstanceState.getInt(CHAR_STATE));
        npCaps.setValue(savedInstanceState.getInt(CAP_STATE));
        npLower.setValue(savedInstanceState.getInt(LOWER_STATE));
        npSymbols.setValue(savedInstanceState.getInt(SYMBOL_STATE));
        npNumbers.setValue(savedInstanceState.getInt(NUMBER_STATE));
        txtPhrase.setText(savedInstanceState.getString(TEXT_PHRASE_STATE));
        lblTest.setText(savedInstanceState.getString(TEXT_RESULT_STATE));
    }
}
