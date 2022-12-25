package com.project.listapp;

import static com.project.listapp.User.AuthenticateUser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.SQLException;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.sync.SyncConfiguration;


public class MainActivity extends AppCompatActivity {

    //real m vp variables
    static String appid="application-1-itwtp";
    private static final String TAG = "MainActivity";
    String realmName = "Application-1";
    static Realm thread;
    static io.realm.mongodb.User monUser ;
    static com.project.listapp.User mUser;
    static App app;

    //activity variables:
    private EditText emailTxt;
    private EditText passTxt;
    private Button signInBtn;
    private Button signUpBtn;
    private Button forgetPassBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginscreen_act);
        startUpRealM();


        emailTxt = (EditText) findViewById(R.id.EmailAddressTxt);
        passTxt = (EditText) findViewById(R.id.passwordTxt);
        signInBtn=(Button) findViewById(R.id.btnSignin);
        signUpBtn=(Button) findViewById(R.id.btnSignUp);
        mUser = new com.project.listapp.User();

       /* TaskRealM Task = new TaskRealM("New Task");
        thread.executeTransaction (transactionRealm -> {
            transactionRealm.insert(Task);
        });
        RealmResults<TaskRealM> Tasks = thread.where(TaskRealM.class).findAll();
        RealmResults<TaskRealM> TasksThatBeginWithN = Tasks.where().beginsWith("name", "N").findAll();
        RealmResults<TaskRealM> openTasks = Tasks.where().equalTo("status", TaskStatus.Open.name()).findAll();*/



    }

    public void startUpRealM(){
        Realm.init(this); // context, usually an Activity or Application( initailized once only )


        app=new App(new AppConfiguration.Builder(appid).build());
        RealmConfiguration config = new RealmConfiguration.Builder().name(realmName).build();
        thread = Realm.getInstance(config);

       /* performing read and write operations on the UI thread can lead to unresponsive or slow UI interactions,
        so it's usually best to handle these operations either asynchronously or in a background thread.*/









    }

    public void logIn(View view) {
        mUser.setEmail(emailTxt.getText().toString());
        mUser.setPassword(passTxt.getText().toString());

        //values not empty
        if(!mUser.getEmail().equals("")&&!mUser.getPassword().equals(""))
        {
            try {
                AuthenticateUser(getApplicationContext(),mUser);
                Log.d(TAG, "logIn: "+app.currentUser());
                monUser=app.currentUser();

                /*SyncConfiguration config1 = new SyncConfiguration.Builder(monUser)
                        .allowQueriesOnUiThread(true)
                        .allowWritesOnUiThread(true)
                        .build();
                thread = Realm.getInstance(config1);*/

                Intent intent = new Intent(getApplicationContext(), homeActivity.class);
                startActivity(intent);

            } catch (SQLException e) {
                Log.d(TAG, "onClick: "+e.getMessage());
            }
        }
        else {
            Toast.makeText(MainActivity.this, "Fill in all fields", Toast.LENGTH_SHORT).show();
        }

    }

    public void signUp(View view) {
        Intent newIntent=new Intent(getApplicationContext(),SignUp.class);
        startActivity(newIntent);

    }




}