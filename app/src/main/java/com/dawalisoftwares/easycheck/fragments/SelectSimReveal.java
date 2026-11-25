package com.dawalisoftwares.easycheck.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.dawalisoftwares.easycheck.Sim;
import com.dawalisoftwares.easycheck.databinding.ItemSelectSimBinding;
import com.dawalisoftwares.easycheck.databinding.ItemSingleSimBinding;
import com.dawalisoftwares.easycheck.interfaces.EasyReveal;
import com.dawalisoftwares.easycheck.interfaces.Network;
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
            Network network1, network2;
            network1 = simCards.get(0).getNetwork();
            network2 = simCards.get(1).getNetwork();

            binding.simLogoHolderOne.setBackground(ContextCompat.getDrawable(context, network1.getNetworkLogoHolder()));
            binding.simLogoOne.setImageDrawable(ContextCompat.getDrawable(context, network1.getNetworkLogo()));
            binding.simNetworkNameOne.setText(network1.getNetworkName());

            binding.simLogoHolderTwo.setBackground(ContextCompat.getDrawable(context, network2.getNetworkLogoHolder()));
            binding.simLogoTwo.setImageDrawable(ContextCompat.getDrawable(context, network2.getNetworkLogo()));
            binding.simNetworkNameTwo.setText(network2.getNetworkName());

            binding.simCardOne.setOnClickListener(view1 -> simOneClick());
            binding.simCardTwo.setOnClickListener(view2 -> simTwoClick());
        } else  {
            ItemSingleSimBinding binding = ItemSingleSimBinding.inflate(layoutInflater, viewGroup, false);
            view = binding.getRoot();
            Network network = simCards.get(0).getNetwork();
            binding.simLogoHolder.setBackground(ContextCompat.getDrawable(context,network.getNetworkLogoHolder()));
            binding.simLogo.setImageDrawable(ContextCompat.getDrawable(context, network.getNetworkLogo()));
            binding.simNetworkName.setText(network.getNetworkName());
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
