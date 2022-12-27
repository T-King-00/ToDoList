package com.project.listapp;

import static android.content.ContentValues.TAG;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;

import io.realm.mongodb.User;


public class AddNewTaskActivity extends AppCompatActivity {


    Button addBtn;
    ImageButton voiceBtn;
    EditText TaskTitleTxt;
    EditText NoteTxt;
    EditText priorityText;
    private final int REQ_CODE=100;
    static User mUser;
    Task nTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_task);
        addBtn =(Button) findViewById(R.id.addItemBtnToDb);
        priorityText = (EditText) findViewById(R.id.priorityTxt);
        voiceBtn=(ImageButton) findViewById(R.id.voiceBtn);
        TaskTitleTxt=(EditText)findViewById(R.id.TaskNameTxt);
        NoteTxt=(EditText)findViewById(R.id.NoteTxt);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //adding to local dataabase
                Log.d(TAG, "onClick: Attemting to add to db");
                if (!TaskTitleTxt.getText().toString().equals(""))
                {
                String title=TaskTitleTxt.getText().toString();
                String note=NoteTxt.getText().toString();

                //getting task details
                nTask=new Task();
                nTask.setUserID(MainActivity.monUser.getId());
                nTask.set_id(new ObjectId().toString());
                nTask.setTitle(title);
                nTask.setDescription(note);
                nTask.setFinished(false);
                nTask.setDateCreated(LocalDate.now().toString());
                nTask.setLastUpdatedDate(LocalDate.now().toString());
                nTask.setPriority(0);

                //adding to local databse
                MainActivity.thread.beginTransaction();
                MainActivity.thread.copyToRealmOrUpdate(nTask);
                MainActivity.thread.commitTransaction();
                MainActivity.thread.close();

                Toast.makeText(AddNewTaskActivity.this, "item inserted", Toast.LENGTH_SHORT).show();


                }
            }
        });
        voiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                newIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                newIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                try {
                    startActivityForResult(newIntent,REQ_CODE);
                }
                catch (ActivityNotFoundException e){
                    Toast.makeText(AddNewTaskActivity.this, "sorry your device not supported", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode , int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {

            case REQ_CODE:{
                if (resultCode==RESULT_OK && null !=data)
                {
                    ArrayList result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    TaskTitleTxt.setText((String) result.get(0));
                }
                break;
            }
        }
    }

    public void SetReminder(View view) {

    }

    public void close(View view) {
        dataInCloud.insertOne(nTask);
        Intent backIntent=new Intent(this,homeActivity.class);
        startActivity(backIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}


