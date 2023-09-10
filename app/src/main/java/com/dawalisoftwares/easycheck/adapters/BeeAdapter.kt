package com.dawalisoftwares.easycheck.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.dawalisoftwares.easycheck.databinding.FaceBeePersonBinding

class BeeAdapter :
    ListAdapter<BeePerson, BeePersonViewHolder>(object : DiffUtil.ItemCallback<BeePerson>() {
        override fun areItemsTheSame(oldItem: BeePerson, newItem: BeePerson): Boolean {
            return oldItem.key == newItem.key
        }

        override fun areContentsTheSame(oldItem: BeePerson, newItem: BeePerson): Boolean {
            return oldItem.phone.contentEquals(newItem.phone)
        }
    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeePersonViewHolder {
        val binding = FaceBeePersonBinding.inflate(LayoutInflater.from(parent.context))
        return BeePersonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BeePersonViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }


}