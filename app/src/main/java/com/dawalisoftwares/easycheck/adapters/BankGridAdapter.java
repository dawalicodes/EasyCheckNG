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

public class BankGridAdapter extends ArrayAdapter<Bank> {
    private final OnBankSelectedListener bankSelectedListener;
    public BankGridAdapter(@NonNull Context context, int resource, @NonNull List<Bank> banks, OnBankSelectedListener bankSelectedListener) {
        super(context, resource, banks);
        this.bankSelectedListener = bankSelectedListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.bank_face,parent,false);
        }
        LinearLayoutCompat bankFace = convertView.findViewById(R.id.bank_face_parent);
        bankFace.setOnClickListener(view -> bankSelectedListener.bankSelected(getItem(position)));
        AppCompatImageView logo = convertView.findViewById(R.id.bank_logo);
        logo.setImageResource(getItem(position).getLogo());
        TextView name = convertView.findViewById(R.id.bank_name);
        name.setText(getItem(position).getName());
        return convertView;
    }
}
