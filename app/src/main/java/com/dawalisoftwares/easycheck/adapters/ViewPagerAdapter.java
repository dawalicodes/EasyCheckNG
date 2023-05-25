package com.dawalisoftwares.easycheck.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.dawalisoftwares.easycheck.INFO;
import com.dawalisoftwares.easycheck.networkcodefragment.AirtelCodes;
import com.dawalisoftwares.easycheck.networkcodefragment.GloCodes;
import com.dawalisoftwares.easycheck.networkcodefragment.MTNcodes;
import com.dawalisoftwares.easycheck.networkcodefragment.MobileCodes;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int i) {
        switch (i)
        {
            case INFO.NETWORK_MTN : return MTNcodes.newInstance();
            case INFO.NETWORK_AIRTEL: return AirtelCodes.newInstance();
            case INFO.NETWORK_GLO: return GloCodes.newInstance();
            case INFO.NETWORK_MOBILE: return MobileCodes.newInstance();
        }
        return new Fragment();
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
