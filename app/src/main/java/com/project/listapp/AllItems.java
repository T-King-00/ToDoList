package com.project.listapp;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import org.bson.Document;


import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;


public class AllItems extends Fragment implements SelectListener{

    public static final  String TAG="TodayItems";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adpt;
    static ArrayList<Task> list;
    Semaphore sem=new Semaphore(0);
    static boolean isfirsttime=true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_today_items, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.listVItemsLayout);
        list = new ArrayList<>();
        if (isfirsttime)
        {
            try {
                MongoClient monClient = MainActivity.monUser.getMongoClient("mongodb-atlas");
                MongoDatabase monDb = monClient.getDatabase("ToDoListApp");
                MongoCollection<Document> mongoCollection = monDb.getCollection(MainActivity.collectionName);
                Document queryFilter = new Document().append("userID", MainActivity.monUser.getId());
                RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();
                //add it to local database
                findTask.getAsync(new App.Callback<MongoCursor<Document>>() {
                    @Override
                    public void onResult(App.Result<MongoCursor<Document>> result) {
                        if (result.isSuccess()) {
                            MongoCursor<Document> results = result.get();
                            while (results.hasNext()) {
                                Document currentDoc = results.next();
                                String json = currentDoc.toJson().toString();
                                Gson gson = new Gson();
                                Task newTask = gson.fromJson(json, Task.class);
                                Log.d(TAG, "onResult: +" + json);

                                MainActivity.thread=Realm.getDefaultInstance();
                                MainActivity.thread.beginTransaction();

                                MainActivity.thread.copyToRealmOrUpdate(newTask);
                                MainActivity.thread.commitTransaction();
                                MainActivity.thread.close();


                                //  Log.d(TAG, "getDataFromdb  json : " + json);
                                //  Log.d(TAG, "getDataFromdb task: " + newTask.getTitle());
                            }
                        }
                    }
                });
                isfirsttime=false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        adpt=new Adapter(this.getContext(),list,this);
        recyclerView.setAdapter(adpt);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Realm.init(getContext());

        list.clear();


        MainActivity.thread=Realm.getDefaultInstance();
        MainActivity.thread.beginTransaction();
        RealmResults<Task> Tasks = MainActivity.thread.where(Task.class).findAll();

        long counter = MainActivity.thread.where(Task.class).findAll().stream().count();
        Log.d(TAG, "onResume: +"+counter);
        if (counter!=0) {
            for (int i = 0; i < counter; i++) {
                Task otherTask = Tasks.get(i);
                //Log.d(TAG, "onCreateView: " + otherTask.getTitle());
                list.add(otherTask);
            }
        }
        adpt = new Adapter(this.getContext(), list,this);
        adpt.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        MainActivity.thread.commitTransaction();
        MainActivity.thread.close();


      /*  new Thread(() -> {
         dataInCloud.updateData();

        }).start();*/
    }


    @Override
    public void onItemClicked(Task mTask) {

        Toast.makeText(this.getContext(), "ON ITEM CLICKED"+ mTask.getTitle() , Toast.LENGTH_SHORT).show();
        //creating new intent
        Intent intent=new Intent(getContext(),EditExistingTaskActivity.class);
        //sending note , user id to another activity .
        Bundle bundle=new Bundle();
        bundle.putString("userID",MainActivity.monUser.getId());
        bundle.putString("task id",mTask.get_id().toString());

        intent.putExtras(bundle);


        Log.d(TAG, "onItemClicked: going to intent");
        startActivity(intent);

    }

}