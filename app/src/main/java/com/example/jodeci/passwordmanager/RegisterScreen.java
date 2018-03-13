package com.example.jodeci.passwordmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterScreen extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private EditText confirmPassword;
    private Button createUser;
    private TextView errText;

    private boolean validUsername = false;
    private boolean validPassword = false;

    private  MyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_screen);

        dbHandler = new MyDBHandler(this,null, null,1);

        username = (EditText) findViewById(R.id.txtNewUser);
        password = (EditText) findViewById(R.id.txtNewPass);
        confirmPassword = (EditText) findViewById(R.id.txtConfirm);
        createUser = (Button) findViewById(R.id.btnCreate);
        errText = (TextView) findViewById(R.id.errText);

        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errText.setText("");
                validUsername = validateUsername();
                validPassword = validatePassword();

                if (validUsername && validPassword) {

                    Users user = new Users(username.getText().toString(), password.getText().toString());

                    if (!dbHandler.containsUser(user)) {
                        dbHandler.addUser(user);
                        Toast.makeText(RegisterScreen.this,"User added successfully.",Toast.LENGTH_SHORT).show();
                        finish();
                    } else{
                        errText.setText("User already exists");
                    }

                }

            }
        });
    }

    //make sure username meets requirements
    private boolean validateUsername(){
        String test = username.getText().toString().trim();
        if (test == null || test.equals("")){
            errText.setText("Please fill in username.");
            return false;
        }

        if (username.getText().toString().matches("^.*[^a-zA-Z0-9].*$")){
            errText.setText("Username contains invalid characters.");
            return false;
        }

        return true;
    }

    private boolean validatePassword(){
        String test1 = password.getText().toString().trim();
        String test2 = confirmPassword.getText().toString().trim();
        //passwords must match
        if (!test1.equals(test2)){
            String old = errText.getText().toString();
            errText.setText(old + " Password mismatch.");
            return false;
        }
        //no spaces
        if (test1 == null || test1.equals("")){
            String old = errText.getText().toString();
            errText.setText(old + " Please fill in password.");
            return false;
        }

        if (test1.matches("^.*[^a-zA-Z0-9!@#$].*$")){
            String old = errText.getText().toString();
            errText.setText(old + " Password contains invalid characters.");
            return false;
        }

        return true;
    }

}
