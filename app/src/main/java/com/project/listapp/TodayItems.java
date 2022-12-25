package com.project.listapp;

import android.nfc.Tag;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.project.listapp.MainActivity;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.mongodb.RealmResultTask;
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
    ArrayList<Task> list;

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

        MainActivity.thread.beginTransaction();
        list.clear();
        RealmResults<Task> Tasks = MainActivity.thread.where(Task.class).findAll();
        long counter = MainActivity.thread.where(Task.class).findAll().stream().count();
        for (int i=0;i<counter;i++) {
            Task otherTask = Tasks.get(i);

            Log.d(TAG, "onCreateView: " + otherTask.getTitle());

            list.add(otherTask);
        }
        adpt=new Adapter(this.getContext(),list);
        MainActivity.thread.commitTransaction();


        adpt.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));




    }




}

