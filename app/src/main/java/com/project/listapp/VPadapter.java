package com.project.listapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class VPadapter extends FragmentStateAdapter {



    public VPadapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }
    public int getCount() {
        return 3;
    }



    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0:
                return new TodayItems();
            case 1:
            case 2:
                return new tomorrowItems();
        }

        return null;
    }

    @Override
    public int getItemCount() {
        return getCount();
    }

}



