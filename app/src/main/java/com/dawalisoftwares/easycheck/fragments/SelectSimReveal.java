package com.dawalisoftwares.easycheck.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dawalisoftwares.easycheck.R;
import com.dawalisoftwares.easycheck.Sim;
import com.dawalisoftwares.easycheck.interfaces.EasyReveal;
import com.dawalisoftwares.easycheck.interfaces.OnSimSelected;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class SelectSimReveal extends EasyReveal {

    List<Sim> simCards;
    private String action;
    private Bundle bundle;
    private OnSimSelected onSimSelected;

    public SelectSimReveal(List<Sim> simCards,String action, Bundle bundle) {
        this.simCards = simCards;
        this.action = action;
        this.bundle = bundle;
    }

    @Override
    protected void onCreate(Context context) {

        onSimSelected = (OnSimSelected) context;
    }

    @Override
    protected View onCreateView(ViewGroup viewGroup, LayoutInflater layoutInflater) {
        View view = null;
        if (simCards.size() > 1) {
            view = layoutInflater.inflate(R.layout.sim_disambiguity, viewGroup, false);
            MaterialButton simOne, simTwo;
            MaterialTextView simOneName, simTwoName;
            simOne = view.findViewById(R.id.sim_one);
            simTwo = view.findViewById(R.id.sim_two);
            simOneName = view.findViewById(R.id.sim_one_name);
            simTwoName = view.findViewById(R.id.sim_two_name);
            simOneName.setText(simCards.get(0).getName().toUpperCase());
            simTwoName.setText(simCards.get(1).getName().toUpperCase());
            simOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onSimSelected.onSimSelected(simCards.get(0), bundle, action);
                }
            });
            simTwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onSimSelected.onSimSelected(simCards.get(1), bundle, action);
                }
            });
        } else  {
            view = layoutInflater.inflate(R.layout.one_sim_implementation,viewGroup,false);
            MaterialButton sim = view.findViewById(R.id.sim);
            sim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onSimSelected.onSimSelected(simCards.get(0),bundle,action);
                }
            });

        }
        return view;
    }


}
