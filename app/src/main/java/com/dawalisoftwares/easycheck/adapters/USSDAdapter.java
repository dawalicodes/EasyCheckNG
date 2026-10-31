package com.dawalisoftwares.easycheck.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.TooltipCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dawalisoftwares.easycheck.AD;
import com.dawalisoftwares.easycheck.INFO;
import com.dawalisoftwares.easycheck.Item;
import com.dawalisoftwares.easycheck.R;
import com.dawalisoftwares.easycheck.databinding.ItemMoreServiceBinding;
import com.dawalisoftwares.easycheck.interfaces.EasyReveal;
import com.dawalisoftwares.easycheck.interfaces.ItemClickListener;
import com.dawalisoftwares.easycheck.databinding.AdFaceBinding;
import com.dawalisoftwares.easycheck.databinding.ActionViewFaceBinding;

import java.util.ArrayList;
import java.util.List;

public class USSDAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public List<Object> items;
    private ItemClickListener itemClickListener;
    public USSDAdapter(ArrayList<Object> items) {
        this.items = items;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == INFO.VIEW_TYPE_AD) {
            AdFaceBinding binding = AdFaceBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new AdViewHolder(binding);
        } else if (viewType == INFO.VIEW_TYPE_USSD) {
            ItemMoreServiceBinding binding = ItemMoreServiceBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new UssdViewHolder(binding);
        } else {
            ActionViewFaceBinding binding = ActionViewFaceBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ActionViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(holder.getAdapterPosition()) == INFO.VIEW_TYPE_USSD)
        {
            UssdViewHolder ussdViewHolder = (UssdViewHolder) holder;
            Item item = (Item) items.get(ussdViewHolder.getAdapterPosition());
            TooltipCompat.setTooltipText(ussdViewHolder.binding.serviceHolder, item.getDescription());
            ussdViewHolder.binding.serviceTitle.setText(item.getName());
            ussdViewHolder.binding.serviceDescription.setText(item.getDescription());
            if (item.getName().equals(INFO.CHECK_NIN)) {
                ussdViewHolder.binding.serviceBadge.setVisibility(View.VISIBLE);
                ussdViewHolder.binding.serviceBadge.setText(R.string.charges_apply);
            }else {
                ussdViewHolder.binding.serviceBadge.setVisibility(View.GONE);
            }
            ussdViewHolder.binding.serviceHolder.setOnClickListener(view -> itemClickListener.onItemClickListener(ussdViewHolder.binding.serviceHolder, item.getName(),item.getDescription(),ussdViewHolder.getAdapterPosition()));


        }else if (getItemViewType(holder.getAdapterPosition()) == INFO.VIEW_TYPE_ACTION){
            ActionViewHolder actionViewHolder = (ActionViewHolder) holder;
            EasyReveal reveal = (EasyReveal) items.get(actionViewHolder.getAdapterPosition());
            actionViewHolder.binding.simCardCard.removeAllViews();
            reveal.addToView(actionViewHolder.binding.simCardCard);
        }else if (getItemViewType(holder.getAdapterPosition()) == INFO.VIEW_TYPE_AD) {
            AdViewHolder adViewHolder = (AdViewHolder) holder;
            adViewHolder.binding.adClose.setOnClickListener(view -> {
                int pos = adViewHolder.getAdapterPosition();
                items.remove(pos);
                notifyItemRemoved(pos);
            });
        }

    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof AD) {
            return INFO.VIEW_TYPE_AD;
        } else if (items.get(position) instanceof Item){
            return INFO.VIEW_TYPE_USSD;
        }else {
            return INFO.VIEW_TYPE_ACTION;
        }
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener)
    {
        this.itemClickListener = itemClickListener;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////
    public static class UssdViewHolder extends RecyclerView.ViewHolder {
        final ItemMoreServiceBinding binding;
        public UssdViewHolder(@NonNull ItemMoreServiceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public static class AdViewHolder extends RecyclerView.ViewHolder {
        final AdFaceBinding binding;
        public AdViewHolder(@NonNull AdFaceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public static class ActionViewHolder extends RecyclerView.ViewHolder {
        final ActionViewFaceBinding binding;
        public ActionViewHolder(@NonNull ActionViewFaceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////
}
