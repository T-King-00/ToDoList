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

import java.util.ArrayList;
import java.util.Locale;

import io.realm.Realm;
import io.realm.mongodb.User;


public class EditExistingTaskActivity extends AppCompatActivity {


    Button editBtn;
    ImageButton voiceBtn;
    EditText TaskTitleTxt;
    EditText TaskDescTxt;
    EditText priorityText ;
    private final int REQ_CODE=100;
    static User mUser;
    Task nTask;
    String uID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        //deleting old one

        //getting data passed by bundle;
        Bundle varGetBundle=getIntent().getExtras();
        String uID=varGetBundle.getString("userID");
        nTask=new Task();
        nTask.setUserID(uID);
        Log.d(TAG, "onCreate: obj id"+ varGetBundle.getString("task id").toString());

        ObjectId tID=new ObjectId (varGetBundle.getString("task id").toString());
        nTask.set_id(String.valueOf(tID));


        editBtn =(Button) findViewById(R.id.editItemBtn);
        voiceBtn=(ImageButton) findViewById(R.id.voiceBtn);
        TaskTitleTxt=(EditText)findViewById(R.id.TaskNameTxt);
        TaskDescTxt =(EditText)findViewById(R.id.NoteTxt);
        priorityText = (EditText) findViewById(R.id.priorityTxt);

        getData();
        //related to voice to text functionality.
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
                    Toast.makeText(EditExistingTaskActivity.this, "sorry your device not supported", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    // after showing activity of voice prompt .
    //we need to get data from it so we use this method
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
   public void getData(){

       MainActivity.init(getApplicationContext());
       MainActivity.thread.beginTransaction();
       Log.d(TAG, "getData: "+nTask.get_id());
       Task editThisTask = MainActivity.thread.where(Task.class).equalTo("userID",nTask.getUserID()).equalTo("_id",nTask.get_id()).findFirst();
       MainActivity.thread.commitTransaction();
       MainActivity.thread.close();

       TaskTitleTxt.setText(editThisTask.getTitle().toString());
       TaskDescTxt.setText(editThisTask.getDescription().toString());
       priorityText.setText(String.valueOf(editThisTask.getPriority()));


   }

    public void updateItem(View view) {

        //local database
        nTask.setTitle(TaskTitleTxt.getText().toString());
        nTask.setDescription(TaskDescTxt.getText().toString());
        nTask.setPriority(Integer.parseInt(priorityText.getText().toString()));
        //nTask.setFinished();
        dataInCloud.updateTask(nTask);

        MainActivity.thread = Realm.getDefaultInstance();
        MainActivity.thread.beginTransaction();
        MainActivity.thread.copyToRealmOrUpdate(nTask);
        MainActivity.thread.commitTransaction();
        MainActivity.thread.close();

        Toast.makeText(EditExistingTaskActivity.this, "item updated", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "update: item updated local db");


    }

    public void DeleteItem(View view) {

        try {
            dataInCloud.delete(nTask);
            //local database
            MainActivity.thread=Realm.getDefaultInstance();
            MainActivity.thread.beginTransaction();
            Task Tasks = MainActivity.thread.where(Task.class).equalTo("_id",nTask.get_id()).findFirst();
            // on below line we are executing a realm transaction.
            if (Tasks != null) {
                Tasks.deleteFromRealm();
            }
            MainActivity.thread.commitTransaction();
            MainActivity.thread.close();

            Toast.makeText(EditExistingTaskActivity.this, "item deleted", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "update: item deleted from local db");

        }catch (Exception e)
        {
            Log.d(TAG, "DeleteItem: "+e.getMessage());
        }


    }
    public void close(View view) {
        Intent backIntent=new Intent(this,homeActivity.class);
        startActivity(backIntent);
    }


}


