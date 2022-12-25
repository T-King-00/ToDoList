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

import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.sync.ClientResetRequiredError;
import io.realm.mongodb.sync.DiscardUnsyncedChangesStrategy;
import io.realm.mongodb.sync.SyncConfiguration;
import io.realm.mongodb.sync.SyncSession;


public class AddNewTaskActivity extends AppCompatActivity {


    Button btn;
    ImageButton voiceBtn;
    EditText TaskTitleTxt;
    EditText NoteTxt;
    private final int REQ_CODE=100;
    static User mUser;
    Task nTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_task);
        btn=(Button) findViewById(R.id.addItemBtnToDb);
        voiceBtn=(ImageButton) findViewById(R.id.voiceBtn);
        TaskTitleTxt=(EditText)findViewById(R.id.TaskNameTxt);
        NoteTxt=(EditText)findViewById(R.id.NoteTxt);

       /* mAuth=FirebaseAuth.getInstance();
        mFirebaseDb=FirebaseDatabase.getInstance();
        myRef=mFirebaseDb.getReference("note");*/


     /*   mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user= firebaseAuth.getCurrentUser();
                if(user!=null)
                {
                    Log.d(TAG, "onAuthStateChanged: signed in "+user.getUid());
                    Toast.makeText(getApplicationContext(), "Signed in successfully "+ user.getEmail().toString(), Toast.LENGTH_LONG).show();
                    Intent newIntent=new Intent(getApplicationContext(),mainPageActivity.class);
                    startActivity(newIntent);


                }
                else {
                    Log.d(TAG, "onAuthStateChanged: signed out");
                    Toast.makeText(getApplicationContext(), "Signed out successfully", Toast.LENGTH_SHORT).show();
                }
            }
        };
// Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });*/

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Attemting to add to db");
                String title=TaskTitleTxt.getText().toString();
                String note=NoteTxt.getText().toString();

                nTask=new Task();
                nTask.setTitle(title);
                nTask.set_id(new ObjectId());
                nTask.setDescription(note);

                MainActivity.thread.beginTransaction();
                MainActivity.thread.copyToRealmOrUpdate(nTask);
                MainActivity.thread.commitTransaction();
                Toast.makeText(AddNewTaskActivity.this, "item inserted", Toast.LENGTH_SHORT).show();

                String realmName = "My Project";

                SyncConfiguration config = new SyncConfiguration.Builder(MainActivity.monUser)
                        .allowQueriesOnUiThread(true)
                        .allowWritesOnUiThread(true)
                        .build();

                Realm backgroundThreadRealm = Realm.getInstance(config);

                backgroundThreadRealm.executeTransaction (transactionRealm -> {
                     transactionRealm.insert(nTask);
                 });

                syncv();


                if (!note.equals(""))
                {

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

void syncv() {

    // all Tasks in the realm

    MainActivity.app= new App(new AppConfiguration.Builder(MainActivity.appid)
            .defaultSyncClientResetStrategy(new DiscardUnsyncedChangesStrategy() {
                @Override
                public void onBeforeReset(Realm realm) {
                    Log.w("EXAMPLE", "Beginning client reset for " + realm.getPath());
                }
                @Override
                public void onAfterReset(Realm before, Realm after) {
                    Log.w("EXAMPLE", "Finished client reset for " + before.getPath());
                }
                @Override
                public void onError(SyncSession session, ClientResetRequiredError error) {
                    Log.e("EXAMPLE", "Couldn't handle the client reset automatically." +
                            " Falling back to manual recovery: " + error.getErrorMessage());

                }
            })
            .build());


    RealmResults<Task> Tasks = MainActivity.thread.where(Task.class).findAllAsync();
    Tasks.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<Task>>() {
        @Override
        public void onChange(RealmResults<Task> collection, OrderedCollectionChangeSet changeSet) {
            // process deletions in reverse order if maintaining parallel data structures so indices don't change as you iterate
            OrderedCollectionChangeSet.Range[] deletions = changeSet.getDeletionRanges();
            for (OrderedCollectionChangeSet.Range range : deletions) {
                Log.d("QUICKSTART", "Deleted range: " + range.startIndex + " to " + (range.startIndex + range.length - 1));
            }
            OrderedCollectionChangeSet.Range[] insertions = changeSet.getInsertionRanges();
            for (OrderedCollectionChangeSet.Range range : insertions) {
                Log.d("QUICKSTART", "Inserted range: " + range.startIndex + " to " + (range.startIndex + range.length - 1));
            }
            OrderedCollectionChangeSet.Range[] modifications = changeSet.getChangeRanges();
            for (OrderedCollectionChangeSet.Range range : modifications) {
                Log.d("QUICKSTART", "Updated range: " + range.startIndex + " to " + (range.startIndex + range.length - 1));
            }
        }
    });


}}


