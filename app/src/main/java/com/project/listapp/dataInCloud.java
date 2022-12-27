package com.project.listapp;


import android.util.Log;

import com.google.gson.Gson;

import org.bson.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;
import io.realm.mongodb.mongo.result.DeleteResult;

public class dataInCloud {

    private static final String TAG = "data";
    public static void updateData(){
        //get local data
        ArrayList<Task> listOfTasks=new ArrayList<>();

        MainActivity.thread= Realm.getDefaultInstance();
        MainActivity.thread.beginTransaction();
        RealmResults<Task> TasksR = MainActivity.thread.where(Task.class).findAll();
        long counter = MainActivity.thread.where(Task.class).findAll().stream().count();
        Log.d(TAG, "onResume: +"+counter);
        MainActivity.thread.commitTransaction();
        MainActivity.thread.close();

        ///sync with cloud

        MongoClient monClient= MainActivity.monUser.getMongoClient("mongodb-atlas");

        MongoDatabase monDb= monClient.getDatabase("ToDoListApp");
        MongoCollection<Document> mongoCollection=monDb.getCollection(MainActivity.collectionName);

        //deletes data

        Document queryFilter = new Document().append("userID", MainActivity.monUser.getId());

        mongoCollection.deleteMany(queryFilter).getAsync(new App.Callback<DeleteResult>() {
            @Override
            public void onResult(App.Result<DeleteResult> result) {
                if(result.isSuccess())
                    Log.d(TAG, "DeleteItem: deleeeteeed successffullyy from dbbbb 4 :21");
                else
                    Log.e(TAG, "DeleteItem: deleeeteeed nottt from dbbbb 4 :21");
            }
        });

        if (counter!=0) {
            for (int i = 0; i < counter; i++) {
                listOfTasks.add(TasksR.get(i));
            }
        }
        for (Task i : listOfTasks) {
           insertOne(i);
        }
    }
    public static void delete(Task nTask){
        //cloud database
        MongoClient monClient= MainActivity.monUser.getMongoClient("mongodb-atlas");
        MongoDatabase monDb= monClient.getDatabase("ToDoListApp");
        MongoCollection<Document> mongoCollection=monDb.getCollection(MainActivity.collectionName);

        try {
            Document queryFilter = new Document().append("_id", nTask.get_id());
            mongoCollection.deleteOne(queryFilter).getAsync(new App.Callback<DeleteResult>() {
                @Override
                public void onResult(App.Result<DeleteResult> result) {
                    if(result.isSuccess())
                        Log.d(TAG, "DeleteItem: deleeeteeed successffullyy from dbbbb 4 :21");
                    else
                        Log.e(TAG, "DeleteItem: deleeeteeed nottt from dbbbb 4 :21");
                }
            });

        }catch (Exception e)
        {
            Log.d(TAG, "DeleteItem: "+e.getMessage());
        }
    }
    public static void insertOne(Task nTask){
        //cloud database
        MongoClient monClient;
        MongoDatabase monDb;
        MongoCollection<Document> mongoCollection;
        try {
             monClient= MainActivity.monUser.getMongoClient("mongodb-atlas");
             monDb= monClient.getDatabase("ToDoListApp");
             mongoCollection=monDb.getCollection(MainActivity.collectionName);
            Gson g=new Gson();
            String json=g.toJson(nTask);
            mongoCollection.insertOne(Document.parse(json)).getAsync(result -> {
                if (result.isSuccess())
                {
                    Log.d(TAG, "onClick: data inserted successfully");
                }else
                    Log.d(TAG, "onClick: data  "+result.getError().toString());
            });
        }catch (Exception e)
        {
            Log.d(TAG, "insertOne: "+e.getMessage());
        }

    }
    public static void updateTask(Task mTask){
        MongoClient monClient;
        MongoDatabase monDb;
        MongoCollection<Document> mongoCollection;
        try {
            monClient = MainActivity.monUser.getMongoClient("mongodb-atlas");
            monDb = monClient.getDatabase("ToDoListApp");
            mongoCollection = monDb.getCollection(MainActivity.collectionName);

            Document queryfilter = new Document().append("_id", mTask.get_id());
            RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryfilter).iterator();
            findTask.getAsync(result -> {
                        if (result.isSuccess()) {
                            MongoCursor<Document> results = result.get();

                            if (results.hasNext()) {
                                Log.d(TAG, "updateTask: found sth ");
                                Document doc = results.next();

                                String data1 = mTask.getTitle();
                                String data2 = mTask.getDescription();
                                int data3 = mTask.getPriority();
                                String data4 = mTask.getDateCreated();
                                String data5 = mTask.getLastUpdatedDate();


                                doc.append("title", data1);
                                doc.append("description", data2);
                                String data6=LocalDateTime.now().toString();
                                doc.append("lastUpdatedDate",data6 );
                                doc.append("priority", data3);
                                mongoCollection.updateOne(queryfilter, doc).getAsync(result1 -> {
                                    if (result1.isSuccess()) {
                                        Log.d(TAG, "updateTask: updateedddd");
                                    }
                                    else {
                                        Log.d(TAG, "updateTask: "+ result1.getError());
                                    }
                                });
                            } else {
                                Log.d(TAG, "updateTask: found nth ");
                                insertOne(mTask);

                            }
                        } else
                            Log.d(TAG, "updateTask: error " + result.getError().toString());
                    }
            );
        }catch (Exception e)
        {
            Log.d(TAG, "updateTask: "+e.getMessage());
        }
    }
}
