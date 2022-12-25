package com.project.listapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.sql.SQLException;

import io.realm.mongodb.App;
import io.realm.mongodb.Credentials;

public class User {
    //shourt cut : logt
    private static final String TAG = "User";
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static void AuthenticateUser(Context x , @NonNull User mUser) throws SQLException {

        Credentials CRED = Credentials.emailPassword(mUser.getEmail(), mUser.getPassword());

        MainActivity.app.loginAsync(CRED, new App.Callback<io.realm.mongodb.User>() {
            @Override
            public void onResult(App.Result<io.realm.mongodb.User> result) {
                if (result.isSuccess()) {
                    Toast.makeText(x, "logged in ", Toast.LENGTH_SHORT).show();
                    MainActivity.monUser=MainActivity.app.currentUser();
                    AddNewTaskActivity.mUser =  MainActivity.monUser;

                    Log.d(TAG, "onResult: logged in");

                } else {
                    Toast.makeText(x, "FAIL TO LOGIN ", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResult: failed to logged in ann");
                }
            }
        });
    }



}
