package com.project.listapp;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Semaphore;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.FindIterable;
import io.realm.mongodb.mongo.iterable.MongoCursor;
import io.realm.mongodb.sync.MutableSubscriptionSet;
import io.realm.mongodb.sync.Subscription;
import io.realm.mongodb.sync.SyncConfiguration;


public class TodayItems extends Fragment {

    public static final  String TAG="TodayItems";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adpt;
    static ArrayList<Task> list;
    Semaphore sem=new Semaphore(0);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_today_items, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.listVItemsLayout);
        list = new ArrayList<>();

        adpt=new Adapter(this.getContext(),list);
        recyclerView.setAdapter(adpt);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        list.clear();
        try {
            getDataFromdb();
         //   sem.acquire();
        } catch (Exception e) {
            e.printStackTrace();
        }

        MainActivity.thread.beginTransaction();
        RealmResults<Task> Tasks = MainActivity.thread.where(Task.class).findAll();
        long counter = MainActivity.thread.where(Task.class).findAll().stream().count();
        MainActivity.thread.commitTransaction();
        if (counter!=0) {
            for (int i = 0; i < counter; i++) {
                Task otherTask = Tasks.get(i);
                //Log.d(TAG, "onCreateView: " + otherTask.getTitle());
                list.add(otherTask);
            }
        }
        adpt = new Adapter(this.getContext(), list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

    public void getDataFromdb() {

        MongoClient monClient = MainActivity.monUser.getMongoClient("mongodb-atlas");
        MongoDatabase monDb = monClient.getDatabase("ToDoListApp");
        MongoCollection<Document> mongoCollection = monDb.getCollection("TasksData");
        Document queryFilter = new Document().append("userID", MainActivity.monUser.getId());
        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();
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
                      //  Log.d(TAG, "getDataFromdb  json : " + json);
                      //  Log.d(TAG, "getDataFromdb task: " + newTask.getTitle());
                        MainActivity.thread.beginTransaction();
                        MainActivity.thread.copyToRealmOrUpdate(newTask);
                        MainActivity.thread.commitTransaction();


                    }
                   // sem.release();
                }

            }
        });
    }


}