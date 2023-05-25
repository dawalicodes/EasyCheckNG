package com.dawalisoftwares.easycheck.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.dawalisoftwares.easycheck.INFO;
import com.dawalisoftwares.easycheck.R;
import com.dawalisoftwares.easycheck.Sim;
import com.dawalisoftwares.easycheck.interfaces.EasyReveal;

import java.util.List;

public class BankAirtimeReveal extends EasyReveal {
    private List<Sim> sims;
    public BankAirtimeReveal(List<Sim> sims) {
        this.sims = sims;
    }

    @Override
    protected void onCreate(Context context) {
    }

    @Override
    protected View onCreateView(ViewGroup viewGroup, LayoutInflater layoutInflater) {
        View view = layoutInflater.inflate(R.layout.airtime_from_bank,viewGroup,false);
        EasyReveal easyReveal = new SelectSimReveal(sims, INFO.LOAD_AIRTIME_FROM_BANK,new Bundle());
        ConstraintLayout simHolder = view.findViewById(R.id.sim_place_holder);
        easyReveal.addToView(simHolder);
        return view;
    }

}
