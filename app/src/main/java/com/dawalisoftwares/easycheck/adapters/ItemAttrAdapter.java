package com.dawalisoftwares.easycheck.adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dawalisoftwares.easycheck.R;

import java.util.List;

public class ItemAttrAdapter extends RecyclerView.Adapter<Model> {

    List<ItemAttr> itemAttrs;
    public ItemAttrAdapter(List<ItemAttr> itemAttrs) {

        this.itemAttrs = itemAttrs;
    }

    @NonNull
    @Override
    public Model onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attr_face,parent,false);
        return new Model(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Model holder, int position) {
        holder.viewFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = itemAttrs.get(holder.getAdapterPosition()).code;
                code = code.replaceAll("#",Uri.encode("#"));
               view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + code)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemAttrs.size();
    }
}

class Model extends RecyclerView.ViewHolder {

    View viewFace;
    public Model(@NonNull View itemView) {
        super(itemView);
        viewFace = itemView;
    }
}