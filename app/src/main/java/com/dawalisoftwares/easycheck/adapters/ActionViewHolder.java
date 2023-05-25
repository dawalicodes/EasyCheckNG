package com.dawalisoftwares.easycheck.adapters;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.dawalisoftwares.easycheck.R;
import com.google.android.material.card.MaterialCardView;

public class ActionViewHolder extends RecyclerView.ViewHolder {
    MaterialCardView actionContainer;
    public ActionViewHolder(@NonNull View itemView) {
        super(itemView);
        actionContainer = itemView.findViewById(R.id.sim_card_card);
    }
}
