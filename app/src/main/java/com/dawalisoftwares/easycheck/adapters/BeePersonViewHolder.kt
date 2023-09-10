package com.dawalisoftwares.easycheck.adapters

import androidx.recyclerview.widget.RecyclerView
import com.dawalisoftwares.easycheck.databinding.FaceBeePersonBinding
import com.dawalisoftwares.easycheck.db
import java.util.concurrent.Executors

class BeePersonViewHolder(var binding: FaceBeePersonBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindTo(item: BeePerson?) {
        binding.name.text = item?.name
        binding.phoneNumber.text = item?.phone?.joinToString(",") ?: ""
        binding.delete.setOnClickListener {
            Executors.newSingleThreadExecutor().submit {
                if (item != null) {
                    db.getDataBase(binding.root.context)?.beePersonDao()?.deleteBeePerson(item)
                }
            }
        }
    }
}