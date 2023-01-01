package com.project.listapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class homeActivity extends AppCompatActivity {
    private static final String TAG = "homeActivity";

    TabLayout tabLayout;
    ViewPager2 viewPager;

    FloatingActionButton addNewItemBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        MainActivity.init(getApplicationContext());
        tabLayout=findViewById(R.id.tabs);
        viewPager=findViewById(R.id.viewPagerLayout);


        SharedPreferences settings = getSharedPreferences("MyPrefs", 0);
        if (settings.getBoolean("is_first_time", true)) {
            //the app is being launched for first time, do something
            Toast.makeText(this, "first time", Toast.LENGTH_SHORT).show();
            // first time task
            // record the fact that the app has been started at least once
            AllItems.isfirsttime=true;

            settings.edit().putBoolean("is_first_time", false).commit();
        }
        else
        {
            AllItems.isfirsttime=false;
            Toast.makeText(this, "second time", Toast.LENGTH_SHORT).show();
        }
        FragmentManager fragmentManager= getSupportFragmentManager();
        VPadapter  vPadapter=new VPadapter(fragmentManager,getLifecycle());
        //as list view we set adaptor for it .
        viewPager.setAdapter(vPadapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
               tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });


        addNewItemBtn=(FloatingActionButton) findViewById(R.id.addNewItemBtn);


        addNewItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addNewItemIntent=new Intent(getApplicationContext(),AddNewTaskActivity.class);
                startActivity(addNewItemIntent);
            }
        });
    }

    @Override
    protected void onResume() {

        super.onResume();

    }


}