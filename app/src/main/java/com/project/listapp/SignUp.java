package com.project.listapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLException;

import io.realm.mongodb.App;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;

public class SignUp extends AppCompatActivity {

    //shourt cut : logt
    private static final String TAG = "SignUp";

    Button signup;
    EditText emailTxt;
    EditText password;
    EditText password2;

    com.project.listapp.User objUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        signup =(Button) findViewById(R.id.buttonSignUp);
        emailTxt = (EditText) findViewById(R.id.emailTxt);
        password = (EditText) findViewById(R.id.passwordTxt);
        password2 = (EditText) findViewById(R.id.password2Txt);

        objUser=new com.project.listapp.User();
    }

    public void signUp(View view) {
        objUser.setEmail(emailTxt.getText().toString());
        objUser.setPassword(password2.getText().toString());
        MainActivity.app.getEmailPassword().registerUserAsync(objUser.getEmail(),objUser.getPassword(),result -> {
            if(result.isSuccess()) {
                Toast.makeText(this, "Signed up successfully", Toast.LENGTH_SHORT).show();
                //Log.v(TAG,"Signed up successfully");
                try {
                    LoginUsingEmailAndPassword();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(this, "Failed to sign up", Toast.LENGTH_SHORT).show();
                Log.d(TAG,"Failed to sign up");
            }
        });
    }

    public void LoginUsingEmailAndPassword() throws SQLException {

        objUser.setEmail(emailTxt.getText().toString());
        objUser.setPassword(password2.getText().toString());


        com.project.listapp.User.AuthenticateUser(getApplicationContext(),objUser);

    }
}

