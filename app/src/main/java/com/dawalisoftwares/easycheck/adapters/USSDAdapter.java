package com.dawalisoftwares.easycheck.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.TooltipCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.dawalisoftwares.easycheck.AD;
import com.dawalisoftwares.easycheck.INFO;
import com.dawalisoftwares.easycheck.Item;
import com.dawalisoftwares.easycheck.R;
import com.dawalisoftwares.easycheck.interfaces.EasyReveal;
import com.dawalisoftwares.easycheck.interfaces.ItemClickListener;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.textview.MaterialTextView;

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
            return new AdViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_face, parent, false));
        }else if (viewType == INFO.VIEW_TYPE_USSD) {
            return new UssdViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ussd_face,parent,false));
        }
        else {
            View aView = LayoutInflater.from(parent.getContext()).inflate(R.layout.action_view_face,parent,false);
            return new ActionViewHolder(aView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == INFO.VIEW_TYPE_USSD)
        {
            UssdViewHolder ussdViewHolder = (UssdViewHolder) holder;
            Item item = (Item) items.get(ussdViewHolder.getAdapterPosition());
            TooltipCompat.setTooltipText(ussdViewHolder.constraintLayout,item.getDescription());
            ussdViewHolder.ussdName.setText(item.getName());
            ussdViewHolder.description.setText(item.getDescription());
            if (item.getName().equals(INFO.CHECK_NIN)) {
                ussdViewHolder.chip.setVisibility(View.VISIBLE);
                ussdViewHolder.chip.setText("Charges Apply");
            }else {
                ussdViewHolder.chip.setVisibility(View.GONE);
            }
            ussdViewHolder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClickListener(ussdViewHolder.cardView, item.getName(),item.getDescription(),ussdViewHolder.getAdapterPosition());
                }
            });


        }else if (getItemViewType(position) == INFO.VIEW_TYPE_ACTION){
            ActionViewHolder actionViewHolder = (ActionViewHolder) holder;
            EasyReveal reveal = (EasyReveal) items.get(actionViewHolder.getAdapterPosition());
            actionViewHolder.actionContainer.removeAllViews();
            reveal.addToView(actionViewHolder.actionContainer);
        }else if (getItemViewType(position) == INFO.VIEW_TYPE_AD) {
            AdViewHolder adViewHolder = (AdViewHolder) holder;
            adViewHolder.getAdClose().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    items.remove(adViewHolder.getAdapterPosition());
                    notifyItemRemoved(adViewHolder.getAdapterPosition());
                }
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
    public static class UssdViewHolder extends RecyclerView.ViewHolder{

        private final MaterialCardView cardView;
        private final TextView ussdName;
        private final MaterialTextView description;
        private final Chip chip;
        private final ConstraintLayout constraintLayout;

        public UssdViewHolder(@NonNull View itemView) {
            super(itemView);
            chip = itemView.findViewById(R.id.chip);
            description = itemView.findViewById(R.id.description);
            constraintLayout = itemView.findViewById(R.id.ussd_parent);
            ussdName = itemView.findViewById(R.id.ussd_name);
            cardView = itemView.findViewById(R.id.ussd_card);
        }

    }
    ////////////////////////////////////////////////////////////////////////////////////////////
}
