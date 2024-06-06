package com.hydrogen.hospitalapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.hydrogen.hospitalapp.Adapter.EnrolledTabLayoutAdapter;
import com.hydrogen.hospitalapp.R;

public class EnrolledDepartmentDetails extends AppCompatActivity {

    TabLayout tabLayout;
    TabItem detailsTabItem, reportsTabItem, medicineTabItem, activityTabItem;
    ViewPager tabViewPager;
    EnrolledTabLayoutAdapter enrolledTabLayoutAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrolled_department_details);

        tabLayout = (TabLayout) findViewById(R.id.tabLayoutEnrolledDepartment);
        tabViewPager = (ViewPager) findViewById(R.id.tabViewPagerEnrolled);

        detailsTabItem = (TabItem) findViewById(R.id.detailsTabEnrolled);
        reportsTabItem = (TabItem) findViewById(R.id.reportsTabEnrolled);
        medicineTabItem = (TabItem) findViewById(R.id.medicineTabEnrolled);
        activityTabItem = (TabItem) findViewById(R.id.activityTabEnrolled);

        enrolledTabLayoutAdapter = new EnrolledTabLayoutAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        tabViewPager.setAdapter(enrolledTabLayoutAdapter);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabViewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0 || tab.getPosition() == 1 || tab.getPosition() == 2 || tab.getPosition() == 3 ){
                    enrolledTabLayoutAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tabViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


    }



}