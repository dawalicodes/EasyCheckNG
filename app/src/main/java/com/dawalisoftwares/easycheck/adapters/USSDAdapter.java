package com.dawalisoftwares.easycheck.adapters;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.TooltipCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dawalisoftwares.easycheck.AD;
import com.dawalisoftwares.easycheck.INFO;
import com.dawalisoftwares.easycheck.R;
import com.dawalisoftwares.easycheck.database.ItemEntity;
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
        if (getItemViewType(holder.getBindingAdapterPosition()) == INFO.VIEW_TYPE_USSD)
        {
            UssdViewHolder ussdViewHolder = (UssdViewHolder) holder;
            ItemEntity item = (ItemEntity) items.get(ussdViewHolder.getBindingAdapterPosition());
            TooltipCompat.setTooltipText(ussdViewHolder.binding.serviceHolder, item.getDescription());
            ussdViewHolder.binding.serviceTitle.setText(item.getName());
            ussdViewHolder.binding.serviceDescription.setText(item.getDescription());
            if (item.getIcon() != 0) {
                ussdViewHolder.binding.serviceIcon.setImageResource(item.getIcon());
            }
            if (!item.getIconBackgroundColor().isEmpty()) {
                ussdViewHolder.binding.serviceIconHolder.setCardBackgroundColor(Color.parseColor(item.getIconBackgroundColor()));
            }
            if (!item.getIconColor().isEmpty()) {
                ussdViewHolder.binding.serviceIcon.setImageTintList(ColorStateList.valueOf(Color.parseColor(item.getIconColor())));
            }
            if (item.getName().equals(INFO.CHECK_NIN)) {
                ussdViewHolder.binding.serviceBadge.setVisibility(View.VISIBLE);
                ussdViewHolder.binding.serviceBadge.setText(R.string.charges_apply);
            }else {
                ussdViewHolder.binding.serviceBadge.setVisibility(View.GONE);
            }
            ussdViewHolder.binding.serviceHolder.setOnClickListener(view -> itemClickListener.onItemClickListener(ussdViewHolder.binding.serviceHolder, item.getName(),item.getDescription(),ussdViewHolder.getBindingAdapterPosition()));


        }else if (getItemViewType(holder.getBindingAdapterPosition()) == INFO.VIEW_TYPE_ACTION){
            ActionViewHolder actionViewHolder = (ActionViewHolder) holder;
            EasyReveal reveal = (EasyReveal) items.get(actionViewHolder.getBindingAdapterPosition());
            actionViewHolder.binding.simCardCard.removeAllViews();
            reveal.addToView(actionViewHolder.binding.simCardCard);
        }else if (getItemViewType(holder.getBindingAdapterPosition()) == INFO.VIEW_TYPE_AD) {
            AdViewHolder adViewHolder = (AdViewHolder) holder;
            adViewHolder.binding.adClose.setOnClickListener(view -> {
                int pos = adViewHolder.getBindingAdapterPosition();
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
        } else if (items.get(position) instanceof ItemEntity){
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
