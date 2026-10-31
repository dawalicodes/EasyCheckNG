package com.dawalisoftwares.easycheck;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.dawalisoftwares.easycheck.adapters.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class USSDCodes extends AppCompatActivity {

    ViewPager2 viewPager;
    FragmentStateAdapter adapter;
    TabLayout tabLayout;
    TabLayoutMediator mediator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ussdcodes);
        viewPager = findViewById(R.id.ussd_codes_viewpager);
        tabLayout = findViewById(R.id.tab_layout);
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(adapter);
        mediator = new TabLayoutMediator(tabLayout, viewPager, false, true, (tab, position) -> {
            switch (position) {
                case INFO.NETWORK_AIRTEL:
                    tab.setIcon(R.drawable.airtel);
                    break;
                case INFO.NETWORK_GLO:
                    tab.setIcon(R.drawable.glo);
                    break;
                case INFO.NETWORK_MOBILE:
                    tab.setIcon(R.drawable.mobile);
                    break;
                case INFO.NETWORK_MTN:
                    tab.setIcon(R.drawable.mtn);
                    break;
            }
        });
        mediator.attach();

    }
}