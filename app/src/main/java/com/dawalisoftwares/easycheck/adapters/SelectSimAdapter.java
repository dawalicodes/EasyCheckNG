package com.dawalisoftwares.easycheck.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dawalisoftwares.easycheck.Airtel;
import com.dawalisoftwares.easycheck.GLO;
import com.dawalisoftwares.easycheck.MTN;
import com.dawalisoftwares.easycheck.Mobile;
import com.dawalisoftwares.easycheck.interfaces.Network;
import com.dawalisoftwares.easycheck.R;
import com.dawalisoftwares.easycheck.Sim;

import java.util.List;

public class SelectSimAdapter extends ArrayAdapter<Sim> {
    List<Sim> list;
    public SelectSimAdapter(@NonNull Context context, int resource, @NonNull List<Sim> objects) {
        super(context, resource, objects);
        this.list = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.select_sim_design,parent,false);
        TextView textView = view.findViewById(R.id.sim_display_name);
        ImageView imageView = view.findViewById(R.id.sim_card_image);
        textView.setText(list.get(position).getName());
        Network network = list.get(position).getNetwork();
        if (network instanceof Airtel)
        {
            imageView.setImageResource(R.drawable.airtel);
        }
        else if (network instanceof GLO)
        {
            imageView.setImageResource(R.drawable.glo);

        }else if (network instanceof Mobile)
        {
            imageView.setImageResource(R.drawable.mobile);

        }else if (network instanceof MTN)
        {
            imageView.setImageResource(R.drawable.mtn);
        }
        return view;
    }
}
