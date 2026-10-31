package com.dawalisoftwares.easycheck.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.dawalisoftwares.easycheck.INFO;
import com.dawalisoftwares.easycheck.R;
import com.dawalisoftwares.easycheck.Sim;
import com.dawalisoftwares.easycheck.databinding.ItemSelectSimBinding;
import com.dawalisoftwares.easycheck.databinding.ItemSingleSimBinding;
import com.dawalisoftwares.easycheck.interfaces.EasyReveal;
import com.dawalisoftwares.easycheck.interfaces.OnSimSelected;

import java.util.List;

public class SelectSimReveal extends EasyReveal {

    List<Sim> simCards;
    private final String action;
    private final Bundle bundle;
    private OnSimSelected onSimSelected;
    private Context context;

    public SelectSimReveal(List<Sim> simCards,String action, Bundle bundle) {
        this.simCards = simCards;
        this.action = action;
        this.bundle = bundle;
    }

    @Override
    protected void onCreate(Context context) {
        this.context = context;
        onSimSelected = (OnSimSelected) context;
    }

    @Override
    protected View onCreateView(ViewGroup viewGroup, @NonNull LayoutInflater layoutInflater) {
        View view = null;
        if (simCards.size() > 1) {
            ItemSelectSimBinding binding = ItemSelectSimBinding.inflate(layoutInflater, viewGroup, false);
            view = binding.getRoot();

            switch (simCards.get(0).getMnc()) {
                case INFO.NETWORK_MNC_AIRTEL -> {
                    binding.simLogoHolderOne.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_sim_airtel));
                    binding.simLogoOne.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logo_airtel));
                    binding.simNetworkNameOne.setText(INFO.NETWORK_NAME_AIRTEL);
                }
                case INFO.NETWORK_MNC_MTN -> {
                    binding.simLogoHolderOne.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_sim_mtn));
                    binding.simLogoOne.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logo_mtn));
                    binding.simNetworkNameOne.setText(INFO.NETWORK_NAME_MTN);
                }

                case INFO.NETWORK_MNC_GLO -> {
                    binding.simLogoHolderOne.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_sim_glo));
                    binding.simLogoOne.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logo_glo));
                    binding.simNetworkNameOne.setText(INFO.NETWORK_NAME_GLO);
                }
                case INFO.NETWORK_MNC_MOBILE -> {
                    binding.simLogoHolderOne.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_sim_9mobile));
                    binding.simLogoOne.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logo_9mobile));
                    binding.simNetworkNameOne.setText(INFO.NETWORK_NAME_MOBILE);
                }
            }

            switch (simCards.get(1).getMnc()) {
                case INFO.NETWORK_MNC_AIRTEL -> {
                    binding.simLogoHolderTwo.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_sim_airtel));
                    binding.simLogoTwo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logo_airtel));
                    binding.simNetworkNameTwo.setText(INFO.NETWORK_NAME_AIRTEL);
                }
                case INFO.NETWORK_MNC_MTN -> {
                    binding.simLogoHolderTwo.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_sim_mtn));
                    binding.simLogoTwo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logo_mtn));
                    binding.simNetworkNameTwo.setText(INFO.NETWORK_NAME_MTN);
                }

                case INFO.NETWORK_MNC_GLO -> {
                    binding.simLogoHolderTwo.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_sim_glo));
                    binding.simLogoTwo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logo_glo));
                    binding.simNetworkNameTwo.setText(INFO.NETWORK_NAME_GLO);
                }

                case INFO.NETWORK_MNC_MOBILE -> {
                    binding.simLogoHolderTwo.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_sim_9mobile));
                    binding.simLogoTwo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logo_9mobile));
                    binding.simNetworkNameTwo.setText(INFO.NETWORK_NAME_MOBILE);
                }
            }

            binding.simCardOne.setOnClickListener(view1 -> simOneClick());
            binding.simCardTwo.setOnClickListener(view2 -> simTwoClick());
        } else  {
            ItemSingleSimBinding binding = ItemSingleSimBinding.inflate(layoutInflater, viewGroup, false);
            view = binding.getRoot();
            switch (simCards.get(0).getMnc()) {
                case INFO.NETWORK_MNC_AIRTEL -> {
                    binding.simLogoHolder.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_sim_airtel));
                    binding.simLogo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logo_airtel));
                    binding.simNetworkName.setText(INFO.NETWORK_NAME_AIRTEL);
                }
                case INFO.NETWORK_MNC_MTN -> {
                    binding.simLogoHolder.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_sim_mtn));
                    binding.simLogo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logo_mtn));
                    binding.simNetworkName.setText(INFO.NETWORK_NAME_MTN);
                }
                case INFO.NETWORK_MNC_GLO -> {
                    binding.simLogoHolder.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_sim_glo));
                    binding.simLogo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logo_glo));
                    binding.simNetworkName.setText(INFO.NETWORK_NAME_GLO);
                }
                case INFO.NETWORK_MNC_MOBILE -> {
                    binding.simLogoHolder.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_sim_9mobile));
                    binding.simLogo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logo_9mobile));
                    binding.simNetworkName.setText(INFO.NETWORK_NAME_MOBILE);
                }
            }
        }
        return view;
    }

    public void simOneClick() {
        onSimSelected.onSimSelected(simCards.get(0),bundle,action);
    }

    public void simTwoClick() {
        onSimSelected.onSimSelected(simCards.get(1),bundle,action);
    }

}
