package com.hydrogen.hospitalapp.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.hydrogen.hospitalapp.Fragment.Medicine;
import com.hydrogen.hospitalapp.Fragment.MoreOptions;
import com.hydrogen.hospitalapp.Fragment.Reports;
import com.hydrogen.hospitalapp.Fragment.DetailsTab;

public class EnrolledTabLayoutAdapter extends FragmentPagerAdapter {
    int tabCount;
    public EnrolledTabLayoutAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        tabCount = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new DetailsTab();
            case 1: return new Reports();
            case 2: return new Medicine();
            case 3: return new MoreOptions();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
