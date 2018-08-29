package com.example.jodeci.passwordmanager;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.widget.Toast.makeText;


/*Password Mananger Application that stores simple Text passwords on your device */

public class LoginScreen extends AppCompatActivity {

    FloatingActionButton newUser;
    Button login;
    EditText username;
    EditText password;
    TextView errText;

    MyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        username = (EditText) findViewById(R.id.txtUsername);
        password = (EditText) findViewById(R.id.txtPassword);
        newUser = (FloatingActionButton) findViewById(R.id.btnRegister);
        login = (Button) findViewById(R.id.btnLogin);
        errText = (TextView) findViewById(R.id.lgErr);

        dbHandler = new MyDBHandler(this,null, null,1);

        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginScreen.this, RegisterScreen.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validateUsername()){
                    Users user = new Users(username.getText().toString(),password.getText().toString());
                    if (user.get_password().equals( dbHandler.getUserPassword(user) )){
                        //Correct Password
                        Intent intent = new Intent(LoginScreen.this, ProfileView.class);
                        intent.putExtra("username",user.get_username());
                        startActivity(intent);
                        errText.setText("");
                    } else {
                        //incorrect password
                        errText.setText("Incorrect Password.");
                        Toast toast = Toast.makeText(getBaseContext(),"Login Failed", Toast.LENGTH_LONG);
                        toast.show();
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


}
