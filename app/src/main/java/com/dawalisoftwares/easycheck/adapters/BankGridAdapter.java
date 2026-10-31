package com.dawalisoftwares.easycheck.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.dawalisoftwares.easycheck.R;
import com.dawalisoftwares.easycheck.interfaces.Bank;
import com.dawalisoftwares.easycheck.interfaces.OnBankSelectedListener;

import java.util.List;
import java.util.Objects;

public class BankGridAdapter extends ArrayAdapter<Bank> {
    private final int resource;
    private final OnBankSelectedListener bankSelectedListener;
    public BankGridAdapter(@NonNull Context context, int resource, @NonNull List<Bank> banks, OnBankSelectedListener bankSelectedListener) {
        super(context,resource,R.id.bank_name,banks);
        this.bankSelectedListener = bankSelectedListener;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resource,parent,false);
        }
        LinearLayoutCompat bankFace = convertView.findViewById(R.id.bank_face_parent);
        if (bankSelectedListener != null) {
            bankFace.setOnClickListener(view -> bankSelectedListener.bankSelected(Objects.requireNonNull(getItem(position))));
        }
        AppCompatImageView logo = convertView.findViewById(R.id.bank_logo);
        logo.setImageResource(Objects.requireNonNull(getItem(position)).getLogo());
        TextView name = convertView.findViewById(R.id.bank_name);
        name.setText(Objects.requireNonNull(getItem(position)).getName());
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       return getView(position,convertView,parent);
    }
}
