package com.project.listapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.mongodb.sync.Subscription;
import io.realm.mongodb.sync.SyncConfiguration;

public class tomorrowItems extends Fragment {

    private static final String TAG = "tomorrowItems";

    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_tomorrow_items, container, false);
        listView=(ListView) view.findViewById(R.id.listVItemsLayout);
        arrayAdapter= new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
        listView.setAdapter(arrayAdapter);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();


       /* arrayAdapter.clear();
        MainActivity.thread.beginTransaction();

        RealmResults<Task> Tasks = MainActivity.thread.where(Task.class).findAll();
        long counter = MainActivity.thread.where(Task.class).findAll().stream().count();
        for (int i=0;i<counter;i++) {
            Task otherTask = Tasks.get(i);
            Log.d(TAG, "onCreateView: " + otherTask.getDescription());
            arrayAdapter.add(otherTask.getDescription());
        }

        MainActivity.thread.commitTransaction();
        arrayAdapter.notifyDataSetChanged();
*/

    }
}